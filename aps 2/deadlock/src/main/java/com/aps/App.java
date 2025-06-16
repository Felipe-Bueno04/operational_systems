package com.aps;

import java.util.List;

public class App {
    public static void main(String[] args) throws InterruptedException {
        int tempoExecucao = 30000;
        int discoTamanho = 200;
        int ramTamanho = 2000;

        List<Conta> contas = List.of(
                new Conta(1, 1000),
                new Conta(2, 1000),
                new Conta(3, 1000),
                new Conta(4, 1000),
                new Conta(5, 1000),
                new Conta(6, 1000),
                new Conta(7, 1000),
                new Conta(8, 1000),
                new Conta(9, 1000),
                new Conta(10, 1000));

        GerenciadorDisco disco = new GerenciadorDisco(discoTamanho);
        GerenciadorRAM ram = new GerenciadorRAM(ramTamanho);
        GerenciadorRecursos gr = new GerenciadorRecursos(disco, ram);
        ConsoleView console = new ConsoleView();

        long fim = System.currentTimeMillis() + tempoExecucao;
        while (System.currentTimeMillis() < fim) {
            Processo p = InstanciadorProcessos.criar(contas, gr);
            gr.adicionarNo(p);
            new Thread(p).start();

            console.adicionarLog(
                    p.getNome() + " - RAM: " + p.getRamMb() + "MB | HD: " + p.getTamanhoMb() + "MB | Executando...");

            if (disco.getTaxaFragmentacao() > 0.10 && ram.getDisponivel() >= 500) {
                ProcessoDesfragmentacao desfrag = new ProcessoDesfragmentacao(disco, ram, gr);
                new Thread(desfrag).start();
                console.adicionarLog("[SISTEMA] Iniciando desfragmentação...");
            }

            StringBuilder visualDisco = new StringBuilder();
            for (int i = 0; i < disco.getTotalMb(); i++) {
                String id = disco.getIdDoBloco(i);
                visualDisco
                        .append(String.format("[%s]", id == null ? "  " : id));
            }
            console.atualizarDisco(visualDisco.toString(), 30);
            console.renderizar();

            Thread.sleep(1000);
        }

        System.out.println("Simulação encerrada.");
    }
}
