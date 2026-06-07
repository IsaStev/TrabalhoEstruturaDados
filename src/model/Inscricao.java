package model;

public class Inscricao {
    private String cpfProfessor;
    private int codigoDisciplina;
    private int codigoProcesso;

    public Inscricao() {}

    // Construtor com parâmetros
    public Inscricao(String cpfProfessor, int codigoDisciplina, int codigoProcesso) {
        this.cpfProfessor = cpfProfessor;
        this.codigoDisciplina = codigoDisciplina;
        this.codigoProcesso = codigoProcesso;
    }

    public String getCpfProfessor() { return cpfProfessor; }
    public void setCpfProfessor(String cpfProfessor) { this.cpfProfessor = cpfProfessor; }

    public int getCodigoDisciplina() { return codigoDisciplina; }
    public void setCodigoDisciplina(int codigoDisciplina) { this.codigoDisciplina = codigoDisciplina; }

    public int getCodigoProcesso() { return codigoProcesso; }
    public void setCodigoProcesso(int codigoProcesso) { this.codigoProcesso = codigoProcesso; }

    @Override
    public String toString() {
        return cpfProfessor + ";" + codigoDisciplina + ";" + codigoProcesso;
    }
}