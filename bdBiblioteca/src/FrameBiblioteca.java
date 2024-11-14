import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;
import java.time.chrono.JapaneseDate;

public class FrameBiblioteca extends JFrame {
    private FormLogin formLogin;
    private FormLivros formLivros;
    private FormExemplares formExemplares;
    private FormEmprestimos formEmprestimos;
    private FormDevolucoes formDevolucoes;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                FrameBiblioteca form = new FrameBiblioteca();

                form.addWindowListener(
                        new WindowAdapter()
                        {
                            public void windowClosing (WindowEvent e)
                            {
                                try {
                                    FrameBiblioteca.FormLogin.conexaoDados.close();
                                } catch (SQLException ex) {
                                    throw new RuntimeException(ex);
                                }
                                System.exit(0);
                            }
                        }
                );

                form.pack();
//                form.setVisible(true);
            }
        });
    }

    public FrameBiblioteca() { // inicializa as coisas
        setTitle("Manutencao de Bibliotecas");
        setSize(1000, 300);
        // apenas chame o evento windowClosing
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        formLogin       = new FormLogin();
        formLivros      = new FormLivros();
        formExemplares  = new FormExemplares();
        formEmprestimos = new FormEmprestimos();
        formDevolucoes  = new FormDevolucoes();

        add(formLogin);
    }

    public class FormLogin extends JFrame {
        private static Connection conexaoDados = null;

        private JLabel labMensagem;
        private JTextField txtServidor, txtNomeBd, txtUsuario, txtSenha;
//        private JPasswordField pswfPassword;
        private JButton btnConectar;
        private JPanel panInputs, panSelectBiblioteca, panBtnConectar, panMensagem;
        private JComboBox<String> cbxBiblioteca;

        // toolbar que contém os botões de navegação entre os formulários
        private JToolBar tbBotoesNavegacao;
        // botões para abrir formulários
        private JButton btnFormLivros, btnFormExemplares, btnFormEmprestimos, btnFormDevolucoes;


        public FormLogin() {
            setTitle("Login");
            setSize(800, 600);

            tbBotoesNavegacao = new JToolBar();

            btnFormLivros = new JButton("Livros");
            btnFormLivros.setPreferredSize(new Dimension(65,45));
            btnFormLivros.setVerticalTextPosition(SwingConstants.BOTTOM);
            btnFormLivros.setHorizontalTextPosition(SwingConstants.CENTER);
            btnFormLivros.setFocusPainted(false);

            btnFormExemplares = new JButton("Exemplares");
            btnFormExemplares.setPreferredSize(new Dimension(65,45));
            btnFormExemplares.setVerticalTextPosition(SwingConstants.BOTTOM);
            btnFormExemplares.setHorizontalTextPosition(SwingConstants.CENTER);
            btnFormExemplares.setFocusPainted(false);

            btnFormEmprestimos = new JButton("Empréstimos");
            btnFormEmprestimos.setPreferredSize(new Dimension(65,45));
            btnFormEmprestimos.setVerticalTextPosition(SwingConstants.BOTTOM);
            btnFormEmprestimos.setHorizontalTextPosition(SwingConstants.CENTER);
            btnFormEmprestimos.setFocusPainted(false);

            btnFormDevolucoes = new JButton("Devoluções");
            btnFormDevolucoes.setPreferredSize(new Dimension(65,45));
            btnFormDevolucoes.setVerticalTextPosition(SwingConstants.BOTTOM);
            btnFormDevolucoes.setHorizontalTextPosition(SwingConstants.CENTER);
            btnFormDevolucoes.setFocusPainted(false);

            tbBotoesNavegacao.setLayout(new FlowLayout());
            tbBotoesNavegacao.add(btnFormLivros);
            tbBotoesNavegacao.add(btnFormExemplares);
            tbBotoesNavegacao.add(btnFormEmprestimos);
            tbBotoesNavegacao.add(btnFormDevolucoes);

            tbBotoesNavegacao.setRollover(true);

            panInputs = new JPanel();
            panInputs.setLayout(new GridLayout(4, 2));

            txtServidor = new JTextField();
            txtServidor.setPreferredSize(new Dimension(200, 50));
            txtNomeBd = new JTextField();
            txtNomeBd.setPreferredSize(new Dimension(200, 50));
            txtUsuario = new JTextField();
            txtUsuario.setPreferredSize(new Dimension(200, 50));
            txtSenha = new JTextField();
            txtSenha.setPreferredSize(new Dimension(200, 50));

            panInputs.add(new JLabel("Servidor:"));
            panInputs.add(txtServidor);
            panInputs.add(new JLabel("Banco de Dados:"));
            panInputs.add(txtNomeBd);
            panInputs.add(new JLabel("Usuário:"));
            panInputs.add(txtUsuario);
            panInputs.add(new JLabel("Senha:"));
            panInputs.add(txtSenha);

            panBtnConectar = new JPanel();
            btnConectar = new JButton();
            btnConectar.setPreferredSize(new Dimension(120, 50));
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
                                ConexaoBD conexaoBD = new ConexaoBD(txtNomeBd.getText(), txtUsuario.getText(), txtSenha.getText());
                                conexaoDados = conexaoBD.getConnection();
                                labMensagem.setText("Mensagem: conectado!");
                                cbxBiblioteca.setEnabled(true);
                            } catch (SQLException err) {
                                labMensagem.setText("Mensagem: Erro ao conectar ao BD");
                                throw new RuntimeException(err);
                            }
                        }
                    }
            );

            Container cntForm = getContentPane();
            cntForm.setLayout(new BorderLayout());

            cntForm.add(tbBotoesNavegacao, BorderLayout.NORTH);
            cntForm.add(panInputs, BorderLayout.WEST);
            cntForm.add(panBtnConectar, BorderLayout.CENTER);
            cntForm.add(panSelectBiblioteca, BorderLayout.EAST);
            cntForm.add(panMensagem, BorderLayout.SOUTH);

            setVisible(true);

        }
    }

    public class FormLivros extends JFrame {
        private int idBibliotecaEscolhida;

        public int getIdBibliotecaEscolhida() {
            return idBibliotecaEscolhida;
        }

        public void setIdBibliotecaEscolhida(int idBibliotecaEscolhida) {
            this.idBibliotecaEscolhida = idBibliotecaEscolhida;
        }
    }

    public class FormExemplares extends JFrame {
        private int idBibliotecaEscolhida;

        public int getIdBibliotecaEscolhida() {
            return idBibliotecaEscolhida;
        }

        public void setIdBibliotecaEscolhida(int idBibliotecaEscolhida) {
            this.idBibliotecaEscolhida = idBibliotecaEscolhida;
        }
    }

    public class FormEmprestimos extends JFrame {
        private int idBibliotecaEscolhida;

        public int getIdBibliotecaEscolhida() {
            return idBibliotecaEscolhida;
        }

        public void setIdBibliotecaEscolhida(int idBibliotecaEscolhida) {
            this.idBibliotecaEscolhida = idBibliotecaEscolhida;
        }
    }

    public class FormDevolucoes extends JFrame {
        private int idBibliotecaEscolhida;

        public int getIdBibliotecaEscolhida() {
            return idBibliotecaEscolhida;
        }

        public void setIdBibliotecaEscolhida(int idBibliotecaEscolhida) {
            this.idBibliotecaEscolhida = idBibliotecaEscolhida;
        }

    }
}
