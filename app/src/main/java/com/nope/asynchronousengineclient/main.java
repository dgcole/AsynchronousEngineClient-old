package com.nope.asynchronousengineclient;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;

public class main extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    public void buttonPress (View v) {
        String word = "HANDSHAKE";
        try {
            Socket clientSocket = new Socket();
            clientSocket.connect(new InetSocketAddress("172.21.38.107", 6789), 10000);

            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            outToServer.writeBytes(word);
            if (inFromServer.toString().toUpperCase().equals(word)) {
                Log.d("Result", "Success");
            }

        } catch (Exception E) {
            Log.d("Something bad happened.", E.toString());
        }
    }
}
