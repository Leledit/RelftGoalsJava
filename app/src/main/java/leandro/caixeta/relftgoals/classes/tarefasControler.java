package leandro.caixeta.relftgoals.classes;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;

public class tarefasControler {
    private SQLiteDatabase db ;
    private sqlitBanco banco ;

    @RequiresApi(api = Build.VERSION_CODES.P)
    public tarefasControler(Context context) {
        banco = new sqlitBanco(context,null,null,null);
    }


    public void ExcluirTarefas(int id){

        String where =  "id_tarefa"+"="+id;
        db = banco.getReadableDatabase();
        db.delete("Tarefas",where,null);
        db.close();


        /*db.execSQL("Delete from Tarefas  where id_tarefa = "+id);
        db.close();
*/

    }

    public int EditarTarefa(Tarefas tarefas){

        ContentValues contentValues ;
        String where ;
        int resultado = 0  ;
        db = banco.getWritableDatabase();

        where = "id_tarefa"+"="+tarefas.getuId();
        contentValues = new ContentValues();
        contentValues.put("uid_cliente",tarefas.getUid_usuario());
        contentValues.put("categoria",tarefas.getCategoria());
        contentValues.put("data_inicio",tarefas.getData_inicial());
        contentValues.put("data_final",tarefas.getData_final());
        contentValues.put("descricao",tarefas.getDescricao());
        contentValues.put("nome",tarefas.getNome());
        contentValues.put("prioridade",tarefas.getPrioridade());
        contentValues.put("status", tarefas.getStatus());
        resultado = db.update("Tarefas",contentValues,where,null);
        db.close();

        return resultado ;
    }

    public void  AlterarStatus( Tarefas tarefas,int indicacao){

        /** Nota: a indicação vai servir para mostrar para qual status vai a tarefa ok**/

        ContentValues contentValues ;
        String where ;

        db = banco.getWritableDatabase();

        where = "id_tarefa"+"="+tarefas.getuId();
        contentValues = new ContentValues();
        contentValues.put("uid_cliente",tarefas.getUid_usuario());
        contentValues.put("categoria",tarefas.getCategoria());
        contentValues.put("data_inicio",tarefas.getData_inicial());
        contentValues.put("data_final",tarefas.getData_final());
        contentValues.put("descricao",tarefas.getDescricao());
        contentValues.put("nome",tarefas.getNome());
        contentValues.put("prioridade",tarefas.getPrioridade());

        if(indicacao == 1) {
            contentValues.put("status", "Expirada");
        }else if(indicacao == 2 ){
            contentValues.put("status", "Finalizada");
        }

        db.update("Tarefas",contentValues,where,null);
        db.close();



    }

    public Tarefas buscarTarefa(){
        Tarefas tarefas = new Tarefas();

        String[] colunas = {"id_tarefa","uid_cliente","categoria","data_inicio","data_final "
                ,"descricao","nome","prioridade","status"};
        db = banco.getReadableDatabase();
        Cursor cursor = db.query("Tarefas",colunas,null,null,null,null,null,null);

        if(cursor != null){

            while (cursor.moveToNext()) {


                tarefas.setuId(cursor.getInt(0));
                tarefas.setUid_usuario(cursor.getString(1));
                tarefas.setCategoria(cursor.getString(2));
                tarefas.setData_inicial(cursor.getString(3));
                tarefas.setData_final(cursor.getString(4));
                tarefas.setDescricao(cursor.getString(5));
                tarefas.setNome(cursor.getString(6));
                tarefas.setPrioridade(cursor.getString(7));
                tarefas.setStatus(cursor.getString(8));




            }//fechamenro do while


        }
        db.close();


        return  tarefas;
    }

    public List<Tarefas> BuscasTarefas(Context context , String tipoBusca,String uid_Usuario){

        List<Tarefas> tarefas  = new ArrayList<>();

        String where = " " ;

        if(tipoBusca.equals("ativa")) {
            where = "status = 'Ativa' and uid_cliente = '"+uid_Usuario+"'";
        }else if(tipoBusca.equals("Expirada")){
            where = "status = 'Expirada' and uid_cliente = '"+uid_Usuario+"'";
        }else if(tipoBusca.equals("Finalizada")){
            where = "status = 'Finalizada' and uid_cliente = '"+uid_Usuario+"'";

        }else{
            where = "uid_cliente = '"+uid_Usuario+"'";
        }



        String[] colunas = {"id_tarefa","uid_cliente","categoria","data_inicio","data_final "
                ,"descricao","nome","prioridade","status"};
        db = banco.getReadableDatabase();
        Cursor cursor = db.query("Tarefas",colunas,where,null,null,null,null,null);



        if(cursor != null){

            while (cursor.moveToNext()) {

                Tarefas taf = new Tarefas();
                taf.setuId(cursor.getInt(0));
                taf.setUid_usuario(cursor.getString(1));
                taf.setCategoria(cursor.getString(2));
                taf.setData_inicial(cursor.getString(3));
                taf.setData_final(cursor.getString(4));
                taf.setDescricao(cursor.getString(5));
                taf.setNome(cursor.getString(6));
                taf.setPrioridade(cursor.getString(7));
                taf.setStatus(cursor.getString(8));
                tarefas.add(taf);

               // Toast.makeText(context,"qtd: "+cursor.getInt(0)+tipoBusca,Toast.LENGTH_LONG).show();

            }//fechamenro do while


        }
        db.close();



              return  tarefas;
    }

    public int insereTarefa(Tarefas tarefa){

        Integer statusOperacao = 0 ;
        ContentValues valores ;
        Long resultado ;

        db = banco.getWritableDatabase(); //essa linha é resposanvel por iniciar a conexao
        valores = new ContentValues();
        valores.put("categoria",tarefa.getCategoria());
        valores.put("data_inicio",tarefa.getData_inicial());
        valores.put("data_final",tarefa.getData_final());
        valores.put("descricao",tarefa.getDescricao());
        valores.put("nome",tarefa.getNome());
        valores.put("prioridade",tarefa.getPrioridade());
        valores.put("status",tarefa.getStatus());
        valores.put("uid_cliente",tarefa.getUid_usuario());

        resultado = db.insert("Tarefas",null,valores);
        db.close(); //fechando a conexao com o banco de dados

        if(resultado != -1){
            statusOperacao = 1;
        }
        //aqui é retornado se deu certo a operação de inserção no banco de dados
        //se for retornado 1 , quer dizer que deu tudo certo, se nao ele retornara 0 dizendo que tem erro
        return  statusOperacao;
    }
}
