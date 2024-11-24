import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;

public class FrameBiblioteca extends JFrame {
    public static Connection conexaoDados = null;

    public static FrameBiblioteca form;

    private static FormLogin formLogin;
    private static FormLivros formLivros;
    private static FormExemplares formExemplares;
    private static FormEmprestimos formEmprestimos;
    private static FormDevolucoes formDevolucoes;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                form = new FrameBiblioteca();

                // inicializa os forms
                formLogin       = new FormLogin(form);
                formLivros      = new FormLivros(form);
                formExemplares  = new FormExemplares(form);
                formEmprestimos = new FormEmprestimos(form);
                formDevolucoes  = new FormDevolucoes(form);

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
    }

    public void setConexaoDados(Connection conexao) {
        conexaoDados = conexao;

        formLivros     .setConexaoDados(conexaoDados);
        formExemplares .setConexaoDados(conexaoDados);
        formEmprestimos.setConexaoDados(conexaoDados);
        formDevolucoes .setConexaoDados(conexaoDados);
    }

    public void setBibliotecaEscolhida(int idBibliotecaEscolhida) throws Exception {
        formLivros     .setIdBibliotecaEscolhida(idBibliotecaEscolhida);
        formExemplares .setIdBibliotecaEscolhida(idBibliotecaEscolhida);
        formEmprestimos.setIdBibliotecaEscolhida(idBibliotecaEscolhida);
        formDevolucoes .setIdBibliotecaEscolhida(idBibliotecaEscolhida);
    }

    public void exibirFormLivros() throws SQLException {
        formLivros.setVisible(true);
        formLivros.preencherDados();
        formLivros.irParaPrimeiroRegistro(); // ja chama o exibirRegistro()
        formLivros.preencherTabela();
    }

    public void exibirFormExemplares() throws SQLException {
        formExemplares.setVisible(true);
        formExemplares.preencherDados();
        formExemplares.irParaPrimeiroRegistro(); // ja chama o exibirRegistro()
        formExemplares.preencherTabela();
    }

    public void exibirFormEmprestimos() throws  SQLException {
        formEmprestimos.setVisible(true);
        formEmprestimos.preencherDadosEmprestimo();
        formEmprestimos.irParaPrimeiroRegistro();
        formEmprestimos.preencherTabelaEmprestimo();
        formEmprestimos.preencherDadosAtrasados();
        formEmprestimos.preencherTabelaAtrasados();
    }

    public void exibirFormDevolucoes() throws SQLException {
        formDevolucoes.setVisible(true);
    }
}
