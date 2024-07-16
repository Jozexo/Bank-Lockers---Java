/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.banklockers;

import java.util.Scanner;

/**
 *
 * @author imad
 */
public class OperacionesBancarias {
    private Scanner scanner = new Scanner(System.in);
    
    private Double obtenerMontoValido(String mensaje, Double saldo){
        boolean valor = true;
        while(valor){
            Double monto = ingresarDepositoValido(mensaje);
            if(monto<= saldo ){
                return monto;
            }
            else{
                System.out.println("El monto solicitado es mayor al saldo");
            }
        }
        return 0.0;
    }
    
    
    private Double ingresarDepositoValido(String mensaje){
        double monto = -1;
        while (monto <= 0) {
            System.out.println(mensaje);
            if (scanner.hasNextDouble()) {
                monto = scanner.nextDouble();
                if (monto <= 0) {
                    System.out.println("El monto debe ser mayor que cero. Intente de nuevo.");
                }
            } else {
                System.out.println("Entrada no válida. Por favor, ingrese un número.");
                scanner.next(); // Descarta la entrada no válida
            }
        }
        return monto;
    }
    public Double retiro(Cliente cliente){
        System.out.println("================================================");
        System.out.println("  Retiro de dinero  ");
        System.out.println("================================================");
        System.out.println("  Atendiendo a " + cliente.getNombre() + " " + cliente.getApellido());
        System.out.println("================================================");
        System.out.println("Saldo actual: " + cliente.getSaldo());

        Double monto = obtenerMontoValido("¿Cuánto dinero deseas retirar de tu cuenta?", cliente.getSaldo());
        System.out.println("--> ");
        System.out.println("Retirando " + monto + " de tu cuenta...");
        System.out.println("Saldo actualizado: " + (cliente.getSaldo() - monto));
        cliente.setSaldo(cliente.getSaldo() - monto);
        return monto;
        
    }
    
    public Double deposito(Cliente cliente){
        System.out.println("================================================");
        System.out.println("  Deposito de dinero  ");
        System.out.println("================================================");
        System.out.println("  Atendiendo a " + cliente.getNombre() + " " + cliente.getApellido());
        System.out.println("================================================");
        System.out.println("Saldo actual: " + cliente.getSaldo());

        Double monto = ingresarDepositoValido("¿Cuánto dinero deseas depositar en tu cuenta?");
        System.out.println("Depositando " + monto + " en tu cuenta...");
        System.out.println("Saldo actualizado: " + (cliente.getSaldo() + monto));
        cliente.setSaldo(cliente.getSaldo() + monto);
        return monto;
    }
    
    public Double actualizacionLibreta(Cliente cliente){
        System.out.println("================================================");
        System.out.println("  Actualizacion de libreta del cliente  ");
        System.out.println("================================================");
        System.out.println("================================================");
        System.out.println("  Atendiendo a " + cliente.getNombre() + " " + cliente.getApellido());
        System.out.println("================================================");
        System.out.println("Libreta actualizada");
        return 0.00;
    }
    
    public void consultaMovimiento(){
        
    }
    
    public Double pagoServicios(Cliente cliente) {
    System.out.println("================================================");
    System.out.println("  Pago de servicios del cliente  ");
    System.out.println("================================================");
    System.out.println("  Atendiendo a " + cliente.getNombre() + " " + cliente.getApellido());
    System.out.println("================================================");

    System.out.println("Servicios disponibles:");
    System.out.println("1. Agua potable");
    System.out.println("2. Electricidad");
    System.out.println("3. Internet");
    System.out.print("Seleccione el servicio que desea pagar (1-3): ");
    int servicio = scanner.nextInt();

    String servicioSeleccionado = "";
    switch (servicio) {
        case 1:
            servicioSeleccionado = "Agua potable";
            break;
        case 2:
            servicioSeleccionado = "Electricidad";
            break;
        case 3:
            servicioSeleccionado = "Internet";
            break;
        default:
            System.out.println("Opción inválida. Intente de nuevo.");
            return pagoServicios(cliente);
    }
        Double monto = obtenerMontoValido("¿Cuánto dinero deseas pagar por el servicio de " + servicioSeleccionado + "?", cliente.getSaldo());
        System.out.println("Saldo actual: " + cliente.getSaldo());
        System.out.println("Monto a pagar: " + monto);
        System.out.println("Saldo actualizado: " + (cliente.getSaldo() - monto));
        cliente.setSaldo(cliente.getSaldo() - monto);
        System.out.println("================================================");
        System.out.println("Operación en proceso. Espere un momento...");
        return monto;
    }
}
