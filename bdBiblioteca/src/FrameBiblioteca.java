import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class FrameBiblioteca extends JFrame {
    public static Connection conexaoDados = null;

    public static FrameBiblioteca form;

    private FormLogin formLogin;
    private FormLivros formLivros;
    private FormExemplares formExemplares;
    private FormEmprestimos formEmprestimos;
    private FormDevolucoes formDevolucoes;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                form = new FrameBiblioteca();

                form.addWindowListener(
                        new WindowAdapter()
                        {
                            public void windowClosing (WindowEvent e)
                            {
                                try {
                                    conexaoDados.close();
                                } catch (SQLException ex) {
                                    throw new RuntimeException(ex);
                                }
                                System.exit(0);
                            }
                        }
                );

                form.pack();
                form.setVisible(false);
            }
        });
    }

    public FrameBiblioteca() { // inicializa as coisas
        setTitle("Manutencao de Bibliotecas");
        setSize(1000, 500);
        // apenas chame o evento windowClosing
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        // inicializa os forms

        formLogin       = new FormLogin();
        formLivros      = new FormLivros();
        formExemplares  = new FormExemplares();
        formEmprestimos = new FormEmprestimos();
        formDevolucoes  = new FormDevolucoes();
    }

    public class FormLogin extends JFrame {
        private JLabel labMensagem;
        private JTextField txtServidor, txtNomeBd, txtUsuario;
        private JPasswordField pswfPassword;
        private JButton btnConectar;
        private JPanel panInputs, panSelectBiblioteca, panBtnConectar, panMensagem;
        private JComboBox<String> cbxBiblioteca;

        // dados
        private static ResultSet dadosDoSelect;

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

        public FormLogin() {
            setTitle("Sistema de Biblioteca | Login");
            setSize(800, 400);

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
                        }
                    }
            );

            btnFormLivros.addActionListener(
                    new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            setVisible(false);
                            try {
                                formLivros.setVisible(true);
                                formLivros.setIdBibliotecaEscolhida(idBibliotecaEscolhida);
                                formLivros.preencherDados();
                                formLivros.exibirRegistro();
                                formLivros.preencherTabela();
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
                                formExemplares.setVisible(true);
                                formExemplares.setIdBibliotecaEscolhida(idBibliotecaEscolhida);
                                formExemplares.preencherDados();
                                formExemplares.exibirRegistro();
                                formExemplares.preencherTabela();
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
                                formEmprestimos.setVisible(true);
                                formEmprestimos.setIdBibliotecaEscolhida(idBibliotecaEscolhida);
                                formEmprestimos.preencherDadosEmprestimo();
                                formEmprestimos.preencherDadosAtrasados();
                                formEmprestimos.exibirRegistroEmprestimos();
                                formEmprestimos.preencherTabelaEmprestimo();
                                formEmprestimos.preencherTabelaAtrasados();
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
                                formDevolucoes.setVisible(true);
                                formDevolucoes.setIdBibliotecaEscolhida(idBibliotecaEscolhida);
                                // preenche e mostra dados
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

                            btnFormLivros.setEnabled(true);
                            btnFormExemplares.setEnabled(true);
                            btnFormEmprestimos.setEnabled(true);
                            btnFormDevolucoes.setEnabled(true);
                        }
                    }
            );
        }
    }

    public class FormLivros extends JFrame {
        private static ResultSet dadosDoSelect;   // tabela resultante de um select no BD, PARA NAVEGAÇÃO

        private static JTextField txtCodLivro, txtTitulo, txtIdAutor, txtIdArea, txtISBN;

        private static JTable tabLivro;	// controle que exibe dados em formato tabular (linhas e colunas)

        private static DefaultTableModel modelo;
        private static String[] colunas;
        private static String[][] linhas;
        private static int quantasLinhas = 0;

        // acoes crud
        private JToolBar tbBotoes; // armazenará os botões abaixo; será colocado no topo do formulári
        private JButton btnIncluir, btnSalvar, btnExcluir, btnBuscar, btnProximo, btnAnterior, btnInicio,
                btnFinal, btnCancelar;

        // botões para abrir formulários
        private JButton btnFormExemplares, btnFormEmprestimos, btnFormDevolucoes;

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

        static private void exibirRegistro() throws SQLException
        {
            if (!dadosDoSelect.rowDeleted())
            {
                txtCodLivro.setText(dadosDoSelect.getString("codLivro"));
                txtTitulo.setText(dadosDoSelect.getString("titulo"));
                txtIdAutor.setText(String.valueOf(dadosDoSelect.getInt("idAutor")));
                txtIdArea.setText(String.valueOf(dadosDoSelect.getInt("idArea")));
                txtISBN.setText(dadosDoSelect.getString("ISBN"));
            }
        }

        private static void preencherDados() {
            String sql = "SELECT * FROM SisBib.Livro order by codLivro";
            try {
                Statement comandoSQL = conexaoDados.createStatement(
                        ResultSet.TYPE_SCROLL_SENSITIVE,	// permite navegação
                        ResultSet.CONCUR_UPDATABLE        // ResultSet é atualizável
                );
                try {
                    dadosDoSelect = comandoSQL.executeQuery(sql);
                    if (dadosDoSelect.next()) {
                        exibirRegistro();
                        preencherTabela();
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
        }

        public static void preencherTabela() throws SQLException {
            colunas = new String[]{"codigo Livro","titulo","id Autor","id Area"/*, "IBSN"*/};
            dadosDoSelect.last();
            int totalLinhas = dadosDoSelect.getRow();
            dadosDoSelect.beforeFirst();
            System.out.println(totalLinhas);

            linhas = new String[totalLinhas][5];

//            linhas[0][0] = "codLivro";
//            linhas[0][1] = "Titulo";
//            linhas[0][2] = "Autor";
//            linhas[0][3] = "Area";
//            linhas[0][4] = "ISBN";


            for (int i = 0; i<totalLinhas; i++) {
                dadosDoSelect.next();
                linhas[i][0] = dadosDoSelect.getString(1);
                linhas[i][1] = dadosDoSelect.getString(2);
                linhas[i][2] = dadosDoSelect.getString(3);
                linhas[i][3] = dadosDoSelect.getString(4);
//                linhas[i][4] = dadosDoSelect.getString(5);
            }

            modelo = new DefaultTableModel(linhas, colunas);

            System.out.println("Rows: " + modelo.getRowCount());
            System.out.println("Columns: " + modelo.getColumnCount());
        }


        public FormLivros() {
            setTitle("Sistema de Biblioteca | Livros");
            setSize(1000, 300);

            // Adiciorenamos os botões ao JToolBar que os conterá
            tbBotoes = new JToolBar();  // orientação padrão é HORIZONTAL

            btnInicio = new JButton("Inicio", new ImageIcon(getClass().getResource("/resources/first.png")));
            btnInicio.setPreferredSize(new Dimension(65,45));
            btnInicio.setVerticalTextPosition(SwingConstants.BOTTOM);
            btnInicio.setHorizontalTextPosition(SwingConstants.CENTER);
            btnInicio.setFocusPainted(false);       //remove uma borda que fica dentro do último botão pressionado

            btnAnterior = new JButton("Voltar", new ImageIcon(getClass().getResource("/resources/prior.png")));
            btnAnterior.setPreferredSize(new Dimension(65,45));
            btnAnterior.setVerticalTextPosition(SwingConstants.BOTTOM);
            btnAnterior.setHorizontalTextPosition(SwingConstants.CENTER);
            btnAnterior.setFocusPainted(false);

            btnProximo = new JButton("Avancar", new ImageIcon(getClass().getResource("/resources/next.png")));
            btnProximo.setPreferredSize(new Dimension(65,45));
            btnProximo.setVerticalTextPosition(SwingConstants.BOTTOM);
            btnProximo.setHorizontalTextPosition(SwingConstants.CENTER);
            btnProximo.setFocusPainted(false);

            btnFinal = new JButton("Final", new ImageIcon(getClass().getResource("/resources//last.png")));
            btnFinal.setPreferredSize(new Dimension(65,45));
            btnFinal.setVerticalTextPosition(SwingConstants.BOTTOM);
            btnFinal.setHorizontalTextPosition(SwingConstants.CENTER);
            btnFinal.setFocusPainted(false);

            btnBuscar = new JButton("Buscar", new ImageIcon(getClass().getResource("/resources/find.png")));
            btnBuscar.setPreferredSize(new Dimension(65,45));
            btnBuscar.setVerticalTextPosition(SwingConstants.BOTTOM);
            btnBuscar.setHorizontalTextPosition(SwingConstants.CENTER);
            btnBuscar.setFocusPainted(false);

            btnIncluir = new JButton("Incluir", new ImageIcon(getClass().getResource("/resources/add.png")));
            btnIncluir.setPreferredSize(new Dimension(65,45));
            btnIncluir.setVerticalTextPosition(SwingConstants.BOTTOM);
            btnIncluir.setHorizontalTextPosition(SwingConstants.CENTER);
            btnIncluir.setFocusPainted(false);

            btnSalvar = new JButton("Atualizar", new ImageIcon(getClass().getResource("/resources/save.png")));
            btnSalvar.setPreferredSize(new Dimension(65,45));
            btnSalvar.setVerticalTextPosition(SwingConstants.BOTTOM);
            btnSalvar.setHorizontalTextPosition(SwingConstants.CENTER);
            btnSalvar.setFocusPainted(false);

            btnExcluir = new JButton("Excluir", new ImageIcon(getClass().getResource("/resources/minus.png")));
            btnExcluir.setPreferredSize(new Dimension(65,45));
            btnExcluir.setVerticalTextPosition(SwingConstants.BOTTOM);
            btnExcluir.setHorizontalTextPosition(SwingConstants.CENTER);
            btnExcluir.setFocusPainted(false);

            btnCancelar = new JButton("Cancelar", new ImageIcon(getClass().getResource("/resources/undo.png")));
            btnCancelar.setPreferredSize(new Dimension(65,45));
            btnCancelar.setVerticalTextPosition(SwingConstants.BOTTOM);
            btnCancelar.setHorizontalTextPosition(SwingConstants.CENTER);
            btnCancelar.setFocusPainted(false);

            // Os botões serão dispostos um ao lado do outro, fluindo da esquerda para a direita, de cima para baixo
            // para isso usamos um gerenciador de layout da classe FlowLayout:
            // estabelecemos o layout do tbBotoes como flowLayout
            tbBotoes.setLayout(new FlowLayout());

            tbBotoes.add(btnInicio);
            tbBotoes.add(btnAnterior);
            tbBotoes.add(btnProximo);
            tbBotoes.add(btnFinal);
            tbBotoes.addSeparator();    // coloca um separador entre esses botões e os seguintes

            tbBotoes.add(btnBuscar);
            tbBotoes.addSeparator();    // coloca um separador entre esses botões e os seguintes

            tbBotoes.add(btnIncluir);
            tbBotoes.add(btnSalvar);
            tbBotoes.add(btnExcluir);
            tbBotoes.add(btnCancelar);
            tbBotoes.addSeparator();    // coloca um separador entre esses botões e os seguintes

            // os botões apenas serão enfatizados visualmente quando o mouse passar sobre eles
            tbBotoes.setRollover(true);

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

            tbBotoes.add(btnFormExemplares);
            tbBotoes.add(btnFormEmprestimos);
            tbBotoes.add(btnFormDevolucoes);


            JPanel pnlGrade = new JPanel();    	 	// colocaremos JTable com os registros da tabela
            JPanel pnlCampos = new JPanel();        // colocaremos os campos de digitação de dados
            JPanel pnlMensagem = new JPanel(); 		// colocaremos mensagens para o usuário

            JLabel lbMensagem = new JLabel("Mensagem:");	// Label para exibirmos mensagens
            pnlMensagem.add(lbMensagem);
            pnlMensagem.setLayout(new FlowLayout(FlowLayout.LEFT));

            Container cntForm = getContentPane(); 			     // acessa a área de conteúdo do frame
            cntForm.setLayout(new BorderLayout());			     // configura o layout da área de conteúdo
            cntForm.add(tbBotoes, BorderLayout.NORTH);
            cntForm.add(pnlGrade , BorderLayout.WEST);		     // Grade de registros fica à esquerda
            cntForm.add(pnlCampos , BorderLayout.CENTER);	     // Painel de campos fica no centro
            cntForm.add(pnlMensagem , BorderLayout.SOUTH);     	 // Painel de mensagens fica abaixo

            setVisible(false); // deixa invisivel ate o usuario selecionar esse form

            Object [][] dadosLivro = {};
            String[] titulosColunas = {"codigo Livro","titulo","id Autor","id Area"};
            tabLivro = new JTable(modelo);
            tabLivro.setVisible(true);
            JScrollPane barraRolagem = new JScrollPane(tabLivro);
            pnlGrade.add(barraRolagem);

            pnlCampos.setLayout(new GridLayout(5, 2));	//  5 linhas e 2 colunas
            txtCodLivro = new JTextField();
            txtTitulo   = new JTextField();
            txtIdAutor  = new JTextField();
            txtIdArea   = new JTextField();
            txtISBN     = new JTextField();

            pnlCampos.add(new JLabel("Id Livro"));         // 1, 1
            pnlCampos.add(txtCodLivro);                         // 1, 2
            pnlCampos.add(new JLabel("Título do Livro:")); // 2, 1
            pnlCampos.add(txtTitulo);					        // 2, 2
            pnlCampos.add(new JLabel("Id Autor:"));		// 3, 1
            pnlCampos.add(txtIdAutor);					        // 3, 2
            pnlCampos.add(new JLabel("Id Área:"));		    // 4, 1
            pnlCampos.add(txtIdArea);					        // 4, 2
            pnlCampos.add(new JLabel("ISBN:"));            // 5, 1
            pnlCampos.add(txtISBN);                             // 5, 2

            // event listeners dos botoes de forms

            btnFormExemplares.addActionListener(
                    new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            setVisible(false);
                            try {
                                formExemplares.setVisible(true);
                                formExemplares.setIdBibliotecaEscolhida(idBibliotecaEscolhida);
                                formExemplares.preencherDados();
                                formExemplares.exibirRegistro();
                                formExemplares.preencherTabela();
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
                                formEmprestimos.setVisible(true);
                                formEmprestimos.setIdBibliotecaEscolhida(idBibliotecaEscolhida);
                                formEmprestimos.preencherDadosEmprestimo();
                                formEmprestimos.preencherDadosAtrasados();
                                formEmprestimos.exibirRegistroEmprestimos();
                                formEmprestimos.preencherTabelaEmprestimo();
                                formEmprestimos.preencherTabelaAtrasados();
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
                                formDevolucoes.setVisible(true);
                                formDevolucoes.setIdBibliotecaEscolhida(idBibliotecaEscolhida);
//                                formDevolucoes.preencherDados();
//                                formDevolucoes.exibirRegistro();
//                                formDevolucoes.preencherTabela();
                            } catch (Exception ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    }
            );

            // Operações CRUD
            // Inserção
            btnIncluir.addActionListener(
                    new ActionListener()
                    {
                        @Override
                        public void actionPerformed(ActionEvent e)
                        {    // Lógica da inclusão
                            try
                            {
                                dadosDoSelect.moveToInsertRow();
                                dadosDoSelect.updateString("codLivro", txtCodLivro.getText());
                                dadosDoSelect.updateString("titulo", txtTitulo.getText());
                                dadosDoSelect.updateInt("idAutor", Integer.parseInt(txtIdAutor.getText()));
                                dadosDoSelect.updateInt("idArea", Integer.parseInt(txtIdArea.getText()));
                                dadosDoSelect.updateString("ISBN", txtISBN.getText());
                                dadosDoSelect.insertRow();
                                JOptionPane.showMessageDialog(null, "Inclusão bem sucedida!");
                            }
                            catch (SQLException ex)
                            {
                                System.out.println(ex.getMessage());
                            }
                        }
                    }
            );

            // Atualização
            btnSalvar.addActionListener(
                    new ActionListener()
                    {
                        @Override
                        public void actionPerformed(ActionEvent e)
                        {        // lógica da atualização
                            try
                            {
                                dadosDoSelect.updateString("titulo", txtTitulo.getText());
                                dadosDoSelect.updateInt("idAutor", Integer.parseInt(txtIdAutor.getText()));
                                dadosDoSelect.updateInt("idArea", Integer.parseInt(txtIdArea.getText()));
                                dadosDoSelect.updateString("ISBN", txtISBN.getText());
                                dadosDoSelect.updateRow();
                                JOptionPane.showMessageDialog(null,"Atualização bem sucedida!");
                            }
                            catch (SQLException ex)
                            {
                                System.out.println(ex.getMessage());
                            }
                        }
                    }
            );

            // Exclusão
            btnExcluir.addActionListener(
                    new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e)
                        {
                            try
                            {
                                if (JOptionPane.showConfirmDialog(
                                        null, "Deseja realmente excluir?") ==
                                        JOptionPane.OK_OPTION)
                                {
                                    dadosDoSelect.deleteRow();
                                    JOptionPane.showMessageDialog(null, "Exclusão bem sucedida!");
                                    exibirRegistro();   // exibe o próximo registro
                                }
                            }
                            catch (SQLException ex)
                            {
                                System.out.println(ex.getMessage());
                            }
                        }
                    }
            );

            // Consulta
            btnBuscar.addActionListener(
                    new ActionListener()
                    {
                        @Override
                        public void actionPerformed(ActionEvent e)
                        {
                            try
                            {
                                int posicaoAnterior = dadosDoSelect.getRow();  // registro atual
                                String chaveProcurada = txtCodLivro.getText();
                                dadosDoSelect.beforeFirst();      // posiciona antes do 1o registro
                                boolean achou = false;
                                while (! achou && dadosDoSelect.next())
                                {
                                    if (dadosDoSelect.getString("codLivro").compareTo(chaveProcurada) == 0)
                                        achou = true;
                                }
                                if (!achou)
                                {
                                    JOptionPane.showMessageDialog(null, "Registro não encontrado!");
                                    dadosDoSelect.absolute(posicaoAnterior);  // retorna ao registro
                                                                              // anteriormente visível
                                }
                                exibirRegistro();   // exibe o registro encontrado ou o original
                            }
                            catch (SQLException exception)
                            {
                                throw new RuntimeException(exception);
                            }
                        }
                    }
            );


            // Navegação entre registros

            btnInicio.addActionListener(
                    new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try {
                                if (dadosDoSelect.first()) {
                                    exibirRegistro();
                                }
                                else {
                                    JOptionPane.showMessageDialog(null, "Não achou Primeiro registro!");
                                }
                            }
                            catch (SQLException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
            );

            btnAnterior.addActionListener(
                    new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try {
                                if (dadosDoSelect.previous()) {
                                    exibirRegistro();
                                }
                                else {
                                    JOptionPane.showMessageDialog(null, "Não achou Registro anterior!");
                                }
                            }
                            catch (SQLException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
            );

            btnProximo.addActionListener(
                    new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try {
                                if (dadosDoSelect.next()) {
                                    exibirRegistro();
                                }
                                else {
                                    JOptionPane.showMessageDialog(null, "Não achou próximo registro!");
                                }
                            }
                            catch (SQLException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
            );

            btnFinal.addActionListener(
                    new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try {
                                if (dadosDoSelect.last()) {
                                    exibirRegistro();
                                }
                                else {
                                    JOptionPane.showMessageDialog(null, "Não achou Último registro!");
                                }
                            }
                            catch (SQLException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
            );
        }
    }

    public class FormExemplares extends JFrame {
        private static ResultSet dadosDoSelect;   // tabela resultante de um select no BD, PARA NAVEGAÇÃO

        private static JTextField txtIdExemplar, txtIdBiblioteca, txtCodLivro, txtNumeroExemplar;

        private static JTable tabExemplar;	// controle que exibe dados em formato tabular (linhas e colunas)

        // acoes crud
        private JToolBar tbBotoes; // armazenará os botões abaixo; será colocado no topo do formulári
        private JButton btnIncluir, btnSalvar, btnExcluir, btnBuscar, btnProximo, btnAnterior, btnInicio,
                btnFinal, btnCancelar;

        // botões para abrir formulários
        private JButton btnFormLivros, btnFormEmprestimos, btnFormDevolucoes;

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

        private static void exibirRegistro() throws SQLException {
            if (!dadosDoSelect.rowDeleted()) {
                txtIdExemplar.setText(String.valueOf(dadosDoSelect.getInt("idExemplar")));
                txtIdBiblioteca.setText(String.valueOf(dadosDoSelect.getInt("idBiblioteca")));
                txtCodLivro.setText(dadosDoSelect.getString("codLivro"));
                txtNumeroExemplar.setText(String.valueOf(dadosDoSelect.getInt("numeroExemplar")));
            }
        }

        private void preencherDados() {
            String sql = "SELECT * FROM SisBib.Exemplar where idBiblioteca = " + idBibliotecaEscolhida + " order by idExemplar";
            try {
                Statement comandoSQL = conexaoDados.createStatement(
                        ResultSet.TYPE_SCROLL_SENSITIVE,	// permite navegação
                        ResultSet.CONCUR_UPDATABLE        // ResultSet é atualizável
                );
                try {
                    dadosDoSelect = comandoSQL.executeQuery(sql);
                    System.out.print(dadosDoSelect);
                    if (dadosDoSelect.next()) {
                        exibirRegistro();
                        //preencherTabela();
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
        }

        public static void preencherTabela() throws SQLException {
            do{
                // preencher tabela
            }
            while (dadosDoSelect.next());
        }


        public FormExemplares() {
            setTitle("Sistema de Biblioteca | Exemplares");
            setSize(1000, 300);

            // Adiciorenamos os botões ao JToolBar que os conterá
            tbBotoes = new JToolBar();  // orientação padrão é HORIZONTAL

            btnInicio = new JButton("Inicio", new ImageIcon(getClass().getResource("/resources/first.png")));
            btnInicio.setPreferredSize(new Dimension(65,45));
            btnInicio.setVerticalTextPosition(SwingConstants.BOTTOM);
            btnInicio.setHorizontalTextPosition(SwingConstants.CENTER);
            btnInicio.setFocusPainted(false);       //remove uma borda que fica dentro do último botão pressionado

            btnAnterior = new JButton("Voltar", new ImageIcon(getClass().getResource("/resources/prior.png")));
            btnAnterior.setPreferredSize(new Dimension(65,45));
            btnAnterior.setVerticalTextPosition(SwingConstants.BOTTOM);
            btnAnterior.setHorizontalTextPosition(SwingConstants.CENTER);
            btnAnterior.setFocusPainted(false);

            btnProximo = new JButton("Avancar", new ImageIcon(getClass().getResource("/resources/next.png")));
            btnProximo.setPreferredSize(new Dimension(65,45));
            btnProximo.setVerticalTextPosition(SwingConstants.BOTTOM);
            btnProximo.setHorizontalTextPosition(SwingConstants.CENTER);
            btnProximo.setFocusPainted(false);

            btnFinal = new JButton("Final", new ImageIcon(getClass().getResource("/resources//last.png")));
            btnFinal.setPreferredSize(new Dimension(65,45));
            btnFinal.setVerticalTextPosition(SwingConstants.BOTTOM);
            btnFinal.setHorizontalTextPosition(SwingConstants.CENTER);
            btnFinal.setFocusPainted(false);

            btnBuscar = new JButton("Buscar", new ImageIcon(getClass().getResource("/resources/find.png")));
            btnBuscar.setPreferredSize(new Dimension(65,45));
            btnBuscar.setVerticalTextPosition(SwingConstants.BOTTOM);
            btnBuscar.setHorizontalTextPosition(SwingConstants.CENTER);
            btnBuscar.setFocusPainted(false);

            btnIncluir = new JButton("Incluir", new ImageIcon(getClass().getResource("/resources/add.png")));
            btnIncluir.setPreferredSize(new Dimension(65,45));
            btnIncluir.setVerticalTextPosition(SwingConstants.BOTTOM);
            btnIncluir.setHorizontalTextPosition(SwingConstants.CENTER);
            btnIncluir.setFocusPainted(false);

            btnSalvar = new JButton("Atualizar", new ImageIcon(getClass().getResource("/resources/save.png")));
            btnSalvar.setPreferredSize(new Dimension(65,45));
            btnSalvar.setVerticalTextPosition(SwingConstants.BOTTOM);
            btnSalvar.setHorizontalTextPosition(SwingConstants.CENTER);
            btnSalvar.setFocusPainted(false);

            btnExcluir = new JButton("Excluir", new ImageIcon(getClass().getResource("/resources/minus.png")));
            btnExcluir.setPreferredSize(new Dimension(65,45));
            btnExcluir.setVerticalTextPosition(SwingConstants.BOTTOM);
            btnExcluir.setHorizontalTextPosition(SwingConstants.CENTER);
            btnExcluir.setFocusPainted(false);

            btnCancelar = new JButton("Cancelar", new ImageIcon(getClass().getResource("/resources/undo.png")));
            btnCancelar.setPreferredSize(new Dimension(65,45));
            btnCancelar.setVerticalTextPosition(SwingConstants.BOTTOM);
            btnCancelar.setHorizontalTextPosition(SwingConstants.CENTER);
            btnCancelar.setFocusPainted(false);

            // Os botões serão dispostos um ao lado do outro, fluindo da esquerda para a direita, de cima para baixo
            // para isso usamos um gerenciador de layout da classe FlowLayout:
            // estabelecemos o layout do tbBotoes como flowLayout
            tbBotoes.setLayout(new FlowLayout());

            tbBotoes.add(btnInicio);
            tbBotoes.add(btnAnterior);
            tbBotoes.add(btnProximo);
            tbBotoes.add(btnFinal);
            tbBotoes.addSeparator();    // coloca um separador entre esses botões e os seguintes

            tbBotoes.add(btnBuscar);
            tbBotoes.addSeparator();    // coloca um separador entre esses botões e os seguintes

            tbBotoes.add(btnIncluir);
            tbBotoes.add(btnSalvar);
            tbBotoes.add(btnExcluir);
            tbBotoes.add(btnCancelar);
            tbBotoes.addSeparator();    // coloca um separador entre esses botões e os seguintes

            // os botões apenas serão enfatizados visualmente quando o mouse passar sobre eles
            tbBotoes.setRollover(true);

            btnFormLivros = new JButton("livros");
            btnFormLivros.setPreferredSize(new Dimension(85,45));
            btnFormLivros.setVerticalTextPosition(SwingConstants.BOTTOM);
            btnFormLivros.setHorizontalTextPosition(SwingConstants.CENTER);
            btnFormLivros.setFocusPainted(false);

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

            tbBotoes.add(btnFormLivros);
            tbBotoes.add(btnFormEmprestimos);
            tbBotoes.add(btnFormDevolucoes);


            JPanel pnlGrade = new JPanel();    	 	// colocaremos JTable com os registros da tabela
            JPanel pnlCampos = new JPanel();        // colocaremos os campos de digitação de dados
            JPanel pnlMensagem = new JPanel(); 		// colocaremos mensagens para o usuário

            JLabel lbMensagem = new JLabel("Mensagem:");	// Label para exibirmos mensagens
            pnlMensagem.add(lbMensagem);
            pnlMensagem.setLayout(new FlowLayout(FlowLayout.LEFT));

            Container cntForm = getContentPane(); 			     // acessa a área de conteúdo do frame
            cntForm.setLayout(new BorderLayout());			     // configura o layout da área de conteúdo
            cntForm.add(tbBotoes, BorderLayout.NORTH);
            cntForm.add(pnlGrade , BorderLayout.WEST);		     // Grade de registros fica à esquerda
            cntForm.add(pnlCampos , BorderLayout.CENTER);	     // Painel de campos fica no centro
            cntForm.add(pnlMensagem , BorderLayout.SOUTH);     	 // Painel de mensagens fica abaixo

            setVisible(false); // deixa invisivel ate o usuario selecionar esse form

            Object [][] dadosLivro = {};
            String[] titulosColunas = {"Id Exemplar","Id Biblioteca","Codigo Livro","Número Exemplar"};
            tabExemplar = new JTable(dadosLivro,  titulosColunas);
            JScrollPane barraRolagem = new JScrollPane(tabExemplar);
            pnlGrade.add(barraRolagem);

            pnlCampos.setLayout(new GridLayout(4, 2));	//  4 linhas e 2 colunas
            txtIdExemplar = new JTextField();
            txtIdBiblioteca = new JTextField();
            txtIdBiblioteca.setEditable(false); // não pode editar o id da bibiloteca
            txtCodLivro = new JTextField();
            txtNumeroExemplar = new JTextField();

            pnlCampos.add(new JLabel("Id Exemplar"));      // 1, 1
            pnlCampos.add(txtIdExemplar);                       // 1, 2
            pnlCampos.add(new JLabel("Id Biblioteca:"));   // 2, 1
            pnlCampos.add(txtIdBiblioteca);				        // 2, 2
            pnlCampos.add(new JLabel("Codigo Livro:"));	// 3, 1
            pnlCampos.add(txtCodLivro);					        // 3, 2
            pnlCampos.add(new JLabel("Número exemplar:")); // 4, 1
            pnlCampos.add(txtNumeroExemplar);			        // 4, 2

            // event listeners dos botoes de forms

            btnFormLivros.addActionListener(
                    new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            setVisible(false);
                            try {
                                formLivros.setVisible(true);
                                formLivros.setIdBibliotecaEscolhida(idBibliotecaEscolhida);
                                formLivros.preencherDados();
                                formLivros.exibirRegistro();
                                formLivros.preencherTabela();
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
                                formEmprestimos.setVisible(true);
                                formEmprestimos.setIdBibliotecaEscolhida(idBibliotecaEscolhida);
                                formEmprestimos.preencherDadosEmprestimo();
                                formEmprestimos.preencherDadosAtrasados();
                                formEmprestimos.exibirRegistroEmprestimos();
                                formEmprestimos.preencherTabelaEmprestimo();
                                formEmprestimos.preencherTabelaAtrasados();
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
                                formDevolucoes.setVisible(true);
                                formDevolucoes.setIdBibliotecaEscolhida(idBibliotecaEscolhida);
//                                formDevolucoes.preencherDados();
//                                formDevolucoes.exibirRegistro();
//                                formDevolucoes.preencherTabela();
                            } catch (Exception ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    }
            );

            // Operações CRUD
            // Inserção
            btnIncluir.addActionListener(
                    new ActionListener()
                    {
                        @Override
                        public void actionPerformed(ActionEvent e)
                        {    // Lógica da inclusão
                            try
                            {
                                dadosDoSelect.moveToInsertRow();
                                dadosDoSelect.updateInt("idExemplar", Integer.parseInt(txtIdExemplar.getText()));
                                dadosDoSelect.updateInt("idBiblioteca", Integer.parseInt(txtIdBiblioteca.getText()));
                                dadosDoSelect.updateString("codLivro", txtCodLivro.getText());
                                dadosDoSelect.updateInt("numeroExemplar", Integer.parseInt(txtNumeroExemplar.getText()));
                                dadosDoSelect.insertRow();
                                JOptionPane.showMessageDialog(null, "Inclusão bem sucedida!");
                            }
                            catch (SQLException ex)
                            {
                                System.out.println(ex.getMessage());
                            }
                        }
                    }
            );

            // Atualização
            btnSalvar.addActionListener(
                    new ActionListener()
                    {
                        @Override
                        public void actionPerformed(ActionEvent e)
                        {        // lógica da atualização
                            try
                            {
                                dadosDoSelect.updateInt("idBiblioteca", Integer.parseInt(txtIdBiblioteca.getText()));
                                dadosDoSelect.updateString("codLivro", txtCodLivro.getText());
                                dadosDoSelect.updateInt("numeroExemplar", Integer.parseInt(txtNumeroExemplar.getText()));
                                dadosDoSelect.updateRow();
                                JOptionPane.showMessageDialog(null,"Atualização bem sucedida!");
                            }
                            catch (SQLException ex)
                            {
                                System.out.println(ex.getMessage());
                            }
                        }
                    }
            );

            // Exclusão
            btnExcluir.addActionListener(
                    new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e)
                        {
                            try
                            {
                                if (JOptionPane.showConfirmDialog(
                                        null, "Deseja realmente excluir?") ==
                                        JOptionPane.OK_OPTION)
                                {
                                    dadosDoSelect.deleteRow();
                                    JOptionPane.showMessageDialog(null, "Exclusão bem sucedida!");
                                    exibirRegistro();   // exibe o próximo registro
                                }
                            }
                            catch (SQLException ex)
                            {
                                System.out.println(ex.getMessage());
                            }
                        }
                    }
            );

            // Consulta
            btnBuscar.addActionListener(
                    new ActionListener()
                    {
                        @Override
                        public void actionPerformed(ActionEvent e)
                        {
                            try
                            {
                                int posicaoAnterior = dadosDoSelect.getRow();  // registro atual
                                String chaveProcurada = txtIdExemplar.getText();
                                dadosDoSelect.beforeFirst();      // posiciona antes do 1o registro
                                boolean achou = false;
                                while (! achou && dadosDoSelect.next())
                                {
                                    if (dadosDoSelect.getString("idExemplar").compareTo(chaveProcurada) == 0)
                                        achou = true;
                                }
                                if (!achou)
                                {
                                    JOptionPane.showMessageDialog(null, "Registro não encontrado!");
                                    dadosDoSelect.absolute(posicaoAnterior);  // retorna ao registro
                                    // anteriormente visível
                                }
                                exibirRegistro();   // exibe o registro encontrado ou o original
                            }
                            catch (SQLException exception)
                            {
                                throw new RuntimeException(exception);
                            }
                        }
                    }
            );


            // Navegação entre registros

            btnInicio.addActionListener(
                    new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try {
                                if (dadosDoSelect.first()) {
                                    exibirRegistro();
                                }
                                else {
                                    JOptionPane.showMessageDialog(null, "Não achou Primeiro registro!");
                                }
                            }
                            catch (SQLException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
            );

            btnAnterior.addActionListener(
                    new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try {
                                if (dadosDoSelect.previous()) {
                                    exibirRegistro();
                                }
                                else {
                                    JOptionPane.showMessageDialog(null, "Não achou Registro anterior!");
                                }
                            }
                            catch (SQLException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
            );

            btnProximo.addActionListener(
                    new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try {
                                if (dadosDoSelect.next()) {
                                    exibirRegistro();
                                }
                                else {
                                    JOptionPane.showMessageDialog(null, "Não achou próximo registro!");
                                }
                            }
                            catch (SQLException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
            );

            btnFinal.addActionListener(
                    new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try {
                                if (dadosDoSelect.last()) {
                                    exibirRegistro();
                                }
                                else {
                                    JOptionPane.showMessageDialog(null, "Não achou Último registro!");
                                }
                            }
                            catch (SQLException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
            );
        }
    }

    public class FormEmprestimos extends JFrame {
        // botoes e coisas do tipo
        private static ResultSet dadosDoSelectEmprestimos, dadosDoSelectAtrasados;   // tabela resultante de um select no BD, PARA NAVEGAÇÃO

        private static JTextField txtIdEmprestimo, txtIdLeitor, txtIdExemplar, txtDataEmprestimo, txtDevolucaoEfetiva, txtDevolucaoPrevista;

        private static JTable tabEmprestimos, tabAtrasados;	// controle que exibe dados em formato tabular (linhas e colunas)

        // tab que alterna entre emprestimos e livros em atraso

        private JTabbedPane tabbedPane;

        // acoes crud
        private JToolBar tbBotoes; // armazenará os botões abaixo; será colocado no topo do formulári
        private JButton btnIncluir, btnSalvar, btnExcluir, btnBuscar, btnProximo, btnAnterior, btnInicio,
                btnFinal, btnCancelar;

        // botões para abrir formulários
        private JButton btnFormLivros, btnFormExemplares, btnFormDevolucoes;


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

        private void preencherDadosEmprestimo() {
            String sql = "SELECT * FROM SisBib.Emprestimo where idExemplar in (select idExemplar from sisbib.Exemplar where idBiblioteca = " + idBibliotecaEscolhida + ") order by idEmprestimo";
            try {
                Statement comandoSQL = conexaoDados.createStatement(
                        ResultSet.TYPE_SCROLL_SENSITIVE,	// permite navegação
                        ResultSet.CONCUR_UPDATABLE        // ResultSet é atualizável
                );
                try {
                    dadosDoSelectEmprestimos = comandoSQL.executeQuery(sql);
                    System.out.print(dadosDoSelectEmprestimos);
                    if (dadosDoSelectEmprestimos.next()) {
                        preencherTabelaEmprestimo();
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
        }

        private void preencherDadosAtrasados() {
            String sql = "SELECT * FROM SisBib.AtrasoEMultas where codigo in(select idExemplar from sisbib.Exemplar where idBiblioteca = " + idBibliotecaEscolhida + ") order by codigo";
            try {
                Statement comandoSQL = conexaoDados.createStatement(
                        ResultSet.TYPE_SCROLL_SENSITIVE,	// permite navegação
                        ResultSet.CONCUR_UPDATABLE        // ResultSet é atualizável
                );
                try {
                    dadosDoSelectAtrasados = comandoSQL.executeQuery(sql);
                    System.out.print(dadosDoSelectAtrasados);
                    if (dadosDoSelectAtrasados.next()) {
                        preencherTabelaAtrasados();
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
        }

        private void preencherTabelaEmprestimo() {
            // dps faz
        }

        private void preencherTabelaAtrasados() {
            // dps faz
        }

        private static void exibirRegistroEmprestimos() throws SQLException {
            if (!dadosDoSelectEmprestimos.rowDeleted()) {
                txtIdEmprestimo.setText(String.valueOf(dadosDoSelectEmprestimos.getInt("idEmprestimo")));
                txtIdLeitor.setText(String.valueOf(dadosDoSelectEmprestimos.getInt("idLeitor")));
                txtIdExemplar.setText(String.valueOf(dadosDoSelectEmprestimos.getInt("idExemplar")));
                txtDataEmprestimo.setText(String.valueOf(dadosDoSelectEmprestimos.getDate("dataEmprestimo")));
                txtDevolucaoEfetiva.setText(String.valueOf(dadosDoSelectEmprestimos.getDate("devolucaoEfetiva")));
                txtDevolucaoPrevista.setText(String.valueOf(dadosDoSelectEmprestimos.getDate("devolucaoPrevista")));
            }
        }

        public FormEmprestimos() {
            setTitle("Sistema de Biblioteca | Empréstimos");
            setSize(1000, 300);

            // Adiciorenamos os botões ao JToolBar que os conterá
            tbBotoes = new JToolBar();  // orientação padrão é HORIZONTAL

            btnInicio = new JButton("Inicio", new ImageIcon(getClass().getResource("/resources/first.png")));
            btnInicio.setPreferredSize(new Dimension(65,45));
            btnInicio.setVerticalTextPosition(SwingConstants.BOTTOM);
            btnInicio.setHorizontalTextPosition(SwingConstants.CENTER);
            btnInicio.setFocusPainted(false);       //remove uma borda que fica dentro do último botão pressionado

            btnAnterior = new JButton("Voltar", new ImageIcon(getClass().getResource("/resources/prior.png")));
            btnAnterior.setPreferredSize(new Dimension(65,45));
            btnAnterior.setVerticalTextPosition(SwingConstants.BOTTOM);
            btnAnterior.setHorizontalTextPosition(SwingConstants.CENTER);
            btnAnterior.setFocusPainted(false);

            btnProximo = new JButton("Avancar", new ImageIcon(getClass().getResource("/resources/next.png")));
            btnProximo.setPreferredSize(new Dimension(65,45));
            btnProximo.setVerticalTextPosition(SwingConstants.BOTTOM);
            btnProximo.setHorizontalTextPosition(SwingConstants.CENTER);
            btnProximo.setFocusPainted(false);

            btnFinal = new JButton("Final", new ImageIcon(getClass().getResource("/resources//last.png")));
            btnFinal.setPreferredSize(new Dimension(65,45));
            btnFinal.setVerticalTextPosition(SwingConstants.BOTTOM);
            btnFinal.setHorizontalTextPosition(SwingConstants.CENTER);
            btnFinal.setFocusPainted(false);

            btnBuscar = new JButton("Buscar", new ImageIcon(getClass().getResource("/resources/find.png")));
            btnBuscar.setPreferredSize(new Dimension(65,45));
            btnBuscar.setVerticalTextPosition(SwingConstants.BOTTOM);
            btnBuscar.setHorizontalTextPosition(SwingConstants.CENTER);
            btnBuscar.setFocusPainted(false);

            btnIncluir = new JButton("Incluir", new ImageIcon(getClass().getResource("/resources/add.png")));
            btnIncluir.setPreferredSize(new Dimension(65,45));
            btnIncluir.setVerticalTextPosition(SwingConstants.BOTTOM);
            btnIncluir.setHorizontalTextPosition(SwingConstants.CENTER);
            btnIncluir.setFocusPainted(false);

            btnSalvar = new JButton("Atualizar", new ImageIcon(getClass().getResource("/resources/save.png")));
            btnSalvar.setPreferredSize(new Dimension(65,45));
            btnSalvar.setVerticalTextPosition(SwingConstants.BOTTOM);
            btnSalvar.setHorizontalTextPosition(SwingConstants.CENTER);
            btnSalvar.setFocusPainted(false);

            btnExcluir = new JButton("Excluir", new ImageIcon(getClass().getResource("/resources/minus.png")));
            btnExcluir.setPreferredSize(new Dimension(65,45));
            btnExcluir.setVerticalTextPosition(SwingConstants.BOTTOM);
            btnExcluir.setHorizontalTextPosition(SwingConstants.CENTER);
            btnExcluir.setFocusPainted(false);

            btnCancelar = new JButton("Cancelar", new ImageIcon(getClass().getResource("/resources/undo.png")));
            btnCancelar.setPreferredSize(new Dimension(65,45));
            btnCancelar.setVerticalTextPosition(SwingConstants.BOTTOM);
            btnCancelar.setHorizontalTextPosition(SwingConstants.CENTER);
            btnCancelar.setFocusPainted(false);

            // Os botões serão dispostos um ao lado do outro, fluindo da esquerda para a direita, de cima para baixo
            // para isso usamos um gerenciador de layout da classe FlowLayout:
            // estabelecemos o layout do tbBotoes como flowLayout
            tbBotoes.setLayout(new FlowLayout());

            tbBotoes.add(btnInicio);
            tbBotoes.add(btnAnterior);
            tbBotoes.add(btnProximo);
            tbBotoes.add(btnFinal);
            tbBotoes.addSeparator();    // coloca um separador entre esses botões e os seguintes

            tbBotoes.add(btnBuscar);
            tbBotoes.addSeparator();    // coloca um separador entre esses botões e os seguintes

            tbBotoes.add(btnIncluir);
            tbBotoes.add(btnSalvar);
            tbBotoes.add(btnExcluir);
            tbBotoes.add(btnCancelar);
            tbBotoes.addSeparator();    // coloca um separador entre esses botões e os seguintes

            // os botões apenas serão enfatizados visualmente quando o mouse passar sobre eles
            tbBotoes.setRollover(true);

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

            btnFormDevolucoes = new JButton("Devoluções");
            btnFormDevolucoes.setPreferredSize(new Dimension(85,45));
            btnFormDevolucoes.setVerticalTextPosition(SwingConstants.BOTTOM);
            btnFormDevolucoes.setHorizontalTextPosition(SwingConstants.CENTER);
            btnFormDevolucoes.setFocusPainted(false);

            tbBotoes.add(btnFormLivros);
            tbBotoes.add(btnFormExemplares);
            tbBotoes.add(btnFormDevolucoes);


            JPanel pnlGrade = new JPanel();    	 	// colocaremos JTable com os registros da tabela
            JPanel pnlCampos = new JPanel();        // colocaremos os campos de digitação de dados
            JPanel pnlMensagem = new JPanel(); 		// colocaremos mensagens para o usuário

            JLabel lbMensagem = new JLabel("Mensagem:");	// Label para exibirmos mensagens
            pnlMensagem.add(lbMensagem);
            pnlMensagem.setLayout(new FlowLayout(FlowLayout.LEFT));


            JPanel pnlEmprestimos = new JPanel(); 			             // panel para a primeira tab
            pnlEmprestimos.setLayout(new BorderLayout());			     // configura o layout da área de conteúdo
            pnlEmprestimos.add(tbBotoes, BorderLayout.NORTH);
            pnlEmprestimos.add(pnlGrade , BorderLayout.WEST);		     // Grade de registros fica à esquerda
            pnlEmprestimos.add(pnlCampos , BorderLayout.CENTER);	     // Painel de campos fica no centro
            pnlEmprestimos.add(pnlMensagem , BorderLayout.SOUTH);     	 // Painel de mensagens fica abaixo

            JPanel pnlAtrasados = new JPanel();
            pnlAtrasados.setLayout(new BorderLayout());

            tabbedPane = new JTabbedPane();
            tabbedPane.addTab("Listar empréstimos", pnlEmprestimos);
            tabbedPane.addTab("Listar atrasados", pnlAtrasados);
            pnlAtrasados.add(tbBotoes, BorderLayout.NORTH);

            Container cntPrincipal = getContentPane();
            cntPrincipal.add(tabbedPane);

            setVisible(false); // deixa invisivel ate o usuario selecionar esse form

            // tab emprestimos
            Object [][] dadosEmprestimo = {};
            String[] titulosColunas = {"Id Empréstimo","Id Leitor","Id Exemplar","Data do Empréstimo", "Data da Devolução Efetiva", "Data Devolução Prevista"};
            tabEmprestimos = new JTable(dadosEmprestimo,  titulosColunas);
            JScrollPane barraRolagem = new JScrollPane(tabEmprestimos);
            pnlGrade.add(barraRolagem);

            // tab de atrasados
            Object [][] dadosAtrasado = {};
            String[] titulosColunasAtrasados = {"codigo","multa"};
            tabAtrasados = new JTable(dadosAtrasado,  titulosColunasAtrasados);
            JScrollPane barraRolagemAtrasados = new JScrollPane(tabAtrasados);
            pnlAtrasados.add(barraRolagemAtrasados, BorderLayout.CENTER);

            pnlCampos.setLayout(new GridLayout(6, 2));	//  5 linhas e 2 colunas
            txtIdEmprestimo      = new JTextField();
            txtIdLeitor          = new JTextField();
            txtIdExemplar        = new JTextField();
            txtDataEmprestimo    = new JTextField();
            txtDevolucaoEfetiva  = new JTextField();
            txtDevolucaoPrevista = new JTextField();

            pnlCampos.add(new JLabel("Id Empréstimo"));              // 1, 1
            pnlCampos.add(txtIdEmprestimo);                               // 1, 2
            pnlCampos.add(new JLabel("Id Leitor:"));                 // 2, 1
            pnlCampos.add(txtIdLeitor);					                  // 2, 2
            pnlCampos.add(new JLabel("Id Exemplar:"));		          // 3, 1
            pnlCampos.add(txtIdExemplar);					              // 3, 2
            pnlCampos.add(new JLabel("Data do Empréstimo:"));		  // 4, 1
            pnlCampos.add(txtDataEmprestimo);					          // 4, 2
            pnlCampos.add(new JLabel("Data da Devolução Efetiva:")); // 5, 1
            pnlCampos.add(txtDevolucaoEfetiva);                           // 5, 2
            pnlCampos.add(new JLabel("Data da Devolução Prevista:"));// 6, 1
            pnlCampos.add(txtDevolucaoPrevista);                          // 6, 2


            // event listeners dos botoes de forms

            btnFormExemplares.addActionListener(
                    new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            setVisible(false);
                            try {
                                formExemplares.setVisible(true);
                                formExemplares.setIdBibliotecaEscolhida(idBibliotecaEscolhida);
                                formExemplares.preencherDados();
                                formExemplares.exibirRegistro();
                                formExemplares.preencherTabela();
                            } catch (Exception ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    }
            );

            btnFormLivros.addActionListener(
                    new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            setVisible(false);
                            try {
                                formLivros.setVisible(true);
                                formLivros.setIdBibliotecaEscolhida(idBibliotecaEscolhida);
                                formLivros.preencherDados();
                                formLivros.exibirRegistro();
                                formLivros.preencherTabela();
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
                                formDevolucoes.setVisible(true);
                                formDevolucoes.setIdBibliotecaEscolhida(idBibliotecaEscolhida);
//                                formDevolucoes.preencherDados();
//                                formDevolucoes.exibirRegistro();
//                                formDevolucoes.preencherTabela();
                            } catch (Exception ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    }
            );

            // Operações CRUD
            // Inserção
//            btnIncluir.addActionListener(
//                    new ActionListener()
//                    {
//                        @Override
//                        public void actionPerformed(ActionEvent e)
//                        {    // Lógica da inclusão
//                            try
//                            {
//                                dadosDoSelectEmprestimos.moveToInsertRow();
//                                dadosDoSelectEmprestimos.updateString("codLivro", txtCodLivro.getText());
//                                dadosDoSelectEmprestimos.updateString("titulo", txtTitulo.getText());
//                                dadosDoSelectEmprestimos.updateInt("idAutor", Integer.parseInt(txtIdAutor.getText()));
//                                dadosDoSelectEmprestimos.updateInt("idArea", Integer.parseInt(txtIdArea.getText()));
//                                dadosDoSelectEmprestimos.updateString("ISBN", txtISBN.getText());
//                                dadosDoSelectEmprestimos.insertRow();
//                                JOptionPane.showMessageDialog(null, "Inclusão bem sucedida!");
//                            }
//                            catch (SQLException ex)
//                            {
//                                System.out.println(ex.getMessage());
//                            }
//                        }
//                    }
//            );
//
//            // Atualização
//            btnSalvar.addActionListener(
//                    new ActionListener()
//                    {
//                        @Override
//                        public void actionPerformed(ActionEvent e)
//                        {        // lógica da atualização
//                            try
//                            {
//                                dadosDoSelectEmprestimos.updateString("titulo", txtTitulo.getText());
//                                dadosDoSelectEmprestimos.updateInt("idAutor", Integer.parseInt(txtIdAutor.getText()));
//                                dadosDoSelectEmprestimos.updateInt("idArea", Integer.parseInt(txtIdArea.getText()));
//                                dadosDoSelectEmprestimos.updateString("ISBN", txtISBN.getText());
//                                dadosDoSelectEmprestimos.updateRow();
//                                JOptionPane.showMessageDialog(null,"Atualização bem sucedida!");
//                            }
//                            catch (SQLException ex)
//                            {
//                                System.out.println(ex.getMessage());
//                            }
//                        }
//                    }
//            );
//
//            // Exclusão
//            btnExcluir.addActionListener(
//                    new ActionListener() {
//                        @Override
//                        public void actionPerformed(ActionEvent e)
//                        {
//                            try
//                            {
//                                if (JOptionPane.showConfirmDialog(
//                                        null, "Deseja realmente excluir?") ==
//                                        JOptionPane.OK_OPTION)
//                                {
//                                    dadosDoSelectEmprestimos.deleteRow();
//                                    JOptionPane.showMessageDialog(null, "Exclusão bem sucedida!");
////                                    exibirRegistro();   // exibe o próximo registro
//                                }
//                            }
//                            catch (SQLException ex)
//                            {
//                                System.out.println(ex.getMessage());
//                            }
//                        }
//                    }
//            );
//
//            // Consulta
//            btnBuscar.addActionListener(
//                    new ActionListener()
//                    {
//                        @Override
//                        public void actionPerformed(ActionEvent e)
//                        {
//                            try
//                            {
//                                int posicaoAnterior = dadosDoSelectEmprestimos.getRow();  // registro atual
//                                String chaveProcurada = txtCodLivro.getText();
//                                dadosDoSelectEmprestimos.beforeFirst();      // posiciona antes do 1o registro
//                                boolean achou = false;
//                                while (! achou && dadosDoSelectEmprestimos.next())
//                                {
//                                    if (dadosDoSelectEmprestimos.getString("codLivro").compareTo(chaveProcurada) == 0)
//                                        achou = true;
//                                }
//                                if (!achou)
//                                {
//                                    JOptionPane.showMessageDialog(null, "Registro não encontrado!");
//                                    dadosDoSelectEmprestimos.absolute(posicaoAnterior);  // retorna ao registro
//                                    // anteriormente visível
//                                }
//                                preencherTabelaEmprestimo();   // exibe o registro encontrado ou o original
//                            }
//                            catch (SQLException exception)
//                            {
//                                throw new RuntimeException(exception);
//                            }
//                        }
//                    }
//            );


            // Navegação entre registros

            btnInicio.addActionListener(
                    new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try {
                                if (dadosDoSelectEmprestimos.first()) {
                                    exibirRegistroEmprestimos();
                                }
                                else {
                                    JOptionPane.showMessageDialog(null, "Não achou Primeiro registro!");
                                }
                            }
                            catch (SQLException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
            );

            btnAnterior.addActionListener(
                    new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try {
                                if (dadosDoSelectEmprestimos.previous()) {
                                    exibirRegistroEmprestimos();
                                }
                                else {
                                    JOptionPane.showMessageDialog(null, "Não achou Registro anterior!");
                                }
                            }
                            catch (SQLException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
            );

            btnProximo.addActionListener(
                    new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try {
                                if (dadosDoSelectEmprestimos.next()) {
                                    exibirRegistroEmprestimos();
                                }
                                else {
                                    JOptionPane.showMessageDialog(null, "Não achou próximo registro!");
                                }
                            }
                            catch (SQLException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
            );

            btnFinal.addActionListener(
                    new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try {
                                if (dadosDoSelectEmprestimos.last()) {
                                    exibirRegistroEmprestimos();
                                }
                                else {
                                    JOptionPane.showMessageDialog(null, "Não achou Último registro!");
                                }
                            }
                            catch (SQLException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
            );
        }
    }

    public class FormDevolucoes extends JFrame {
        // botoes e coisas do tipo sei la mo preguica slk

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
    }
}
