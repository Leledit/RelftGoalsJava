package leandro.caixeta.relftgoals.ui.relatorios;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import leandro.caixeta.relftgoals.R;
import leandro.caixeta.relftgoals.classes.Relatorios;
import leandro.caixeta.relftgoals.classes.Tarefas;
import leandro.caixeta.relftgoals.classes.Utilities;
import leandro.caixeta.relftgoals.classes.relatoriosControler;
import leandro.caixeta.relftgoals.classes.tarefasControler;


public class relatorios extends Fragment {

    private TextView textViewReltMes;
    private TextView textviQtdCadastro,textviQtdAtivas,textviQtdfinalizadas,qtdexpiradas,textviQtdEcluidas;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_relatorios, container, false);

        //ligando os componentes visuais as suas respectivas variavies
        textViewReltMes = root.findViewById(R.id.textViewReltMes);
        textviQtdCadastro = root.findViewById(R.id.textviQtdCadastro);
        textviQtdAtivas = root.findViewById(R.id.textviQtdAtivas);
        textviQtdfinalizadas = root.findViewById(R.id.textviQtdfinalizadas);
        qtdexpiradas = root.findViewById(R.id.qtdexpiradas);
        textviQtdEcluidas = root.findViewById(R.id.textviQtdEcluidas);






        //criando uma intancia da classe Utilies
        Utilities utilities = new Utilities();

        String mes  = utilities.RetornarMesAtual(0);

        textViewReltMes.setText(mes );


       //chamando função para mostrar os dados
        buscasInfoRelatorios();


        return root;
    }//fechamento do metodo onCreateView

    @RequiresApi(api = Build.VERSION_CODES.P)
    public void buscasInfoRelatorios(){

        relatoriosControler relateControler = new relatoriosControler(getContext());
        Relatorios relatorios = new Relatorios();
        relatorios = relateControler.BuscarInfoRelatorio();

        Tarefas tarefas = new Tarefas();
        List<Tarefas> listTarefa = new ArrayList<>();

        listTarefa = tarefas.BuscarTarefas(365,getContext(),"ativa");



       textviQtdCadastro.setText(""+relatorios.getQtd_cadastro());
       textviQtdEcluidas.setText(""+relatorios.getQtd_deletadas());
       textviQtdfinalizadas.setText(""+relatorios.getQtd_finalizada());
       qtdexpiradas.setText(""+relatorios.getQtd_expirada());
       textviQtdAtivas.setText(""+listTarefa.size());



    }
}
