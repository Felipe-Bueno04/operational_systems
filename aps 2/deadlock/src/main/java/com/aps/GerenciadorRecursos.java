package com.aps;

import main.java.com.aps.Disco;

public class GerenciadorRecursos {
    private RAM ram;
    private Disco disco;

    public GerenciadorRecursos(RAM ram, Disco disco) {
        this.ram = ram;
        this.disco = disco;
    }

    public synchronized boolean alocarRecursos(int idProcesso, int custo, int tamanhoHd) {
        if (!ram.alocar(custo)) return false;
        if (!disco.alocar(idProcesso, tamanhoHd)) {
            ram.desalocar(custo);
            return false;
        }
        return true;
    }

    public synchronized void liberarRecursos(int idProcesso, int custo) {
        disco.desalocar(idProcesso);
        ram.desalocar(custo);
    }

    public Disco getDisco() { return disco; }
    public RAM getRam() { return ram; }
}
