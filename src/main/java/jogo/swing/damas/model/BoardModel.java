package jogo.swing.damas.model;

import jogo.swing.damas.model.memento.BoardMemento;
import jogo.swing.damas.pecas.DamaState;
import jogo.swing.damas.pecas.PecaComumState;

public class BoardModel {
    private PecaModel[][] matriz;

    public BoardModel() {
        matriz = new PecaModel[8][8];
        inicializarTabuleiroLogico();
    }

    private void inicializarTabuleiroLogico() {
        for (int linha = 0; linha < 8; linha++) {
            for (int coluna = 0; coluna < 8; coluna++) {
                if ((linha + coluna) % 2 != 0) {
                    if (linha < 3) matriz[linha][coluna] = new PecaModel(2); // Pretas (Bot)
                    else if (linha > 4) matriz[linha][coluna] = new PecaModel(1); // Brancas (J1)
                }
            }
        }
    }

    public PecaModel[][] getMatriz() {
        return matriz;
    }

    public int getPecaNaPosicao(int l, int c) {
        if (l < 0 || l >= 8 || c < 0 || c >= 8 || matriz[l][c] == null) return 0;
        return matriz[l][c].getCor();
    }

    public PecaModel getObjetoPeca(int l, int c) {
        if (l < 0 || l >= 8 || c < 0 || c >= 8) return null;
        return matriz[l][c];
    }

    public boolean ejecutarMovimentoFisico(int oL, int oC, int dL, int dC, int corEsperada) {
        PecaModel peca = getObjetoPeca(oL, oC);

        if (peca == null || peca.getCor() != corEsperada) return false;

        if (peca.validarMovimento(oL, oC, dL, dC, this)) {
            matriz[dL][dC] = peca;
            matriz[oL][oC] = null;

            if ((peca.getCor() == 1 && dL == 0) || (peca.getCor() == 2 && dL == 7)) {
                peca.setEstadoAtual(new DamaState());
                System.out.println("[Promoção] Uma peça virou Dama!");
            }

            if (Math.abs(dL - oL) == 2) {
                int meioL = oL + ((dL - oL) / 2);
                int meioC = oC + ((dC - oC) / 2);
                matriz[meioL][meioC] = null;
                System.out.println("[Captura] Peça eliminada na posição: (" + meioL + "," + meioC + ")");
            }

            return true;
        }
        return false;
    }

    public int contarPecas(int cor) {
        int cont = 0;
        for (int l = 0; l < 8; l++) {
            for (int c = 0; c < 8; c++) {
                if (matriz[l][c] != null && matriz[l][c].getCor() == cor) cont++;
            }
        }
        return cont;
    }

    public String exportarEstadoParaTexto() {
        StringBuilder sb = new StringBuilder();
        for (int l = 0; l < 8; l++) {
            for (int c = 0; c < 8; c++) {
                PecaModel peca = matriz[l][c];
                if (peca == null) {
                    sb.append("0"); // Sem peça
                } else {
                    // Formato: Cor_Estado (Ex: 1_COMUM ou 2_DAMA)
                    sb.append(peca.getCor()).append("_").append(peca.getEstadoAtual().getNomeEstado());
                }
                if (c < 7) sb.append(",");
            }
            if (l < 7) sb.append(";");
        }
        return sb.toString();
    }

    public void restaurarEstadoPorTexto(String texto) {
        // Limpa a matriz atual
        this.matriz = new PecaModel[8][8];

        String[] linhas = texto.split(";");
        for (int l = 0; l < 8; l++) {
            String[] colunas = linhas[l].split(",");
            for (int c = 0; c < 8; c++) {
                String token = colunas[c];
                if (!token.equals("0")) {
                    String[] partes = token.split("_");
                    int cor = Integer.parseInt(partes[0]);
                    String tipo = partes[1];

                    PecaModel peca = new PecaModel(cor);
                    if (tipo.equals("DAMA")) {
                        peca.setEstadoAtual(new DamaState());
                    } else {
                        peca.setEstadoAtual(new PecaComumState());
                    }
                    this.matriz[l][c] = peca;
                }
            }
        }
    }
}