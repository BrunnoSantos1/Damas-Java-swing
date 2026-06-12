package jogo.swing.damas.persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexaoFactory {
    // Banco criado em arquivo local no diretório do projeto. Delay para manter em memória enquanto ativo.
    private static final String URL_H2 = "jdbc:h2:./db_damas;DB_CLOSE_DELAY=-1";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver do H2 não foi encontrado no Classpath.", e);
        }
        return DriverManager.getConnection(URL_H2, USER, PASSWORD);
    }

    /**
     * Cria as tabelas do Histórico e do SaveGame (Memento) caso elas não existam.
     */
    public static void inicializarBanco() {
        String tabelaHistorico = "CREATE TABLE IF NOT EXISTS historico (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "modo_jogo VARCHAR(30) NOT NULL, " +
                "vencedor VARCHAR(30) NOT NULL, " +
                "data_hora VARCHAR(30) NOT NULL, " +
                "duracao VARCHAR(20) NOT NULL" +
                ");";

        String tabelaSave = "CREATE TABLE IF NOT EXISTS save_game (" +
                "id INT PRIMARY KEY, " + // ID 1 fixo para simular Slot Único (Quick Save)
                "estado_tabuleiro TEXT NOT NULL, " + // String serializada do Memento
                "turno_atual VARCHAR(20) NOT NULL, " +
                "data_salvamento TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ");";

        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute(tabelaHistorico);
            stmt.execute(tabelaSave);
            System.out.println("[H2 Banco] Tabelas verificadas/inicializadas com sucesso.");
        } catch (SQLException e) {
            System.err.println("[H2 Erro] Falha ao inicializar tabelas: " + e.getMessage());
        }
    }
}
