package leandro.caixeta.relftgoals.classes;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.Date;

public class sqlitBanco extends SQLiteOpenHelper {
    private static final String NOMEBANCO = "Relft_Goals.bd";
    private static final String TABELA_TAREFAS = "Tarefas";
    private static final String TABELA_USUARIOS = "Usuarios";
    private static final String TABELA_RELATORIOS = "Relatorios";
    private static final int Taf_versao = 26 ;


    public sqlitBanco(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, Integer version) {
        super(context, NOMEBANCO, null, Taf_versao);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {

        //criando a atbela  de tarefas
        String sql = "CREATE TABLE " + TABELA_TAREFAS +"("
                +  "id_tarefa integer primary key autoincrement ,"
                + "uid_cliente text,"
                + "categoria text,"
                + "data_inicio text,"
                + "data_final text,"
                + "descricao text,"
                + "nome text,"
                + "prioridade text,"
                + "status text" + ")";
        //Executando o comando de criação da tabela
        db.execSQL(sql);

        //criando a tabela de usuarios
        String sqlUsuarios = " CREATE TABLE  "+ TABELA_USUARIOS +"("
                + "id_usuario integer primary key autoincrement,"
                + "nome text,"
                + "data_nacimento text,"
                + "email text,"
                + "tipo text,"
                + "uid_usuario text "+")";
        db.execSQL(sqlUsuarios);


        String sqlRelatorios = " CREATE TABLE "+TABELA_RELATORIOS +"("
                + "id_relatorio integer primary key autoincrement,"
                + "qtd_cadastro integer,"
                + "qtd_expirada integer,"
                + "qtd_finalizada integer,"
                + "qtd_deletadas integer,"
                + "mes text,"
                + "ano integer,"
                + "status text"+ ")";
        db.execSQL(sqlRelatorios);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //aqui vai ser apagado o banco e em seguida ele sera recriado de novo
        // (para aplicar possiveis mudanças na sua estrutura)
        db.execSQL("DROP TABLE IF EXISTS "+TABELA_TAREFAS);
        db.execSQL("DROP TABLE IF EXISTS "+TABELA_USUARIOS);
        db.execSQL("DROP TABLE IF EXISTS "+TABELA_RELATORIOS);
        onCreate(db);
    }
}
