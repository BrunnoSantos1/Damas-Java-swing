package jogo.swing.damas.model.estado;

import jogo.swing.damas.model.GameManager;
import jogo.swing.damas.command.MovimentoCommand;

public class TurnoJogador1State implements GameState {

    @Override
    public void aoEntrarNoEstado(GameManager manager) {}

    @Override
    public void processarClique(GameManager manager, int oLinha, int oColuna, int dLinha, int dColuna) {
        // Cria o objeto de Comando encapsulando a intenção de movimento
        MovimentoCommand comando = new MovimentoCommand(
                manager.getTabuleiro(), oLinha, oColuna, dLinha, dColuna
        );

        // Tenta executar o comando
        if (comando.execute()) {
            // Se funcionou, registra no histórico para habilitar o botão Voltar
            manager.getHistoricoJogadas().registrarComando(comando);

            // Transiciona o turno
            if (manager.getTabuleiro().contarPecas(2) == 0) {
                manager.setEstadoAtual(new FimJogoState("Jogador 1 Venceu!"));
            } else {
                manager.setEstadoAtual(new TurnoBotState());
            }
        } else {
            System.out.println("[Aviso] Movimento bloqueado pelas regras das Damas!");
        }
    }
}
