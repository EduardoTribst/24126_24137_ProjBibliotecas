import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class FormLivros extends JFrame {
    private ResultSet dadosDoSelect;   // tabela resultante de um select no BD, PARA NAVEGAÇÃO

    private JTextField txtCodLivro, txtTitulo, txtIdAutor, txtIdArea, txtISBN;

    private JTable tabLivro;	// controle que exibe dados em formato tabular (linhas e colunas)

    private DefaultTableModel modelo;

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

    // conexao e classe controladora
    private Connection conexaoDados;
    public FrameBiblioteca framePrincipal;

    public void setConexaoDados(Connection conexao) {
        conexaoDados = conexao;
    }

    public void exibirRegistro() throws SQLException
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

    public void preencherDados() {
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

    public void preencherTabela() throws SQLException {
        DefaultTableModel model = (DefaultTableModel) tabLivro.getModel();

        while(dadosDoSelect.next()) {

            String codLivro = dadosDoSelect.getString(1);
            String titulo = dadosDoSelect.getString(2);
            String autor = dadosDoSelect.getString(3);
            String area = dadosDoSelect.getString(4);
            String isbn = dadosDoSelect.getString(5);
            model.addRow(new String[]{codLivro, titulo, autor, area, isbn});
        }

        tabLivro.setModel(model);
        tabLivro.repaint();
    }

    public void irParaPrimeiroRegistro() {
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


    public FormLivros(FrameBiblioteca controlador) {
        setTitle("Sistema de Biblioteca | Livros");
        setSize(1000, 600);

        // inicializa o frame principal
        framePrincipal = controlador;

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
        modelo = new DefaultTableModel(titulosColunas, 0);
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
                        irParaPrimeiroRegistro();
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