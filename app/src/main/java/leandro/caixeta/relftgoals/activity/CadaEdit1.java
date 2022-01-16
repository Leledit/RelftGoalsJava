package leandro.caixeta.relftgoals.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import leandro.caixeta.relftgoals.R;
import leandro.caixeta.relftgoals.classes.Usuario;
import leandro.caixeta.relftgoals.classes.Utilities;
import leandro.caixeta.relftgoals.classes.usuariosControler;

public class CadaEdit1 extends AppCompatActivity {

    private Button proximo;
    private EditText nome, dataNacimento;
    private String acaoPage ,uidUsuarioLogado;
    private TextView textCadastreEditUsu ;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth user = FirebaseAuth.getInstance();
    private Usuario usuarioLocal = new Usuario();



    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cada_edit1);

        proximo = findViewById(R.id.btnProximo);
        nome = findViewById(R.id.edtNomeUsuario);
        dataNacimento = findViewById(R.id.edtData);

        textCadastreEditUsu = findViewById(R.id.textCadastreEditUsu);

        //recuperando a acao que indica qual sera a funcionalidade da pagina
        Bundle acaoRecebida = getIntent().getExtras();
        acaoPage = acaoRecebida.getString("Acao");

        //
       //ArrayAdapter<CharSequence> Adapter = ArrayAdapter.createFromResource(this,R.,R.layout.);
        //verificando qual função a pagina exercera(Cadastro/Edicao de usuario)
        if(acaoPage.equals("Cadastro")){
            textCadastreEditUsu.setText("Cadastre-se");
            proximo.setText("Próximo");

        }else{
            textCadastreEditUsu.setText("Informaçoes pessoais ");
            textCadastreEditUsu.setTextSize(28);
            // caso a ação seja de edição ,sera mostrados os dados do ususario logado nos seus respectivos campos
            buscarDadosUsuarioLogado();
            proximo.setText("Editar");

        }



        final Usuario usuario = new Usuario();

        proximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //pegando os valores digitados pelo usuario
                String nomeResc = nome.getText().toString();
                String dataNacimentoRecb = dataNacimento.getText().toString();


                //verificando se as variaveis nao foram enviadas vazias

                //verificado se o campo nom esta vazio

                switch (usuario.setNome(nomeResc)) {
                    case 1:

                        Toast.makeText(getApplicationContext(), "O campo nome não pode esta vazio!", Toast.LENGTH_SHORT).show();
                        nome.requestFocus();

                        break;
                    case 2:

                        Toast.makeText(getApplicationContext(), "O campo nome deve possuir mais que 3 caracteres! ", Toast.LENGTH_SHORT).show();
                        nome.requestFocus();

                        break;
                    default:

                        switch (usuario.setData(dataNacimentoRecb)) {
                            case 1:

                                Toast.makeText(getApplicationContext(), "O campo data de nascimento não pode estar vazio!", Toast.LENGTH_SHORT).show();
                                dataNacimento.requestFocus();

                                break;
                            case 2:
                                Toast.makeText(getApplicationContext(), "Por favor, informe uma data completa!", Toast.LENGTH_SHORT).show();

                                dataNacimento.requestFocus();
                                break;
                            default:
                                if (!usuario.verificarAnoDataNaciment()) {

                                    Toast.makeText(getApplicationContext(), "Data invalida!! Insira um ano válido! ", Toast.LENGTH_SHORT).show();
                                    dataNacimento.setText("");
                                    dataNacimento.requestFocus();

                                } else {


                                    if (!usuario.verificarDataNaciment()) {

                                        Toast.makeText(getApplicationContext(), "Data inválida!! favor digitar uma data válida!", Toast.LENGTH_SHORT).show();
                                        dataNacimento.setText("");
                                        dataNacimento.requestFocus();


                                    } else {

                                        if(acaoPage.equals("Cadastro")){

                                            Intent proximoIntent = new Intent(getApplicationContext(), CadaEdit2.class);
                                            proximoIntent.putExtra("Obejct", usuario);
                                            startActivity(proximoIntent);

                                        }else{

                                            Map<String , Object> dadosbd =  new HashMap<>();
                                            dadosbd.put("Nome",usuario.getNome());
                                            dadosbd.put("Data aniversário",usuario.getData());
                                            dadosbd.put("Tipo",usuario.getTipo());

                                            String uid = user.getUid();


                                            db.collection("Usuarios").document(uid).set(dadosbd)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {


                                                    //chamando a função respoesnavel por alterar os dados no banco local

                                                    atualizarDadosBdLocal(usuario);

                                                    //realizando procedimento de fechamento da pagina
                                                    finish();
                                                    Toast.makeText(getApplicationContext(),"Edição realizada com sucesso!",Toast.LENGTH_LONG).show();
                                                    Intent proximoIntent = new Intent(getApplicationContext(),home.class);
                                                    startActivity(proximoIntent);
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(getApplicationContext(),"Erro ao realizar a edição!"+e,Toast.LENGTH_LONG).show();

                                                }
                                            });




                                        }




                                        }//fechamento switch celular


                                    }


                                }


                                break;
                        }//fechamento do switch data



            }//fechamento do evento de onclick do botao cadastro



        });
        }//fechamento do metodo oncreate

    @RequiresApi(api = Build.VERSION_CODES.P)
    public void atualizarDadosBdLocal(Usuario usuarioAlter){

        //setando os valores
        usuarioLocal.setData(usuarioAlter.getData());
        usuarioLocal.setNome(usuarioAlter.getNome());

        usuariosControler controler = new usuariosControler(getApplicationContext());
        controler.alterandoDados(usuarioLocal);




    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public void buscarDadosUsuarioLogado(){

        //pegando o email do usuario logado atualmente no sistema
        String usuEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        usuariosControler usuariocontrol = new usuariosControler(getApplicationContext());
        usuarioLocal = usuariocontrol.buscarUsuario(usuEmail);

        //realizando o corte da data em parte para realizar um determinada verificação
        Utilities utilities = new Utilities();
        int dia = utilities.cortarDataDia(usuarioLocal.getData());
        int mes = utilities.cortarDataMes(usuarioLocal.getData());
        int ano = utilities.cortarDataAno(usuarioLocal.getData());

        //setando os valores em seus respectivos locais
        if(mes <10){
            dataNacimento.setText("" + dia +"0"+ mes + ano);

        }else {
            dataNacimento.setText("" + dia + mes + ano);
        }
        nome.setText(usuarioLocal.getNome());
    }//fechamento da função "buscarDados"

    }




