package controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import model.Inscricao;

public class InscricaoController {

    private final String caminhoArquivo = "data" + File.separator + "inscricoes.csv";

    public InscricaoController() {
        try {
            File arquivo = new File(caminhoArquivo);
            if (!arquivo.exists()) {
                arquivo.createNewFile();
            }
        } catch (Exception e) {
            System.err.println("Erro ao inicializar arquivo de inscrições: " + e.getMessage());
        }
    }

    // 1. INSERÇÃO: Grava a nova inscrição no fim do arquivo CSV
    public void cadastrarInscricao(Inscricao inscricao) throws Exception {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(caminhoArquivo, true))) {
            bw.write(inscricao.toString());
            bw.newLine();
        }
    }

    // 2. CONSULTA: Popula uma FILA a partir do arquivo para listagem geral
    public Fila<Inscricao> listarInscricoes() throws Exception {
        Fila<Inscricao> fila = new Fila<>();
        File arquivo = new File(caminhoArquivo);

        if (!arquivo.exists()) {
            return fila;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                if (linha.trim().isEmpty()) continue;
                
                String[] partes = linha.split(";");
                Inscricao insc = new Inscricao();
                insc.cpfProfessor = partes[0];
                insc.codigoDisciplina = Integer.parseInt(partes[1]);
                insc.codigoProcesso = Integer.parseInt(partes[2]);
                
                fila.insert(insc);
            }
        }
        return fila;
    }

    // 3. REMOÇÃO E ATUALIZAÇÃO: Envolve Lista Encadeada manual
    public void removerOuAtualizarInscricao(String cpfAlvo, int codigoDisciplinaAlvo, Inscricao inscAtualizada, boolean isRemocao) throws Exception {
        ListaEncadeada<Inscricao> lista = new ListaEncadeada<>();
        
        // Passo 1: Carrega todas as inscrições para a lista encadeada
        try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                if (linha.trim().isEmpty()) continue;
                String[] partes = linha.split(";");
                lista.addLast(new Inscricao(partes[0], Integer.parseInt(partes[1]), Integer.parseInt(partes[2])));
            }
        }

        // Passo 2: Procura a posição do elemento combinando o CPF e o Código da Disciplina
        int posicaoAlvo = -1;
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).cpfProfessor.equals(cpfAlvo) && lista.get(i).codigoDisciplina == codigoDisciplinaAlvo) {
                posicaoAlvo = i;
                break;
            }
        }

        if (posicaoAlvo == -1) {
            throw new Exception("Inscrição não encontrada!");
        }

        // Passo 3: Aplica a operação na lista encadeada
        if (isRemocao) {
            lista.remove(posicaoAlvo);
        } else {
            Inscricao existente = lista.get(posicaoAlvo);
            existente.codigoProcesso = inscAtualizada.codigoProcesso;
        }

        // Passo 4: Reescreve o arquivo CSV limpo sem deixar linhas vazias
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(caminhoArquivo, false))) {
            for (int i = 0; i < lista.size(); i++) {
                bw.write(lista.get(i).toString());
                bw.newLine();
            }
        }
    }
}