package com.example.root.gopigoproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;

public class MainActivity extends AppCompatActivity {

    TextView txt;
    InterfaceRetrofit interfce;
    Button ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txt = (TextView) findViewById(R.id.textvie);
        ok = (Button) findViewById(R.id.button);
         interfce = new RestAdapter.Builder()
                .setEndpoint(InterfaceRetrofit.ENDPOINT)
                .build()
                .create(InterfaceRetrofit.class);


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,GraphActivity.class);
                startActivity(i);
            }
        });


    }

}

