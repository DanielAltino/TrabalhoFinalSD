package com.example.chamadoincidentesservidor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SaveActivity extends AppCompatActivity {

    EditText comoFoiResolvido;
    ImageView anexo;
    Spinner spinner;
    Button buttonSave;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    long IDBanco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);

        init();


        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.arraySpinner, android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(SaveActivity.this, MainActivity.class);
                startActivity(it);
            }
        });
    }

    void init() {
        comoFoiResolvido = findViewById(R.id.editTextComoFoiResolvido);
        spinner = findViewById(R.id.spinnerSetor);
        buttonSave = findViewById(R.id.buttonSalvar);
    }

    private void inicializarBD() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference().child("Registo_Incidentes_Salvos");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                IDBanco = dataSnapshot.getChildrenCount();
                //databaseReference.child(String.valueOf(IDBanco)).setValue(requisicao);

                //Toast.makeText(MainActivity.this, "Dados inceridos", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
