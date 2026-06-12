package GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Tabuleiro extends JPanel {
    private JanelaPrincipal controller;
    private JLabel lblModo;
    private JLabel lblTurno;
    private JLabel lblPecasJ1;
    private JLabel lblPecasJ2;
    private JPanel painelGrid;
    private CasaBoard[][] casas;

    // Estado visual temporário para controle de cliques (Input da View)
    private CasaBoard casaSelecionada = null;

    private JButton btnDesfazer;
    private JButton btnRefazer;
    private JButton btnSalvar;
    private JButton btnCarregar;

    public Tabuleiro(JanelaPrincipal controller) {
        this.controller = controller;
        setLayout(new BorderLayout());

        // 1. Painel Lateral (Leste)
        JPanel painelLateral = new JPanel();
        painelLateral.setLayout(new BoxLayout(painelLateral, BoxLayout.Y_AXIS));
        painelLateral.setBorder(new EmptyBorder(20, 20, 20, 20));
        painelLateral.setPreferredSize(new Dimension(260, 0));
        painelLateral.setBackground(new Color(230, 230, 230));

        JLabel lblPainelTitulo = new JLabel("PAINEL DE CONTROLE");
        lblPainelTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        lblPainelTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblPainelTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        lblModo = new JLabel("Modo: ");
        lblTurno = new JLabel("Vez de: Jogador 1 (Brancas)");
        lblPecasJ1 = new JLabel("Peças J1: 12");
        lblPecasJ2 = new JLabel("Peças Bot: 12");

        Font infoFont = new Font("Arial", Font.PLAIN, 14);
        lblModo.setFont(new Font("Arial", Font.BOLD, 14));
        lblTurno.setFont(infoFont);
        lblPecasJ1.setFont(infoFont);
        lblPecasJ2.setFont(infoFont);

        lblModo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTurno.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblPecasJ1.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblPecasJ2.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel painelComandos = new JPanel(new GridLayout(1, 2, 10, 0));
        painelComandos.setMaximumSize(new Dimension(240, 35));
        painelComandos.setBackground(new Color(230, 230, 230));

        btnDesfazer = new JButton("← Voltar");
        btnRefazer = new JButton("Avançar →");
        painelComandos.add(btnDesfazer);
        painelComandos.add(btnRefazer);

        JPanel painelPersistencia = new JPanel(new GridLayout(2, 1, 0, 10));
        painelPersistencia.setMaximumSize(new Dimension(240, 80));
        painelPersistencia.setBackground(new Color(230, 230, 230));

        btnSalvar = new JButton("Salvar Jogo");
        btnCarregar = new JButton("Carregar Jogo");
        painelPersistencia.add(btnSalvar);
        painelPersistencia.add(btnCarregar);

        JButton btnVoltarMenu = new JButton("Menu Principal");
        btnVoltarMenu.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnVoltarMenu.setMaximumSize(new Dimension(240, 35));
        btnVoltarMenu.addActionListener(e -> controller.mostrarTela(JanelaPrincipal.TELA_MENU));

        painelLateral.add(lblPainelTitulo);
        painelLateral.add(new JSeparator(JSeparator.HORIZONTAL));
        painelLateral.add(Box.createRigidArea(new Dimension(0, 15)));
        painelLateral.add(lblModo);
        painelLateral.add(Box.createRigidArea(new Dimension(0, 10)));
        painelLateral.add(lblTurno);
        painelLateral.add(Box.createRigidArea(new Dimension(0, 10)));
        painelLateral.add(lblPecasJ1);
        painelLateral.add(Box.createRigidArea(new Dimension(0, 5)));
        painelLateral.add(lblPecasJ2);
        painelLateral.add(Box.createRigidArea(new Dimension(0, 20)));

        painelLateral.add(new JLabel("Jogadas (Command):", SwingConstants.CENTER) {{
            setAlignmentX(Component.CENTER_ALIGNMENT);
            setFont(new Font("Arial", Font.BOLD, 12));
        }});
        painelLateral.add(Box.createRigidArea(new Dimension(0, 5)));
        painelLateral.add(painelComandos);
        painelLateral.add(Box.createRigidArea(new Dimension(0, 20)));

        painelLateral.add(new JLabel("Partida (Memento):", SwingConstants.CENTER) {{
            setAlignmentX(Component.CENTER_ALIGNMENT);
            setFont(new Font("Arial", Font.BOLD, 12));
        }});
        painelLateral.add(Box.createRigidArea(new Dimension(0, 5)));
        painelLateral.add(painelPersistencia);

        painelLateral.add(Box.createVerticalGlue());
        painelLateral.add(btnVoltarMenu);

        add(painelLateral, BorderLayout.EAST);

        // 2. Área do Jogo (Centro)
        painelGrid = new JPanel(new GridLayout(8, 8));
        painelGrid.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        casas = new CasaBoard[8][8];
        inicializarGrid();

        JPanel containerTabuleiro = new JPanel(new GridBagLayout());
        containerTabuleiro.add(painelGrid);
        add(containerTabuleiro, BorderLayout.CENTER);
    }

    private void inicializarGrid() {
        Color corClara = new Color(245, 222, 179); // Bege
        Color corEscura = new Color(139, 69, 19);  // Marrom

        for (int linha = 0; linha < 8; linha++) {
            for (int coluna = 0; coluna < 8; coluna++) {
                boolean casaEscura = (linha + coluna) % 2 != 0;
                casas[linha][coluna] = new CasaBoard(casaEscura ? corEscura : corClara, linha, coluna);

                if (casaEscura) {
                    if (linha < 3) casas[linha][coluna].setPeca(2); // Bot (Pretas)
                    else if (linha > 4) casas[linha][coluna].setPeca(1); // Jogador 1 (Brancas)
                }

                painelGrid.add(casas[linha][coluna]);
            }
        }
    }

    /**
     * Gerencia a máquina de estados simples de cliques na interface gráfica.
     */
    private void tratarCliqueNaCasa(CasaBoard casaClicada) {
        // Se não houver peça selecionada e o jogador clicar em uma casa com sua peça (Peça 1)
        if (casaSelecionada == null) {
            if (casaClicada.getPeca() == 1) {
                casaSelecionada = casaClicada;
                casaSelecionada.setSelecionada(true);
            }
        } else {
            // Se clicar na mesma casa, cancela a seleção
            if (casaSelecionada == casaClicada) {
                casaSelecionada.setSelecionada(false);
                casaSelecionada = null;
            } else {
                // Caso clique em outra casa, envia as coordenadas para o Controller validar o movimento
                int origemLinha = casaSelecionada.getLinha();
                int origemColuna = casaSelecionada.getColuna();
                int destinoLinha = casaClicada.getLinha();
                int destinoColuna = casaClicada.getColuna();

                // Avisa o controller para processar a tentativa de jogada no backend
                controller.processarTentativaMovimento(origemLinha, origemColuna, destinoLinha, destinoColuna);

                // Reseta a seleção visual
                casaSelecionada.setSelecionada(false);
                casaSelecionada = null;
            }
        }
    }

    public void configurarNovaPartida(String modo) {
        lblModo.setText("Modo: " + modo);
    }

    /**
     * Atualiza dinamicamente as informações textuais da barra lateral.
     */
    public void atualizarLabels(String turno, int pecasJ1, int pecasBot) {
        lblTurno.setText("Vez de: " + turno);
        lblPecasJ1.setText("Peças J1: " + pecasJ1);
        lblPecasJ2.setText("Peças Bot: " + pecasBot);
    }

    // --- Getters Únicos e Desambiguados para o Controller (JanelaPrincipal) ---
    public CasaBoard[][] getCasas() { return casas; }
    public JButton getBtnDesfazer() { return btnDesfazer; }
    public JButton getBtnRefazer() { return btnRefazer; }
    public JButton getBtnSalvar() { return btnSalvar; }
    public JButton getBtnCarregar() { return btnCarregar; }


    // --- Classe Interna da Casa Modificada para Eventos e Seleção ---
    public class CasaBoard extends JPanel {
        private int peca = 0; // 0 = vazio, 1 = Branca, 2 = Preta
        private final int linha;
        private final int coluna;
        private boolean selecionada = false; // Borda de destaque se clicada

        public CasaBoard(Color corFundo, int linha, int coluna) {
            this.linha = linha;
            this.coluna = coluna;
            setBackground(corFundo);
            setPreferredSize(new Dimension(60, 60));

            // Adiciona o escutador de cliques do mouse
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    tratarCliqueNaCasa(CasaBoard.this);
                }
            });
        }

        public int getPeca() { return peca; }
        public int getLinha() { return linha; }
        public int getColuna() { return coluna; }

        public void setPeca(int peca) {
            this.peca = peca;
            repaint();
        }

        public void setSelecionada(boolean selecionada) {
            this.selecionada = selecionada;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Desenha borda amarela se a peça estiver selecionada para mover
            if (selecionada) {
                g2d.setColor(Color.YELLOW);
                g2d.setStroke(new BasicStroke(3));
                g2d.drawRect(1, 1, getWidth() - 3, getHeight() - 3);
            }

            if (peca != 0) {
                int margin = 8;
                int size = getWidth() - (margin * 2);

                if (peca == 1) {
                    g2d.setColor(Color.WHITE);
                    g2d.fillOval(margin, margin, size, size);
                    g2d.setColor(Color.GRAY);
                    g2d.drawOval(margin, margin, size, size);
                } else if (peca == 2) {
                    g2d.setColor(new Color(50, 50, 50));
                    g2d.fillOval(margin, margin, size, size);
                    g2d.setColor(Color.BLACK);
                    g2d.drawOval(margin, margin, size, size);
                }
            }
        }
    }
}