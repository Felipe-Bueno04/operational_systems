package com.aps;

import java.util.concurrent.locks.ReentrantLock;

public class Conta extends NoGrafo {
    public int id;
    public double saldo;
    public final ReentrantLock lock = new ReentrantLock();

    public Conta(int id, double saldo) {
        this.id = id;
        this.saldo = saldo;
    }

    public void sacar(double valor) {
        saldo -= valor;
    }

    public void depositar(double valor) {
        saldo += valor;
    }

    @Override
    public String getIdentificador() {
        return "C" + id;
    }
}
