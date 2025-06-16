package com.aps;

public class GerenciadorRAM {
  private final int totalMb;
  private int usadoMb;

  public GerenciadorRAM(int totalMb) {
      this.totalMb = totalMb;
      this.usadoMb = 0;
  }

  public synchronized boolean reservar(int mb) {
      if (usadoMb + mb > totalMb) return false;
      usadoMb += mb;
      return true;
  }

  public synchronized void liberar(int mb) {
      usadoMb = Math.max(usadoMb - mb, 0);
  }

  public synchronized int getDisponivel() {
      return totalMb - usadoMb;
  }
}