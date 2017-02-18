package com.example.davidcruz.hackfsu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class InterestsActivity extends AppCompatActivity {

    ArrayList<String> interests;
    private Button readybtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interests);

        interests = new ArrayList<>();
        readybtn = (Button) findViewById(R.id.btn_submitSurvey);
        readybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Eating_Choice.class);
                startActivity(intent);
            }
        });
    }

    public void onCheckboxClicked(View view){
        CheckBox chkClicked = (CheckBox)view;

        if(chkClicked.isChecked()){
            interests.add(chkClicked.getText().toString());
        }
    }
}
