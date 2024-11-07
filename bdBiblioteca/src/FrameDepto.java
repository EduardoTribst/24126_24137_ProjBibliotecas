import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;

public class FrameDepto extends JFrame {

  private JToolBar tbBotoes; // armazenará os botões abaixo; será colocado no topo do formulári
  private JButton btnIncluir, btnSalvar, btnExcluir, btnBuscar, btnProximo, btnAnterior, btnInicio,
      btnFinal, btnCancelar;

  private static ResultSet dadosDoSelect;   // tabela resultante de um select no BD, PARA NAVEGAÇÃO

  private static JTextField txtNumDepto, txtNomeDepto, txtGerente_NSS,
      txtData_Gerente;

  private static JTable tabDepto;	// controle que exibe dados em formato tabular (linhas e colunas)

  // será usada para manter aberta uma conexão ao BD para
  // podermos navegar entre registros e, futuramente, realizar
  // operações CRUD
  static private Connection conexaoDados = null;

  public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        FrameDepto form = new FrameDepto();
        // Adaptador para o fechamento da janela, matando o processo
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

  static private void exibirRegistro() throws SQLException
  {
    if (!dadosDoSelect.rowDeleted())
    {
      txtNumDepto.setText(dadosDoSelect.getString("numDepto"));
      txtNomeDepto.setText(dadosDoSelect.getString("nomeDepto"));
      txtGerente_NSS.setText(dadosDoSelect.getString("gerente_numSegSocial"));
      txtData_Gerente.setText(dadosDoSelect.getString("gerente_dataInicial"));
    }
  }

  private static void preencherDados() {
    String sql = "SELECT * FROM emp.Departamento order by NumDepto";
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

  // construtor do formulário
  public FrameDepto() {
    setTitle("Manutencao de Departamentos - CRUD com JDBC e Swing");
    setSize(1000, 300);
    // apenas chame o evento windowClosing
    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

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

    JPanel pnlGrade = new JPanel();     		 	// colocaremos JTable com os registros da tabela
    JPanel pnlCampos = new JPanel();    		 // colocaremos os campos de digitação de dados
    JPanel pnlMensagem = new JPanel(); 		// colocaremos mensagens para o usuário

    JLabel lbMensagem = new JLabel("Mensagem:");	// Label para exibirmos mensagens
    pnlMensagem.add(lbMensagem);
    pnlMensagem.setLayout(new FlowLayout(FlowLayout.LEFT));

    Container cntForm = getContentPane(); 			// acessa a área de conteúdo do frame
    cntForm.setLayout(new BorderLayout());			// configura o layout da área de conteúdo
    cntForm.add(tbBotoes , BorderLayout.NORTH);		// Toolbar fica na parte superior
    cntForm.add(pnlGrade , BorderLayout.WEST);		// Grade de registros fica à esquerda
    cntForm.add(pnlCampos , BorderLayout.CENTER);		// Painel de campos fica no centro
    cntForm.add(pnlMensagem , BorderLayout.SOUTH);	// Painel de mensagens fica abaixo

    // O código acima poderia ser usado como uma classe base,
    // com esses botões e layouts, para tratar outras entidades
    // de banco de dados (outras tabelas)

    // a partir daqui, temos controles específicos para
    // manutenção de departamentos.

    // https://www.devmedia.com.br/jtable-utilizando-o-componente-em-interfaces-graficas-swing/28857

    // a matriz abaixo não está ligada à tabela de Departamentos ainda
    // será apenas um exemplo para vermos como ficará a janela
    Object [][] dadosDepto = { {0, "", "", ""}, { 1, "", "", ""} };
    String[] titulosColunas = {"Num.Depto","Nome","Gerente","Inicio gerencia"};
    tabDepto = new JTable(dadosDepto,  titulosColunas);
    JScrollPane barraRolagem = new JScrollPane(tabDepto);
    pnlGrade.add(barraRolagem);

    pnlCampos.setLayout(new GridLayout(4, 2));	//  4 linhas e 2 colunas
    txtNumDepto = new JTextField();
    txtNomeDepto = new JTextField();
    txtGerente_NSS  = new JTextField();
    txtData_Gerente  = new JTextField();

    pnlCampos.add(new JLabel("Num. Depto:"));			// 1, 1
    pnlCampos.add(txtNumDepto);					// 1, 2
    pnlCampos.add(new JLabel("Nome:"));				// 2, 1
    pnlCampos.add(txtNomeDepto);					// 2, 2
    pnlCampos.add(new JLabel("Num. Seg. Social Gerente:"));		// 3, 1
    pnlCampos.add(txtGerente_NSS);					// 3, 2
    pnlCampos.add(new JLabel("Data inicial do gerente:"));		// 4, 1
    pnlCampos.add(txtData_Gerente);					// 4, 2

    try {
      conexaoDados = ConexaoBD.getConnection();
      preencherDados();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    // agora codificamos os tratadores de eventos dos botões

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
              dadosDoSelect.updateInt("numDepto", Integer.parseInt(txtNumDepto.getText()));
              dadosDoSelect.updateString("nomeDepto", txtNomeDepto.getText());
              dadosDoSelect.updateString("gerente_numSegSocial", txtGerente_NSS.getText());
              dadosDoSelect.updateDate("gerente_dataInicial", Date.valueOf(txtData_Gerente.getText()));
              dadosDoSelect.insertRow();
              JOptionPane.showMessageDialog(null, "Inclusão bem sucedida!");
              // dadosDoSelect.moveToCurrentRow();	// volta à linha original
              // exibirRegistro();			// e a reexibe
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
              // não alteraremos a chave primária numDepto
              dadosDoSelect.updateString("nomeDepto", txtNomeDepto.getText());
              dadosDoSelect.updateString("gerente_numSegSocial",
                  txtGerente_NSS.getText());
              dadosDoSelect.updateDate("gerente_dataInicial",
                  Date.valueOf(txtData_Gerente.getText()));
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
              int chaveProcurada = Integer.parseInt(txtNumDepto.getText());
              dadosDoSelect.beforeFirst();      // posiciona antes do 1o registro
              boolean achou = false;
              while (! achou && dadosDoSelect.next())
              {
                if (dadosDoSelect.getInt("numDepto") == chaveProcurada)
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

