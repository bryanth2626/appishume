package com.example.appishume;

import android.app.DatePickerDialog;
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

import java.util.Calendar;
import java.util.Locale;

public class Registrar extends AppCompatActivity {

  EditText edtNombre, edtApellidos, edtTelefono, edtTipoEvento, edtFechaEvento, edtUbicacion, edtDuracion;
  Button btnGuardarEvento;
  Context context = this;
  DBAccess acceso;

  private void loadUI() {
    edtNombre = findViewById(R.id.edtNombre);
    edtApellidos = findViewById(R.id.edtApellidos);
    edtTelefono = findViewById(R.id.edtTelefono);
    edtTipoEvento = findViewById(R.id.edtTipoEvento);
    edtFechaEvento = findViewById(R.id.edtFechaEvento);
    edtUbicacion = findViewById(R.id.edtUbicacion);
    edtDuracion = findViewById(R.id.edtDuracion);
    btnGuardarEvento = findViewById(R.id.btnGuardarEvento);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    EdgeToEdge.enable(this);
    setContentView(R.layout.activity_registrar);
    ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
      Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
      // v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
      return insets;
    });

    loadUI();
    acceso = new DBAccess(context);

    // Al tocar el campo fecha abre el selector de calendario
    edtFechaEvento.setFocusable(false);
    edtFechaEvento.setClickable(true);
    edtFechaEvento.setOnClickListener(v -> abrirDatePicker());

    btnGuardarEvento.setOnClickListener(v -> { registrar(); });
  }

  private void abrirDatePicker() {
    Calendar calendario = Calendar.getInstance();
    int anio = calendario.get(Calendar.YEAR);
    int mes = calendario.get(Calendar.MONTH);
    int dia = calendario.get(Calendar.DAY_OF_MONTH);

    DatePickerDialog datePicker = new DatePickerDialog(context,
      (view, anioSel, mesSel, diaSel) -> {
        // Guardamos siempre en formato DD/MM/YYYY
        String fechaFormateada = String.format(Locale.getDefault(),
          "%02d/%02d/%04d", diaSel, mesSel + 1, anioSel);
        edtFechaEvento.setText(fechaFormateada);
      }, anio, mes, dia);

    datePicker.show();
  }

  private void registrar() {
    String nombre, apellidos, telefono, tipoEvento, fechaEvento, ubicacion;
    int duracion;

    if (edtNombre.getText().toString().isEmpty()) {
      edtNombre.setError("Complete este campo");
      edtNombre.requestFocus();
      return;
    }

    if (edtApellidos.getText().toString().isEmpty()) {
      edtApellidos.setError("Complete este campo");
      edtApellidos.requestFocus();
      return;
    }

    if (edtTelefono.getText().toString().isEmpty()) {
      edtTelefono.setError("Complete este campo");
      edtTelefono.requestFocus();
      return;
    }

    if (edtTipoEvento.getText().toString().isEmpty()) {
      edtTipoEvento.setError("Escriba el tipo de evento");
      edtTipoEvento.requestFocus();
      return;
    }

    if (edtFechaEvento.getText().toString().isEmpty()) {
      edtFechaEvento.setError("Este campo es obligatorio");
      edtFechaEvento.requestFocus();
      return;
    }

    if (edtUbicacion.getText().toString().isEmpty()) {
      edtUbicacion.setError("Complete este campo");
      edtUbicacion.requestFocus();
      return;
    }

    if (edtDuracion.getText().toString().isEmpty()) {
      edtDuracion.setError("Falta indicar duración");
      edtDuracion.requestFocus();
      return;
    }

    // Asignar a variables
    nombre = edtNombre.getText().toString();
    apellidos = edtApellidos.getText().toString();
    telefono = edtTelefono.getText().toString();
    tipoEvento = edtTipoEvento.getText().toString();
    fechaEvento = edtFechaEvento.getText().toString();
    ubicacion = edtUbicacion.getText().toString();
    duracion = Integer.parseInt(edtDuracion.getText().toString());

    // Confirmación al usuario
    AlertDialog.Builder builder = new AlertDialog.Builder(context);
    builder.setTitle("Registro de evento");
    builder.setMessage("¿Está seguro de continuar?");

    builder.setPositiveButton("Sí", (dialog, which) -> {
      // Obtener o crear cliente
      int idCliente = acceso.obtenerOCrearCliente(nombre, apellidos, telefono);

      if (idCliente == -1) {
        Toast.makeText(getApplicationContext(), "Error al registrar cliente", Toast.LENGTH_SHORT).show();
        return;
      }

      long idGenerado = acceso.agregarEvento(new Evento(tipoEvento, fechaEvento, ubicacion, duracion, idCliente));

      if (idGenerado == -1) {
        Toast.makeText(getApplicationContext(), "No se pudo guardar", Toast.LENGTH_SHORT).show();
        return;
      }

      Toast.makeText(getApplicationContext(), "Guardado con ID: " + String.valueOf(idGenerado), Toast.LENGTH_LONG).show();
      limpiarCampos();
    });

    builder.setNegativeButton("No", null);

    AlertDialog dialog = builder.create();
    dialog.show();
  }

  private void limpiarCampos() {
    edtNombre.setText("");
    edtApellidos.setText("");
    edtTelefono.setText("");
    edtTipoEvento.setText("");
    edtFechaEvento.setText("");
    edtUbicacion.setText("");
    edtDuracion.setText("");
    edtNombre.requestFocus();
  }
}
