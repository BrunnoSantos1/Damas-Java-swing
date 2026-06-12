package jogo.swing.damas.command;

import java.util.ArrayList;
import java.util.List;

public class CompositeJogada implements Command {
    // Lista que armazena os movimentos individuais da sequência de capturas
    private final List<Command> subJogadas = new ArrayList<>();
    private boolean executadoComSucesso = false;

    /**
     * Adiciona um movimento (uma captura do "combo") ao composite.
     */
    public void adicionarSubJogada(Command comando) {
        subJogadas.add(comando);
    }

    @Override
    public boolean execute() {
        List<Command> executadosAteAgora = new ArrayList<>();

        for (Command cmd : subJogadas) {
            if (cmd.execute()) {
                executadosAteAgora.add(cmd);
            } else {
                // Se qualquer sub-jogada falhar, desfaz tudo o que foi feito para não quebrar o tabuleiro
                for (int i = executadosAteAgora.size() - 1; i >= 0; i--) {
                    executadosAteAgora.get(i).undo();
                }
                return false;
            }
        }

        this.executadoComSucesso = !subJogadas.isEmpty();
        return this.executadoComSucesso;
    }

    @Override
    public void undo() {
        if (!executadoComSucesso) return;

        // Desfaz na ordem INVERSA da execução (da última captura para a primeira)
        for (int i = subJogadas.size() - 1; i >= 0; i--) {
            subJogadas.get(i).undo();
        }
    }

    public boolean isEmpty() {
        return subJogadas.isEmpty();
    }
}