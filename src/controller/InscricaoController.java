package controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import model.Inscricao;
import model.Professor;
import model.Disciplina;

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

    public void cadastrarInscricao(Inscricao inscricao) throws Exception {
        if (inscricao.getCpfProfessor() == null || inscricao.getCpfProfessor().trim().isEmpty()) {
            throw new Exception("O CPF do professor é obrigatório!");
        }

        // Verifica se o professor ja existe
        ProfessorController profCtrl = new ProfessorController();
        Fila<Professor> professores = profCtrl.listarProfessores();
        boolean profExiste = false;
        while (!professores.isEmpty()) {
            if (professores.remove().getCpf().equals(inscricao.getCpfProfessor())) {
                profExiste = true;
                break;
            }
        }
        if (!profExiste) {
            throw new Exception("Erro de Integridade: Não existe nenhum professor com o CPF informado!");
        }

        // Verifica se a disciplina já existe
        DisciplinaController dispCtrl = new DisciplinaController();
        Fila<Disciplina> disciplinas = dispCtrl.listarDisciplinas();
        boolean dispExiste = false;
        while (!disciplinas.isEmpty()) {
            if (disciplinas.remove().getCodigo() == inscricao.getCodigoDisciplina()) {
                dispExiste = true;
                break;
            }
        }
        if (!dispExiste) {
            throw new Exception("Erro de Integridade: Código de disciplina inexistente!");
        }

        // Validação de duplicados (Impede dupla inscrição no mesmo processo/disciplina)
        Fila<Inscricao> inscricoesExistentes = listarInscricoes();
        while (!inscricoesExistentes.isEmpty()) {
            Inscricao cadastrada = inscricoesExistentes.remove();
            if (cadastrada.getCpfProfessor().equals(inscricao.getCpfProfessor()) &&
                cadastrada.getCodigoDisciplina() == inscricao.getCodigoDisciplina()) {
                throw new Exception("O candidato já está inscrito nesta disciplina!");
            }
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(caminhoArquivo, true))) {
            bw.write(inscricao.toString());
            bw.newLine();
        }
    }

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
                insc.setCpfProfessor(partes[0]);
                insc.setCodigoDisciplina(Integer.parseInt(partes[1]));
                insc.setCodigoProcesso(Integer.parseInt(partes[2]));
                
                fila.insert(insc);
            }
        }
        return fila;
    }

    public void removerOuAtualizarInscricao(String cpfAlvo, int codigoDisciplinaAlvo, Inscricao inscAtualizada, boolean isRemocao) throws Exception {
        String linha;
        ListaEncadeada<Inscricao> lista = new ListaEncadeada<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
            while ((linha = br.readLine()) != null) {
                if (linha.trim().isEmpty()) continue;
                String[] partes = linha.split(";");
                Inscricao ins = new Inscricao();
                ins.setCpfProfessor(partes[0]);
                ins.setCodigoDisciplina(Integer.parseInt(partes[1]));
                ins.setCodigoProcesso(Integer.parseInt(partes[2]));
                lista.addLast(ins);
            }
        }

        int posicaoAlvo = -1;
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getCpfProfessor().equals(cpfAlvo) && lista.get(i).getCodigoDisciplina() == codigoDisciplinaAlvo) {
                posicaoAlvo = i;
                break;
            }
        }

        if (posicaoAlvo == -1) {
            throw new Exception("Inscrição não encontrada!");
        }

        if (isRemocao) {
            lista.remove(posicaoAlvo);
        } else {
        	Inscricao existente = lista.get(posicaoAlvo);
            existente.setCodigoProcesso(inscAtualizada.getCodigoProcesso());
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(caminhoArquivo, false))) {
            for (int i = 0; i < lista.size(); i++) {
                bw.write(lista.get(i).toString());
                bw.newLine();
            }
        }
    }

    public ListaEncadeada<Professor> listarInscritosOrdenados(int codigoDisciplinaAlvo) throws Exception {
        ListaEncadeada<Professor> listaInscritos = new ListaEncadeada<>();
        
        Fila<Inscricao> todasInscricoes = listarInscricoes();
        ListaEncadeada<Inscricao> inscricoesFiltradas = new ListaEncadeada<>();
        
        while (!todasInscricoes.isEmpty()) {
            Inscricao insc = todasInscricoes.remove();
            if (insc.getCodigoDisciplina() == codigoDisciplinaAlvo) {
                inscricoesFiltradas.addLast(insc);
            }
        }

        ProfessorController profCtrl = new ProfessorController();
        for (int i = 0; i < inscricoesFiltradas.size(); i++) {
            String cpfInscrito = inscricoesFiltradas.get(i).getCpfProfessor();
            
            Fila<Professor> todosProfessores = profCtrl.listarProfessores();
            while (!todosProfessores.isEmpty()) {
                Professor prof = todosProfessores.remove();
                if (prof.getCpf().equals(cpfInscrito)) {
                    listaInscritos.addLast(prof);
                    break;
                }
            }
        }

        int tamanho = listaInscritos.size();
        for (int i = 0; i < tamanho - 1; i++) {
            for (int j = 0; j < tamanho - 1 - i; j++) {
                Professor profA = listaInscritos.get(j);
                Professor profB = listaInscritos.get(j + 1);
                
                if (profA.getPontuacao() < profB.getPontuacao()) {
                    String tempCpf = profA.getCpf();
                    String tempNome = profA.getNome();
                    String tempArea = profA.getArea();
                    int tempPontos = profA.getPontuacao();
                    
                    profA.setCpf(profB.getCpf());
                    profA.setNome(profB.getNome());
                    profA.setArea(profB.getArea());
                    profA.setPontuacao(profB.getPontuacao());
                    
                    profB.setCpf(tempCpf);
                    profB.setNome(tempNome);
                    profB.setArea(tempArea);
                    profB.setPontuacao(tempPontos);
                }
            }
        }
        return listaInscritos;
    }
}