package org.techtown.smartkey_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class add_category extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        final TextView textView = (TextView)findViewById(R.id.textView);
        Spinner spinner = (Spinner)findViewById(R.id.spinner);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                textView.setText(parent.getItemAtPosition(position) + " 이(가) 맞나요?");
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        Button button1 = (Button) findViewById(R.id.exit_btn);
        button1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), lock.class);
                startActivity(intent);
            }
        });
    }
}
