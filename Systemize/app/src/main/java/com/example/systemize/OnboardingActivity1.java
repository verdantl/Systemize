package com.example.systemize;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Objects;

public class OnboardingActivity1 extends AppCompatActivity {
    EditText name;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding1);
        name = findViewById(R.id.welcome_enter_text);
        button = findViewById(R.id.next_button);
        button.setBackgroundResource(R.drawable.next_button_na);
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                button.setBackgroundResource(R.drawable.next_button);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name.getText().length() == 0){
                    Toast.makeText(getApplicationContext(), "Please enter your name", Toast.LENGTH_SHORT).show();
                }
                else{
                    startTut();
                }
            }
        });
    }

    private void startTut(){
        Intent intent = new Intent(this, OnboardingActivity2.class);
        intent.putExtra("Name", name.getText().toString());
        startActivity(intent);
    }
}