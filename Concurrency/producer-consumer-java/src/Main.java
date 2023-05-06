import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

public class Main {
    private static final int producers = 1000;
    private static final int consumers = 1000;
    private static final int capacity = 10;

    public static void main(String[] args) throws InterruptedException {

        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        // Set the number of CPU cores used by the program to 1
        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "0");

        BlockingQueue<Integer> queue = new LinkedBlockingQueue<>(capacity);

        long startTime = System.nanoTime();
        // Launch producer threads
        Thread[] producerThreads = new Thread[producers];
        for (int i = 0; i < producers; i++) {
            final int id = i;
            producerThreads[i] = new Thread(() -> {
                try {
                    queue.put(id);
                    System.out.printf("Producer %d produced%n", id);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            producerThreads[i].start();
        }

        // Launch consumer threads
        Thread[] consumerThreads = new Thread[consumers];
        for (int i = 0; i < consumers; i++) {
            final int id = i;
            consumerThreads[i] = new Thread(() -> {
                try {
                    int value = queue.take();
                    System.out.printf("Consumer %d consumed value %d%n", id, value);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            consumerThreads[i].start();
        }

        // Wait for all producer threads to finish
        for (Thread producerThread : producerThreads) {
            producerThread.join();
        }

        // Wait for all consumer threads to finish
        for (Thread consumerThread : consumerThreads) {
            consumerThread.join();
        }
        long endTime = System.nanoTime();
        double elapsedTime = (endTime - startTime) / 1_000_000_000.0;
        System.out.println("Program finished in " + elapsedTime + " seconds");

        System.out.println("---");
        System.out.println("Done");

        // force garbage collection
        System.gc();

        // get the peak heap memory usage
        MemoryUsage peakMemoryUsage = memoryMXBean.getHeapMemoryUsage();
        System.out.printf("Peak heap memory usage: %.2f MB\n", (double)peakMemoryUsage.getUsed() / (1024 * 1024));
    }
}
