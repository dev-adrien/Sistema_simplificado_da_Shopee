package com.shopee.view.swing;

import java.awt.*;
import javax.swing.*;
import com.shopee.service.UsuarioService;

public class CadastroFrame extends JFrame {

    private JTextField campoNome;
    private JTextField campoEmail;
    private JPasswordField campoSenha;
    private JComboBox<String> comboTipo; // caixa de seleção
    private JButton btnSalvar;
    private JButton btnVoltar;

    public CadastroFrame() {
        setTitle("Cadastro");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // topo
        JPanel painelTopo = new JPanel();
        painelTopo.setBackground(Color.ORANGE);
        JLabel titulo = new JLabel("Cadastro de Usuário");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        painelTopo.add(titulo);

        // formulario
        JPanel painelForm = new JPanel(new GridLayout(4, 2, 10, 15));
        painelForm.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        painelForm.add(new JLabel("Nome completo:"));
        campoNome = new JTextField();
        painelForm.add(campoNome);

        painelForm.add(new JLabel("E-mail:"));
        campoEmail = new JTextField();
        painelForm.add(campoEmail);

        painelForm.add(new JLabel("Senha:"));
        campoSenha = new JPasswordField();
        painelForm.add(campoSenha);

        painelForm.add(new JLabel("Você é:"));
        String[] opcoes = {"cliente", "vendedor"};
        comboTipo = new JComboBox<>(opcoes);
        painelForm.add(comboTipo);

        // botoes no bottom
        JPanel painelBotoes = new JPanel();
        btnVoltar = new JButton("Voltar");
        btnSalvar = new JButton("Finalizar Cadastro");

        painelBotoes.add(btnVoltar);
        painelBotoes.add(btnSalvar);

        // se clicar em Voltar, fecha essa tela e abre o Login de novo
        btnVoltar.addActionListener(e -> {
            this.dispose();
            new LoginFrame().setVisible(true);
        });

        // cadastra o cliente se clicar em salvar
        btnSalvar.addActionListener(e -> {
            try {
                String nome = campoNome.getText();
                String email = campoEmail.getText();
                String senha = new String(campoSenha.getPassword());
                String tipo = (String) comboTipo.getSelectedItem();

                UsuarioService service = new UsuarioService();
                service.cadastrarUsuario(nome, email, senha, tipo);

                JOptionPane.showMessageDialog(this, "Conta criada com sucesso! Faça seu login.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

                //volta pra tela de login
                this.dispose();
                new LoginFrame().setVisible(true);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro no Cadastro", JOptionPane.ERROR_MESSAGE);
            }
        });
        add(painelTopo, BorderLayout.NORTH);
        add(painelForm, BorderLayout.CENTER);
        add(painelBotoes, BorderLayout.SOUTH);
    }
}