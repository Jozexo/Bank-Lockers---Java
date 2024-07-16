/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.banklockers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author imad
 */

public class Operacion {
    private NodoCliente primerNodo;

    public Operacion(String nombreArchivo) {
        primerNodo = null;
        cargarClientesDesdeArchivo(nombreArchivo);
    }

    private void cargarClientesDesdeArchivo(String nombreArchivo) {
        try (BufferedReader br = new BufferedReader(new FileReader(nombreArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                String cedula = partes[0].trim();
                String nombre = partes[1].trim();
                String apellido = partes[2].trim();
                String clave = partes[3].trim();
                String prioridad = partes[5].trim();
                double saldo = Double.parseDouble(partes[4].trim());
                Cliente cliente = new Cliente(cedula, nombre, apellido, clave, saldo, prioridad);
                insertarAlFinal(cliente);
            }
        } catch (Exception e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void insertarAlFinal(Cliente cliente) {
        NodoCliente nuevoNodo = new NodoCliente(cliente);
        if (primerNodo == null) {
            primerNodo = nuevoNodo;
        } else {
            NodoCliente actual = primerNodo;
            while (actual.siguiente != null) {
                actual = actual.siguiente;
            }
            actual.siguiente = nuevoNodo;
        }
    }
    
     public Cliente buscarClientePorCedula(String cedula) {
        NodoCliente actual = primerNodo;
        while (actual != null) {
            if (actual.cliente.getCedula().equals(cedula)) {
                return actual.cliente;
            }
            actual = actual.siguiente;
        }
        return null; // Si no se encuentra el cliente
    }

}


class NodoCliente {
    Cliente cliente;
    NodoCliente siguiente;

    public NodoCliente(Cliente cliente) {
        this.cliente = cliente;
        this.siguiente = null;
    }
}

