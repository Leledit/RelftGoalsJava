package leandro.caixeta.relftgoals.ui.visao_geral;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.material.snackbar.Snackbar;

import leandro.caixeta.relftgoals.R;
import leandro.caixeta.relftgoals.activity.Visao_geral_list;


public class visao_geral extends Fragment {

    private ImageView imgInterogacaoVisaoGeral ,imgVisaoDia ,imgVisaoSemana ,imgVisaoMensal ,imgVisaoAnual
            ,imgTarefasExpiradas,imgTarefasFinalizadas;
    public visao_geral() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View root = inflater.inflate(R.layout.fragment_visao_geral, container, false);


        //ligando os compoenentes as suas respectivas variaveis na programção
        imgInterogacaoVisaoGeral = root.findViewById(R.id.imgInterogacaoVisaoGeral);
        imgVisaoDia = root.findViewById(R.id.imgVisaoDia);
        imgVisaoSemana = root.findViewById(R.id.imgVisaoSemana);
        imgVisaoMensal = root.findViewById(R.id.imgVisaoMensal);
        imgVisaoAnual = root.findViewById(R.id.imgVisaoAnual);
        imgTarefasFinalizadas = root.findViewById(R.id.imgTarefasFinalizadas);
        imgTarefasExpiradas = root.findViewById(R.id.imgTarefasExpiradas);

        imgInterogacaoVisaoGeral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setView(R.layout.popup_visao_geral);
                dialog.create();
                dialog.show();

            }//fechamento do evento de click do botao de interegação

        });//fechamento do setonclicklisterner

        imgVisaoDia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent VisaoGeral = new Intent(getContext(),Visao_geral_list.class);
                VisaoGeral.putExtra("Acao","buscarDia");
                startActivity(VisaoGeral);
            }
        });

        imgVisaoSemana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent VisaoGeral = new Intent(getContext(),Visao_geral_list.class);
                VisaoGeral.putExtra("Acao","buscarSemana");
                startActivity(VisaoGeral);
            }
        });

        imgVisaoMensal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent VisaoGeral = new Intent(getContext(),Visao_geral_list.class);
                VisaoGeral.putExtra("Acao","buscarMes");
                startActivity(VisaoGeral);
            }
        });
        imgVisaoAnual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent VisaoGeral = new Intent(getContext(),Visao_geral_list.class);
                VisaoGeral.putExtra("Acao","buscarAno");
                startActivity(VisaoGeral);
            }
        });

        imgTarefasExpiradas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent VisaoGeral = new Intent(getContext(),Visao_geral_list.class);
                VisaoGeral.putExtra("Acao","buscarExpiradas");
                startActivity(VisaoGeral);
            }
        });

        imgTarefasFinalizadas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent VisaoGeral = new Intent(getContext(),Visao_geral_list.class);
                VisaoGeral.putExtra("Acao","buscarFinalizadas");
                startActivity(VisaoGeral);
            }
        });

        return root;
    }
}
