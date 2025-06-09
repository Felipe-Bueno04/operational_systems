package com.aps;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class GerenciadorRecursos {
  private final Map<NoGrafo, Set<NoGrafo>> grafo = new ConcurrentHashMap<>();
  private final Map<Processo, Thread> mapaProcessos = new ConcurrentHashMap<>();

  public void registrarProcesso(Processo p, Thread t) {
    mapaProcessos.put(p, t);
  }

  public synchronized void solicitarRecurso(Processo p, Conta c) {
    grafo.putIfAbsent(p, new HashSet<>());
    grafo.get(p).add(c); // Aresta: P -> C (esperando C)
  }

  public synchronized void adquirirRecurso(Processo p, Conta c) {
    grafo.get(p).remove(c); // Remove espera
    grafo.putIfAbsent(c, new HashSet<>());
    grafo.get(c).add(p); // Aresta: C -> P (C está com P)
  }

  public synchronized void liberarRecurso(Processo p, Conta c) {
    grafo.getOrDefault(p, Collections.emptySet()).remove(c);
    grafo.getOrDefault(c, Collections.emptySet()).remove(p);
  }

  public void verificarDeadlockEmLoop(long intervaloMs) throws InterruptedException {
    while (!Thread.currentThread().isInterrupted()) {
      Thread.sleep(intervaloMs);
      Processo p = verificarDeadlock();
      if (p != null) {
        System.out.println("Deadlock detectado! Interrompendo processo " + p.getId());
        this.desenharGrafo();

        this.interromperProcessosEnvolvidos(p);
      }
    }
  }


  private void interromperProcessosEnvolvidos(NoGrafo no) {
    Set<NoGrafo> nosEnvolvidos = this.trazerNosVizinhos(no, new HashSet<>());

    this.interromperVariosProcessos(
      nosEnvolvidos
    );
  }

  private Set<NoGrafo> trazerNosVizinhos(NoGrafo no, Set<NoGrafo> visitados) {
    visitados.add(no);   
    for (NoGrafo vizinho : grafo.getOrDefault(no, Collections.emptySet())) {
      if (visitados.contains(vizinho)) continue;
      this.trazerNosVizinhos(vizinho, visitados);
    }

    return visitados;
  }


  private void interromperVariosProcessos(Set<NoGrafo> nos) {
    for (NoGrafo no : nos) {
      if (no instanceof Processo) {
        Thread t = mapaProcessos.get(no);
        if (t != null)
          t.interrupt();
        this.grafo.remove(no);
      }
    }
  }

  private synchronized Processo verificarDeadlock() {
    Set<String> visitados = new HashSet<>();
    Set<String> emPilha = new HashSet<>();

    for (NoGrafo no : grafo.keySet()) {
      if (detectarCiclo(no, visitados, emPilha)) {
        if (no instanceof Processo)
          return (Processo) no;
      }
    }
    return null;
  }

  private boolean detectarCiclo(NoGrafo no, Set<String> visitados, Set<String> emPilha) {
    String id = no.getIdentificador();
    if (emPilha.contains(id))
      return true;
    if (visitados.contains(id))
      return false;

    visitados.add(id);
    emPilha.add(id);
    for (NoGrafo vizinho : grafo.getOrDefault(no, Collections.emptySet())) {
      if (detectarCiclo(vizinho, visitados, emPilha))
        return true;
    }
    emPilha.remove(id);
    return false;
  }

  public void desenharGrafo() {
    System.out.println("O grafo do deadlock foi salvo em /visualizacao-deadlock/grafo.png");
    try {
      GraphPrinter.printGraphClustered(this.grafo);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
