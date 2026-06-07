package controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import model.Curso;

public class CursoController {

    // Caminho do arquivo baseado na pasta 'data' que criámos na raiz do projeto
    private final String caminhoArquivo = "data" + File.separator + "cursos.csv";

    public CursoController() {
        // Garante que o arquivo exista ao iniciar o controlador
        try {
            File arquivo = new File(caminhoArquivo);
            arquivo.getParentFile().mkdirs();
            if (!arquivo.exists()) {
                arquivo.createNewFile();
            }
        } catch (Exception e) {
            System.err.println("Erro ao inicializar arquivo de cursos: " + e.getMessage());
        }
    }

    // INSERÇÃO: Apenas grava o novo curso no final do arquivo CSV
    public void cadastrarCurso(Curso curso) throws Exception {
        // true no FileWriter indica que irá acrescentar dados no fim (append)
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(caminhoArquivo, true))) {
            bw.write(curso.toString());
            bw.newLine();
        }
    }

    // CONSULTA: Popular uma FILA a partir do arquivo
    public Fila<Curso> listarCursos() throws Exception {
        Fila<Curso> filaCursos = new Fila<>();
        File arquivo = new File(caminhoArquivo);

        if (!arquivo.exists()) {
            return filaCursos;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                if (linha.trim().isEmpty()) continue; // Ignora linhas em branco
                
                String[] partes = linha.split(";");
                Curso curso = new Curso();
                curso.codigo = Integer.parseInt(partes[0]);
                curso.nome = partes[1];
                curso.areaConhecimento = partes[2];
                
                filaCursos.insert(curso);
            }
        }
        return filaCursos;
    }

    // REMOÇÃO E ATUALIZAÇÃO:Envolve LISTA ENCADEADA evita linhas vazias
    public void removerOuAtualizarCurso(int codigoAlvo, Curso cursoAtualizado, boolean isRemocao) throws Exception {
        ListaEncadeada<Curso> lista = new ListaEncadeada<>();
        
        // 1. Carrega todo o arquivo CSV para dentro da Lista Encadeada manual
        try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                if (linha.trim().isEmpty()) continue;
                
                String[] partes = linha.split(";");
                Curso curso = new Curso(Integer.parseInt(partes[0]), partes[1], partes[2]);
                lista.addLast(curso);
            }
        }

        // 2. Procura a posição do elemento que queremos alterar/remover
        int posicaoAlvo = -1;
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).codigo == codigoAlvo) {
                posicaoAlvo = i;
                break;
            }
        }

        if (posicaoAlvo == -1) {
            throw new Exception("Curso não encontrado!");
        }

        // 3. Aplica a operação na Lista Encadeada
        if (isRemocao) {
            lista.remove(posicaoAlvo);
        } else {
            // Atualização: substitui os dados na posição
            Curso cursoExistente = lista.get(posicaoAlvo);
            cursoExistente.nome = cursoAtualizado.nome;
            cursoExistente.areaConhecimento = cursoAtualizado.areaConhecimento;
        }

        // 4. Reescreve o arquivo CSV do zero com o estado atual da lista - evita linhas vazias
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(caminhoArquivo, false))) {
            for (int i = 0; i < lista.size(); i++) {
                bw.write(lista.get(i).toString());
                bw.newLine();
            }
        }
    }
}