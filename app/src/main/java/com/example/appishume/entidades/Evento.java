package com.example.appishume.entidades;

public class Evento {
  private int idEvento;
  private String tipoEvento;
  private String fechaEvento;
  private String ubicacion;
  private int duracion;
  private int idCliente;

  // Constructor vacío
  public Evento() {
  }

  // Constructor sin ID (para registrar)
  public Evento(String tipoEvento, String fechaEvento, String ubicacion, int duracion, int idCliente) {
    this.tipoEvento = tipoEvento;
    this.fechaEvento = fechaEvento;
    this.ubicacion = ubicacion;
    this.duracion = duracion;
    this.idCliente = idCliente;
  }

  // Constructor completo (para actualizar)
  public Evento(int idEvento, String tipoEvento, String fechaEvento, String ubicacion, int duracion, int idCliente) {
    this.idEvento = idEvento;
    this.tipoEvento = tipoEvento;
    this.fechaEvento = fechaEvento;
    this.ubicacion = ubicacion;
    this.duracion = duracion;
    this.idCliente = idCliente;
  }

  // Getters y Setters
  public int getIdEvento() {
    return idEvento;
  }

  public void setIdEvento(int idEvento) {
    this.idEvento = idEvento;
  }

  public String getTipoEvento() {
    return tipoEvento;
  }

  public void setTipoEvento(String tipoEvento) {
    this.tipoEvento = tipoEvento;
  }

  public String getFechaEvento() {
    return fechaEvento;
  }

  public void setFechaEvento(String fechaEvento) {
    this.fechaEvento = fechaEvento;
  }

  public String getUbicacion() {
    return ubicacion;
  }

  public void setUbicacion(String ubicacion) {
    this.ubicacion = ubicacion;
  }

  public int getDuracion() {
    return duracion;
  }

  public void setDuracion(int duracion) {
    this.duracion = duracion;
  }

  public int getIdCliente() {
    return idCliente;
  }

  public void setIdCliente(int idCliente) {
    this.idCliente = idCliente;
  }
}
