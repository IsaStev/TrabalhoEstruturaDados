package view;

import controller.Fila;
import controller.InscricaoController;
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
import model.Inscricao;

public class TelaInscricao extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField tfCpfProfessor;
    private JTextField tfCodigoDisciplina;
    private JTextField tfCodigoProcesso;
    private JTextArea taLista;

    private InscricaoController ctrl = new InscricaoController();

    public TelaInscricao() {
        setTitle("Gerenciamento de Inscrições — FATEC ZL");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 500, 450);
        setLocationRelativeTo(null);
        
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblTitulo = new JLabel("Inscrição em Processos Seletivos");
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblTitulo.setBounds(130, 11, 260, 25);
        contentPane.add(lblTitulo);

        JLabel lblCpf = new JLabel("CPF do Professor:");
        lblCpf.setBounds(30, 60, 130, 20);
        contentPane.add(lblCpf);

        tfCpfProfessor = new JTextField();
        tfCpfProfessor.setBounds(180, 60, 160, 20);
        contentPane.add(tfCpfProfessor);

        JLabel lblDisciplina = new JLabel("Código da Disciplina:");
        lblDisciplina.setBounds(30, 95, 130, 20);
        contentPane.add(lblDisciplina);

        tfCodigoDisciplina = new JTextField();
        tfCodigoDisciplina.setBounds(180, 95, 100, 20);
        contentPane.add(tfCodigoDisciplina);

        JLabel lblProcesso = new JLabel("Código do Processo:");
        lblProcesso.setBounds(30, 130, 130, 20);
        contentPane.add(lblProcesso);

        tfCodigoProcesso = new JTextField();
        tfCodigoProcesso.setBounds(180, 130, 100, 20);
        contentPane.add(tfCodigoProcesso);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(30, 240, 430, 150);
        contentPane.add(scrollPane);

        taLista = new JTextArea();
        taLista.setEditable(false);
        scrollPane.setViewportView(taLista);

        JButton btnInscrever = new JButton("Inscrever");
        btnInscrever.setBounds(30, 185, 95, 30);
        contentPane.add(btnInscrever);
        btnInscrever.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    Inscricao insc = new Inscricao();
                    insc.setCpfProfessor(tfCpfProfessor.getText().trim());
                    insc.setCodigoDisciplina(Integer.parseInt(tfCodigoDisciplina.getText().trim()));
                    insc.setCodigoProcesso(Integer.parseInt(tfCodigoProcesso.getText().trim()));
                    
                    ctrl.cadastrarInscricao(insc);
                    JOptionPane.showMessageDialog(null, "Professor inscrito com sucesso no processo!");
                    limparCampos();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Atenção: Os campos numéricos não podem ficar vazios e devem conter apenas números inteiros.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            }
        });

        JButton btnListar = new JButton("Listar");
        btnListar.setBounds(135, 185, 95, 30);
        contentPane.add(btnListar);
        btnListar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    taLista.setText("");
                    Fila<Inscricao> fila = ctrl.listarInscricoes();
                    if (fila.isEmpty()) {
                        taLista.setText("Nenhuma inscrição realizada.");
                        return;
                    }
                    while (!fila.isEmpty()) {
                        Inscricao insc = fila.remove();
                        taLista.append("Processo: " + insc.getCodigoProcesso() + " | Disciplina: " + insc.getCodigoDisciplina() + " | CPF: " + insc.getCpfProfessor() + "\n");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao listar: " + ex.getMessage());
                }
            }
        });

        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.setBounds(240, 185, 95, 30);
        contentPane.add(btnAtualizar);
        btnAtualizar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String cpfAlvo = tfCpfProfessor.getText().trim();
                    int dispAlvo = Integer.parseInt(tfCodigoDisciplina.getText().trim());
                    
                    Inscricao insc = new Inscricao();
                    insc.setCodigoProcesso(Integer.parseInt(tfCodigoProcesso.getText().trim()));
                    
                    ctrl.removerOuAtualizarInscricao(cpfAlvo, dispAlvo, insc, false);
                    JOptionPane.showMessageDialog(null, "Inscrição atualizada!");
                    limparCampos();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Atenção: Os campos numéricos não podem ficar vazios e devem conter apenas números inteiros.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            }
        });

        JButton btnRemover = new JButton("Remover");
        btnRemover.setBounds(345, 185, 95, 30);
        contentPane.add(btnRemover);
        btnRemover.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String cpfAlvo = tfCpfProfessor.getText().trim();
                    if(cpfAlvo.isEmpty()) throw new Exception("Informe o CPF e a Disciplina para remover.");
                    int dispAlvo = Integer.parseInt(tfCodigoDisciplina.getText().trim());
                    
                    ctrl.removerOuAtualizarInscricao(cpfAlvo, dispAlvo, null, true);
                    JOptionPane.showMessageDialog(null, "Inscrição removida!");
                    limparCampos();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Atenção: Os campos numéricos não podem ficar vazios e devem conter apenas números inteiros.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            }
        });
    }

    private void limparCampos() {
        tfCpfProfessor.setText("");
        tfCodigoDisciplina.setText("");
        tfCodigoProcesso.setText("");
    }
}