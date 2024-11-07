import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;

public class FrameBiblioteca extends JFrame {
    private FormLogin formLogin;
    private FormLivros formLivros;
    private FormExemplares formExemplares;
    private FormEmprestimos formEmprestimos;
    private FormDevolucoes formDevolucoes;

    // toolbar que contém os botões de navegação entre os formulários
    private JToolBar tbBotoesNavegacao;
    // botões para abrir formulários
    private JButton btnFormLivros, btnFormExemplares, btnFormEmprestimos, btnFormDevolucoes;


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


    }

    public class FormLogin extends JFrame {

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
