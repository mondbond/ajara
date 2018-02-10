package controllers;

import entity.Author;
import exception.AuthorException;
import lombok.Getter;
import lombok.Setter;
import managers.AuthorManager;
import model.AbstractExtendedModel;
import model.AuthorDataModel;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

@ManagedBean(name = "authorController")
@SessionScoped
public class AuthorController implements AbstractExtendedModel.Sorted {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorController.class);

    private final @Getter String COLUMN_NAME = "column_name_author_key";

    private @Getter @Setter Author detailAuthor = new Author();
    private @Getter @Setter Author newAuthor = new Author();
    private @Getter @Setter ArrayList<Long> selectedToDeletePks = new ArrayList<>();

    @EJB
    private AuthorManager authorManager;

    @EJB
    private @Getter AuthorDataModel authorDataModel;

    /**
     * Inserting new author to database
     * */
    public void insertNewAuthor() throws AuthorException {
        LOGGER.info("IN insertNewAuthor(author = [{}])", newAuthor.toString());
        authorManager.save(newAuthor);
        newAuthor = new Author();
    }

    /**
     * Updating detail author in database
     * */
    public void update() throws AuthorException{
        LOGGER.info("IN update(author = [{}])", detailAuthor);
        authorManager.update(detailAuthor);
    }

    /**
     * Delete selected authors
     * */
    public void deleteSelected() throws AuthorException {
        LOGGER.info("IN deleteSelected:(deletedList = [{}])", selectedToDeletePks);
        if(CollectionUtils.isNotEmpty(selectedToDeletePks)) {
            authorManager.deleteList(selectedToDeletePks);
            selectedToDeletePks.clear();
        }
    }

    public void clearSelected() {
        LOGGER.info("IN clearSelected = [{}]", selectedToDeletePks);
        selectedToDeletePks.clear();
        LOGGER.info("OUT clearSelected = [{}]", selectedToDeletePks);
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
    public void selectToDelete(long pk) throws AuthorException{
        LOGGER.info("IN selectToDelete = [{}]", pk);
        if(selectedToDeletePks.contains(pk)){
            selectedToDeletePks.remove(pk);
        }else {
            selectedToDeletePks.add(pk);
        }
        LOGGER.info("OUT selectPkList = [{}]", selectedToDeletePks);
    }

    @Override
    public void sortBy() {
        Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        authorDataModel.sortBy(params.get(COLUMN_NAME));
    }

//    redirected methods

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
}
