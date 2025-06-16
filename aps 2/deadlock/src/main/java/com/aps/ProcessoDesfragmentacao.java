package com.aps;

public class ProcessoDesfragmentacao extends NoGrafo implements Runnable {
  private final GerenciadorDisco disco;
  private final GerenciadorRAM ram;
  private final GerenciadorRecursos recursos;
  private final String id = "Desfragmentador";

  public ProcessoDesfragmentacao(GerenciadorDisco disco, GerenciadorRAM ram, GerenciadorRecursos recursos) {
    this.disco = disco;
    this.ram = ram;
    this.recursos = recursos;
  }

  @Override
  public void run() {
    int restante = disco.getTamanhoFragmentado();
    while (restante > 0) {
      int disponivel = ram.getDisponivel();
      int etapa = Math.min(restante, disponivel);

      if (ram.reservar(etapa)) {
        disco.desfragmentar(etapa);
        ram.liberar(etapa);
        restante -= etapa;
        try {
          Thread.sleep(1000);
        } catch (InterruptedException ignored) {
        }
      } else {
        try {
          Thread.sleep(500);
        } catch (InterruptedException ignored) {
        }
      }
    }
  }

  @Override
  public String getIdentificador() {
    return id;
  }
}