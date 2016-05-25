package com.nope.asynchronousengineclient;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;

public class TCPClient extends AsyncTask<String, Integer, Boolean> {
    protected Boolean doInBackground(String... strings) {
        String word = strings[0];
        String ip = strings[1];
        int port = Integer.parseInt(strings[2]);
        Boolean success = false;

        System.out.println("IP: " + ip);
        System.out.println("Port: " + port);
        try {
            Socket clientSocket = new Socket();
            System.out.println("Attempting to connect.");
            clientSocket.connect(new InetSocketAddress(ip, port), 10000);
            System.out.println("Connected... probably");
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            outToServer.writeBytes(word + '\n');
            if (word.toUpperCase().equals(inFromServer.readLine().toUpperCase())) {
                System.out.println("Contact established.");
                success = true;
            }
        } catch (Exception E) {
            E.printStackTrace();
            return false;
        }
        return success;
    }
}
