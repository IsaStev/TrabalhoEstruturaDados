package controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import model.Professor;

public class ProfessorController {

    private final String caminhoArquivo = "data" + File.separator + "professor.csv";

    public ProfessorController() {
        try {
            File arquivo = new File(caminhoArquivo);
            arquivo.getParentFile().mkdirs();
            if (!arquivo.exists()) {
                arquivo.createNewFile();
            }
        } catch (Exception e) {
            System.err.println("Erro ao inicializar arquivo de professores: " + e.getMessage());
        }
    }

    // 1. INSERÇÃO: Grava o novo professor no fim do arquivo CSV
    public void cadastrarProfessor(Professor professor) throws Exception {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(caminhoArquivo, true))) {
            bw.write(professor.toString());
            bw.newLine();
        }
    }

    // 2. CONSULTA: Popula uma FILA a partir do arquivo
    public Fila<Professor> listarProfessores() throws Exception {
        Fila<Professor> fila = new Fila<>();
        File arquivo = new File(caminhoArquivo);

        if (!arquivo.exists()) {
            return fila;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                if (linha.trim().isEmpty()) continue;
                
                String[] partes = linha.split(";");
                Professor prof = new Professor();
                prof.cpf = partes[0];
                prof.nome = partes[1];
                prof.area = partes[2];
                prof.pontuacao = Integer.parseInt(partes[3]);
                
                fila.insert(prof);
            }
        }
        return fila;
    }

    // 3. REMOÇÃO E ATUALIZAÇÃO: Envolve Lista Encadeada manual
    public void removerOuAtualizarProfessor(String cpfAlvo, Professor profAtualizado, boolean isRemocao) throws Exception {
        ListaEncadeada<Professor> lista = new ListaEncadeada<>();
        
        // Passo 1: Carrega todos os professores para a lista encadeada
        try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                if (linha.trim().isEmpty()) continue;
                String[] partes = linha.split(";");
                lista.addLast(new Professor(partes[0], partes[1], partes[2], Integer.parseInt(partes[3])));
            }
        }

        // Passo 2: Procura a posição do elemento pelo CPF (String)
        int posicaoAlvo = -1;
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).cpf.equals(cpfAlvo)) {
                posicaoAlvo = i;
                break;
            }
        }

        if (posicaoAlvo == -1) {
            throw new Exception("Professor não encontrado!");
        }

        // Passo 3: Aplica a operação na lista encadeada
        if (isRemocao) {
            lista.remove(posicaoAlvo);
        } else {
            Professor existente = lista.get(posicaoAlvo);
            existente.nome = profAtualizado.nome;
            existente.area = profAtualizado.area;
            existente.pontuacao = profAtualizado.pontuacao;
        }

        // Passo 4: Reescreve o arquivo CSV limpo
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(caminhoArquivo, false))) {
            for (int i = 0; i < lista.size(); i++) {
                bw.write(lista.get(i).toString());
                bw.newLine();
            }
        }
    }
}