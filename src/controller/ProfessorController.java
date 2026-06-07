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

    public void cadastrarProfessor(Professor professor) throws Exception {
        if (professor.getCpf() == null || professor.getCpf().trim().isEmpty() ||
            professor.getNome() == null || professor.getNome().trim().isEmpty() ||
            professor.getArea() == null || professor.getArea().trim().isEmpty()) {
            throw new Exception("Todos os campos do professor devem ser informados!");
        }

        Fila<Professor> fila = listarProfessores();
        while (!fila.isEmpty()) {
            if (fila.remove().getCpf().equals(professor.getCpf())) {
                throw new Exception("Já existe um professor cadastrado com este CPF!");
            }
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(caminhoArquivo, true))) {
            bw.write(professor.toString());
            bw.newLine();
        }
    }

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
                prof.setCpf(partes[0]);
                prof.setNome(partes[1]);
                prof.setArea(partes[2]);
                prof.setPontuacao(Integer.parseInt(partes[3]));
                
                fila.insert(prof);
            }
        }
        return fila;
    }

    public void removerOuAtualizarProfessor(String cpfAlvo, Professor profAtualizado, boolean isRemocao) throws Exception {
        String linha;
        ListaEncadeada<Professor> lista = new ListaEncadeada<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
            while ((linha = br.readLine()) != null) {
                if (linha.trim().isEmpty()) continue;
                String[] partes = linha.split(";");
                lista.addLast(new Professor(partes[0], partes[1], partes[2], Integer.parseInt(partes[3])));
            }
        }

        int posicaoAlvo = -1;
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getCpf().equals(cpfAlvo)) {
                posicaoAlvo = i;
                break;
            }
        }

        if (posicaoAlvo == -1) {
            throw new Exception("Professor não encontrado!");
        }

        if (isRemocao) {
            lista.remove(posicaoAlvo);
        } else {
            if (profAtualizado.getNome() == null || profAtualizado.getNome().trim().isEmpty()) {
                throw new Exception("Os campos modificados não podem ser vazios!");
            }
            Professor existente = lista.get(posicaoAlvo);
            existente.setNome(profAtualizado.getNome());
            existente.setArea(profAtualizado.getArea());
            existente.setPontuacao(profAtualizado.getPontuacao());
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(caminhoArquivo, false))) {
            for (int i = 0; i < lista.size(); i++) {
                bw.write(lista.get(i).toString());
                bw.newLine();
            }
        }
    }
}