package controller;

public class ListaEncadeada<T> {
    private No<T> primeiro;

    public ListaEncadeada() {
        this.primeiro = null;
    }

    public boolean isEmpty() {
        return (this.primeiro == null);
    }

    // Adiciona no final da lista
    public void addLast(T item) {
        No<T> novoNo = new No<>(item);
        if (isEmpty()) {
            this.primeiro = novoNo;
        } else {
            No<T> auxiliar = this.primeiro;
            while (auxiliar.proximo != null) {
                auxiliar = auxiliar.proximo;
            }
            auxiliar.proximo = novoNo;
        }
    }

    // Retorna o tamanho atual da lista
    public int size() {
        int cont = 0;
        No<T> auxiliar = this.primeiro;
        while (auxiliar != null) {
            cont++;
            auxiliar = auxiliar.proximo;
        }
        return cont;
    }

    // Pega o elemento de uma posição específica
    public T get(int posicao) throws Exception {
        if (isEmpty() || posicao < 0 || posicao >= size()) {
            throw new Exception("Posição Inválida");
        }
        No<T> auxiliar = this.primeiro;
        for (int i = 0; i < posicao; i++) {
            auxiliar = auxiliar.proximo;
        }
        return auxiliar.dado;
    }

    // Remove um elemento de uma posição específica
    public void remove(int posicao) throws Exception {
        if (isEmpty() || posicao < 0 || posicao >= size()) {
            throw new Exception("Posição Inválida");
        }
        if (posicao == 0) {
            this.primeiro = this.primeiro.proximo;
        } else {
            No<T> auxiliar = this.primeiro;
            for (int i = 0; i < posicao - 1; i++) {
                auxiliar = auxiliar.proximo;
            }
            auxiliar.proximo = auxiliar.proximo.proximo;
        }
    }
}