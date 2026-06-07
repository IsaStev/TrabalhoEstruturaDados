package view;

import controller.InscricaoController;
import controller.ListaEncadeada;
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

public class TelaConsultaInscritos extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField tfCodigoDisciplina;
    private JTextArea taResultado;

    private InscricaoController ctrl = new InscricaoController();

    public TelaConsultaInscritos() {
        setTitle("Classificação de Candidatos — FATEC ZL");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 520, 400);
        setLocationRelativeTo(null);
        
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblTitulo = new JLabel("Consulta de Inscritos por Disciplina");
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblTitulo.setBounds(130, 11, 280, 25);
        contentPane.add(lblTitulo);

        JLabel lblCodigo = new JLabel("Código da Disciplina:");
        lblCodigo.setBounds(40, 65, 140, 20);
        contentPane.add(lblCodigo);

        tfCodigoDisciplina = new JTextField();
        tfCodigoDisciplina.setBounds(180, 65, 100, 20);
        contentPane.add(tfCodigoDisciplina);

        // --- ÁREA PARA EXIBIR A CLASSIFICAÇÃO ORDENADA ---
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(40, 110, 430, 220);
        contentPane.add(scrollPane);

        taResultado = new JTextArea();
        taResultado.setEditable(false);
        scrollPane.setViewportView(taResultado);

        // --- BOTÃO CONSULTAR (Chama a Ordenação Manual do Backend) ---
        JButton btnConsultar = new JButton("Consultar Ranking");
        btnConsultar.setBounds(300, 60, 170, 30);
        contentPane.add(btnConsultar);
        btnConsultar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    taResultado.setText(""); // Limpa a tela
                    int codigoAlvo = Integer.parseInt(tfCodigoDisciplina.getText().trim());
                    
                    // Dispara a busca cruzada + Bubble Sort que estruturamos no controller
                    ListaEncadeada<Professor> listaOrdenada = ctrl.listarInscritosOrdenados(codigoAlvo);
                    
                    if (listaOrdenada.isEmpty()) {
                        taResultado.setText("Nenhum professor inscrito nesta disciplina.");
                        return;
                    }
                    
                    taResultado.append("=== CLASSIFICAÇÃO FINAL (ORDEM DE PONTUAÇÃO) ===\n\n");
                    
                    // Varre a lista encadeada já classificada
                    for (int i = 0; i < listaOrdenada.size(); i++) {
                        Professor p = listaOrdenada.get(i);
                        taResultado.append((i + 1) + "º Lugar - Nota: " + p.pontuacao + " | " + p.nome + " (CPF: " + p.cpf + ")\n");
                    }
                    
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao processar consulta: " + ex.getMessage());
                }
            }
        });
    }
}