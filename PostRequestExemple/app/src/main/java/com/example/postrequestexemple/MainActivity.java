package com.example.postrequestexemple;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText emailEditText;
    private Button looginButton;

  private  String server="http://start.webpower.cf/test/auth/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameEditText=findViewById(R.id.editTextUserName);
        emailEditText=findViewById(R.id.editTextEmail);
        passwordEditText=findViewById(R.id.editTextPassword);
        LoginOnServer task=new LoginOnServer();

        try {

            String result=task.execute(server).get();


        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    private static class LoginOnServer extends AsyncTask< String,Void,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            String urlString = strings[0]; // URL to call
           // String data = strings[1]; //data to post

             URL url=null;
            HttpURLConnection urlConnection=null;

            try {
//=========================================================================
                url = new URL(urlString);
                 urlConnection = (HttpURLConnection) url.openConnection();
                 urlConnection.setRequestMethod("POST");
//=========================================================================
                OutputStream outputStream = urlConnection.getOutputStream();
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
                BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
                bufferedWriter.write("nikname=jerry&pasword=666666");
//===========================================================================

                urlConnection.connect();
            } catch (Exception e) {
                System.out.println(e.getMessage());
               String s=e.getMessage();
                Log.i("ex",e.getMessage());
;            }


            return null;
        }
    }
    }
