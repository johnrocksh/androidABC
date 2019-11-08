package com.example.cafeorder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import static com.example.cafeorder.MainActivity.duration;

public class Registration extends AppCompatActivity {

    private static HashMap<String, String> postDataParams;
    private String registrationUrl = "http://start.webpower.cf/test/register/";
    private static Context mContext;
    static int duration;
    TextView textViewName;
    TextView textViewEmail;
    TextView textViewPassword;
    TextView textViewPassConf;
    ServerRegistrationTask serverRegistrationTask;
    static boolean isResponseSuccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        duration = Toast.LENGTH_SHORT;
        textViewName=findViewById(R.id.editTextName);
        textViewEmail=findViewById(R.id.editTextEmail);
        textViewPassword=findViewById(R.id.editTextPassword);
        textViewPassConf=findViewById(R.id.editTextConfirmPassword);
        mContext = this;
        serverRegistrationTask=new ServerRegistrationTask();

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


    public void onClickSubmit(View view) {

        /*check internet connection*/
        if (!getInternetConnectionInfo()) {

            Toast toast = Toast.makeText(this, "Нет подключения к интернету!", duration);
            toast.show();
            return;
        }

        postDataParams = new HashMap<String, String>();
//        postDataParams.put("nickname", textViewName.toString());
//        postDataParams.put("password", textViewPassword.toString());
//        postDataParams.put("email", textViewEmail.toString());

          postDataParams.put("nickname", "JohnRock");
          postDataParams.put("password", "777");
          postDataParams.put("email", "johnrock@mail.ru");

        /*destruct  loginAndRegistration an construct now*/

            /*destruct  loginAndRegistration an construct now*/
            if (serverRegistrationTask != null) {

                serverRegistrationTask = null;
                serverRegistrationTask = new ServerRegistrationTask();
            }

        try {
            serverRegistrationTask.execute(registrationUrl.toString());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

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
        protected void onPostExecute(String s){
            super.onPostExecute(s);
            Log.i("MyResult", s);

            try {



                    Toast toast = Toast.makeText(mContext,s, duration);
                    toast.show();




            } catch (Exception e) {
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
