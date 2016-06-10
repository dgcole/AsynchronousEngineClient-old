package com.nope.asynchronousengineclient;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;

public class TCPClient extends AsyncTask<String, Integer, Boolean> {

    String out;
    File dir;
    int lowerBound;
    int upperBound;
    int fileLength;
    AppCompatActivity appMain;
    String androidId;

    public TCPClient(File f, main m, String id) {
        super();
        dir = f;
        appMain = m;
        androidId = id;
    }

    protected ArrayList<String> getServerAddress() {
        ArrayList<String> details = new ArrayList<>();
        try {
            URL infourl = new URL("http://www.expert700.me/serverinfo.php/");
            BufferedReader in = new BufferedReader( new InputStreamReader(infourl.openStream()));
            String input = in.readLine();

            int split = input.indexOf(":");
            String ip = input.substring(0, split);
            String port = input.substring(split + 1, input.length());
            details.add(0, ip);
            details.add(1, port);
        } catch (Exception e) {
            e.printStackTrace();
            details.add(0, "173.76.28.219");
            details.add(1, "6789");
        }
        return details;
    }

    protected Boolean doInBackground(String... strings) {
        ArrayList<String> info = getServerAddress();
        String word = strings[0];
        String ip = info.get(0);
        int port = Integer.parseInt(info.get(1));
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
            DataInputStream dis = new DataInputStream(clientSocket.getInputStream());

            //TODO; Add id
            outToServer.writeBytes(androidId + "\n");
            outToServer.writeBytes(word + '\n');
            out = inFromServer.readLine();
            if (word.toUpperCase().equals(out)) {
                System.out.println("Contact established.");

                success = true;
            }
            lowerBound = Integer.parseInt(inFromServer.readLine());
            upperBound = Integer.parseInt(inFromServer.readLine());
            System.out.println("Range is from " + lowerBound + " to " + upperBound + ".");

            ArrayList<Integer> primes = new primeCalculator(lowerBound, upperBound).calculate();
            primes.add(-1);
            System.out.println("Done with primes.");

            for (Integer i : primes) {
                System.out.println("Prime: " + i);
                outToServer.writeBytes(i.toString() + "\n");
            }
        } catch (Exception E) {
            E.printStackTrace();
            return false;
        }
        return success;
    }

    @Override
    protected void onPreExecute() {
        showToast("Attempting to connect.", Toast.LENGTH_SHORT);
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if (out == null) {
            showToast("Failed to connect.", Toast.LENGTH_LONG);
        } else {
            showToast("From Server: " + out, Toast.LENGTH_SHORT);
        }
    }

    protected void showToast(final String s, final int l) {
        appMain.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(appMain, s, l).show();
            }
        });
    }
}
