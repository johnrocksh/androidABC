package com.example.dovnloadwebcontent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    TextView textViewUrl=null;
    EditText textViewHtml=null;

    String weather = "https://mail.ru/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewUrl=findViewById(R.id.editTextUrl);
        textViewHtml=findViewById(R.id.editTextHtml);


    }

    public void onClickGetHtml(View view) {
        DownloadTask task = new DownloadTask();

        try {
            String result = task.execute(textViewUrl.getText().toString()).get();
            textViewHtml.append(result);
        } catch (ExecutionException e) {

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void onClickClear(View view) {
          textViewHtml.setText("");
    }

    private static class DownloadTask extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... strings) {

            StringBuilder result=new StringBuilder();
            URL url = null;
            HttpURLConnection urlConnection = null;


            try {
                url = new URL(strings[0]);//передаем наш weather первым параметром
                urlConnection = (HttpURLConnection) url.openConnection();//открываем коннекшин
                InputStream in =urlConnection.getInputStream();//открываем поток ввода для чтения
                InputStreamReader reader=new InputStreamReader(in);//создаем инпут стрим для чтения данных по символу
                BufferedReader bufferedReader=new BufferedReader(reader);//создаем чтение по строкам

                String line= bufferedReader.readLine();//здесь мы получаем первую строку из ншего ссйта
                //для кидаем ее в  ресулт и дальше считываем все построчно
                while (line!=null){

                    result.append(line);
                    line=bufferedReader.readLine();
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {//закрываем коннекшин если что то идет не так
               if(urlConnection!=null){
                   urlConnection.disconnect();
               }
            }


            return result.toString();
        }
    }
}