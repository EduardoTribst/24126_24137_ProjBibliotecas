import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class FormEmprestimos extends JFrame {
    // botoes e coisas do tipo
    private ResultSet dadosDoSelectEmprestimos, dadosDoSelectAtrasados;   // tabela resultante de um select no BD, PARA NAVEGAÇÃO

    private JTextField txtIdEmprestimo, txtIdLeitor, txtIdExemplar, txtDataEmprestimo, txtDevolucaoEfetiva, txtDevolucaoPrevista;

    private JTable tabEmprestimos, tabAtrasados;	// controle que exibe dados em formato tabular (linhas e colunas)

    // tab que alterna entre emprestimos e livros em atraso

    private JTabbedPane tabbedPane;

    // acoes crud
    private JToolBar tbBotoes, tbBotoesNavegacao; // armazenará os botões abaixo; será colocado no topo do formulári
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

    // conexao e classe controladora
    private Connection conexaoDados;
    public FrameBiblioteca framePrincipal;

    public void setConexaoDados(Connection conexao) {
        conexaoDados = conexao;
    }

    public void preencherDadosEmprestimo() {
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

    public void preencherDadosAtrasados() {
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

    public void preencherTabelaEmprestimo() {
        // dps faz
    }

    public void preencherTabelaAtrasados() {
        // dps faz
    }

    public void exibirRegistroEmprestimos() throws SQLException {
        if (!dadosDoSelectEmprestimos.rowDeleted()) {
            txtIdEmprestimo.setText(String.valueOf(dadosDoSelectEmprestimos.getInt("idEmprestimo")));
            txtIdLeitor.setText(String.valueOf(dadosDoSelectEmprestimos.getInt("idLeitor")));
            txtIdExemplar.setText(String.valueOf(dadosDoSelectEmprestimos.getInt("idExemplar")));
            txtDataEmprestimo.setText(String.valueOf(dadosDoSelectEmprestimos.getDate("dataEmprestimo")));
            txtDevolucaoEfetiva.setText(String.valueOf(dadosDoSelectEmprestimos.getDate("devolucaoEfetiva")));
            txtDevolucaoPrevista.setText(String.valueOf(dadosDoSelectEmprestimos.getDate("devolucaoPrevista")));
        }
    }

    public void irParaPrimeiroRegistro() {
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

    public FormEmprestimos(FrameBiblioteca controlador) {
        setTitle("Sistema de Biblioteca | Empréstimos");
        setSize(1000, 600);

        // inicializa o frame principal
        framePrincipal = controlador;

        // Adiciorenamos os botões ao JToolBar que os conterá
        tbBotoes          = new JToolBar();  // orientação padrão é HORIZONTAL
        tbBotoesNavegacao = new JToolBar();

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
        tbBotoes         .setLayout(new FlowLayout());
        tbBotoesNavegacao.setLayout((new FlowLayout()));

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
        tbBotoes         .setRollover(true);
        tbBotoesNavegacao.setRollover(true);

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

        tbBotoesNavegacao.add(btnFormLivros);
        tbBotoesNavegacao.add(btnFormExemplares);
        tbBotoesNavegacao.add(btnFormDevolucoes);


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
        tabbedPane.addTab("Listar atrasados"  , pnlAtrasados);

        Container cntPrincipal = getContentPane();
        cntPrincipal.setLayout(new BorderLayout());
        cntPrincipal.add(tbBotoesNavegacao, BorderLayout.NORTH);
        cntPrincipal.add(tabbedPane       , BorderLayout.CENTER);

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
                                dadosDoSelectEmprestimos.moveToInsertRow();
                                dadosDoSelectEmprestimos.updateString("idLeitor", txtIdLeitor.getText());
                                dadosDoSelectEmprestimos.updateString("idExemplar", txtIdExemplar.getText());
                                dadosDoSelectEmprestimos.updateDate("dataEmprestimo", Date.valueOf(txtDataEmprestimo.getText()));
                                dadosDoSelectEmprestimos.updateDate("devolucaoEfetiva", Date.valueOf(txtDevolucaoEfetiva.getText()));
                                dadosDoSelectEmprestimos.updateDate("devolucaoPrevista", Date.valueOf(txtDevolucaoPrevista.getText()));
                                dadosDoSelectEmprestimos.insertRow();
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
                                dadosDoSelectEmprestimos.updateString("idLeitor", txtIdLeitor.getText());
                                dadosDoSelectEmprestimos.updateString("idExemplar", txtIdExemplar.getText());
                                dadosDoSelectEmprestimos.updateDate("dataEmprestimo", Date.valueOf(txtDataEmprestimo.getText()));
                                dadosDoSelectEmprestimos.updateDate("devolucaoEfetiva", Date.valueOf(txtDevolucaoEfetiva.getText()));
                                dadosDoSelectEmprestimos.updateDate("devolucaoPrevista", Date.valueOf(txtDevolucaoPrevista.getText()));
                                dadosDoSelectEmprestimos.updateRow();
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
                                    dadosDoSelectEmprestimos.deleteRow();
                                    JOptionPane.showMessageDialog(null, "Exclusão bem sucedida!");
                                    exibirRegistroEmprestimos();   // exibe o próximo registro
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
                                int posicaoAnterior = dadosDoSelectEmprestimos.getRow();  // registro atual
                                String chaveProcurada = txtIdEmprestimo.getText();
                                dadosDoSelectEmprestimos.beforeFirst();      // posiciona antes do 1o registro
                                boolean achou = false;
                                while (! achou && dadosDoSelectEmprestimos.next())
                                {
                                    if (dadosDoSelectEmprestimos.getString("idEmprestimo").compareTo(chaveProcurada) == 0)
                                        achou = true;
                                }
                                if (!achou)
                                {
                                    JOptionPane.showMessageDialog(null, "Registro não encontrado!");
                                    dadosDoSelectEmprestimos.absolute(posicaoAnterior);  // retorna ao registro
                                    // anteriormente visível
                                }
                                preencherTabelaEmprestimo();   // exibe o registro encontrado ou o original
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
                        irParaPrimeiroRegistro();
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