package com.example.cafeorder;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
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

    static TableLayout tableLayout;


    private static Context mContext;//to calling intent from doIn Background

    static TextView textViewName;
    static TextView textViewActivity;
    static TextView textViewAge;
    static TextView textViewEmail;

    GetUserInformationTask getUserInformationTask = new GetUserInformationTask();
    private String userDataUrl = "http://start.webpower.cf/test/data/";
    static public String name = "";
    static public String email = "";
    static public String activity = "";
    static public int age = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);
        textViewName = findViewById(R.id.textViewName);
        textViewActivity = findViewById(R.id.textViewActivity);
        textViewAge = findViewById(R.id.textViewAge);
        textViewEmail = findViewById(R.id.textViewEmail);

        mContext = this;

        getUserInformationTask.execute(userDataUrl);

    }

    public void OnClickOk(View view) {
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
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

                JSONObject json = new JSONObject(s);
                name = json.getString("name");
                activity = json.getString("activity");
                age = json.getInt("age");
                email = json.getString("e-mail");
                showUserInformation(name, activity, age, email);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }


    //creation of table with user Data
    static private void showUserInformation(String Name, String Acctivity, int Age, String email) {

        textViewName.setText(Name);
        textViewActivity.setText(Acctivity);
        textViewAge.setText(String.valueOf(Age));
        textViewEmail.setText(email);


    }


}

