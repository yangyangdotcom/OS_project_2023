import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class ParallelHttpRequests {

    public static void httpRequest(int requestCount) {
        Random random = new Random();
        int idNum = random.nextInt(100) + 1;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://dummyjson.com/todos/" + idNum))
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            // Uncomment the following line to print the response
            // System.out.println(response.body());
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        int numRequests = 1000;

        // Parallel execution using an ExecutorService
        long startTime = System.nanoTime();
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        List<Future<?>> futures = new ArrayList<>();

        for (int i = 0; i < numRequests; i++) {
            futures.add(executor.submit(() -> httpRequest(1000)));
        }

        futures.forEach(future -> {
            try {
                future.get();
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        });

        executor.shutdown();
        long endTime = System.nanoTime();
        double elapsedTime = (endTime - startTime) / 1_000_000_000.0;
        System.out.println("Program finished in " + elapsedTime + " seconds - using ExecutorService");
        System.out.println("---");

        // Sequential execution
        startTime = System.nanoTime();
        for (int i = 0; i < numRequests; i++) {
            httpRequest(1000);
        }
        endTime = System.nanoTime();
        elapsedTime = (endTime - startTime) / 1_000_000_000.0;
        System.out.println("Program finished in " + elapsedTime + " seconds");
        System.out.println("---");
    }
}
