package controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import model.Disciplina;
import model.Inscricao;
import model.Curso;

public class DisciplinaController {

    private final String caminhoArquivo = "data" + File.separator + "disciplinas.csv";
    private final String caminhoInscricoes = "data" + File.separator + "inscricoes.csv";

    public DisciplinaController() {
        try {
            File arquivo = new File(caminhoArquivo);
            arquivo.getParentFile().mkdirs();
            if (!arquivo.exists()) {
                arquivo.createNewFile();
            }
        } catch (Exception e) {
            System.err.println("Erro ao inicializar arquivo de disciplinas: " + e.getMessage());
        }
    }

    public void cadastrarDisciplina(Disciplina disciplina) throws Exception {
        if (disciplina.getNome() == null || disciplina.getNome().trim().isEmpty() ||
            disciplina.getDiaSemana() == null || disciplina.getDiaSemana().trim().isEmpty() ||
            disciplina.getHorarioInicial() == null || disciplina.getHorarioInicial().trim().isEmpty()) {
            throw new Exception("Todos os campos da disciplina devem ser preenchidos!");
        }

        // Verifica se o curso já existe
        CursoController cursoCtrl = new CursoController();
        Fila<Curso> cursos = cursoCtrl.listarCursos();
        boolean cursoExiste = false;
        while (!cursos.isEmpty()) {
            if (cursos.remove().getCodigo() == disciplina.getCodigoCurso()) {
                cursoExiste = true;
                break;
            }
        }
        if (!cursoExiste) {
            throw new Exception("Erro de Integridade: O código do curso informado não existe!");
        }

        // Validação de duplicados
        Fila<Disciplina> disciplinasExistentes = listarDisciplinas();
        while (!disciplinasExistentes.isEmpty()) {
            if (disciplinasExistentes.remove().getCodigo() == disciplina.getCodigo()) {
                throw new Exception("Já existe uma disciplina cadastrada com este código!");
            }
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(caminhoArquivo, true))) {
            bw.write(disciplina.toString());
            bw.newLine();
        }
    }

    public Fila<Disciplina> listarDisciplinas() throws Exception {
        Fila<Disciplina> fila = new Fila<>();
        File arquivo = new File(caminhoArquivo);

        if (!arquivo.exists()) {
            return fila;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                if (linha.trim().isEmpty()) continue;
                
                String[] partes = linha.split(";");
                Disciplina disp = new Disciplina();
                disp.setCodigo(Integer.parseInt(partes[0]));
                disp.setNome(partes[1]);
                disp.setDiaSemana(partes[2]);
                disp.setHorarioInicial(partes[3]);
                disp.setHorasDiarias(Integer.parseInt(partes[4]));
                disp.setCodigoCurso(Integer.parseInt(partes[5]));
                
                fila.insert(disp);
            }
        }
        return fila;
    }

    public void removerOuAtualizarDisciplina(int codigoAlvo, Disciplina dispAtualizada, boolean isRemocao) throws Exception {
        String linha;
        ListaEncadeada<Disciplina> listaDisp = new ListaEncadeada<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
            while ((linha = br.readLine()) != null) {
                if (linha.trim().isEmpty()) continue;
                String[] partes = linha.split(";");
                listaDisp.addLast(new Disciplina(Integer.parseInt(partes[0]), partes[1], partes[2], partes[3], Integer.parseInt(partes[4]), Integer.parseInt(partes[5])));
            }
        }

        int posicaoAlvo = -1;
        for (int i = 0; i < listaDisp.size(); i++) {
            if (listaDisp.get(i).getCodigo() == codigoAlvo) {
                posicaoAlvo = i;
                break;
            }
        }

        if (posicaoAlvo == -1) {
            throw new Exception("Disciplina não encontrada!");
        }

        if (isRemocao) {
            listaDisp.remove(posicaoAlvo);
            removerInscricoesEmCascata(codigoAlvo);
        } else {
            if (dispAtualizada.getNome() == null || dispAtualizada.getNome().trim().isEmpty()) {
                throw new Exception("Campos obrigatórios não podem ficar vazios!");
            }
            Disciplina existente = listaDisp.get(posicaoAlvo);
            existente.setNome(dispAtualizada.getNome());
            existente.setDiaSemana(dispAtualizada.getDiaSemana());
            existente.setHorarioInicial(dispAtualizada.getHorarioInicial());
            existente.setHorasDiarias(dispAtualizada.getHorasDiarias());
            existente.setCodigoCurso(dispAtualizada.getCodigoCurso());
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(caminhoArquivo, false))) {
            for (int i = 0; i < listaDisp.size(); i++) {
                bw.write(listaDisp.get(i).toString());
                bw.newLine();
            }
        }
    }

    private void removerInscricoesEmCascata(int codigoDisciplinaAlvo) throws Exception {
        File arquivoInsc = new File(caminhoInscricoes);
        if (!arquivoInsc.exists()) return;

        String linha;
        ListaEncadeada<Inscricao> listaInsc = new ListaEncadeada<>();

        try (BufferedReader br = new BufferedReader(new FileReader(caminhoInscricoes))) {
            while ((linha = br.readLine()) != null) {
                if (linha.trim().isEmpty()) continue;
                String[] partes = linha.split(";");
                Inscricao insc = new Inscricao();
                insc.setCpfProfessor(partes[0]);
                insc.setCodigoDisciplina(Integer.parseInt(partes[1]));
                insc.setCodigoProcesso(Integer.parseInt(partes[2]));
                listaInsc.addLast(insc);
            }
        }

        for (int i = listaInsc.size() - 1; i >= 0; i--) {
            if (listaInsc.get(i).getCodigoDisciplina() == codigoDisciplinaAlvo) {
                listaInsc.remove(i);
            }
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(caminhoInscricoes, false))) {
            for (int i = 0; i < listaInsc.size(); i++) {
                bw.write(listaInsc.get(i).toString());
                bw.newLine();
            }
        }
    }

    public TabelaHash listarProcessosAbertos() throws Exception {
        TabelaHash tabelaHash = new TabelaHash();
        InscricaoController inscCtrl = new InscricaoController();
        
        Fila<Inscricao> inscricoes = inscCtrl.listarInscricoes();
        ListaEncadeada<Integer> codigosAtivos = new ListaEncadeada<>();
        
        while (!inscricoes.isEmpty()) {
            Inscricao insc = inscricoes.remove();
            
            boolean jaExiste = false;
            for (int i = 0; i < codigosAtivos.size(); i++) {
                if (codigosAtivos.get(i) == insc.getCodigoDisciplina()) {
                    jaExiste = true;
                    break;
                }
            }
            if (!jaExiste) {
                codigosAtivos.addLast(insc.getCodigoDisciplina());
            }
        }

        Fila<Disciplina> todasDisciplinas = listarDisciplinas();
        while (!todasDisciplinas.isEmpty()) {
            Disciplina disp = todasDisciplinas.remove();
            for (int i = 0; i < codigosAtivos.size(); i++) {
                if (codigosAtivos.get(i) == disp.getCodigo()) {
                    tabelaHash.put(disp);
                    break;
                }
            }
        }
        return tabelaHash;
    }
}