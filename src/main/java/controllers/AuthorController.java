package controllers;

import data.entity.Author;
import managers.AuthorManager;
import model.AuthorDataModule;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.util.*;

@ManagedBean(name = "authorController")
@SessionScoped
public class AuthorController {

    private final String TAG = "Author";

    private Author detailAuthor = null;

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

    @PostConstruct
    void init(){
        mAuthors = (ArrayList<Author>) getAllAuthors();
    }

    public Author getAuthorByPk(long pk){
        return authorManager.getAuthorByPk(pk);
    }

    public String getAuthorFullNameByPk(){
        return detailAuthor.getFirstName() + " " + detailAuthor.getSecondName();
    }

    private List<Author> getAllAuthors(){
        mAuthors = (ArrayList<Author>) authorManager.getAllAuthors();
        return mAuthors;
    }

    public void newAuthor() {
        authorManager.save(new Author(author.getFirstName(), author.getSecondName(), new Date()));
        author = new Author();
    }

    public String deleteSelected() {
        authorManager.deleteList(selectedPks);
        selectedPks.clear();
        return null;
    }

    public void selectPk(long pk) {
        if(selectedPks.contains(pk)){
            selectedPks.remove(pk);
        }else {
            selectedPks.add(pk);
        }
    }

    public String sortBy() {
        Map<String,String> params =
                FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        sortingColumn = params.get("columnName");
        changeOrder(sortingColumn);
        return null;
    }

    private void sortBySecondName(){
        if (mOderMap.get(sortingColumn)){
            Collections.sort(mAuthors, new Comparator<Author>() {
                @Override
                public int compare(Author o1, Author o2) {
                    return (int)(o1.getSecondName().compareTo(o2.getSecondName()));
                }
            });
        } else {
            Collections.sort(mAuthors, new Comparator<Author>() {
                @Override
                public int compare(Author o1, Author o2) {
                    return (int)(o2.getSecondName().compareTo(o1.getSecondName()));
                }
            });
        }
    }

    private void sortByPk() {
        if (mOderMap.get(sortingColumn)){
            Collections.sort(mAuthors, new Comparator<Author>() {
                @Override
                public int compare(Author o1, Author o2) {
                    return (int)(o1.getId() - o2.getId());
                }
            });
        } else {
            Collections.sort(mAuthors, new Comparator<Author>() {
                @Override
                public int compare(Author o1, Author o2) {
                    return (int)(o2.getId() - o1.getId());
                }
            });
        }
    }

    private void sortByDate(){
        if (mOderMap.get(sortingColumn)){
            Collections.sort(mAuthors, new Comparator<Author>() {
                @Override
                public int compare(Author o1, Author o2) {
                    return (int)(o1.getCreateDate().compareTo(o2.getCreateDate()));
                }
            });
        } else {
            Collections.sort(mAuthors, new Comparator<Author>() {
                @Override
                public int compare(Author o1, Author o2) {
                    return o2.getCreateDate().compareTo(o1.getCreateDate());
                }
            });
        }
    }

    private void sort() {
        switch (sortingColumn){
            case "pk":
                sortByPk();
                break;
            case "secondName":
                sortBySecondName();
                break;
            case "createDate":
                sortByDate();
                break;
        }
    }

    private void changeOrder(String columnName){

        System.out.println(mOderMap.toString());
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

//    pagination

//    getset
    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public ArrayList<Author> getAuthors() {
        mAuthors = (ArrayList<Author>) getAllAuthors();
        if(sortingColumn!= null) {
            sort();
        }
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
}
