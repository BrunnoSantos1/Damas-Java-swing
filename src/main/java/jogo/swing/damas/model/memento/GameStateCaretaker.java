package jogo.swing.damas.model.memento;

import jogo.swing.damas.persistencia.DamasDAO;
import jogo.swing.damas.model.GameManager;

public class GameStateCaretaker {
    private final DamasDAO dao = new DamasDAO();

    /**
     * Pega o memento e empurra para a tabela do banco de dados.
     */
    public void salvarJogo(BoardMemento memento) {
        System.out.println("[Caretaker] Salvando snapshot no H2...");
        dao.salvarEstadoJogo(memento.getEstadoTabuleiroTexto(), memento.getTurnoAtual());
    }

    /**
     * Puxa os dados brutos do H2 e reconstrói o objeto Memento.
     */
    public BoardMemento carregarJogo() {
        System.out.println("[Caretaker] Buscando snapshot no H2...");
        String[] dados = dao.carregarEstadoJogo();

        if (dados == null) {
            return null; // Nenhum save encontrado no banco
        }

        return new BoardMemento(dados[0], dados[1]);
    }
}
