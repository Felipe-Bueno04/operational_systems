package main.java.com.aps;

import java.util.ArrayList;
import java.util.List;

public class Disco {
    private List<Integer> memoriaHD;

    public Disco(int tamanho) {
        memoriaHD = new ArrayList<>();
        for (int i = 0; i < tamanho; i++) memoriaHD.add(0); // 0 é bloco livre
    }

    public synchronized boolean alocar(int idProcesso, int tamanhoMb) {
        int livres = 0;
        for (int i = 0; i < memoriaHD.size(); i++) {
            if (memoriaHD.get(i) == 0) livres++;
        }
        if (livres < tamanho) return false;

        int alocados = 0;
        for (int i = 0; i < memoriaHD.size() && alocados < tamanhoMb; i++) {
            if (memoriaHD.get(i) == 0) {
                memoriaHD.set(i, idProcesso);
                alocados++;
            }
        }
        return true;
    }

    public synchronized void desalocar(int idProcesso) {
        for (int i = 0; i < memoriaHD.size(); i++) {
            if (memoriaHD.get(i) == idProcesso) memoriaHD.set(i, 0);
        }
    }

    public synchronized double calcularFragmentacao() {
        int fragmentos = 0;
        Integer atual = null;
        for (Integer bloco : memoriaHD) {
            if (bloco != 0 && !bloco.equals(atual)) fragmentos++;
        }
        return (fragmentos * 100.0) / memoriaHD.size();
    }

    public void mostrarDico() {
        System.out.print("Disco: ");
        for (Integer bloco : memoriaHD) {
            System.out.print(bloco == 0 ? "-" : bloco);
        }
        System.out.println();
    }
}
