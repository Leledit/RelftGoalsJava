package leandro.caixeta.relftgoals.classes;



import android.content.Context;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.google.firebase.auth.FirebaseAuth;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import leandro.caixeta.relftgoals.R;


public class Tarefas implements Serializable {

    private String Nome;
    private String Descricao;
    private String data_inicial;
    private String data_final;
    private String status;
    private String prioridade;
    private String categoria;
    private int codCorPrioridade;
    private int uId ;
    private int diasRestantes ;
    private String uid_usuario ;

    public String getUid_usuario() {
        return uid_usuario;
    }

    public void setUid_usuario(String uid_usuario) {
        this.uid_usuario = uid_usuario;
    }

    public Tarefas(String nome, String descricao, String data_inicial, String data_final, String status, String prioridade, String categoria, int codCorPrioridade, int uId, int diasRestantes) {
        Nome = nome;
        Descricao = descricao;
        this.data_inicial = data_inicial;
        this.data_final = data_final;
        this.status = status;
        this.prioridade = prioridade;
        this.categoria = categoria;
        this.codCorPrioridade = codCorPrioridade;
        this.uId = uId;
        this.diasRestantes = diasRestantes;
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public  List<Tarefas> BuscarTarefas(int dias,  Context context ,String tipoBusca){

        //criação da intancia da classe que controla as operaçoes com o banco
        tarefasControler tarefasControler = new tarefasControler(context);
        //realizando a busca de todas as tarefas por status e jogando o resultado em uma lista
        List<Tarefas> listTarefasStaus = tarefasControler.BuscasTarefas(context , tipoBusca , FirebaseAuth.getInstance().getCurrentUser().getUid());




        List<Tarefas> tarefaHoje = new ArrayList<>();
        List<Tarefas> tarefaSemana = new ArrayList<>();
        List<Tarefas> tarefaMes = new ArrayList<>();
        List<Tarefas> tarefaAno = new ArrayList<>();


        List<Tarefas> tarefasListFiltrada = new ArrayList<>();





        for (int i = 0 ;i < listTarefasStaus.size() ;i++) {

            //pegando o codigo da cor que representa cada nivel de prioridade
            listTarefasStaus.get(i).setCodCorPrioridade(this.definindoCorDaPrioridade(listTarefasStaus.get(i).getPrioridade()));



            if(tipoBusca.equals("ativa")){
                //aqui é chamado o metodo "verificarStatusTarefa" que é resposnavel por verificar qual é a situação da tarefa e asim retornar um numero que representando-a
                int DefinirStatus = this.verificarStatusTarefa(listTarefasStaus.get(i).getData_final());

                 if(DefinirStatus == 1){
                    //se for 1 quer dizer que a tarefa ja espirou
                     tarefasControler.AlterarStatus(listTarefasStaus.get(i),1);
                     //se a tarefa espirou essa deve ser retirada da lista
                     listTarefasStaus.remove(i);

                     /**Adiconando mais um a qtd_Expiradas de tarefas desse mes(Relatorios) **/
                     //aqui é feito uma verificação que ve se exitem algum relatorio para o mes corrente(ativo) se nao existir é cadastrado um novo
                     relatoriosControler  relatControl = new relatoriosControler(context);


                     //aqui é chamado uma função que busca o relatorio ativo e muda alguns dos seus dados
                     relatControl.alterarContadorRelat("Expirada",context);

                }else if(DefinirStatus == 0 ) {
                    //se for 0 quer dizer que vence no futuro

                    long diasRestantes = this.calculandoDiasRestantes(listTarefasStaus.get(i).getData_final());

                     listTarefasStaus.get(i).setDiasRestantes((int) diasRestantes);

                    if(diasRestantes<=6){
                        tarefaSemana.add(listTarefasStaus.get(i));
                        tarefaMes.add(listTarefasStaus.get(i));
                        tarefaAno.add(listTarefasStaus.get(i));
                    }else if(diasRestantes<=29){

                        tarefaMes.add(listTarefasStaus.get(i));
                        tarefaAno.add(listTarefasStaus.get(i));
                    }else if(diasRestantes<=365){
                        tarefaAno.add(listTarefasStaus.get(i));

                    }//fechamento do bloco de if dos dias restantes

                    //aplicando o filtro por periodo na tarefa


                }else if(DefinirStatus == 2){
                    //se for 2 quer dizer que é uma tarefa de hoje

                    //como o vencimento é hoje , a tarefa recebe com valor para o dias restantes 0
                     listTarefasStaus.get(i).setDiasRestantes(0);
                    //adiciona a tarefa a sua respectiva lista
                     tarefaHoje.add(listTarefasStaus.get(i));
                     tarefaSemana.add(listTarefasStaus.get(i));
                     tarefaMes.add(listTarefasStaus.get(i));
                     tarefaAno.add(listTarefasStaus.get(i));
                }//fechamento do else da verificação de status da tarefa



            }else{
                listTarefasStaus.get(i).setDiasRestantes(-1);
                tarefasListFiltrada.add(listTarefasStaus.get(i));

            }//fechamento do else da verificaçao de tipo de busca






        }//fechamento do for


        if((tipoBusca.equals("ativa"))&&(dias==0)){
            tarefasListFiltrada = tarefaHoje;
        }else if((tipoBusca.equals("ativa"))&&(dias==7)){
            tarefasListFiltrada = tarefaSemana;
        }else if ((tipoBusca.equals("ativa"))&&(dias==30)) {
            tarefasListFiltrada = tarefaMes;
        }else if ((tipoBusca.equals("ativa"))&&(dias==365)) {
            tarefasListFiltrada = tarefaAno;

        }else{

        }



        return tarefasListFiltrada;
    }


    public int definindoCorDaPrioridade(String prioridade){

        int cordCor = 0 ;
        switch (prioridade){
            case "Muito alto":
                cordCor = R.color.PDC_Priorit_Tarefas_Muito_alto;
                break;
            case "alto":
                cordCor =  R.color.PDC_Priorit_Tarefas_alto;
                break;
            case "Medio":
                cordCor =  R.color.PDC_Priorit_Tarefas_Medio;
                break;
            case "Baixo":
                cordCor =  R.color.PDC_Priorit_Tarefas_Baixo;
                break;
        }//fechamento do switch

        return cordCor ;

    }
    public Long calculandoDiasRestantes(String dataFinal){
        Long diasRestantes = 00l ;

        Utilities utilities = new Utilities();
        //pegando a data atual em String
        String dataAtual =  utilities.recuperarDataAtual();

        int dataAtualAno = utilities.cortarDataAno(dataAtual);
        int dataFinalAno = utilities.cortarDataAno(dataFinal);


      if(!(dataFinalAno < dataAtualAno)){

          SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
          try {
              Date dataFinalDate = sdf.parse(dataFinal);
              Date dataAtualDate = sdf.parse(dataAtual);

              Long data =(dataFinalDate.getTime() - dataAtualDate.getTime())+3600000;
              diasRestantes = (data/86400000L);

          }catch (Exception e){

          }

      }




        return  diasRestantes;
    }
    public  int  verificarStatusTarefa(String dataFinal){
        int situcao = 0 ; // inicio ela com 0 pois parto do pricipio que a data vai ser futura,sendo que o codigo abaixo vai me dizer se eu estou errado

        Utilities utilities = new Utilities();
        //pegando a data  atual
        String dataAtual =  utilities.recuperarDataAtual();
        //cortando a data atual e dividido em varias variaveis
        int dataAtualDia = utilities.cortarDataDia(dataAtual);
        int dataAtualMes = utilities.cortarDataMes(dataAtual);
        int dataAtualAno = utilities.cortarDataAno(dataAtual);
        //cortando a data final e dividido em varias variaveis
        int dataFinalDia = utilities.cortarDataDia(dataFinal);
        int dataFinalMes = utilities.cortarDataMes(dataFinal);
        int dataFinalAno = utilities.cortarDataAno(dataFinal);

        //legenda da situaçoes possiveis:
        /* 0 - da futura
        *  1 - data Expirou
        *  2 - data iguais(é hoje)
        *
        * */

        //verificando se o ano ja espirou
        if(dataFinalAno<dataAtualAno){
            situcao = 1 ;
        }else if (dataAtualAno == dataFinalAno){
            if(dataFinalMes<dataAtualMes){
                situcao = 1 ;
            }else if(dataFinalMes== dataAtualMes){
                if(dataFinalDia<dataAtualDia){
                    situcao = 1 ;
                }else if(dataFinalDia == dataAtualDia){
                    situcao = 2 ;
                }
            }//fechamento do if da verificação de igualdade de mes

        }//fechamento do if da verificação de igualdade de ano



        return situcao;
    }

    public Tarefas() {
    }

    public String getNome() {
        return Nome;
    }


    public int getCodCorPrioridade() {
        return codCorPrioridade;
    }

    public void setCodCorPrioridade(int codCorPrioridade) {
        this.codCorPrioridade = codCorPrioridade;
    }

    public int getuId() {
        return uId;
    }

    public void setuId(int uId) {
        this.uId = uId;
    }

    public int getDiasRestantes() {
        return diasRestantes;
    }

    public void setDiasRestantes(int diasRestantes) {
        this.diasRestantes = diasRestantes;
    }

    public int setNome(String nome) {

        int nomeStatus = 0 ;

        if((nome == null)||(nome.trim().equals(""))){
            nomeStatus = 1;
        } else if(nome.trim().length()<5){
            nomeStatus = 2;
        }else if(nome.trim().length()>70){
            nomeStatus = 3;
        } else{
            Nome = nome;
            return 4;
        }

        return  nomeStatus;

    }

    public String getCategoria() {
        return categoria;
    }

    public boolean setCategoria(String categoria) {


        if(categoria == "sem categoria"){
          return  false;
        }else{

            this.categoria = categoria;
            return  true;
        }
    }

    public String getDescricao() {
        return Descricao;
    }

    public int setDescricao(String descricao) {

        int statusDescrip = 0 ;

        if((descricao == null)||( descricao.trim().equals(""))){

            Descricao = "Nenhuma descrição";


        }else if((descricao.length() < 5)){

           statusDescrip = 1;
        } else if(descricao.length() > 190){
           statusDescrip = 2;
        }else{
            Descricao = descricao;
          statusDescrip  = 3 ;
        }
         return  statusDescrip ;
    }

    public String getData_inicial() {
        return data_inicial;
    }

    public void setData_inicial(String data_inicial) {

        this.data_inicial = data_inicial;
    }

    public String getData_final() {
        return data_final;
    }

    public Boolean setData_final(String data_final) {


        if((data_final == null)||(data_final.equals(""))){

            return false;
        }else{

            this.data_final = data_final;
        }

        return true;

    }



    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPrioridade() {
        return prioridade;
    }

    public Boolean setPrioridade(String prioridade) {


        if(prioridade.equals("Sem estado")){

            return false;

        }else{
            this.prioridade = prioridade;
            return  true;
        }

    }
}
