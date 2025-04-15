package com.aps;

public class App {
    public static void main(String[] args) throws InterruptedException {
        Conta a = new Conta(1, 300);
        Conta b = new Conta(2, 300);
        Conta c = new Conta(3, 300);

        GerenciadorRecursos gerenciador = new GerenciadorRecursos();

        Processo p1 = new Processo(1, a, b, 150, gerenciador);
        Processo p2 = new Processo(2, b, c, 150, gerenciador);
        Processo p3 = new Processo(3, c, a, 150, gerenciador); // cria deadlock circular

        Thread t1 = new Thread(p1);
        Thread t2 = new Thread(p2);
        Thread t3 = new Thread(p3);

        gerenciador.registrarProcesso(p1, t1);
        gerenciador.registrarProcesso(p2, t2);
        gerenciador.registrarProcesso(p3, t3);

        Thread monitor = new Thread(() -> {
            try {
                gerenciador.verificarDeadlockEmLoop(1000);
            } catch (InterruptedException e) {
                System.out.println("Monitor encerrado.");
            }
        });

        monitor.start();
        t1.start();
        t2.start();
        t3.start();

        t1.join();
        t2.join();
        t3.join();

        monitor.interrupt();
        monitor.join();

        System.out.println("Todos os processos foram finalizados.");
    }
}
