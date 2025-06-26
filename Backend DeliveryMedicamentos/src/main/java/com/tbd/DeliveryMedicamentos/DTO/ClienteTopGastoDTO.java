package com.tbd.DeliveryMedicamentos.DTO;

public class ClienteTopGastoDTO {
    private String nombre;
    private String apellido;
    private Long clienteId;
    private Double totalGastado;

    // Constructores
    public ClienteTopGastoDTO() {}

    public ClienteTopGastoDTO(String nombre, String apellido, Long clienteId, Double totalGastado) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.clienteId = clienteId;
        this.totalGastado = totalGastado;
    }

    // Getters y setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public Double getTotalGastado() {
        return totalGastado;
    }

    public void setTotalGastado(Double totalGastado) {
        this.totalGastado = totalGastado;
    }
}
