package model;

public class Inscricao {
	
	public String cpfProfessor;
    public int codigoDisciplina;
    public int codigoProcesso;
    
    public Inscricao() {}
    
    public Inscricao(String cpfProfessor, int codigoDisciplina, int codigoProcesso) {
        this.cpfProfessor = cpfProfessor;
        this.codigoDisciplina = codigoDisciplina;
        this.codigoProcesso = codigoProcesso;
    }
    
    @Override
    public String toString() {
        return cpfProfessor + ";" + codigoDisciplina + ";" + codigoProcesso;
    }
}