package model;

public class Disciplina {
	
	public int codigo;
    public String nome;
    public String diaSemana;
    public String horarioInicial;
    public int horasDiarias;
    public int codigoCurso; // Uma disciplina pertence a apenas um curso
    
    public Disciplina() {}
    
    public Disciplina(int codigo, String nome, String diaSemana, String horarioInicial, int horasDiarias, int codigoCurso) {
        this.codigo = codigo;
        this.nome = nome;
        this.diaSemana = diaSemana;
        this.horarioInicial = horarioInicial;
        this.horasDiarias = horasDiarias;
        this.codigoCurso = codigoCurso;
    }
    
    @Override
    public String toString() {
        return codigo + ";" + nome + ";" + diaSemana + ";" + horarioInicial + ";" + horasDiarias + ";" + codigoCurso;
    }
}