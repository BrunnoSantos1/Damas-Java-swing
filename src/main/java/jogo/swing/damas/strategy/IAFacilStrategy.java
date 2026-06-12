package jogo.swing.damas.strategy;

import jogo.swing.damas.command.MovimentoCommand;
import jogo.swing.damas.model.BoardModel;
import jogo.swing.damas.model.PecaModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class IAFacilStrategy implements EstrategiaIA {
    private final Random random = new Random();

    @Override
    public MovimentoCommand calcularMelhorMovimento(BoardModel board, int corBot) {
        List<MovimentoCommand> jogadasPossiveis = recuperarTodasJogadasValidas(board, corBot);

        if (jogadasPossiveis.isEmpty()) {
            return null; // Bot não tem movimentos (derrota por bloqueio)
        }

        // Escolhe uma jogada aleatória da lista
        return jogadasPossiveis.get(random.nextInt(jogadasPossiveis.size()));
    }

    /**
     * Mtodo utilitário que varre o grid 8x8 procurando movimentações que respeitem as regras físicas.
     */
    private List<MovimentoCommand> recuperarTodasJogadasValidas(BoardModel board, int corBot) {
        List<MovimentoCommand> validas = new ArrayList<>();

        for (int oL = 0; oL < 8; oL++) {
            for (int oC = 0; oC < 8; oC++) {
                PecaModel peca = board.getObjetoPeca(oL, oC);

                // Se a peça pertence ao Bot
                if (peca != null && peca.getCor() == corBot) {
                    // Testa todas as casas de destino possíveis no tabuleiro
                    for (int dL = 0; dL < 8; dL++) {
                        for (int dC = 0; dC < 8; dC++) {
                            // Cria um comando hipotético e testa a validação do State Pattern da peça
                            if (peca.validarMovimento(oL, oC, dL, dC, board)) {
                                validas.add(new MovimentoCommand(board, oL, oC, dL, dC));
                            }
                        }
                    }
                }
            }
        }
        return validas;
    }
}
