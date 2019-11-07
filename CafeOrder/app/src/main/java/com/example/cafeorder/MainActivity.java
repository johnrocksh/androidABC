package com.example.cafeorder;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
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
    private static Context mContext;//to calling intent from doIn Background
    static private HashMap<String, String> postDataParams;
    static private AlertDialog.Builder responseAlert;
    static boolean isResponseSuccess;
    TextView textViewName;
    TextView textViewPassword;
    static  CharSequence errorLogin;
    static int duration;
    LoginAndRegistration loginAndRegistration = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Context context = getApplicationContext();
        errorLogin = "";
        duration = Toast.LENGTH_SHORT;


        textViewName = findViewById(R.id.editTextName);
        textViewPassword = findViewById(R.id.editTextPassword);
        loginAndRegistration = new LoginAndRegistration();
        responseAlert = new AlertDialog.Builder(this);
        mContext = this;
    }

    public void onClickRegistration(View view) {

        postDataParams = new HashMap<String, String>();
        postDataParams.put("nickname", "john");
        postDataParams.put("password", "salvation777");

       /*destruct an construct now*/
        if(loginAndRegistration!=null){

            loginAndRegistration=null;
            loginAndRegistration = new LoginAndRegistration();

        }
        loginAndRegistration.execute(authUrl).toString();

        if (isResponseSuccess) {


        }
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
            String message = "success";

            try {

                JSONObject jsonObject = new JSONObject(s);
                String error = jsonObject.getString("error");
                String success = jsonObject.getString("success");

                if (error.equals("")) {

                    Toast toast = Toast.makeText(mContext, success, duration);
                    toast.show();

                    isResponseSuccess = true;
                    Intent login = new Intent(mContext, UserInformation.class);
                    mContext.startActivity(login);

                } else if (success.equals("")) {
                    Toast toast = Toast.makeText(mContext, error, duration);
                    toast.show();
                    isResponseSuccess = false;

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

}



