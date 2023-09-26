package br.ifmt.cba.persistencia;

public class DAO {

    protected ConexaoBD conexao;

    public DAO() throws PersistenciaException {
        this.conexao = ConexaoBD.getInstancia();
    }

    public ConexaoBD getConexao() {
        return conexao;
    }

    public void setConexao(ConexaoBD conexao) {
        this.conexao = conexao;
    }
    
    public void desconectarBD() throws PersistenciaException {
        conexao.desconectar();
    }
}
