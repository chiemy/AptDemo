package com.chiemy.nbrouter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.chiemy.nbrouter.annotation.Route;

@Route(path = "main2")
public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
    }
}
