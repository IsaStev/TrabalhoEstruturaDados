package controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import model.Disciplina;
import model.Inscricao;

public class DisciplinaController {

    private final String caminhoArquivo = "data" + File.separator + "disciplinas.csv";
    private final String caminhoInscricoes = "data" + File.separator + "inscricoes.csv";

    public DisciplinaController() {
        try {
            File arquivo = new File(caminhoArquivo);
            if (!arquivo.exists()) {
                arquivo.createNewFile();
            }
        } catch (Exception e) {
            System.err.println("Erro ao inicializar arquivo de disciplinas: " + e.getMessage());
        }
    }

    // INSERÇÃO: Grava a nova disciplina no fim do arquivo CSV
    public void cadastrarDisciplina(Disciplina disciplina) throws Exception {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(caminhoArquivo, true))) {
            bw.write(disciplina.toString());
            bw.newLine();
        }
    }

    // CONSULTA: Popula uma FILA a partir do arquivo
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
                disp.codigo = Integer.parseInt(partes[0]);
                disp.nome = partes[1];
                disp.diaSemana = partes[2];
                disp.horarioInicial = partes[3];
                disp.horasDiarias = Integer.parseInt(partes[4]);
                disp.codigoCurso = Integer.parseInt(partes[5]);
                
                fila.insert(disp);
            }
        }
        return fila;
    }

    // REMOÇÃO E ATUALIZAÇÃO: Envolve Lista Encadeada + Remoção em Cascata das Inscrições
    public void removerOuAtualizarDisciplina(int codigoAlvo, Disciplina dispAtualizada, boolean isRemocao) throws Exception {
        ListaEncadeada<Disciplina> listaDisp = new ListaEncadeada<>();
        
        // 1. Carrega as disciplinas para a lista encadeada
        try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                if (linha.trim().isEmpty()) continue;
                String[] partes = linha.split(";");
                listaDisp.addLast(new Disciplina(Integer.parseInt(partes[0]), partes[1], partes[2], partes[3], Integer.parseInt(partes[4]), Integer.parseInt(partes[5])));
            }
        }

        // 2. Procura a posição do elemento que queremos alterar/remover
        int posicaoAlvo = -1;
        for (int i = 0; i < listaDisp.size(); i++) {
            if (listaDisp.get(i).codigo == codigoAlvo) {
                posicaoAlvo = i;
                break;
            }
        }

        if (posicaoAlvo == -1) {
            throw new Exception("Disciplina não encontrada!");
        }

        // 3. Aplica a operação na lista de disciplinas
        if (isRemocao) {
            listaDisp.remove(posicaoAlvo);
            
            // Remoção em cascata no .csv
            removerInscricoesEmCascata(codigoAlvo);
        } else {
            Disciplina existente = listaDisp.get(posicaoAlvo);
            existente.nome = dispAtualizada.nome;
            existente.diaSemana = dispAtualizada.diaSemana;
            existente.horarioInicial = dispAtualizada.horarioInicial;
            existente.horasDiarias = dispAtualizada.horasDiarias;
            existente.codigoCurso = dispAtualizada.codigoCurso;
        }

        // 4. Reescreve o arquivo de disciplinas limpo
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(caminhoArquivo, false))) {
            for (int i = 0; i < listaDisp.size(); i++) {
                bw.write(listaDisp.get(i).toString());
                bw.newLine();
            }
        }
    }

    // Método auxiliar privado para limpar as inscrições vinculadas à disciplina excluída
    private void removerInscricoesEmCascata(int codigoDisciplinaAlvo) throws Exception {
        File arquivoInsc = new File(caminhoInscricoes);
        if (!arquivoInsc.exists()) return;

        ListaEncadeada<Inscricao> listaInsc = new ListaEncadeada<>();

        // Le todas as inscrições existentes
        try (BufferedReader br = new BufferedReader(new FileReader(caminhoInscricoes))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                if (linha.trim().isEmpty()) continue;
                String[] partes = linha.split(";");
                listaInsc.addLast(new Inscricao(partes[0], Integer.parseInt(partes[1]), Integer.parseInt(partes[2])));
            }
        }

        // Remove da lista todas as que baterem com o código da disciplina apagada
        // Varremos de trás para frente para não quebrar os índices ao remover itens
        for (int i = listaInsc.size() - 1; i >= 0; i--) {
            if (listaInsc.get(i).codigoDisciplina == codigoDisciplinaAlvo) {
                listaInsc.remove(i);
            }
        }

        // Salva o arquivo de inscrições atualizado e limpo
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(caminhoInscricoes, false))) {
            for (int i = 0; i < listaInsc.size(); i++) {
                bw.write(listaInsc.get(i).toString());
                bw.newLine();
            }
        }
    }
    
    // CONSULTA AVANÇADA: Popula e retorna a Tabela Hash com os processos ativos
    public TabelaHash listarProcessosAbertos() throws Exception {
        TabelaHash tabelaHash = new TabelaHash();
        InscricaoController inscCtrl = new InscricaoController();
        
        // 1. Pega todas as inscrições ativas (da fila) e descobre os códigos das disciplinas
        Fila<model.Inscricao> inscricoes = inscCtrl.listarInscricoes();
        ListaEncadeada<Integer> codigosAtivos = new ListaEncadeada<>();
        
        while (!inscricoes.isEmpty()) {
            model.Inscricao insc = inscricoes.remove();
            
            // Verifica se o código da disciplina já foi adicionado para evitar duplicados
            boolean jaExiste = false;
            for (int i = 0; i < codigosAtivos.size(); i++) {
                if (codigosAtivos.get(i) == insc.codigoDisciplina) {
                    jaExiste = true;
                    break;
                }
            }
            if (!jaExiste) {
                codigosAtivos.addLast(insc.codigoDisciplina);
            }
        }

        // 2. Busca os dados completos de cada disciplina ativa e joga na Tabela Hash
        Fila<Disciplina> todasDisciplinas = listarDisciplinas();
        while (!todasDisciplinas.isEmpty()) {
            Disciplina disp = todasDisciplinas.remove();
            
            // Se o código da disciplina está na lista de ativos, insere na tabela hash
            for (int i = 0; i < codigosAtivos.size(); i++) {
                if (codigosAtivos.get(i) == disp.codigo) {
                    tabelaHash.put(disp);
                    break;
                }
            }
        }

        return tabelaHash;
    }
}