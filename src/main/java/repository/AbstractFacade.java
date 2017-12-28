package repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

abstract class AbstractFacade<T> {

    Class<T> entity;

    @PersistenceContext
    private EntityManager entityManager;

    AbstractFacade(Class<T> entity) {
        this.entity = entity;
    }

    private EntityManager getEntityManager() {
        return entityManager;
    }

    public T findByPk(long pk){
        return getEntityManager().find(entity, pk);
    }

    public List<T> findAll(){
        return getEntityManager().createQuery("from " + entity.getName()).getResultList();
    }

    public List<T> getPagination(int skip, int limit) {
        Query query = getEntityManager().createQuery("from " + entity.getName());
        query.setFirstResult(skip);
        query.setMaxResults(limit);
        return query.getResultList();
    }

    public int countAll() {
        return ((Long) getEntityManager().createQuery("SELECT count(*) from " + entity.getName()).getSingleResult()).intValue();
    }
}
