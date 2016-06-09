package com.nope.asynchronousengineclient;

import android.os.Environment;
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
        new TCPClient(Environment.getExternalStorageDirectory(), this).execute("handshake");
    }


}
