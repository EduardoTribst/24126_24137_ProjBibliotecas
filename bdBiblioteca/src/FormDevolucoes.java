import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class FormDevolucoes extends JFrame {
    private ResultSet dadosDoSelect;

    private JTextField txtIdLeitor, txtCodLivro, txtNumeroExemplar;

    // acoes crud
    private JToolBar tbBotoes; // armazenará os botões abaixo; será colocado no topo do formulári
    private JButton btnRealizarDevolucao;

    // botões para abrir formulários
    private JButton btnFormLivros, btnFormExemplares, btnFormEmprestimos;

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


    public FormDevolucoes(FrameBiblioteca controlador) {
        setTitle("Sistema de Biblioteca | Devoluções");
        setSize(1000, 600);

        // inicializa o frame principal
        framePrincipal = controlador;

        // Adiciorenamos os botões ao JToolBar que os conterá
        tbBotoes = new JToolBar();  // orientação padrão é HORIZONTAL

        btnRealizarDevolucao = new JButton("Realizar Devolução", new ImageIcon(getClass().getResource("/resources/add.png")));
        btnRealizarDevolucao.setPreferredSize(new Dimension(90,45));
        btnRealizarDevolucao.setVerticalTextPosition(SwingConstants.BOTTOM);
        btnRealizarDevolucao.setHorizontalTextPosition(SwingConstants.CENTER);
        btnRealizarDevolucao.setFocusPainted(false);

        tbBotoes.add(btnRealizarDevolucao);

        tbBotoes.addSeparator();

        // botoes de navegacao

        btnFormLivros = new JButton("Devoluções");
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

        tbBotoes.add(btnFormExemplares);
        tbBotoes.add(btnFormEmprestimos);
        tbBotoes.add(btnFormLivros);

        tbBotoes.setRollover(true);

        // posicoes no painel

        JPanel pnlCampos = new JPanel();        // colocaremos os campos de digitação de dados
        JPanel pnlMensagem = new JPanel(); 		// colocaremos mensagens para o usuário

        JLabel lbMensagem = new JLabel("Mensagem:");	// Label para exibirmos mensagens
        pnlMensagem.add(lbMensagem);
        pnlMensagem.setLayout(new FlowLayout(FlowLayout.LEFT));

        Container cntForm = getContentPane(); 			     // acessa a área de conteúdo do frame
        cntForm.setLayout(new BorderLayout());			     // configura o layout da área de conteúdo
        cntForm.add(tbBotoes, BorderLayout.NORTH);
        cntForm.add(pnlCampos , BorderLayout.CENTER);	     // Painel de campos fica no centro
        cntForm.add(pnlMensagem , BorderLayout.SOUTH);     	 // Painel de mensagens fica abaixo

        setVisible(false); // deixa invisivel ate o usuario selecionar esse form

        // inicializa os campos de texto

        pnlCampos.setLayout(new GridLayout(3, 2));	//  5 linhas e 2 colunas
        txtIdLeitor       = new JTextField();
        txtCodLivro       = new JTextField();
        txtNumeroExemplar = new JTextField();

        pnlCampos.add(new JLabel("Id leitor"));          // 1, 1
        pnlCampos.add(txtCodLivro);                           // 1, 2
        pnlCampos.add(new JLabel("Código do Livro:"));   // 2, 1
        pnlCampos.add(txtCodLivro);					          // 2, 2
        pnlCampos.add(new JLabel("Numero do exemplar:"));// 3, 1
        pnlCampos.add(txtNumeroExemplar);					  // 3, 2

        // event listeners

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

        btnRealizarDevolucao.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        
                    }
                }
        );
    }
}