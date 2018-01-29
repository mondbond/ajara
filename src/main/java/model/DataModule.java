package model;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.AbstractFacade;

import java.util.ArrayList;

public class DataModule<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataModule.class);

    AbstractFacade<T> facade;

    private @Getter @Setter int rowCount;

    private @Getter @Setter ArrayList<T> results = new ArrayList<>();

    private @Getter @Setter String sortingColumn;

    private @Getter @Setter boolean isASC = true;

    public DataModule(int rowCount, String defaultSortingColumn, AbstractFacade<T> facade) {
        this.rowCount = rowCount;
        this.sortingColumn = defaultSortingColumn;
        this.facade = facade;
        setInitValues();
    }

    public void setInitValues(){
        results = (ArrayList<T>) facade.getPagination(0, rowCount, sortingColumn, isASC);
    }

    public void refreshPagination(int page) {
        results = (ArrayList<T>) facade.getPagination(page*rowCount, rowCount, sortingColumn, isASC);
    }
}
