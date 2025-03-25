import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Account {
    public int id;

    public double balance;

    public Lock lock = new ReentrantLock(true);


    // helper methods

    public Account(int id, double balance) {
        this.id = id;
        this.balance = balance;
    }

    void withdraw(double amount){
        balance -= amount;
     } 
   
     void deposit(double amount){
        balance += amount;
     } 
   
    public static void transfer(Account from, Account to, double amount) throws InterruptedException{
        System.out.println("Thread: " + Thread.currentThread().getName());
        System.out.println(Thread.currentThread().getName() + " - Acessando recurso Account: " + from.id);
        from.lock.lock();
        System.out.println(Thread.currentThread().getName() + " - Sucesso!");
        System.out.println(Thread.currentThread().getName() + " - Acessando recurso Account: " + to.id);
        to.lock.lock();
        System.out.println(Thread.currentThread().getName() + " - Sucesso!");

        System.out.println(Thread.currentThread().getName() + " - Realizando transferência...");
        from.withdraw(amount);
        to.deposit(amount);
        Thread.sleep(1000);
        System.out.println(Thread.currentThread().getName() + " - Transferência realizada!");

        from.lock.unlock();
        to.lock.unlock();
    }
}
