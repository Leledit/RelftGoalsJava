package leandro.caixeta.relftgoals.classes;

public class Relatorios {



    private int id_relatorio ;
    private int qtd_cadastro ;
    private int qtd_expirada;
    private int qtd_finalizada;
    private int qtd_deletadas;
    private String mes ;
    private int ano;
    private String status;

    public int getQtd_deletadas() {
        return qtd_deletadas;
    }

    public void setQtd_deletadas(int qtd_deletadas) {
        this.qtd_deletadas = qtd_deletadas;
    }

    public int getId_relatorio() {
        return id_relatorio;
    }

    public void setId_relatorio(int id_relatorio) {
        this.id_relatorio = id_relatorio;
    }

    public int getQtd_cadastro() {
        return qtd_cadastro;
    }

    public void setQtd_cadastro(int qtd_cadastro) {
        this.qtd_cadastro = qtd_cadastro;
    }

    public int getQtd_expirada() {
        return qtd_expirada;
    }

    public void setQtd_expirada(int qtd_expirada) {
        this.qtd_expirada = qtd_expirada;
    }

    public int getQtd_finalizada() {
        return qtd_finalizada;
    }

    public void setQtd_finalizada(int qtd_finalizada) {
        this.qtd_finalizada = qtd_finalizada;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
