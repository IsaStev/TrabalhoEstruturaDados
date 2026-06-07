package controller;

import model.Disciplina;

public class TabelaHash {

    private ListaEncadeada<Disciplina>[] tabela;
    private final int TAMANHO = 11;

    @SuppressWarnings("unchecked")
    public TabelaHash() {
        tabela = new ListaEncadeada[TAMANHO];
        for (int i = 0; i < TAMANHO; i++) {
            tabela[i] = new ListaEncadeada<>();
        }
    }

    private int funcaoHash(int codigo) {
        return codigo % TAMANHO;
    }

    public void put(Disciplina disciplina) throws Exception {
        int indice = funcaoHash(disciplina.getCodigo());
        ListaEncadeada<Disciplina> lista = tabela[indice];

        // Evita duplicados na mesma posição da tabela
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getCodigo() == disciplina.getCodigo()) {
                return; // Ignora se já estiver na tabela
            }
        }
        lista.addLast(disciplina);
    }

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