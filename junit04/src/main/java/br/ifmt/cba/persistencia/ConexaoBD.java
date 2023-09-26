package br.ifmt.cba.persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.h2.tools.Server;

public class ConexaoBD {

    private Connection con;
    private static ConexaoBD instancia;

    private ConexaoBD() throws PersistenciaException {
        try {
            Class.forName("org.h2.Driver");
            String url =  "jdbc:h2:~/test";
            con = DriverManager.getConnection(url,"sa","");
            apagaTabela();
            criaTabelaDados(con);
        } catch (SQLException ex) {
            throw new PersistenciaException("Erro ao conectar o banco de dados - "+ex.toString());
        } catch (ClassNotFoundException ex) {
            throw new PersistenciaException("Driver do banco de dados não localizado - "+ex.toString());
        }
    }

    public void apagaTabela() throws PersistenciaException {
        try {
            // Criar uma declaração SQL
            Statement statement = con.createStatement();

            // Executar o comando SQL para apagar a tabela
            String dropTableSQL = "DROP TABLE IF EXISTS ALUNO";
            statement.execute(dropTableSQL);

            System.out.println("Tabela " + "ALUNO" + " apagada com sucesso.");
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao apagar a tabela - " + e.toString());
        }
    }

    private static void criaTabelaDados(Connection con ) throws SQLException{
            Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082").start();
            System.out.println("Servidor H2 iniciado. Conexões TCP permitidas em localhost:9092");

            if(con != null){
                // Criar uma declaração SQL
                Statement statement = con.createStatement();

                // Executar o script SQL para criar a tabela
                String createTableSQL = "CREATE TABLE ALUNO ( "+
                                                            "matricula SERIAL PRIMARY KEY, "+
                                                            "nome VARCHAR(50) NOT NULL," +
                                                            "nomemae VARCHAR(50) NOT NULL, " +
                                                            "nomepai VARCHAR(50) NOT NULL, "+
                                                            "sexo INTEGER NOT NULL, " +
                                                            "logradouro VARCHAR(50), "+
                                                            "numero INTEGER, " +
                                                            "bairro VARCHAR(40), "+
                                                            "cidade VARCHAR(40),"+ 
                                                            "uf CHAR(2));";
                statement.execute(createTableSQL);

                System.out.println("Tabela criada com sucesso.");
            }
    }

    public static ConexaoBD getInstancia() throws PersistenciaException {
        if (instancia == null) {
            instancia = new ConexaoBD();
            
        }
        return instancia;
    }

    public Connection getConexao() {
        return con;
    }

    public void desconectar() throws PersistenciaException {
        try {
            con.close();
        } catch (SQLException ex) {
            throw new PersistenciaException("Erro ao desconectar o banco de dados - " +ex.toString());
        }
    }
}
