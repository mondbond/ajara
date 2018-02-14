package controllers;

import entity.Author;
import entity.Book;
import exception.AuthorException;
import exception.BookException;
import lombok.Getter;
import lombok.Setter;
import managers.AuthorManager;
import managers.BookManager;
import model.AbstractExtendedModel;
import model.BookDataModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.faces.FacesException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ManagedBean
@SessionScoped
public class BookController implements AbstractExtendedModel.Sorted{
    private static final Logger LOGGER = LoggerFactory.getLogger(BookController.class);

    private final @Getter String COLUMN_NAME = "column_name_books_key";

    private @Setter @Getter Book newBook = new Book();
    private @Getter @Setter Book detailBook = new Book();

    private @Setter @Getter String authorAutocomplete = new String();

    private @Getter @Setter String hiddenDetailBookAddAuthorId;
    private @Getter @Setter String hiddenManageBookAddAuthorId;
    private @Getter @Setter String hiddenManageBookAddAuthorIdForFilter;

    private @Getter @Setter String bookAddAuthorAutocompleteValue;
    private @Getter @Setter String booksAddAuthorAutocompleteValue;

    private @Getter @Setter String newBookAddAuthorAutocompleteMessage;
    private @Getter @Setter String detailBookAddAuthorAutocompleteMessage;
    private @Getter @Setter String filterAuthorValidationMessage;

    private @Getter @Setter String isbnValidMessage;
//    private @Getter @Setter String authorIsbnValidMessage;

    private ArrayList<Long> selectedToDeletePks = new ArrayList<>();

    @EJB
    private @Getter BookManager bookManager;

    @EJB
    private @Getter AuthorManager authorManager;

    @EJB
    private @Getter
    BookDataModel dataModule;

    public BookController() { }

    /**
     * Inserting new book
     * */
    public void insertNewBook() throws BookException {
            ArrayList<Author> authors = new ArrayList<>();
            newBook.getAuthors().addAll(authors);
            LOGGER.info("insertNewBook:(book = [{}])", newBook);
            bookManager.save(newBook);
            newBook = new Book();
    }

    /**
     * Create book by pointed single author from detail author page
     * @param author author of a book
     * */
    public void createBookByAuthor(Author author) throws BookException {
            ArrayList<Author> authors = (ArrayList<Author>) newBook.getAuthors();
        authors.add(author);
        newBook.setAuthors(authors);
        LOGGER.info("insertNewBook:(book = [{}], author = [{}])", newBook, author);
        bookManager.save(newBook);
        newBook = new Book();
    }

    /**
     * Get list of authors book by authors pk
     * @param pk authors pk
     * @return BookDataModel instance with filtered by author
     * */
    public BookDataModel getBooksByAuthorPk(Long pk) throws AuthorException {
        LOGGER.info("getBooksByAuthorPk:(pk = [{}]", pk);
        dataModule.setFilteredAuthor(authorManager.getAuthorByPk(pk));
        return dataModule;
    }

    /**
     * Get books count by rating from pointed rating to rating -1
     * */
    public Long getBookCountByRating(int rating) throws BookException {
        LOGGER.info("IN getBookCountByRating:(rating = [{}]", rating);
        return bookManager.getCountByRatingRange(rating-1, rating);
    }

    /**
     * Update detail book
     * */
    public void update() throws BookException {
        isbnValidMessage = "";
        Book sameBookInDb = bookManager.getBookByPk(detailBook.getId());
        if(!sameBookInDb.getIsbn().equals( detailBook.getIsbn())){
            if(bookManager.isUnique(Book.getISBN_COLUMN(), detailBook.getIsbn())){
                LOGGER.info("update:(detailBook = [{}]", detailBook);
                detailBook.setAuthors(detailBook.getAuthors().stream().distinct().collect(Collectors.toList()));
                bookManager.update(detailBook);
            } else {
                isbnValidMessage = "ISBN must be unique";
            }
        }else {
            detailBook.setAuthors(detailBook.getAuthors().stream().distinct().collect(Collectors.toList()));
            bookManager.update(detailBook);
        }
    }

    /**
     * Delete selected books
     * */
    public String deleteSelected() throws BookException {
        LOGGER.info("deleteSelected:(selectedBookList = [{}]", selectedToDeletePks);
        if(!selectedToDeletePks.isEmpty()) {
            bookManager.deleteList(selectedToDeletePks);
            selectedToDeletePks.clear();
        }
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
        } catch (Exception e) {
            throw new BookException("Something wrong");
        }
    }

    public void addAuthorToBook() throws AuthorException {
        LOGGER.info("IN addAuthorToBook:");
        bookAddAuthorAutocompleteValue = null;
        detailBookAddAuthorAutocompleteMessage = null;
        if(hiddenDetailBookAddAuthorId != null && !hiddenDetailBookAddAuthorId.equals("")){
            Author author = authorManager.getAuthorByPk(Long.parseLong(hiddenDetailBookAddAuthorId));
            if(isHasSameBook(detailBook, author)){
                detailBookAddAuthorAutocompleteMessage = "You already select this author";
            }else {
                detailBook.getAuthors().add(author);
            }
        }else {
            detailBookAddAuthorAutocompleteMessage = "Field must be choosed from autocomplete form";
        }
        hiddenDetailBookAddAuthorId = null;
    }

    public void addAuthorToAddNewBookForm() throws AuthorException {
        LOGGER.info("IN addAuthorToAddNewBookForm:");
        booksAddAuthorAutocompleteValue = null;
        newBookAddAuthorAutocompleteMessage = "";
        if(hiddenManageBookAddAuthorId != null && !hiddenManageBookAddAuthorId.equals("")){
            Author author = authorManager.getAuthorByPk(Long.parseLong(hiddenManageBookAddAuthorId));
            if(isHasSameBook(newBook, author)){
                newBookAddAuthorAutocompleteMessage = "You already select this author";
            }else {
                newBook.getAuthors().add(author);
            }
        }else {
            newBookAddAuthorAutocompleteMessage = "Field must be choosed from autocomplete form";
        }
        hiddenManageBookAddAuthorId =null;
    }

    public void clearMessages(){
        LOGGER.info("clearMessages message is cleared");
        newBookAddAuthorAutocompleteMessage = "";
        detailBookAddAuthorAutocompleteMessage = "";
        isbnValidMessage = "";
    }

    private boolean isHasSameBook(Book book, Author author){
        return book.getAuthors().stream().anyMatch(item -> item.getId() == author.getId());
    }

    public void deleteAuthorFromBook(Long pk) throws BookException {
        LOGGER.info("IN deleteAuthorFromBook:(pk = [{}]", pk);
        detailBook.getAuthors().removeIf(author1 -> author1.getId() == pk);
        bookManager.update(detailBook);
    }

    public void deleteAuthorFromAddBookForm(Long pk){
        LOGGER.info("IN deleteAuthorFromAddBookForm:(pk = [{}]", pk);
        newBook.getAuthors().removeIf(author1 -> author1.getId() == pk);
    }

    /**
     * Get autocompleted authors list by authors second name prefix
     * @param prefix prefix of author
     * */
    public List<Author> autocompleteAuthor(FacesContext context, UIComponent component, Object prefix) throws AuthorException {
        return authorManager.getAutocompleteBySecondName(((String) prefix));
    }

    /**
     * Filter books by entered author second name
     * */
    public void filterByAuthor() throws AuthorException {
        LOGGER.info("IN filterByAuthor " + hiddenManageBookAddAuthorIdForFilter);
        filterAuthorValidationMessage = null;
        if((hiddenManageBookAddAuthorIdForFilter == null || hiddenManageBookAddAuthorIdForFilter.equals("")) && authorAutocomplete.equals("")){
            dataModule.setFilteredRating(null);
            dataModule.setFilteredAuthor(null);
        } else if(hiddenManageBookAddAuthorIdForFilter == null || hiddenManageBookAddAuthorIdForFilter.equals("")){
            filterAuthorValidationMessage = "Choose correct author name";
        }else {
            Author filteredAuthor = authorManager.getAuthorByPk(Long.parseLong(hiddenManageBookAddAuthorIdForFilter));
            dataModule.setFilteredAuthor(filteredAuthor);
            hiddenManageBookAddAuthorIdForFilter = null;
        }
    }

    private void unsetBookFilter(){
        dataModule.setFilteredRating(null);
        dataModule.setFilteredAuthor(null);
    }

    /**
     * Adding and removing selected book to delete list
     * @param pk pk of selected book
     * */
    public void selectPk(long pk) {
        LOGGER.info("IN selectToDelete = [{}]", pk);
        if(selectedToDeletePks.contains(pk)){
            selectedToDeletePks.remove(pk);
        }else {
            selectedToDeletePks.add(pk);
        }
        LOGGER.info("OUT selectPkList = [{}]", selectedToDeletePks);
    }

    /**
     * Adding and removing selected book to delete list
     * */
    public void clearSelected() {
        LOGGER.info("IN clearSelected = [{}]", selectedToDeletePks);
        selectedToDeletePks.clear();
        filterAuthorValidationMessage = null;
        newBookAddAuthorAutocompleteMessage = null;
        detailBookAddAuthorAutocompleteMessage = null;

        LOGGER.info("OUT clearSelected = [{}]", selectedToDeletePks);
    }

    @Override
    public void sortBy() {
        Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        dataModule.sortBy(params.get(COLUMN_NAME));
    }

    public void refreshDetailBook() throws BookException {
        detailBook = bookManager.getBookByPk(detailBook.getId());
    }

//    redirected methods

    /**
     * Redirect to detail page by pointed pk
     * @param pk pk of Book
     * */
    public void toDetailPage(long pk) throws BookException {
        LOGGER.info("IN toDetailPage:(pk = [{}])", pk);
        detailBook = bookManager.getBookByPk(pk);

        clearMessages();

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

    /**
     * Redirect to book manage page without filter
     * */
    public void redirectToManagePageAndUnsetFilters() throws BookException {
        LOGGER.info("IN redirectToManagePageAndUnsetFilters:");
        unsetBookFilter();
        redirectToManagePage();
    }

    /**
     * Redirect to book manage page
     * */
    private void redirectToManagePage() throws BookException {
        try {
            FacesContext ctx = FacesContext.getCurrentInstance();

            ExternalContext extContext = ctx.getExternalContext();
            String url = extContext.encodeActionURL(ctx.getApplication().
                    getViewHandler().getActionURL(ctx, "/view/books_manage.xhtml"));

            extContext.redirect(url);
        } catch (Exception e) {
            throw new BookException("Something wrong");
        }
    }

    /**
     * Redirect to book manage page with rating filter
     * @param rating
     * */
    public String toBookManageByRating(int rating) throws BookException {
        LOGGER.info("IN toBookManageByRating:(rating = [{}])", rating);
        dataModule.setFilteredAuthor(null);
        dataModule.setFilteredRating(rating);
        redirectToManagePage();
        return null;
    }
}
