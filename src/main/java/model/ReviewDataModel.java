package model;

import entity.Review;
import util.exception.ReviewException;
import lombok.Setter;
import managers.ReviewManager;
import org.ajax4jsf.model.DataVisitor;
import org.ajax4jsf.model.Range;
import org.ajax4jsf.model.SequenceRange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.faces.context.FacesContext;

@Stateful
public class ReviewDataModel extends AbstractExtendedModel<Review> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReviewDataModel.class);

    private @Setter Long bookId;

    @EJB
    private ReviewManager manager;

    public ReviewDataModel() {
    }

    @Override
    public void walk(FacesContext facesContext, DataVisitor dataVisitor, Range range, Object o) {
        int firstRow = ((SequenceRange) range).getFirstRow();
        int numberOfLines = ((SequenceRange) range).getRows();

        try {
            String sortingColumn = Review.DATE_COLUMN;
            this.list = manager.getPagination(firstRow, numberOfLines, sortingColumn, isASC, bookId);
        } catch (ReviewException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < list.size(); i++) {
            dataVisitor.process(facesContext, i, o);
        }
    }

    @Override
    public int getRowCount() {
        try {
            return manager.countAllByBookId(bookId);
        } catch (ReviewException e) {
            return 0;
        }
    }
}
