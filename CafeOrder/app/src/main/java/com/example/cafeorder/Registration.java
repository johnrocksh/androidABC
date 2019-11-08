package com.example.cafeorder;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

public class Registration extends AppCompatActivity {

    private static HashMap<String, String> postDataParams;
    private String registrationUrl = "http://start.webpower.cf/test/register/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
    }

    public void onClickSubmit(View view) {

    }

    public static class ServerRegistrationTask extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... strings) {

            URL url = null;
            HttpURLConnection conn;
            String response = "";

            {
                try {
                    url = new URL(strings[0]);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(15000);
                    conn.setConnectTimeout(15000);
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setRequestMethod("POST");

                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

                    writer.write(getPostDataString(postDataParams));
                    writer.flush();
                    writer.close();
                    os.close();

                    int responseCode = conn.getResponseCode();
                    if (responseCode == HttpsURLConnection.HTTP_OK) {

                        String line;
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                        while ((line = br.readLine()) != null) {
                            response += line;
                        }
                    } else {

                        response = "";
                    }
                    return response.toString();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


        }

        private String getPostDataString(HashMap<String, String> params) {
            String str = "";

            return str;
        }
    }
}
