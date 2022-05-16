package com.example.recyclerview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    //variable initialization
    RecyclerView recyclerView;
    TextView tvEmpty;
    ArrayList<String> arrayList = new ArrayList<>();
    MainAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ASSIGN VARIABLES
        recyclerView = findViewById(R.id.recycler_view);
        tvEmpty = findViewById(R.id.tv_empty);

        //ADD VALUES IN ARRAY LIST
        arrayList.addAll(Arrays.asList("one","Two","Three","Four","Five"
                ,"Six","Seven","Eight","Nine","Ten","Eleven","Twelve"
                ,"Thirteen","Fourteen","Fifteen"));

        //SET LAYOUT MANAGER
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //INITIALIZE ADAPTER
        adapter = new MainAdapter(this,arrayList,tvEmpty);
        //SET ADAPTER
        recyclerView.setAdapter(adapter);

    }
}