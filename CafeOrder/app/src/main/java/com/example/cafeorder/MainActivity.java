package com.example.cafeorder;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;

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

  private  String ServerUrl="http://start.webpower.cf/test/register/";
    static  private HashMap<String,String> postDataParams;
    TextView textViewName;
    TextView textViewEmail;
    TextView textViewPassword;

    LoginAndRegistration loginAndRegistration=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

           textViewName=findViewById(R.id.editTextName);
           textViewEmail =findViewById(R.id.editTextEmail);
           textViewPassword=findViewById(R.id.editTextPassword);
     loginAndRegistration=new LoginAndRegistration();



    }

 public void onClickRegistration(View view){

     postDataParams.put("name",textViewName.toString());
     postDataParams.put("email",textViewEmail.toString());
     postDataParams.put("password",textViewPassword.toString());


     loginAndRegistration.execute(ServerUrl);


 }

    public static class  LoginAndRegistration extends AsyncTask<String,Void,String> {



        @Override
        protected String doInBackground(String... strings) {

           URL url=null;
            HttpURLConnection urlConnection=null;
            String response= "";



            try {

                   url=new URL(strings[0]);
                   HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                   conn.setReadTimeout(15000);
                   conn.setConnectTimeout(15000);
                   conn.setRequestMethod("POST");
                   conn.setDoInput(true);
                   conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));

                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                int responseCode=conn.getResponseCode();

                if (responseCode== HttpsURLConnection.HTTP_OK){

                    String line;

                    BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    while((line=br.readLine())!=null){


                        response+=line;

                    }
                }
                else{

                    response="";
                }

                return response.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally{

            if(urlConnection!=null){

                urlConnection.disconnect();
            }

            }


            return null;

       }

        private String getPostDataString(HashMap<String,String>params) throws UnsupportedEncodingException {

            StringBuilder result=new StringBuilder();
            boolean first= true;
            for(Map.Entry<String,String> entry :params.entrySet()){

                if (first)
                    first=false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(entry.getKey(),"UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(),"UTF-8"));

            }
            return result.toString();
        }


    }
}
