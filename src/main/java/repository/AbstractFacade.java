package repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

public abstract class AbstractFacade<T> {

    private Class<T> entity;

    @PersistenceContext
    private EntityManager entityManager;

    public AbstractFacade(Class<T> entity) {
        this.entity = entity;
    }

    protected EntityManager getEntityManager() {
        return entityManager;
    }

    public T findByPk(long pk){
        return getEntityManager().find(entity, pk);
    } // TODO: use field instead of getter

    public List<T> findAll(){
        return getEntityManager().createQuery("from " + entity.getName()).getResultList();
    }

    public List<T> getPagination(int skip, int limit, String column, boolean isAsc) {
        String sql = "from " + entity.getName() + " order by " + column + " " + ((isAsc)? "ASC": "DESC"); // TODO: rename variable
        Query query = getEntityManager().createQuery(sql);
        query.setFirstResult(skip);
        query.setMaxResults(limit);
        return query.getResultList();
    }

    public int countAll() {
        return ((Long) getEntityManager().createQuery("SELECT count(*) from " + entity.getName()).getSingleResult()).intValue();
    }
}
