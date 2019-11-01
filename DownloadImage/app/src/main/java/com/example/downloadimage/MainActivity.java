package com.example.downloadimage;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private String url="https://www.fiesta.city/uploads/slider_image/image/144041/v880_61.jpg"e;
    private ImageView imageView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView= findViewById(R.id.imageView);
    }

    public void onClickDownloadImage(View view) {

        DownloadImageTask task=new DownloadImageTask();
        Bitmap bitmap=null;
        try {
             bitmap = task.execute(url).get();


        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();


        }
        imageView.setImageBitmap(bitmap);

    }

    private static class DownloadImageTask extends AsyncTask<String,Void, Bitmap>{
        @Override
        protected Bitmap doInBackground(String... strings) {

            URL url=null;
            HttpURLConnection urlConnection=null;

            try {
                url = new URL(strings[0]);
                try {
                    urlConnection=(HttpURLConnection) url.openConnection();
                    InputStream inputStream= urlConnection.getInputStream();
                    Bitmap bitmap= BitmapFactory.decodeStream(inputStream);

                    return bitmap;

                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            finally {
                if(urlConnection!=null){

                    urlConnection.disconnect();
                }
            }


            return null;
        }
    }
}
