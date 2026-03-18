package com.example.appishume.entidades;

public class Cliente {
  private int idCliente;
  private String nombre;
  private String apellidos;
  private String telefono;

  // Constructor vacío
  public Cliente() {
  }

  // Constructor sin ID (para registrar)
  public Cliente(String nombre, String apellidos, String telefono) {
    this.nombre = nombre;
    this.apellidos = apellidos;
    this.telefono = telefono;
  }

  // Constructor completo (para actualizar)
  public Cliente(int idCliente, String nombre, String apellidos, String telefono) {
    this.idCliente = idCliente;
    this.nombre = nombre;
    this.apellidos = apellidos;
    this.telefono = telefono;
  }

  // Getters y Setters
  public int getIdCliente() {
    return idCliente;
  }

  public void setIdCliente(int idCliente) {
    this.idCliente = idCliente;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getApellidos() {
    return apellidos;
  }

  public void setApellidos(String apellidos) {
    this.apellidos = apellidos;
  }

  public String getTelefono() {
    return telefono;
  }

  public void setTelefono(String telefono) {
    this.telefono = telefono;
  }
}
