package jogo.swing.damas.model.memento;

public class BoardMemento {
    private final String estadoTabuleiroTexto;
    private final String turnoAtual;

    public BoardMemento(String estadoTabuleiroTexto, String turnoAtual) {
        this.estadoTabuleiroTexto = estadoTabuleiroTexto;
        this.turnoAtual = turnoAtual;
    }

    public String getEstadoTabuleiroTexto() {
        return estadoTabuleiroTexto;
    }

    public String getTurnoAtual() {
        return turnoAtual;
    }
}
