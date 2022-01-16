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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import leandro.caixeta.relftgoals.R;
import leandro.caixeta.relftgoals.classes.Usuario;
import leandro.caixeta.relftgoals.classes.usuariosControler;

public class CadaEdit2 extends AppCompatActivity {

    private Button cadastrar ;
    private EditText email , senha, confirSenha;
    private FirebaseAuth usuarioFireAuth =  FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ProgressBar progressBarCadstUsu ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cada_edit2);

        //ligando os componentes as suas respectivas variaveis
        cadastrar = findViewById(R.id.btnCadastro);
        email = findViewById(R.id.edtEmail);
        senha = findViewById(R.id.edtSenha);
        confirSenha = findViewById(R.id.edtConfSenha);
        progressBarCadstUsu = findViewById(R.id.progressBarCadstUsu);

        //recuperando variaveis traziadas da primeira parte do cadastro
        Bundle dadosRecebidos =  getIntent().getExtras();
        final Usuario usuario = (Usuario)dadosRecebidos.getSerializable("Obejct");



        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ativarDesativarProgreBar(1);

                //receperando os valores digitados pelo usuario
                String emailRecb = email.getText().toString();
                String senhaRecb = senha.getText().toString();
                String confirSenhaRecb = confirSenha.getText().toString();

                //verificando se os valores estao prenchidos

                //verificando o email
                if(!usuario.setEmail(emailRecb)){
                    Toast.makeText(getApplicationContext(),"O campo e-mail não pode estar vazio!",Toast.LENGTH_SHORT).show();
                    email.requestFocus();
                    ativarDesativarProgreBar(2);
                }else{
                    ativarDesativarProgreBar(1);
                    //verificando o campo senha

                    switch (usuario.setSenha(senhaRecb)){
                        case 1:

                            Toast.makeText(getApplicationContext(),"O campo senha não pode estar vazio!",Toast.LENGTH_SHORT).show();
                            senha.requestFocus();
                            ativarDesativarProgreBar(2);
                            break;
                        case 2:

                            Toast.makeText(getApplicationContext(),"O campo senha deve possuir mais que 6 caracteres!",Toast.LENGTH_SHORT).show();
                            senha.requestFocus();
                            ativarDesativarProgreBar(2);
                            break;

                        default:
                            //verificando o campo confirmarSenha
                            if(!usuario.setConfSenha(confirSenhaRecb)){
                                Toast.makeText(getApplicationContext(),"O campo confirmar senha não pode estar vazio!",Toast.LENGTH_SHORT).show();
                                confirSenha.requestFocus();
                                ativarDesativarProgreBar(2);
                            }else{

                                //verificando se o campo senha é confirmar senha sao iguais
                                if(!usuario.verificandoSenha()){
                                    Toast.makeText(getApplicationContext(),"O campos senha e confirmar senha devem ser iguais! ",Toast.LENGTH_SHORT).show();
                                    confirSenha.requestFocus();
                                    ativarDesativarProgreBar(2);
                                }else{






                                    //gravando os dados no authentication do firebase

                                    usuarioFireAuth.createUserWithEmailAndPassword(usuario.getEmail(),usuario.getSenha())
                                            .addOnCompleteListener(CadaEdit2.this, new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    if(task.isSuccessful()){

                                                        //setando o uid do usuario atual
                                                        usuario.SetarUidUsuarioAtual();


                                                        //gerando a intancia do banco FirebaseFirestore
                                                        FirebaseFirestore db = FirebaseFirestore.getInstance();

                                                        //passando os dados da class
                                                        String nomedb = usuario.getNome();
                                                        String uid = usuario.getUid();
                                                        String datadb = usuario.getData();
                                                        String emailUid = usuario.getEmail();

                                                        //criando o mapa para enviar ao bd
                                                        Map<String , Object> dadosbd =  new HashMap<>();
                                                        dadosbd.put("Nome",nomedb);
                                                        dadosbd.put("Data aniversario",datadb);
                                                        dadosbd.put("Tipo","normal");

                                                        //gravando dados no cloud Firestore

                                                        db.collection("Usuarios")
                                                                .document(uid).set(dadosbd)
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @RequiresApi(api = Build.VERSION_CODES.P)
                                                            @Override
                                                            public void onSuccess(Void aVoid) {


                                                                //cadastrando no banco local
                                                                cadastrandoBDLocal(usuario);



                                                                ativarDesativarProgreBar(2);

                                                                usuario.setUid(FirebaseAuth.getInstance().getUid());








                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(getApplicationContext(),"Erro ao realizar o cadastro! "+ e,Toast.LENGTH_LONG).show();
                                                            }
                                                        });

                                                    }else{
                                                        try{
                                                            throw task.getException();
                                                        }catch (FirebaseAuthWeakPasswordException e){

                                                            Toast.makeText(getApplicationContext(),"Senha fraca!! Sua senha deve possuir 6 ou mais caracteres! ",Toast.LENGTH_LONG).show();
                                                            senha.requestFocus();
                                                            ativarDesativarProgreBar(2);

                                                        }catch (FirebaseAuthInvalidCredentialsException e){
                                                            Toast.makeText(getApplicationContext(),"O e-mail inserido é inválido! "+e,Toast.LENGTH_LONG).show();
                                                            email.requestFocus();
                                                            ativarDesativarProgreBar(2);
                                                        }catch (FirebaseAuthUserCollisionException e){
                                                            Toast.makeText(getApplicationContext(),"E-mail já cadastrado no nosso sistema! ",Toast.LENGTH_LONG).show();
                                                            email.requestFocus();
                                                            ativarDesativarProgreBar(2);
                                                        }
                                                        catch (Exception e){
                                                            Toast.makeText(getApplicationContext(),"Um erro inesperado aconteceu! Para mais informações entre em contato conosco!"+e,Toast.LENGTH_LONG).show();
                                                            ativarDesativarProgreBar(2);
                                                        }
                                                    }
                                                }
                                            });







                                }
                            }
                            break;
                    }







                }
            }
        });//termino do evento de click do botao cadastrar

    }


    @RequiresApi(api = Build.VERSION_CODES.P)
    public void cadastrandoBDLocal(Usuario usuario){

        int resultaod = 0 ;

        usuariosControler controler = new usuariosControler(getApplicationContext());
        // Usuario usuarioRecb = controler.buscarUsuario(usuario.getEmail());

        Usuario usuarioRecb = controler.verificarqtdRegistros();

        if(usuarioRecb.getNome() == null){
            //  cadastrarUsuario(usuario);
            //se nao exitir nenhum cadastro no bd devera ser feito o cadastro duas vezes a fim de arumar o erro de criação ok
            controler.insereUsuario(usuario);
            Toast.makeText(getApplicationContext(),"Cadastro efetuado com sucesso ",Toast.LENGTH_LONG).show();

        }else{

            usuariosControler usucont = new usuariosControler(getApplicationContext());
            //Realizando uma busca no banco com o email do usuario que esta logando, caso seja retornado algo quer dize que ele ja posui conta, se nao devemos criar uma
           int result = usucont.insereUsuario(usuario);

           if(result == 1){
               finish();
               startActivity(new Intent(getApplicationContext(),home.class));
               Toast.makeText(getApplicationContext(),"Cadastro efetuado com sucesso ",Toast.LENGTH_LONG).show();

           }

          //  Toast.makeText(getApplicationContext(),"Cadastro efetuado com sucesso "+ usuario.getNome(),Toast.LENGTH_LONG).show();

        }



    }//fechamento da funçao verificarUsuario


    public void ativarDesativarProgreBar(int situacao){
        //Nessa função sera feito a ativação e a desativação da barra de progresso
        //alem de desabilitar o botao e os campos na hora que a ProgresBar estiver liberada,
        //quando essa estiver invisvel os campos voltaram ao seu estado padrao
        if(situacao == 1){
            //se for 1, quer dizer que vamos ativar a progresBar e desativar os componentes
            progressBarCadstUsu.setVisibility(View.VISIBLE);
            cadastrar.setEnabled(false);
            email.setEnabled(false);
            senha.setEnabled(false);
            confirSenha.setEnabled(false);
        }else{
          //se nao vamos desligar a progresBar e ativar os componentes
            progressBarCadstUsu.setVisibility(View.GONE);
            cadastrar.setEnabled(true);
            email.setEnabled(true);
            senha.setEnabled(true);
            confirSenha.setEnabled(true);
        }//fechamento do if
    }
}
