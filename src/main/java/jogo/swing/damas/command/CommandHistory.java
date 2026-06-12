package jogo.swing.damas.command;

import java.util.Stack;

public class CommandHistory {
    private final Stack<Command> pilhaDesfazer = new Stack<>();
    private final Stack<Command> pilhaRefazer = new Stack<>();

    /**
     * Adiciona um comando recém-executado ao histórico.
     */
    public void registrarComando(Command cmd) {
        pilhaDesfazer.push(cmd);
        pilhaRefazer.clear(); // Nova jogada limpa a árvore de refazer
    }

    public void desfazer() {
        if (!pilhaDesfazer.isEmpty()) {
            Command cmd = pilhaDesfazer.pop();
            cmd.undo();
            pilhaRefazer.push(cmd); // Guarda no refazer caso o usuário mude de ideia
        }
    }

    public void refazer() {
        if (!pilhaRefazer.isEmpty()) {
            Command cmd = pilhaRefazer.pop();
            cmd.execute();
            pilhaDesfazer.push(cmd);
        }
    }
}
