package jogo.swing.damas.model.estado;

import jogo.swing.damas.model.GameManager;

public class TurnoBotState implements GameState {

    @Override
    public void aoEntrarNoEstado(GameManager manager) {
        System.out.println("[Estado] Turno do Bot. Processando inteligência artificial...");

        // Simula a IA agindo (Em breve usaremos o StrategyPattern aqui)
        manager.executarJogadaIA();
    }

    @Override
    public void processarClique(GameManager manager, int oLinha, int oColuna, int dLinha, int dColuna) {
        // Bloqueia cliques do usuário na tela caso ele tente clicar enquanto o Bot pensa
        System.out.println("[Bloqueio] Aguarde! O Bot está jogando agora.");
    }
}
