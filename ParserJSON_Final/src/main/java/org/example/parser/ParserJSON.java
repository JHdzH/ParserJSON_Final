package org.example.parser;

import com.sun.tools.javac.Main;
import jakarta.json.*;
import org.example.modelo.Direccion;
import org.example.modelo.Empleado;
import org.example.modelo.Telefono;

import java.util.ArrayList;
import java.util.List;

public class ParserJSON {
    JsonReader reader = Json.createReader(Main.class.getResourceAsStream("/nuevo.json"));
    JsonStructure structure;

    public ParserJSON() {
        structure = reader.read();
    }

    public List<Empleado> obtenerEmpleados() {
        List<Empleado> empleados = new ArrayList<>();
        JsonValue valores = structure.getValue("/");
        JsonObject datos = valores.asJsonObject();

        // Obtener todos los objetos que empiezan con "datos"
        for (String key : datos.keySet()) {
            if (key.startsWith("datos")) {
                JsonObject empleadoData = datos.getJsonObject(key);
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

                empleados.add(emp);
            }
        }
        return empleados;
    }

    public void agregarEmpleado(Empleado em) {
        String nuevoId = "datos" + (obtenerCantidadEmpleados() + 1);

        JsonObjectBuilder empleadoBuilder = Json.createObjectBuilder()
                .add("firstName", em.getNombre())
                .add("lastName", em.getApellido())
                .add("age", em.getEdad());

        // Construir dirección
        JsonObjectBuilder dirBuilder = Json.createObjectBuilder()
                .add("streetAddress", em.getDir().getCalle())
                .add("city", em.getDir().getCiudad())
                .add("state", em.getDir().getEstado())
                .add("postalCode", em.getDir().getCp());

        empleadoBuilder.add("address", dirBuilder);

        // Construir teléfonos
        JsonArrayBuilder arrayTelefonos = Json.createArrayBuilder();
        for (Telefono telefono : em.getTelefonos()) {
            JsonObjectBuilder telBuilder = Json.createObjectBuilder()
                    .add("type", telefono.getTipo())
                    .add("number", telefono.getNumero());
            arrayTelefonos.add(telBuilder);
        }

        empleadoBuilder.add("phoneNumbers", arrayTelefonos);

        JsonPointer pointer = Json.createPointer("/" + nuevoId);
        structure = pointer.add(structure, empleadoBuilder.build());
    }

    public boolean actualizarEmpleado(String idEmpleado, Empleado empleadoActualizado) {
        JsonPointer pointer = Json.createPointer("/" + idEmpleado);

        if (!pointer.containsValue(structure)) {
            return false; // El empleado no existe
        }

        JsonObjectBuilder empleadoBuilder = Json.createObjectBuilder()
                .add("firstName", empleadoActualizado.getNombre())
                .add("lastName", empleadoActualizado.getApellido())
                .add("age", empleadoActualizado.getEdad());

        // Construir dirección actualizada
        JsonObjectBuilder dirBuilder = Json.createObjectBuilder()
                .add("streetAddress", empleadoActualizado.getDir().getCalle())
                .add("city", empleadoActualizado.getDir().getCiudad())
                .add("state", empleadoActualizado.getDir().getEstado())
                .add("postalCode", empleadoActualizado.getDir().getCp());

        empleadoBuilder.add("address", dirBuilder);

        // Construir teléfonos actualizados
        JsonArrayBuilder arrayTelefonos = Json.createArrayBuilder();
        for (Telefono telefono : empleadoActualizado.getTelefonos()) {
            JsonObjectBuilder telBuilder = Json.createObjectBuilder()
                    .add("type", telefono.getTipo())
                    .add("number", telefono.getNumero());
            arrayTelefonos.add(telBuilder);
        }

        empleadoBuilder.add("phoneNumbers", arrayTelefonos);

        // Reemplazar el empleado existente
        structure = pointer.replace(structure, empleadoBuilder.build());
        return true;
    }

    public boolean borrarEmpleado(String idEmpleado) {
        JsonPointer pointer = Json.createPointer("/" + idEmpleado);

        if (!pointer.containsValue(structure)) {
            return false; // El empleado no existe
        }

        structure = pointer.remove(structure);
        return true;
    }

    // Método auxiliar para obtener cantidad de empleados
    private int obtenerCantidadEmpleados() {
        JsonValue valores = structure.getValue("/");
        JsonObject datos = valores.asJsonObject();
        int count = 0;

        for (String key : datos.keySet()) {
            if (key.startsWith("datos")) {
                count++;
            }
        }
        return count;
    }

    // Métodos auxiliares maestraDiana
    private Direccion obtenerDir(JsonObject direccion) {
        Direccion dire = new Direccion();
        dire.setCalle(direccion.getString("streetAddress"));
        dire.setCiudad(direccion.getString("city"));
        dire.setEstado(direccion.getString("state"));
        dire.setCp(direccion.getInt("postalCode"));
        return dire;
    }

    private List<Telefono> obtenerTelefonos(JsonArray telefonos) {
        List<Telefono> telefonoList = new ArrayList<>();
        for (int i = 0; i < telefonos.size(); i++) {
            JsonObject jsonTelefono = telefonos.getJsonObject(i);
            Telefono telefono = new Telefono();
            telefono.setTipo(jsonTelefono.getString("type"));
            telefono.setNumero(jsonTelefono.getString("number"));
            telefonoList.add(telefono);
        }
        return telefonoList;
    }

    public void contenido() {
        // Método existente sin cambios
        JsonValue valores = structure.getValue("");
        JsonObject objeto = (JsonObject) valores;
        for(String e : objeto.keySet()) {
            System.out.println("=== " + e + " ===");
            JsonObject valore = (JsonObject) objeto.getValue("/" + e);
            for(String v : valore.keySet()) {
                System.out.println(v + ":");
                if (valore.get(v).getValueType() == JsonValue.ValueType.ARRAY) {
                    JsonArray array = (JsonArray) valore.getJsonArray(v);
                    for(JsonValue j : array){
                        JsonObject obj = (JsonObject) j;
                        for (String k : obj.keySet()) {
                            System.out.println("  " + k + ": " + obj.get(k));
                        }
                    }
                } else if (valore.get(v).getValueType() == JsonValue.ValueType.OBJECT) {
                    JsonObject objeto1 = (JsonObject) valore.get(v);
                    for(String e1 : objeto1.keySet()) {
                        System.out.println("  " + e1 + ": " + objeto1.get(e1));
                    }
                } else {
                    System.out.println("  " + valore.get(v));
                }
            }
            System.out.println();
        }
    }
}