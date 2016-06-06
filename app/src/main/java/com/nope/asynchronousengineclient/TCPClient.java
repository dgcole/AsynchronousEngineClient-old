package com.nope.asynchronousengineclient;

import android.os.AsyncTask;
import android.widget.TextView;

import org.python.core.PyObject;
import org.python.core.PyType;
import org.python.util.PythonInterpreter;


import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;

public class TCPClient extends AsyncTask<String, Integer, Boolean> {

    TextView view;
    String out;
    File dir;
    int lowerBound;
    int upperBound;
    int fileLength;

    public TCPClient(TextView v, File f) {
        super();
        view = v;
        dir = f;
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


            File outFile = new File(dir, "AsynchronousEngine/program.py");
            FileOutputStream fos = new FileOutputStream(outFile);

            byte[] buffer = new byte[fileLength];
            for(int i = 0; i < fileLength; i++) {
                String temp = inFromServer.readLine();
                byte data = Byte.parseByte(temp);
                fos.write(data);
            }

            System.out.println("Done reading.");
            fos.close();
            runProgram(outFile);
        } catch (Exception E) {
            E.printStackTrace();
            return false;
        }
        return success;
    }

    protected void runProgram(File f) throws IOException {
        String pythonFilePath = f.getPath();
        PythonInterpreter interp;
        BufferedReader terminal = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("hello");
        interp = new PythonInterpreter();

        ArrayList<String> pyCode = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line;
            while(!(line = br.readLine()).isEmpty()) {
                pyCode.add(line);
                System.out.println(line);
            }
        } catch (Exception E){

        }
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
