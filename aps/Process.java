import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class Process implements Runnable {
  private int processId;
  private Account from;
  private Account to;
  private double amount;
  private ResourceManager manager;

  public Process(int id, Account from, Account to, double amount, ResourceManager manager) {
    this.processId = id;
    this.from = from;
    this.to = to;
    this.amount = amount;
    this.manager = manager;
  }

  @Override
  public void run() {
    try {
      manager.registerProcess(this, from, to);

      System.out.println("Processo " + processId + " tentando transferir de " + from.id + " para " + to.id);

      if (!from.lock.tryLock(2, TimeUnit.SECONDS)) {
        System.out.println("Processo " + processId + " - Não conseguiu acessar a conta " + from.id + "(já em uso)");
        return;
      }

      Thread.sleep(100);
      if (!to.lock.tryLock(2, TimeUnit.SECONDS)) {
        System.out.println("Processo " + processId + " - Não conseguiu acessar a conta " + to.id + "(já em uso)");
        from.lock.unlock(); // Se não conseguir acessar a segunda conta então libera o lock da primeira e retorna.
        return;
      }

      // Exeuta a transmissão
      from.withdraw(amount);
      to.deposit(amount);
      System.out.println("Processo " + processId + " Transferência completa!");

    } catch (InterruptedException e) {
      System.out.println("Processo " + processId + " fechou um deadlock!");
    } finally {
      if (from.lock instanceof ReentrantLock && ((ReentrantLock) from.lock).isHeldByCurrentThread()) {
        from.lock.unlock();
      }
      if (to.lock instanceof ReentrantLock && ((ReentrantLock) to.lock).isHeldByCurrentThread()) {
        to.lock.unlock();
      }
      manager.removeProcess(this);
    }
  }

  public int getProcessId() {
    return processId;
  }
}
