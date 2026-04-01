package com.example.appishume;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

  Button btnActRegistrar, btnActListar, btnActBuscar, btnActCalendario;

  private void loadUI() {
    btnActRegistrar = findViewById(R.id.btnActRegistrar);
    btnActListar = findViewById(R.id.btnActListar);
    btnActBuscar = findViewById(R.id.btnActBuscar);
    btnActCalendario = findViewById(R.id.btnActCalendario);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    EdgeToEdge.enable(this);
    setContentView(R.layout.activity_main);
    ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
      Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
      // v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
      return insets;
    });

    loadUI();

    btnActRegistrar.setOnClickListener(v -> {
      startActivity(new Intent(getApplicationContext(), Registrar.class));
    });
    btnActListar.setOnClickListener(v -> {
      startActivity(new Intent(getApplicationContext(), Listar.class));
    });
    btnActBuscar.setOnClickListener(v -> {
      startActivity(new Intent(getApplicationContext(), Buscar.class));
    });
    btnActCalendario.setOnClickListener(v -> {
      startActivity(new Intent(getApplicationContext(), Calendario.class));
    });
  }
}
