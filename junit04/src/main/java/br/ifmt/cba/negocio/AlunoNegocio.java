package br.ifmt.cba.negocio;

import java.util.List;
import br.ifmt.cba.persistencia.AlunoDAO;
import br.ifmt.cba.persistencia.PersistenciaException;
import br.ifmt.cba.vo.AlunoVO;

public class AlunoNegocio {

    private AlunoDAO alunoDAO;

    public AlunoNegocio(AlunoDAO alunoDAO) throws NegocioException {
        if (alunoDAO != null) {
            this.alunoDAO = alunoDAO;
        } else {
            throw new NegocioException("Persistencia nao iniciada ");
        }
    }

    public void inserir(AlunoVO alunoVO) throws NegocioException {

        String mensagemErros = this.validarDados(alunoVO);

        if (!mensagemErros.isEmpty()) {
            throw new NegocioException(mensagemErros);
        }

        try {
            if (alunoDAO.incluir(alunoVO) == 0) {
                throw new NegocioException("Inclusao nao realizada!!");
            }
        } catch (PersistenciaException ex) {
            throw new NegocioException("Erro ao incluir o aluno - " + ex.getMessage());
        }
    }

    public void alterar(AlunoVO alunoVO) throws NegocioException {
        String mensagemErros = this.validarDados(alunoVO);
        if (!mensagemErros.isEmpty()) {
            throw new NegocioException(mensagemErros);
        }
        
        try {
            if(alunoDAO.buscarPorMatricula(alunoVO.getMatricula()) == null){
                throw new NegocioException("Aluno nao localizdo!!");
            }

            if (alunoDAO.alterar(alunoVO) == 0) {
                throw new NegocioException("Alteracao nao realizada!!");
            }
        } catch (PersistenciaException ex) {
            throw new NegocioException("Erro ao alterar o aluno - " + ex.getMessage());
        }
    }

    public void excluir(int matricula) throws NegocioException {
        try {
            if(alunoDAO.buscarPorMatricula(matricula) == null){
                throw new NegocioException("Aluno nao localizdo!!");
            }
            if (alunoDAO.excluir(matricula) == 0) {
                throw new NegocioException("Exclusao nao realizada!!");
            }
        } catch (PersistenciaException ex) {
            throw new NegocioException("Erro ao excluir o aluno - " + ex.getMessage());
        }
    }

    public List<AlunoVO> pesquisaParteNome(String parteNome) throws NegocioException {
        try {
            return alunoDAO.buscarPorNome(parteNome);
        } catch (PersistenciaException ex) {
            throw new NegocioException("Erro ao pesquisar aluno pelo nome - " + ex.getMessage());
        }
    }

    public AlunoVO pesquisaMatricula(int matricula) throws NegocioException {
        try {
            return alunoDAO.buscarPorMatricula(matricula);
        } catch (PersistenciaException ex) {
            throw new NegocioException("Erro ao pesquisar aluno pela matricula - " + ex.getMessage());
        }
    }

    private String validarDados(AlunoVO alunoVO) {

        String mensagemErros = "";

        if (alunoVO.getNome() == null || alunoVO.getNome().length() == 0) {
            mensagemErros += "Nome do aluno nao pode ser vazio";
        }

        if (alunoVO.getNomeMae() == null || alunoVO.getNomeMae().length() == 0) {
            mensagemErros += "\nNome da mae nao pode ser vazio";
        }

        if (alunoVO.getNomePai() == null || alunoVO.getNomePai().length() == 0) {
            mensagemErros += "\nNome do pai nao pode ser vazio";
        }

        if (alunoVO.getSexo() == null) {
            mensagemErros += "\nSexo nao pode ser nulo";
        }

        if (alunoVO.getEndereco() != null) {
            if (alunoVO.getEndereco().getLogradouro() == null || alunoVO.getEndereco().getLogradouro().length() == 0) {
                mensagemErros += "\nLogradouro nao pode ser vazio";
            }

            if (alunoVO.getEndereco().getNumero() <= 0) {
                mensagemErros += "\nNumero deve ser maior que zero";
            }

            if (alunoVO.getEndereco().getBairro() == null || alunoVO.getEndereco().getBairro().length() == 0) {
                mensagemErros += "\nBairro nao pode ser vazio";
            }

            if (alunoVO.getEndereco().getCidade() == null || alunoVO.getEndereco().getCidade().length() == 0) {
                mensagemErros += "\nCidade nao pode ser vazio";
            }
            if (alunoVO.getEndereco().getUf() == null) {
                mensagemErros += "\nUF nao pode ser vazio";
            }
        }else{
            mensagemErros += "\nEndereco nao pode ser vazio";
        }

        return mensagemErros;
    }
}
