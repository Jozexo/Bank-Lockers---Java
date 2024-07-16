/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.banklockers;



/**
 *
 * @author joseg
 */
public class Cliente {
    private String nombre;
    private String apellido;
    private String cedula;
    private String clave; 
    private double saldo;
    private String prioridad;

    public Cliente(String cedula, String nombre, String apellido, String clave, double saldo, String prioridad) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.cedula = cedula;
        this.clave = clave; 
        this.saldo = saldo;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }
    
    
     public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }
    
    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    // Getters y setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
    
    @Override
    public String toString() {
        return cedula + "," + nombre + "," + apellido + "," + clave + "," + saldo + "," + prioridad;
    }
}