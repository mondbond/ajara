package model;

import entity.Author;
import org.ajax4jsf.model.DataVisitor;
import org.ajax4jsf.model.Range;
import org.ajax4jsf.model.SequenceRange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.AuthorFacade;

import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.faces.context.FacesContext;

@Stateful
public class AuthorDataModel extends AbstractExtendedModel<Author> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorDataModel.class);

    private String sortingColumn = Author.getDATE_COLUMN();

    @EJB
    private  AuthorFacade dao;

    public AuthorDataModel() {
    }

    /**
     * Sorting authors by params from RequestParameterMap
     * */
    public void sortBy(String sortingColumn) {
        LOGGER.info("SORT [{}] and map = [{}]", sortingColumn, mOderMap);
        this.sortingColumn = sortingColumn;
        changeOrder(sortingColumn);
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
    public int getRowCount() {
        LOGGER.info("getRowCount: " + String.valueOf(dao.countAll()));
        return dao.countAll();
    }



    public String getPkColumnConstant() {
        return Author.getPK_COLUMN();
    }

    public String getNameColumnConstant() {
        return Author.getNAME_COLUMN();
    }

    public String getSecondNameColumnConstant() {
        return Author.getSECOND_NAME_COLUMN();
    }

    public String getRatingColumnConstant() {
        return Author.getAVG_RATING_COLUMN();
    }

    public String getDateColumnConstant() {
        return Author.getDATE_COLUMN();
    }
}
