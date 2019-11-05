package com.example.chamadoincidentesservidor;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class TestService extends Service
{

    String msgDecode;
    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("TAG", "onStartCommand");
        // START_STICKY serve para executar seu serviço até que você pare ele, é reiniciado automaticamente sempre que termina



        try {
            InetAddress group = InetAddress.getByName("228.5.6.7");
            MulticastSocket s = new MulticastSocket(6789);
            s.joinGroup(group);

            // get their responses!
            byte[] buf = new byte[1000];
            DatagramPacket recv = new DatagramPacket(buf, buf.length);

            String received= "";
            while(received!=null)
            {
                s.receive(recv);
                msgDecode  = new String(buf, "UTF-8");
                Toast.makeText(this, received, Toast.LENGTH_SHORT).show();
                System.out.println("Address: " + received);
            }





            Toast.makeText(this, msgDecode, Toast.LENGTH_SHORT).show();

            // OK, I'm done talking - leave the group...
            s.leaveGroup(group);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public String getMsgDecode() {
        return msgDecode;
    }

    public void setMsgDecode(String msgDecode) {
        this.msgDecode = msgDecode;
    }
}