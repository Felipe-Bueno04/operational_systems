package com.aps;

public class Processo implements Runnable {
  private int id;
  private int custoRam;
  private int tamanhoHd;
  private int duracao;
  private GerenciadorRecursos gerenciador;

  public Processo(int id, int custoRam, int tamanhoHd, int duracao, GerenciadorRecursos gerenciador) {
    this.id = id;
    this.custoRam = custoRam;
    this.tamanhoHd = tamanhoHd;
    this.duracao = duracao;
    this.gerenciador = gerenciador;
  }

  @Override
  public void run() {
    if (gerenciador.alocarRecursos(id, custoRam, tamanhoHd)) {
      System.out.println("Processo " + id + " iniciado.");
      try { Thread.sleep(duracao); } catch (InterruptedException e) {}
      gerenciador.liberarRecurso(id, custoRam);
      System.out.println("Processo " + id + " finalizado.");
    } else {
      System.out.println("Processo " + id + "falhou na alocação de recursos.");
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
