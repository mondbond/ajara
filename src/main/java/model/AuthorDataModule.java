package model;

import data.entity.Author;
import org.ajax4jsf.model.DataVisitor;
import org.ajax4jsf.model.ExtendedDataModel;
import org.ajax4jsf.model.Range;
import org.ajax4jsf.model.SequenceRange;
import repository.AuthorFacade;

import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.faces.context.FacesContext;
import java.util.List;

@Stateful
public class AuthorDataModule extends ExtendedDataModel<Author> {

    private Integer rowKey;
    private List<Author> list;
    private Integer cachedCount;

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

        this.list = dao.getPagination(firstRow, numberOfLines);
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
        if (cachedCount == null) {
            this.cachedCount = dao.countAll();
        }
        return cachedCount;
    }

    @Override
    public Author getRowData() {
        Author author = list.get(rowKey);
        return author;
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
}
