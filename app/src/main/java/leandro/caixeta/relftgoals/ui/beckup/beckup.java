package leandro.caixeta.relftgoals.ui.beckup;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.provider.DocumentsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import leandro.caixeta.relftgoals.R;
import leandro.caixeta.relftgoals.classes.Tarefas;
import leandro.caixeta.relftgoals.classes.Usuario;
import leandro.caixeta.relftgoals.classes.Utilities;
import leandro.caixeta.relftgoals.classes.tarefasControler;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link beckup#newInstance} factory method to
 * create an instance of this fragment.
 */
public class beckup extends Fragment {

    private ImageView imgbenckup_beckup , imgbenckup_download ;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ProgressBar progressBarbeckup ;
    private ImageView imgInterogacaoBeckup ;

    // TODO: Rename and change types and number of parameters
    public static beckup newInstance(String param1, String param2) {
        beckup fragment = new beckup();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_beckup, container, false);

        //ligando oc componentes visuais as suas respctivas variavies

        imgbenckup_beckup = root.findViewById(R.id.imgbenckup_beckup);
        imgbenckup_download = root.findViewById(R.id.imgbenckup_download);
        progressBarbeckup = root.findViewById(R.id.progressBarbeckup);
        imgInterogacaoBeckup = root.findViewById(R.id.imgInterogacaoBeckup);


        //criando os evento de click da imagem
        imgbenckup_beckup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verificandoBdOn();
                progressBarbeckup.setVisibility(View.VISIBLE);
            }
        });

        imgbenckup_download.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onClick(View v) {

                VerificandoBdOFF();
                progressBarbeckup.setVisibility(View.VISIBLE);
            }
        });
        imgInterogacaoBeckup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chamandoPopUp();
            }
        });


        return root;
    }

    public void chamandoPopUp(){
        android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(getContext());
        dialog.setView(R.layout.popupbeckup);
        dialog.create();
        dialog.show();
    }
    @RequiresApi(api = Build.VERSION_CODES.P)
    public void VerificandoBdOFF(){
        //o primeiro passo é verificar se existem tarefas cadastardas no bd local(off-line)

        tarefasControler controlerTarel = new tarefasControler(getContext());
        List<Tarefas> tarefasList = controlerTarel.BuscasTarefas(getContext(),"nd",FirebaseAuth.getInstance().getCurrentUser().getUid());

        if(tarefasList.size() == 0){
            //nao existem tarefas cadastaradas no bd offile,pode cadastrar as online de boas

            buscandoBdOnline();

        }else{
            //exitem tarefas cadastrar localmente, é necessario perguntar se quer subustituir

            pedidoPermisao("Local");
        }



    }
    public void verificandoBdOn(){


        Usuario usuario = new Usuario();
        final String uid = usuario.retornarUidUsuarioLogado();

        //intanciando a classe de ultilidades
        Utilities utilities = new Utilities();


        final CollectionReference TarefasBD = db.collection("Usuarios").document(uid).collection("Tarefas");

        Query querTarefas = TarefasBD.whereEqualTo("Status", "Ativa").limit(1);
        querTarefas.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful()){

                    //verificando
                    if(task.getResult().isEmpty()){
                        //buscando as tarefas que estao no banco off-line
                       List<Tarefas> tarefasList =  busncadobdLocalTarefas();
                       //Verificando se exitem tarefas para serem enviadas ao bd
                        if(tarefasList.size() == 0){
                            Toast.makeText(getContext(),"É necessario ter tarefas cadastradas para que possa haver o beckup",Toast.LENGTH_LONG).show();

                            progressBarbeckup.setVisibility(View.GONE);
                        }else{
                            //caso tenha, essas deveram ser enviadas ao bd online
                            enviandoBdOnline(tarefasList);
                        }
                    }else{

                        // a função abaixo pede permisao para o usuario ,afim de permitir que os dados sejam substituido s no bd
                        //com a permissao dada , as tarefas antigas sao apagadas e as novas sao cadastradas no lugar
                        pedidoPermisao("online");

                    }



                }else{
                    Toast.makeText(getContext(),"ERRO",Toast.LENGTH_LONG).show();
                }//fim do else que verifica se deu certo a busca
            }
        });//fechamento do metodo oncomplete da query 'querTarefas'


    }//fechamento do metodo verificandoBDOn

    @RequiresApi(api = Build.VERSION_CODES.P)
    public void pedidoPermisao(final String destinoPergunta){

        final List<Tarefas> tarefasList =  busncadobdLocalTarefas();

        progressBarbeckup.setVisibility(View.GONE);

        //criando a intancia da classe dialog
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext(),R.style.dialog_finalizar_Tarefa);
        //configurando o titulo e a mensgaem que vao aparecer na caixa
        dialog.setTitle("Substituir Tarefas");
        //criando uma variavel para armazenar a mensagem do pop up
         String mensagem = " ";
        if(destinoPergunta.equals("online")){
            mensagem = "Ja foi feito um beckup anterior, gostaria de subustituir o antigo por um novo? ";
        }else{
            mensagem = "Parece que ja existem tarefas cadastradas, gostaria de subustituir por as que esta no beckup?" ;
        }
        dialog.setMessage(mensagem);

        //o comando abaixo nao permite
        dialog.setCancelable(false);

        //definindo um desgner personalizado

        //LayoutInflater é utilizado para inflar nosso layout em uma view.
        //-pegamos nossa instancia da classe
        LayoutInflater li =  getLayoutInflater();

        dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(destinoPergunta.equals("online")) {
                    //se o usuario deixou fazer a susbustituição dos dados antigos pelo novos,deve ser chamado  um metodo resposavel por limpar o bd online
                    //se a limpeza foi um sucesso , devera ser feito o cadastro dentro do proprio metodo de limpar
                    limpandoBdOnline(tarefasList);
                    progressBarbeckup.setVisibility(View.VISIBLE);
                }else{

                    progressBarbeckup.setVisibility(View.VISIBLE);
                    limpandoBdOff(tarefasList);
                    buscandoBdOnline();


                }//fechamento do else que verifica o tipo da mensagem



            }//fechamento do evento de clicl da opção "sim" do pop up
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

    @RequiresApi(api = Build.VERSION_CODES.P)
    public void buscandoBdOnline(){

        final CollectionReference TarefasBD = db.collection("Usuarios").document(FirebaseAuth.getInstance().getUid()).collection("Tarefas");

        final tarefasControler  controler = new tarefasControler(getContext());


        TarefasBD.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {


                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {

                        Tarefas tarefas = new Tarefas();
                        tarefas.setNome(document.getData().get("Nome").toString());
                        tarefas.setDescricao(document.getString("Descricao").toString());
                        tarefas.setData_inicial(document.getString("Data_inicio").toString());
                        tarefas.setData_final(document.getString("Data_termino").toString());
                        tarefas.setStatus(document.getData().get("Status").toString());
                        tarefas.setPrioridade(document.getData().get("Prioridade").toString());
                        tarefas.setCategoria( document.getData().get("Categoria").toString());
                        tarefas.setUid_usuario(document.getString("uid_cliente").toString());

                        //depois de buscar as informaçoes da tarefas, essa ja é cadastrada no bd Local
                         controler.insereTarefa(tarefas);

                    }//fehcamento do for
                    Toast.makeText(getContext(),"Download realizado com sucesso !! suas tarefas ja estao disponiveis nesse dispositivo.",Toast.LENGTH_LONG).show();
                    progressBarbeckup.setVisibility(View.GONE);
                }//fechamento do isSuccessful
            }});


        }//fechamento do metodo "buscandoBdOnline"


    public void limpandoBdOff(List<Tarefas> tarefasList){



        tarefasControler controleTaf = new tarefasControler(getContext());

        for(int i=0 ; i<tarefasList.size(); i++){

            controleTaf.ExcluirTarefas(tarefasList.get(i).getuId());
        }//fechamento do for de excluir tarefas



    }

    public void limpandoBdOnline(final List<Tarefas> tarefasList){

        final CollectionReference TarefasBD = db.collection("Usuarios").document(FirebaseAuth.getInstance().getUid()).collection("Tarefas");


        TarefasBD.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot  document: task.getResult()){


                        db.collection("Usuarios").document(FirebaseAuth.getInstance().getUid()).
                                collection("Tarefas").document(document.getId()).delete();


                    }//fechamento do for de busca de tarefas online

                    enviandoBdOnline(tarefasList);

                }//fechamento da verificação do isSuccessful



            }//fechamento do metodo oncomplete
        });



    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public List<Tarefas> busncadobdLocalTarefas(){

        //criando liste para armazenar as tarefas
        List<Tarefas> tarefasList = new ArrayList<>();

        //criando a intancia da classe  tarefasControler
        tarefasControler controler = new tarefasControler(getContext());

        //buscando todas as tarefas que estao no bd
        tarefasList = controler.BuscasTarefas(getContext(),"tudo", FirebaseAuth.getInstance().getCurrentUser().getUid());

        return  tarefasList ;
    }

    public void enviandoBdOnline(List<Tarefas>tarefasList){


        for(int i =0 ; i<tarefasList.size();i++){

           //criando uma matriz de dados para ser enviado ao BD
            Map<String,Object> user = new HashMap<>();

            user.put("Nome",tarefasList.get(i).getNome());
            user.put("Descricao",tarefasList.get(i).getDescricao());
            user.put("Data_inicio",tarefasList.get(i).getData_inicial());
            user.put("Data_termino",tarefasList.get(i).getData_final());
            user.put("Categoria",tarefasList.get(i).getCategoria());
            user.put("Status",tarefasList.get(i).getStatus());
            user.put("Prioridade",tarefasList.get(i).getPrioridade());
            user.put("uid_cliente",tarefasList.get(i).getUid_usuario());


            db.collection("Usuarios").document(FirebaseAuth.getInstance().getUid()).
                    collection("Tarefas").add(user);

        }
        progressBarbeckup.setVisibility(View.GONE);
        Toast.makeText(getContext(),"Beckup realizado com sucesso !! suas tarefas ja estao salvas no nosso servidor online",Toast.LENGTH_LONG).show();



    }//fechamento do metodo "enviandoBdOnline
}
