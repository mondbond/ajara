package controllers;

import data.entity.Author;
import data.entity.Book;
import lombok.Getter;
import lombok.Setter;
import managers.AuthorManager;
import managers.BookManager;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import java.util.*;

@ManagedBean
@SessionScoped
public class BookController {

    private final String COLUMN_NAME = "column_name_books";

    @EJB
    private @Getter BookManager bookManager;

    @EJB
    private @Getter AuthorManager authorManager;

    private @Setter @Getter Book newBook = new Book();
    private @Getter @Setter Book detailBook = new Book();

    //    sorting
    private String sortingColumn = null;
    private HashMap<String, Boolean> mOderMap = new HashMap<>();
    private ArrayList<Long> selectedPks = new ArrayList<>();

    private @Getter @Setter List <Author> autocompleteAuthors = new ArrayList<>();

    public BookController() { }

    public void createBook(Author author){
        ArrayList<Author> authors = (ArrayList<Author>) newBook.getAuthors();
        authors.add(author);
        newBook.setAuthors(authors);
        newBook.setCrateDate(new Date());
        bookManager.save(newBook);
        newBook = new Book();
    }

    public void update() {
        bookManager.update(detailBook);
    }

    public void changeName(ValueChangeEvent e){
        detailBook.setName(((UIInput) e.getComponent()).getValue().toString());
    }

    public void changePublisher(ValueChangeEvent e){
        detailBook.setPublisher(((UIInput) e.getComponent()).getValue().toString());
    }

    public void selectPk(long pk) {
        if(selectedPks.contains(pk)){
            selectedPks.remove(pk);
        }else {
            selectedPks.add(pk);
        }
    }

    public String deleteSelected() {
        bookManager.deleteList(selectedPks);
        selectedPks.clear();
        return null;
    }

    public String sortBy() {
        Map<String,String> params =
                FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        sortingColumn = params.get(COLUMN_NAME);
        changeOrder(sortingColumn);
        bookManager.getDataModule().setSortField(sortingColumn, mOderMap.get(sortingColumn));
        return null;
    }

    private void changeOrder(String columnName){
        if(mOderMap.containsKey(columnName)) {
            mOderMap.put(columnName, !mOderMap.get(columnName));
        } else {
            mOderMap.put(columnName, true);
        }
    }

    public void deleteDetail(){
        bookManager.delete(detailBook.getId());
    }

    public void toDetailPage(long pk) {
        detailBook = bookManager.getBookByPk(Long.valueOf(pk));
        FacesContext.getCurrentInstance().getApplication().getNavigationHandler()
                .handleNavigation(FacesContext.getCurrentInstance(), null, "book_detail.xhtml");
    }

    public List<Long> getBookCountByRating(){
        return bookManager.getCountByRating();
    }

    //    query
    public List<Author> getAutocomplete(String prefix ) {
        return authorManager.getAutocompleteBySecondName(prefix);
    }

    public Author getGeneralAuthor(Book book){
        return book.getAuthors().get(0);
    }

    public String getColumnConstant() {
        return COLUMN_NAME;
    }
}
