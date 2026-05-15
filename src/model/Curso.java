package model;

public class Curso {
	
	public int codigo;
	public String nome;
	public String areaConhecimento;
	
	//Construtor vazio
	public Curso() {
		
	}
	
	public String toString() {
        return codigo + ";" + nome + ";" + areaConhecimento;
    }
}