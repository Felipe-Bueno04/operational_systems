package com.aps;

import java.util.List;
import java.util.Random;

public class InstanciadorProcessos {
  private static final Random rand = new Random();
  private static int contador = 0;

  private InstanciadorProcessos() {
  }

  public static Processo criar(List<Conta> contas, GerenciadorRecursos gerenciador) {
    Conta origem = contas.get(rand.nextInt(contas.size()));
    Conta destino;
    do {
      destino = contas.get(rand.nextInt(contas.size()));
    } while (destino == origem);

    int tamanhoMb = rand.nextInt(5) + 5;
    int ramMb = rand.nextInt(5) + 5;
    int duracao = rand.nextInt(1000) + 2000;

    System.out.println(contador);
    return new Processo("P" + (++contador), origem, destino, tamanhoMb, ramMb, duracao, gerenciador);
  }
}
