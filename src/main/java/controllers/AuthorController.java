package controllers;

import data.entity.Author;
import managers.AuthorManager;
import model.AuthorDataModule;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import java.util.*;

@ManagedBean(name = "authorController")
@SessionScoped
public class AuthorController {

    private final String TAG = "Author";
    private final String COLUMN_NAME = "column_name";

    private Author detailAuthor = null;

    //    for  creating
    private Author author = new Author();

    //    for multiple selection
    private ArrayList<Long> selectedPks = new ArrayList<>();
    private ArrayList<Author> mAuthors = new ArrayList<>();

//    sorting
    private String sortingColumn = null;
    private HashMap<String, Boolean> mOderMap = new HashMap<>();

//    editing
    private HtmlInputText name;
    private HtmlInputText secondName;

    @EJB
    private AuthorManager authorManager;

    @EJB
    private AuthorDataModule authorDataModule;

    @PostConstruct
    void init(){
//        mAuthors = (ArrayList<Author>) getAllAuthors();
    }

    public Author getAuthorByPk(long pk){
        return authorManager.getAuthorByPk(pk);
    }

    private List<Author> getAllAuthors(){
        mAuthors = (ArrayList<Author>) authorManager.getAllAuthors();
        return mAuthors;
    }

    public void newAuthor() {
        authorManager.save(new Author(author.getFirstName(), author.getSecondName(), new Date()));
        author = new Author();
    }

    public void update() {
        detailAuthor.setFirstName(name.getValue().toString());
        detailAuthor.setSecondName(secondName.getValue().toString());
        authorManager.update(detailAuthor);
    }

    public String deleteSelected() {
        authorManager.deleteList(selectedPks);
        selectedPks.clear();
        return null;
    }

    public void deleteDetail(){
        authorManager.delete(detailAuthor.getId());
    }

    public void selectPk(long pk) {
        if(selectedPks.contains(pk)){
            selectedPks.remove(pk);
        }else {
            selectedPks.add(pk);
        }
    }

    private UIComponent getUIComponent(String id) {
        return FacesContext.getCurrentInstance().getViewRoot().findComponent(id) ;
    }

//    sort
    public String sortBy() {
        Map<String,String> params =
                FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        sortingColumn = params.get("columnName");
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
        detailAuthor = getAuthorByPk(Long.valueOf(pk));
        FacesContext.getCurrentInstance().getApplication().getNavigationHandler()
                .handleNavigation(FacesContext.getCurrentInstance(), null, "author_detail.xhtml");
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

    public HtmlInputText getName() {
        return name;
    }

    public void setName(HtmlInputText name) {
        this.name = name;
    }

    public HtmlInputText getSecondName() {
        return secondName;
    }

    public void setSecondName(HtmlInputText secondName) {
        this.secondName = secondName;
    }
}
