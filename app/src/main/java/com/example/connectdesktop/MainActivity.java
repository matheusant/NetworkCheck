package com.example.connectdesktop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    private EditText edtTexto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtTexto = findViewById(R.id.edtTexto);
    }
    public void enviarDados(View v){
        String basePath = "/storage/emulated/0";
        /*String message = edtTexto.getText().toString();
        BackgroundTask task = new BackgroundTask();
        task.execute(message);*/
        File file = new File(basePath+"/PASTA_SQL/local_04_ILHA_01_foto_1.jpg");
        Tasks tasks = new Tasks();
        tasks.execute(file);
    }

    class BackgroundTask extends AsyncTask<String, Void, Void>{

        Socket s;
        PrintWriter writer;

        @Override
        protected Void doInBackground(String... strings) {
            try {

               String message = strings[0];
               s = new Socket("192.168.0.70", 7800);
               writer = new PrintWriter(s.getOutputStream());
               writer.write(message);
               writer.flush();
               writer.close();
               s.close();

            }catch (IOException e){
                e.printStackTrace();
            }

            return null;
        }
    }

    class Tasks extends AsyncTask<File, Void, Void>{
        Socket s;

        @Override
        protected Void doInBackground(File... files) {

            try {
                File file = files[0];
                s = new Socket("192.168.0.70", 7800);
                FileInputStream fis = new FileInputStream(file);
                OutputStream outputStream = s.getOutputStream();
                byte[] b = new byte[2002];
                fis.read(b, 0, b.length);
                outputStream.write(b,0, b.length);
            }catch (IOException e){
                e.printStackTrace();
            }


            return null;
        }
    }

    public void isOnline(View v){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info != null && info.isConnected()){
            Toast.makeText(getApplicationContext(), "Conectado", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(), "Sem conexão", Toast.LENGTH_SHORT).show();
        }
    }
}