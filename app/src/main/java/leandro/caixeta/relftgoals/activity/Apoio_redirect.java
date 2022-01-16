package leandro.caixeta.relftgoals.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import leandro.caixeta.relftgoals.R;

public class Apoio_redirect extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redirect);

        finish();
    }
}
