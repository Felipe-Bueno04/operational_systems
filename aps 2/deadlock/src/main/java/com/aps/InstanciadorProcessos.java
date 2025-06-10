package main.java.com.aps;

import java.util.Random;

public class InstanciadorProcessos {
    private static Random rand = new Random();

    public static Processo criarProcesso(int id, GerenciadorRecursos gerenciador) {
        int custoRam = rand.nextInt(200) + 50;
        int tamanhoHd = rand.nextInt(20) + 5;
        int duracao = rand.nextInt(3000) + 1500;
        return new Processo(id, custoRam, tamanhoHd, duracao, gerenciador);
    }
}