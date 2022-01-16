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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import leandro.caixeta.relftgoals.R;
import leandro.caixeta.relftgoals.classes.Usuario;
import leandro.caixeta.relftgoals.classes.usuariosControler;

public class Login extends AppCompatActivity {

    private EditText email, senha ;
    private Button login;
    private TextView textViewLogCadastre ;
    private FirebaseAuth mUsuario = FirebaseAuth.getInstance();
    private FirebaseFirestore usuarioBD = FirebaseFirestore.getInstance();
    private ProgressBar progressBarLoginUsu ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //realizando a ligação entre as variaveis e os componentes
        email = findViewById(R.id.edtEmail);
        senha = findViewById(R.id.edtSenha);
        login = findViewById(R.id.btnRealizarLogin);
        textViewLogCadastre = findViewById(R.id.textViewLogCadastre);
        progressBarLoginUsu = findViewById(R.id.progressBarLoginUsu);

        //click do botao cadastre-se
        textViewLogCadastre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent NewCadastro = new Intent(getApplicationContext(),CadaEdit1.class);
                NewCadastro.putExtra("Acao","Cadastro");
                startActivity(NewCadastro);
            }
        });

        //click do botao de login

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ativarDesativarProgreBar(1);

                //recebendo valores digitado pelo usuario
                String emailRecb = email.getText().toString();
                String senhaRecb = senha.getText().toString();

                final Usuario usuario = new Usuario();

                if(!usuario.setEmail(emailRecb)){
                    Toast.makeText(getApplicationContext(),"O campo e-mail não pode estar vazio!",Toast.LENGTH_SHORT).show();
                    email.requestFocus();
                    ativarDesativarProgreBar(2);


                }else {

                    //verificando o campo senha

                    switch (usuario.setSenha(senhaRecb)) {
                        case 1:

                            Toast.makeText(getApplicationContext(), "O campo senha não pode estar vazio!", Toast.LENGTH_SHORT).show();
                            senha.requestFocus();
                            ativarDesativarProgreBar(2);
                            break;
                        case 2:

                            Toast.makeText(getApplicationContext(), "O campo senha deve possuir mais que 6 caracteres!", Toast.LENGTH_SHORT).show();
                            senha.requestFocus();
                            ativarDesativarProgreBar(2);
                            break;

                        default:


                            mUsuario.signInWithEmailAndPassword(emailRecb, senhaRecb)
                                    .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {

                                                //intanciando a classe do usuario
                                                final Usuario usuario = new Usuario();

                                                //setando o uid , email do usuario que esta logado atualmente
                                                usuario.SetarUidUsuarioAtual();
                                                usuario.setarEmail();

                                                //recuperando uid do usuario
                                                String uidUsuario = usuario.getUid();


                                                usuarioBD.collection("Usuarios").document(uidUsuario).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @RequiresApi(api = Build.VERSION_CODES.P)
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                                        if(task.isSuccessful()){

                                                            DocumentSnapshot documentoUsuario = task.getResult();
                                                            usuario.setData(documentoUsuario.get("Data aniversario").toString());
                                                            usuario.setNome(documentoUsuario.get("Nome").toString());
                                                            usuario.setEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                                                            usuario.setTipo(documentoUsuario.get("Tipo").toString());
                                                            usuario.setUid(FirebaseAuth.getInstance().getUid());


                                                            //chamando funçao que verifica se o cliente que fez login tem conta no bd local , se nao tiver deve ser criado uma para ele
                                                              int resultaodo =   verificandoBdLocal(usuario);



                                                            finishAffinity();
                                                            startActivity(new Intent(getApplicationContext(), home.class));

                                                            ativarDesativarProgreBar(2);
                                                            Toast.makeText(getApplicationContext(), "Login efetuado com sucesso!", Toast.LENGTH_LONG).show();

                                                        }else{

                                                            Toast.makeText(getApplicationContext(),"Erro ao buscar os dados do usuário!",Toast.LENGTH_LONG).show();
                                                            ativarDesativarProgreBar(2);
                                                        }

                                                    }
                                                });








                                            } else {

                                                try {
                                                    throw task.getException();


                                                }catch (FirebaseAuthInvalidCredentialsException e){
                                                    Toast.makeText(getApplicationContext(), "O email inserido é invalido .", Toast.LENGTH_LONG).show();
                                                    ativarDesativarProgreBar(2);
                                                }catch (FirebaseAuthInvalidUserException e) {
                                                    Toast.makeText(getApplicationContext(), "O email inserido nao esta vinculado com nenhuma conta.", Toast.LENGTH_LONG).show();
                                                    ativarDesativarProgreBar(2);
                                                } catch (Exception e) {
                                                    Toast.makeText(getApplicationContext(), "Erro ao realizar o login: " + e, Toast.LENGTH_LONG).show();
                                                    ativarDesativarProgreBar(2);
                                                }

                                            }//fechamento do else do task.isSuccessful

                                        }//fechamento do on complete
                                    });//fechamento do metodo signInWithEmailAndPassword

                            break;
                    }




                    }


                }




        });


    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public int verificandoBdLocal(Usuario usuario){

        int resultaod = 0 ;

        usuariosControler controler = new usuariosControler(getApplicationContext());
       // Usuario usuarioRecb = controler.buscarUsuario(usuario.getEmail());

        Usuario usuarioRecb = controler.verificarqtdRegistros();

        if(usuarioRecb.getNome() == null){
          //  cadastrarUsuario(usuario);

            //se nao exitir nenhum cadastro no bd devera ser feito o cadastro duas vezes a fim de arumar o erro de criação ok

            controler.insereUsuario(usuario);



           resultaod = 2 ;
        }else{
            resultaod = 1;
            //realizando uma busca no banco com o email do usuario que esta logando, caso seja retornado algo quer dize que ele ja posui conta, se nao devemos criar uma
            Usuario  usuarioTestandConta = new Usuario();
            usuarioTestandConta = controler.buscarUsuario(usuario.getEmail());

            if(usuarioTestandConta.getEmail() == null){
               int result =  controler.insereUsuario(usuario);


            }else{


            }

        }


       return  resultaod;

    }//fechamento da funçao verificarUsuario

    @RequiresApi(api = Build.VERSION_CODES.P)
    public void cadastrarUsuario(Usuario usuario){
        usuariosControler cadastrandoBDLocal = new usuariosControler(getApplicationContext());
        int resultado = cadastrandoBDLocal.insereUsuario(usuario);

    }
    public void ativarDesativarProgreBar(int situacao){
        //Nessa função sera feito a ativação e a desativação da barra de progresso
        //alem de desabilitar o botao e os campos na hora que a ProgresBar estiver liberada,
        //quando essa estiver invisvel os campos voltaram ao seu estado padrao
        if(situacao == 1){
            //se for 1, quer dizer que vamos ativar a progresBar e desativar os componentes
            progressBarLoginUsu.setVisibility(View.VISIBLE);
            email.setEnabled(false);
            senha.setEnabled(false);
        }else{
            //se nao vamos desligar a progresBar e ativar os componentes
            progressBarLoginUsu.setVisibility(View.GONE);
            email.setEnabled(true);
            senha.setEnabled(true);
        }//fechamento do if
    }

}
