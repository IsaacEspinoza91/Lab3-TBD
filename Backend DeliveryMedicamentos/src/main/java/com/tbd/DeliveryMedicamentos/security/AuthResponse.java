package com.tbd.DeliveryMedicamentos.security;


public class AuthResponse {

    private String token;
    private String tipoUsuario;
    private String nombreUsuario;
    private Integer idUsuario;


    // Constructor
    public AuthResponse() {
    }

    public AuthResponse(String token, String tipoUsuario, String nombreUsuario, Integer idUsuario) {
        this.token = token;
        this.tipoUsuario = tipoUsuario;
        this.nombreUsuario = nombreUsuario;
        this.idUsuario = idUsuario;
    }


    // Gettes y Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    // Builder
    public static class Builder {
        private String token;
        private String tipoUsuario;
        private String nombreUsuario;
        private Integer idUsuario;

        public Builder token(String token, String tipoUsuario, String nombreUsuario, Integer idUsuario) {
            this.token = token;
            this.tipoUsuario = tipoUsuario;
            this.nombreUsuario = nombreUsuario;
            this.idUsuario = idUsuario;
            return this;
        }

        public AuthResponse build() {
            AuthResponse response = new AuthResponse();
            response.setToken(this.token);
            response.setTipoUsuario(this.tipoUsuario);
            response.setNombreUsuario(this.nombreUsuario);
            response.setIdUsuario(this.idUsuario);
            return response;
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
