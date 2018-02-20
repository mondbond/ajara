package model;

import entity.Author;
import entity.Book;
import util.exception.BookException;
import lombok.Getter;
import lombok.Setter;
import managers.BookManager;
import org.ajax4jsf.model.DataVisitor;
import org.ajax4jsf.model.Range;
import org.ajax4jsf.model.SequenceRange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.faces.context.FacesContext;

@Stateful
public class BookDataModel extends AbstractExtendedModel<Book> {
    private static final Logger LOGGER = LoggerFactory.getLogger(BookDataModel.class);

    private @Getter @Setter
    Author filteredAuthor;

    private @Getter @Setter
    Integer filteredRating = null;

    private String sortingColumn = Book.DATE_COLUMN;

    @EJB
    private BookManager manager;

    public BookDataModel() {
    }

    /**
     * Sorting authors by params from RequestParameterMap
     *
     * @param sortingColumn name of the column to sort
     */
    public void sortBy(String sortingColumn) {
        this.sortingColumn = sortingColumn;
        changeOrder(sortingColumn);
    }

    @Override
    public void walk(FacesContext facesContext, DataVisitor dataVisitor, Range range, Object o) {
        LOGGER.info("BookDataModel");
        int firstRow = ((SequenceRange) range).getFirstRow();
        int numberOfLines = ((SequenceRange) range).getRows();

        if (filteredAuthor == null && filteredRating == null) {
            try {
                this.list = manager.getPagination(firstRow, numberOfLines, sortingColumn, isASC);
            } catch (BookException e) {
                e.printStackTrace();
            }
        } else if (filteredAuthor != null) {
            try {
                this.list = manager.getPaginationByAuthor(filteredAuthor, firstRow, numberOfLines, sortingColumn, isASC);
            } catch (BookException e) {
                e.printStackTrace();
            }
        } else {
            try {
                this.list = manager.getPaginationByRating((filteredRating - 1), filteredRating, firstRow, numberOfLines, sortingColumn, isASC);
            } catch (BookException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < list.size(); i++) {
            dataVisitor.process(facesContext, i, o);
        }
    }

    @Override
    public int getRowCount() {

        if (filteredAuthor == null && filteredRating == null) {
            try {
                return manager.countAll();
            } catch (BookException e) {
                e.printStackTrace();
                return 0;
            }
        } else if (filteredAuthor != null) {
            try {
                return (manager.getCountByAuthor(filteredAuthor)).intValue();
            } catch (BookException e) {
                e.printStackTrace();
                return 0;
            }
        } else {
            try {
                return (manager.getCountByRatingRange((filteredRating - 1), filteredRating)).intValue();
            } catch (BookException e) {
                e.printStackTrace();
                return 0;
            }
        }
    }

    public String getPkColumnConstant() {
        return Book.PK_COLUMN;
    }

    public String getNameColumnConstant() {
        return Book.NAME_COLUMN;
    }

    public String getPublisherColumnConstant() {
        return Book.PUBLISHER_COLUMN;
    }

    public String getRatingColumnConstant() {
        return Book.AVG_RATING_COLUMN;
    }

    public String getYearColumnConstant() {
        return Book.YEAR_COLUMN;
    }

    public String getDateColumnConstant() {
        return Book.DATE_COLUMN;
    }
}
