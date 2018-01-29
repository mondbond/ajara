package controllers;

import entity.Author;
import entity.Book;
import lombok.Getter;
import lombok.Setter;
import managers.AuthorManager;
import managers.BookManager;
import model.BookJPAModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.faces.FacesException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ManagedBean
@SessionScoped
public class BookController {
    private static final Logger LOGGER = LoggerFactory.getLogger(BookController.class);

    private @Setter @Getter Book newBook = new Book();
    private @Getter @Setter Book detailBook = new Book();

    private @Setter @Getter String authorA = new String();

    private List<Author> aAuthors = new ArrayList<>();

    private @Getter @Setter List<String> authorsForBook = new ArrayList<>();

    //    all authors for autocompleteAll. fix
    private List<Author> allAuthors = new ArrayList<>();

    //    sorting
    private String sortingColumn = null;
    private HashMap<String, Boolean> mOderMap = new HashMap<>();

    private ArrayList<Long> selectedToDeletePks = new ArrayList<>();

    private @Getter @Setter List <Author> autocompleteAuthors = new ArrayList<>();

    @EJB
    private @Getter BookManager bookManager;

    @EJB
    private @Getter AuthorManager authorManager;

    public BookController() { }

    /**
     * Inserting new book
     * */
    public String insertNewBook(){
        ArrayList<Author> authors = new ArrayList<>();
        authorsForBook.forEach(author -> authors.add(authorManager.getAuthorByPk(Long.parseLong(author))));
        newBook.getAuthors().addAll(authors);
        LOGGER.info("insertNewBook:(book = [{}])", newBook);
        bookManager.save(newBook);
        newBook = new Book();
        authorsForBook.clear();
        return "books_manage.xhtml";
    }

    /**
     * Create book by pointed single author from detail author page
     * @param author author of a book
     * */
    public String createBookByAuthor(Author author) throws IOException {
        ArrayList<Author> authors = (ArrayList<Author>) newBook.getAuthors();
        authors.add(author);
        newBook.setAuthors(authors);
        LOGGER.info("insertNewBook:(book = [{}], author = [{}])", newBook, author);
        bookManager.save(newBook);
        newBook = new Book();
        return "author_detail.xhtml";
    }

    /**
     * Get list of authors book by authors pk
     * @param pk authors pk
     * @return BookJPAModel instance with filtered by author
     * */
    public BookJPAModel getBooksByAuthorPk(Long pk){
        LOGGER.info("getBooksByAuthorPk:(pk = [{}]", pk);
//        dataModule.setFilteredAuthor(authorManager.getAuthorByPk(pk));
//        getModel().setFilter(Book.);
//        dataModule.setFilteredAuthor(authorManager.getAuthorByPk(pk));
//
        CriteriaBuilder cb = getBookManager().getBookFacade().getEntityManager().getCriteriaBuilder();
//
//        CriteriaQuery<Book> cq = cb.createQuery(Book.class);
//        Root<Author> authorRoot = cq.from(Author.class);
//        Root<Book> bookRoot = cq.from(Book.class);
//        Join<Book, Author> join = authorRoot.join("Book");
////        cq.select(bookRoot).where(cb.equal(authorRoot.get("id"), pk));
//        cq.select(bookRoot).where(cb.equal(join.get("id"), pk));
//
//        getModel().getFilters().add(cb.equal(join.get("id"), pk));

            CriteriaQuery<Book> criteriaQuery = cb.createQuery(Book.class);
            Root<Author> answerRoot = criteriaQuery.from(Author.class);
            criteriaQuery.where(cb.equal(answerRoot.get("id"), pk));
            ListJoin<Author, Book> answers = answerRoot.joinList("books");
            CriteriaQuery<Book> cq = criteriaQuery.select(answers);

//            String s = answers;
//            answers.

        getModel().join = answers;
        getModel().filtere = cb.equal(answerRoot.get("id"), pk);


//            criteriaQuery.
//            TypedQuery<Book> query = entityManager.createQuery(cq);
//            return query.getResultList();
//        }

//        getModel();


        return getModel();
    }

    /**
     * Update detail book
     * */
    public void update() {
        LOGGER.info("update:(detailBook = [{}]", detailBook);
        bookManager.update(detailBook);
    }

    /**
     * Delete selected books
     * */
    public String deleteSelected() {
        LOGGER.info("deleteSelected:(selectedBookList = [{}]", selectedToDeletePks);
        bookManager.deleteList(selectedToDeletePks);
        selectedToDeletePks.clear();
        return null;
    }

    /**
     * Delete detail book and redirect to book manage page
     * */
    public void deleteDetail(){
        LOGGER.info("deleteDetail:(detailBook = [{}]", detailBook);
        bookManager.delete(detailBook.getId());
        try {
            redirectToManagePage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get autocompleted authors list by authors second name prefix
     * @param prefix prefix of author
     * */
    public List<Author> autocompleteAuthor(FacesContext context, UIComponent component, Object prefix) {
        aAuthors = authorManager.getAutocompleteBySecondName(((String) prefix));
        return aAuthors;
    }


    /**
     * Filter books by entered author second name
     * */
//    public void filterByAuthor(){
//        if(authorA.equals("")){
//            dataModule.setFilteredRating(null);
//            dataModule.setFilteredAuthor(null);
//        }
//        if(authorA != null && !authorA.equals("")){
//            List<Author> authors = new ArrayList<>();
//            Author author2 = aAuthors.stream().filter(author1 -> author1.fullName().equals(authorA)).findFirst().get();
//            dataModule.setFilteredAuthor(author2);
//        }
//    }

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
//        sortingColumn = params.get(Book.COLUMN_NAME);
//        dataModule.setSortField(sortingColumn, mOderMap.get(sortingColumn));
//        getBookManager().

        authorManager.getAuthorFacade().getModel().setSortingColumnAndConfigureOrder(params.get(getColumnName()));
        return null;
    }

//    /**
//     * Handle order changing in author table
//     * */
//    private void changeOrder(String columnName) {
//        if(mOderMap.containsKey(columnName)) {
//            mOderMap.put(columnName, !mOderMap.get(columnName));
//        } else {
//            mOderMap.put(columnName, true);
//        }
//    }

    /**
     * Redirect to detail page by pointed pk
     * @param pk pk of Book
     * */
    public void toDetailPage(long pk) {
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

    private void reload() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
    }

    /**
     * FIX
     * */
    public List<Author> autocompleteAll() {
        if(allAuthors.isEmpty()) {
            allAuthors = authorManager.getAllAuthors();
        }
        return allAuthors;
    }

//    private void unsetBookFilter(){
//        dataModule.setFilteredRating(null);
//        dataModule.setFilteredAuthor(null);
//    }

    /**
     * Redirect to book manage page without filter
     * */
//    public void redirectToManagePageAndUnsetFilters() throws IOException {
//        LOGGER.info("redirectToManagePageAndUnsetFilters:");
//        unsetBookFilter();
//        redirectToManagePage();
//    }

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
    public Long getBookCountByRating(int rating){
        LOGGER.info("getBookCountByRating:(rating = [{}]", rating);
        return bookManager.getCountByRating(rating-1, rating);
    }

    /**
     * Redirect to book manage page with rating filter
     * */
//    public String toBookManageByRating(int rating) throws IOException {
//        LOGGER.info("toBookManageByRating:(rating = [{}])", rating);
//        dataModule.setFilteredAuthor(null);
//        dataModule.setFilteredRating(rating);
//        redirectToManagePage();
//        return null;
//    }

    public BookJPAModel getModel() {
        return bookManager.getBookFacade().getModel();
    }

    public String getPkColumn() {
        return Book.PK_COLUMN;
    }

    public String getNameColumn() {
        return Book.NAME_COLUMN;
    }

    public String getPublisherColumn() {
        return Book.PUBLISHER_COLUMN;
    }

    public String getPublishYearColumn() {
        return Book.PUBLISH_YEAR_COLUMN;
    }

    public String getAvgRatingColumn() {
        return Book.AVG_RATING_COLUMN;
    }

    public String getDateColumn() {
        return Book.DATE_COLUMN;
    }

    public String getColumnName() {
        return Book.COLUMN_NAME;
    }
}
