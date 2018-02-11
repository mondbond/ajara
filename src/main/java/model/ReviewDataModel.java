package model;

import entity.Review;
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
public class ReviewDataModel extends AbstractExtendedModel<Review> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReviewDataModel.class);

    private @Setter Long bookId;

    private String sortingColumn = Review.getDATE_COLUMN();


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
        return Review.getPK_COLUMN();
    }

    public String getNameColumnConstant() {
        return Review.getNAME_COLUMN();
    }

    public String getRatingColumnConstant() {
        return Review.getAVG_RATING_COLUMN();
    }

    public String getDateColumnConstant() {
        return Review.getDATE_COLUMN();
    }
}
