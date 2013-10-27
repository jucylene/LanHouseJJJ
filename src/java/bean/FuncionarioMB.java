/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import dao.FuncionarioJpaController;
import dao.exceptions.NonexistentEntityException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.persistence.EntityExistsException;
import javax.persistence.RollbackException;
import modelo.Funcionario;
import util.EMF;
import util.FacesUtil;

/**
 *
 * @author HELISMARA
 */
@ManagedBean
@RequestScoped
public class FuncionarioMB {

    private Funcionario fun = new Funcionario();
    private FuncionarioJpaController dao = new FuncionarioJpaController(EMF.getEntityManagerFactory());
    private List<Funcionario> funcs;
    private String pesquisa;
    private Integer v = 0;

    /**
     * Creates a new instance of ProfessorMB
     */
    public FuncionarioMB() {
        pesquisar();
    }

    public void carregar(Funcionario fc) {
        setFun(fc);
    }
    
    public void cadastrar() {
        try {
            for (Funcionario f : dao.findFuncionarioEntities()) {
                if ((f.getLogin().toLowerCase().contains(fun.getLogin()))) {

                    v += 1;
                } 
            }
            if (v == 0) {
                dao.create(fun);
                FacesUtil.adicionarMensagem("formulario", "O funcionario foi cadastrado");
                fun = new Funcionario();
            }else{
            
            //FacesUtil.mensErro("Login", "Login ja existe no sistema!");
            FacesUtil.adicionarMensagem("campoLogin", "Erro:"+" Login já existe no sistema");
            }

        } catch (EntityExistsException e) {
            FacesUtil.adicionarMensagem("formulario", "Este funcionario já está cadastrado");
        } catch (RollbackException e) {
            FacesUtil.adicionarMensagem("formulario", "Erro: Algo deu errado "
                    + "no cadastro");
        }
        pesquisar();
    }
    
    
    
     /**
     * Limpa o formulário de cadastro de alunos.
     * Apenas atribui um novo Aluno para este bean.
     */
    public void alterar() {
        try {
            if (fun.getId() != null) {
                dao.edit(fun);
                fun = new Funcionario();
                FacesUtil.adicionarMensagem("formulario", "O funcionario foi alterado");
            } else {
                FacesUtil.adicionarMensagem("formulario", "Nenhum funcionario foi " + "selecionado. Clique em um funcionario para alterá-lo.");
            }

        } catch (NonexistentEntityException ex) {
            Logger.getLogger(FuncionarioMB.class.getName()).log(Level.SEVERE, null, ex);
            FacesUtil.adicionarMensagem("formulario", "Erro: O funcionario não foi "
                    + "cadastrado ou já havia sido excluído");
        } catch (Exception ex) {
            Logger.getLogger(FuncionarioMB.class.getName()).log(Level.SEVERE, null, ex);
            FacesUtil.adicionarMensagem("formulario", "Erro: Algo deu errado "
                    + "na alteração");
        }
        pesquisar();
    }

    public void cancelar() {
        setFun(new Funcionario());
    }

    public void excluir(Long id) {

        try {
            dao.destroy(id);
            FacesUtil.adicionarMensagem("formulario", "O funcionario foi excluído");
        } catch (NonexistentEntityException ex) {
            FacesUtil.adicionarMensagem("formulario", "Erro: O funcionario não foi "
                    + "cadastrado ou já havia sido excluído");
            Logger.getLogger(FuncionarioMB.class.getName()).log(Level.SEVERE, null, ex);
        }
        pesquisar();
    }

    /**
     * Pesquisa os alunos por nome de acordo com o atributo pesquisa. Não
     * retorna nada. Para acessar os resultados, utilize o atributo alunos deste
     * bean.
     */
    public void pesquisar() {
        funcs = dao.findFuncionarioEntities();
    }

    public void pesquisarPorNome() {
        funcs = dao.pesquisarPorNome(pesquisa);
    }

    public boolean pesquisarPorLogin(String l) {
        return dao.pesquisarPorLogin(pesquisa);
    }

    /**
     * Limpa a pesquisa e pega todos os alunos. Não retorna nada. Para acessar
     * os resultados, utilize o atributo alunos deste bean.
     */
    public void getTodos() {
        pesquisa = "";
        getFuncs();
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
     * @return the prof
     */
    public Funcionario getFun() {
        return fun;
    }

    /**
     * @param prof the prof to set
     */
    public void setFun(Funcionario fun) {
        this.fun = fun;
    }

    /**
     * @return the profs
     */
    public List<Funcionario> getFuncs() {
        return funcs;
    }

    /**
     * @param profs the profs to set
     */
    public void setFuncs(List<Funcionario> funcs) {
        this.funcs = funcs;
    }

    

    /**
     * @return the v
     */
    public Integer getV() {
        return v;
    }

    /**
     * @param v the v to set
     */
    public void setV(Integer v) {
        this.v = v;
    }
    
    
}
