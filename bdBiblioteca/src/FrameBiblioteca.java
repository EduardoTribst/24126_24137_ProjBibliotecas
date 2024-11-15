import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;

public class FrameBiblioteca extends JFrame {
    public static Connection conexaoDados = null;

    public int idBibliotecaEscolhida;

    public static FrameBiblioteca form;

    private  FormLogin formLogin;
    private FormLivros formLivros;
    private FormExemplares formExemplares;
    private FormEmprestimos formEmprestimos;
    private FormDevolucoes formDevolucoes;

    private static ResultSet dadosDoSelect;

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
                form.setVisible(true);
            }
        });
    }

    public FrameBiblioteca() { // inicializa as coisas
        setTitle("Manutencao de Bibliotecas");
        setSize(1000, 300);
        // apenas chame o evento windowClosing
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        // inicializa os forms

        formLogin       = new FormLogin();
        formLivros      = new FormLivros();
        formExemplares  = new FormExemplares();
        formEmprestimos = new FormEmprestimos();
        formDevolucoes  = new FormDevolucoes();


        add(formLogin);
        add(formLivros);
    }

    public class FormLogin extends JFrame {
        private JLabel labMensagem;
        private JTextField txtServidor, txtNomeBd, txtUsuario, txtSenha;
        //        private JPasswordField pswfPassword;
        private JButton btnConectar;
        private JPanel panInputs, panSelectBiblioteca, panBtnConectar, panMensagem;
        private JComboBox<String> cbxBiblioteca;

        // toolbar que contém os botões de navegação entre os formulários
        public JToolBar tbBotoesNavegacao;
        // botões para abrir formulários
        public JButton btnFormLivros, btnFormExemplares, btnFormEmprestimos, btnFormDevolucoes;

        public FormLogin() {
            setTitle("Login");
            setSize(800, 300);

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
                                conexaoDados = ConexaoBD.getConnection(txtServidor.getText(), txtNomeBd.getText(), txtUsuario.getText(), txtSenha.getText());
                                labMensagem.setText("Mensagem: conectado!");
                                cbxBiblioteca.setEnabled(true);
                                btnFormLivros.setEnabled(true);
                                btnFormExemplares.setEnabled(true);
                                btnFormEmprestimos.setEnabled(true);
                                btnFormDevolucoes.setEnabled(true);
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
                            formLivros.setVisible(true);
                        }
                    }
            );

            cbxBiblioteca.addActionListener(
                    new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {

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
        }
    }

    public class FormLivros extends JFrame {
        private static ResultSet dadosDoSelect;   // tabela resultante de um select no BD, PARA NAVEGAÇÃO

        private static JTextField txtTitulo;
        private static JSpinner spnCodLivro, spnIdAutor, spnIdArea;

        private static JTable tabLivro;	// controle que exibe dados em formato tabular (linhas e colunas)

        // acoes crud
        private JToolBar tbBotoes; // armazenará os botões abaixo; será colocado no topo do formulári
        private JButton btnIncluir, btnSalvar, btnExcluir, btnBuscar, btnProximo, btnAnterior, btnInicio,
                btnFinal, btnCancelar;

        // botões para abrir formulários
        public JButton btnFormExemplares, btnFormEmprestimos, btnFormDevolucoes;

        static private void exibirRegistro() throws SQLException
        {
            if (!dadosDoSelect.rowDeleted())
            {
                spnCodLivro.setValue(dadosDoSelect.getInt("codLivro"));
                txtTitulo.setText(dadosDoSelect.getString("titulo"));
                spnIdAutor.setValue(dadosDoSelect.getInt("idAutor"));
                spnIdArea.setValue(dadosDoSelect.getInt("idArea"));
            }
        }

        private static void preencherDados() {
            String sql = "SELECT * FROM SisBib.livro order by codLivro";
            try {
                Statement comandoSQL = conexaoDados.createStatement(
                        ResultSet.TYPE_SCROLL_SENSITIVE,	// permite navegação
                        ResultSet.CONCUR_UPDATABLE        // ResultSet é atualizável
                );
                try {
                    dadosDoSelect = comandoSQL.executeQuery(sql);
                    if (dadosDoSelect.next()) {
                        exibirRegistro();
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

        public FormLivros() {
            setTitle("Livros");
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
            tabLivro = new JTable(dadosLivro,  titulosColunas);
            JScrollPane barraRolagem = new JScrollPane(tabLivro);
            pnlGrade.add(barraRolagem);

            pnlCampos.setLayout(new GridLayout(4, 2));	//  4 linhas e 2 colunas
            spnCodLivro = new JSpinner();
            txtTitulo = new JTextField();
            spnIdAutor     = new JSpinner();
            spnIdArea      = new JSpinner();

            pnlCampos.add(new JLabel("Id Livro"));         // 1, 1
            pnlCampos.add(spnCodLivro);                         // 1, 2
            pnlCampos.add(new JLabel("Título do Livro:")); // 2, 1
            pnlCampos.add(txtTitulo);					        // 2, 2
            pnlCampos.add(new JLabel("Id Autor:"));		// 3, 1
            pnlCampos.add(spnIdAutor);					        // 3, 2
            pnlCampos.add(new JLabel("Id Área:"));		    // 4, 1
            pnlCampos.add(spnIdArea);					        // 4, 2


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
//                                dadosDoSelect.moveToInsertRow();
//                                dadosDoSelect.updateInt("numDepto", Integer.parseInt(txtNumDepto.getText()));
//                                dadosDoSelect.updateString("nomeDepto", txtNomeDepto.getText());
//                                dadosDoSelect.updateString("gerente_numSegSocial", txtGerente_NSS.getText());
//                                dadosDoSelect.updateDate("gerente_dataInicial", Date.valueOf(txtData_Gerente.getText()));
//                                dadosDoSelect.insertRow();
//                                JOptionPane.showMessageDialog(null, "Inclusão bem sucedida!");
//                                // dadosDoSelect.moveToCurrentRow();	// volta à linha original
//                                // exibirRegistro();			// e a reexibe
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
//                                // não alteraremos a chave primária numDepto
//                                dadosDoSelect.updateString("nomeDepto", txtNomeDepto.getText());
//                                dadosDoSelect.updateString("gerente_numSegSocial",
//                                        txtGerente_NSS.getText());
//                                dadosDoSelect.updateDate("gerente_dataInicial",
//                                        Date.valueOf(txtData_Gerente.getText()));
//                                dadosDoSelect.updateRow();
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
//                                    dadosDoSelect.deleteRow();
//                                    JOptionPane.showMessageDialog(null, "Exclusão bem sucedida!");
//                                    exibirRegistro();   // exibe o próximo registro
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
//                                int posicaoAnterior = dadosDoSelect.getRow();  // registro atual
//                                int chaveProcurada = Integer.parseInt(txtNumDepto.getText());
//                                dadosDoSelect.beforeFirst();      // posiciona antes do 1o registro
//                                boolean achou = false;
//                                while (! achou && dadosDoSelect.next())
//                                {
//                                    if (dadosDoSelect.getInt("numDepto") == chaveProcurada)
//                                        achou = true;
//                                }
//                                if (!achou)
//                                {
//                                    JOptionPane.showMessageDialog(null, "Registro não encontrado!");
//                                    dadosDoSelect.absolute(posicaoAnterior);  // retorna ao registro
//                                    // anteriormente visível
//                                }
//                                exibirRegistro();   // exibe o registro encontrado ou o original
//                            }
//                            catch (SQLException exception)
//                            {
//                                throw new RuntimeException(exception);
//                            }
//                        }
//                    }
//            );
//
//
//            // Navegação entre registros
//
//            btnInicio.addActionListener(
//                    new ActionListener() {
//                        @Override
//                        public void actionPerformed(ActionEvent e) {
//                            try {
//                                if (dadosDoSelect.first()) {
//                                    exibirRegistro();
//                                }
//                                else {
//                                    JOptionPane.showMessageDialog(null, "Não achou Primeiro registro!");
//                                }
//                            }
//                            catch (SQLException ex) {
//                                ex.printStackTrace();
//                            }
//                        }
//                    }
//            );
//
//            btnAnterior.addActionListener(
//                    new ActionListener() {
//                        @Override
//                        public void actionPerformed(ActionEvent e) {
//                            try {
//                                if (dadosDoSelect.previous()) {
//                                    exibirRegistro();
//                                }
//                                else {
//                                    JOptionPane.showMessageDialog(null, "Não achou Registro anterior!");
//                                }
//                            }
//                            catch (SQLException ex) {
//                                ex.printStackTrace();
//                            }
//                        }
//                    }
//            );
//
//            btnProximo.addActionListener(
//                    new ActionListener() {
//                        @Override
//                        public void actionPerformed(ActionEvent e) {
//                            try {
//                                if (dadosDoSelect.next()) {
//                                    exibirRegistro();
//                                }
//                                else {
//                                    JOptionPane.showMessageDialog(null, "Não achou próximo registro!");
//                                }
//                            }
//                            catch (SQLException ex) {
//                                ex.printStackTrace();
//                            }
//                        }
//                    }
//            );
//
//            btnFinal.addActionListener(
//                    new ActionListener() {
//                        @Override
//                        public void actionPerformed(ActionEvent e) {
//                            try {
//                                if (dadosDoSelect.last()) {
//                                    exibirRegistro();
//                                }
//                                else {
//                                    JOptionPane.showMessageDialog(null, "Não achou Último registro!");
//                                }
//                            }
//                            catch (SQLException ex) {
//                                ex.printStackTrace();
//                            }
//                        }
//                    }
//            );
        }

    }

    public class FormExemplares extends JFrame {

    }

    public class FormEmprestimos extends JFrame {

    }

    public class FormDevolucoes extends JFrame {

    }
}
