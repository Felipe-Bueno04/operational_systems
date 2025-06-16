package com.aps;

public class Processo extends NoGrafo implements Runnable {
  private final String id;
  private Conta origem;
  private Conta destino;
  private GerenciadorRecursos gerenciador;
  private int tamanhoMb;
  private int ramMb;
  private int duracaoMs;

  public Processo(String id, Conta origem, Conta destino, int tamanhoMb, int ramMb, int duracaoMs,
      GerenciadorRecursos gr) {
    this.id = id;
    this.origem = origem;
    this.destino = destino;
    this.tamanhoMb = tamanhoMb;
    this.ramMb = ramMb;
    this.duracaoMs = duracaoMs;
    this.gerenciador = gr;
  }

  @Override
  public void run() {
    try {
      if (!gerenciador.iniciarExecucao(this, origem, destino, tamanhoMb, ramMb)) {
        System.out.println("Não foi possível iniciar execução do processo " + this.getIdentificador());
      }

      origem.saldo -= 10;
      destino.saldo += 10;
      Thread.sleep(duracaoMs);

    } catch (InterruptedException e) {
      System.out.println("Processo " + id + " interrompido devido a deadlock!");
    } finally {
      gerenciador.finalizarExecucao(this, origem, destino, tamanhoMb, ramMb);
    }
  }

  public int getTamanhoMb() {
    return tamanhoMb;
  }

  public int getRamMb() {
    return ramMb;
  }

  public String getNome() {
    return id;
  }

  @Override
  public String getIdentificador() {
    return id;
  }
}