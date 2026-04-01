package com.example.appishume;

import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.appishume.entidades.DBAccess;
import com.example.appishume.entidades.Evento;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Calendario extends AppCompatActivity {

  CalendarView calendarView;
  ListView lsvEventosDia;
  TextView tvFechaSeleccionada, tvSinEventos;
  Context context = this;
  DBAccess acceso;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    EdgeToEdge.enable(this);
    setContentView(R.layout.activity_calendario);
    ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
      Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
      // v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
      return insets;
    });

    calendarView = findViewById(R.id.calendarView);
    lsvEventosDia = findViewById(R.id.lsvEventosDia);
    tvFechaSeleccionada = findViewById(R.id.tvFechaSeleccionada);
    tvSinEventos = findViewById(R.id.tvSinEventos);

    acceso = new DBAccess(context);

    // Mostrar eventos del día actual al abrir la pantalla
    String hoy = obtenerFechaFormateada(System.currentTimeMillis());
    mostrarEventosDia(hoy);

    calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
      // month es 0-indexado en Android, por eso sumamos 1
      String fechaSeleccionada = String.format(Locale.getDefault(),
        "%02d/%02d/%04d", dayOfMonth, month + 1, year);
      mostrarEventosDia(fechaSeleccionada);
    });
  }

  private void mostrarEventosDia(String fecha) {
    tvFechaSeleccionada.setText("Eventos del " + fecha);

    ArrayList<Evento> eventos = acceso.listarEventosPorFecha(fecha);
    ArrayList<String> items = new ArrayList<>();

    if (eventos.isEmpty()) {
      tvSinEventos.setVisibility(TextView.VISIBLE);
      lsvEventosDia.setVisibility(ListView.GONE);
    } else {
      tvSinEventos.setVisibility(TextView.GONE);
      lsvEventosDia.setVisibility(ListView.VISIBLE);

      for (Evento e : eventos) {
        String nombreCliente = acceso.obtenerNombreCompleto(e.getIdCliente());
        items.add("Tipo: " + e.getTipoEvento() +
          "\nCliente: " + nombreCliente +
          "\nUbicación: " + e.getUbicacion() +
          "\nDuración: " + e.getDuracion() + " hrs");
      }

      ArrayAdapter<String> adapter = new ArrayAdapter<>(context,
        android.R.layout.simple_list_item_1, items);
      lsvEventosDia.setAdapter(adapter);
    }
  }

  private String obtenerFechaFormateada(long milisegundos) {
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    return sdf.format(new Date(milisegundos));
  }
}
