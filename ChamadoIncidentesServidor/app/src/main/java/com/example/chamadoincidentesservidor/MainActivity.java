package com.example.chamadoincidentesservidor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Set;


public class MainActivity extends AppCompatActivity {

    Button ir;
    Spinner spinnerSetor;
    private ListView listView;
    String msgDecode = "xxx";
    ArrayList<String> dados = new ArrayList<>();
    public ArrayAdapter adapterListView;



    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    ArrayList<String> listSetores;

    Setores setoresClass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setoresClass = new Setores();

        init();
        initFirebase();


        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.arraySpinnerSetor, android.R.layout.simple_dropdown_item_1line);
        spinnerSetor.setAdapter(adapter);


        spinnerSetor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                /*if(parent.getItemAtPosition(position).equals("Setor 1")){
                    adapterListView = ArrayAdapter.createFromResource(MainActivity.this,R.array.array_Setor_1, android.R.layout.simple_list_item_1);
                    listView.setAdapter(adapterListView);
                }
                if(parent.getItemAtPosition(position).equals("Setor 2")){
                    adapterListView = ArrayAdapter.createFromResource(MainActivity.this,R.array.array_Setor_2, android.R.layout.simple_list_item_1);
                    listView.setAdapter(adapterListView);
                }
                if(parent.getItemAtPosition(position).equals("Setor 3")){
                    adapterListView = ArrayAdapter.createFromResource(MainActivity.this,R.array.array_Setor_3, android.R.layout.simple_list_item_1);
                    listView.setAdapter(adapterListView);
                }*/


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void initFirebase() {

        FirebaseApp.initializeApp(MainActivity.this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Incidentes");

        listSetores = new ArrayList<>();

        adapterListView = new ArrayAdapter<>(this, R.layout.activity_incident, R.id.textViewDescription, listSetores);


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    setoresClass = ds.getValue(Setores.class);
                    listSetores.add(setoresClass.getSetor() + "\n\n" + setoresClass.getDescricao() + "\n\n" + setoresClass.getNome() + "\n\n" + setoresClass.getEmail());

                }
                listView.setAdapter(adapterListView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        // startService(new Intent(getBaseContext(), TestService.class));


        //Toast.makeText(this, "Background", Toast.LENGTH_SHORT).show();

    }

    void init() {

        spinnerSetor = findViewById(R.id.spinnerSetor);
        listView = findViewById(R.id.listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent it = new Intent(MainActivity.this, SaveActivity.class);
                startActivity(it);
            }
        });
    }


    public void receiveGroup() throws IOException {

        InetAddress group = InetAddress.getByName("230.0.0.0");
        MulticastSocket s = new MulticastSocket(4321);
        s.joinGroup(group);

        // get their responses!
        byte[] buf = new byte[1000];
        DatagramPacket recv = new DatagramPacket(buf, buf.length);
        String msgDecode = null;
        while (msgDecode == null) {
            s.receive(recv);
            msgDecode = new String(buf, "UTF-8");
            Toast.makeText(this, msgDecode, Toast.LENGTH_SHORT).show();
            System.out.println("Address: " + msgDecode);
        }

        // OK, I'm done talking - leave the group...

        Toast.makeText(this, msgDecode, Toast.LENGTH_SHORT).show();
        s.leaveGroup(group);


    }


}
