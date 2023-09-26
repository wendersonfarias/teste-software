package br.ifmt.cba.persistencia;

public class PersistenciaException extends Exception {

    public PersistenciaException() {
        super("Erro ocorrido na manipulação do banco de dados");
    }

    public PersistenciaException(String msg) {
        super(msg);
    }
}
