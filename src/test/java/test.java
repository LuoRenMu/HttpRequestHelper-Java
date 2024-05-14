import java.io.IOException;
import java.util.Arrays;

/**
 * @author LoMu
 * Date 2024.01.04 15:49
 */
public class test {
    public static void main(String[] args) throws Exception {
        Thread thread = Thread.currentThread();
        Thread thread1 = new Thread(() -> {
            try {
                test1();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        new Thread(() -> {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(Arrays.hashCode(thread1.getStackTrace()));
            System.out.println(Arrays.hashCode(thread.getStackTrace()));
        }).start();
        thread1.start();
        test1();



    }

    public static void test1() throws IOException {
        test2();
    }

    public static void test2() throws IOException {
        System.in.read();
    }
}
