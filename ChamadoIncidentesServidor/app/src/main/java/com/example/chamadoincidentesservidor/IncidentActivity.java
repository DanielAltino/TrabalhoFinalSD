package com.example.chamadoincidentesservidor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class IncidentActivity extends AppCompatActivity {

    TextView textViewDescription, textViewName;
    Button buttonFecharChamado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incident);

        init();


        buttonFecharChamado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(IncidentActivity.this, SaveActivity.class);
                startActivity(it);
            }
        });
    }

    private void init(){


        textViewDescription = findViewById(R.id.textViewDescription);
        textViewName = findViewById(R.id.textViewName);
        buttonFecharChamado = findViewById(R.id.button_FecharChamado);

    }


}
