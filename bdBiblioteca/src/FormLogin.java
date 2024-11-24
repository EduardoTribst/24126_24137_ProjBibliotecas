import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class FormLogin extends JFrame {
    private JLabel labMensagem;
    private JTextField txtServidor, txtNomeBd, txtUsuario;
    private JPasswordField pswfPassword;
    private JButton btnConectar;
    private JPanel panInputs, panSelectBiblioteca, panBtnConectar, panMensagem;
    private JComboBox<String> cbxBiblioteca;

    // dados
    private ResultSet dadosDoSelect;

    // toolbar que contém os botões de navegação entre os formulários
    private JToolBar tbBotoesNavegacao;
    // botões para abrir formulários
    private JButton btnFormLivros, btnFormExemplares, btnFormEmprestimos, btnFormDevolucoes;

    // id biblioteca escolhida
    private int idBibliotecaEscolhida;

    public int getIdBibliotecaEscolhida() {
        return idBibliotecaEscolhida;
    }

    public void setIdBibliotecaEscolhida(int idBibliotecaEscolhida) throws Exception {
        if (idBibliotecaEscolhida > 0) {
            this.idBibliotecaEscolhida = idBibliotecaEscolhida;
        }
        else {
            throw new Exception("O id deve ser maior que 0");
        }
    }

    // conexao e classe controladora
    private Connection conexaoDados;
    public FrameBiblioteca framePrincipal;

    public FormLogin(FrameBiblioteca controlador) {
        setTitle("Sistema de Biblioteca | Login");
        setSize(800, 500);

        // inicializa o frame principal
        framePrincipal = controlador;

        // inicializa tab de botoes de navegacao
        tbBotoesNavegacao = new JToolBar();

        btnFormLivros = new JButton("Livros");
        btnFormLivros.setPreferredSize(new Dimension(85,45));
        btnFormLivros.setVerticalTextPosition(SwingConstants.BOTTOM);
        btnFormLivros.setHorizontalTextPosition(SwingConstants.CENTER);
        btnFormLivros.setFocusPainted(false);

        btnFormExemplares = new JButton("Exemplares");
        btnFormExemplares.setPreferredSize(new Dimension(85,45));
        btnFormExemplares.setVerticalTextPosition(SwingConstants.BOTTOM);
        btnFormExemplares.setHorizontalTextPosition(SwingConstants.CENTER);
        btnFormExemplares.setFocusPainted(false);

        btnFormEmprestimos = new JButton("Empréstimos");
        btnFormEmprestimos.setPreferredSize(new Dimension(85,45));
        btnFormEmprestimos.setVerticalTextPosition(SwingConstants.BOTTOM);
        btnFormEmprestimos.setHorizontalTextPosition(SwingConstants.CENTER);
        btnFormEmprestimos.setFocusPainted(false);

        btnFormDevolucoes = new JButton("Devoluções");
        btnFormDevolucoes.setPreferredSize(new Dimension(85,45));
        btnFormDevolucoes.setVerticalTextPosition(SwingConstants.BOTTOM);
        btnFormDevolucoes.setHorizontalTextPosition(SwingConstants.CENTER);
        btnFormDevolucoes.setFocusPainted(false);

        tbBotoesNavegacao.setLayout(new FlowLayout());
        tbBotoesNavegacao.add(btnFormLivros);
        tbBotoesNavegacao.add(btnFormExemplares);
        tbBotoesNavegacao.add(btnFormEmprestimos);
        tbBotoesNavegacao.add(btnFormDevolucoes);

        tbBotoesNavegacao.setRollover(true);

        btnFormLivros.setEnabled(false);
        btnFormExemplares.setEnabled(false);
        btnFormEmprestimos.setEnabled(false);
        btnFormDevolucoes.setEnabled(false);


        panInputs = new JPanel();
        panInputs.setLayout(new GridLayout(4, 2));

        txtServidor = new JTextField();
        txtServidor.setPreferredSize(new Dimension(200, 50));
        txtNomeBd = new JTextField();
        txtNomeBd.setPreferredSize(new Dimension(200, 50));
        txtUsuario = new JTextField();
        txtUsuario.setPreferredSize(new Dimension(200, 50));
        pswfPassword = new JPasswordField();
        pswfPassword.setPreferredSize(new Dimension(200, 50));

        panInputs.add(new JLabel("Servidor:"));
        panInputs.add(txtServidor);
        panInputs.add(new JLabel("Banco de Dados:"));
        panInputs.add(txtNomeBd);
        panInputs.add(new JLabel("Usuário:"));
        panInputs.add(txtUsuario);
        panInputs.add(new JLabel("Senha:"));
        panInputs.add(pswfPassword);

        panBtnConectar = new JPanel();
        btnConectar = new JButton();
        btnConectar.setPreferredSize(new Dimension(100, 30));
        btnConectar.setText("Conectar");
        btnConectar.setVerticalTextPosition(SwingConstants.BOTTOM);
        btnConectar.setHorizontalTextPosition(SwingConstants.CENTER);
        panBtnConectar.setLayout(new FlowLayout());
        panBtnConectar.setAlignmentY(SwingConstants.CENTER);
        panBtnConectar.add(btnConectar);

        panSelectBiblioteca = new JPanel();
        panSelectBiblioteca.setLayout(new GridLayout(1, 2));
        cbxBiblioteca = new JComboBox<String>();
        panSelectBiblioteca.add(new JLabel("Biblioteca:"));
        panSelectBiblioteca.add(cbxBiblioteca);
        cbxBiblioteca.setEnabled(false);

        panMensagem = new JPanel();
        labMensagem = new JLabel("Mensagem: ");
        panMensagem.setLayout(new FlowLayout(FlowLayout.LEFT));
        panMensagem.add(labMensagem);

        btnConectar.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            conexaoDados = ConexaoBD.getConnection(txtServidor.getText(), txtNomeBd.getText(), txtUsuario.getText(), pswfPassword.getPassword());
                            labMensagem.setText("Mensagem: conectado!");
                            cbxBiblioteca.setEnabled(true);
                            preencherCbxBibliotecas();
                        } catch (SQLException err) {
                            labMensagem.setText("Mensagem: Erro ao conectar ao BD");
                            throw new RuntimeException(err);
                        }
                        framePrincipal.setConexaoDados(conexaoDados);
                    }
                }
        );

        btnFormLivros.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        setVisible(false);
                        try {
                            framePrincipal.exibirFormLivros();
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
        );

        btnFormExemplares.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        setVisible(false);
                        try {
                            framePrincipal.exibirFormExemplares();
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
        );

        btnFormEmprestimos.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        setVisible(false);
                        try {
                            framePrincipal.exibirFormEmprestimos();
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
        );

        btnFormDevolucoes.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        setVisible(false);
                        try {
                            framePrincipal.exibirFormDevolucoes();
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
        );

        Container cntForm = getContentPane();
        cntForm.setLayout(new BorderLayout());

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(panMensagem, BorderLayout.NORTH);
        southPanel.add(panBtnConectar, BorderLayout.CENTER);
        southPanel.add(panSelectBiblioteca, BorderLayout.SOUTH);

        JPanel westPanel = new JPanel(new BorderLayout());
        westPanel.add(panInputs, BorderLayout.NORTH);
        westPanel.add(southPanel, BorderLayout.SOUTH);

        cntForm.add(tbBotoesNavegacao, BorderLayout.NORTH);
        cntForm.add(westPanel, BorderLayout.WEST);

        setVisible(true);

    }

    private void preencherCbxBibliotecas() {
        String sql = "SELECT * FROM SisBib.biblioteca order by idBiblioteca";
        try {
            Statement comandoSQL = conexaoDados.createStatement(
                    ResultSet.TYPE_SCROLL_SENSITIVE,	// permite navegação
                    ResultSet.CONCUR_UPDATABLE        // ResultSet é atualizável
            );
            try {
                dadosDoSelect = comandoSQL.executeQuery(sql);
                if (dadosDoSelect.next()) {
                    cbxBiblioteca.addItem(dadosDoSelect.getString("nome"));
                    while (dadosDoSelect.next()) {
                        cbxBiblioteca.addItem(dadosDoSelect.getString("nome"));
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Registro não encontrado!");
                }
            }
            catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }

        // adiciona o event listener para a cbx
        cbxBiblioteca.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String sql = "SELECT idBiblioteca FROM SisBib.biblioteca where nome = '" + cbxBiblioteca.getSelectedItem() + "'";
                        try {
                            Statement comandoSQL = conexaoDados.createStatement();
                            try {
                                dadosDoSelect = comandoSQL.executeQuery(sql);
                                if (dadosDoSelect.next()) {
                                    idBibliotecaEscolhida = dadosDoSelect.getInt("idBiblioteca");
                                    framePrincipal.setBibliotecaEscolhida(idBibliotecaEscolhida);
                                } else {
                                    JOptionPane.showMessageDialog(null, "Registro não encontrado!");
                                }
                            }
                            catch (SQLException ex) {
                                ex.printStackTrace();
                            } catch (Exception ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                        catch (SQLException ex) {
                            ex.printStackTrace();
                        }

                        btnFormLivros.setEnabled(true);
                        btnFormExemplares.setEnabled(true);
                        btnFormEmprestimos.setEnabled(true);
                        btnFormDevolucoes.setEnabled(true);
                    }
                }
        );
    }
}