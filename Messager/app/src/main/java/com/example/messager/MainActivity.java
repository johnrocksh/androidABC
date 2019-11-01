package com.example.messager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private EditText editTextMsg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextMsg=findViewById(R.id.editTextMessage);
    }


    public void onClickSendMessage(View view){
        String msg=editTextMsg.getText().toString();
        //creating intent
        //puting msg to intent and start intent
        //        Intent intent = new Intent(this,ReceivedMessageActivity.class);
//        intent.putExtra("msg",msg);
//        startActivity(intent);
        Intent intent =new Intent(Intent.ACTION_SEND);//we are need intent hoo can send message
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,msg);
        startActivity(intent);


    }

}

