package com.tbd.DeliveryMedicamentos.DTO;

public class OpinionesPorHoraDTO {
    private int hora;
    private double promedioPuntuacion;
    private long totalOpiniones;

    public OpinionesPorHoraDTO() {}
    public OpinionesPorHoraDTO(int hora, double promedioPuntuacion, long totalOpiniones) {
        this.hora = hora;
        this.promedioPuntuacion = promedioPuntuacion;
        this.totalOpiniones = totalOpiniones;
    }

    // Getters y setters
    public int getHora() {
        return hora;
    }

    public void setHora(int hora) {
        this.hora = hora;
    }

    public double getPromedioPuntuacion() {
        return promedioPuntuacion;
    }

    public void setPromedioPuntuacion(double promedioPuntuacion) {
        this.promedioPuntuacion = promedioPuntuacion;
    }

    public long getTotalOpiniones() {
        return totalOpiniones;
    }

    public void setTotalOpiniones(long totalOpiniones) {
        this.totalOpiniones = totalOpiniones;
    }
}