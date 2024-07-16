/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.banklockers;

import com.banklockers.Cliente;
import com.banklockers.PilaCliente.Transaccion;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;

/**
 *
 * @author joseg
 */
public class Taquilla {
    private AsesorTaquilla asesor;
    private ColaCliente colaClientes;
    private Scanner scanner;
    private PilaCliente pilaClientes;
    int solicitudes = 0;

    public Taquilla(AsesorTaquilla asesor) {
        this.asesor = asesor;
        this.colaClientes = new ColaCliente();
        this.scanner = new Scanner(System.in);
        this.pilaClientes = new PilaCliente();
    }

    
    public void cierreTaquilla(){
        
    }
    
    public void iniciarTaquilla() {
        System.out.println("===============================================");
        System.out.println("              Welcome to Bancamiga             ");
        System.out.println("===============================================");
        System.out.println("Asesor de taquilla: " + asesor.getNombre() + " " + asesor.getApellido());
        System.out.println("Estado: Disponible");
        while (true) {
            System.out.println("1 - Llegada del Cliente.");
            System.out.println("2 - Salir.");
            System.out.print("--> ");
            int opcion = scanner.nextInt();
            if (opcion == 1) {
                agregarCliente();
            } else if (opcion == 2) {
               // Ruta del archivo de log actual
               colaClientes.almacenarClientesPendientes();
               String rutaArchivoLog = "src\\\\banklocker\\\\Taquilla.log";

               // Renombrar archivo existente si existe
               renombrarArchivoLogExistente(rutaArchivoLog);

               // Escribir transacciones en el nuevo archivo de log
               escribirTransaccionesEnLog(rutaArchivoLog);
               
                break;
            }
        }
    }

    public String obtenerCedulaValida(){
        String cedula = "";
        boolean valor = true;
        
        String regex = "^[0-9]{7,10}$";
        while(valor){
            System.out.print("Ingrese la cedula del cliente: " );
            cedula = scanner.next();
            
            if(cedula.matches(regex)){
                valor = false;
                return cedula;
            }
        }
        return "";
    }
    private void agregarCliente() {
        System.out.println("================================================");
        System.out.println("  Ingreso del Cliente  ");
        System.out.println("================================================");
        
        String cedula = obtenerCedulaValida();
        Operacion operacion = new Operacion("src\\\\banklocker\\\\Clientes.in");
        Cliente cliente = operacion.buscarClientePorCedula(cedula);
        if(cliente != null){
           colaClientes.encolar(cliente);
           System.out.println("Cliente agregado a la cola");
           procesarCliente();
        }
        else{
            System.out.println("Cliente no registrado en Clientes.in");
        }
        
    }
    
    public void cargarClientesPendientes() {
        File archivoPendientes = new File("src\\\\banklocker\\\\clientes_pendientes.in");
        if (archivoPendientes.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(archivoPendientes))) {
                String linea;
                while ((linea = reader.readLine()) != null) {
                    String[] partes = linea.split(",");
                    String cedula = partes[0].trim();
                    String nombre = partes[1].trim();
                    String apellido = partes[2].trim();
                    String clave = partes[3].trim();
                    double saldo = Double.parseDouble(partes[4].trim());
                    String prioridad = partes[5].trim();
                    Cliente cliente = new Cliente(cedula, nombre, apellido, clave, saldo, prioridad);
                    colaClientes.encolar(cliente);
                }
                // Eliminar archivo de clientes pendientes después de cargar
                Files.delete(Paths.get("src\\\\banklocker\\\\clientes_pendientes.in"));
            } catch (IOException e) {
                System.err.println("Error al leer el archivo de clientes pendientes: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void procesarCliente() {
        if (!colaClientes.estaVacia() && asesor.isDisponible()) {
            Cliente cliente = colaClientes.frente();
            System.out.println("================================================");
            System.out.println("  Atendiendo a " + cliente.getNombre() + " " + cliente.getApellido());
            System.out.println("================================================");
            procesarSolicitudes(cliente);
            clienteSaleDeLaSede(cliente); 
        }
        else{
            System.out.println("La taquilla esta ocupada, esta en la cola");
        }
    }
    
    private void clienteSaleDeLaSede(Cliente cliente){
        System.out.println("================================================");
        System.out.println("  " + cliente.getNombre() + " " + cliente.getApellido() + " sale de la sede de Bancamiga");
        System.out.println("================================================");  
    }
    
     public void renombrarArchivoLogExistente(String rutaArchivoLog) {
         //acceder al archivo de la taquillas.log
        File archivoLog = new File(rutaArchivoLog);
        if (archivoLog.exists()) {
            //dar el formato de fecha
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Calendar calendar = Calendar.getInstance();
             // Fecha del día anterior
            calendar.add(Calendar.DAY_OF_YEAR, -1);
            String fechaAyer = sdf.format(calendar.getTime());

            String nuevoNombre = "src\\\\banklocker\\\\Taquilla" + fechaAyer + ".log";
            //renombra el archivo si es necesario
            File nuevoArchivo = new File(nuevoNombre);
            archivoLog.renameTo(nuevoArchivo);
        }
    }
     

    public void escribirTransaccionesEnLog(String rutaArchivoLog) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaArchivoLog))) {
            while (!pilaClientes.estaVacia()) {
                Transaccion transaccion = pilaClientes.desapilar();
                writer.write(transaccion.toString() + System.lineSeparator());
            }
        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo de log: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void procesarSolicitudes(Cliente cliente) {
        //tiempo de los hilos
        double tiempoProceso = 0;

        //para mantenerse en el menu
        boolean continuar = true;

        //pasa a esta el asesor ocupado
        asesor.setDisponible(false);

        // tiene los metodos de todas las operaciones bancarias
        OperacionesBancarias operacionesBancarias = new OperacionesBancarias();
        
        while (continuar) {
            //menu
            System.out.println("Seleccione una operación:");
            System.out.println("1. Retiro");
            System.out.println("2. Depósito");
            System.out.println("3. Consulta de Movimientos");
            System.out.println("4. Actualización de Libretas");
            System.out.println("5. Pago de Servicios");
            System.out.println("6. Salir");
            System.out.print("--> ");

            String opcion = scanner.next();
            Double monto = 0.00;
            Transaccion transaccion;
            Thread tareaThread;
            
            switch (opcion) {
                case "1":
                    monto = operacionesBancarias.retiro(cliente);
                    tiempoProceso = 4;
                    System.out.println("Ha seleccionado 'Retiro'. Tiempo de proceso: " + tiempoProceso + " minutos.");
                    transaccion = new Transaccion(cliente.getCedula(), cliente.getNombre(), "Retiro", monto);
                    tareaThread = new Thread(new TareaConEspera(tiempoProceso, transaccion));
                    tareaThread.start();
                    break;
                case "2":
                    monto = operacionesBancarias.deposito(cliente);
                    tiempoProceso = 3;
                    System.out.println("Ha seleccionado 'Depósito'. Tiempo de proceso: " + tiempoProceso + " minutos.");
                    transaccion = new Transaccion(cliente.getCedula(), cliente.getNombre(), "Deposito", monto);
                    tareaThread = new Thread(new TareaConEspera(tiempoProceso, transaccion));
                    tareaThread.start();
                    break;
                case "3":
                    tiempoProceso = 1.5;
                    System.out.println("Ha seleccionado 'Consulta de movimientos'. Tiempo de proceso: " + tiempoProceso + " minutos.");
                    transaccion = new Transaccion(cliente.getCedula(), cliente.getNombre(), "Consulta de movimientos", 0.00);
                    tareaThread = new Thread(new TareaConEspera(tiempoProceso, transaccion));
                    tareaThread.start();
                    break;
                case "4":
                    monto = operacionesBancarias.actualizacionLibreta(cliente);
                    tiempoProceso = 5;
                    System.out.println("Ha seleccionado 'Actualización de libretas'. Tiempo de proceso: " + tiempoProceso + " minutos.");
                    transaccion = new Transaccion(cliente.getCedula(), cliente.getNombre(), "Actualización de libretas", monto);
                    tareaThread = new Thread(new TareaConEspera(tiempoProceso, transaccion));
                    tareaThread.start();
                    break;
                case "5":
                    monto = operacionesBancarias.pagoServicios(cliente);
                    tiempoProceso = 2;
                    System.out.println("Ha seleccionado 'Pago de servicios'. Tiempo de proceso: " + tiempoProceso + " minutos.");
                    transaccion = new Transaccion(cliente.getCedula(), cliente.getNombre(), "Pago de servicios", monto);
                    tareaThread = new Thread(new TareaConEspera(tiempoProceso, transaccion));
                    tareaThread.start();
                    break;
                case "6":
                    continuar = false;
                    System.out.println("Gracias por usar nuestro servicio. Bye Bye");
                    break;
                default:
                    System.out.println("Opción no valida. Por favor, intente de nuevo.");
                    break;
            }

            if (continuar) {
                System.out.println();
                  //aumenta las solicitudes hasta llegar al maximo
                solicitudes++;
                System.out.println("Numero de solicitudes: "+solicitudes);
                if (solicitudes < 5) {
                    System.out.println("Desea realizar otra solicitud si o no s/n");
                    String texto = scanner.next().trim();
                      if(texto.equals("s")){
                            continuar = true;
                        }
                        else if(texto.equals("n")){
                            continuar = false;
                        }
                        else{
                            System.out.println("Operacion incorrecta");
                        }
                } else {
                    System.out.println("Ha realizado el máximo de 5 solicitudes. Hasta luego, vuelva pronto.");
                    continuar = false;
                }
            }
        }
    }
    
    

    class TareaConEspera implements Runnable {
        //para que se pueden ejecutar hilos secundarios
        private double tiempoProceso;
        private Transaccion transaccion;

        //pasa como parametro el tiempo que se va a tomar la operacion
        public TareaConEspera(double tiempoProceso, Transaccion transaccion) {
            this.tiempoProceso = tiempoProceso;
            this.transaccion = transaccion;
        }

        //sobreescribe el metodo run
        @Override
        public void run() {
            try {
                System.out.println("Tarea iniciada en el hilo secundario...");
                Thread.sleep((int) (tiempoProceso * 60 * 1000)); // Espera simulada
                System.out.println("Tarea completada en el hilo secundario.");
                pilaClientes.apilar(transaccion);
                //procesa las solicitudes
                solicitudes -= 1;
                if(solicitudes == 0){
                    asesor.setDisponible(true);
                    if(!colaClientes.estaVacia()){
                        //da el turno al siguiente en la fila
                        Cliente cliente = colaClientes.desencolar();
                        System.out.println("================================================");
                        System.out.println("  Atendiendo a " + cliente.getNombre() + " " + cliente.getApellido());
                        System.out.println("================================================");
                        procesarSolicitudes(cliente);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    } 
}
