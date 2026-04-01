package com.example.appishume.entidades;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DBAccess extends SQLiteOpenHelper {

  private static final String NOMBRE_BD = "ishume.db";
  private static final int VERSION_BD = 1;

  private static final String TABLA_CLIENTES = "CREATE TABLE clientes(" +
    "idCliente INTEGER PRIMARY KEY AUTOINCREMENT, " +
    "nombre TEXT NOT NULL, " +
    "apellidos TEXT NOT NULL, " +
    "telefono TEXT NOT NULL)";

  private static final String TABLA_EVENTOS = "CREATE TABLE eventos(" +
    "idEvento INTEGER PRIMARY KEY AUTOINCREMENT, " +
    "tipo_evento TEXT NOT NULL, " +
    "fecha_evento TEXT NOT NULL, " +
    "ubicacion TEXT NOT NULL, " +
    "duracion INTEGER NOT NULL, " +
    "idCliente INTEGER, " +
    "FOREIGN KEY (idCliente) REFERENCES clientes(idCliente))";

  public DBAccess(@Nullable Context context) {
    super(context, NOMBRE_BD, null, VERSION_BD);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    Log.i("DBAccess", "Creando tablas...");
    db.execSQL(TABLA_CLIENTES);
    db.execSQL(TABLA_EVENTOS);
    Log.i("DBAccess", "Tablas creadas exitosamente");
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("DROP TABLE IF EXISTS eventos");
    db.execSQL("DROP TABLE IF EXISTS clientes");
    onCreate(db);
  }


  public long agregarCliente(Cliente cliente) {
    SQLiteDatabase db = getWritableDatabase();
    long idCliente = -1;

    if (db != null) {
      ContentValues parametros = new ContentValues();
      parametros.put("nombre", cliente.getNombre());
      parametros.put("apellidos", cliente.getApellidos());
      parametros.put("telefono", cliente.getTelefono());

      idCliente = db.insert("clientes", null, parametros);
      Log.i("PK Cliente obtenido", String.valueOf(idCliente));
      db.close();
    }
    return idCliente;
  }

  public int obtenerOCrearCliente(String nombre, String apellidos, String telefono) {
    SQLiteDatabase db = getReadableDatabase();
    int idCliente = -1;

    String[] paramConsulta = {nombre, apellidos, telefono};
    Cursor cursor = db.query("clientes", new String[]{"idCliente"},
      "nombre=? AND apellidos=? AND telefono=?", paramConsulta, null, null, null);

    if (cursor.moveToFirst()) {
      idCliente = cursor.getInt(0);
      Log.i("DBAccess", "Cliente encontrado: " + idCliente);
    }
    cursor.close();
    db.close();

    if (idCliente == -1) {
      Cliente nuevoCliente = new Cliente(nombre, apellidos, telefono);
      idCliente = (int) agregarCliente(nuevoCliente);
      Log.i("DBAccess", "Cliente creado: " + idCliente);
    }

    return idCliente;
  }

  public String obtenerNombreCompleto(int idCliente) {
    SQLiteDatabase db = getReadableDatabase();
    String nombreCompleto = "";
    String[] paramConsulta = {String.valueOf(idCliente)};

    Cursor cursor = db.query("clientes", new String[]{"nombre", "apellidos"},
      "idCliente=?", paramConsulta, null, null, null);

    if (cursor.moveToFirst()) {
      nombreCompleto = cursor.getString(0) + " " + cursor.getString(1);
    }
    cursor.close();
    db.close();
    return nombreCompleto;
  }

  public String obtenerTelefonoCliente(int idCliente) {
    SQLiteDatabase db = getReadableDatabase();
    String telefono = "";
    String[] paramConsulta = {String.valueOf(idCliente)};

    Cursor cursor = db.query("clientes", new String[]{"telefono"},
      "idCliente=?", paramConsulta, null, null, null);

    if (cursor.moveToFirst()) {
      telefono = cursor.getString(0);
    }
    cursor.close();
    db.close();
    return telefono;
  }


  public long agregarEvento(Evento evento) {
    SQLiteDatabase db = getWritableDatabase();
    long idEvento = -1;

    if (db != null) {
      ContentValues parametros = new ContentValues();
      parametros.put("tipo_evento", evento.getTipoEvento());
      parametros.put("fecha_evento", evento.getFechaEvento());
      parametros.put("ubicacion", evento.getUbicacion());
      parametros.put("duracion", evento.getDuracion());
      parametros.put("idCliente", evento.getIdCliente());

      idEvento = db.insert("eventos", null, parametros);
      Log.i("PK Evento obtenido", String.valueOf(idEvento));
      db.close();
    }
    return idEvento;
  }

  public ArrayList<Evento> listarEventos() {
    SQLiteDatabase db = getReadableDatabase();
    ArrayList<Evento> lista = new ArrayList<>();
    Evento evento;

    Cursor cursor = db.rawQuery("SELECT * FROM eventos", null);
    Log.i("DBAccess", "Eventos encontrados: " + cursor.getCount());

    while (cursor.moveToNext()) {
      evento = new Evento();
      evento.setIdEvento(cursor.getInt(0));
      evento.setTipoEvento(cursor.getString(1));
      evento.setFechaEvento(cursor.getString(2));
      evento.setUbicacion(cursor.getString(3));
      evento.setDuracion(cursor.getInt(4));
      evento.setIdCliente(cursor.getInt(5));
      lista.add(evento);
    }

    cursor.close();
    db.close();
    return lista;
  }

  public int eliminarEvento(int idEvento) {
    SQLiteDatabase db = getWritableDatabase();
    String[] parametros = {String.valueOf(idEvento)};
    int eliminados = db.delete("eventos", "idEvento=?", parametros);
    db.close();
    return eliminados;
  }

  public boolean actualizarEvento(Evento evento) {
    SQLiteDatabase db = getWritableDatabase();
    String[] paramConsulta = {String.valueOf(evento.getIdEvento())};

    ContentValues parametros = new ContentValues();
    parametros.put("tipo_evento", evento.getTipoEvento());
    parametros.put("fecha_evento", evento.getFechaEvento());
    parametros.put("ubicacion", evento.getUbicacion());
    parametros.put("duracion", evento.getDuracion());
    parametros.put("idCliente", evento.getIdCliente());

    int filasAfectadas = db.update("eventos", parametros, "idEvento=?", paramConsulta);
    db.close();
    return filasAfectadas > 0;
  }

  public Evento buscarEvento(int id) {
    SQLiteDatabase db = getReadableDatabase();
    Evento evento = null;
    String[] paramConsulta = {String.valueOf(id)};
    String[] camposObtener = {"tipo_evento", "fecha_evento", "ubicacion", "duracion", "idCliente"};

    try {
      Cursor resultado = db.query("eventos", camposObtener, "idEvento=?",
        paramConsulta, null, null, null);

      if (resultado.moveToFirst()) {
        Log.i("Tipo Evento", resultado.getString(0));
        evento = new Evento();
        evento.setIdEvento(id);
        evento.setTipoEvento(resultado.getString(0));
        evento.setFechaEvento(resultado.getString(1));
        evento.setUbicacion(resultado.getString(2));
        evento.setDuracion(resultado.getInt(3));
        evento.setIdCliente(resultado.getInt(4));
      } else {
        Log.i("Resultado :", "NO ENCONTRADO");
      }
      resultado.close();
    } catch (Exception err) {
      Log.e("Error busqueda: ", err.toString());
    }
    db.close();
    return evento;
  }

  public ArrayList<Evento> listarEventosPorFecha(String fecha) {
    SQLiteDatabase db = getReadableDatabase();
    ArrayList<Evento> lista = new ArrayList<>();

    Cursor cursor = db.query("eventos", null,
      "fecha_evento=?", new String[]{fecha}, null, null, null);

    while (cursor.moveToNext()) {
      Evento evento = new Evento();
      evento.setIdEvento(cursor.getInt(0));
      evento.setTipoEvento(cursor.getString(1));
      evento.setFechaEvento(cursor.getString(2));
      evento.setUbicacion(cursor.getString(3));
      evento.setDuracion(cursor.getInt(4));
      evento.setIdCliente(cursor.getInt(5));
      lista.add(evento);
    }

    cursor.close();
    db.close();
    return lista;
  }
}
