package leandro.caixeta.relftgoals.classes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

public class relatoriosControler {

    private SQLiteDatabase db ;
    private sqlitBanco banco ;
    private String[] colunas = {"id_relatorio","qtd_cadastro","qtd_expirada","qtd_finalizada","qtd_deletadas","mes","ano","status"};

    public relatoriosControler(Context context) {
        banco = new sqlitBanco(context,null,null,null);
    }

    public Relatorios BuscarInfoRelatorio(){


        //criando uma intancia da classe utilities e chamando um metodo que retorna o mes atual em forma de string e o ano em forma de inteiro
        Utilities utilities = new Utilities();
        String mesAtul = utilities.RetornarMesAtual(0);
        int Ano = utilities.recuperarAnoAtual();


        //pesquisando se ja existem algum registro de relatorio para o mes atual

        String where = "mes = '"+ mesAtul+"' and ano = "+Ano+" and status = 'ativa' ";
        db = banco.getReadableDatabase();
        Cursor cursor = db.query("Relatorios",colunas,where,null,null,null,null,null);

        Relatorios relatorios = new Relatorios();

        if(cursor != null) {
            while (cursor.moveToNext()) {
                relatorios.setId_relatorio(cursor.getInt(0));
                relatorios.setQtd_cadastro(cursor.getInt(1));
                relatorios.setQtd_expirada(cursor.getInt(2));
                relatorios.setQtd_finalizada(cursor.getInt(3));
                relatorios.setQtd_deletadas(cursor.getInt(4));
                relatorios.setMes(cursor.getString(5));
                relatorios.setAno(cursor.getInt(6));
                relatorios.setStatus(cursor.getString(7));

            }//fechamento do comando whilhe
        }//fechamento do if de cursor

        return relatorios;

    }//fechamento do metodo "BuscarInfoRelatorio"
    public void alterarContadorRelat(String tipo ,Context context){

        //criando uma intancia da classe utilities e chamando um metodo que retorna o mes atual em forma de string e o ano em forma de inteiro
        Utilities utilities = new Utilities();
        String mesAtul = utilities.RetornarMesAtual(0);
        int Ano = utilities.recuperarAnoAtual();


        //pesquisando se ja existem algum registro de relatorio para o mes atual
        String where = "mes = '"+ mesAtul+"' and ano = "+Ano+" and status = 'ativa' ";
        db = banco.getReadableDatabase();
        Cursor cursor = db.query("Relatorios",colunas,where,null,null,null,null,null);

        if(cursor != null) {
            Relatorios relatorios = new Relatorios();
            while (cursor.moveToNext()) {



                relatorios.setId_relatorio(cursor.getInt(0));
                relatorios.setQtd_cadastro(cursor.getInt(1));
                relatorios.setQtd_expirada(cursor.getInt(2));
                relatorios.setQtd_finalizada(cursor.getInt(3));
                relatorios.setQtd_deletadas(cursor.getInt(4));
                relatorios.setMes(cursor.getString(5));
                relatorios.setAno(cursor.getInt(6));
                relatorios.setStatus(cursor.getString(7));
                if(tipo.equals("Cadastro")){

                    relatorios.setQtd_cadastro(relatorios.getQtd_cadastro()+1);
                }else if(tipo.equals("Expirada")){
                    relatorios.setQtd_expirada(relatorios.getQtd_expirada()+1);
                }else if(tipo.equals("Finalizada")){
                    relatorios.setQtd_finalizada(relatorios.getQtd_finalizada()+1);
                }else if(tipo.equals("Excluida")){
                    relatorios.setQtd_deletadas(relatorios.getQtd_deletadas()+1);
                }

            }//fechamento do while
            alteraRelatorio(relatorios,1);
        }//fechmaento da verificação do cursor










        //chamando funçao resposanvel por alterar os dados do relatorio

    }//fechamento do metodo alterarContadorRelat

    public void alteraRelatorio(Relatorios relatorios,int acao){
        ContentValues contentValues ;
        String where ;

        db = banco.getWritableDatabase();
        where = "id_relatorio"+"= '"+relatorios.getId_relatorio()+"'";
        contentValues = new ContentValues();
        contentValues.put("qtd_cadastro",relatorios.getQtd_cadastro());
        contentValues.put("qtd_expirada",relatorios.getQtd_expirada());
        contentValues.put("qtd_finalizada",relatorios.getQtd_finalizada());
        contentValues.put("qtd_deletadas",relatorios.getQtd_deletadas());
        contentValues.put("mes",relatorios.getMes());
        contentValues.put("ano",relatorios.getAno());
        if(acao == 1) {
            contentValues.put("status", relatorios.getStatus());
        }else{
            contentValues.put("status", "desativo");
        }
        db.update("Relatorios",contentValues,where,null);
        db.close();

    }

    public void desativandoRelatorioAntigos(){

        Utilities utilities = new Utilities();
        String mesPassado = utilities.RetornarMesAtual(1);

        //primeiro é feita uma verificação afim de ver se existe um relatorio do mes anterio ativo
        //pesquisando se ja existem algum registro de relatorio para o mes atual
        String where = "mes = '"+ mesPassado+"' and status = 'ativa' ";
        db = banco.getReadableDatabase();
        Cursor cursor = db.query("Relatorios",colunas,where,null,null,null,null,null);
        Relatorios relatorios = new Relatorios();
        if(cursor != null) {

            while (cursor.moveToNext()) {


                relatorios.setId_relatorio(cursor.getInt(0));
                relatorios.setQtd_cadastro(cursor.getInt(1));
                relatorios.setQtd_expirada(cursor.getInt(2));
                relatorios.setQtd_finalizada(cursor.getInt(3));
                relatorios.setQtd_deletadas(cursor.getInt(4));
                relatorios.setMes(cursor.getString(5));
                relatorios.setAno(cursor.getInt(6));
                relatorios.setStatus(cursor.getString(7));

            }//fechamento do while que vai buscar os dados
        }//fechamento da verificação de null do cursor


        //se a variavel StatusMes for igual a 1 quer dizer que nao foi encontrado nehum registro para o mes corente , se for
        if(cursor.getCount() != 0){

            alteraRelatorio(relatorios,1);
        }


    }//fechamento do metodo desativandoRelatorioAntigos


    public int verifarExistMes(){
        int StatusMes = 0 ;


        //criando uma intancia da classe utilities e chamando um metodo que retorna o mes atual em forma de string
        Utilities utilities = new Utilities();

        // o paramentro tipo indica se vamos buscar um relatorio para o mes atual ou para o mes anterior

        String mesAtul = utilities.RetornarMesAtual(0);

        int Ano = utilities.recuperarAnoAtual();

        //pesquisando se ja existem algum registro de relatorio para o mes atual
        String where = "mes = '"+ mesAtul+"' and ano = "+Ano+" and status = 'ativa' ";
        db = banco.getReadableDatabase();
        Cursor cursor = db.query("Relatorios",colunas,where,null,null,null,null,null);

        //se a variavel StatusMes for igual a 1 quer dizer que nao foi encontrado nehum registro para o mes corente , se for




        if(cursor.getCount() == 0){
            StatusMes = 1 ;
            cadastrarRelatorio(mesAtul,Ano);
        }else{
            StatusMes = 2 ;
        }



        return StatusMes;
    }




    public void cadastrarRelatorio(String mes,int ano){

        int statusOperacao = 0 ;
        ContentValues valores ;
        Long resultado ;



        db = banco.getWritableDatabase(); //essa linha é resposanvel por iniciar a conexao
        valores = new ContentValues();
        valores.put("qtd_cadastro",0);
        valores.put("qtd_expirada",0);
        valores.put("qtd_finalizada",0);
        valores.put("qtd_deletadas",0);
        valores.put("status","ativa");
        valores.put("mes",mes);
        valores.put("ano",ano);


        resultado = db.insert("Relatorios",null,valores);
        db.close(); //fechando a conexao com o banco de dados

        if(resultado != -1){
            statusOperacao = 1;

        }

    }

}
