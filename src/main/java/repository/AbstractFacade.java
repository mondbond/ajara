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
        return entityManager.find(entity, pk);
    }

    public List<T> findByPks(List<Long> list){
        Query query = entityManager.createQuery("from " + entity.getName() +" t where t.id in (:list)");
        query.setParameter("list", 1136);
        System.out.println("!inser" + list.toString());
        return query.getResultList();
    }

    public List<T> findAll(){
        return entityManager.createQuery("from " + entity.getName()).getResultList();
    }

    public List<T> getPagination(int skip, int limit, String column, boolean isAsc) {
        String sqlString = "from " + entity.getName() + " order by " + column + " " + ((isAsc)? "ASC": "DESC");
        Query query = entityManager.createQuery(sqlString);
        query.setFirstResult(skip);
        query.setMaxResults(limit);
        return query.getResultList();
    }

    public int countAll() {
        return ((Long) entityManager.createQuery("SELECT count(*) from " + entity.getName()).getSingleResult()).intValue();
    }
}
