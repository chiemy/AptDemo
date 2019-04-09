package com.chiemy.nbrouter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.chiemy.lib.router.NBRouter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btnJump).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NBRouter.instance()
                        .navigate(MainActivity.this, "main2");
            }
        });
    }
}
