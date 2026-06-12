package jogo.swing.damas.model.estado;

import jogo.swing.damas.model.GameManager;

public interface GameState {
    void aoEntrarNoEstado(GameManager manager);
    void processarClique(GameManager manager, int oLinha, int oColuna, int dLinha, int dColuna);
}
