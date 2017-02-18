package com.example.davidcruz.hackfsu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class InterestsActivity extends AppCompatActivity {

    ArrayList<String> interests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interests);

        interests = new ArrayList<>();
    }

    public void onCheckboxClicked(View view){
        CheckBox chkClicked = (CheckBox)view;

        if(chkClicked.isChecked()){
            interests.add(chkClicked.getText().toString());
        }
    }
}
