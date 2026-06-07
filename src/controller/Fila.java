package controller;

public class Fila<T> {
    private No<T> inicio;
    private No<T> fim;

    public Fila() {
        this.inicio = null;
        this.fim = null;
    }

    public boolean isEmpty() {
        return (this.inicio == null && this.fim == null);
    }

    // Insere no fim da fila
    public void insert(T item) {
        No<T> novoNo = new No<>(item);
        if (isEmpty()) {
            this.inicio = novoNo;
            this.fim = novoNo;
        } else {
            this.fim.proximo = novoNo;
            this.fim = novoNo;
        }
    }

    // Remove do início da fila
    public T remove() throws Exception {
        if (isEmpty()) {
            throw new Exception("Fila Vazia");
        }
        T dado = this.inicio.dado;
        if (this.inicio == this.fim) {
            this.inicio = null;
            this.fim = null;
        } else {
            this.inicio = this.inicio.proximo;
        }
        return dado;
    }
}