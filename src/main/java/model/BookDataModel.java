package model;

import entity.Author;
import entity.Book;
import lombok.Getter;
import lombok.Setter;
import org.ajax4jsf.model.DataVisitor;
import org.ajax4jsf.model.Range;
import org.ajax4jsf.model.SequenceRange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.BookFacade;

import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.faces.context.FacesContext;

@Stateful
public class BookDataModel extends AbstractExtendedModel<Book> {
    private static final Logger LOGGER = LoggerFactory.getLogger(BookDataModel.class);

    private @Getter @Setter Author filteredAuthor;
    private @Getter @Setter Integer filteredRating = null;

    private String sortingColumn = Book.getDATE_COLUMN();

    @EJB
    private BookFacade dao;

    public BookDataModel() {
    }

    /**
     * Sorting authors by params from RequestParameterMap
     * @param sortingColumn name of the column to sort
     * */
    public void sortBy(String sortingColumn) {
        this.sortingColumn = sortingColumn;
        changeOrder(sortingColumn);
    }

    @Override
    public void walk(FacesContext facesContext, DataVisitor dataVisitor, Range range, Object o) {
        LOGGER.info("BookDataModel");
        int firstRow = ((SequenceRange) range).getFirstRow();
        int numberOfLines = ((SequenceRange) range).getRows();

        if(filteredAuthor == null && filteredRating == null) {
            this.list = dao.getPagination(firstRow, numberOfLines, sortingColumn, isASC);
        }else if(filteredAuthor != null) {
            this.list = dao.getPaginationByAuthor(filteredAuthor, firstRow, numberOfLines, sortingColumn, isASC);
        }else {
            this.list = dao.getPaginationByRating((filteredRating-1), filteredRating, firstRow, numberOfLines, sortingColumn, isASC);
        }

        for (int i = 0; i < list.size(); i++) {
            dataVisitor.process(facesContext, i, o);
        }
    }

    @Override
    public int getRowCount() {

        if(filteredAuthor == null && filteredRating == null) {
            return dao.countAll();
        }else if(filteredAuthor != null) {
            return (dao.getCountByAuthor(filteredAuthor)).intValue();
        }else {
            return (dao.getCountByRatingRange((filteredRating-1), filteredRating)).intValue();
        }
    }

    public String getPkColumnConstant() {
        return Book.getPK_COLUMN();
    }

    public String getNameColumnConstant() {
        return Book.getNAME_COLUMN();
    }

    public String getPublisherColumnConstant() {
        return Book.getPUBLISHER_COLUMN();
    }

    public String getRatingColumnConstant() {
        return Book.getAVG_RATING_COLUMN();
    }

    public String getYearColumnConstant() {
        return Book.getYEAR_COLUMN();
    }

    public String getDateColumnConstant() {
        return Book.getDATE_COLUMN();
    }
}

