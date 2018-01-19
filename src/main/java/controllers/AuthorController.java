package controllers;

import entity.Author;
import managers.AuthorManager;
import model.AuthorDataModule;
import org.richfaces.component.AbstractAutocomplete;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

@ManagedBean(name = "authorController")
@SessionScoped
public class AuthorController {

//    @ManagedProperty(value="#{commonUtil}")
//    private CommonUtil util;

    private final String TAG = "Author";
    private final String COLUMN_NAME = "column_name_authors";

    private Author detailAuthor = new Author();

    private AbstractAutocomplete inputAuthor = null;
    private UIComponent authorInput;

    //    for  creating
    private Author author = new Author();

    //    for multiple selection
    private ArrayList<Long> selectedPks = new ArrayList<>();
    private ArrayList<Author> mAuthors = new ArrayList<>();

//    sorting
    private String sortingColumn = null;
    private HashMap<String, Boolean> mOderMap = new HashMap<>();

    @EJB
    private AuthorManager authorManager;

    @EJB
    private AuthorDataModule authorDataModule;

    private List<Author> getAllAuthors(){
        mAuthors = (ArrayList<Author>) authorManager.getAllAuthors();
        return mAuthors;
    }

    public void newAuthor() {
        authorManager.save(new Author(author.getFirstName(), author.getSecondName(), new Date()));
        author = new Author();
    }

    public String update() throws IOException {
//        Map<String, String> map = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
//        detailAuthor.setSecondName( map.get("author_edit_form:updateSecondName"));
//        detailAuthor.setFirstName(map.get("author_edit_form:updateName"));

        authorManager.update(detailAuthor);

        reload();
        return "author_detail.xhtml";
    }

    public void changeName(ValueChangeEvent e){
        detailAuthor.setFirstName(((UIInput) e.getComponent()).getValue().toString());
    }

    public void changeSecondName(ValueChangeEvent e){
        detailAuthor.setSecondName(((UIInput) e.getComponent()).getValue().toString());
    }

    public String deleteSelected() {
        authorManager.deleteList(selectedPks);
        selectedPks.clear();
    return null;
    }

    public void deleteDetail() throws IOException {
        authorManager.delete(detailAuthor.getId());
        detailAuthor = new Author();

        redirectToManagePage();
//        add redirect
    }

    public void selectPk(long pk) {
        if(selectedPks.contains(pk)){
            selectedPks.remove(pk);
        }else {
            selectedPks.add(pk);
        }
    }

    public List<Author> autocomplate(FacesContext context, UIComponent component, Object input) {
        System.out.println("!WWWWWWW "  + " " + ((String )input).toString());
//        return authors;
        return authorManager.getAutocompleteBySecondName(((String )input).toString());
    }

//    sort
    public String sortBy() {
        Map<String,String> params =
                FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        sortingColumn = params.get(COLUMN_NAME);
        changeOrder(sortingColumn);
        authorDataModule.setSortField(sortingColumn, mOderMap.get(sortingColumn));
        return null;
    }

    private void changeOrder(String columnName){
        if(mOderMap.containsKey(columnName)) {
            mOderMap.put(columnName, !mOderMap.get(columnName));
        } else {
            mOderMap.put(columnName, true);
        }
    }

//    redirect
    public void toDetailPage(long pk) {
        detailAuthor = authorManager.getAuthorByPk(Long.valueOf(pk));
        FacesContext.getCurrentInstance().getApplication().getNavigationHandler()
                .handleNavigation(FacesContext.getCurrentInstance(), null, "author_detail.xhtml");
    }

    public void reload() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
    }

    public void redirectToManagePage() throws IOException {
        FacesContext.getCurrentInstance().getApplication().getNavigationHandler()
                .handleNavigation(FacesContext.getCurrentInstance(), null, "author_manage.xhtml");
    }

    public void changeAuthorInput(ValueChangeEvent e){
        System.out.println("!ttttt1 " + e.toString() );
        System.out.println("!ttttt " + authorInput );
    }

    public void ajaxListener() {
//        System.out.println(event); // Look, (new) value is already set.
        System.out.println("!ttttt "  );
    }

//    getset
    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public ArrayList<Author> getAuthors() {
        mAuthors = (ArrayList<Author>) getAllAuthors();
        return mAuthors;
    }

    public void setAuthors(ArrayList<Author> mAuthors) {
        this.mAuthors = mAuthors;
    }

    public Author getDetailAuthor() {
        return detailAuthor;
    }

    public void setDetailAuthor(Author detailAuthor) {
        this.detailAuthor = detailAuthor;
    }

    public AuthorDataModule getAuthorDataModule() {
        return authorDataModule;
    }

    public String getColumnConstant() {
        return COLUMN_NAME;
    }

    public AbstractAutocomplete getInputAuthor() {
        return inputAuthor;
    }

    public void setInputAuthor(AbstractAutocomplete inputAuthor) {
        this.inputAuthor = inputAuthor;
    }

    public UIComponent getAuthorInput() {
        return authorInput;
    }

    public void setAuthorInput(UIComponent authorInput) {
        this.authorInput = authorInput;
    }
}
