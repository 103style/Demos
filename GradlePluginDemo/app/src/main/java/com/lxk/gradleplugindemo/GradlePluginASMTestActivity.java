package com.lxk.gradleplugindemo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @author https://github.com/103style
 * @date 2020/1/15 10:36
 */
public class GradlePluginASMTestActivity extends AppCompatActivity {

    private Button test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        test = findViewById(R.id.button);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickTest();
            }
        });
    }

    private void clickTest() {
        try {
            Thread.sleep(1000);
            Toast.makeText(this, "sleep 1000", Toast.LENGTH_SHORT).show();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
