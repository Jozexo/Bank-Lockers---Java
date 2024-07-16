/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.banklockers;

/**
 *
 * @author imad
 */
public class PilaCliente {
    private Nodo<Transaccion> cima;
    private int tamaño;

    
    static class Transaccion {
    String cedula;
    String nombre;
    String tipoTransaccion;
    double monto;

    public Transaccion(String cedula, String nombre, String tipoTransaccion, double monto) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.tipoTransaccion = tipoTransaccion;
        this.monto = monto;
    }

    @Override
    public String toString() {
        return cedula + "," + nombre + "," + tipoTransaccion + "," + monto;
    }
}

    private static class Nodo<Transaccion> {
        private Transaccion dato;
        private Nodo<Transaccion> siguiente;

        Nodo(Transaccion transaccion) {
            this.dato = transaccion;
        }
    }

    public PilaCliente() {
        cima = null;
        tamaño = 0;
    }

    public void apilar(Transaccion transaccion) {
        Nodo<Transaccion> nuevoNodo = new Nodo<>(transaccion);
        nuevoNodo.siguiente = cima;
        cima = nuevoNodo;
        tamaño++;
    }

    public Transaccion desapilar() {
        if (estaVacia()) {
            throw new RuntimeException("La pila está vacía");
        }
        Transaccion elemento = cima.dato;
        cima = cima.siguiente;
        tamaño--;
        return elemento;
    }

    public Transaccion cima() {
        if (estaVacia()) {
            throw new RuntimeException("La pila está vacía");
        }
        return cima.dato;
    }

    public boolean estaVacia() {
        return cima == null;
    }

    public int obtenerTamaño() {
        return tamaño;
    }
}
