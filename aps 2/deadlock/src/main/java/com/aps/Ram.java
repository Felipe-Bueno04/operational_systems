package main.java.com.aps;

public class Ram {
    private int capacidade;
    private int disponivel;
    
    public RAM(int capacidade) {
        this.capacidade = capacidade;
        this.disponivel = capacidade;
    }

    public synchronized boolean alocar(int quantidade) {
        if (quantidade > disponivel) return false;
        disponivel -= quantidade;
        return true;
    }

    public synchronized void desalocar(int quantidade) {
        disponivel += quantidade;
        if (disponivel > capacidade) disponivel = capacidade;
    }

    public void mostrarEstado() {
        System.out.println("RAM: " + disponivel + "/" + capacidade + "MB disponíveis");
    }
}
