package leandro.caixeta.relftgoals.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import leandro.caixeta.relftgoals.R;
import leandro.caixeta.relftgoals.classes.Tarefas;
import leandro.caixeta.relftgoals.classes.Usuario;
import leandro.caixeta.relftgoals.classes.relatoriosControler;
import leandro.caixeta.relftgoals.classes.tarefasControler;

public class Tarefa_info extends AppCompatActivity {

     private TextView textTarefaCriada,textTarefaTermino,textTarefaStatus,textTarefaDescricao,textTarefaCategoria ,textViewNomeTarefa;
     private ImageView textTarefaPrioridade;
     private Button btnTarefaAlterar ,btnTarefaRestricoes ,btnTarefaFinalizar ;
     private Tarefas tarefaEscolhida ;
     private FirebaseFirestore dbfire = FirebaseFirestore.getInstance();
     private String Acao , acaoIN ;
     private LinearLayout linearLayoutOpsFinAlt ;
     private String origem ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_tarefa);

        //Ligando os compoenentes visuais as suas respectivas variaveis

        textTarefaCriada = findViewById(R.id.textTarefaCriada);
        textTarefaTermino = findViewById(R.id.textTarefaTermino);
        textTarefaStatus = findViewById(R.id.textTarefaStatus);
        textTarefaPrioridade = findViewById(R.id.textTarefaPrioridade);
        textTarefaDescricao = findViewById(R.id.textTarefaDescricao);
        textTarefaCategoria = findViewById(R.id.textTarefaCategoria);
        textViewNomeTarefa = findViewById(R.id.textViewNomeTarefa);

        btnTarefaAlterar = findViewById(R.id.btnTarefaAlterar);
        btnTarefaRestricoes = findViewById(R.id.btnMetaRestricoes);
        btnTarefaFinalizar= findViewById(R.id.btnTarefaFinalizar);

        linearLayoutOpsFinAlt = findViewById(R.id.linearLayoutOpsFinAlt);



        //recebendo os dados da meta que foi selecionada pelo usuario
        Bundle dadosRecebidos = getIntent().getExtras();
        tarefaEscolhida  = (Tarefas) dadosRecebidos.getSerializable("object_tarefa");
        Acao = dadosRecebidos.getString("Acao");
        origem = dadosRecebidos.getString("Origem");
        acaoIN = dadosRecebidos.getString("acaoIN");




        if((Acao.equals("Visao_geral_Espirada"))||(Acao.equals("Visao_geral_Finalizadas"))){
           linearLayoutOpsFinAlt.setVisibility(View.GONE);
        }


        //verificando qual pagina o usuarios estava anteriormente



        //setando os valores no visual

        textViewNomeTarefa.setText(tarefaEscolhida.getNome());
        textTarefaCriada.setText(tarefaEscolhida.getData_inicial());
        textTarefaTermino.setText(tarefaEscolhida.getData_final());
        textTarefaStatus.setText(tarefaEscolhida .getStatus());
        textTarefaDescricao.setText(tarefaEscolhida .getDescricao());
        textTarefaCategoria.setText(tarefaEscolhida .getCategoria());

        switch (tarefaEscolhida .getPrioridade()){

            case "Muito alto":
                textTarefaPrioridade.setImageResource(R.drawable.tarefa_inpornt_vermelho);
                break;
            case "alto":
                textTarefaPrioridade.setImageResource(R.drawable.tarefa_inpornt_salmao);
                break;
            case "Medio":
                textTarefaPrioridade.setImageResource(R.drawable.tarefa_inpornt_azul);
                break;
            case"Baixo":
                textTarefaPrioridade.setImageResource(R.drawable.tarefa_inpornt_verde);
                break;

        }//fechamento do switch das prioridades


        //criando evento de click no botao
        btnTarefaAlterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               Intent editarTarefa = new Intent(getApplicationContext(), Tarefa_cadas_Edit_p1.class);
               editarTarefa.putExtra("object",tarefaEscolhida);
                editarTarefa.putExtra("AcaoPrimar","Editar");
                editarTarefa.putExtra("Origem",origem);
                finish();
                startActivity(editarTarefa);

            }
        });

        btnTarefaRestricoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
        btnTarefaFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              FinalisarTarefa();
            }
        });



        //Toast.makeText(getApplicationContext(),"meta selecionada: "+metas.getNome(),Toast.LENGTH_LONG).show();


    }

    public void onBackPressed() {

        finish();

        Intent intent ;


        if(origem.equals("FragHome")){
            intent = new Intent(getApplicationContext(),home.class);
        }else{
            intent = new Intent(getApplicationContext(),Visao_geral_list.class);
        }

        intent.putExtra("Acao",acaoIN);
        startActivity(intent);

    }
    public void FinalisarTarefa(){
        //criando a intancia da classe dialog
        AlertDialog.Builder dialog = new AlertDialog.Builder(this,R.style.dialog_finalizar_Tarefa);

        //configurando o titulo e a mensgaem que vao aparecer na caixa
        dialog.setTitle("Finalizar tarefa");
        dialog.setMessage("Deseja realmente finalizar a tarefa ?");

        //o comando abaixo nao permite
        dialog.setCancelable(false);

        //definindo um desgner personalizado

        //LayoutInflater é utilizado para inflar nosso layout em uma view.
        //-pegamos nossa instancia da classe
        LayoutInflater li =  getLayoutInflater();

        dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.P)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        tarefasControler controler = new tarefasControler(getApplicationContext());
                        controler.AlterarStatus(tarefaEscolhida, 2);
                        Toast.makeText(getApplicationContext(),"Tarefa Finalisada com sucesso ",Toast.LENGTH_LONG).show();
                        finish();
                        Intent intent ;

                        if(origem.equals("FragHome")){
                            intent = new Intent(getApplicationContext(),home.class);
                        }else{
                            intent = new Intent(getApplicationContext(),Visao_geral_list.class);
                        }

                        /**Adiconando mais um a qtd_Expiradas de tarefas desse mes(Relatorios) **/
                        //aqui é feito uma verificação que ve se exitem algum relatorio para o mes corrente(ativo) se nao existir é cadastrado um novo
                        relatoriosControler relatControl = new relatoriosControler(getApplicationContext());


                        //aqui é chamado uma função que busca o relatorio ativo e muda alguns dos seus dados
                        relatControl.alterarContadorRelat("Finalizada",getApplicationContext());



                        intent.putExtra("Acao",acaoIN);
                        startActivity(intent);


                    }
                });
                dialog.setNegativeButton("Nao", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        //dialog.setView(view);
        dialog.create();
        dialog.show();

    }



}
