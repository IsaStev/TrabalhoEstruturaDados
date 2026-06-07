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
            arquivo.getParentFile().mkdirs();
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
    
 // CONSULTA AVANÇADA: Retorna uma lista de professores inscritos ordenada por pontuação
    public ListaEncadeada<model.Professor> listarInscritosOrdenados(int codigoDisciplinaAlvo) throws Exception {
        ListaEncadeada<model.Professor> listaInscritos = new ListaEncadeada<>();
        
        // 1. Carrega todas as inscrições que batem com a disciplina escolhida
        Fila<Inscricao> todasInscricoes = listarInscricoes();
        ListaEncadeada<Inscricao> inscricoesFiltradas = new ListaEncadeada<>();
        
        while (!todasInscricoes.isEmpty()) {
            Inscricao insc = todasInscricoes.remove();
            if (insc.codigoDisciplina == codigoDisciplinaAlvo) {
                inscricoesFiltradas.addLast(insc);
            }
        }

        // 2. Para cada inscrição filtrada, busca os dados completos do professor no arquivo
        ProfessorController profCtrl = new ProfessorController();
        for (int i = 0; i < inscricoesFiltradas.size(); i++) {
            String cpfInscrito = inscricoesFiltradas.get(i).cpfProfessor;
            
            // Busca o professor na fila de professores
            Fila<model.Professor> todosProfessores = profCtrl.listarProfessores();
            while (!todosProfessores.isEmpty()) {
                model.Professor prof = todosProfessores.remove();
                if (prof.cpf.equals(cpfInscrito)) {
                    listaInscritos.addLast(prof);
                    break;
                }
            }
        }

     // 3. Algoritmo de Ordenação Manual (Bubble Sort) - Decrescente
        int tamanho = listaInscritos.size();
        for (int i = 0; i < tamanho - 1; i++) {
            for (int j = 0; j < tamanho - 1 - i; j++) {
                model.Professor profA = listaInscritos.get(j);
                model.Professor profB = listaInscritos.get(j + 1);
                
                if (profA.pontuacao < profB.pontuacao) {
                    // Faz a troca dos dados diretamente alterando os atributos do objeto auxiliar
                    String tempCpf = profA.cpf;
                    String tempNome = profA.nome;
                    String tempArea = profA.area;
                    int tempPontos = profA.pontuacao;
                    
                    profA.cpf = profB.cpf;
                    profA.nome = profB.nome;
                    profA.area = profB.area;
                    profA.pontuacao = profB.pontuacao;
                    
                    profB.cpf = tempCpf;
                    profB.nome = tempNome;
                    profB.area = tempArea;
                    profB.pontuacao = tempPontos;
                }
            }
        }
        return listaInscritos;
    }
}