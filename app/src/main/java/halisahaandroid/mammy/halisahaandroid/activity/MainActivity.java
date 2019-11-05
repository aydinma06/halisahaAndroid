package halisahaandroid.mammy.halisahaandroid.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import halisahaandroid.mammy.halisahaandroid.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            Intent intent = new Intent(this, MainMenuActivity.class);
            startActivity(intent);
    }
}
