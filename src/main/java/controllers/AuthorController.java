package controllers;

import data.entity.Author;
import managers.AuthorManager;

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

    @PostConstruct
    void init(){
        mAuthors = (ArrayList<Author>) getAllAuthors();
    }

    public String getAuthorName(){
        return authorManager.getAuthorByPk(1).getFirstName();
    }

    public String getAuthorFullNameByPk(){
        Author author = authorManager.getAuthorByPk((long)1);
        return author.getFirstName() + " " + author.getSecondName();
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

    public String sortByNumber() {
        Map<String,String> params =
                FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        sortingColumn = params.get("columnName");
        changeOrder(sortingColumn);

        return null;
    }

    private void sort() {
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

    private void changeOrder(String columnName){

        System.out.println(mOderMap.toString());
        if(mOderMap.containsKey(columnName)){
            mOderMap.put(columnName, !mOderMap.get(columnName));
        } else {
            mOderMap.put(columnName, true);
        }
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
        if(sortingColumn!= null) {
            sort();
        }
        return mAuthors;
    }

    public void setAuthors(ArrayList<Author> mAuthors) {
        this.mAuthors = mAuthors;
    }
}
