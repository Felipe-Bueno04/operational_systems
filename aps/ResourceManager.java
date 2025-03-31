import java.util.*;

public class ResourceManager {
  private final Map<Process, List<Account>> processMap = new HashMap<>();

  // Register process dependencies
  public synchronized void registerProcess(Process process, Account from, Account to) {
    processMap.put(process, Arrays.asList(from, to));
    checkForDeadlock();
  }

  // Remove a process after it completes
  public synchronized void removeProcess(Process process) {
    processMap.remove(process);
  }

  // Detect and resolve deadlocks
  private synchronized void checkForDeadlock() {
    Set<Process> visited = new HashSet<>();
    Set<Process> stack = new HashSet<>();

    for (Process process : processMap.keySet()) {
      if (detectCycle(process, visited, stack)) {
        System.out.println("Deadlock detected! Terminating process " + process.getProcessId());
        terminateProcess(process);
        break; // Only kill one process per cycle detection
      }
    }
  }

  private boolean detectCycle(Process process, Set<Process> visited, Set<Process> stack) {
    if (stack.contains(process)) {
      return true; // Cycle detected
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

  private void terminateProcess(Process process) {
    processMap.remove(process);
    Thread.currentThread().interrupt(); // Simulate killing process
  }
}
