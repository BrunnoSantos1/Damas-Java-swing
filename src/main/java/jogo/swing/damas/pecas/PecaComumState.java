package jogo.swing.damas.pecas;

import jogo.swing.damas.model.BoardModel;

public class PecaComumState implements TipoPeca {

    @Override
    public boolean validarMovimento(int oLinha, int oColuna, int dLinha, int dColuna, int corPeca, BoardModel board) {
        int distLinha = dLinha - oLinha;
        int distColuna = Math.abs(dColuna - oColuna);

        if (Math.abs(distLinha) != distColuna) {
            return false;
        }

        int direcaoPermitida = (corPeca == 1) ? -1 : 1;

        // Movimento Simples (Avançar 1 casa)
        if (distLinha == direcaoPermitida && distColuna == 1) {
            return board.getPecaNaPosicao(dLinha, dColuna) == 0;
        }

        // Movimento de Captura (Avançar 2 casas)
        if (distLinha == (2 * direcaoPermitida) && distColuna == 2) {
            int casaMeioLinha = oLinha + direcaoPermitida;
            int casaMeioColuna = oColuna + ((dColuna - oColuna) / 2);

            int pecaMeio = board.getPecaNaPosicao(casaMeioLinha, casaMeioColuna);
            int pecaDestino = board.getPecaNaPosicao(dLinha, dColuna);

            return pecaDestino == 0 && pecaMeio != 0 && pecaMeio != corPeca;
        }

        return false;
    }

    @Override
    public String getNomeEstado() {
        return "COMUM";
    }
}
