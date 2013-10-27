package bean;

import dao.ClienteJpaController;
import dao.exceptions.NonexistentEntityException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.persistence.EntityExistsException;
import javax.persistence.RollbackException;
import modelo.Cliente;
import util.EMF;
import util.FacesUtil;

/**
 *
 * @author ciro
 */
@ManagedBean
@RequestScoped
public class ClienteMB {
    private Cliente cliente = new Cliente();
    private ClienteJpaController dao = new ClienteJpaController(EMF.getEntityManagerFactory());
    private List<Cliente> clientes;
    private String pesquisa;
    
    /**
     * Carrega o Aluno clicado para este bean. Quando o usuário clica num
     * aluno numa tabela, invoca-se este método para caregá-lo neste bean.
     * @param aluno o Aluno que o usuário selecionou.
     */
    public void carregar(Cliente cliente){
        setCliente(cliente);
    }
    
    /**
     * Limpa o formulário de cadastro de alunos.
     * Apenas atribui um novo Aluno para este bean.
     */
    public void limpar(){
        setCliente(new Cliente());
    }
    
    public void excluir(Long id){
        try {
            dao.destroy(id);
            FacesUtil.adicionarMensagem("formCadClientes", "O Cliente foi excluído");
        } catch (NonexistentEntityException ex) {
            FacesUtil.adicionarMensagem("formCadClientes", "Erro: O Cliente não foi "
                    + "cadastrado ou já havia sido excluído");
            Logger.getLogger(ClienteMB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void cadastrar(){
        try{
            if(verificarResponsavel()){
                dao.create(cliente);
                FacesUtil.adicionarMensagem("formCadClientes", "O aluno foi cadastrado");
                cliente = new Cliente();
            } else {
                FacesUtil.adicionarMensagem("formCadClientes", "Preencha os dados"
                        + " do seu responsável");
            }
        } catch (EntityExistsException e) {
            FacesUtil.adicionarMensagem("formCadClientes", "Este Cliente já está cadastrado");
        } catch (RollbackException e) {
            FacesUtil.adicionarMensagem("formCadClientes", "Erro: Algo deu errado "
                    + "no cadastro");
        }
    }
    
    
    
    
    public void alterar(){
        try {
            if(cliente.getId() != null){
                if(verificarResponsavel()){
                    dao.edit(cliente);
                    cliente = new Cliente();
                    FacesUtil.adicionarMensagem("formCadClientes", "O Cliente foi alterado");
                } else {
                    FacesUtil.adicionarMensagem("formCadClientes", "Preencha os dados"
                        + " do seu responsável");
                }
            } else {
                FacesUtil.adicionarMensagem("formCadClientes", "Nenhum aluno foi "
                        + "selecionado. Clique em um Cliente para alterá-lo.");
            }
            
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ClienteMB.class.getName()).log(Level.SEVERE, null, ex);
            FacesUtil.adicionarMensagem("formCadClientes", "Erro: O Cliente não foi "
                    + "cadastrado ou já havia sido excluído");
        } catch (Exception ex) {
            Logger.getLogger(ClienteMB.class.getName()).log(Level.SEVERE, null, ex);
            FacesUtil.adicionarMensagem("formCadClientes", "Erro: Algo deu errado "
                    + "na alteração");
        }
    }
    
    /**
     * Verifica se o responsável dos alunos menores de idade está cadastrado.
     * Caso o aluno tenha menos de 18 anos e não informe o responsável, retorna
     * false. Caso tenha informado o responsável ou seja maior de 18 anos, retorna
     * true.
     */
    public boolean verificarResponsavel(){
        // Indica se o nome foi preenchido
        boolean respOk = !(cliente.getResponsavel().isEmpty());
        // Indica se o parentesco foi preenchido
        boolean parOk = !cliente.getRespParentesco().isEmpty();
        // Indica se o telefone foi preenchido
        boolean telOk = !cliente.getRespTelefone().isEmpty();
                
        if (cliente.maioridade()){
            return true; // Maior de idade
        } else if ( respOk && parOk && telOk ) {
            return true; // Menor de idade com responsável preenchido
        }
        return false;    // Menor de idade sem responsável preenchido
    }
    
    /**
     * Creates a new instance of AlunoMB
     */
    public ClienteMB() {
    }
    
    public void cancelar() {
        setCliente(new Cliente());
    }

    public Cliente getCliente() {
        return cliente;
    }

    /**
     * @param aluno the aluno to set
     */
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    
    /**
     * Pega os alunos filtrando pelo atributo pesquisa. Se o atributo não for
     * preenchido, retorna todos os alunos.
     * @return alunos filtrados pela pesquisa
     */
    public List<Cliente> getClientes(){
        if(pesquisa == null){
            clientes = dao.findClienteEntities();
        } else if(pesquisa.isEmpty()){
            clientes = dao.findClienteEntities();
        } else {
            pesquisarPorNome();
        }
        return clientes;
    }
    
    /**
     * Pesquisa os alunos por nome de acordo com o atributo pesquisa. Não
     * retorna nada. Para acessar os resultados, utilize o atributo alunos deste
     * bean.
     */
    public void pesquisarPorNome(){
        clientes = dao.pesquisarPorNome(pesquisa);
    }
    
    /**
     * Limpa a pesquisa e pega todos os alunos. Não retorna nada. Para acessar
     * os resultados, utilize o atributo alunos deste bean.
     */
    public void getTodos(){
        pesquisa = "";
        getClientes();
    }
    
     public void pesquisar() {
        clientes = dao.findClienteEntities();
    }
    
    /**
     * @return the pesquisa
     */
    public String getPesquisa() {
        return pesquisa;
    }

    /**
     * @param pesquisa the pesquisa to set
     */
    public void setPesquisa(String pesquisa) {
        this.pesquisa = pesquisa;
    }

    /**
     * @param alunos the alunos to set
     */
    public void setAlunos(List<Cliente> clientes) {
        this.clientes = clientes;
    }
}
