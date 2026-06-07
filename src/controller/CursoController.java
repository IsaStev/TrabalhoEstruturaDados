package controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import model.Curso;

public class CursoController {

    private final String caminhoArquivo = "data" + File.separator + "cursos.csv";

    public CursoController() {
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

    public void cadastrarCurso(Curso curso) throws Exception {
        // Validação de campos vazios
        if (curso.getNome() == null || curso.getNome().trim().isEmpty() ||
            curso.getAreaConhecimento() == null || curso.getAreaConhecimento().trim().isEmpty()) {
            throw new Exception("Todos os campos do curso devem ser preenchidos!");
        }

        // Validação de duplicados (Chave primária)
        Fila<Curso> cursosExistentes = listarCursos();
        while (!cursosExistentes.isEmpty()) {
            Curso cadastrado = cursosExistentes.remove();
            if (cadastrado.getCodigo() == curso.getCodigo()) {
                throw new Exception("Já existe um curso cadastrado com este código!");
            }
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(caminhoArquivo, true))) {
            bw.write(curso.toString());
            bw.newLine();
        }
    }

    public Fila<Curso> listarCursos() throws Exception {
        Fila<Curso> filaCursos = new Fila<>();
        File arquivo = new File(caminhoArquivo);

        if (!arquivo.exists()) {
            return filaCursos;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                if (linha.trim().isEmpty()) continue;
                
                String[] partes = linha.split(";");
                Curso curso = new Curso();
                curso.setCodigo(Integer.parseInt(partes[0]));
                curso.setNome(partes[1]);
                curso.setAreaConhecimento(partes[2]);
                
                filaCursos.insert(curso);
            }
        }
        return filaCursos;
    }

    public void removerOuAtualizarCurso(int codigoAlvo, Curso cursoAtualizado, boolean isRemocao) throws Exception {
        ListaEncadeada<Curso> lista = new ListaEncadeada<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                if (linha.trim().isEmpty()) continue;
                String[] partes = linha.split(";");
                lista.addLast(new Curso(Integer.parseInt(partes[0]), partes[1], partes[2]));
            }
        }

        int posicaoAlvo = -1;
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getCodigo() == codigoAlvo) {
                posicaoAlvo = i;
                break;
            }
        }

        if (posicaoAlvo == -1) {
            throw new Exception("Curso não encontrado!");
        }

        if (isRemocao) {
            lista.remove(posicaoAlvo);
        } else {
            if (cursoAtualizado.getNome() == null || cursoAtualizado.getNome().trim().isEmpty() ||
                cursoAtualizado.getAreaConhecimento() == null || cursoAtualizado.getAreaConhecimento().trim().isEmpty()) {
                throw new Exception("Os campos para atualização não podem ser vazios!");
            }
            Curso cursoExistente = lista.get(posicaoAlvo);
            cursoExistente.setNome(cursoAtualizado.getNome());
            cursoExistente.setAreaConhecimento(cursoAtualizado.getAreaConhecimento());
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(caminhoArquivo, false))) {
            for (int i = 0; i < lista.size(); i++) {
                bw.write(lista.get(i).toString());
                bw.newLine();
            }
        }
    }
}