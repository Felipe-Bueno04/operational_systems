public class Main {

    public static void main(String[] args) throws InterruptedException {
        final Account a = new Account(1, 300);
        final Account b = new Account(2, 300);
        final Account c = new Account(3, 300);

        Thread T1 = new Thread(() -> {
            try {
                Account.transfer(a, b, 150);
                
                System.out.println("T1 - A: " + a.balance);
                System.out.println("T1 - B: " + b.balance);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "T1");
        
        Thread T2 = new Thread(() -> {
            try {
                Account.transfer(b, c, 150);

                System.out.println("T2 - A: " + a.balance);
                System.out.println("T2 - B: " + b.balance);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "T2");
        
        Thread T3 = new Thread(() -> {
            try {
                Account.transfer(c, a, 150);

                System.out.println("T3 - A: " + a.balance);
                System.out.println("T3 - B: " + b.balance);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "T3");

        T1.start();
        T2.start();
        T3.start();
        T1.join();
        T2.join();
        T3.join();
    }    
}