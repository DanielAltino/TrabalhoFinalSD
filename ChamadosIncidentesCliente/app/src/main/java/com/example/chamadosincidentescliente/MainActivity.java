package com.example.chamadosincidentescliente;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chamadosincidentescliente.modelo.Requisicao;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Constants;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EventListener;
import java.util.Locale;
import java.util.UUID;


public class MainActivity extends AppCompatActivity {

    Spinner spinner_Responsaveis;
    ImageView imageCam;
    Button send;
    EditText editTextDescription, editTextName, editTextEmail;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    StorageReference ImagePath;

    ProgressDialog mProgress;

    String pathToFile;

    Requisicao requisicao;

    private Uri uriFoto;

    Bitmap image;

    long IDBanco;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 0);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }

        init();

        inicializarBD();

        requisicao = new Requisicao();

        imageCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tirarFoto();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                requisicao.setSetor(spinner_Responsaveis.getSelectedItem().toString());
                requisicao.setNome(editTextName.getText().toString());
                requisicao.setEmail(editTextEmail.getText().toString());
                requisicao.setDescricao(editTextDescription.getText().toString());
               // requisicao.setFoto(ImagePath.toString());
                //databaseReference.child("ID001").setValue(requisicao);
                databaseReference.child(String.valueOf(IDBanco)).setValue(requisicao);



                try {
                    sendGroup();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                limparCampos();
            }
        });

    }

    private void sendGroup() throws IOException {


        String msg = "Hello";
        InetAddress group = InetAddress.getByName("230.0.0.0");
        MulticastSocket s = new MulticastSocket(4321);
        s.joinGroup(group);
        byte[] bytes = msg.getBytes(StandardCharsets.UTF_8);
        DatagramPacket hi = new DatagramPacket(bytes, bytes.length,
                group, 6789);
        s.send(hi);
       /* // get their responses!
        byte[] buf = new byte[1000];
        DatagramPacket recv = new DatagramPacket(buf, buf.length);
        s.receive(recv);

        String msgDecode  = new String(buf, "UTF-8");

        Toast.makeText(this, msgDecode, Toast.LENGTH_SHORT).show();

        // OK, I'm done talking - leave the group...
        s.leaveGroup(group);
*/


        /*
        int groupPort = 6789;
        InetAddress groupAddr = null;
        try {
            groupAddr = InetAddress.getByName("200.235.86.56");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        MulticastSocket sock = null;
        try {
            sock = new MulticastSocket();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte buf[] = new byte[10];
        for (int i=0; i< buf.length ; i++)
            buf[i] = (byte)i;

        DatagramPacket packet = new DatagramPacket(buf, buf.length,
                groupAddr, groupPort);

        Byte ttl = 1;
        sock.send(packet);




        sock.joinGroup(groupAddr);

        //byte[] buf2 = new byte[10];
        //DatagramPacket recv = new DatagramPacket(buf2, buf2.length);
        //sock.receive(recv);

        Toast.makeText(this, buf.toString(), Toast.LENGTH_SHORT).show();


        sock.close();*/

    }

    private void limparCampos() {
        editTextDescription.setText("");
        editTextName.setText("");
        editTextEmail.setText("");
    }

    private void inicializarBD() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference().child("Incidentes");

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


    void init() {

        spinner_Responsaveis = findViewById(R.id.spinner);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.arraySpinnerResposaveis, android.R.layout.simple_dropdown_item_1line);
        spinner_Responsaveis.setAdapter(adapter);
        send = findViewById(R.id.button);
        imageCam = findViewById(R.id.imageView_Cam);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);

        mProgress = new ProgressDialog(this);
    }

    public void tirarFoto() {
        Intent intentFoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //intentFoto.setType("image/*");
        if (intentFoto.resolveActivity(getPackageManager()) != null) {
            File fotoFile = null;
            fotoFile = createFotoFile();

            if (fotoFile != null) {
                pathToFile = fotoFile.getAbsolutePath();
                uriFoto = FileProvider.getUriForFile(MainActivity.this, "com.example.chamadosincidentescliente", fotoFile);


                /*ImagePath = FirebaseStorage.getInstance().getReference().child("Incidente").child(uriFoto.getLastPathSegment());
                ImagePath.putFile(uriFoto).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(MainActivity.this, "Imagem carregada com sucesso", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Imagem não carregada", Toast.LENGTH_SHORT).show();

                    }
                });*/


                intentFoto.putExtra(MediaStore.EXTRA_OUTPUT, uriFoto);
                startActivityForResult(intentFoto, 1);
            }
        }
    }


    public File createFotoFile() {
        String name = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            image = File.createTempFile(name, ".jpg", storageDir);
        } catch (Exception e) {
        }

        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {



            Uri uri = data.getData();

            //Bitmap imgBitMap = BitmapFactory.decodeFile(pathToFile);
            //imageCam.setImageBitmap(imgBitMap);



            ImagePath = FirebaseStorage.getInstance().getReference().child("Incidente").child(uriFoto.getLastPathSegment());

            ImagePath.putFile(uriFoto).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(MainActivity.this, "Imagem carregada com sucesso", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this, "Imagem não carregada", Toast.LENGTH_SHORT).show();

                }
            });


        }


    }


    private void salvarArquivos(){

        File folder = new File(Environment.getExternalStorageDirectory() + "Pictures/Incidentes");
        if (folder.exists()){
            folder.mkdir();
        }
    }


}


