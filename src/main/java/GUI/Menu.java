package GUI;

import javax.swing.*;
import java.awt.*;

public class Menu extends JPanel {

    public Menu(JanelaPrincipal controller) {
        setLayout(new GridBagLayout()); // Ideal para centralizar componentes
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título Estilizado
        JLabel lblTitulo = new JLabel("JOGO DE DAMAS", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 36));
        lblTitulo.setForeground(new Color(139, 69, 19)); // Tom de marrom
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 40, 10); // Espaçamento maior abaixo do título
        add(lblTitulo, gbc);

        // Configuração dos Botões (Removido o botão de Modo Local)
        gbc.insets = new Insets(10, 10, 10, 10);
        JButton btnJogarBot = new JButton("Jogar contra Bot");
        JButton btnHistorico = new JButton("Ver Histórico de Partidas");
        JButton btnSair = new JButton("Sair");

        // Estilização simples dos botões
        Font fontBotoes = new Font("Arial", Font.PLAIN, 16);
        JButton[] botoes = {btnJogarBot, btnHistorico, btnSair};
        for (int i = 0; i < botoes.length; i++) {
            botoes[i].setFont(fontBotoes);
            gbc.gridy = i + 1;
            add(botoes[i], gbc);
        }

        // ActionListeners: Disparam chamadas para o Controller (JanelaPrincipal)
        btnJogarBot.addActionListener(e -> controller.iniciarPartida("Bot"));
        btnHistorico.addActionListener(e -> controller.mostrarTela(JanelaPrincipal.TELA_HISTORICO));
        btnSair.addActionListener(e -> System.exit(0));
    }
}