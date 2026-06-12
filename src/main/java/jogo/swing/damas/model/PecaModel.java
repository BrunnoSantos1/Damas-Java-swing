package jogo.swing.damas.model;

import jogo.swing.damas.pecas.PecaComumState;
import jogo.swing.damas.pecas.TipoPeca;

public class PecaModel {
    private final int cor;
    private TipoPeca estadoAtual;

    public PecaModel(int cor) {
        this.cor = cor;
        this.estadoAtual = new PecaComumState();
    }

    public int getCor() { return cor; }
    public TipoPeca getEstadoAtual() { return estadoAtual; }

    public void setEstadoAtual(TipoPeca novoEstado) {
        this.estadoAtual = novoEstado;
    }

    public boolean validarMovimento(int oL, int oC, int dL, int dC, BoardModel board) {
        return estadoAtual.validarMovimento(oL, oC, dL, dC, this.cor, board);
    }
}