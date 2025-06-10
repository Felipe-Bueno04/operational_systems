package main.java.com.aps;

public class GerenciadorFragmentacao {
    private GerenciadorRecursos gerenciador;

    public GerenciadorFragmentacao(GerenciadorRecursos gerenciador) {
        this.gerenciador = gerenciador;
    }

    public void verificarEExecutar() {
        double fragmentacao = gerenciador.getDisco().calcularFragmentacao();
        System.out.println("Fragmentação atual: %.2f%%\n", fragmentacao);
        if (fragmentacao > 10.0) {
            System.out.println("Iniciando desfragmentação...");
            Processo desfragmentador = new Proceso(999, 100, 0, 300, gerenciador);
            new Thread(desfragmentador).start();
        } else {
            System.out.println("Fragmentação dentro do limite aceitável.");
        }
    }
}
