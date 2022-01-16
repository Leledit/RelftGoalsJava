package leandro.caixeta.relftgoals.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import leandro.caixeta.relftgoals.R;
import leandro.caixeta.relftgoals.classes.Tarefas;

public class Tarefa_cadas_Edit_p3 extends AppCompatActivity  {

    private  LinearLayout muitoAlto , alto, medio , baixo ;
    private String estadoSelecionado = "Sem estado" ;
    private FloatingActionButton fabMP3 , fabMV2 ;
    private TextView  textTarefaCadasEdit3 ;
    private String acaoPage ,    voltarAcao =null , Origem , ReloadPos;
    private Tarefas tarefa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarefa_cadas_edit_p3);

        //ligando as variaveis do designer a programção

        muitoAlto = findViewById(R.id.LineraMuitoAlto);
        alto = findViewById(R.id.LinerarAlto);
        medio = findViewById(R.id.LinerarMedio);
        baixo = findViewById(R.id.LinerarBaixo);
        fabMP3 = findViewById(R.id.fabMP3);
        fabMV2 = findViewById(R.id.fabMV2);
        textTarefaCadasEdit3 = findViewById(R.id.textTarefaCadasEdit3);


        //recebendo os valores digitados na pagina anterior
        Bundle dadosRecebidos = getIntent().getExtras();
        tarefa = (Tarefas) dadosRecebidos.getSerializable("object");
        acaoPage = dadosRecebidos.getString("Acao");
        voltarAcao = dadosRecebidos.getString("voltar");
        Origem = dadosRecebidos.getString("Origem");
        ReloadPos = dadosRecebidos.getString("ReloadPos");



        //verificando se o botao voltar foi selecionado
        if(voltarAcao != null){
            tarefa  = (Tarefas) dadosRecebidos.getSerializable("object");


            switch (tarefa.getPrioridade()){

                case "Muito alto":
                    estadoSelecionado = "Muito alto";
                    muitoAlto.setBackground(getResources().getDrawable(R.drawable.btn_bordas_arendondadas));
                    break;
                case "alto":
                    alto.setBackground(getResources().getDrawable(R.drawable.btn_bordas_arendondadas));
                    estadoSelecionado = "alto";
                    break;
                case "Medio":
                    medio.setBackground(getResources().getDrawable(R.drawable.btn_bordas_arendondadas));
                    estadoSelecionado = "Medio";
                    break;
                case "Baixo":
                    baixo.setBackground(getResources().getDrawable(R.drawable.btn_bordas_arendondadas));
                    estadoSelecionado = "Baixo";
                    break;
            }
        }

        if(acaoPage.equals("Editar")){



            //setando os valores nos componentes

            switch (tarefa.getPrioridade()){

                case "Muito alto":
                    estadoSelecionado = "Muito alto";
                    muitoAlto.setBackground(getResources().getDrawable(R.drawable.btn_bordas_arendondadas));
                    break;
                case "alto":
                    alto.setBackground(getResources().getDrawable(R.drawable.btn_bordas_arendondadas));
                    estadoSelecionado = "alto";
                    break;
                case "Medio":
                    medio.setBackground(getResources().getDrawable(R.drawable.btn_bordas_arendondadas));
                    estadoSelecionado = "Medio";
                    break;
                case "Baixo":
                    baixo.setBackground(getResources().getDrawable(R.drawable.btn_bordas_arendondadas));
                    estadoSelecionado = "Baixo";
                    break;
            }
            textTarefaCadasEdit3.setText("Editando a Tarefa: "+tarefa.getNome());

        } else if(acaoPage.equals("Cadastro_dia")){
            textTarefaCadasEdit3.setText("Cadastrado Tarefa para hoje.");

        }else {
            textTarefaCadasEdit3.setText("Cadastrando uma nova Tarefa");
        }

        fabMV2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent IntentVoltar = new Intent(getApplicationContext(),Tarefa_cadas_Edit_p2.class);
                Intent IntentVoltar_dia = new Intent(getApplicationContext(),Tarefa_cadas_Edit_p1.class);

                if (acaoPage.equals("Cadastro_dia")||(acaoPage.equals("cadastro"))) {

                    IntentVoltar_dia.putExtra("voltar","voltar");
                    IntentVoltar_dia.putExtra("object",tarefa);
                    IntentVoltar_dia.putExtra("Acao",acaoPage);
                    IntentVoltar_dia.putExtra("AcaoPrimar",acaoPage);
                    IntentVoltar_dia.putExtra("Origem",Origem);
                    IntentVoltar_dia.putExtra("ReloadPos",ReloadPos);
                    finish();
                    startActivity(IntentVoltar_dia);

                }else{

                    IntentVoltar.putExtra("voltar","voltar");
                    IntentVoltar.putExtra("object",tarefa);
                    IntentVoltar.putExtra("Acao",acaoPage);
                    IntentVoltar.putExtra("AcaoPrimar",acaoPage);
                    IntentVoltar.putExtra("Origem",Origem);
                    IntentVoltar.putExtra("ReloadPos",ReloadPos);
                    finish();
                    startActivity(IntentVoltar);

                }




            }
        });


        fabMP3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!tarefa.setPrioridade(estadoSelecionado)){
                    Toast.makeText(getApplicationContext(), "É necessario escolher a prioridade da Tarefa", Toast.LENGTH_LONG).show();

                }else {

                    Intent Proximo = new Intent(getApplicationContext(), Tarefa_cadas_Edit_p4.class);
                        Proximo.putExtra("Acao",acaoPage);
                        Proximo.putExtra("object",tarefa);
                        Proximo.putExtra("Origem",Origem);
                        Proximo.putExtra("ReloadPos",ReloadPos);
                        finish();
                        startActivity(Proximo);

                }
            }
        });


        muitoAlto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                estadoSelecionado = "Muito alto";
                muitoAlto.setBackground(getResources().getDrawable(R.drawable.btn_bordas_arendondadas));
                alto.setBackground(getResources().getDrawable(R.drawable.btn_branco));
                 medio.setBackground(getResources().getDrawable(R.drawable.btn_branco));
                baixo.setBackground(getResources().getDrawable(R.drawable.btn_branco));

            }
        });

        alto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                estadoSelecionado = "alto";
                alto.setBackground(getResources().getDrawable(R.drawable.btn_bordas_arendondadas));
                muitoAlto.setBackground(getResources().getDrawable(R.drawable.btn_branco));
                medio.setBackground(getResources().getDrawable(R.drawable.btn_branco));
                baixo.setBackground(getResources().getDrawable(R.drawable.btn_branco));

            }
        });

        medio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                estadoSelecionado = "Medio";
                medio.setBackground(getResources().getDrawable(R.drawable.btn_bordas_arendondadas));
                muitoAlto.setBackground(getResources().getDrawable(R.drawable.btn_branco));
                alto.setBackground(getResources().getDrawable(R.drawable.btn_branco));
                baixo.setBackground(getResources().getDrawable(R.drawable.btn_branco));
            }
        });

        baixo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                estadoSelecionado = "Baixo";
                baixo.setBackground(getResources().getDrawable(R.drawable.btn_bordas_arendondadas));
                muitoAlto.setBackground(getResources().getDrawable(R.drawable.btn_branco));
                alto.setBackground(getResources().getDrawable(R.drawable.btn_branco));
                medio.setBackground(getResources().getDrawable(R.drawable.btn_branco));

            }
        });



    }




    }

