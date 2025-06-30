package com.tbd.DeliveryMedicamentos.DTO;

public class ClienteSinCompraDTO {

    private String clienteId;

    // Constructor con argumentos
    public ClienteSinCompraDTO(String clienteId) {
        this.clienteId = clienteId;
    }

    // Constructor sin argumentos
    public ClienteSinCompraDTO() {
    }

    //Getters y setters
    public String getClienteId() {
        return clienteId;
    }

    public void setClienteId(String clienteId) {
        this.clienteId = clienteId;
    }
}