package model;

public class Curso {
	
	public int codigo;
	public String nome;
	public String areaConhecimento;
	
	//Construtor vazio
	public Curso() {
		
	}
	
	// Construtor com parâmetros
    public Curso(int codigo, String nome, String areaConhecimento) {
        this.codigo = codigo;
        this.nome = nome;
        this.areaConhecimento = areaConhecimento;
    }
    
    @Override
    public String toString() {
        return codigo + ";" + nome + ";" + areaConhecimento;
    }
}