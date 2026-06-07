package view;

import controller.CursoController;
import controller.Fila;
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
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import model.Curso;

public class TelaCurso extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField tfCodigo;
    private JTextField tfNome;
    private JTextField tfArea;
    private JTextArea taLista;
    
    // Instancia o controlador para fazer a ponte com os arquivos e estruturas
    private CursoController ctrl = new CursoController();

    public TelaCurso() {
        setTitle("Cadastro de Cursos — FATEC ZL");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Fecha apenas esta janela, mantém o menu aberto
        setBounds(100, 100, 500, 450);
        setLocationRelativeTo(null);
        
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblTitulo = new JLabel("Gerenciamento de Cursos");
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblTitulo.setBounds(150, 11, 200, 25);
        contentPane.add(lblTitulo);

        // --- RÓTULOS E CAMPOS DE TEXTO ---
        JLabel lblCodigo = new JLabel("Código:");
        lblCodigo.setBounds(30, 60, 60, 20);
        contentPane.add(lblCodigo);

        tfCodigo = new JTextField();
        tfCodigo.setBounds(100, 60, 100, 20);
        contentPane.add(tfCodigo);
        tfCodigo.setColumns(10);

        JLabel lblNome = new JLabel("Nome:");
        lblNome.setBounds(30, 95, 60, 20);
        contentPane.add(lblNome);

        tfNome = new JTextField();
        tfNome.setBounds(100, 95, 340, 20);
        contentPane.add(tfNome);
        tfNome.setColumns(10);

        JLabel lblArea = new JLabel("Área:");
        lblArea.setBounds(30, 130, 60, 20);
        contentPane.add(lblArea);

        tfArea = new JTextField();
        tfArea.setBounds(100, 130, 340, 20);
        contentPane.add(tfArea);
        tfArea.setColumns(10);

        // --- ÁREA DE LISTAGEM COM ROLAGEM ---
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(30, 220, 410, 160);
        contentPane.add(scrollPane);

        taLista = new JTextArea();
        taLista.setEditable(false);
        scrollPane.setViewportView(taLista);

        // --- BOTÃO CADASTRAR ---
        JButton btnCadastrar = new JButton("Cadastrar");
        btnCadastrar.setBounds(30, 175, 95, 30);
        contentPane.add(btnCadastrar);
        btnCadastrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    Curso c = new Curso();
                    c.codigo = Integer.parseInt(tfCodigo.getText());
                    c.nome = tfNome.getText();
                    c.areaConhecimento = tfArea.getText();
                    
                    ctrl.cadastrarCurso(c);
                    JOptionPane.showMessageDialog(null, "Curso cadastrado com sucesso!");
                    limparCampos();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao cadastrar: " + ex.getMessage());
                }
            }
        });

        // --- BOTÃO LISTAR (Usa Fila do Backend) ---
        JButton btnListar = new JButton("Listar");
        btnListar.setBounds(135, 175, 95, 30);
        contentPane.add(btnListar);
        btnListar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    taLista.setText(""); // Limpa a área de texto
                    Fila<Curso> fila = ctrl.listarCursos();
                    
                    if (fila.isEmpty()) {
                        taLista.setText("Nenhum curso registrado.");
                        return;
                    }
                    
                    // Esvazia a fila jogando os dados formatados na tela
                    while (!fila.isEmpty()) {
                        Curso c = fila.remove();
                        taLista.append("Código: " + c.codigo + " | Nome: " + c.nome + " | Área: " + c.areaConhecimento + "\n");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao listar: " + ex.getMessage());
                }
            }
        });

        // --- BOTÃO ATUALIZAR (Usa Lista Encadeada do Backend) ---
        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.setBounds(240, 175, 95, 30);
        contentPane.add(btnAtualizar);
        btnAtualizar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int codigoAlvo = Integer.parseInt(tfCodigo.getText());
                    Curso novoCurso = new Curso();
                    novoCurso.nome = tfNome.getText();
                    novoCurso.areaConhecimento = tfArea.getText();
                    
                    ctrl.removerOuAtualizarCurso(codigoAlvo, novoCurso, false);
                    JOptionPane.showMessageDialog(null, "Curso atualizado com sucesso!");
                    limparCampos();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao atualizar: " + ex.getMessage());
                }
            }
        });

        // --- BOTÃO REMOVER (Usa Lista Encadeada do Backend) ---
        JButton btnRemover = new JButton("Remover");
        btnRemover.setBounds(345, 175, 95, 30);
        contentPane.add(btnRemover);
        btnRemover.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int codigoAlvo = Integer.parseInt(tfCodigo.getText());
                    
                    ctrl.removerOuAtualizarCurso(codigoAlvo, null, true);
                    JOptionPane.showMessageDialog(null, "Curso removido com sucesso!");
                    limparCampos();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao remover: " + ex.getMessage());
                }
            }
        });
    }

    private void limparCampos() {
        tfCodigo.setText("");
        tfNome.setText("");
        tfArea.setText("");
    }
}