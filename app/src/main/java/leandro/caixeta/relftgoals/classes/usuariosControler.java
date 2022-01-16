package leandro.caixeta.relftgoals.classes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

public class usuariosControler {

    private SQLiteDatabase db ;
    private sqlitBanco banco ;

    @RequiresApi(api = Build.VERSION_CODES.P)
    public usuariosControler(Context context) {
        banco = new sqlitBanco(context,null,null,null);
    }


    public Usuario verificarqtdRegistros(){

        String[] colunas = {"id_usuario","nome","data_nacimento","email","tipo ","uid_usuario"};


        db = banco.getReadableDatabase();
        Cursor cursor = db.query("Usuarios",colunas,null,null,null,null,null,null);


        Usuario usuario = new Usuario();

        if(cursor != null) {
            while (cursor.moveToNext()) {

                usuario.setId(cursor.getInt(0));
                usuario.setNome(cursor.getString(1));
                usuario.setData(cursor.getString(2));
                usuario.setEmail(cursor.getString(3));
                usuario.setTipo(cursor.getString(4));
                usuario.setUid(cursor.getString(5));


            }//Fechamento do while
        }//fechamento do if

        db.close();
        return  usuario;

    }

    public void alterandoDados(Usuario usuario){

        ContentValues contentValues ;
        String where ;

        db = banco.getWritableDatabase();

        where = "uid_usuario"+"= '"+usuario.getUid()+"'";
        contentValues = new ContentValues();
        contentValues.put("nome",usuario.getNome());
        contentValues.put("data_nacimento",usuario.getData());
        contentValues.put("email",usuario.getEmail());
        contentValues.put("tipo",usuario.getTipo());
        contentValues.put("uid_usuario",usuario.getUid());

        db.update("Usuarios",contentValues,where,null);
        db.close();

    }

    public Usuario buscarUsuario(String email){
        Usuario usuario = new Usuario();


        String[] colunas = {"id_usuario","nome","data_nacimento","email","tipo ","uid_usuario"};
        String where ;
            where = "email = '"+ email + "'";
        db = banco.getReadableDatabase();
        Cursor cursor = db.query("Usuarios",colunas,where,null,null,null,null,null);


        if(cursor != null) {
            while (cursor.moveToNext()) {

                usuario.setId(cursor.getInt(0));
                usuario.setNome(cursor.getString(1));
                usuario.setData(cursor.getString(2));
                usuario.setEmail(cursor.getString(3));
                usuario.setTipo(cursor.getString(4));
                usuario.setUid(cursor.getString(5));


            }//Fechamento do while
        }//fechamento do if

        db.close();
        return  usuario;
    }

    public int insereUsuario(Usuario usuario){

        Integer statusOperacao = 0 ;
        ContentValues valores ;
        Long resultado ;



        db = banco.getWritableDatabase(); //essa linha é resposanvel por iniciar a conexao
        valores = new ContentValues();
        valores.put("nome",usuario.getNome());
        valores.put("data_nacimento",usuario.getData());
        valores.put("email",usuario.getEmail());
        valores.put("tipo",usuario.getTipo());
        valores.put("uid_usuario",usuario.getUid());


        resultado = db.insert("Usuarios",null,valores);
        db.close(); //fechando a conexao com o banco de dados

        if(resultado != -1){
            statusOperacao = 1;
        }
        //aqui é retornado se deu certo a operação de inserção no banco de dados
        //se for retornado 1 , quer dizer que deu tudo certo, se nao ele retornara 0 dizendo que tem erro
        return  statusOperacao;
    }

}
