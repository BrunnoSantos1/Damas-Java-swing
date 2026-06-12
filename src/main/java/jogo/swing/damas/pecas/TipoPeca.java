package jogo.swing.damas.pecas;

import jogo.swing.damas.model.BoardModel;

public interface TipoPeca {
    /**
     * Valida se o movimento é geometricamente aceitável para o estado atual da peça.
     */
    boolean validarMovimento(int oLinha, int oColuna, int dLinha, int dColuna, int corPeca, BoardModel board);

    /**
     * Retorna o identificador textual do estado da peça.
     */
    String getNomeEstado();
}