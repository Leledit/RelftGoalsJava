package leandro.caixeta.relftgoals.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.R.layout;

import leandro.caixeta.relftgoals.R;

public class Contato_form extends AppCompatActivity {

    private EditText editContatFormAssunto,editContatFormEmail,editContatFormMensagem;
    private Button btnEnviarMensagem ;
    private Spinner spinnerTipo ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contato_form);

        //realizando a ligação dos componentes as suas rescpectivas variaveis na programação

        editContatFormEmail = findViewById(R.id.editContatFormEmail);
        editContatFormAssunto =findViewById(R.id.editContatFormAssunto);
        editContatFormMensagem = findViewById(R.id.editContatFormMensagem);
        btnEnviarMensagem = findViewById(R.id.btnEnviarMensagem);
        spinnerTipo = findViewById(R.id.spinnerTipo);

        //recuperando os dados que estao armazenados no arquivo do usuario
        SharedPreferences preferencesUsuario = getSharedPreferences("RELFT_GOAS_USUARIO", 0);
        String emailUsuarioLogado = preferencesUsuario.getString("Email","").toString();


        //colocando ops dentro do componente Spiner de categoria
        ArrayAdapter<CharSequence> CategoriaAdapter = ArrayAdapter.createFromResource
                (this,R.array.contat_tipo , R.layout.spinner_contat_dropdown_item );

        CategoriaAdapter.setDropDownViewResource(R.layout.spinner_contat_dropdown_iten_list);
        spinnerTipo.setAdapter(CategoriaAdapter);




        //colocando o email do susario no imput de email
        editContatFormEmail.setText(emailUsuarioLogado);

       btnEnviarMensagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //recebendo os dados que foram enviados pelo usuario
                String tipoMensg = spinnerTipo.getSelectedItem().toString();
                String assunto = editContatFormAssunto.getText().toString();
                String mensagem = editContatFormMensagem.getText().toString();



                //chamando o app de email padrao do celular
                Intent chamandoEmail = new Intent(Intent.ACTION_SEND);

                chamandoEmail.setData(Uri.parse("mailto:"));
                chamandoEmail.setType("image/*");
                chamandoEmail.putExtra(Intent.EXTRA_EMAIL,new String[]{"Leandro_ricardo99@outlook.com"});

                chamandoEmail.putExtra(Intent.EXTRA_SUBJECT,assunto);
                chamandoEmail.putExtra(Intent.EXTRA_TEXT,"(Tipo de mensagem "+tipoMensg+") Mensagem "+mensagem);


                try{
                    startActivity(Intent.createChooser(chamandoEmail,"seu email"));
                    finish();
                }catch (android.content.ActivityNotFoundException ex){
                    Toast.makeText(getApplicationContext(), "There is no email client installed.", Toast.LENGTH_SHORT).show();
                }
            }


        });











    }

}
