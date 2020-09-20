package com.example.systemize;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

public class OnboardingActivity2 extends AppCompatActivity {
    private String name;
    private CheckBox productive;
    private CheckBox alright;
    private CheckBox struggling;
    private String productivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        name = getIntent().getExtras().getString("Name");
        setContentView(R.layout.activity_onboarding2);
        setUpCheckboxes();
    }

    private void setUpCheckboxes(){
        productive = findViewById(R.id.pretty_productive_text);
        alright  = findViewById(R.id.alright_text);
        struggling = findViewById(R.id.struggling_text);
        for (final CheckBox checkbox: new CheckBox[]{productive, alright, struggling}){
            checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    productive.setChecked(false);
                    alright.setChecked(false);
                    struggling.setChecked(false);
                    checkbox.setChecked(true);
                    productivity = checkbox.getText().toString();
                }
            });
        }
    }
}
