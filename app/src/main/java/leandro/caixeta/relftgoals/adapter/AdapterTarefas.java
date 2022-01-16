package leandro.caixeta.relftgoals.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;

import leandro.caixeta.relftgoals.R;
import leandro.caixeta.relftgoals.classes.Tarefas;


public class AdapterTarefas extends RecyclerView.Adapter<AdapterTarefas.myViewHold> {

    private List<Tarefas> listaTarefas;


    public AdapterTarefas(List<Tarefas> TarefasList) {
        this.listaTarefas = TarefasList ;
    }

    @NonNull
    @Override
    public myViewHold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itenLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_tarefas,parent,false);

        return new myViewHold(itenLista);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull myViewHold holder, int position) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        Tarefas tarefas = listaTarefas.get(position);

        holder.nomeMeta.setText(tarefas.getNome());



        holder.inicioMeta.setText(""+tarefas.getData_inicial() );

        //convertendo a data que veio em formato de Timestamp para string


        holder.finMeta.setText(""+tarefas.getData_final());
        holder.prioridade.setBackgroundResource(tarefas.getCodCorPrioridade());

        //holder.faltoNdias.setText(""+tarefas.getDiasRestantes());
        if(tarefas.getDiasRestantes() == 0){
            holder.faltoNdias.setText("O Vencimento é hoje ");
        }else if(tarefas.getDiasRestantes()== -1) {
           holder.faltoNdias.setText("Tarefa Vencida");
        }else{
            holder.faltoNdias.setText("Vence em  "+tarefas.getDiasRestantes()+" dias ");
        }

       //holder.prioridade.setImageResource(R.drawable.meta_inpornt_azul);
    }

    @Override
    public int getItemCount() {

        return this.listaTarefas.size();
    }

    public class myViewHold extends RecyclerView.ViewHolder{


        TextView nomeMeta;
        TextView inicioMeta;
        TextView finMeta;
        TextView prioridade;
        TextView faltoNdias ;
        public myViewHold(@NonNull View itemView) {
            super(itemView);

          nomeMeta = itemView.findViewById(R.id.textNomeTarefa);
          inicioMeta = itemView.findViewById(R.id.textDataInicial);
          finMeta = itemView.findViewById(R.id.textDataFinal);
          prioridade = itemView.findViewById(R.id.textPrioridade);
          faltoNdias = itemView.findViewById(R.id.textMesagem);


        }


    }

}
