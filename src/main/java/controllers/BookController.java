package controllers;

import entity.Author;
import entity.Book;
import lombok.Getter;
import lombok.Setter;
import managers.AuthorManager;
import managers.BookManager;
import model.BookDataModule;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIInput;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

@ManagedBean
@SessionScoped
public class BookController {

    private final String COLUMN_NAME = "column_name_books";

    @EJB
    private @Getter BookManager bookManager;

    @EJB
    private @Getter AuthorManager authorManager;

    @EJB
    private @Getter
    BookDataModule dataModule;

    private @Setter @Getter Book newBook = new Book();
    private @Getter @Setter Book detailBook = new Book();


    //    all authors for autocomplete
    private List<Author> allAuthors = new ArrayList<>();
    private @Getter @Setter List<String> authorsForBook = new ArrayList<>();
    //    sorting
    private String sortingColumn = null;
    private HashMap<String, Boolean> mOderMap = new HashMap<>();
    private ArrayList<Long> selectedPks = new ArrayList<>();

    private @Getter @Setter List <Author> autocompleteAuthors = new ArrayList<>();

    public BookController() { }

    public void createBook(){
        ArrayList<Author> authors = new ArrayList<>();
        authorsForBook.forEach(author -> authors.add(authorManager.getAuthorByPk(Long.parseLong(author))));
        newBook.getAuthors().addAll(authors);
        newBook.setCrateDate(new Date());
        bookManager.save(newBook);
        newBook = new Book();
    }

    public void createBook(Author author) throws IOException {
        Map<String, String> map = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        newBook.setName( map.get("add_book_by_author:inputBookName"));
        newBook.setIsbn( map.get("add_book_by_author:inputBookIsbn"));
        newBook.setPublisher( map.get("add_book_by_author:inputBookPublisher"));
        newBook.setPublishYear( Integer.parseInt(map.get("add_book_by_author:inputBookYear")));
        ArrayList<Author> authors = new ArrayList<>();
        authors.add(author);
        newBook.setAuthors(authors);
        newBook.setCrateDate(new Date());
        bookManager.save(newBook);
        newBook = new Book();
        reload();
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
        dataModule.setSortField(sortingColumn, mOderMap.get(sortingColumn));
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

    public void reload() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
    }

    //    query
    public List<Author> autocomplete() {
        if(allAuthors.isEmpty()) {
            allAuthors = authorManager.getAllAuthors();
        }
//        Collection<Author> authors = Collections2.filter(allAuthors, new Predicate<Author>() {
//            @Override
//            public boolean apply(Author author) {
//                if (prefix == null) {
//                    return true;
//                }
//                return author.getSecondName().toLowerCase().startsWith(prefix.toLowerCase());
//            }
//        });
        return allAuthors;
    }

    public void onSelectItem(){

        Map<String,String> params =
                FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String action = params.get("username");
//        authorsForBook.add(author.getId());
    }

    public List<Long> getBookCountByRating(){
        return bookManager.getCountByRating();
    }

    public Author getGeneralAuthor(Book book){
        return book.getAuthors().get(0);
    }

    public String getColumnConstant() {
        return COLUMN_NAME;
    }
}
