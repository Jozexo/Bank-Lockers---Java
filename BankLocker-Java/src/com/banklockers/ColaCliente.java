/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.banklockers;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author imad
 */
public class ColaCliente {
    private Nodo<Cliente> frente;
    private Nodo<Cliente> finalCola;
    private int tamaño;
    private int contadorComun;

    private static class Nodo<T> {
        private T dato;
        private Nodo<T> siguiente;

        Nodo(T dato) {
            this.dato = dato;
            this.siguiente = null;
        }
    }

    public ColaCliente() {
        frente = null;
        finalCola = null;
        tamaño = 0;
    }

    public void encolar(Cliente cliente) {
        Nodo<Cliente> nuevoNodo = new Nodo<>(cliente);
        if (finalCola != null) {
            finalCola.siguiente = nuevoNodo;
        }
        finalCola = nuevoNodo;
        if (frente == null) {
            frente = finalCola;
        }
        tamaño++;
    }

    public Cliente desencolar() {
        if (estaVacia()) {
            return null;
        }

        Cliente clienteDesencolado = null;
        if (frente.dato.getPrioridad().equals("1") || contadorComun == 4) {
            clienteDesencolado = frente.dato;
            frente = frente.siguiente;
            if (frente == null) {
                finalCola = null;
            }
            if (!clienteDesencolado.getPrioridad().equals("1")) {
                // Reiniciar contador de clientes comunes atendidos
                contadorComun = 0; 
            }
        } else {
            Nodo<Cliente> actual = frente;
            Nodo<Cliente> anterior = null;
            while (actual != null && !(actual.dato.getPrioridad().equals("1") || contadorComun == 4)) {
                anterior = actual;
                actual = actual.siguiente;
                contadorComun++;
            }
            if (actual != null) {
                clienteDesencolado = actual.dato;
                if (actual == finalCola) {
                    finalCola = anterior;
                }
                if (anterior != null) {
                    anterior.siguiente = actual.siguiente;
                } else {
                    frente = actual.siguiente;
                }
                if (!clienteDesencolado.getPrioridad().equals("1")) {
                    contadorComun = 0; // Reiniciar contador de clientes comunes atendidos
                }
            }
        }

        return clienteDesencolado;
    }


    public Cliente frente() {
        if (estaVacia()) {
            throw new RuntimeException("La cola está vacía");
        }
        return frente.dato;
    }

    public boolean estaVacia() {
        return frente == null;
    }

    public int obtenerTamaño() {
        return tamaño;
    }
    
     public void almacenarClientesPendientes() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src\\banklocker\\clientes_pendientes.in"))) {
            Nodo<Cliente> actual = frente;
            while (actual != null) {
                writer.write(actual.dato.toString() + System.lineSeparator());
                actual = actual.siguiente;
            }
        } catch (IOException e) {
            System.err.println("Error al escribir el archivo de clientes pendientes: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
