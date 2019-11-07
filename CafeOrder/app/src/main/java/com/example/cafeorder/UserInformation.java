package com.example.cafeorder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class UserInformation extends AppCompatActivity {

 static   TableLayout tableLayout;
    private static Context mContext;//to calling intent from doIn Background

    GetUserInformationTask getUserInformationTask = new GetUserInformationTask();
    private String userDataUrl="http://start.webpower.cf/test/data/";
   static public String name = "";
    static public String email = "";
    static public String activity = "";
    static public int age=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);
        tableLayout = findViewById(R.id.TableLayoutUserInfo);
        mContext=this;


        getUserInformationTask.execute(userDataUrl);

    }

    static public class GetUserInformationTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            /*declaring url*/
            URL url = null;
            StringBuilder result = new StringBuilder();
            HttpURLConnection urlConnection = null;


            /*open curl connection*/
            try {
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                BufferedReader bufferedReader = new BufferedReader(reader);
                String line = bufferedReader.readLine();

                /*read data*/
                while (line != null) {
                    result.append(line);
                    line = bufferedReader.readLine();
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {//закрываем коннекшин если что то идет не так
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }


            return result.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            try {

                JSONObject json=new JSONObject(s);
                name=json.getString("name");
                activity= json.getString("activity");
                age=json.getInt("age");
                email=json.getString("e-mail");
                drawTableLayout(name,activity,age,email);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }


    //creation of table with user Data
 static    private void drawTableLayout(String Name, String Job, int Age, String email) {

        LinearLayout.LayoutParams tableRowParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        /* create a table row */
        TableRow tableRow = new TableRow(mContext);
        tableRow.setLayoutParams(tableRowParams);


        /* create cell element - textview */
        TextView tvName = new TextView(mContext);
        tvName.setBackgroundColor(0xff12dd12);
        tvName.setText(Name);

        /* create cell element - textview */
        TextView tvJob = new TextView(mContext);
        tvJob = new TextView(mContext);
        tvJob.setBackgroundColor(0xff12dd12);
        tvJob.setText(Job);


        /* create cell element - textview */
        TextView tvAge = new TextView(mContext);
        tvAge = new TextView(mContext);
        tvAge.setBackgroundColor(0xff12dd12);
        tvAge.setText(String.valueOf(Age));

        /* create cell element - textview */
        TextView tvEmail = new TextView(mContext);
        tvEmail.setBackgroundColor(0xff12dd12);
        tvEmail.setText(email);



        /* set params for cell elements */
        TableRow.LayoutParams cellParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT);
        tvName.setLayoutParams(cellParams);
        tvEmail.setLayoutParams(cellParams);
        tvAge.setLayoutParams(cellParams);
        tvJob.setLayoutParams(cellParams);
        //cellParams.
        cellParams.weight = 1;
        tvName.setGravity(Gravity.CENTER);
        tvName.setTypeface(Typeface.DEFAULT_BOLD);

        tvEmail.setGravity(Gravity.CENTER);
        tvEmail.setTypeface(Typeface.DEFAULT_BOLD);

        tvAge.setGravity(Gravity.CENTER);
        tvAge.setTypeface(Typeface.DEFAULT_BOLD);

        tvJob.setGravity(Gravity.CENTER);
        tvJob.setTypeface(Typeface.DEFAULT_BOLD);

        tableLayout.addView(tableRow);


        /* add views to the row */
        tableRow.addView(tvName);
        tableRow.addView(tvEmail);
        tableRow.addView(tvAge);
        tableRow.addView(tvJob);

    }


}

