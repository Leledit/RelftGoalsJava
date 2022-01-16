package leandro.caixeta.relftgoals.classes;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Build;
import android.preference.PreferenceManager;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Utilities {
/*
* Nota do programador: nessa classe seram armazenados metodos que poderam ser usados em varias partes do app
* */


    //metodo resposanvel por apagar todos os arquivos de preferencias do usuario
    public void deletarSharedPreferences(Context context)
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    public Date retornarDataFusoHorario(){

        Date dataAtual = new Date(System.currentTimeMillis());
        TimeZone.setDefault(TimeZone.getTimeZone("America/Sao_Paulo"));

        return  dataAtual;
    }

    public  Calendar parseDate(String data) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(data));


        return  cal;
    }

    //metodo responsavel por recuperar a dataAtualTime

    public Timestamp converterStringTimestamp(String data)throws ParseException {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
            Date parsedDate = dateFormat.parse(data);
            Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
            return  timestamp ;
    }




    public void retirarTempoDatas(Timestamp data){

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dataIniString = dateFormat.format(data);

    }
    public Timestamp recuperarDataAtualTimesTamp(){


        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Locale ptBr = new Locale("pt", "BR");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", ptBr);
        Locale.setDefault(new Locale("pt", "BR"));

        return timestamp;
    }

    public Timestamp dataAtualAcrecidaTimes(int dias){

        Timestamp dataAcrecida = this.recuperarDataAtualTimesTamp();
        dataAcrecida.setDate(dataAcrecida.getDate()+dias);

        return dataAcrecida;

    }





    //metodo responsavel por recuperar a hora atual
    public String recuperarDataAtual(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        Calendar cal = Calendar.getInstance();
        int ano =  cal.get(Calendar.YEAR);
        int mes = cal.get(Calendar.MONTH) ;
        int dia = cal.get(Calendar.DAY_OF_MONTH);


        cal.set(ano,mes,dia);
        Date data_atual = cal.getTime();

        String dataAtual = dateFormat.format(data_atual);
        return dataAtual;
    }

    public int recuperarAnoAtual() {

        Calendar cal = Calendar.getInstance();
        int ano = cal.get(Calendar.YEAR);

        return ano;
    }


    //metodo resposnavel por retornar a hora atual
    public String recuperarHoraAtual(){

        SimpleDateFormat dateFormat_hora = new SimpleDateFormat("HH:mm:ss");

        Date data = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        Date data_atual = cal.getTime();
        String hora_atual = dateFormat_hora.format(data_atual);
        return  hora_atual ;
    }

    public String RetornarMesAtual(int tipo) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();

        int mes = cal.get(Calendar.MONTH) ;

        //esse paramentro tipo indica se vai retornar o mes anterior ao atual , ou se vai retornar o atual
        if(tipo == 1) {
           mes = mes -1 ;
        }

        String mesAtual = "";
        switch (mes){

            case 0 :
                mesAtual = "Janeiro";
                break;
            case 1:
                mesAtual = "Fevereiro";
                break;
            case 2:
                mesAtual = "Março";
                break;
            case 3:
                mesAtual = "Abril";
                break;
            case 4:
                mesAtual = "Maio";
                break;
            case 5:
                mesAtual = "Junho";
                break;
            case 6:
                mesAtual = "Julho";
                break;
            case 7:
                mesAtual = "Agosto";
                break;
            case 8:
                mesAtual = "Setembro";
                break;
            case 9:
                mesAtual = "Outubro";
                break;
            case 10:
                mesAtual = "Novembro";
                break;
            case 11:
                mesAtual = "Dezembro";
                break;

        }




        return mesAtual;
    }

    public Calendar RetornarDataAtual_Date() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();
        int ano =  cal.get(Calendar.YEAR);
        int mes = cal.get(Calendar.MONTH) ;
        int dia = cal.get(Calendar.DAY_OF_MONTH);

        cal.set(ano,mes,dia);
        Date data_atual = cal.getTime();

        return cal;
    }
    public Calendar RetornarDataAtual_menos_um() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        Calendar cal = Calendar.getInstance();

        int ano =  cal.get(Calendar.YEAR);
        int mes = cal.get(Calendar.MONTH) ;
        int dia = cal.get(Calendar.DAY_OF_MONTH);

        dia = dia +1 ;
        cal.set(ano,mes,dia);
        Date data_atual = cal.getTime();

        return cal;
    }

    public int RetornarDiaSemana(){

        Calendar cal = Calendar.getInstance();
        int diaSemana = cal.get(Calendar.DAY_OF_WEEK);

        return diaSemana;

    }
    //os 3 metodos abaixos serevem para dividir uma determinada data em dia,mes ,ano
    public int cortarDataDia(String data){

        String [] pedacosData = data.split("/");

        int anoRecb = Integer.parseInt(pedacosData[0]);

        return anoRecb;
    }
    public int cortarDataMes(String data){

        String [] pedacosData = data.split("/");

        int anoRecb = Integer.parseInt(pedacosData[1]);

        return anoRecb;
    }
    public int cortarDataAno(String data){

        String [] pedacosData = data.split("/");

        int anoRecb = Integer.parseInt(pedacosData[2]);

        return anoRecb;
    }
    public String retirarParentes(String valor){

        String [] pedacosvalor = valor.split("");

        return  pedacosvalor[0];
    }

}
