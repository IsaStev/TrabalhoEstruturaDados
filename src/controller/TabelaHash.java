package controller;

import model.Disciplina;

public class TabelaHash {
    // Um tamanho primo ajuda a reduzir a ocorrência de colisões na função Hash
    private static final int TAMANHO = 11;
    private ListaEncadeada<Disciplina>[] tabela;

    @SuppressWarnings("unchecked")
    public TabelaHash() {
        tabela = new ListaEncadeada[TAMANHO];
        for (int i = 0; i < TAMANHO; i++) {
            tabela[i] = new ListaEncadeada<Disciplina>();
        }
    }

    // função matemática de espalhamento - Função Hash
    private int funcaoHash(int codigoDisciplina) {
        return codigoDisciplina % TAMANHO;
    }

    // Insere a disciplina na tabela de forma espalhada
    public void put(Disciplina disp) throws Exception {
        int indice = funcaoHash(disp.codigo);
        tabela[indice].addLast(disp);
    }

    // Retorna todas as disciplinas armazenadas na tabela para exibição na tela
    public ListaEncadeada<Disciplina> getAll() throws Exception {
        ListaEncadeada<Disciplina> todas = new ListaEncadeada<>();
        for (int i = 0; i < TAMANHO; i++) {
            for (int j = 0; j < tabela[i].size(); j++) {
                todas.addLast(tabela[i].get(j));
            }
        }
        return todas;
    }
}