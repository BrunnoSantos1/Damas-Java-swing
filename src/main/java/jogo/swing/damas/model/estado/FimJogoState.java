package jogo.swing.damas.model.estado;

import jogo.swing.damas.model.GameManager;

public class FimJogoState implements jogo.swing.damas.model.estado.GameState {
    private String resultado;

    public FimJogoState(String resultado) {
        this.resultado = resultado;
    }

    @Override
    public void aoEntrarNoEstado(GameManager manager) {
        System.out.println("[Fim de Jogo] Partida encerrada: " + resultado);
        // Lógica futura: disparar gravação no banco via DamasDAO
    }

    @Override
    public void processarClique(GameManager manager, int oLinha, int oColuna, int dLinha, int dColuna) {
        System.out.println("[Bloqueio] A partida já acabou! Volte ao menu principal.");
    }
}
