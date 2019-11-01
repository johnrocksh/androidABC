package com.example.messager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ReceivedMessageActivity extends AppCompatActivity {
  TextView textViewRecivedMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_received_message);
        textViewRecivedMessage=findViewById(R.id.textViewRecivedMessage);
        Intent intent=getIntent();
        String message = intent.getStringExtra("msg");
        textViewRecivedMessage.setText(message);

    }
}
