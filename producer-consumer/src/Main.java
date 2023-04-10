import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Main {
    private static final int producers = 1000;
    private static final int consumers = 1000;
    private static final int capacity = 10;

    public static void main(String[] args) throws InterruptedException {
        // Set the number of CPU cores used by the program to 1
        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "0");

        BlockingQueue<Integer> queue = new LinkedBlockingQueue<>(capacity);

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

        System.out.println("Done");
    }
}
