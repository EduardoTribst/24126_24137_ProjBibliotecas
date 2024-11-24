import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class FormDevolucoes extends JFrame {
    private ResultSet dadosDoSelect;
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

    // conexao e classe controladora
    private Connection conexaoDados;
    public FrameBiblioteca framePrincipal;

    public void setConexaoDados(Connection conexao) {
        conexaoDados = conexao;
    }

    public void preencherDados() {

    }

    public void preencherTabela() {

    }

    public void exibirRegistro() {

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

    public FormDevolucoes(FrameBiblioteca controlador) {
        setTitle("Sistema de Biblioteca | Devoluções");
        setSize(1000, 600);

        // inicializa o frame principal
        framePrincipal = controlador;
    }
}