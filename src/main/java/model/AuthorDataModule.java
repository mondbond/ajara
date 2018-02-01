package model;

import entity.Author;
import org.ajax4jsf.model.DataVisitor;
import org.ajax4jsf.model.ExtendedDataModel;
import org.ajax4jsf.model.Range;
import org.ajax4jsf.model.SequenceRange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.AuthorFacade;

import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.faces.context.FacesContext;
import java.util.HashMap;
import java.util.List;

@Stateful
public class AuthorDataModule extends ExtendedDataModel<Author> {
    final Logger logger = LoggerFactory.getLogger(AuthorDataModule.class);

    public final String PK_COLUMN = "ID";
    public final String NAME_COLUMN = "FIRST_NAME";
    public final String SECOND_NAME_COLUMN = "SECOND_NAME";
    public final String AVG_RATING_COLUMN = "AVG_RATING";
    public final String DATE_COLUMN = "CREATE_DATE";

    private Integer rowKey;
    private List<Author> list;

    private boolean isASC;
//    sorting

    private String sortingColumn = DATE_COLUMN;

    private HashMap<String, Boolean> mOderMap = new HashMap<>();

    @EJB
    private  AuthorFacade dao;

    public AuthorDataModule() {
    }

    /**
     * Sorting authors by params from RequestParameterMap
     * */
    public void sortBy(String sortingColumn) {
        logger.info("SORT [{}] and map = [{}]", sortingColumn, mOderMap);
        this.sortingColumn = sortingColumn;
        changeOrder(sortingColumn);
    }

    /**
     * Handle order changing in author table
     * */
    private void changeOrder(String columnName){
        if(mOderMap.containsKey(columnName)) {
            mOderMap.put(columnName, !mOderMap.get(columnName));
        } else {
            mOderMap.put(columnName, true);
        }
        isASC = mOderMap.get(columnName);
    }

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

        this.list = dao.getPagination(firstRow, numberOfLines, sortingColumn , isASC);

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
        logger.info("getRowCount: " + String.valueOf(dao.countAll()));
        return dao.countAll();
    }

    @Override
    public Author getRowData() {
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

    public String getSecondNameColumnConstant() {
        return SECOND_NAME_COLUMN;
    }

    public String getRatingColumnConstant() {
        return AVG_RATING_COLUMN;
    }

    public String getDateColumnConstant() {
        return DATE_COLUMN;
    }
}
