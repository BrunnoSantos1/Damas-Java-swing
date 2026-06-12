package jogo.swing.damas.command;

import jogo.swing.damas.model.BoardModel;
import jogo.swing.damas.model.PecaModel;
import jogo.swing.damas.pecas.DamaState;
import jogo.swing.damas.pecas.TipoPeca;

public class MovimentoCommand implements Command {

    private final BoardModel board;
    private final int oLinha, oColuna;
    private final int dLinha, dColuna;

    // Estado guardado para o Undo (Memento local do comando)
    private PecaModel pecaMovida;
    private PecaModel pecaCapturada;
    private int linhaCapturada, colunaCapturada;
    private TipoPeca estadoAnteriorPecaMovida;
    private boolean promoveuA_Dama = false;
    private boolean executadoComSucesso = false;

    public MovimentoCommand(BoardModel board, int oLinha, int oColuna, int dLinha, int dColuna) {
        this.board = board;
        this.oLinha = oLinha;
        this.oColuna = oColuna;
        this.dLinha = dLinha;
        this.dColuna = dColuna;
    }

    @Override
    public boolean execute() {
        this.pecaMovida = board.getObjetoPeca(oLinha, oColuna);
        if (pecaMovida == null) return false;

        // Guarda o estado de evolução da peça antes do movimento
        this.estadoAnteriorPecaMovida = pecaMovida.getEstadoAtual();

        // Valida o movimento usando a regra intrínseca da peça (State Pattern)
        if (!pecaMovida.validarMovimento(oLinha, oColuna, dLinha, dColuna, board)) {
            return false;
        }

        // 1. Move a peça na matriz lógica
        board.getMatriz()[dLinha][dColuna] = pecaMovida;
        board.getMatriz()[oLinha][oColuna] = null;

        // 2. Se o movimento foi um salto (Captura), remove a peça do meio e guarda ela
        if (Math.abs(dLinha - oLinha) == 2) {
            this.linhaCapturada = oLinha + ((dLinha - oLinha) / 2);
            this.colunaCapturada = oColuna + ((dColuna - oColuna) / 2);
            this.pecaCapturada = board.getObjetoPeca(linhaCapturada, colunaCapturada);

            board.getMatriz()[linhaCapturada][colunaCapturada] = null; // Captura!
        }

        // 3. Verifica e aplica promoção a Dama
        if ((pecaMovida.getCor() == 1 && dLinha == 0) || (pecaMovida.getCor() == 2 && dLinha == 7)) {
            if (!(estadoAnteriorPecaMovida instanceof DamaState)) {
                pecaMovida.setEstadoAtual(new DamaState());
                this.promoveuA_Dama = true;
            }
        }

        this.executadoComSucesso = true;
        return true;
    }

    @Override
    public void undo() {
        if (!executadoComSucesso) return;

        // 1. Devolve a peça para a posição de origem
        board.getMatriz()[oLinha][oColuna] = pecaMovida;
        board.getMatriz()[dLinha][dColuna] = null;

        // 2. Se houve captura, ressuscita a peça comida na coordenada exata
        if (pecaCapturada != null) {
            board.getMatriz()[linhaCapturada][colunaCapturada] = pecaCapturada;
        }

        // 3. Se a peça virou dama nesta jogada, ela perde o chapéu e volta a ser comum
        if (promoveuA_Dama) {
            pecaMovida.setEstadoAtual(estadoAnteriorPecaMovida);
        }
    }
}