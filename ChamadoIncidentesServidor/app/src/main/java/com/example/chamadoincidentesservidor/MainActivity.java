package com.example.chamadoincidentesservidor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;



public class MainActivity extends AppCompatActivity {

    Button ir;
    Spinner spinnerSetor;
    private ListView listView;
    String msgDecode = "xxx";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        init();


        ArrayList<String> stringListView = preencherListView();

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.arraySpinnerSetor, android.R.layout.simple_dropdown_item_1line);
        spinnerSetor.setAdapter(adapter);

        ArrayAdapter<String> adapterListView = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stringListView);
        listView.setAdapter(adapterListView);


    }
    @Override
    protected void onPause(){
        super.onPause();

       // startService(new Intent(getBaseContext(), TestService.class));


        //Toast.makeText(this, "Background", Toast.LENGTH_SHORT).show();
        
    }

    void init(){

        spinnerSetor = findViewById(R.id.spinnerSetor);
        listView = findViewById(R.id.listView);
    }


    public void receiveGroup() throws IOException {

        InetAddress group = InetAddress.getByName("230.0.0.0");
        MulticastSocket s = new MulticastSocket(4321);
        s.joinGroup(group);

        // get their responses!
        byte[] buf = new byte[1000];
        DatagramPacket recv = new DatagramPacket(buf, buf.length);
        String msgDecode= null;
        while(msgDecode==null)
        {
            s.receive(recv);
            msgDecode  = new String(buf, "UTF-8");
            Toast.makeText(this, msgDecode, Toast.LENGTH_SHORT).show();
            System.out.println("Address: " + msgDecode);
        }

        // OK, I'm done talking - leave the group...

        Toast.makeText(this, msgDecode, Toast.LENGTH_SHORT).show();
        s.leaveGroup(group);


    }

    private ArrayList<String> preencherListView(){
        ArrayList<String> dados = new ArrayList<String>();
        dados.add(msgDecode);
        dados.add("Lista 2");
        dados.add("Lista 3");
        dados.add("Lista 4");
        dados.add("Lista 5");
        dados.add("Lista 5");
        dados.add("Lista 5");
        dados.add("Lista 5");
        dados.add("Lista 5");
        dados.add("Lista 5");
        dados.add("Lista 5");
        dados.add("Lista 5");

        return dados;
    }

}
