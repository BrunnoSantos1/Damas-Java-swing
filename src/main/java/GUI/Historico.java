package GUI;

import jogo.swing.damas.persistencia.DamasDAO;
import jogo.swing.damas.persistencia.PartidaDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class Historico extends JPanel {

    private DefaultTableModel model;
    private JTable tabelaHistorico;
    private DamasDAO damasDAO;

    public Historico(JanelaPrincipal controller) {
        this.damasDAO = new DamasDAO();
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Título da tela
        JLabel lblTitulo = new JLabel("Histórico de Partidas", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        add(lblTitulo, BorderLayout.NORTH);

        // Definição das colunas da tabela
        String[] colunas = {"ID", "Modo de Jogo", "Vencedor", "Data/Hora", "Duração"};

        // Modelo de tabela customizado para bloquear a edição das células
        model = new DefaultTableModel(null, colunas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Impede que o usuário altere os dados visualmente
            }
        };

        tabelaHistorico = new JTable(model);
        tabelaHistorico.setRowHeight(25);
        tabelaHistorico.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        // Envolve a tabela em um painel de rolagem
        JScrollPane scrollPane = new JScrollPane(tabelaHistorico);
        add(scrollPane, BorderLayout.CENTER);

        // Painel inferior para o botão de voltar
        JPanel painelInferior = new JPanel();
        JButton btnVoltar = new JButton("Voltar ao Menu Principal");
        btnVoltar.setFont(new Font("Arial", Font.PLAIN, 16));
        btnVoltar.addActionListener(e -> controller.mostrarTela(JanelaPrincipal.TELA_MENU));

        painelInferior.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        painelInferior.add(btnVoltar);
        add(painelInferior, BorderLayout.SOUTH);
    }

    /**
     * Limpa a tabela e busca os registros atualizados do banco H2.
     */
    public void carregarDadosDoBanco() {
        // Limpa as linhas antigas para evitar duplicidade
        model.setRowCount(0);

        // Recupera a lista de DTOs direto da camada de persistência
        List<PartidaDTO> partidas = damasDAO.listarHistorico();

        // Alimenta o componente visual linha por linha
        for (PartidaDTO partida : partidas) {
            Object[] linha = {
                    partida.getId(),
                    partida.getModoJogo(),
                    partida.getVencedor(),
                    partida.getDataHora(),
                    partida.getDuracao()
            };
            model.addRow(linha);
        }
    }
}