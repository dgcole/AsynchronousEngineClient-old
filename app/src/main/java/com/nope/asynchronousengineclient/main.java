package com.nope.asynchronousengineclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class main extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void buttonPress (View v) {
        ArrayList<String> info = getServerAddress();
        new TCPClient().execute("handshake", info.get(0), info.get(1));
    }

    private ArrayList<String> getServerAddress() {
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
}
