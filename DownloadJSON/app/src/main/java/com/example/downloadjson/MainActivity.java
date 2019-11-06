package com.example.downloadjson;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DownloadJSONTask task=new DownloadJSONTask();
        task.execute();
    }

    private static class DownloadJSONTask extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... strings) {

            URL url = null;
            HttpURLConnection urlConnection = null;
            StringBuilder result = new StringBuilder();

            try {

                url = new URL(strings[0]);//create url from parameters
                urlConnection = (HttpURLConnection) url.openConnection();//opening Connection

                InputStream inputStream = urlConnection.getInputStream();// And geting OpenStrim
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(inputStreamReader);

                //start to read data from connection
                String line = reader.readLine();

                while (line != null) {

                    result.append(line);
                    line = reader.readLine();


                }
             return result.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {

                if(urlConnection!=null){

                    urlConnection.disconnect();
                }

            }


            return null;
        }
        @Override
        protected void onPostExecute(String s){

            super.onPostExecute(s);
            Log.i("MyResult",s);
        }
    }

}
