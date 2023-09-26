package br.ifmt.cba.execucao;

import java.util.List;
import javax.swing.JOptionPane;

import br.ifmt.cba.negocio.AlunoNegocio;
import br.ifmt.cba.negocio.NegocioException;
import br.ifmt.cba.persistencia.AlunoDAO;
import br.ifmt.cba.persistencia.PersistenciaException;
import br.ifmt.cba.vo.AlunoVO;
import br.ifmt.cba.vo.EnumSexo;
import br.ifmt.cba.vo.EnumUF;


public class Principal {

    private static AlunoNegocio alunoNegocio;

    public static void main(String[] args) {
        try {
            alunoNegocio = new AlunoNegocio(new AlunoDAO());
        } catch (NegocioException nex) {
            System.out.println("Camada de negocio nao iniciada - " + nex.getMessage());
        } catch (PersistenciaException pex) {
            System.out.println("Camada de persistencia nao iniciada - " + pex.getMessage());
        }

        if (alunoNegocio != null) {
            EnumMenu opcao = EnumMenu.Sair;
            do {
                try {
                    opcao = exibirMenu();
                    switch (opcao) {
                        case IncluirAluno:
                            incluirAluno();
                            break;
                        case AlterarAluno:
                            alterarAluno();
                            break;
                        case ExcluirAluno:
                            excluirAluno();
                            break;
                        case PesqMatricula:
                            pesquisarPorMatricula();
                            break;
                        case PesqNome:
                            pesquisarPorNome();
                    }
                } catch (NegocioException ex) {
                    System.out.println("Operacao nao realizada corretamente - " + ex.getMessage());
                }
            } while (opcao != EnumMenu.Sair);
        }
        System.exit(0);
    }

    /**
     * Inclui um novo aluno na base de dados
     *
     * @throws NegocioException
     */
    private static void incluirAluno() throws NegocioException {

        AlunoVO alunoTemp = lerDados();
        alunoNegocio.inserir(alunoTemp);
    }

    /**
     * Permite a alteracao dos dados de um aluno por meio da matricula
     * fornecida.
     *
     * @throws NegocioException
     */
    private static void alterarAluno() throws NegocioException {
        int matricula = 0;
        try {
            matricula = Integer.parseInt(JOptionPane.showInputDialog(null, "Forneca a matricula do Aluno", "Leitura de Dados", JOptionPane.QUESTION_MESSAGE));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Digitacao inconsistente - " + ex.getMessage());
        }
        
        AlunoVO alunoVO = alunoNegocio.pesquisaMatricula(matricula);
        if (alunoVO != null) {
            AlunoVO alunoTemp = lerDados(alunoVO);
            alunoNegocio.alterar(alunoTemp);
        } else {
            JOptionPane.showMessageDialog(null, "Aluno nao localizado");
        }
    }

    /**
     * Exclui um aluno por meio de uma matricula fornecida.
     *
     * @throws NegocioException
     */
    private static void excluirAluno() throws NegocioException {
        int matricula = 0;
        try {
            matricula = Integer.parseInt(JOptionPane.showInputDialog(null, "Forneca a matricula do Aluno", "Leitura de Dados", JOptionPane.QUESTION_MESSAGE));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Digitacao inconsistente - " + ex.getMessage());
        }
        
        AlunoVO alunoVO = alunoNegocio.pesquisaMatricula(matricula);
        if (alunoVO != null) {
            alunoNegocio.excluir(alunoVO.getMatricula());
        } else {
            JOptionPane.showMessageDialog(null, "Aluno nao localizado");
        }
    }

    /**
     * Pesquisa um aluno por meio da matricula.
     *
     * @throws NegocioException
     */
    private static void pesquisarPorMatricula() throws NegocioException {
        int matricula = 0;
        try {
            matricula = Integer.parseInt(JOptionPane.showInputDialog(null, "Forneça a matricula do Aluno", "Leitura de Dados", JOptionPane.QUESTION_MESSAGE));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Digitacao inconsistente - " + ex.getMessage());
        }

        AlunoVO alunoVO = alunoNegocio.pesquisaMatricula(matricula);
        if (alunoVO != null) {
            mostrarDados(alunoVO);
        } else {
            JOptionPane.showMessageDialog(null, "Aluno nao localizado");
        }
    }

    /**
     * Le um nome ou parte de um nome de um aluno e busca no banco de dados
     * alunos que possuem esse nome, ou que iniciam com a parte do nome
     * fornecida. Caso nao seja fornecido nenhum valor de entrada sera retornado
     * os 10 primmeiros alunos ordenados pelo nome.
     *
     * @throws NegocioException
     */
    private static void pesquisarPorNome() throws NegocioException {

        String nome = JOptionPane.showInputDialog(null, "Forneça o nome do Aluno", "Leitura de Dados", JOptionPane.QUESTION_MESSAGE);
        if (nome != null) {
            List<AlunoVO> listaAlunoVO = alunoNegocio.pesquisaParteNome(nome);

            if (listaAlunoVO.size() > 0) {
                for (AlunoVO alunoVO : listaAlunoVO) {
                    mostrarDados(alunoVO);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Aluno nao localizado");
            }
        }else{
            JOptionPane.showMessageDialog(null, "Nome nao pode ser nulo");
        }
    }

    /**
     * Exibe no console da aplicacao os dados dos alunos recebidos pelo
     * parametro alunoVO.
     *
     * @param alunoVO
     */
    private static void mostrarDados(AlunoVO alunoVO) {
        String dados = "-------Resultado da Consulta do Aluno-----";
        if (alunoVO != null) {
            dados += "\n\nMatricula: " + alunoVO.getMatricula();
            dados += "\nNome: " + alunoVO.getNome();
            dados += "\nNome da Mae: " + alunoVO.getNomeMae();
            dados += "\nNome da Pai: " + alunoVO.getNomePai();
            dados += "\nSexo: " + alunoVO.getSexo().name();
            if (alunoVO.getEndereco() != null) {
                dados += "\nLogradouro: " + alunoVO.getEndereco().getLogradouro();
                dados += "\nNumero: " + alunoVO.getEndereco().getNumero();
                dados += "\nBairro: " + alunoVO.getEndereco().getBairro();
                dados += "\nCidade: " + alunoVO.getEndereco().getCidade();
                dados += "\nUF: " + alunoVO.getEndereco().getUf();
            }
        }
        JOptionPane.showMessageDialog(null, dados);
    }

    /**
     * Le os dados de um aluno exibindo os dados atuais recebidos pelo parametro
     * alunoTemp. Na alteracao permite que os dados atuais do alunos sejam
     * visualizados. Na inclusão são exibidos os dados inicializados no AlunoVO.
     *
     * @param alunoTemp
     * @return
     */
    private static AlunoVO lerDados(AlunoVO alunoTemp) {

        String nome, nomeMae, nomePai, logradouro, bairro, cidade;
        int numero;
        EnumSexo sexo;
        EnumUF uf;

        try {
            nome = JOptionPane.showInputDialog("Forneca o nome do Aluno", alunoTemp.getNome().trim());
            alunoTemp.setNome(nome);
            nomeMae = JOptionPane.showInputDialog("Forneca o nome da mae do Aluno", alunoTemp.getNomeMae().trim());
            alunoTemp.setNomeMae(nomeMae);

            nomePai = JOptionPane.showInputDialog("Forneca o nome do pai do Aluno", alunoTemp.getNomePai().trim());
            alunoTemp.setNomePai(nomePai);

            sexo = (EnumSexo) JOptionPane.showInputDialog(null, "Escolha uma Opcaoo", "Leitura de Dados",
                    JOptionPane.QUESTION_MESSAGE, null, EnumSexo.values(), alunoTemp.getSexo());
            alunoTemp.setSexo(sexo);

            logradouro = JOptionPane.showInputDialog("Forneça o logradouro do endereco", alunoTemp.getEndereco().getLogradouro().trim());
            alunoTemp.getEndereco().setLogradouro(logradouro);

            numero = Integer.parseInt(JOptionPane.showInputDialog("Forneça o numero no endereco", alunoTemp.getEndereco().getNumero()));
            alunoTemp.getEndereco().setNumero(numero);

            bairro = JOptionPane.showInputDialog("Forneça o bairro no endereco", alunoTemp.getEndereco().getBairro().trim());
            alunoTemp.getEndereco().setBairro(bairro);

            cidade = JOptionPane.showInputDialog("Forneça a cidade no endereco", alunoTemp.getEndereco().getCidade().trim());
            alunoTemp.getEndereco().setCidade(cidade);

            uf = (EnumUF) JOptionPane.showInputDialog(null, "Escolha uma Opcao", "Leitura de Dados",
                    JOptionPane.QUESTION_MESSAGE, null, EnumUF.values(), alunoTemp.getEndereco().getUf());
            alunoTemp.getEndereco().setUf(uf);
        } catch (Exception ex) {
            System.out.println("Digitacao inconsistente - " + ex.getMessage());
        }
        return alunoTemp;
    }

    /**
     * Cria uma nova intancia de AlunoVO e chama o metodo lerdados(AlunoVO
     * alunoVO).
     *
     * @return
     */
    private static AlunoVO lerDados() {
        AlunoVO alunoTemp = new AlunoVO();
        return lerDados(alunoTemp);
    }

    /**
     * Exibe as opcoes por meio de uma tela de dialogo.
     *
     * @return
     */
    private static EnumMenu exibirMenu() {
        EnumMenu opcao;

        opcao = (EnumMenu) JOptionPane.showInputDialog(null, "Escolha uma Opcao", "Menu",
                JOptionPane.QUESTION_MESSAGE, null, EnumMenu.values(), EnumMenu.values()[0]);
        if (opcao == null) {
            JOptionPane.showMessageDialog(null, "Nenhuma Opcao Escolhida");
            opcao = EnumMenu.Sair;
        }
        return opcao;
    }
}
