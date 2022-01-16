package leandro.caixeta.relftgoals.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import leandro.caixeta.relftgoals.R;

public class MainActivity extends AppCompatActivity {

    private Button sim, nao ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sim = findViewById(R.id.btnSim);
        nao = findViewById(R.id.btnNao);

        sim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });

        nao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCadadastro = new Intent(getApplicationContext(), CadaEdit1.class);
                intentCadadastro.putExtra("Acao","Cadastro");
                startActivity(intentCadadastro);
            }
        });




    }

    public void onBackPressed() {
        finish();
    }
}
