package model;

public class Professor {
	
	public String cpf;
    public String nome;
    public String area;
    public int pontuacao;
    
    public Professor() {}
    
    public Professor(String cpf, String nome, String area, int pontuacao) {
        this.cpf = cpf;
        this.nome = nome;
        this.area = area;
        this.pontuacao = pontuacao;
    }
    
    @Override
    public String toString() {
        return cpf + ";" + nome + ";" + area + ";" + pontuacao;
    }
}