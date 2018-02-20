package model;

import entity.Author;
import util.exception.AuthorException;
import managers.AuthorManager;
import org.ajax4jsf.model.DataVisitor;
import org.ajax4jsf.model.Range;
import org.ajax4jsf.model.SequenceRange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.faces.context.FacesContext;

@Stateful
public class AuthorDataModel extends AbstractExtendedModel<Author> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorDataModel.class);

    private String sortingColumn = Author.DATE_COLUMN;

    @EJB
    private AuthorManager authorManager;

    public AuthorDataModel() {
    }

    /**
     * Sorting authors by params from RequestParameterMap
     */
    public void sortBy(String sortingColumn) {
        LOGGER.info("SORT [{}] and map = [{}]", sortingColumn, mOderMap);
        this.sortingColumn = sortingColumn;
        changeOrder(sortingColumn);
    }

    @Override
    public void walk(FacesContext facesContext, DataVisitor dataVisitor, Range range, Object o) {
        int firstRow = ((SequenceRange) range).getFirstRow();
        int numberOfLines = ((SequenceRange) range).getRows();

        try {
            this.list = authorManager.getPagination(firstRow, numberOfLines, sortingColumn, isASC);
        } catch (AuthorException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < list.size(); i++) {
            dataVisitor.process(facesContext, i, o);
        }
    }

    @Override
    public int getRowCount() {
        try {
            return authorManager.countAll();
        } catch (AuthorException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public String getPkColumnConstant() {
        return Author.PK_COLUMN;
    }

    public String getNameColumnConstant() {
        return Author.NAME_COLUMN;
    }

    public String getSecondNameColumnConstant() {
        return Author.SECOND_NAME_COLUMN;
    }

    public String getRatingColumnConstant() {
        return Author.AVG_RATING_COLUMN;
    }

    public String getDateColumnConstant() {
        return Author.DATE_COLUMN;
    }
}
