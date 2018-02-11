package model;

import org.ajax4jsf.model.ExtendedDataModel;

import java.util.HashMap;
import java.util.List;

public abstract class AbstractExtendedModel<E> extends ExtendedDataModel<E> {

    protected Integer rowKey;
    protected List<E> list;
    protected boolean isASC;

    protected HashMap<String, Boolean> mOderMap = new HashMap<>();


    /**
     * Handle order changing in author table
     * */
    protected void changeOrder(String columnName){
        if(mOderMap.containsKey(columnName)) {
            mOderMap.put(columnName, !mOderMap.get(columnName));
        } else {
            mOderMap.put(columnName, true);
        }
        isASC = mOderMap.get(columnName);
    }


    @Override
    public boolean isRowAvailable() {
        return list.size() > rowKey;
    }


    @Override
    public E getRowData() {
        return list.get(rowKey);
    }

    @Override
    public int getRowIndex() {
        return rowKey == null ? 0 : rowKey;
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

    public interface Sorted{
        /**
         * Sorting by params from RequestParameterMap
         * */
        void sortBy();
    }
}
