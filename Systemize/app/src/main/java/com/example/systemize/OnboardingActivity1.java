package com.example.systemize;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class OnboardingActivity1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding1);
    }

    public void onNextClicked(View view){
        EditText name = findViewById(R.id.welcome_enter_text);
        if (name.getText().length() == 0){
            Toast.makeText(getApplicationContext(), "Please enter your name", Toast.LENGTH_SHORT).show();
        }
        else{
            Intent intent = new Intent(this, OnboardingActivity2.class);
            intent.putExtra("Name", name.getText().toString());
            startActivity(intent);
        }
    }
}