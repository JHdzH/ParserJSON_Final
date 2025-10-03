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

        System.out.println("=== PRUEBAS DEL SISTEMA CRUD DE EMPLEADOS ===\n");

        // PRUEBA 1: Leer empleados existentes
        System.out.println("1. LEER EMPLEADOS EXISTENTES");
        System.out.println("----------------------------");
        List<Empleado> empleados = parser.obtenerEmpleados();
        if (empleados.isEmpty()) {
            System.out.println("No se encontraron empleados existentes");
        } else {
            for (Empleado emp : empleados) {
                System.out.println(emp);
                System.out.println("---");
            }
        }
        System.out.println();

        // PRUEBA 2: Crear nuevo empleado
        System.out.println("2. CREAR NUEVO EMPLEADO");
        System.out.println("-----------------------");
        Empleado nuevoEmpleado = new Empleado();
        nuevoEmpleado.setNombre("María");
        nuevoEmpleado.setApellido("García");
        nuevoEmpleado.setEdad(30);

        Direccion dirNueva = new Direccion("Av. Reforma 123", "Benito Juárez", "CDMX", 06600);
        nuevoEmpleado.setDir(dirNueva);

        Telefono tel1 = new Telefono("Celular", "55 1234 5678");
        Telefono tel2 = new Telefono("Oficina", "55 8765 4321");
        List<Telefono> telefonosNuevos = Arrays.asList(tel1, tel2);
        nuevoEmpleado.setTelefonos(telefonosNuevos);

        parser.agregarEmpleado(nuevoEmpleado);
        System.out.println("Empleado creado exitosamente: " + nuevoEmpleado.getNombre() + " " + nuevoEmpleado.getApellido());
        System.out.println();

        // PRUEBA 3: Mostrar contenido después de crear
        System.out.println("3. CONTENIDO DESPUES DE CREAR");
        System.out.println("-----------------------------");
        parser.contenido();
        System.out.println();

        // PRUEBA 4: Actualizar empleado
        System.out.println("4. ACTUALIZAR EMPLEADO");
        System.out.println("----------------------");
        Empleado empleadoActualizado = new Empleado();
        empleadoActualizado.setNombre("María Elena");
        empleadoActualizado.setApellido("García López");
        empleadoActualizado.setEdad(31);

        Direccion dirActualizada = new Direccion("Av. Insurgentes 456", "Cuauhtémoc", "CDMX", 06700);
        empleadoActualizado.setDir(dirActualizada);

        Telefono telActualizado = new Telefono("Celular", "55 9999 8888");
        List<Telefono> telefonosActualizados = Arrays.asList(telActualizado);
        empleadoActualizado.setTelefonos(telefonosActualizados);

        boolean actualizacionExitosa = parser.actualizarEmpleado("datos2", empleadoActualizado);
        if (actualizacionExitosa) {
            System.out.println("Empleado actualizado exitosamente");
        } else {
            System.out.println("Error: No se pudo actualizar el empleado");
        }
        System.out.println();

        // PRUEBA 5: Mostrar contenido después de actualizar
        System.out.println("5. CONTENIDO DESPUES DE ACTUALIZAR");
        System.out.println("----------------------------------");
        parser.contenido();
        System.out.println();

        // PRUEBA 6: Eliminar empleado
        System.out.println("6. ELIMINAR EMPLEADO");
        System.out.println("--------------------");
        boolean eliminacionExitosa = parser.borrarEmpleado("datos2");
        if (eliminacionExitosa) {
            System.out.println("Empleado eliminado exitosamente");
        } else {
            System.out.println("Error: No se pudo eliminar el empleado");
        }
        System.out.println();

        // PRUEBA 7: Mostrar contenido final
        System.out.println("7. CONTENIDO FINAL");
        System.out.println("------------------");
        parser.contenido();
        System.out.println();

        // PRUEBA 8: Verificar lista final de empleados
        System.out.println("8. LISTA FINAL DE EMPLEADOS");
        System.out.println("---------------------------");
        List<Empleado> empleadosFinales = parser.obtenerEmpleados();
        if (empleadosFinales.isEmpty()) {
            System.out.println("No hay empleados en el sistema");
        } else {
            System.out.println("Total de empleados: " + empleadosFinales.size());
            for (Empleado emp : empleadosFinales) {
                System.out.println(emp);
                System.out.println("---");
            }
        }

        System.out.println("=== PRUEBAS COMPLETADAS ===");
    }
}