package jogo.swing.damas.command;

public interface Command {
    /**
     * Executa a jogada no tabuleiro.
     * @return true se a execução foi bem-sucedida.
     */
    boolean execute();

    /**
     * Desfaz a jogada, restaurando perfeitamente o estado anterior do tabuleiro.
     */
    void undo();
}
