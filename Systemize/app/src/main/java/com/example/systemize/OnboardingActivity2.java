package com.example.systemize;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

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
        name = getIntent().getExtras().getString("Name");
        setContentView(R.layout.activity_onboarding2);
        setUpCheckboxes();
        button = findViewById(R.id.next_button);
        button.setBackgroundResource(R.drawable.next_button_na);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (okay){

                }
            }
        });
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
