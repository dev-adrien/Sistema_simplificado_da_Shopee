package com.shopee.view.swing;

import com.shopee.dao.UsuarioDAO;
import com.shopee.model.Usuario;
import javax.swing.*;
import java.awt.*;
import java.util.Optional;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {

    // componentes
    private JTextField campoEmail;
    private JPasswordField campoSenha;
    private JButton botaoLogin;

    public LoginFrame() {
        setTitle("Shopee - Autenticação");
        setSize(350, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centraliza a janela no meio do monitor
        setLayout(new BorderLayout()); // Divide a tela em Norte, Sul, Leste, Oeste e Centro

        // Título
        JPanel painelTopo = new JPanel();
        painelTopo.setBackground(Color.ORANGE);
        JLabel titulo = new JLabel("Login na Shopee");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        painelTopo.add(titulo);

        // formulário de login
        JPanel painelFormulario = new JPanel(new GridLayout(2, 2, 10, 10));
        painelFormulario.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        painelFormulario.add(new JLabel("E-mail:"));
        campoEmail = new JTextField();
        painelFormulario.add(campoEmail);
        painelFormulario.add(new JLabel("Senha:"));
        campoSenha = new JPasswordField(); // Esconde a senha com '***'
        painelFormulario.add(campoSenha);

        // botão de login
        JPanel painelBotao = new JPanel();
        botaoLogin = new JButton("Entrar");

        // ação de execução do botão
        botaoLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tentarLogar();
            }
        });
        painelBotao.add(botaoLogin);

        // instanciando componentes na tela
        add(painelTopo, BorderLayout.NORTH);
        add(painelFormulario, BorderLayout.CENTER);
        add(painelBotao, BorderLayout.SOUTH);
    }

    // Método que faz a mágica de conversar com o banco
    private void tentarLogar() {
        String email = campoEmail.getText();
        String senha = new String(campoSenha.getPassword());

        try {
            // chama o dao pra fazer validação
            UsuarioDAO dao = new UsuarioDAO();
            if (dao.validarLogin(email, senha)) {
                // se deu certo, busca quem é o userpara passarmos para a próxima tela
                Optional<Usuario> usuarioLogado = dao.buscarPorEmail(email);

                JOptionPane.showMessageDialog(this, "Bem-vindo, " + usuarioLogado.get().getNome() + "!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

                // esconde a tela de login
                this.dispose();

                // mainframe
                new MainFrame(usuarioLogado.get()).setVisible(true);

            } else {
                JOptionPane.showMessageDialog(this, "E-mail ou senha incorretos!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao conectar no banco de dados.", "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}