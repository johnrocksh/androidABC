package com.example.cafeorder;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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

public class MainActivity extends AppCompatActivity {

    private String registerUrl = "http://start.webpower.cf/test/register/";
    private String authUrl = "http://start.webpower.cf/test/auth/";

    static private HashMap<String, String> postDataParams;
    TextView textViewName;
    TextView textViewPassword;
    static private AlertDialog.Builder responseAlert;

    LoginAndRegistration loginAndRegistration = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewName = findViewById(R.id.editTextName);
        textViewPassword = findViewById(R.id.editTextPassword);
        loginAndRegistration = new LoginAndRegistration();
        responseAlert = new AlertDialog.Builder(this);
    }

    public void onClickRegistration(View view) {

        postDataParams = new HashMap<String, String>();
        postDataParams.put("nickname", "john");
        postDataParams.put("password", "salvation777");

        loginAndRegistration.execute(authUrl);


    }

    static public class LoginAndRegistration extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... strings) {

            URL url = null;
            HttpURLConnection urlConnection = null;
            String response = "";


            try {

                url = new URL(strings[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

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
                Log.i("response", response);
                return response.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {

                if (urlConnection != null) {
                    urlConnection.disconnect();
                }

            }


            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i("MyResult", s);

            try {

                JSONObject jsonObject = new JSONObject(s);
                String error = jsonObject.getString("error");
                String success = jsonObject.getString("success");

                if (error.equals("")) {
                    String message = "success";
                    showAllertLoginSuccess(message);
                } else if (success.equals("")) {
                    String message = "error";
                    showAllertLoginSuccess(message);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }


        private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {

            StringBuilder result = new StringBuilder();
            boolean first = true;
            for (Map.Entry<String, String> entry : params.entrySet()) {

                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));

            }
            return result.toString();
        }


    }

    static public void showAllertLoginSuccess(String events) {


        if (events.equals("error")) {
            responseAlert.setMessage("Неверные данные\n" +
                    "авторизации\n")
                    .create();
            responseAlert.show();


        } else if (events.equals("success")) {
            responseAlert.setMessage("Вы успешно авторизовались\n")
                    .create();
            responseAlert.show();

        }

    }
}

