/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package banklocker;

/**
 *
 * @author joseg
 */
public class BankLocker {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
         com.banklockers.AsesorTaquilla asesor = new com.banklockers.AsesorTaquilla("Juan", "Perez");
        com.banklockers.Taquilla taquilla = new com.banklockers.Taquilla(asesor);
        taquilla.iniciarTaquilla();
    }
    
}
