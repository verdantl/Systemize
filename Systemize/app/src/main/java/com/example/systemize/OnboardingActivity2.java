package com.example.systemize;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import java.util.Objects;

public class OnboardingActivity2 extends AppCompatActivity {
    private String name;
    private CheckBox productive;
    private CheckBox alright;
    private CheckBox struggling;
    private boolean okay;
    private String productivity;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        name = Objects.requireNonNull(getIntent().getExtras()).getString("Name");
        setContentView(R.layout.activity_onboarding2);
        setUpCheckboxes();
        button = findViewById(R.id.next_button);
        button.setBackgroundResource(R.drawable.next_button_na);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (okay){
                    nextActivity();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Please choose an option.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void nextActivity(){
        Intent intent = new Intent(this, EditProfileActivity.class);
        intent.putExtra("Name", name);
        intent.putExtra("Productivity", productivity);
        intent.putExtra("First", true);
        startActivityForResult(intent, 333);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 333) {
            Intent intent = new Intent(this, HomeScreenActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void setUpCheckboxes(){
        productive = findViewById(R.id.pretty_productive_text);
        alright  = findViewById(R.id.alright_text);
        struggling = findViewById(R.id.struggling_text);
        for (final CheckBox checkbox: new CheckBox[]{productive, alright, struggling}){
            checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    okay = true;
                    productive.setChecked(false);
                    alright.setChecked(false);
                    struggling.setChecked(false);
                    checkbox.setChecked(true);
                    productivity = checkbox.getText().toString();
                    button.setBackgroundResource(R.drawable.next_button);

                }
            });
        }
    }


}
