package com.example.android.foodshare;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

/**
 * Created by User on 2017-05-07.
 */

public class SetPickup extends AppCompatActivity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setpickup);

        Button subDate = (Button) findViewById(R.id.bSubDate);
        subDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SetPickup.this, ThankYou.class);
                startActivity(i);
            }
        });
    }


}
