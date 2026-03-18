package com.example.appishume;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.appishume.entidades.DBAccess;
import com.example.appishume.entidades.Evento;

public class Buscar extends AppCompatActivity {

  EditText edtIDBuscado, edtNombreQ, edtApellidosQ, edtTelefonoQ, edtTipoEventoQ, edtFechaEventoQ, edtUbicacionQ, edtDuracionQ;
  Button btnBuscarID, btnEliminarEvento, btnActualizarEvento;
  Context context = this;
  DBAccess acceso;

  int idbuscado = -1;
  boolean encontrado = false;

  private void loadUI() {
    edtIDBuscado = findViewById(R.id.edtIDBuscado);
    edtNombreQ = findViewById(R.id.edtNombreQ);
    edtApellidosQ = findViewById(R.id.edtApellidosQ);
    edtTelefonoQ = findViewById(R.id.edtTelefonoQ);
    edtTipoEventoQ = findViewById(R.id.edtTipoEventoQ);
    edtFechaEventoQ = findViewById(R.id.edtFechaEventoQ);
    edtUbicacionQ = findViewById(R.id.edtUbicacionQ);
    edtDuracionQ = findViewById(R.id.edtDuracionQ);
    btnBuscarID = findViewById(R.id.btnBuscarID);
    btnEliminarEvento = findViewById(R.id.btnEliminarEvento);
    btnActualizarEvento = findViewById(R.id.btnActualizarEvento);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    EdgeToEdge.enable(this);
    setContentView(R.layout.activity_buscar);
    ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
      Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
      // v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
      return insets;
    });

    loadUI();

    btnBuscarID.setOnClickListener(v -> { encontrarEvento(); });
    btnEliminarEvento.setOnClickListener(v -> { eliminarEvento(); });
    btnActualizarEvento.setOnClickListener(v -> { actualizarEvento(); });

    acceso = new DBAccess(context);
  }

  private void notificar(String mensaje) {
    Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show();
  }

  private void actualizarEvento() {
    if (idbuscado == -1) {
      notificar("No indicó un ID");
      edtIDBuscado.requestFocus();
      return;
    }
    if (!encontrado) {
      notificar("No existe este evento");
      edtIDBuscado.requestFocus();
      return;
    }

    String nombre = edtNombreQ.getText().toString();
    String apellidos = edtApellidosQ.getText().toString();
    String telefono = edtTelefonoQ.getText().toString();
    int idCliente = acceso.obtenerOCrearCliente(nombre, apellidos, telefono);

    Evento evento = new Evento();
    evento.setIdEvento(idbuscado);
    evento.setTipoEvento(edtTipoEventoQ.getText().toString());
    evento.setFechaEvento(edtFechaEventoQ.getText().toString());
    evento.setUbicacion(edtUbicacionQ.getText().toString());
    evento.setDuracion(Integer.parseInt(edtDuracionQ.getText().toString()));
    evento.setIdCliente(idCliente);

    if (acceso.actualizarEvento(evento)) {
      notificar("Datos actualizados correctamente");
    }
  }

  private void eliminarEvento() {
    if (idbuscado == -1) {
      notificar("No indicó un ID");
      edtIDBuscado.requestFocus();
      return;
    }
    if (!encontrado) {
      notificar("No existe este evento");
      edtIDBuscado.requestFocus();
      return;
    }

    AlertDialog.Builder builder = new AlertDialog.Builder(context);
    builder.setTitle("Evento");
    builder.setMessage("¿Seguro de eliminar?");

    builder.setPositiveButton("Si", (x, y) -> {
      if (acceso.eliminarEvento(idbuscado) > 0) {
        resetUI();
        encontrado = false;
        edtIDBuscado.requestFocus();
        notificar("Eliminado correctamente");
      }
    });
    builder.setNegativeButton("No", null);
    AlertDialog dialog = builder.create();
    dialog.show();
  }

  private void encontrarEvento() {
    if (edtIDBuscado.getText().toString().isEmpty()) {
      edtIDBuscado.setError("Escriba un ID");
      edtIDBuscado.requestFocus();
      return;
    }

    idbuscado = Integer.parseInt(edtIDBuscado.getText().toString());
    Evento eventoBuscado = acceso.buscarEvento(idbuscado);

    if (eventoBuscado != null) {
      encontrado = true;
      edtTipoEventoQ.setText(eventoBuscado.getTipoEvento());
      edtFechaEventoQ.setText(eventoBuscado.getFechaEvento());
      edtUbicacionQ.setText(eventoBuscado.getUbicacion());
      edtDuracionQ.setText(String.valueOf(eventoBuscado.getDuracion()));

      String nombreCompleto = acceso.obtenerNombreCompleto(eventoBuscado.getIdCliente());
      String[] partesNombre = nombreCompleto.split(" ", 2);
      edtNombreQ.setText(partesNombre.length > 0 ? partesNombre[0] : "");
      edtApellidosQ.setText(partesNombre.length > 1 ? partesNombre[1] : "");
      edtTelefonoQ.setText(acceso.obtenerTelefonoCliente(eventoBuscado.getIdCliente()));
    } else {
      encontrado = false;
      resetUI();
      Toast.makeText(context, "No encontrado", Toast.LENGTH_SHORT).show();
    }
  }

  private void resetUI() {
    edtNombreQ.setText(null);
    edtApellidosQ.setText(null);
    edtTelefonoQ.setText(null);
    edtTipoEventoQ.setText(null);
    edtFechaEventoQ.setText(null);
    edtUbicacionQ.setText(null);
    edtDuracionQ.setText(null);
  }
}
