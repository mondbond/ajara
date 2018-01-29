package model;

import entity.Author;
import lombok.Getter;
import lombok.Setter;
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
import java.util.List;

@Stateful
public class AuthorDataModule extends ExtendedDataModel<Author> {
    final Logger logger = LoggerFactory.getLogger(AuthorDataModule.class);

    private @Getter @Setter final String PK_COLUMN = "ID";
    private @Getter @Setter final String NAME_COLUMN = "FIRST_NAME";
    private @Getter @Setter final String SECOND_NAME_COLUMN = "SECOND_NAME";
    private @Getter @Setter final String AVG_RATING_COLUMN = "AVG_RATING";
    private @Getter @Setter final String DATE_COLUMN = "CREATE_DATE";

    private Integer rowKey;
    private List<Author> list;
    private Integer cachedCount;

    private boolean isASC;
//    sorting
    private String sortingColumn = DATE_COLUMN;
    @EJB
    private  AuthorFacade dao;

    public AuthorDataModule() {
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
        if (cachedCount == null) {
            this.cachedCount = dao.countAll();
        }
        return cachedCount;
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

    public void setSortField(String sortField, boolean isASC) {
        this.isASC = isASC;
        sortingColumn = sortField;
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
