package jogo.swing.damas.model;

import jogo.swing.damas.model.estado.GameState;
import jogo.swing.damas.model.estado.TurnoJogador1State;
import jogo.swing.damas.model.memento.BoardMemento;
import jogo.swing.damas.model.memento.GameStateCaretaker;
import jogo.swing.damas.model.estado.TurnoJogador1State;
import jogo.swing.damas.model.estado.TurnoBotState;
import jogo.swing.damas.model.estado.FimJogoState;
import jogo.swing.damas.strategy.EstrategiaIA;
import jogo.swing.damas.strategy.IAFacilStrategy;
/*import jogo.swing.damas.strategy.IAMediaStrategy;*/
import jogo.swing.damas.command.MovimentoCommand;
import jogo.swing.damas.model.estado.TurnoJogador1State;
import jogo.swing.damas.model.estado.FimJogoState;
import javax.swing.SwingUtilities;

public class GameManager {

    // 1. Instância única e privada (Eager Initialization para Thread-Safety simples)
    private static final GameManager INSTANCE = new GameManager();

    // Atributos lógicos do motor do jogo
    private BoardModel tabuleiro;
    private GameState estadoAtual;
    private String modoJogoAtivo;
    private EstrategiaIA estrategiaIA;

    // 2. Construtor privado para impedir clonagem via operador 'new' externo
    private GameManager() {
        // Inicializa as estruturas básicas quando o motor acorda
        this.tabuleiro = new BoardModel();
        // O jogo sempre começa no estado de turno do Jogador 1 (Brancas)
        this.estadoAtual = new TurnoJogador1State();
    }

    // 3. Ponto unificado de acesso global para o Singleton
    public static GameManager getInstance() {
        return INSTANCE;
    }

    /**
     * Reinicia completamente uma partida, criando um novo tabuleiro limpo.
     */
    public void iniciarNovaPartida(String modo) {
        System.out.println("[GameManager] Inicializando nova partida no modo: " + modo);
        this.modoJogoAtivo = modo;
        this.tabuleiro = new BoardModel();
        /*this.historicoJogadas.reset();*/

        // Instancia a estratégia inicial da IA
        this.estrategiaIA = new IAFacilStrategy();

        setEstadoAtual(new TurnoJogador1State());
    }

    public void setEstrategiaIA(EstrategiaIA novaEstrategia) {
        this.estrategiaIA = novaEstrategia;
        System.out.println("[GameManager] Dificuldade da IA alterada.");
    }


    /**
     * Centralizador de Cliques vindo da Interface Gráfica.
     * Repassa as coordenadas para a Máquina de Estados (State Pattern) processar.
     */
    public void receberComandoClique(int oLinha, int oColuna, int dLinha, int dColuna) {
        // Delega o processamento da jogada estritamente para o estado atual do fluxo
        estadoAtual.processarClique(this, oLinha, oColuna, dLinha, dColuna);
    }

    /**
     * Acionado automaticamente pelo TurnoBotState quando for a vez da IA agir.
     */
    public void executarJogadaIA() {
        new Thread(() -> {
            try {
                // Pequeno delay simulando o "pensamento" do Bot
                Thread.sleep(1000);

                // Invoca a estratégia polimorficamente (Quem calcula é o objeto Strategy plugado)
                MovimentoCommand comandoBot = estrategiaIA.calcularMelhorMovimento(this.tabuleiro, 2);

                // Executa a alteração de estado de forma segura na Thread da Interface Gráfica (EDT)
                SwingUtilities.invokeLater(() -> {
                    if (comandoBot != null && comandoBot.execute()) {
                        // Registra a jogada do bot no histórico de comandos (permite dar undo na jogada dele também!)
                        this.historicoJogadas.registrarComando(comandoBot);

                        // Verifica se o Bot venceu tirando todas as peças brancas (Cor 1)
                        if (this.tabuleiro.contarPecas(1) == 0) {
                            setEstadoAtual(new FimJogoState("O Bot Venceu!"));
                        } else {
                            // Devolve o turno para o Humano
                            setEstadoAtual(new TurnoJogador1State());
                        }
                    } else {
                        // Se a IA retornou null, ela está sem movimentos válidos bloqueada -> Vitória do Humano
                        setEstadoAtual(new FimJogoState("Jogador 1 Venceu por Bloqueio!"));
                    }

                    // Força a JanelaPrincipal a se repintar visualmente
                    // Para isso, precisamos avisar o controller. Como estamos fazendo front-to-back,
                    // certifique-se de que a JanelaPrincipal chame seu mtodo de sincronizar após a IA jogar.
                });

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    // --- Getters e Setters Estruturais ---

    public BoardModel getTabuleiro() {
        return tabuleiro;
    }

    public GameState getEstadoAtual() {
        return estadoAtual;
    }

    // Adicione o atributo no topo do GameManager
    private final jogo.swing.damas.command.CommandHistory historicoJogadas = new jogo.swing.damas.command.CommandHistory();

    // Adicione estes métodos no GameManager para responder aos botões da View
    public void desfazerJogada() {
        historicoJogadas.desfazer();
    }

    public void refazerJogada() {
        historicoJogadas.refazer();
    }

    public jogo.swing.damas.command.CommandHistory getHistoricoJogadas() {
        return this.historicoJogadas;
    }

    /**
     * Altera o estado dinâmico do jogo e dispara o gatilho de entrada do novo estado.
     */
    public void setEstadoAtual(GameState novoEstado) {
        this.estadoAtual = novoEstado;
        // Padrão State clássico: avisa o novo objeto de estado que ele assumiu o controle
        this.estadoAtual.aoEntrarNoEstado(this);
    }

    public String getModoJogoAtivo() {
        return modoJogoAtivo;
    }

    private final GameStateCaretaker caretaker = new GameStateCaretaker();

    /**
     * Salvar Jogo: Captura o estado atual, empacota e grava no banco.
     */
    public void salvarPartidaAtual() {
        String textoTabuleiro = tabuleiro.exportarEstadoParaTexto();

        // Descobre o identificador do estado do turno atual
        String textoTurno = "TURNO_J1";
        if (this.estadoAtual instanceof TurnoBotState) {
            textoTurno = "TURNO_BOT";
        } else if (this.estadoAtual instanceof FimJogoState) {
            textoTurno = "FIM";
        }

        BoardMemento memento = new BoardMemento(textoTabuleiro, textoTurno);
        caretaker.salvarJogo(memento);
    }

    /**
     * Carregar Jogo: Lê do banco, força a reconstrução da matriz e reconfigura o turno.
     */
    public boolean carregarPartidaSalva() {
        BoardMemento memento = caretaker.carregarJogo();
        if (memento == null) {
            return false; // Retorna falso se não havia nada salvo no banco
        }

        // 1. Restaura a física matemática do tabuleiro
        this.tabuleiro.restaurarEstadoPorTexto(memento.getEstadoTabuleiroTexto());

        // 2. Restaura o estado correto do ciclo de vida do jogo
        switch (memento.getTurnoAtual()) {
            case "TURNO_BOT":
                setEstadoAtual(new TurnoBotState());
                break;
            case "FIM":
                setEstadoAtual(new FimJogoState("Partida Carregada Encerrada"));
                break;
            default:
                setEstadoAtual(new TurnoJogador1State());
                break;
        }
        return true;
    }
}