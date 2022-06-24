package com.potato.oneall.ui.main;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.potato.timetable.R;

public class MainTent extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maintent);
        Button button = findViewById(R.id.back_button);
        button.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(MainTent.this,Main0Activity.class);
            startActivity(intent);
            }
        );
    }
}
