package com.example.xiongfeng.wave;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv1,tv2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv1=(TextView) findViewById(R.id.tv1);
        tv2=(TextView) findViewById(R.id.tv2);

        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv1:
                Intent intent1=new Intent(MainActivity.this,ZhenXuanActivity.class);
                startActivity(intent1);
                break;

            case R.id.tv2:
                Intent intent2=new Intent(MainActivity.this,ZheZhaoActivity.class);
                startActivity(intent2);
                break;
        }
    }
}
