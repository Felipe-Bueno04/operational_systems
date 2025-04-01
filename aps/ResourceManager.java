import java.util.*;

public class ResourceManager {
  private final Map<Process, List<Account>> processMap = new HashMap<>();

  // Método para registrar processo.
  public synchronized void registerProcess(Process process, Account from, Account to) {
    processMap.put(process, Arrays.asList(from, to));
    checkForDeadlock();
  }

  // Método para remover processo.
  public synchronized void removeProcess(Process process) {
    processMap.remove(process);
  }

  // Detecta os deadlocks
  private synchronized void checkForDeadlock() {
    Set<Process> visited = new HashSet<>();
    Set<Process> stack = new HashSet<>();

    for (Process process : processMap.keySet()) {
      if (detectCycle(process, visited, stack)) {
        System.out.println("Deadlock detected! Terminating process " + process.getProcessId());
        terminateProcess(process);
        break;
      }
    }
  }

  /**
   * É um método recursivo que tenta detectar alguma
   * dependência ciclica no grafo de processos atual.
   *
   * @param process Processo da iteração atual.
   * @param visited Processos já visitados nas iterações anteriores.
   * @param stack Pilha de processos.
   * @return Se há ou não uma dependência circular.
   */
  // Detecta se tem dependencia ciclica
  private boolean detectCycle(Process process, Set<Process> visited, Set<Process> stack) {
    if (stack.contains(process)) {
      return true;
    }
    if (visited.contains(process)) {
      return false;
    }

    visited.add(process);
    stack.add(process);

    for (Process p : processMap.keySet()) {
      if (!p.equals(process) && !Collections.disjoint(processMap.get(process), processMap.get(p))) {
        if (detectCycle(p, visited, stack)) {
          return true;
        }
      }
    }

    stack.remove(process);
    return false;
  }

  /**
   * Mata o processo.
   * @param process Refer"encia do processo
   */
  private void terminateProcess(Process process) {
    processMap.remove(process);
    Thread.currentThread().interrupt();
  }
}
