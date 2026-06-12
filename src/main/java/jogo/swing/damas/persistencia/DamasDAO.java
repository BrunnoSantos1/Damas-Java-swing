package jogo.swing.damas.persistencia;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DamasDAO {

    // ==========================================
    //          MÉTODOS DE HISTÓRICO
    // ==========================================

    public void salvarPartidaNoHistorico(String modo, String vencedor, String data, String duracao) {
        String sql = "INSERT INTO historico (modo_jogo, vencedor, data_hora, duracao) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexaoFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, modo);
            stmt.setString(2, vencedor);
            stmt.setString(3, data);
            stmt.setString(4, duracao);
            stmt.executeUpdate();
            System.out.println("[DAO] Partida registrada no histórico.");

        } catch (SQLException e) {
            System.err.println("[DAO Erro] Falha ao salvar histórico: " + e.getMessage());
        }
    }

    public List<PartidaDTO> listarHistorico() {
        List<PartidaDTO> lista = new ArrayList<>();
        String sql = "SELECT * FROM historico ORDER BY id DESC";

        try (Connection conn = ConexaoFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(new PartidaDTO(
                        rs.getInt("id"),
                        rs.getString("modo_jogo"),
                        rs.getString("vencedor"),
                        rs.getString("data_hora"),
                        rs.getString("duracao")
                ));
            }
        } catch (SQLException e) {
            System.err.println("[DAO Erro] Falha ao listar histórico: " + e.getMessage());
        }
        return lista;
    }

    // ==========================================
    //       MÉTODOS DO MEMENTO (SAVE / LOAD)
    // ==========================================

    public void salvarEstadoJogo(String estadoTabuleiro, String turnoAtual) {
        // MERGE substitui o registro existente se o ID for 1, agindo como slot único
        String sql = "MERGE INTO save_game (id, estado_tabuleiro, turno_atual, data_salvamento) KEY(id) VALUES (1, ?, ?, CURRENT_TIMESTAMP)";

        try (Connection conn = ConexaoFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, estadoTabuleiro);
            stmt.setString(2, turnoAtual);
            stmt.executeUpdate();
            System.out.println("[DAO] Snapshot do Memento persistido com sucesso.");

        } catch (SQLException e) {
            System.err.println("[DAO Erro] Falha ao salvar estado do jogo: " + e.getMessage());
        }
    }

    public String[] carregarEstadoJogo() {
        String sql = "SELECT estado_tabuleiro, turno_atual FROM save_game WHERE id = 1";

        try (Connection conn = ConexaoFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return new String[] {
                        rs.getString("estado_tabuleiro"),
                        rs.getString("turno_atual")
                };
            }
        } catch (SQLException e) {
            System.err.println("[DAO Erro] Falha ao carregar estado do jogo: " + e.getMessage());
        }
        return null;
    }
}
