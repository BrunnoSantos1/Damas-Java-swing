package jogo.swing.damas.pecas;

import jogo.swing.damas.model.BoardModel;

public class DamaState implements TipoPeca {

    @Override
    public boolean validarMovimento(int oLinha, int oColuna, int dLinha, int dColuna, int corPeca, BoardModel board) {
        int distLinha = Math.abs(dLinha - oLinha);
        int distColuna = Math.abs(dColuna - oColuna);

        if (distLinha != distColuna || distLinha == 0) {
            return false;
        }

        if (board.getPecaNaPosicao(dLinha, dColuna) != 0) {
            return false;
        }

        int passoLinha = (dLinha > oLinha) ? 1 : -1;
        int passoColuna = (dColuna > oColuna) ? 1 : -1;

        int pecasNoCaminho = 0;
        int corPecaDetectada = 0;

        int l = oLinha + passoLinha;
        int c = oColuna + passoColuna;

        while (l != dLinha && c != dColuna) {
            int pecaAtual = board.getPecaNaPosicao(l, c);
            if (pecaAtual != 0) {
                pecasNoCaminho++;
                corPecaDetectada = pecaAtual;
            }
            l += passoLinha;
            c += passoColuna;
        }

        if (pecasNoCaminho == 0) {
            return true;
        }

        if (pecasNoCaminho == 1) {
            return corPecaDetectada != corPeca;
        }

        return false;
    }

    @Override
    public String getNomeEstado() {
        return "DAMA";
    }
}
