package model;

import entity.Reviews;
import lombok.Setter;
import managers.ReviewManager;
import org.ajax4jsf.model.DataVisitor;
import org.ajax4jsf.model.Range;
import org.ajax4jsf.model.SequenceRange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.ReviewFacade;

import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.faces.context.FacesContext;

@Stateful
public class ReviewDataModel extends AbstractExtendedModel<Reviews> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReviewDataModel.class);

    public final String PK_COLUMN = "ID";
    public final String NAME_COLUMN = "FIRST_NAME";
    public final String AVG_RATING_COLUMN = "AVG_RATING";
    public final String DATE_COLUMN = "CREATE_DATE";

    private @Setter Long bookId;

    private String sortingColumn = DATE_COLUMN;


    @EJB
    private ReviewFacade dao;

    @EJB
    private ReviewManager manager;

    public ReviewDataModel() {
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

        this.list = manager.getPagination(firstRow, numberOfLines, sortingColumn , isASC, bookId);

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
        return PK_COLUMN;
    }

    public String getNameColumnConstant() {
        return NAME_COLUMN;
    }

    public String getRatingColumnConstant() {
        return AVG_RATING_COLUMN;
    }

    public String getDateColumnConstant() {
        return DATE_COLUMN;
    }
}
