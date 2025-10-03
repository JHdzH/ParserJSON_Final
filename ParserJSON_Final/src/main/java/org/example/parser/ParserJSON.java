package org.example.parser;

import jakarta.json.*;
import org.example.modelo.Direccion;
import org.example.modelo.Empleado;
import org.example.modelo.Telefono;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ParserJSON {
    private JsonStructure structure;

    public ParserJSON() {
        try {
            // Cargar el archivo JSON desde resources
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("nuevo.json");
            if (inputStream != null) {
                JsonReader reader = Json.createReader(inputStream);
                structure = reader.read();
                reader.close();
                System.out.println("Archivo JSON cargado exitosamente");
            } else {
                // Si no existe el archivo, crear estructura básica
                System.out.println("Archivo no encontrado, creando estructura vacía");
                JsonObjectBuilder rootBuilder = Json.createObjectBuilder();
                structure = rootBuilder.build();
            }
        } catch (Exception e) {
            System.out.println("Error al cargar el archivo: " + e.getMessage());
            // Crear estructura vacía en caso de error
            JsonObjectBuilder rootBuilder = Json.createObjectBuilder();
            structure = rootBuilder.build();
        }
    }

    // OPERACIÓN READ - Obtener todos los empleados
    public List<Empleado> obtenerEmpleados() {
        List<Empleado> empleados = new ArrayList<>();

        if (structure == null || structure.getValueType() != JsonValue.ValueType.OBJECT) {
            return empleados;
        }

        JsonObject datos = (JsonObject) structure;

        for (String key : datos.keySet()) {
            if (key.startsWith("datos")) {
                JsonObject empleadoData = datos.getJsonObject(key);
                Empleado emp = crearEmpleadoDesdeJson(empleadoData);
                if (emp != null) {
                    empleados.add(emp);
                }
            }
        }
        return empleados;
    }

    // OPERACIÓN CREATE - Agregar nuevo empleado
    public boolean agregarEmpleado(Empleado em) {
        try {
            String nuevoId = generarNuevoId();
            JsonObject empleadoJson = construirJsonEmpleado(em);
            JsonPointer pointer = Json.createPointer("/" + nuevoId);

            structure = pointer.add(structure, empleadoJson);
            System.out.println("Empleado agregado con ID: " + nuevoId);
            return true;
        } catch (Exception e) {
            System.out.println("Error al agregar empleado: " + e.getMessage());
            return false;
        }
    }

    // OPERACIÓN UPDATE - Actualizar empleado existente
    public boolean actualizarEmpleado(String idEmpleado, Empleado empleadoActualizado) {
        try {
            JsonPointer pointer = Json.createPointer("/" + idEmpleado);

            if (!pointer.containsValue(structure)) {
                System.out.println("Empleado con ID " + idEmpleado + " no encontrado");
                return false;
            }

            JsonObject empleadoJson = construirJsonEmpleado(empleadoActualizado);
            structure = pointer.replace(structure, empleadoJson);
            System.out.println("Empleado " + idEmpleado + " actualizado exitosamente");
            return true;
        } catch (Exception e) {
            System.out.println("Error al actualizar empleado: " + e.getMessage());
            return false;
        }
    }

    // OPERACIÓN DELETE - Eliminar empleado
    public boolean borrarEmpleado(String idEmpleado) {
        try {
            JsonPointer pointer = Json.createPointer("/" + idEmpleado);

            if (!pointer.containsValue(structure)) {
                System.out.println("Empleado con ID " + idEmpleado + " no encontrado");
                return false;
            }

            structure = pointer.remove(structure);
            System.out.println("Empleado " + idEmpleado + " eliminado exitosamente");
            return true;
        } catch (Exception e) {
            System.out.println("Error al eliminar empleado: " + e.getMessage());
            return false;
        }
    }

    // Métodos auxiliares privados
    private Empleado crearEmpleadoDesdeJson(JsonObject empleadoData) {
        try {
            Empleado emp = new Empleado();
            emp.setNombre(empleadoData.getString("firstName", ""));
            emp.setApellido(empleadoData.getString("lastName", ""));
            emp.setEdad(empleadoData.getInt("age", 0));

            JsonObject address = empleadoData.getJsonObject("address");
            if (address != null) {
                emp.setDir(obtenerDir(address));
            }

            JsonArray phones = empleadoData.getJsonArray("phoneNumbers");
            if (phones != null) {
                emp.setTelefonos(obtenerTelefonos(phones));
            }

            return emp;
        } catch (Exception e) {
            System.out.println("Error al crear empleado desde JSON: " + e.getMessage());
            return null;
        }
    }

    private JsonObject construirJsonEmpleado(Empleado em) {
        JsonObjectBuilder empleadoBuilder = Json.createObjectBuilder()
                .add("firstName", em.getNombre())
                .add("lastName", em.getApellido())
                .add("age", em.getEdad());

        // Construir dirección
        if (em.getDir() != null) {
            JsonObjectBuilder dirBuilder = Json.createObjectBuilder()
                    .add("streetAddress", em.getDir().getCalle())
                    .add("city", em.getDir().getCiudad())
                    .add("state", em.getDir().getEstado())
                    .add("postalCode", em.getDir().getCp());
            empleadoBuilder.add("address", dirBuilder);
        }

        // Construir teléfonos
        if (em.getTelefonos() != null && !em.getTelefonos().isEmpty()) {
            JsonArrayBuilder arrayTelefonos = Json.createArrayBuilder();
            for (Telefono telefono : em.getTelefonos()) {
                JsonObjectBuilder telBuilder = Json.createObjectBuilder()
                        .add("type", telefono.getTipo())
                        .add("number", telefono.getNumero());
                arrayTelefonos.add(telBuilder);
            }
            empleadoBuilder.add("phoneNumbers", arrayTelefonos);
        }

        return empleadoBuilder.build();
    }

    private String generarNuevoId() {
        int maxId = 0;
        if (structure.getValueType() == JsonValue.ValueType.OBJECT) {
            JsonObject datos = (JsonObject) structure;
            for (String key : datos.keySet()) {
                if (key.startsWith("datos")) {
                    try {
                        int idNum = Integer.parseInt(key.substring(5));
                        if (idNum > maxId) {
                            maxId = idNum;
                        }
                    } catch (NumberFormatException e) {
                        // Ignorar claves que no siguen el formato esperado
                    }
                }
            }
        }
        return "datos" + (maxId + 1);
    }

    private Direccion obtenerDir(JsonObject direccion) {
        Direccion dire = new Direccion();
        dire.setCalle(direccion.getString("streetAddress", ""));
        dire.setCiudad(direccion.getString("city", ""));
        dire.setEstado(direccion.getString("state", ""));
        dire.setCp(direccion.getInt("postalCode", 0));
        return dire;
    }

    private List<Telefono> obtenerTelefonos(JsonArray telefonos) {
        List<Telefono> telefonoList = new ArrayList<>();
        for (int i = 0; i < telefonos.size(); i++) {
            JsonObject jsonTelefono = telefonos.getJsonObject(i);
            Telefono telefono = new Telefono();
            telefono.setTipo(jsonTelefono.getString("type", ""));
            telefono.setNumero(jsonTelefono.getString("number", ""));
            telefonoList.add(telefono);
        }
        return telefonoList;
    }

    public void mostrarContenido() {
        System.out.println("=== CONTENIDO ACTUAL DEL SISTEMA ===");

        if (structure == null || structure.getValueType() != JsonValue.ValueType.OBJECT) {
            System.out.println("No hay datos en el sistema");
            return;
        }

        JsonObject datos = (JsonObject) structure;

        if (datos.isEmpty()) {
            System.out.println("El sistema está vacío");
            return;
        }

        for (String key : datos.keySet()) {
            System.out.println("\n--- " + key + " ---");
            JsonObject empleadoData = datos.getJsonObject(key);
            mostrarEmpleado(empleadoData);
        }
    }

    private void mostrarEmpleado(JsonObject empleadoData) {
        System.out.println("Nombre: " + empleadoData.getString("firstName", "N/A") +
                " " + empleadoData.getString("lastName", "N/A"));
        System.out.println("Edad: " + empleadoData.getInt("age", 0));

        JsonObject address = empleadoData.getJsonObject("address");
        if (address != null) {
            System.out.println("Dirección:");
            System.out.println("  Calle: " + address.getString("streetAddress", "N/A"));
            System.out.println("  Ciudad: " + address.getString("city", "N/A"));
            System.out.println("  Estado: " + address.getString("state", "N/A"));
            System.out.println("  CP: " + address.getInt("postalCode", 0));
        }

        JsonArray phones = empleadoData.getJsonArray("phoneNumbers");
        if (phones != null) {
            System.out.println("Teléfonos:");
            for (int i = 0; i < phones.size(); i++) {
                JsonObject phone = phones.getJsonObject(i);
                System.out.println("  " + phone.getString("type", "N/A") + ": " +
                        phone.getString("number", "N/A"));
            }
        }
    }

    // Método para obtener el contenido JSON como string (para debugging)
    public String obtenerJsonComoString() {
        if (structure != null) {
            return structure.toString();
        }
        return "{}";
    }

    public String obtenerJSONComoString() {
        try {
            java.io.StringWriter stWriter = new java.io.StringWriter();
            jakarta.json.JsonWriter jsonWriter = jakarta.json.Json.createWriter(stWriter);
            jsonWriter.write(structure);
            jsonWriter.close();
            return stWriter.toString();
        } catch (Exception e) {
            return "Error al convertir JSON: " + e.getMessage();
        }
    }

}