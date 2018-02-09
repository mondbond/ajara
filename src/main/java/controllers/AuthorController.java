package controllers;

import entity.Author;
import exception.AuthorException;
import lombok.Getter;
import lombok.Setter;
import managers.AuthorManager;
import model.AuthorDataModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

@ManagedBean(name = "authorController")
@SessionScoped
public class AuthorController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorController.class);

    private final String COLUMN_NAME = "column_name_author_key";

    private @Getter @Setter Author detailAuthor = new Author();

    //    create
    private @Getter @Setter Author newAuthor = new Author();

    //    for multiple selection
    private @Getter @Setter ArrayList<Long> selectedToDeletePks = new ArrayList<>();

    private @Getter @Setter
    LinkedHashMap<Long, Boolean> selectBox = new LinkedHashMap<>();

    private @Setter @Getter boolean selectAllState;

    @EJB
    private AuthorManager authorManager;

    @EJB
    private @Getter AuthorDataModule authorDataModule;

    /**
     * Inserting new author to database
     * */
    public String insertNewAuthor() throws AuthorException {
        authorManager.save(new Author(newAuthor.getFirstName(), newAuthor.getSecondName()));
        LOGGER.info("IN insertNewAuthor(author = [{}])", newAuthor);
        newAuthor = new Author();
        return "author_manage.xhtml";
    }

    /**
     * Updating detail author to database
     * */
    public String update() throws AuthorException{
        LOGGER.info("IN update(author = [{}])", detailAuthor);
        authorManager.update(detailAuthor);
        return "author_detail.xhtml";
    }

    /**
     * Delete selected authors
     * */
    public void deleteSelected() throws AuthorException {
        LOGGER.info("deleteSelected:(deletedList = [{}])", selectedToDeletePks);
        if(!selectedToDeletePks.isEmpty()) {
            authorManager.deleteList(selectedToDeletePks);
            selectedToDeletePks.clear();
        }
    }

    public void clearSelected() {
        LOGGER.info("clearSelected = [{}]", selectedToDeletePks);
        selectedToDeletePks.clear();
        LOGGER.info("clearSelected = [{}]", selectedToDeletePks);
    }

    /**
     * Delete detail author and redirecting to manage page
     * */
    public void deleteDetail() throws IOException, AuthorException {
        LOGGER.info("deleteDetail:(deleted = [{}])", detailAuthor);
        authorManager.delete(detailAuthor.getId());
        detailAuthor = new Author();
        redirectToManagePage();
    }

    /**
     * Adding and removing selected author to delete list
     * @param pk pk of selected author
     * */
    public void selectPk(long pk) throws AuthorException{
        LOGGER.info("selectPk = [{}]", pk);
        LOGGER.info("selectPkList = [{}]", selectedToDeletePks);
        if(selectedToDeletePks.contains(pk)){
            selectedToDeletePks.remove(pk);
        }else {
            selectedToDeletePks.add(pk);
        }
        LOGGER.info("selectPkList = [{}]", selectedToDeletePks);
    }

    /**
     * Sorting authors by params from RequestParameterMap
     * */
    public String sortBy() {
        Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        authorDataModule.sortBy(params.get(COLUMN_NAME));
        return null;
    }

    /**
     * Redirect to detail author page by pk. Set detailAuthor var
     * @param pk authors pk
     * */
    public void toDetailPage(long pk) throws IOException, AuthorException {
        LOGGER.info("toDetailPage:(pk = [{}])", pk);
        detailAuthor = authorManager.getAuthorByPk(pk);
            FacesContext ctx = FacesContext.getCurrentInstance();

            ExternalContext extContext = ctx.getExternalContext();
            String url = extContext.encodeActionURL(ctx.getApplication().
                    getViewHandler().getActionURL(ctx, "/view/author_detail.xhtml"));

            extContext.redirect(url);
    }

    /**
     * Redirect to manage author page
     * */
    private void redirectToManagePage() throws IOException {
        FacesContext ctx = FacesContext.getCurrentInstance();

        ExternalContext extContext = ctx.getExternalContext();
        String url = extContext.encodeActionURL(ctx.getApplication().
                getViewHandler().getActionURL(ctx, "/view/author_manage.xhtml"));
        extContext.redirect(url);
    }

//    get set
    public String getColumnConstant() {
        return COLUMN_NAME;
    }
}
