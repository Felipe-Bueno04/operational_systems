public class Main {
    public static void main(String[] args) throws InterruptedException {
        Account a = new Account(1, 300);
        Account b = new Account(2, 300);
        Account c = new Account(3, 300);

        ResourceManager manager = new ResourceManager();

        Process p1 = new Process(1, a, b, 150, manager);
        Process p2 = new Process(2, b, c, 150, manager);
        // Process p3 = new Process(3, c, a, 150, manager); // Cria a dependencia circular

        Thread t1 = new Thread(p1);
        Thread t2 = new Thread(p2);
        // Thread t3 = new Thread(p3);

        t1.start();
        t2.start();
        // t3.start();

        t1.join();
        t2.join();
        // t3.join();

        System.out.println("All processes completed.");
    }
}
