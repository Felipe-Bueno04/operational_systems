package com.aps;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GerenciadorDisco {
  private static class Bloco {
    String processoId = null;

    @Override
    public String toString() {
      return this.processoId;
    }
  }

  private final List<Bloco> blocos;
  private final int totalMb;

  public GerenciadorDisco(int totalMb) {
    this.totalMb = totalMb;
    this.blocos = new ArrayList<>();
    for (int i = 0; i < totalMb; i++) {
      this.blocos.add(new Bloco());
    }
  }

  public synchronized boolean alocar(String processoId, int tamanhoMb) {
    int livres = (int) blocos.stream().filter(b -> b.processoId == null).count();
    if (livres < tamanhoMb)
      return false;
    int alocados = 0;
    for (Bloco b : blocos) {
      if (b.processoId == null && alocados < tamanhoMb) {
        b.processoId = processoId;
        alocados = alocados + 1;
      }
    }
    return true;
  }

  public synchronized void liberar(String processoId) {
    for (Bloco b : blocos) {
      if (processoId.equals(b.processoId))
        b.processoId = null;
    }
  }

  public synchronized int getTamanhoFragmentado() {
    Map<String, List<Integer>> mapa = new HashMap<>();
    for (int i = 0; i < blocos.size(); i++) {
      Bloco b = blocos.get(i);
      if (b.processoId != null)
        mapa.computeIfAbsent(b.processoId, k -> new ArrayList<>()).add(i);
    }
    int total = 0;
    for (List<Integer> pos : mapa.values()) {
      if (ehFragmentado(pos))
        total += pos.size();
    }
    return total;
  }

  private boolean ehFragmentado(List<Integer> posicoes) {
    Collections.sort(posicoes);
    for (int i = 1; i < posicoes.size(); i++) {
      if (posicoes.get(i) != posicoes.get(i - 1) + 1)
        return true;
    }
    return false;
  }

  public synchronized double getTaxaFragmentacao() {
    int usados = (int) blocos.stream().filter(b -> b.processoId != null).count();
    if (usados == 0)
      return 0;
    return (double) getTamanhoFragmentado() / usados;
  }

  public synchronized void desfragmentar(int mb) {
    List<Bloco> usados = new ArrayList<>();
    for (Bloco b : blocos)
      if (b.processoId != null)
        usados.add(b);
    List<Bloco> novos = new ArrayList<>();
    int count = 0;
    for (Bloco b : usados) {
      if (count < mb) {
        Bloco novo = new Bloco();
        novo.processoId = b.processoId;
        novos.add(novo);
        count++;
      }
    }
    while (novos.size() < totalMb)
      novos.add(new Bloco());
    for (int i = 0; i < totalMb; i++)
      blocos.set(i, novos.get(i));
  }

  public int getTotalMb() {
    return totalMb;
  }

  public synchronized String getIdDoBloco(int index) {
    return blocos.get(index).processoId;
  }
}
