package view;

import controller.Fila;
import controller.ProfessorController;
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
import model.Professor;

public class TelaProfessor extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField tfCpf;
    private JTextField tfNome;
    private JTextField tfArea;
    private JTextField tfPontuacao;
    private JTextArea taLista;

    private ProfessorController ctrl = new ProfessorController();

    public TelaProfessor() {
        setTitle("Cadastro de Professores — FATEC ZL");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 500, 450);
        setLocationRelativeTo(null);
        
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblTitulo = new JLabel("Gerenciamento de Professores");
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblTitulo.setBounds(140, 11, 250, 25);
        contentPane.add(lblTitulo);

        // --- CAMPOS DE TEXTO ---
        JLabel lblCpf = new JLabel("CPF (Apenas números):");
        lblCpf.setBounds(30, 60, 140, 20);
        contentPane.add(lblCpf);

        tfCpf = new JTextField();
        tfCpf.setBounds(180, 60, 150, 20);
        contentPane.add(tfCpf);

        JLabel lblNome = new JLabel("Nome Completo:");
        lblNome.setBounds(30, 95, 140, 20);
        contentPane.add(lblNome);

        tfNome = new JTextField();
        tfNome.setBounds(180, 95, 280, 20);
        contentPane.add(tfNome);

        JLabel lblArea = new JLabel("Área de Interesse:");
        lblArea.setBounds(30, 130, 140, 20);
        contentPane.add(lblArea);

        tfArea = new JTextField();
        tfArea.setBounds(180, 130, 280, 20);
        contentPane.add(tfArea);

        JLabel lblPontuacao = new JLabel("Pontuação / Títulos:");
        lblPontuacao.setBounds(30, 165, 140, 20);
        contentPane.add(lblPontuacao);

        tfPontuacao = new JTextField();
        tfPontuacao.setBounds(180, 165, 100, 20);
        contentPane.add(tfPontuacao);

        // --- ÁREA DE LISTAGEM ---
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(30, 260, 430, 130);
        contentPane.add(scrollPane);

        taLista = new JTextArea();
        taLista.setEditable(false);
        scrollPane.setViewportView(taLista);

        // --- BOTÃO CADASTRAR ---
        JButton btnCadastrar = new JButton("Cadastrar");
        btnCadastrar.setBounds(30, 210, 95, 30);
        contentPane.add(btnCadastrar);
        btnCadastrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    Professor p = new Professor();
                    p.cpf = tfCpf.getText().trim();
                    p.nome = tfNome.getText().trim();
                    p.area = tfArea.getText().trim();
                    p.pontuacao = Integer.parseInt(tfPontuacao.getText().trim());
                    
                    ctrl.cadastrarProfessor(p);
                    JOptionPane.showMessageDialog(null, "Professor cadastrado com sucesso!");
                    limparCampos();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Erro: " + ex.getMessage());
                }
            }
        });

        // --- BOTÃO LISTAR (Usa Fila) ---
        JButton btnListar = new JButton("Listar");
        btnListar.setBounds(135, 210, 95, 30);
        contentPane.add(btnListar);
        btnListar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    taLista.setText("");
                    Fila<Professor> fila = ctrl.listarProfessores();
                    if (fila.isEmpty()) {
                        taLista.setText("Nenhum professor registrado.");
                        return;
                    }
                    while (!fila.isEmpty()) {
                        Professor p = fila.remove();
                        taLista.append("CPF: " + p.cpf + " | " + p.nome + " | Área: " + p.area + " | Pontos: " + p.pontuacao + "\n");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Erro: " + ex.getMessage());
                }
            }
        });

        // --- BOTÃO ATUALIZAR (Busca por CPF) ---
        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.setBounds(240, 210, 95, 30);
        contentPane.add(btnAtualizar);
        btnAtualizar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String cpfAlvo = tfCpf.getText().trim();
                    Professor p = new Professor();
                    p.nome = tfNome.getText().trim();
                    p.area = tfArea.getText().trim();
                    p.pontuacao = Integer.parseInt(tfPontuacao.getText().trim());
                    
                    ctrl.removerOuAtualizarProfessor(cpfAlvo, p, false);
                    JOptionPane.showMessageDialog(null, "Dados do professor atualizados!");
                    limparCampos();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Erro: " + ex.getMessage());
                }
            }
        });

        // --- BOTÃO REMOVER (Busca por CPF) ---
        JButton btnRemover = new JButton("Remover");
        btnRemover.setBounds(345, 210, 95, 30);
        contentPane.add(btnRemover);
        btnRemover.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String cpfAlvo = tfCpf.getText().trim();
                    ctrl.removerOuAtualizarProfessor(cpfAlvo, null, true);
                    JOptionPane.showMessageDialog(null, "Professor removido do sistema!");
                    limparCampos();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Erro: " + ex.getMessage());
                }
            }
        });
    }

    private void limparCampos() {
        tfCpf.setText("");
        tfNome.setText("");
        tfArea.setText("");
        tfPontuacao.setText("");
    }
}