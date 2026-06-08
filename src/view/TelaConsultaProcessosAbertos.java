package view;

import controller.CursoController;
import controller.DisciplinaController;
import controller.Fila;
import controller.ListaEncadeada;
import controller.TabelaHash;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import model.Curso;
import model.Disciplina;

public class TelaConsultaProcessosAbertos extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextArea taResultado;

    private DisciplinaController ctrl = new DisciplinaController();

    public TelaConsultaProcessosAbertos() {
        setTitle("Processos Seletivos Ativos — FATEC ZL");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 520, 400);
        setLocationRelativeTo(null);
        
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblTitulo = new JLabel("Disciplinas com Processos Abertos");
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblTitulo.setBounds(120, 11, 280, 25);
        contentPane.add(lblTitulo);

        JLabel lblDescricao = new JLabel("Clique abaixo para carregar a Tabela Hash de processos ativos:");
        lblDescricao.setBounds(30, 50, 440, 20);
        contentPane.add(lblDescricao);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(30, 120, 440, 210);
        contentPane.add(scrollPane);

        taResultado = new JTextArea();
        taResultado.setEditable(false);
        taResultado.setFont(new Font("Monospaced", Font.PLAIN, 12));
        scrollPane.setViewportView(taResultado);

        JButton btnCarregar = new JButton("Carregar via Tabela Hash");
        btnCarregar.setBounds(140, 80, 220, 30);
        contentPane.add(btnCarregar);
        btnCarregar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    taResultado.setText(""); 
                    
                    TabelaHash tabela = ctrl.listarProcessosAbertos();
                    ListaEncadeada<Disciplina> listaAtivos = tabela.getAll();
                    
                    if (listaAtivos.isEmpty()) {
                        taResultado.setText("Não há processos seletivos abertos no momento.");
                        return;
                    }
                    
                    taResultado.append("Disciplinas identificadas no sistema:\n");
                    taResultado.append("--------------------------------------------------\n");
                    
                    CursoController cursoCtrl = new CursoController();
                    
                    for (int i = 0; i < listaAtivos.size(); i++) {
                        Disciplina d = listaAtivos.get(i);
                        
                        // Busca o nome do curso correspondente ao código da disciplina
                        String nomeCurso = "Não encontrado";
                        Fila<Curso> cursos = cursoCtrl.listarCursos();
                        while (!cursos.isEmpty()) {
                            Curso c = cursos.remove();
                            if (c.getCodigo() == d.getCodigoCurso()) {
                                nomeCurso = c.getNome();
                                break;
                            }
                        }
                        
                        // Exibe o nome do curso por extenso na listagem
                        taResultado.append("ID: " + d.getCodigo() + " | " + d.getNome() + " (Curso: " + nomeCurso + ")\n");
                    }
                    
                    taResultado.append("--------------------------------------------------\n");
                    taResultado.append("Consulta processada com sucesso via Hash.");
                    
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Erro na Tabela Hash: " + ex.getMessage());
                }
            }
        });
    }
}