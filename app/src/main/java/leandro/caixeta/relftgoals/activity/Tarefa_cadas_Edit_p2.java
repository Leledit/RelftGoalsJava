package leandro.caixeta.relftgoals.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormatter;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import leandro.caixeta.relftgoals.R;
import leandro.caixeta.relftgoals.classes.Tarefas;
import leandro.caixeta.relftgoals.classes.Utilities;

public class Tarefa_cadas_Edit_p2 extends AppCompatActivity  {

    private ImageView imgCalendarioTarefa;
    private int ano , mes ,dia , diafinal ;
    private Calendar calendar;
    private TextView texDataTarefa ,textTarefaCadasEdit2;
    private FloatingActionButton voltar , proximo;
    private String acaoPage , voltarAcao = null  , Origem, ReloadPos;
    private Tarefas tarefa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarefa_cadas_edit_p2);

        //ligando os componentes visuais as suas respectivas variaveis
        imgCalendarioTarefa =  findViewById(R.id.imgCalendarioTarefa);
        texDataTarefa = findViewById(R.id.textDataTarefa);
        proximo = findViewById(R.id.fabMP2);
        voltar = findViewById(R.id.fabMV);
        textTarefaCadasEdit2 = findViewById(R.id.textTarefaCadasEdit2);


        //intanciando a classe de ultilidades
        Utilities utilities = new Utilities();

        //recebendo valores digitados na tela de cadastro 1
        Bundle dadosRecebidos = getIntent().getExtras();
        tarefa = (Tarefas) dadosRecebidos.getSerializable("object");
        acaoPage = dadosRecebidos.getString("Acao");
        Origem = dadosRecebidos.getString("Origem");
        ReloadPos = dadosRecebidos.getString("ReloadPos");



        //verificando qual é a ação que essa pagina ira fazer

        if(acaoPage.equals("Editar")){


            //setando os valores nos componentes


            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String dataFinalString = tarefa.getData_final();
            //essa variavel do dia final é serve para colocar dentro do pop up do claendario a data final da tarefas(que vei o bd),
            //so fiz ela aqui para aproveitar a conversao de Timestamp para string
            diafinal = utilities.cortarDataDia(dataFinalString);

            texDataTarefa.setText(""+dataFinalString);

            textTarefaCadasEdit2.setText("Editando a Tarefa: "+tarefa.getNome());



        }else{

            textTarefaCadasEdit2.setText("Cadastrando uma nova Tarefa");
        }


        //verificando se o botao voltar foi selecionado
     if(voltarAcao != null){

            tarefa = (Tarefas) dadosRecebidos.getSerializable("object");



         SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
         String dataFinalString = dateFormat.format(tarefa.getData_final());

         texDataTarefa.setText(""+dataFinalString);

        }


        //evento de click do botao proximo
         proximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pegando data
                String dataFinal = texDataTarefa.getText().toString();


                if(!dataFinal.equals("")){



                try {



                   if(!tarefa.setData_final(dataFinal)){


                    }else {

                       Intent Proximo = new Intent(getApplicationContext(), Tarefa_cadas_Edit_p3.class);

                       Proximo.putExtra("object", tarefa );
                       Proximo.putExtra("Acao", acaoPage);
                       Proximo.putExtra("Origem",Origem);
                       Proximo.putExtra("ReloadPos",ReloadPos);
                       finish();
                       startActivity(Proximo);

                   }
                }catch (Exception e){

                    Toast.makeText(getApplicationContext(),"erro na conversao de datas "+e,Toast.LENGTH_LONG).show();
                }








            }else{
                    Toast.makeText(getApplicationContext(),"Deve ser escolhida uma data para a conclusao da Tarefa",Toast.LENGTH_LONG).show();
                }//fechamento do if que verifica se existe uma  data selecionada
            }

        });




        //evento de click do botao voltar
        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent IntentVoltar = new Intent(getApplicationContext(),Tarefa_cadas_Edit_p1.class);
                IntentVoltar.putExtra("voltar","voltar");
                IntentVoltar.putExtra("object",tarefa);
                IntentVoltar.putExtra("AcaoPrimar",acaoPage);
                IntentVoltar.putExtra("Acao",acaoPage);
                IntentVoltar.putExtra("Origem",Origem);
                IntentVoltar.putExtra("ReloadPos",ReloadPos);
                finish();
                startActivity(IntentVoltar);

            }
        });

        //evento de click da imagen
        imgCalendarioTarefa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                calendar =  Calendar.getInstance();
                ano =  calendar.get(Calendar.YEAR);
                mes =  calendar.get(Calendar.MONTH);
                dia =  calendar.get(Calendar.DAY_OF_MONTH);


                if((acaoPage.equals("Editar")) || (acaoPage.equals("voltar"))){


                    dia = diafinal;







                }


                DatePickerDialog datePickerDialog = new DatePickerDialog(Tarefa_cadas_Edit_p2.this,R.style.dialog_cadatro_tarefa,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int day) {
                            month = month + 1;
                            if(month <10){
                                texDataTarefa.setText(day+"/0"+month+"/"+year);
                            }else{
                                texDataTarefa.setText(day+"/"+month+"/"+year);
                            }

                        }
                    }, ano, mes ,dia);


                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());


                datePickerDialog.show();



            }//fechamento do onclick da imagem
        });//fechamento do metodo de click

    }






    }
