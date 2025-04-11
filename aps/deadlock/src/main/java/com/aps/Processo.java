package com.aps;

public class Processo extends NoGrafo implements Runnable {
  private int id;
  private Conta origem;
  private Conta destino;
  private double valor;
  private GerenciadorRecursos gerenciador;

  public Processo(int id, Conta origem, Conta destino, double valor, GerenciadorRecursos gerenciador) {
    this.id = id;
    this.origem = origem;
    this.destino = destino;
    this.valor = valor;
    this.gerenciador = gerenciador;
  }

  @Override
  public void run() {
    try {
      System.out.println("Processo " + id + " solicita recurso " + origem.id);
      gerenciador.solicitarRecurso(this, origem);
      
      Thread.sleep(2000);
      origem.lock.lockInterruptibly();

      System.out.println("Processo " + id + " adquire recurso " + origem.id);
      gerenciador.adquirirRecurso(this, origem);
      Thread.sleep(2000);

      System.out.println("Processo " + id + " solicita recurso " + destino.id);
      gerenciador.solicitarRecurso(this, destino);      
      Thread.sleep(2000);

      destino.lock.lockInterruptibly();
      System.out.println("Processo " + id + " adquire recurso " + destino.id);
      gerenciador.adquirirRecurso(this, destino);

      origem.sacar(valor);
      destino.depositar(valor);
      System.out.println("Processo " + id + " transferência concluída!");

    } catch (Exception e) {
      System.out.println("Processo " + id + " interrompido devido a `deadlock`!");
    } finally {
      if (origem.lock.isHeldByCurrentThread()) {
        origem.lock.unlock();
        gerenciador.liberarRecurso(this, origem);
      }
      if (destino.lock.isHeldByCurrentThread()) {
        destino.lock.unlock();
        gerenciador.liberarRecurso(this, destino);
      }
    }
  }

  @Override
  public String getIdentificador() {
    return "P" + id;
  }

  public int getId() {
    return id;
  }
}
