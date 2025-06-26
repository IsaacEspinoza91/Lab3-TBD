package com.tbd.DeliveryMedicamentos.entities;

public class ZonasEntity {
    private int id;
    private String nombre;
    private String geom;

    public ZonasEntity(int id, String nombre, String geom) {
        this.id = id;
        this.nombre = nombre;
        this.geom = geom;
    }

    public ZonasEntity() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    public void setGeom(String geom) {
        this.geom = geom;
    }

    // Para setear el polígono a partir de una lista de coordenadas (lat, lng)
    public void setGeomFromCoordenadas(double[][] coordenadas) {
        StringBuilder sb = new StringBuilder("POLYGON((");
        for (double[] coord : coordenadas) {
            sb.append(coord[1]).append(" ").append(coord[0]).append(", "); // lng lat
        }
        // Cerrar el polígono (el último punto debe ser igual al primero)
        sb.append(coordenadas[0][1]).append(" ").append(coordenadas[0][0]).append("))");
        this.geom = sb.toString();
    }

    public String getGeom() {
        return geom;
    }
}


