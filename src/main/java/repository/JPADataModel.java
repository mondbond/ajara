package repository;

import entity.Author;
import lombok.Setter;
import org.ajax4jsf.model.DataVisitor;
import org.ajax4jsf.model.ExtendedDataModel;
import org.ajax4jsf.model.Range;
import org.ajax4jsf.model.SequenceRange;
import org.richfaces.model.Arrangeable;
import org.richfaces.model.ArrangeableState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.HashMap;
import java.util.List;

public abstract class JPADataModel<T> extends ExtendedDataModel<T> implements Arrangeable {
    private static final Logger LOGGER = LoggerFactory.getLogger(JPADataModel.class);

    private EntityManager entityManager;
    private Object rowKey;

//    private ArrangeableState arrangeableState;
    private Class<T> entityClass;

    public JPADataModel() {}

    private @Setter String sortingColumn = Author.DATE_COLUMN;
//    private @Getter @Setter String filteredColumn;
//    private @Getter @Setter Object filteredValue;

//    private @Getter @Setter List<Predicate> filters;

    private HashMap<String, Boolean> mOderMap = new HashMap<>();

    private CriteriaQuery<T> filterCriteriaQuery;

    public Expression<Boolean> filtere;
    public ListJoin join;

//    ArrangeableState arrangeableState;


    public JPADataModel(EntityManager entityManager, String defaultSortingColumn, Class<T> entityClass) {
        super();
        this.entityManager = entityManager;
        this.entityClass = entityClass;

        this.sortingColumn = defaultSortingColumn;
        changeOrder(sortingColumn);
    }

    public void arrange(FacesContext context, ArrangeableState state) {
//        arrangeableState = state;
    }

    @Override
    public void setRowKey(Object key) {
        rowKey = key;
    }

    @Override
    public Object getRowKey() {
        return rowKey;
    }

    private CriteriaQuery<Long> createCountCriteriaQuery() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<T> root = criteriaQuery.from(entityClass);

        Expression<Boolean> filterCriteria = filtere;
        if(join != null){
            criteriaQuery.select(join);
        }

        if (filterCriteria != null) {
            criteriaQuery.where(filterCriteria);

//            criteriaQuery.where(filters.get(1));


//            criteriaQuery.where(root.get(filteredColumn).equals(filteredValue));
//            criteriaQuery.where(root.get(filteredColumn).in(filteredValue));
        }

        Expression<Long> count = criteriaBuilder.count(root);
        criteriaQuery.select(count);

        return criteriaQuery;
    }

    private CriteriaQuery<T> createSelectCriteriaQuery() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entityClass);
        Root<T> root = criteriaQuery.from(entityClass);

        ParameterExpression<String> p = criteriaBuilder.parameter(String.class);
        criteriaQuery.select(root).orderBy((mOderMap.get(sortingColumn))?criteriaBuilder.asc(root.get(sortingColumn)):criteriaBuilder.desc(root.get(sortingColumn)));

        Expression<Boolean> filterCriteria = filtere;

        if (filterCriteria != null) {
            criteriaQuery.where(filterCriteria);
        }

//        if (filters != null) {
//            criteriaQuery.where(filters.get(1));
//        }

        return criteriaQuery;
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
    }
//
//    private List<Order> createOrders(CriteriaBuilder criteriaBuilder, Root<T> root) {
//        List<Order> orders = Lists.newArrayList();
//        List<SortField> sortFields = arrangeableState.getSortFields();
//        if (sortFields != null && !sortFields.isEmpty()) {
//
//            FacesContext facesContext = FacesContext.getCurrentInstance();
//
//            for (SortField sortField : sortFields) {
//                String propertyName = (String) sortField.getSortBy().getValue(facesContext.getELContext());
//
//                Path<Object> expression = root.get(propertyName);
//
//                Order jpaOrder;
//                SortOrder sortOrder = sortField.getSortOrder();
//                if (sortOrder == SortOrder.ascending) {
//                    jpaOrder = criteriaBuilder.asc(expression);
//                } else if (sortOrder == SortOrder.descending) {
//                    jpaOrder = criteriaBuilder.desc(expression);
//                } else {
//                    throw new IllegalArgumentException(sortOrder.toString());
//                }
//
//                orders.add(jpaOrder);
//            }
//        }
//        return orders;
//    }

//    public ArrangeableState getArrangeableState() {
//        return arrangeableState;
//    }

    protected Class<T> getEntityClass() {
        return entityClass;
    }

//    protected Expression<Boolean> createFilterCriteriaForField(String propertyName, Object filterValue, Root<T> root,
//                                                               CriteriaBuilder criteriaBuilder) {
//        String stringFilterValue = (String) filterValue;
//        if (Strings.isNullOrEmpty(stringFilterValue)) {
//            return null;
//        }
//
//        stringFilterValue = stringFilterValue.toLowerCase(arrangeableState.getLocale());
//
//        Path<String> expression = root.get(propertyName);
//        Expression<Integer> locator = criteriaBuilder.locate(criteriaBuilder.lower(expression), stringFilterValue, 1);
//        return criteriaBuilder.gt(locator, 0);
//    }
//
//    private Expression<Boolean> createFilterCriteria(CriteriaBuilder criteriaBuilder, Root<T> root) {
//        Expression<Boolean> filterCriteria = null;
//        if(arrangeableState != null) {
//            List<FilterField> filterFields = arrangeableState.getFilterFields();
//            if (filterFields != null && !filterFields.isEmpty()) {
//                FacesContext facesContext = FacesContext.getCurrentInstance();
//
//                for (FilterField filterField : filterFields) {
//                    String propertyName = (String) filterField.getFilterExpression().getValue((facesContext.getELContext()));
//                    Object filterValue = filterField.getFilterValue();
//
//                    Expression<Boolean> predicate = createFilterCriteriaForField(propertyName, filterValue, root, criteriaBuilder);
//
//                    if (predicate == null) {
//                        continue;
//                    }
//
//                    if (filterCriteria == null) {
//                        filterCriteria = predicate.as(Boolean.class);
//                    } else {
//                        filterCriteria = criteriaBuilder.and(filterCriteria, predicate.as(Boolean.class));
//                    }
//                }
//            }
//        }
//        return filterCriteria;
//    }



    public void setSortingColumnAndConfigureOrder(String sortingColumn){
        this.sortingColumn = sortingColumn;
        changeOrder(sortingColumn);
    }

//    public void setFilter(String filterColumn, Object filteredValue){
//        this.filteredColumn = filterColumn;
//        this.filteredValue = filteredValue;
//    }


//    logic code

    @Override
    public void walk(FacesContext context, DataVisitor visitor, Range range, Object argument) {
        CriteriaQuery<T> criteriaQuery = createSelectCriteriaQuery();
        TypedQuery<T> query = entityManager.createQuery(criteriaQuery);

        SequenceRange sequenceRange = (SequenceRange) range;
        if (sequenceRange.getFirstRow() >= 0 && sequenceRange.getRows() > 0) {
            query.setFirstResult(sequenceRange.getFirstRow());
            query.setMaxResults(sequenceRange.getRows());
        }

        List<T> data = query.getResultList();
        for (T t : data) {
            visitor.process(context, getId(t), argument);
        }
    }

    @Override
    public boolean isRowAvailable() {
        return rowKey != null;
    }

    @Override
    public int getRowCount() {
        CriteriaQuery<Long> criteriaQuery = createCountCriteriaQuery();
        return entityManager.createQuery(criteriaQuery).getSingleResult().intValue();
    }

    @Override
    public T getRowData() {
        return entityManager.find(entityClass, rowKey);
    }

    @Override
    public int getRowIndex() {
        return -1;
    }

    @Override
    public void setRowIndex(int rowIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object getWrappedData() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setWrappedData(Object data) {
        throw new UnsupportedOperationException();
    }

    protected abstract Object getId(T t);
}