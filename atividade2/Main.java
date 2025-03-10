import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Main {
    private static final BlockingQueue<Long> pingQueue = new LinkedBlockingQueue<>();
    private static final BlockingQueue<Boolean> pongQueue = new LinkedBlockingQueue<>();

    public static void main(String[] args) {
        Thread pingThread = new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("Press ENTER to Ping...");
                scanner.nextLine(); // Wait for user input

                long startTime = System.nanoTime();
                pingQueue.offer(startTime); // Send ping timestamp

                try {
                    pongQueue.take(); // Wait for pong response
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            scanner.close();
        });

        Thread pongThread = new Thread(() -> {
            while (true) {
                try {
                    long startTime = pingQueue.take(); // Get ping time

                    // Simulate a small delay to make it measurable
                    Thread.sleep(2);

                    long endTime = System.nanoTime();
                    long durationMs = (endTime - startTime) / 1_000_000; // Convert nanoseconds to milliseconds

                    System.out.println("Thread A: Ping");
                    System.out.println("Took " + durationMs + " ms");
                    System.out.println("Thread B: Pong\n");

                    pongQueue.offer(true); // Send pong confirmation
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });

        pingThread.start();
        pongThread.start();
    }
}
