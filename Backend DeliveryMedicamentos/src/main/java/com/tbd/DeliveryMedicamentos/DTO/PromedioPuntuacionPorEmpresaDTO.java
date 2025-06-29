package com.tbd.DeliveryMedicamentos.DTO;

import org.springframework.data.annotation.Id;

public class PromedioPuntuacionPorEmpresaDTO {

    //Campo 'id' para mapear el _id resultante de la agregación
    @Id
    private String id;

    private Double promedioPuntuacion;

    // Constructor sin argumentos
    public PromedioPuntuacionPorEmpresaDTO() {
    }

    // Constructor con argumentos para inicializar los campos
    public PromedioPuntuacionPorEmpresaDTO(String id, Double promedioPuntuacion) {
        this.id = id;
        this.promedioPuntuacion = promedioPuntuacion;
    }

    // Getters y setters
    public String getEmpresaId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getPromedioPuntuacion() {
        return promedioPuntuacion;
    }

    public void setPromedioPuntuacion(Double promedioPuntuacion) {
        this.promedioPuntuacion = promedioPuntuacion;
    }
}