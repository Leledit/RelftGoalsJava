package leandro.caixeta.relftgoals.classes;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.auth.User;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;


public class Usuario implements Serializable {

    private String Uid;
    private String nome;
    private String data;
    private String email;
    private String senha;
    private String confSenha ;
    private String tipo;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    //demais metodos da aplicação



    public void deslogarUsuario(){
        FirebaseAuth usuario = FirebaseAuth.getInstance();
        usuario.signOut();

    }

    //Metodo responsavel por setar a senha e o email do usuario logado atualmente
    public void setarEmail(){
        FirebaseUser User = FirebaseAuth.getInstance().getCurrentUser();
        this.email = User.getEmail();
    }

    public void SetarUidUsuarioAtual(){

        FirebaseUser User = FirebaseAuth.getInstance().getCurrentUser();

        this.Uid = User.getUid();

    }
    public String retornarUidUsuarioLogado(){
        String uidUsuario ;

        FirebaseUser User = FirebaseAuth.getInstance().getCurrentUser();

        uidUsuario = User.getUid();

        return  uidUsuario;
    }

    public String RetirarMascarNumero(String numero , Context tela){

        String resultado = numero.replace("(","");
        resultado = resultado.replace(")","");
        resultado = resultado.replace("-","");
        resultado = resultado.replace(" ","");
        return resultado;

    }
    public Boolean verificarDataNaciment(){

        DateFormat dataform = new SimpleDateFormat("dd/MM/yyyy");
        dataform.setLenient(false);
        try{
            dataform.parse(data);
            return true;
        }catch (Exception e) {
            return false;
        }
    }

    public boolean verificarAnoDataNaciment(){

        Utilities utilities = new Utilities();

        int anoRecb = utilities.cortarDataAno(data);

        if(anoRecb>2020){
            return false;
        }else if(anoRecb< 1900){
            return  false;
        }else{
            return  true;
        }

    }


    //contrutor da aplicação
    public Usuario() {
    }

    //metodos get and set
  public boolean verificandoSenha(){
        if (this.senha.equals(this.confSenha)){
            return true;
        }else{
            return  false;
        }
  }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid=uid;
    }


    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNome() {
        return nome;
    }

    public int setNome(String nome) {

        int erro = 0 ;
        if((nome == null) || (nome.trim().isEmpty())){
            erro = 1 ;
        }else if(nome.trim().length()<3){
            erro = 2 ;
        }else{
            erro = 0;
            this.nome=nome;
        }

        return  erro;
    }

    public String getData() {
        return data;
    }

    public int setData(String data) {
        int erro = 0 ;

        if((data == "##/##/####") || (data.isEmpty())){
            erro = 1;

        } else if( data.length() != 10) {
            erro = 2;
    }else {


            erro = 0;
            this.data=data;

        }




        return erro ;
    }

    public String getEmail() {
        return email;
    }

    public Boolean setEmail(String email) {

        if((email == null)||(email.isEmpty())){
            return  false;
        }else{
            this.email=email;
            return  true ;

        }
    }

    public String getSenha() {
        return senha;
    }

    public int setSenha(String senha) {

        int erro = 0 ;
        if((senha == null)||(senha.isEmpty())){
            erro = 1 ;
        }else if(senha.length()<6){
            erro = 2;
        } else{
            this.senha=senha;
            erro = 0;
        }

        return erro;
    }

    public String getConfSenha() {
        return confSenha;
    }

    public Boolean setConfSenha(String confSenha) {
        if((confSenha == null)||(confSenha.isEmpty())){
            return  false;
        }else {
            this.confSenha = confSenha;
            return  true;

        }

        }
}
