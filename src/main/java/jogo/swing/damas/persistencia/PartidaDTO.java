package jogo.swing.damas.persistencia;

public class PartidaDTO {
    private int id;
    private String modoJogo;
    private String vencedor;
    private String dataHora;
    private String duracao;

    public PartidaDTO(int id, String modoJogo, String vencedor, String dataHora, String duracao) {
        this.id = id;
        this.modoJogo = modoJogo;
        this.vencedor = vencedor;
        this.dataHora = dataHora;
        this.duracao = duracao;
    }

    // Getters utilizados pelo Componente JTable do Swing
    public int getId() { return id; }
    public String getModoJogo() { return modoJogo; }
    public String getVencedor() { return vencedor; }
    public String getDataHora() { return dataHora; }
    public String getDuracao() { return duracao; }
}
