package leandro.caixeta.relftgoals.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.joda.time.DateTime;
import org.joda.time.Days;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import leandro.caixeta.relftgoals.R;
import leandro.caixeta.relftgoals.adapter.AdapterTarefas;
import leandro.caixeta.relftgoals.classes.Tarefas;
import leandro.caixeta.relftgoals.classes.RecyclerItemClickListener;
import leandro.caixeta.relftgoals.classes.Usuario;
import leandro.caixeta.relftgoals.classes.Utilities;
import leandro.caixeta.relftgoals.classes.relatoriosControler;
import leandro.caixeta.relftgoals.classes.tarefasControler;

public class Visao_geral_list extends AppCompatActivity {


    private TextView texVisaoGeralTitulo ,textTarefasInvalid;
    private String acaoPage ,acaoINfinalise = "";
    private FloatingActionButton fabCadastraTarefasVision;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private RecyclerView recyclerView;
  private EditText edtTesteloucos;
    @SuppressLint("RestrictedApi")
    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visao_geral_list);

        //ligando os compoennetes aos seus rescpetivos
        texVisaoGeralTitulo = findViewById(R.id.texVisaoGeralTitulo);
        textTarefasInvalid = findViewById(R.id.textTarefasInvalid);
        fabCadastraTarefasVision = findViewById(R.id.fabCadastraTarefasVision);
        recyclerView = findViewById(R.id.recyclerViewVisaoGeral);
       edtTesteloucos = findViewById(R.id.edtTesteloucos);



        //buscando dados da pagina anterior
        Bundle dadosRecebidos = getIntent().getExtras();
        acaoPage = dadosRecebidos.getString("Acao");
        acaoINfinalise = dadosRecebidos.getString("acaoIN");





        //verificando quais dados a pagina ira exibir(dia,semana,mes,ano)
        switch (acaoPage){

            case "buscarDia":

                texVisaoGeralTitulo.setText("Tarefas ativas para hoje");

               buscarTarefasBd("buscarDia");
                break;
            case "buscarSemana":

                buscarTarefasBd("buscarSemana");
                texVisaoGeralTitulo.setText("Tarefas ativas para essa semana");
                break;
            case "buscarMes":

                buscarTarefasBd("buscarMes");
                texVisaoGeralTitulo.setText("Tarefas ativas para o mes atual");
                break;
            case "buscarAno":

                buscarTarefasBd("buscarAno");
                texVisaoGeralTitulo.setText("Tarefas ativas para esse ano");
                break;
            case "buscarExpiradas":

                buscarTarefasBd("buscarExpiradas");
                texVisaoGeralTitulo.setText("Tarefas Expiradas");
                fabCadastraTarefasVision.setVisibility(View.GONE);
                break;
            case"buscarFinalizadas":
                buscarTarefasBd("buscarFinalizadas");
                texVisaoGeralTitulo.setText("Tarefas finalizadas");
                fabCadastraTarefasVision.setVisibility(View.GONE);
                break;

        }//fechamento do switch

        //criando evento responsavel por chamar tela de cadastro
        cadastrarTarefa();

        //chamandando o metodo resposnavel por realizar a consulta das tarefas no bd
     //   BuscarDadosbd();

    }//fechamento do Oncreat

    public void cadastrarTarefa(){

        fabCadastraTarefasVision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent VisaoGeral = new Intent(getApplicationContext(), Tarefa_cadas_Edit_p1.class);
                VisaoGeral.putExtra("Origem","VisaoList");
                switch (acaoPage) {

                    case "buscarDia":

                        VisaoGeral.putExtra("AcaoPrimar", "cadastro");
                        break;
                    default:
                        VisaoGeral.putExtra("AcaoPrimar", "cadastro_data");
                        break;


                }
                VisaoGeral.putExtra("ReloadPos",acaoPage);
                finish();
                startActivity(VisaoGeral);
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.P)
    public void buscarTarefasBd(String buscaAcao){

        int dias = 0 ;
        String  tipoBusca = "" ;

        if(buscaAcao.equals("buscarDia")){
            dias = 0;
            tipoBusca = "ativa";
        }else if(buscaAcao.equals("buscarSemana")){
            dias = 7;
            tipoBusca = "ativa";
        }else if(buscaAcao.equals("buscarMes")){
            dias = 30;
            tipoBusca = "ativa";
        }else if(buscaAcao.equals("buscarAno")){
            dias = 365;
            tipoBusca = "ativa";

        }else if(buscaAcao.equals("buscarExpiradas")){
            tipoBusca = "Expirada";

            dias = 0;
        }else if(buscaAcao.equals("buscarFinalizadas")){
            tipoBusca = "Finalizada";
        }



        final tarefasControler controler = new tarefasControler(getApplicationContext());

        Tarefas tarefas = new Tarefas();



        final List<Tarefas> tarefasList = tarefas.BuscarTarefas(dias,getApplicationContext(),tipoBusca);

        if (tarefasList.isEmpty()){


            textTarefasInvalid.setText("Opss.. Parece que nao Existem Tarefas cadastradas , que tal cadastrar algumas ?");
        }else{

            textTarefasInvalid.setVisibility(View.GONE);
            //configurando o adapter
            AdapterTarefas adapterTarefas = new AdapterTarefas(tarefasList);

            //configurar o RecycleyView
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(adapterTarefas);

            //adicionando evento de click
            recyclerView.addOnItemTouchListener(
                    new RecyclerItemClickListener(
                            getApplicationContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            Tarefas tarefas = tarefasList.get(position);

                            //  Toast.makeText(getContext(),"test: "+tarefas.getNome(), Toast.LENGTH_LONG).show();
                            Intent intentTarefas = new Intent(getApplicationContext(), Tarefa_info.class);
                            intentTarefas.putExtra("object_tarefa",tarefas);
                            intentTarefas.putExtra("Origem","visao_geral");

                            if(acaoPage.equals("buscarExpiradas")){
                                intentTarefas.putExtra("Acao","Visao_geral_Espirada");
                            }else if(acaoPage.equals("buscarFinalizadas")){
                                intentTarefas.putExtra("Acao","Visao_geral_Finalizadas");
                            }else{
                                intentTarefas.putExtra("Acao","Visao_geral");
                            }

                            intentTarefas.putExtra("acaoIN",acaoPage);
                            finish();
                            startActivity(intentTarefas);




                        }//fechamento do metodo onItemclick

                        @Override
                        public void onLongItemClick(View view, final int position) {


                            //criando a intancia da classe dialog
                            AlertDialog.Builder dialog = new AlertDialog.Builder(Visao_geral_list.this,R.style.dialog_finalizar_Tarefa);
                            //configurando o titulo e a mensgaem que vao aparecer na caixa
                            dialog.setTitle("Excluir tarefa");
                            dialog.setMessage("Deseja realmente excluir esta tarefa ?");

                            //o comando abaixo nao permite
                            dialog.setCancelable(false);

                            //definindo um desgner personalizado

                            //LayoutInflater é utilizado para inflar nosso layout em uma view.
                            //-pegamos nossa instancia da classe
                            LayoutInflater li =  getLayoutInflater();

                            dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    Toast.makeText(getApplicationContext(),"Tarefa deletada com sucesso",Toast.LENGTH_LONG).show();
                                    controler.ExcluirTarefas(tarefasList.get(position).getuId());

                                    /**Adiconando mais um a qtd_Expiradas de tarefas desse mes(Relatorios) **/
                                    //aqui é feito uma verificação que ve se exitem algum relatorio para o mes corrente(ativo) se nao existir é cadastrado um novo
                                    relatoriosControler relatControl = new relatoriosControler(getApplicationContext());


                                    //aqui é chamado uma função que busca o relatorio ativo e muda alguns dos seus dados
                                    relatControl.alterarContadorRelat("Excluida",getApplicationContext());


                                    reloadPag();

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
                        }//fechamento do metodo onLongClick





                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        }
                    }));//fechamento do evento de touchListener


        }//fechamento do else do if que verifica se existem tarefas cadastradas












    }//fechamento do evento de buscar as tarefas que estao no banco de dados


    public void reloadPag(){

        finish();
        Intent reload = new Intent(getApplicationContext(),Visao_geral_list.class);
        reload.putExtra("Acao",acaoPage);
        startActivity(reload);




    }



}
