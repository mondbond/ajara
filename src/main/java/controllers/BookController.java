package controllers;

import entity.Author;
import entity.Book;
import exception.AuthorException;
import exception.BookException;
import lombok.Getter;
import lombok.Setter;
import managers.AuthorManager;
import managers.BookManager;
import model.BookDataModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.faces.FacesException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ManagedBean
@SessionScoped
public class BookController {
    private static final Logger LOGGER = LoggerFactory.getLogger(BookController.class);

    private final String COLUMN_NAME = "column_name_books_key";

    private @Setter @Getter Book newBook = new Book();
    private @Getter @Setter Book detailBook = new Book();

    private @Setter @Getter String authorAutocomplete = new String();

    private List<Author> aAuthors = new ArrayList<>();

    private @Getter @Setter String hiddenId;

    private @Getter @Setter
    UIInput isbnValidMessage;

    private @Getter @Setter
    String isbnMessage;

    private @Getter @Setter
    String authorIsbnMessage;

    private @Getter @Setter String detailBookAddAuthorId;
    private @Getter @Setter String newBookAddAuthorId;
    private @Getter @Setter String filterAuthorId;
    private @Getter @Setter String filterAuthorMessage;

    private @Getter @Setter String bookAddAuthorAutocomplete;
    private @Getter @Setter String booksAddAuthorAutocomplete;

    private ArrayList<Long> selectedToDeletePks = new ArrayList<>();

    private @Getter @Setter List <Author> autocompleteAuthors = new ArrayList<>();

    @EJB
    private @Getter BookManager bookManager;

    @EJB
    private @Getter AuthorManager authorManager;

    @EJB
    private @Getter BookDataModule dataModule;

    public BookController() { }

    /**
     * Inserting new book
     * */
    public String insertNewBook() throws BookException {
        isbnMessage = "";
        if(bookManager.isUnique("ISBN", newBook.getIsbn())){
            ArrayList<Author> authors = new ArrayList<>();
            newBook.getAuthors().addAll(authors);
            LOGGER.info("insertNewBook:(book = [{}])", newBook);
            bookManager.save(newBook);
            newBook = new Book();
        }else {
            isbnMessage = "ISBN must be unique";
        }
        return "books_manage.xhtml";
    }

    /**
     * Create book by pointed single author from detail author page
     * @param author author of a book
     * */
    public String createBookByAuthor(Author author) throws BookException {
        authorIsbnMessage = "khbjbvh";
        if(bookManager.isUnique("ISBN", newBook.getIsbn())){
            ArrayList<Author> authors = (ArrayList<Author>) newBook.getAuthors();
        authors.add(author);
        newBook.setAuthors(authors);
        LOGGER.info("insertNewBook:(book = [{}], author = [{}])", newBook, author);
        bookManager.save(newBook);
        newBook = new Book();
        }else {
            LOGGER.info("insertNewBook nooooooooo :(book = [{}], author = [{}])", newBook, author);
            authorIsbnMessage = "ISBN must be unique";
        }
        return "author_detail.xhtml";
    }

    /**
     * Get list of authors book by authors pk
     * @param pk authors pk
     * @return BookDataModel instance with filtered by author
     * */
    public BookDataModule getBooksByAuthorPk(Long pk) throws AuthorException {
        LOGGER.info("getBooksByAuthorPk:(pk = [{}]", pk);
        dataModule.setFilteredAuthor(authorManager.getAuthorByPk(pk));
        return dataModule;
    }

    /**
     * Update detail book
     * */
    public void update() throws BookException {
        LOGGER.info("update:(detailBook = [{}]", detailBook);
        bookManager.update(detailBook);
    }

    /**
     * Delete selected books
     * */
    public String deleteSelected() throws BookException {
        LOGGER.info("deleteSelected:(selectedBookList = [{}]", selectedToDeletePks);
        bookManager.deleteList(selectedToDeletePks);
        selectedToDeletePks.clear();
        return null;
    }

    /**
     * Delete detail book and redirect to book manage page
     * */
    public void deleteDetail() throws BookException {
        LOGGER.info("deleteDetail:(detailBook = [{}]", detailBook);
        bookManager.delete(detailBook.getId());
        try {
            redirectToManagePage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteAuthorFromBook(Long pk) throws BookException {
        LOGGER.info("IN deleteAuthorFromBook:(pk = [{}]", pk);
        detailBook.getAuthors().removeIf(author1 -> author1.getId() == pk);
        bookManager.update(detailBook);
    }

    public void addAuthorToBook() throws AuthorException, BookException {
        LOGGER.info("IN addAuthorToBook:");
        boolean hasSame = false;
        if(detailBookAddAuthorId != null && !detailBookAddAuthorId.equals("")){
            Author author = authorManager.getAuthorByPk(Long.parseLong(detailBookAddAuthorId));
            for(Author item : detailBook.getAuthors()){
                if(item.getId() == author.getId()){
                    LOGGER.info("IN addAuthorToBook: id, id = [{}] and [{}]", item.getId(), author.getId());
                    hasSame = true;
                }
            }
            if(!hasSame) {
                detailBook.getAuthors().add(author);
                bookManager.update(detailBook);
                bookAddAuthorAutocomplete = null;
            }
        }
    }


    public void deleteAuthorFromAddBookForm(Long pk){
        LOGGER.info("IN deleteAuthorFromAddBookForm:(pk = [{}]", pk);
        newBook.getAuthors().removeIf(author1 -> author1.getId() == pk);

    }

    public void addAuthorToAddBookForm() throws AuthorException {
        LOGGER.info("IN addAuthorToAddBookForm:");
        boolean hasSame = false;

        if(newBookAddAuthorId != null && !newBookAddAuthorId.equals("")){
            LOGGER.info("IN addAuthorToAddBookForm:   nullll = [{}]", newBookAddAuthorId);
            Author author = authorManager.getAuthorByPk(Long.parseLong(newBookAddAuthorId));
            for(Author item : detailBook.getAuthors()){
                if(item.getId() == author.getId()){
                    LOGGER.info("IN addAuthorToBook: id, id = [{}] and [{}]", item.getId(), author.getId());
                    hasSame = true;
                }
            }
            if(!hasSame) {
                newBook.getAuthors().add(author);
                booksAddAuthorAutocomplete = null;
            }
        }
    }

    /**
     * Get autocompleted authors list by authors second name prefix
     * @param prefix prefix of author
     * */
    public List<Author> autocompleteAuthor(FacesContext context, UIComponent component, Object prefix) throws AuthorException {
        aAuthors = authorManager.getAutocompleteBySecondName(((String) prefix));
        return aAuthors;
    }

    /**
     * Filter books by entered author second name
     * */
    public void filterByAuthor() throws AuthorException {
        LOGGER.info("filterByAuthor " + hiddenId);
        filterAuthorMessage = null;
        if((hiddenId == null || hiddenId.equals("")) && authorAutocomplete.equals("")){
            dataModule.setFilteredRating(null);
            dataModule.setFilteredAuthor(null);
        } else if(hiddenId == null || hiddenId.equals("")){
            filterAuthorMessage = "Choose correct author name";
        }else {
            Author filteredAuthor = authorManager.getAuthorByPk(Long.parseLong(hiddenId));
            dataModule.setFilteredAuthor(filteredAuthor);
            hiddenId = null;
        }
//        if(authorAutocomplete != null && !authorAutocomplete.equals("")){
//            List<Author> authors = new ArrayList<>();
//            Author author2 = aAuthors.stream().filter(author1 -> author1.fullName().equals(authorAutocomplete)).findFirst().get();
//            dataModule.setFilteredAuthor(author2);
//        }
    }

    /**
     * Adding and removing selected book to delete list
     * @param pk pk of selected book
     * */
    public void selectPk(long pk) {
        if(selectedToDeletePks.contains(pk)){
            selectedToDeletePks.remove(pk);
        }else {
            selectedToDeletePks.add(pk);
        }
    }

    /**
     * Sorting authors by params from RequestParameterMap
     * */
    public String sortBy() {
        Map<String,String> params =
                FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        dataModule.sortBy(params.get(COLUMN_NAME));
        return null;
    }

    /**
     * Redirect to detail page by pointed pk
     * @param pk pk of Book
     * */
    public void toDetailPage(long pk) throws BookException {
        LOGGER.info("toDetailPage:(pk = [{}])", pk);
        detailBook = bookManager.getBookByPk(pk);

        try {
            FacesContext ctx = FacesContext.getCurrentInstance();

            ExternalContext extContext = ctx.getExternalContext();
            String url = extContext.encodeActionURL(ctx.getApplication().
                    getViewHandler().getActionURL(ctx, "/view/book_detail.xhtml"));

            extContext.redirect(url);
        } catch (IOException e) {
            throw new FacesException(e);
        }
        FacesContext.getCurrentInstance().getApplication().getNavigationHandler()
                .handleNavigation(FacesContext.getCurrentInstance(), null, "book_detail.xhtml");
    }

    private void unsetBookFilter(){
        dataModule.setFilteredRating(null);
        dataModule.setFilteredAuthor(null);
    }

    /**
     * Redirect to book manage page without filter
     * */
    public void redirectToManagePageAndUnsetFilters() throws IOException {
        LOGGER.info("redirectToManagePageAndUnsetFilters:");
        unsetBookFilter();
        redirectToManagePage();
    }

    /**
     * Redirect to book manage page
     * */
    private void redirectToManagePage() throws IOException {
        try {
            FacesContext ctx = FacesContext.getCurrentInstance();

            ExternalContext extContext = ctx.getExternalContext();
            String url = extContext.encodeActionURL(ctx.getApplication().
                    getViewHandler().getActionURL(ctx, "/view/books_manage.xhtml"));

            extContext.redirect(url);
        } catch (IOException e) {
            throw new FacesException(e);
        }
    }

    /**
     * Get books count by rating from pointed rating to rating -1
     * */
    public Long getBookCountByRating(int rating) throws BookException {
        LOGGER.info("getBookCountByRating:(rating = [{}]", rating);
        return bookManager.getCountByRating(rating-1, rating);
    }

    /**
     * Redirect to book manage page with rating filter
     * @param rating
     * */
    public String toBookManageByRating(int rating) throws IOException {
        LOGGER.info("toBookManageByRating:(rating = [{}])", rating);
        dataModule.setFilteredAuthor(null);
        dataModule.setFilteredRating(rating);
        redirectToManagePage();
        return null;
    }

    public String getColumnConstant() {
        return COLUMN_NAME;
    }
}
