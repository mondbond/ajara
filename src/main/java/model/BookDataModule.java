package model;

import entity.Author;
import entity.Book;
import lombok.Getter;
import lombok.Setter;
import org.ajax4jsf.model.DataVisitor;
import org.ajax4jsf.model.ExtendedDataModel;
import org.ajax4jsf.model.Range;
import org.ajax4jsf.model.SequenceRange;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.BookFacade;

import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Stateful
public class BookDataModule  extends ExtendedDataModel<Book> {
    private static final Logger LOGGER = LoggerFactory.getLogger(BookDataModule.class);

    public final String PK_COLUMN = "ID";
    public final String NAME_COLUMN = "NAME";
    public final String AUTHOR_COLUMN = "AUTHOR";
    public final String PUBLISHER_COLUMN = "PUBLISHER";
    public final String YEAR_COLUMN = "PUBLISH_YEAR";
    public final String AVG_RATING_COLUMN = "AVG_RATING";
    public final String DATE_COLUMN = "CREATE_DATE";

    private @Getter @Setter Author filteredAuthor;
    private @Getter @Setter Integer filteredRating = null;

    private Integer rowKey;
    private List<Book> list = new ArrayList<>();

    private boolean isASC;
    //    sorting

    private HashMap<String, Boolean> mOderMap = new HashMap<>();

    private String sortingColumn = DATE_COLUMN;
    @EJB
    private BookFacade dao;

    public BookDataModule() { }

    @Override
    public void setRowKey(Object o) {
        this.rowKey = (Integer) o;
    }

    @Override
    public Object getRowKey() {
        return rowKey;
    }

    @Override
    public void walk(FacesContext facesContext, DataVisitor dataVisitor, Range range, Object o) {
        int firstRow = ((SequenceRange) range).getFirstRow();
        int numberOfLines = ((SequenceRange) range).getRows();

        if(!ObjectUtils.allNotNull(filteredAuthor, filteredRating)) {
            this.list = dao.getPagination(firstRow, numberOfLines, sortingColumn, isASC);
        }else if(ObjectUtils.allNotNull(filteredAuthor)) {
            this.list = dao.getPaginationByAuthor(filteredAuthor, firstRow, numberOfLines, sortingColumn, isASC);
        }else {
            this.list = dao.getPaginationByRating((filteredRating-1), filteredRating, firstRow, numberOfLines, sortingColumn, isASC);
        }

        for (int i = 0; i < list.size(); i++) {
            dataVisitor.process(facesContext, i, o);
        }
    }

    @Override
    public boolean isRowAvailable() {
        return list.size() > rowKey;
    }

    @Override
    public int getRowCount() {
        if(filteredAuthor == null && filteredRating == null) {
            return dao.countAll();
        }else if(filteredAuthor != null) {
            return ((dao.getCountByAuthor(filteredAuthor))).intValue();
        }else {
            return ((dao.getCountByRating((filteredRating-1), filteredRating))).intValue();
        }
    }

    /**
     * Sorting authors by params from RequestParameterMap
     * */
    public void sortBy(String sortingColumn) {
        this.sortingColumn = sortingColumn;
        changeOrder(sortingColumn);
    }

    /**
     * Handle order changing in author table
     * */
    private void changeOrder(String columnName) {
        if(mOderMap.containsKey(columnName)) {
            mOderMap.put(columnName, !mOderMap.get(columnName));
        } else {
            mOderMap.put(columnName, true);
        }
        isASC = mOderMap.get(columnName);
    }

    @Override
    public Book getRowData() {
        return list.get(rowKey);
    }

    @Override
    public int getRowIndex() {
        return rowKey == null ? 0 : rowKey;
    }

    @Override
    public void setRowIndex(int i) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object getWrappedData() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setWrappedData(Object o) {
        throw new UnsupportedOperationException();
    }

    public String getPkColumnConstant() {
        return PK_COLUMN;
    }

    public String getNameColumnConstant() {
        return NAME_COLUMN;
    }

    public String getAuthorColumnConstant() {
        return AUTHOR_COLUMN;
    }

    public String getPublisherColumnConstant() {
        return PUBLISHER_COLUMN;
    }

    public String getRatingColumnConstant() {
        return AVG_RATING_COLUMN;
    }

    public String getDateColumnConstant() {
        return DATE_COLUMN;
    }

    public String getYearColumnConstant() {
        return YEAR_COLUMN;
    }
}

