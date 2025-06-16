package com.aps;

import java.util.*;

public class ConsoleView {
  private final int largura = 60;
  private final int alturaMaxLog = 10;
  private final List<String> logProcessos = new LinkedList<>();
  private String estadoDisco = "";
  private int blocosPorLinha = 30; // quantidade de blocos por linha visual

  public synchronized void adicionarLog(String log) {
    if (logProcessos.size() >= alturaMaxLog)
      logProcessos.remove(0);
    logProcessos.add(log);
  }

  public synchronized void atualizarDisco(String visual, int blocosPorLinha) {
    this.estadoDisco = visual;
    this.blocosPorLinha = blocosPorLinha;
  }

  public synchronized void renderizar() {
    System.out.print("\033[H\033[2J");
    System.out.flush();

    // Seção do disco
    System.out.println("╔" + "═".repeat(largura) + "╗");
    System.out.println("║               ESTADO DO DISCO               ║");
    System.out.println("╠" + "═".repeat(largura) + "╣");

    List<String> linhas = dividirDiscoEmLinhas(estadoDisco, blocosPorLinha);
    for (String linha : linhas) {
      System.out.println("║ " + pad(linha, largura - 2) + "║");
    }

    System.out.println("╚" + "═".repeat(largura) + "╝");
    // Seção de processos
    System.out.println("╔" + "═".repeat(largura) + "╗");
    System.out.println("║        PROCESSOS ATIVOS / CONCLUÍDOS        ║");
    System.out.println("╠" + "═".repeat(largura) + "╣");
    for (String linha : logProcessos) {
      System.out.println("║ " + pad(linha, largura - 2) + "║");
    }
    for (int i = logProcessos.size(); i < alturaMaxLog; i++) {
      System.out.println("║ " + pad("", largura - 2) + "║");
    }
    System.out.println("╚" + "═".repeat(largura) + "╝");
  }

  private List<String> dividirDiscoEmLinhas(String visual, int blocosPorLinha) {
    List<String> linhas = new ArrayList<>();
    int blocoTamanho = 5; // ex: "[P1]" tem 4 caracteres

    for (int i = 0; i < visual.length(); i += blocosPorLinha * blocoTamanho) {
      int end = Math.min(i + blocosPorLinha * blocoTamanho, visual.length());
      linhas.add(visual.substring(i, end));
    }
    return linhas;
  }

  private String pad(String s, int len) {
    return String.format("%-" + len + "s", s);
  }
}
