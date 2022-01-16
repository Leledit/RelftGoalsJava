package leandro.caixeta.relftgoals.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import leandro.caixeta.relftgoals.R;
import leandro.caixeta.relftgoals.classes.Tarefas;

public class Tarefa_cadas_Edit_p1 extends AppCompatActivity {

        private Spinner spinPrioridades , spinerCategoria;
        private EditText Nome  , descricao ;
        private FloatingActionButton fab ;
        private TextView textTarefaCadasEdit1 ;
        private String acaoPrimar ,voltar = "vazio", Origem , ReloadPos;


        private Tarefas tarefa ;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarefa_cadas_edit_p1);

        //conectando os componentes de designer com a programação
        Nome = findViewById(R.id.edtNomeTarefa);
        descricao = findViewById(R.id.edtDescripTarefa);
        fab =  findViewById(R.id.fabProximo1);
        textTarefaCadasEdit1 = findViewById(R.id.textTarefaCadasEdit1);

        //Trazendo a string que ira definir se essa vai ser uma pagina de adastro ou edição
        Bundle dadosRecebidos = getIntent().getExtras();
        acaoPrimar = dadosRecebidos.getString("AcaoPrimar");
        voltar = dadosRecebidos.getString("Acao");
        Origem = dadosRecebidos.getString("Origem");
        ReloadPos = dadosRecebidos.getString("ReloadPos");






        //verificando qual é a ação que essa pagina ira fazer

        tarefa = new Tarefas();

        if((acaoPrimar.equals("Cadastro_dia"))||(acaoPrimar.equals("cadastro"))){

            textTarefaCadasEdit1.setText("Cadastrando tarefas para hoje ");



        }else if(acaoPrimar.equals("Editar") ){

            tarefa = (Tarefas) dadosRecebidos.getSerializable("object");
            textTarefaCadasEdit1.setText("Editando a Tarefa: "+tarefa.getNome());

            //setando os valores nos componentes
            Nome.setText(tarefa.getNome());

            if(!tarefa.getDescricao().equals("Nenhuma descrição")){
                descricao.setText(tarefa.getDescricao());
            }

        } else{

            textTarefaCadasEdit1.setText("Cadastrando uma nova tarefa");

        }

         //verificando se o botao voltar foi selecionado
       if(voltar != null){

           tarefa = (Tarefas) dadosRecebidos.getSerializable("object");

            Nome.setText(tarefa.getNome());
            if(!tarefa.getDescricao().equals("Nenhuma descrição")){
                descricao.setText(tarefa.getDescricao());
            }
        }


       fab.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               //recebendo os valores digitado pelo usuario
               String nomeRecb =  Nome.getText().toString();
               String descricaoRecb = descricao.getText().toString();


               int statusNome = tarefa.setNome(nomeRecb);
               int statusDescrip = tarefa.setDescricao(descricaoRecb);

               if(statusNome == 1){
                   Toast.makeText(getApplicationContext(), "O campo nome é obrigatório!", Toast.LENGTH_SHORT).show();
                   Nome.requestFocus();
               }
               else if(statusNome == 2) {
                   Toast.makeText(getApplicationContext(), "O campo Nome Deve posuir mais que 5 caracteres ", Toast.LENGTH_SHORT).show();
                   Nome.requestFocus();
               }else if (statusNome == 3) {
                   Toast.makeText(getApplicationContext(), "O campo Nome  é muito grande. por favor reduza a quantidade de caracteres  ", Toast.LENGTH_SHORT).show();
                   Nome.requestFocus();
               }else {

                 if(statusDescrip == 1){
                     Toast.makeText(getApplicationContext(), "O campo Descricao deve posui mais de 5 caracteres ", Toast.LENGTH_SHORT).show();
                     descricao.requestFocus();
                   }else if (statusDescrip == 2){
                     Toast.makeText(getApplicationContext(), "O campo Descricao  é muito grande . por favor reduza a quantidade de caracteres ", Toast.LENGTH_SHORT).show();
                     descricao.requestFocus();
                 }else {

                     Intent proxima_Cadastro = new Intent(getApplicationContext(), Tarefa_cadas_Edit_p2.class);
                     Intent proxima_Cadastro_dia = new Intent(getApplicationContext(), Tarefa_cadas_Edit_p3.class);


                     if (acaoPrimar.equals("cadastro")) {

                         proxima_Cadastro_dia.putExtra("object", tarefa);
                         proxima_Cadastro_dia.putExtra("Acao", acaoPrimar);
                         proxima_Cadastro_dia.putExtra("Origem",Origem);
                         proxima_Cadastro_dia.putExtra("ReloadPos",ReloadPos);
                         finish();
                         startActivity(proxima_Cadastro_dia);

                     }else{
                         proxima_Cadastro.putExtra("object", tarefa);
                         proxima_Cadastro.putExtra("Acao", acaoPrimar);
                         proxima_Cadastro.putExtra("Origem",Origem);
                         proxima_Cadastro.putExtra("ReloadPos",ReloadPos);
                         finish();
                         startActivity(proxima_Cadastro);
                     }

                 }





               }

           }
       });




    }


    public void onBackPressed() {

        if((acaoPrimar.equals("cadastro"))||(acaoPrimar.equals("cadastro_data"))){

            finish();

        }else{
            Intent voltar = new Intent(getApplicationContext(),Tarefa_info.class);
            voltar.putExtra("object_tarefa",tarefa);
            voltar.putExtra("Origem",Origem);
            voltar.putExtra("Acao",acaoPrimar);

           /* switch (acaoPage) {

            case "Cadastro_dia":

                voltar.putExtra("Acao","buscarDia");

                break;
            case "Cadastro_semana":
                voltar.putExtra("Acao","buscarSemana");
                break;
            case "Cadastro_mes":
                voltar.putExtra("Acao","buscarMes");
                break;
            case "Cadastro_ano":
                voltar.putExtra("Acao","buscarAno");

                break;

        }*/
     finish();

     startActivity(voltar);

        }
    }





}
