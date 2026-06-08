package view;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class TelaMenu extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    // Método MAIN
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    TelaMenu frame = new TelaMenu();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public TelaMenu() {
        setTitle("Sistema de Inscrição FATEC ZL");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 350);
        setLocationRelativeTo(null); // Centraliza a tela no monitor
        
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblTitulo = new JLabel("Gerenciamento de Processos Seletivos");
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblTitulo.setBounds(10, 11, 414, 25);
        contentPane.add(lblTitulo);

        // --- BOTÕES DOS CRUDS ---

        JButton btnCursos = new JButton("Gerenciar Cursos");
        btnCursos.setBounds(36, 79, 185, 35);
        contentPane.add(btnCursos);
        btnCursos.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Abre a tela de Cursos (as telas precisam ser JFrame)
                TelaCurso tela = new TelaCurso();
                tela.setVisible(true);
            }
        });

        JButton btnDisciplinas = new JButton("Gerenciar Disciplinas");
        btnDisciplinas.setBounds(36, 129, 185, 35);
        contentPane.add(btnDisciplinas);
        btnDisciplinas.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                TelaDisciplina tela = new TelaDisciplina();
                tela.setVisible(true);
            }
        });

        JButton btnProfessores = new JButton("Gerenciar Professores");
        btnProfessores.setBounds(36, 179, 185, 35);
        contentPane.add(btnProfessores);
        btnProfessores.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                TelaProfessor tela = new TelaProfessor();
                tela.setVisible(true);
            }
        });

        JButton btnInscricoes = new JButton("Gerenciar Inscrições");
        btnInscricoes.setBounds(36, 229, 185, 35);
        contentPane.add(btnInscricoes);
        btnInscricoes.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                TelaInscricao tela = new TelaInscricao();
                tela.setVisible(true);
            }
        });

        // --- BOTÕES DAS CONSULTAS AVANÇADAS ---

        JButton btnConsInscritos = new JButton("Consulta Inscritos");
        btnConsInscritos.setBounds(253, 104, 150, 45);
        contentPane.add(btnConsInscritos);
        btnConsInscritos.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                TelaConsultaInscritos tela = new TelaConsultaInscritos();
                tela.setVisible(true);
            }
        });

        JButton btnConsProcessos = new JButton("Processos Abertos");
        btnConsProcessos.setBounds(253, 179, 150, 45);
        contentPane.add(btnConsProcessos);
        btnConsProcessos.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                TelaConsultaProcessosAbertos tela = new TelaConsultaProcessosAbertos();
                tela.setVisible(true);
            }
        });
    }
}