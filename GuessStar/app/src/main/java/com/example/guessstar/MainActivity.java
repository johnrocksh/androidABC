package com.example.guessstar;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.jar.Attributes;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private  String url="http://www.posh24.se/";
    private ArrayList<String>urls;
    private ArrayList<String>names;

    Button button0=null;
    Button button1=null;
    Button button2=null;
    Button button3=null;

    ImageView imageViewStar=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        button0=findViewById(R.id.button0);
        button1=findViewById(R.id.button1);
        button2=findViewById(R.id.button2);
        button3=findViewById(R.id.button3);

        imageViewStar=findViewById(R.id.imageViewStar);
        urls=new ArrayList<>();
        names=new ArrayList<>();
        GetContent();
    }

    private void GetContent(){


        DownloadContentTask task=new DownloadContentTask();

        try {


            String content=task.execute(url).get() ;

            String start="<h1 class=\"header\">Topp 20 kändisar</h1>";
            String finish="<div class=\"moreArticlesContainer contentBlock\">";

            Pattern  pattern=Pattern.compile(start+"(.*?)"+finish);
            Matcher matcher=pattern.matcher(content);
            String splitContent="";
            while(matcher.find()){

                splitContent=matcher.group(1);

            }

            Pattern paternImg=Pattern.compile("<img src=\"(.*?)\"");
            Pattern paternName=Pattern.compile("alt=\"(.*?)\"/>");

            Matcher matcherImg=paternImg.matcher(splitContent);
            Matcher matcherName=paternName.matcher(splitContent);

            while(matcherImg.find()){
                urls.add(matcherImg.group(1));

            }
            while(matcherName.find()){
                names.add(matcherName.group(1));

            }

            for(String s: urls){
                Log.i("MyResult",s);

            }
           // Log.i("myResult",splitContent);

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private static class DownloadContentTask extends AsyncTask<String,Void,String>{


        @Override
        protected String doInBackground(String... strings) {
            URL url=null;
            HttpURLConnection urlConnection=null;
            StringBuilder result= new StringBuilder();

            try {
                url=new URL(strings[0]);
                urlConnection=(HttpURLConnection)url.openConnection();
                InputStream inputStream=urlConnection.getInputStream();
                InputStreamReader inputStreamReader=new InputStreamReader(inputStream);
                BufferedReader reader=new BufferedReader(inputStreamReader);

                String line=reader.readLine();
                while(line!=null){

                    result.append(line);
                    line=reader.readLine();
                }
                return result.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private static  class DownloadImageTask extends AsyncTask<String,Void, Bitmap>{


        @Override
        protected Bitmap doInBackground(String... strings) {
            URL url=null;
            HttpURLConnection urlConnection=null;
            StringBuilder result= new StringBuilder();

            try {
                url=new URL(strings[0]);
                urlConnection=(HttpURLConnection)url.openConnection();
                InputStream inputStream=urlConnection.getInputStream();
                Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
                return bitmap;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
