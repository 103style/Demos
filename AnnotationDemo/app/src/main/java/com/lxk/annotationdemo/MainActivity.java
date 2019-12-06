package com.lxk.annotationdemo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lxk.annotationdemo.adapter.TestAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author https://github.com/103style
 * @date 2019/11/29 11:43
 */
public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.rv_test);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        TestAdapter adapter = new TestAdapter(this);
        adapter.setItems(getItems());
        recyclerView.setAdapter(adapter);


    }

    private List<String> getItems() {
        List<String> t = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            t.add(String.valueOf(i));
        }
        return t;
    }
}
