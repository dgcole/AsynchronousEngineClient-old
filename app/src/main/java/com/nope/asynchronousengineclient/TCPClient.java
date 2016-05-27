package com.nope.asynchronousengineclient;

import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.TextView;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;

public class TCPClient extends AsyncTask<String, Integer, Boolean> {

    TextView view;
    String out;
    int lowerBound;
    int upperBound;
    int fileLength;

    public TCPClient(TextView v) {
        super();
        view = v;
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
            outToServer.writeBytes("insertidlater\n");
            outToServer.writeBytes(word + '\n');
            out = inFromServer.readLine();
            if (word.toUpperCase().equals(out)) {
                System.out.println("Contact established.");

                success = true;
            }
            lowerBound = Integer.parseInt(inFromServer.readLine());
            upperBound = Integer.parseInt(inFromServer.readLine());
            fileLength = Integer.parseInt(inFromServer.readLine());
            System.out.println(lowerBound + " to " + upperBound);
            System.out.println("File Length: " + fileLength);

            FileOutputStream fos = new FileOutputStream(//TODO; file?);
            byte[] buffer = new byte[4096];

            int read = 0;
            int totalRead = 0;
            int remaining = fileLength;
            while((read = dis.read(buffer, 0, Math.min(buffer.length, remaining))) > 0) {
                totalRead += read;
                remaining -= read;
                System.out.println("Read: " + totalRead + " bytes.");
                fos.write(buffer, 0, read);
            }

        } catch (Exception E) {
            E.printStackTrace();
            return false;
        }
        return success;
    }

    @Override
    protected void onPreExecute() {
        view.setText("Attempting to connect.");
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if (out == null) {
            view.setText("Failed to connect.");
        } else {
            view.setText("From Server: " + out);
        }
    }
}
