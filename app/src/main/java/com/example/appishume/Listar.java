package com.example.appishume;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.appishume.entidades.DBAccess;
import com.example.appishume.entidades.Evento;

import java.util.ArrayList;

public class Listar extends AppCompatActivity {

  ListView lsvEventos;
  Context context = this;
  DBAccess acceso;
  ArrayList<Evento> listaEventos;
  ArrayList<String> listaFinal;

  private void loadUI() {
    lsvEventos = findViewById(R.id.lsvEventos);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    EdgeToEdge.enable(this);
    setContentView(R.layout.activity_listar);
    ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
      Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
      // v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
      return insets;
    });

    loadUI();
    acceso = new DBAccess(this);
    obtenerDatos();
  }

  private void obtenerDatos() {
    listaEventos = acceso.listarEventos();
    listaFinal = new ArrayList<>();

    Log.i("Total eventos: ", String.valueOf(listaEventos.size()));

    for (int i = 0; i < listaEventos.size(); i++) {
      String nombreCliente = acceso.obtenerNombreCompleto(listaEventos.get(i).getIdCliente());

      listaFinal.add("ID: " + listaEventos.get(i).getIdEvento() +
        " | Tipo: " + listaEventos.get(i).getTipoEvento() +
        " | Fecha: " + listaEventos.get(i).getFechaEvento() +
        " | Ubicación: " + listaEventos.get(i).getUbicacion() +
        " | Duración: " + listaEventos.get(i).getDuracion() + " hrs" +
        " | Cliente: " + nombreCliente);
    }

    ArrayAdapter adapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, listaFinal);
    lsvEventos.setAdapter(adapter);
  }
}
