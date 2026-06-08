package view;

import controller.DisciplinaController;
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
import model.Disciplina;

public class TelaDisciplina extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField tfCodigo;
    private JTextField tfNome;
    private JTextField tfDiaSemana;
    private JTextField tfHorario;
    private JTextField tfHorasDiarias;
    private JTextField tfCodigoCurso;
    private JTextArea taLista;

    private DisciplinaController ctrl = new DisciplinaController();

    public TelaDisciplina() {
        setTitle("Cadastro de Disciplinas — FATEC ZL");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 520, 500);
        setLocationRelativeTo(null);
        
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblTitulo = new JLabel("Gerenciamento de Disciplinas");
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblTitulo.setBounds(150, 11, 250, 25);
        contentPane.add(lblTitulo);

        JLabel lblCodigo = new JLabel("Código:");
        lblCodigo.setBounds(30, 55, 100, 20);
        contentPane.add(lblCodigo);

        tfCodigo = new JTextField();
        tfCodigo.setBounds(140, 55, 100, 20);
        contentPane.add(tfCodigo);

        JLabel lblNome = new JLabel("Nome:");
        lblNome.setBounds(30, 90, 100, 20);
        contentPane.add(lblNome);

        tfNome = new JTextField();
        tfNome.setBounds(140, 90, 320, 20);
        contentPane.add(tfNome);

        JLabel lblDia = new JLabel("Dia da Semana:");
        lblDia.setBounds(30, 125, 100, 20);
        contentPane.add(lblDia);

        tfDiaSemana = new JTextField();
        tfDiaSemana.setBounds(140, 125, 120, 20);
        contentPane.add(tfDiaSemana);

        JLabel lblHorario = new JLabel("Horário Inicial:");
        lblHorario.setBounds(280, 125, 90, 20);
        contentPane.add(lblHorario);

        tfHorario = new JTextField();
        tfHorario.setBounds(370, 125, 90, 20);
        contentPane.add(tfHorario);

        JLabel lblHoras = new JLabel("Horas Diárias:");
        lblHoras.setBounds(30, 160, 100, 20);
        contentPane.add(lblHoras);

        tfHorasDiarias = new JTextField();
        tfHorasDiarias.setBounds(140, 160, 100, 20);
        contentPane.add(tfHorasDiarias);

        JLabel lblCurso = new JLabel("Código do Curso:");
        lblCurso.setBounds(260, 160, 110, 20);
        contentPane.add(lblCurso);

        tfCodigoCurso = new JTextField();
        tfCodigoCurso.setBounds(370, 160, 90, 20);
        contentPane.add(tfCodigoCurso);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(30, 260, 460, 170);
        contentPane.add(scrollPane);

        taLista = new JTextArea();
        taLista.setEditable(false);
        scrollPane.setViewportView(taLista);

        JButton btnCadastrar = new JButton("Cadastrar");
        btnCadastrar.setBounds(30, 210, 100, 30);
        contentPane.add(btnCadastrar);
        btnCadastrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    Disciplina d = new Disciplina();
                    d.setCodigo(Integer.parseInt(tfCodigo.getText().trim()));
                    d.setNome(tfNome.getText().trim());
                    d.setDiaSemana(tfDiaSemana.getText().trim());
                    d.setHorarioInicial(tfHorario.getText().trim());
                    d.setHorasDiarias(Integer.parseInt(tfHorasDiarias.getText().trim()));
                    d.setCodigoCurso(Integer.parseInt(tfCodigoCurso.getText().trim()));
                    
                    ctrl.cadastrarDisciplina(d);
                    JOptionPane.showMessageDialog(null, "Disciplina cadastrada com sucesso!");
                    limparCampos();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Atenção: Os campos numéricos não podem ficar vazios e devem conter apenas números inteiros.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            }
        });

        JButton btnListar = new JButton("Listar");
        btnListar.setBounds(150, 210, 100, 30);
        contentPane.add(btnListar);
        btnListar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    taLista.setText("");
                    Fila<Disciplina> fila = ctrl.listarDisciplinas();
                    if (fila.isEmpty()) {
                        taLista.setText("Nenhuma disciplina registrada.");
                        return;
                    }
                    while (!fila.isEmpty()) {
                        Disciplina d = fila.remove();
                        taLista.append("Cod: " + d.getCodigo() + " | " + d.getNome() + " | " + d.getDiaSemana() + " | " + d.getHorarioInicial() + " | " + d.getHorasDiarias() + "h | Curso: " + d.getCodigoCurso() + "\n");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao listar: " + ex.getMessage());
                }
            }
        });

        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.setBounds(270, 210, 100, 30);
        contentPane.add(btnAtualizar);
        btnAtualizar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int codigoAlvo = Integer.parseInt(tfCodigo.getText().trim());
                    Disciplina d = new Disciplina();
                    d.setNome(tfNome.getText().trim());
                    d.setDiaSemana(tfDiaSemana.getText().trim());
                    d.setHorarioInicial(tfHorario.getText().trim());
                    d.setHorasDiarias(Integer.parseInt(tfHorasDiarias.getText().trim()));
                    d.setCodigoCurso(Integer.parseInt(tfCodigoCurso.getText().trim()));
                    
                    ctrl.removerOuAtualizarDisciplina(codigoAlvo, d, false);
                    JOptionPane.showMessageDialog(null, "Disciplina atualizada com sucesso!");
                    limparCampos();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Atenção: Os campos numéricos não podem ficar vazios e devem conter apenas números inteiros.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            }
        });

        JButton btnRemover = new JButton("Remover");
        btnRemover.setBounds(390, 210, 100, 30);
        contentPane.add(btnRemover);
        btnRemover.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int codigoAlvo = Integer.parseInt(tfCodigo.getText().trim());
                    ctrl.removerOuAtualizarDisciplina(codigoAlvo, null, true);
                    JOptionPane.showMessageDialog(null, "Disciplina removida (inscrições limpas em cascata)!");
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
        tfCodigo.setText("");
        tfNome.setText("");
        tfDiaSemana.setText("");
        tfHorario.setText("");
        tfHorasDiarias.setText("");
        tfCodigoCurso.setText("");
    }
}