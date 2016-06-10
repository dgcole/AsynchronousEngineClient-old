package com.nope.asynchronousengineclient;

import android.os.Environment;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class main extends AppCompatActivity
{
    @Override
    //@use this is for things
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void buttonPress (View v) throws Exception {
        //Starts a new thread which connects to the server and does things when button is pressed.
        String androidId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        new TCPClient(Environment.getExternalStorageDirectory(), this, androidId).execute("handshake");
    }


}
