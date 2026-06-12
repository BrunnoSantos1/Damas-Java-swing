package jogo.swing.damas.strategy;

import jogo.swing.damas.command.MovimentoCommand;
import jogo.swing.damas.model.BoardModel;

public interface EstrategiaIA {
    /**
     * Analisa o cenário atual do tabuleiro e calcula a melhor jogada para o Bot.
     * @param board O modelo matemático do tabuleiro atual.
     * @param corBot A cor atribuída ao bot (no nosso caso, 2 para Pretas).
     * @return Um objeto MovimentoCommand pronto para ser executado, ou null se não houver jogadas válidas.
     */
    MovimentoCommand calcularMelhorMovimento(BoardModel board, int corBot);
}