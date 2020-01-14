package com.example.cafeorder;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
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

   // private String authUrl = "http://start.webpower.cf/test/auth/";
   //private String authUrl = "http://docto.webpower.cf/API/loginController";

   //главный URL
   private String authUrl = "http://docto.webpower.cf/API/";

    private static Context mContext;//to calling intent from doIn Background
    static private HashMap<String, String> postDataParams;
    static boolean isResponseSuccess;
    static CharSequence errorLogin;
    static int duration;

    TextView textViewName;
    TextView textViewPassword;
    LoginToServer loginToServer = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        errorLogin = "";
        duration = Toast.LENGTH_SHORT;
        textViewName = findViewById(R.id.editTextName);
        textViewName.setText("");
        textViewPassword = findViewById(R.id.editTextPassword);
        textViewPassword.setText("");
        loginToServer = new LoginToServer();
        mContext = this;
    }


    public void onClickLogin(View view) {

        /*check internet connection*/
        if (!getInternetConnectionInfo()) {

            Toast toast = Toast.makeText(this, "Нет подключения к интернету!", duration);
            toast.show();
            return;
        }


//        postDataParams.put("nickname", textViewName.toString());
//        postDataParams.put("password", textViewPassword.toString());
      
        //здесь я создаю запрос!!!
        postDataParams = new HashMap<String, String>();
        postDataParams.put("task", "loginController");
        postDataParams.put("email", "Icosmo12@mail.ru");
        postDataParams.put("password", "123456");
        postDataParams.put("rememberMe", "0");


       String name = textViewName.getText().toString();
        if (name.equals("")) {

            Toast toast = Toast.makeText(this, "Пожалуйста заполните все поля", duration);
            toast.show();
            return;
        }
        /*destruct  loginAndRegistration an construct now*/
        if (loginToServer != null) {

            loginToServer = null;
            loginToServer = new LoginToServer();

        }
        try {
            loginToServer.execute(authUrl).toString();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

    public void onClickRegistration(View view) {
        Intent intentReg = new Intent(this, Registration.class);
        startActivity(intentReg);
    }

    boolean getInternetConnectionInfo() {

        boolean isInternetConnection = false;

        // Getting our connectivity manager.
        ConnectivityManager mgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        // Getting our active network information.
        NetworkInfo netInfo = mgr.getActiveNetworkInfo();
        // We have a network connection, but not necessarily a data connection.

        if (netInfo != null) {
            if (netInfo.getType() == ConnectivityManager.TYPE_MOBILE) {

            } else if (netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                // We're on WiFi data

            }
            if (netInfo.isConnected()) {
                isInternetConnection = true;
                // We have a valid data connection
            }
        }
        return isInternetConnection;
    }


    static public class LoginToServer extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... strings) {

            URL url = null;
            HttpURLConnection conn = null;
            String response = "";


            try {

                url = new URL(strings[0]);
                conn = (HttpURLConnection) url.openConnection();
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
                    String line="";
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                   
                    //по строкам считываю ответ
                    while ((line = br.readLine()) != null) {
                        response += line;
                    }
                } else {
                    response+=Integer.toString(responseCode);
//
                    response = "";
                }
                Log.i("response", response);
                return response.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {

                if (conn != null) {
                    conn.disconnect();
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



