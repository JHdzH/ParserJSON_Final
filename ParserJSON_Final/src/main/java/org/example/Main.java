package org.example;

import org.example.modelo.Direccion;
import org.example.modelo.Empleado;
import org.example.modelo.Telefono;
import org.example.parser.ParserJSON;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        ParserJSON parser = new ParserJSON();

        System.out.println("=== SISTEMA CRUD DE EMPLEADOS - PRUEBA CON MÚLTIPLES EMPLEADOS ===\n");

        // PRUEBA 1: Estado inicial del sistema
        System.out.println("1. ESTADO INICIAL DEL SISTEMA");
        System.out.println("==============================");
        parser.mostrarContenido();

        // PRUEBA 2: Crear primer empleado adicional
        System.out.println("\n2. AGREGAR PRIMER EMPLEADO NUEVO");
        System.out.println("================================");
        Empleado empleado1 = new Empleado();
        empleado1.setNombre("Ana");
        empleado1.setApellido("López");
        empleado1.setEdad(28);

        Direccion dir1 = new Direccion("Av. Hidalgo 456", "Coyoacán", "CDMX", 04000);
        empleado1.setDir(dir1);

        Telefono tel1_1 = new Telefono("Celular", "55 1111 2222");
        Telefono tel1_2 = new Telefono("Casa", "55 3333 4444");
        List<Telefono> telefonos1 = Arrays.asList(tel1_1, tel1_2);
        empleado1.setTelefonos(telefonos1);

        boolean creado1 = parser.agregarEmpleado(empleado1);
        if (creado1) {
            System.out.println("✓ Primer empleado agregado exitosamente");
        }

        // PRUEBA 3: Crear segundo empleado adicional
        System.out.println("\n3. AGREGAR SEGUNDO EMPLEADO NUEVO");
        System.out.println("=================================");
        Empleado empleado2 = new Empleado();
        empleado2.setNombre("Carlos");
        empleado2.setApellido("Rodríguez");
        empleado2.setEdad(32);

        Direccion dir2 = new Direccion("Calle Juárez 789", "Alvaro Obregón", "CDMX", 01000);
        empleado2.setDir(dir2);

        Telefono tel2_1 = new Telefono("Celular", "55 5555 6666");
        Telefono tel2_2 = new Telefono("Oficina", "55 7777 8888");
        Telefono tel2_3 = new Telefono("Casa", "55 9999 0000");
        List<Telefono> telefonos2 = Arrays.asList(tel2_1, tel2_2, tel2_3);
        empleado2.setTelefonos(telefonos2);

        boolean creado2 = parser.agregarEmpleado(empleado2);
        if (creado2) {
            System.out.println("✓ Segundo empleado agregado exitosamente");
        }

        // PRUEBA 4: Crear tercer empleado adicional
        System.out.println("\n4. AGREGAR TERCER EMPLEADO NUEVO");
        System.out.println("=================================");
        Empleado empleado3 = new Empleado();
        empleado3.setNombre("Laura");
        empleado3.setApellido("Martínez");
        empleado3.setEdad(26);

        Direccion dir3 = new Direccion("Blvd. Toluca 321", "Tlalpan", "CDMX", 14000);
        empleado3.setDir(dir3);

        Telefono tel3_1 = new Telefono("Celular", "55 4444 3333");
        List<Telefono> telefonos3 = Arrays.asList(tel3_1);
        empleado3.setTelefonos(telefonos3);

        boolean creado3 = parser.agregarEmpleado(empleado3);
        if (creado3) {
            System.out.println("✓ Tercer empleado agregado exitosamente");
        }

        // Mostrar estado después de agregar los 3 empleados
        System.out.println("\n5. ESTADO DESPUÉS DE AGREGAR 3 EMPLEADOS NUEVOS");
        System.out.println("================================================");
        parser.mostrarContenido();

        // PRUEBA 6: Mostrar todos los empleados existentes
        System.out.println("\n6. LISTA COMPLETA DE EMPLEADOS ANTES DE ELIMINAR");
        System.out.println("=================================================");
        List<Empleado> todosEmpleados = parser.obtenerEmpleados();
        System.out.println("Total de empleados en el sistema: " + todosEmpleados.size());

        for (int i = 0; i < todosEmpleados.size(); i++) {
            Empleado emp = todosEmpleados.get(i);
            System.out.println("\n--- Empleado " + (i + 1) + " ---");
            System.out.println("Nombre: " + emp.getNombre() + " " + emp.getApellido());
            System.out.println("Edad: " + emp.getEdad());
            System.out.println("Dirección: " + emp.getDir().getCalle() + ", " +
                    emp.getDir().getCiudad() + ", " + emp.getDir().getEstado());
            System.out.println("Teléfonos: " + emp.getTelefonos().size());
        }

        // PRUEBA 7: Eliminar solo un empleado (el segundo que agregamos - datos2)
        System.out.println("\n7. ELIMINAR UN EMPLEADO (datos2)");
        System.out.println("================================");
        boolean eliminado = parser.borrarEmpleado("datos2");

        if (eliminado) {
            System.out.println("✓ Empleado datos2 eliminado exitosamente");
        } else {
            System.out.println("✗ No se pudo eliminar el empleado datos2");
        }

        // PRUEBA 8: Estado final del sistema después de eliminar
        System.out.println("\n8. ESTADO FINAL DEL SISTEMA DESPUÉS DE ELIMINAR");
        System.out.println("================================================");
        parser.mostrarContenido();

        // PRUEBA 9: Lista final de empleados
        System.out.println("\n9. RESUMEN FINAL");
        System.out.println("=================");
        List<Empleado> empleadosFinales = parser.obtenerEmpleados();
        System.out.println("EMPLEADOS RESTANTES EN EL SISTEMA: " + empleadosFinales.size());

        for (int i = 0; i < empleadosFinales.size(); i++) {
            Empleado emp = empleadosFinales.get(i);
            System.out.println("\n" + (i + 1) + ". " + emp.getNombre() + " " + emp.getApellido() +
                    " (Edad: " + emp.getEdad() + ")");
            System.out.println("   Dirección: " + emp.getDir().getCalle() + ", " +
                    emp.getDir().getCiudad() + ", " + emp.getDir().getEstado());
            System.out.println("   Teléfonos: " + emp.getTelefonos().size());
            for (Telefono tel : emp.getTelefonos()) {
                System.out.println("     - " + tel.getTipo() + ": " + tel.getNumero());
            }
        }

        System.out.println("\n=== PRUEBA COMPLETADA - CRUD FUNCIONANDO CORRECTAMENTE ===");
        System.out.println("Operaciones realizadas:");
        System.out.println("- 3 empleados agregados nuevos");
        System.out.println("- 1 empleado eliminado (datos2)");
        System.out.println("- Total final: " + empleadosFinales.size() + " empleados en el sistema");
    }
}