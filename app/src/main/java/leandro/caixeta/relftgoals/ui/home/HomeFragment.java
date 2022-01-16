package leandro.caixeta.relftgoals.ui.home;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import leandro.caixeta.relftgoals.R;
import leandro.caixeta.relftgoals.activity.Apoio_redirect;
import leandro.caixeta.relftgoals.activity.Tarefa_cadas_Edit_p1;
import leandro.caixeta.relftgoals.activity.Tarefa_info;
import leandro.caixeta.relftgoals.activity.home;
import leandro.caixeta.relftgoals.adapter.AdapterTarefas;
import leandro.caixeta.relftgoals.classes.Tarefas;
import leandro.caixeta.relftgoals.classes.RecyclerItemClickListener;
import leandro.caixeta.relftgoals.classes.Usuario;
import leandro.caixeta.relftgoals.classes.Utilities;
import leandro.caixeta.relftgoals.classes.relatoriosControler;
import leandro.caixeta.relftgoals.classes.tarefasControler;


public class HomeFragment extends Fragment {


    private RecyclerView recycler;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TextView testeMensagemInicial ;
    private FloatingActionButton fabCadastraDia ;
    private List<Tarefas> tarefasList = new ArrayList<>();


    @RequiresApi(api = Build.VERSION_CODES.P)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_home, container, false);

        recycler = root.findViewById(R.id.recyclerExibTarefas);

        testeMensagemInicial = root.findViewById(R.id.testeMensagemInicial);
        fabCadastraDia = root.findViewById(R.id.fabCadastraDia);

        //chamando metodo resposansavel por listar as Tarefa
         // this.buscarTarefaFirebase();

          //clicando no botao fab sera aberto a pagina de cadastro de Tarefas(cadastrando Tarefa para o dia de hoje)
        fabCadastraDia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cadastroDia = new Intent(getContext(), Tarefa_cadas_Edit_p1.class);
                cadastroDia.putExtra("AcaoPrimar","cadastro") ;
                cadastroDia.putExtra("Origem","FragHome");
                startActivity(cadastroDia);

            }
        });




        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onResume() {
        super.onResume();
        buscarTarefasBd();

    }

    public void verificarMesAtual(){

        Utilities utilities = new Utilities();
        String mesAnterior = utilities.RetornarMesAtual(1);

        //aqui é criada a instancia da classe relatoriosControler
        relatoriosControler  relatControl = new relatoriosControler(getContext());
        //aqui é chamadao o metodo resposanvel por verificar se exite uma relatorio para o mes atual, se esse nao existir ele é criado
        relatControl.verifarExistMes();

        //chamando função responsavel por verificar se exite um relatorio do mes anterior(se esse tiver com status ativo , essa vai ter que ser desativada)








    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public void buscarTarefasBd(){

       final tarefasControler controler = new tarefasControler(getContext());


        Tarefas tarefas = new Tarefas();
        final List<Tarefas> tarefasList = tarefas.BuscarTarefas(0,getContext(),"ativa");

        if (tarefasList.isEmpty()){

            testeMensagemInicial.setText("Não há tarefas cadastradas, deseja cadastrar alguma?");
            recycler.setVisibility(View.GONE);
        }else{
            recycler.setVisibility(View.VISIBLE);
                //configurando o adapter
                AdapterTarefas adapterTarefas = new AdapterTarefas(tarefasList);

                //configurar o RecycleyView
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                recycler.setLayoutManager(layoutManager);
                recycler.setHasFixedSize(true);
                recycler.setAdapter(adapterTarefas);

                //adicionando evento de click
                recycler.addOnItemTouchListener(
                        new RecyclerItemClickListener(
                                getContext(), recycler, new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Tarefas tarefas = tarefasList.get(position);

                                //  Toast.makeText(getContext(),"test: "+tarefas.getNome(), Toast.LENGTH_LONG).show();
                                Intent intentTarefas = new Intent(getContext(), Tarefa_info.class);
                                intentTarefas.putExtra("object_tarefa",tarefas);
                                intentTarefas.putExtra("Acao","homeList");
                                intentTarefas.putExtra("Origem","FragHome");
                                //intentTarefas.putExtra("acaoIN",);
                                startActivity(intentTarefas);




                            }//fechamento do metodo onItemclick

                            @Override
                            public void onLongItemClick(View view, final int position) {

                                //criando a intancia da classe dialog
                                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext(),R.style.dialog_finalizar_Tarefa);
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

                                        Toast.makeText(getContext(),"Tarefa deletada com sucesso",Toast.LENGTH_LONG).show();
                                        controler.ExcluirTarefas(tarefasList.get(position).getuId());

                                        /**Adiconando mais um a qtd_Expiradas de tarefas desse mes(Relatorios) **/
                                        //aqui é feito uma verificação que ve se exitem algum relatorio para o mes corrente(ativo) se nao existir é cadastrado um novo
                                        relatoriosControler relatControl = new relatoriosControler(getContext());


                                        //aqui é chamado uma função que busca o relatorio ativo e muda alguns dos seus dados
                                        relatControl.alterarContadorRelat("Excluida",getContext());

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

        startActivity(new Intent(getContext(), Apoio_redirect.class));




    }




}
