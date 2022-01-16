package leandro.caixeta.relftgoals.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import leandro.caixeta.relftgoals.R;
import leandro.caixeta.relftgoals.classes.Tarefas;
import leandro.caixeta.relftgoals.classes.Usuario;
import leandro.caixeta.relftgoals.classes.Utilities;
import leandro.caixeta.relftgoals.classes.relatoriosControler;
import leandro.caixeta.relftgoals.classes.tarefasControler;

public class Tarefa_cadas_Edit_p4 extends AppCompatActivity  {

    private LinearLayout catFinanceiro, catViagens, catEscolar, catTrabalho, catFamilia, catLazer, catPessoal, catSocial, catOutros;
    private String categoriaEscolhida ="sem categoria" , acaoPage , Origem , ReloadPos;
    private Button btnEnviarTarefa;
    private FirebaseFirestore dbFirestore = FirebaseFirestore.getInstance();
    private FloatingActionButton voltar ;
    private Tarefas tarefa;
    private TextView textTarefaCadasEdit4;
    private ProgressBar progressBarCadastreTaref ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarefa_cadas_edit_p4);

        //recebendo os valores digitados na pagina anterior
        Bundle dadosRecebidos = getIntent().getExtras();
        tarefa = (Tarefas) dadosRecebidos.getSerializable("object");
        acaoPage = dadosRecebidos.getString("Acao");
        Origem = dadosRecebidos.getString("Origem");
        ReloadPos = dadosRecebidos.getString("ReloadPos");



        catFinanceiro = findViewById(R.id.LinearFinanceiro);
        catViagens = findViewById(R.id.LinearViagens);
        catEscolar = findViewById(R.id.LinearEscolar);
        catTrabalho = findViewById(R.id.LinearTrabalho);
        catFamilia = findViewById(R.id.LinearFamilia);
        catLazer = findViewById(R.id.LinearLazer);
        catPessoal = findViewById(R.id.LinearPessoal);
        catSocial = findViewById(R.id.LinearSocial);
        catOutros = findViewById(R.id.LinearOutros);
        btnEnviarTarefa = findViewById(R.id.btnEnviarTarefa);
        voltar = findViewById(R.id.fabMV3);
        textTarefaCadasEdit4 = findViewById(R.id.textTarefaCadasEdit4);
        progressBarCadastreTaref = findViewById(R.id.progressBarCadastreTaref);

        //verificando qual é a ação que essa pagina ira fazer
        if(acaoPage.equals("Editar")){

            //setando os valores nos componentes

            textTarefaCadasEdit4.setText("Editando a tarefa: "+tarefa.getNome());
            //verificando qual categoria pertence a tarefa
            switch (tarefa.getCategoria()){
                case "Financeiro":
                     categoriaEscolhida = "Financeiro";
                    catFinanceiro.setBackground(getResources().getDrawable(R.drawable.btn_bordas_arendondadas));
                    break;
                case "Viagens":
                    categoriaEscolhida = "Viagens";
                    catViagens.setBackground(getResources().getDrawable(R.drawable.btn_bordas_arendondadas));
                    break;
                case "Escolar":
                    categoriaEscolhida = "Escolar";
                    catEscolar.setBackground(getResources().getDrawable(R.drawable.btn_bordas_arendondadas));
                    break;
                case "Trabalho":
                    categoriaEscolhida = "Trabalho";
                    catTrabalho.setBackground(getResources().getDrawable(R.drawable.btn_bordas_arendondadas));
                    break;
                case "Familia":
                    categoriaEscolhida = "Familia";
                    catFamilia.setBackground(getResources().getDrawable(R.drawable.btn_bordas_arendondadas));
                    break;
                case "Lazer":
                    categoriaEscolhida = "Lazer";
                    catLazer.setBackground(getResources().getDrawable(R.drawable.btn_bordas_arendondadas));
                    break;
                case "Pessoal":
                    categoriaEscolhida = "Pessoal";
                    catPessoal.setBackground(getResources().getDrawable(R.drawable.btn_bordas_arendondadas));
                    break;
                case "Social":
                    categoriaEscolhida = "Social";
                    catSocial.setBackground(getResources().getDrawable(R.drawable.btn_bordas_arendondadas));
                    break;
                case "Outros":
                    categoriaEscolhida = "Outros";
                    catOutros.setBackground(getResources().getDrawable(R.drawable.btn_bordas_arendondadas));
                    break;

            }
            btnEnviarTarefa.setText("Editar");

        }else if(acaoPage.equals("Cadastro_dia")){
           textTarefaCadasEdit4.setText("Cadastrado Tarefa para hoje.");
            btnEnviarTarefa.setText("Cadastrar");
        }else {
            textTarefaCadasEdit4.setText("Cadastrando uma nova tarefa");
            btnEnviarTarefa.setText("Cadastrar");
        }



        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent IntentVoltar = new Intent(getApplicationContext(), Tarefa_cadas_Edit_p3.class);
                IntentVoltar.putExtra("voltar","voltar");
                IntentVoltar.putExtra("object",tarefa);
                IntentVoltar.putExtra("Acao",acaoPage);
                IntentVoltar.putExtra("AcaoPrimar",acaoPage);
                IntentVoltar.putExtra("Origem",Origem);
                finish();
                startActivity(IntentVoltar);
            }
        });
        //criando evento de click para o botao de cadastrar Tarefa

        btnEnviarTarefa.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onClick(View v) {

                ativarDesativarProgreBar(1);

                if (!tarefa.setCategoria(categoriaEscolhida)) {
                    Toast.makeText(getApplicationContext(), "é necessario escolher um categoria para realizar o cadastro ", Toast.LENGTH_LONG).show();
                    ativarDesativarProgreBar(0);
                } else {

                    //intanciando a classe Ultilities.java
                    Utilities utilities = new Utilities();

                    //recuperado uid do usuario logado atualmente no app e adicionando a intancia da classe de tarefa
                    Usuario usuario = new Usuario();
                    tarefa.setUid_usuario(usuario.retornarUidUsuarioLogado());

                    //setando o status atual da Tarefa . quando essa é cadastrada ela fica com o status de "Ativa"
                    tarefa.setStatus("Ativa");

                    //buscado a data atual com fusoHorario
                    String date = utilities.recuperarDataAtual();

                    int resultadoEnvio ;
                    if(acaoPage.equals("Editar")){
                        /** Edição de uma tarefa ja existente **/

                        tarefasControler controlerEdicao = new tarefasControler(getApplicationContext());
                        resultadoEnvio =  controlerEdicao.EditarTarefa(tarefa);

                        ativarDesativarProgreBar(0);
                    }else {
                        /** Cadastrando uma nova tarefa **/

                        /**Adiconando mais um a qtd_total de tarefas desse mes(Relatorios) **/
                        //aqui é feito uma verificação que ve se exitem algum relatorio para o mes corrente(ativo) se nao existir é cadastrado um novo
                        relatoriosControler  relatControl = new relatoriosControler(getApplicationContext());
                        relatControl.verifarExistMes();

                        //aqui é chamado uma função que busca o relatorio ativo e muda alguns dos seus dados
                        relatControl.alterarContadorRelat("Cadastro",getApplicationContext());



                        //verificando se a tarefa foi cadastrada para hoje , se sim sera feita a setagem
                        if (acaoPage.equals("cadastro")) {
                            tarefa.setData_final(date);
                            tarefa.setUid_usuario(FirebaseAuth.getInstance().getUid());

                        }

                        //setando a data atual dentro da intancia da tarefa
                        tarefa.setData_inicial(date);

                        //Criando uma inatncia da classe controle de tarefas
                        tarefasControler controlerCadastro = new tarefasControler(getBaseContext());
                        //Criando uma intancia da classe controle para fazer a busca
                        tarefasControler controlerTAF = new tarefasControler(getApplicationContext());
                        //fazendo uma busca no banco para localizar tarefas
                        Tarefas tarefaBusca = controlerTAF.buscarTarefa();
                        //caso o nome da tarefa seja null quer dizer que nao temos tarefas no bd(dai vamos cadastrar duas)

                        //passando os dados para a funçao que os envia para o banco(é retornado se deu certo ou deu erro)
                        resultadoEnvio = controlerCadastro.insereTarefa(tarefa);


                    }//fechamento do else da verificação da ação da pagina (Cadastro ou edição)

                        if(resultadoEnvio == 1){

                            Intent start ;
                            start = new Intent(getApplicationContext(),home.class);

                            if ((acaoPage.equals("cadastro"))||(acaoPage.equals(""))||acaoPage.equals("cadastro_data")) {
                                Toast.makeText(getApplicationContext(), "Tarefa cadastrada com sucesso", Toast.LENGTH_LONG).show();

                                if(!Origem.equals("FragHome")){

                                    start = new Intent(getApplicationContext(),Visao_geral_list.class);
                                    start.putExtra("Acao",ReloadPos);

                                }


                            }else{
                                Toast.makeText(getApplicationContext(), "Tarefa Editada com sucesso", Toast.LENGTH_LONG).show();

                                start.putExtra("Acao",acaoPage);
                            }


                            finish();
                            startActivity(start);
                            ativarDesativarProgreBar(0);

                            // o codigo abaixo é responsavel por definir para onde o usuario sera mandado





                        }else{
                            Toast.makeText(getApplicationContext(),"Erro ao cadastrar a tarefa ",Toast.LENGTH_LONG).show();

                        }














                }
            }

        });


        //criando evento de click para todos as categorias

        catFinanceiro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoriaEscolhida = "Financeiro";
                catFinanceiro.setBackground(getResources().getDrawable(R.drawable.btn_bordas_arendondadas));
                catViagens.setBackground(getResources().getDrawable(R.drawable.btn_branco));
                catEscolar.setBackground(getResources().getDrawable(R.drawable.btn_branco));
                catTrabalho.setBackground(getResources().getDrawable(R.drawable.btn_branco));
                catFamilia.setBackground(getResources().getDrawable(R.drawable.btn_branco));
                catLazer.setBackground(getResources().getDrawable(R.drawable.btn_branco));
                catPessoal.setBackground(getResources().getDrawable(R.drawable.btn_branco));
                catSocial.setBackground(getResources().getDrawable(R.drawable.btn_branco));
                catOutros.setBackground(getResources().getDrawable(R.drawable.btn_branco));
            }
        });
        catViagens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoriaEscolhida = "Viagens";
                catFinanceiro.setBackground(getResources().getDrawable(R.drawable.btn_branco));
                catViagens.setBackground(getResources().getDrawable(R.drawable.btn_bordas_arendondadas));
                catEscolar.setBackground(getResources().getDrawable(R.drawable.btn_branco));
                catTrabalho.setBackground(getResources().getDrawable(R.drawable.btn_branco));
                catFamilia.setBackground(getResources().getDrawable(R.drawable.btn_branco));
                catLazer.setBackground(getResources().getDrawable(R.drawable.btn_branco));
                catPessoal.setBackground(getResources().getDrawable(R.drawable.btn_branco));
                catSocial.setBackground(getResources().getDrawable(R.drawable.btn_branco));
                catOutros.setBackground(getResources().getDrawable(R.drawable.btn_branco));
            }
        });
        catEscolar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoriaEscolhida = "Escolar";
                catFinanceiro.setBackground(getResources().getDrawable(R.drawable.btn_branco));
                catViagens.setBackground(getResources().getDrawable(R.drawable.btn_branco));
                catEscolar.setBackground(getResources().getDrawable(R.drawable.btn_bordas_arendondadas));
                catTrabalho.setBackground(getResources().getDrawable(R.drawable.btn_branco));
                catFamilia.setBackground(getResources().getDrawable(R.drawable.btn_branco));
                catLazer.setBackground(getResources().getDrawable(R.drawable.btn_branco));
                catPessoal.setBackground(getResources().getDrawable(R.drawable.btn_branco));
                catSocial.setBackground(getResources().getDrawable(R.drawable.btn_branco));
                catOutros.setBackground(getResources().getDrawable(R.drawable.btn_branco));
            }
        });
        catTrabalho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoriaEscolhida = "Trabalho";
                catFinanceiro.setBackground(getResources().getDrawable(R.drawable.btn_branco));
                catViagens.setBackground(getResources().getDrawable(R.drawable.btn_branco));
                catEscolar.setBackground(getResources().getDrawable(R.drawable.btn_branco));
                catTrabalho.setBackground(getResources().getDrawable(R.drawable.btn_bordas_arendondadas));
                catFamilia.setBackground(getResources().getDrawable(R.drawable.btn_branco));
                catLazer.setBackground(getResources().getDrawable(R.drawable.btn_branco));
                catPessoal.setBackground(getResources().getDrawable(R.drawable.btn_branco));
                catSocial.setBackground(getResources().getDrawable(R.drawable.btn_branco));
                catOutros.setBackground(getResources().getDrawable(R.drawable.btn_branco));
            }
        });
        catFamilia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoriaEscolhida = "Familia";
                catFinanceiro.setBackground(getResources().getDrawable(R.drawable.btn_branco));
                catViagens.setBackground(getResources().getDrawable(R.drawable.btn_branco));
                catEscolar.setBackground(getResources().getDrawable(R.drawable.btn_branco));
                catTrabalho.setBackground(getResources().getDrawable(R.drawable.btn_branco));
                catFamilia.setBackground(getResources().getDrawable(R.drawable.btn_bordas_arendondadas));
                catLazer.setBackground(getResources().getDrawable(R.drawable.btn_branco));
                catPessoal.setBackground(getResources().getDrawable(R.drawable.btn_branco));
                catSocial.setBackground(getResources().getDrawable(R.drawable.btn_branco));
                catOutros.setBackground(getResources().getDrawable(R.drawable.btn_branco));
            }
        });
        catLazer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoriaEscolhida = "Lazer";
                catFinanceiro.setBackground(getResources().getDrawable(R.drawable.btn_branco));
                catViagens.setBackground(getResources().getDrawable(R.drawable.btn_branco));
                catEscolar.setBackground(getResources().getDrawable(R.drawable.btn_branco));
                catTrabalho.setBackground(getResources().getDrawable(R.drawable.btn_branco));
                catFamilia.setBackground(getResources().getDrawable(R.drawable.btn_branco));
                catLazer.setBackground(getResources().getDrawable(R.drawable.btn_bordas_arendondadas));
                catPessoal.setBackground(getResources().getDrawable(R.drawable.btn_branco));
                catSocial.setBackground(getResources().getDrawable(R.drawable.btn_branco));
                catOutros.setBackground(getResources().getDrawable(R.drawable.btn_branco));
            }
        });
        catPessoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoriaEscolhida = "Pessoal";
                catFinanceiro.setBackground(getResources().getDrawable(R.drawable.btn_branco));
                catViagens.setBackground(getResources().getDrawable(R.drawable.btn_branco));
                catEscolar.setBackground(getResources().getDrawable(R.drawable.btn_branco));
                catTrabalho.setBackground(getResources().getDrawable(R.drawable.btn_branco));
                catFamilia.setBackground(getResources().getDrawable(R.drawable.btn_branco));
                catLazer.setBackground(getResources().getDrawable(R.drawable.btn_branco));
                catPessoal.setBackground(getResources().getDrawable(R.drawable.btn_bordas_arendondadas));
                catSocial.setBackground(getResources().getDrawable(R.drawable.btn_branco));
                catOutros.setBackground(getResources().getDrawable(R.drawable.btn_branco));
            }
        });
        catSocial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoriaEscolhida = "Social";
                catFinanceiro.setBackground(getResources().getDrawable(R.drawable.btn_branco));
                catViagens.setBackground(getResources().getDrawable(R.drawable.btn_branco));
                catEscolar.setBackground(getResources().getDrawable(R.drawable.btn_branco));
                catTrabalho.setBackground(getResources().getDrawable(R.drawable.btn_branco));
                catFamilia.setBackground(getResources().getDrawable(R.drawable.btn_branco));
                catLazer.setBackground(getResources().getDrawable(R.drawable.btn_branco));
                catPessoal.setBackground(getResources().getDrawable(R.drawable.btn_branco));
                catSocial.setBackground(getResources().getDrawable(R.drawable.btn_bordas_arendondadas));
                catOutros.setBackground(getResources().getDrawable(R.drawable.btn_branco));
            }
        });
        catOutros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoriaEscolhida = "Outros";
                catFinanceiro.setBackground(getResources().getDrawable(R.drawable.btn_branco));
                catViagens.setBackground(getResources().getDrawable(R.drawable.btn_branco));
                catEscolar.setBackground(getResources().getDrawable(R.drawable.btn_branco));
                catTrabalho.setBackground(getResources().getDrawable(R.drawable.btn_branco));
                catFamilia.setBackground(getResources().getDrawable(R.drawable.btn_branco));
                catLazer.setBackground(getResources().getDrawable(R.drawable.btn_branco));
                catPessoal.setBackground(getResources().getDrawable(R.drawable.btn_branco));
                catSocial.setBackground(getResources().getDrawable(R.drawable.btn_branco));
                catOutros.setBackground(getResources().getDrawable(R.drawable.btn_bordas_arendondadas));
            }
        });


    }
    public void ativarDesativarProgreBar(int situacao){
        //Nessa função sera feito a ativação e a desativação da barra de progresso
        //alem de desabilitar o botao e os campos na hora que a ProgresBar estiver liberada,
        //quando essa estiver invisvel os campos voltaram ao seu estado padrao
        if(situacao == 1){
            //se for 1, quer dizer que vamos ativar a progresBar e desativar os componentes
            progressBarCadastreTaref.setVisibility(View.VISIBLE);
            btnEnviarTarefa.setEnabled(false);
        }else{
            //se nao vamos desligar a progresBar e ativar os componentes
            progressBarCadastreTaref.setVisibility(View.GONE);
            btnEnviarTarefa.setEnabled(true);
        }//fechamento do if
    }



}
