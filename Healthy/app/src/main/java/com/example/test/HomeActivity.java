package com.example.test;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class HomeActivity extends AppCompatActivity {

    public RadioButton rbtn_male;
    private RadioButton rbtn_female;
    private EditText edt_age;
    private EditText edt_feet;
    private EditText edt_inch;
    private EditText edt_weight;
    private Button btn_cal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        findview();
        setupBtn();
        //Toast.makeText(this, "Tôi đã làm được điều đó!", Toast.LENGTH_LONG).show();

    }
    private void setupBtn(){
        btn_cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomeActivity.this, "abcask", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void findview()
    {
        rbtn_male = findViewById(R.id.rbtn_male);
        rbtn_female = findViewById(R.id.rbtn_female);
        edt_age = findViewById(R.id.edt_age);
        edt_feet = findViewById(R.id.edt_feet);
        edt_inch = findViewById(R.id.edt_inch);
        edt_weight = findViewById(R.id.edt_weight);
        btn_cal = findViewById(R.id.btn_cal);
    }
    String [] dish ={"lkakdlksadjasj","akskdllka"} ;
    ArrayAdapter<String> myArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dish);

}