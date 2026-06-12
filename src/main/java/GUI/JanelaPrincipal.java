package GUI;

import javax.swing.*;
import java.awt.*;
import jogo.swing.damas.model.GameManager;
import jogo.swing.damas.model.BoardModel;

public class JanelaPrincipal extends JFrame {

    private CardLayout cardLayout;
    private JPanel painelPrincipal;

    private Menu menuPanel;
    private Tabuleiro tableroPanel;
    private Historico historicoPanel;

    public static final String TELA_MENU = "MENU";
    public static final String TELA_TABULEIRO = "TABULEIRO";
    public static final String TELA_HISTORICO = "HISTORICO";

    public JanelaPrincipal() {
        setTitle("Jogo de Damas - Arquitetura MVC");
        setSize(850, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        painelPrincipal = new JPanel(cardLayout);

        // Inicializa as Views no mesmo pacote (GUI)
        menuPanel = new Menu(this);
        tableroPanel = new Tabuleiro(this);
        historicoPanel = new Historico(this);

        painelPrincipal.add(menuPanel, TELA_MENU);
        painelPrincipal.add(tableroPanel, TELA_TABULEIRO);
        painelPrincipal.add(historicoPanel, TELA_HISTORICO);

        add(painelPrincipal);

        // Configura as conexões com os botões da barra lateral
        configurarListenersControle();
    }

    public void mostrarTela(String nomeTela) {
        if (nomeTela.equals(TELA_HISTORICO)) {
            historicoPanel.carregarDadosDoBanco();
        }
        cardLayout.show(painelPrincipal, nomeTela);
    }

    private void configurarListenersControle() {
        tableroPanel.getBtnSalvar().addActionListener(e -> tratarSalvar());
        tableroPanel.getBtnCarregar().addActionListener(e -> tratarCarregar());
        tableroPanel.getBtnDesfazer().addActionListener(e -> tratarDesfazer());
        tableroPanel.getBtnRefazer().addActionListener(e -> tratarRefazer());
    }

    public void iniciarPartida(String modo) {
        // Inicializa o jogo no Singleton do Backend
        GameManager.getInstance().iniciarNovaPartida(modo);

        // Configura os textos da barra lateral de controle
        tableroPanel.configurarNovaPartida(modo);
        mostrarTela(TELA_TABULEIRO);

        // Sincroniza a matriz lógica com o grid visual
        sincronizarTabuleiroGrafico();
    }

    public void processarTentativaMovimento(int oLinha, int oColuna, int dLinha, int dColuna) {
        // Envia as coordenadas para a Máquina de Estados (State Pattern) no Backend
        GameManager.getInstance().receberComandoClique(oLinha, oColuna, dLinha, dColuna);

        // Atualiza a tela imediatamente após o processamento da jogada
        sincronizarTabuleiroGrafico();
    }

    public void tratarDesfazer() {
        GameManager.getInstance().desfazerJogada();
        sincronizarTabuleiroGrafico();
    }

    public void tratarRefazer() {
        GameManager.getInstance().refazerJogada();
        sincronizarTabuleiroGrafico();
    }

    public void tratarSalvar() {
        GameManager.getInstance().salvarPartidaAtual();
        JOptionPane.showMessageDialog(this, "Jogo salvo no banco H2!", "Progresso Salvo", JOptionPane.INFORMATION_MESSAGE);
    }

    public void tratarCarregar() {
        boolean sucesso = GameManager.getInstance().carregarPartidaSalva();
        if (sucesso) {
            sincronizarTabuleiroGrafico();
            JOptionPane.showMessageDialog(this, "Partida restaurada do H2!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Nenhum save encontrado no banco.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Varre o Model lógico e repassa os dados atualizados para atualizar a View.
     */
    public void sincronizarTabuleiroGrafico() {
        GameManager manager = GameManager.getInstance();
        BoardModel boardLogico = manager.getTabuleiro();

        // 1. Atualiza o estado visual das peças no tabuleiro 8x8
        for (int l = 0; l < 8; l++) {
            for (int c = 0; c < 8; c++) {
                int pecaNoBackend = boardLogico.getPecaNaPosicao(l, c);
                tableroPanel.getCasas()[l][c].setPeca(pecaNoBackend);
            }
        }

        // 2. Traduz o estado lógico atual para o texto informativo (State Pattern)
        String textoTurno = "Jogador 1 (Brancas)";
        if (manager.getEstadoAtual() instanceof jogo.swing.damas.model.estado.TurnoBotState) {
            textoTurno = "Bot (Pretas) Pensando...";
        } else if (manager.getEstadoAtual() instanceof jogo.swing.damas.model.estado.FimJogoState) {
            textoTurno = "Partida Encerrada!";
        }

        // 3. Obtém a contagem matemática dinâmica de peças restantes
        int qtdJ1 = boardLogico.contarPecas(1); // 1 = Brancas
        int qtdBot = boardLogico.contarPecas(2); // 2 = Pretas

        // 4. Empurra os dados consolidados para atualizar os JLabels da barra lateral
        tableroPanel.atualizarLabels(textoTurno, qtdJ1, qtdBot);
    }

    public static void main(String[] args) {
        // 1. Inicializa o banco de dados H2 criando as tabelas estruturais locais
        jogo.swing.damas.persistencia.ConexaoFactory.inicializarBanco();

        // 2. Dispara a interface gráfica na Thread correta e segura do Swing (EDT)
        SwingUtilities.invokeLater(() -> {
            new JanelaPrincipal().setVisible(true);
        });
    }
}