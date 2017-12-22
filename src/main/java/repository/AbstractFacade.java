package repository;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

abstract class AbstractFacade<T> {

    Class<T> entity;

    @PersistenceContext
    private EntityManager entityManager;

    AbstractFacade(Class<T> entity) {
        this.entity = entity;
    }

    EntityManager getEntityManager() {
        return entityManager;
    }

    Session getSession() {
        return (Session) getEntityManager().getDelegate();
    }

    public T findByPk(long pk){
        return getSession().get(entity, pk);
    }

    public List<T> findAll(){
        return getSession().createQuery("from " + entity.getName()).list();
    }

    public List<T> getPagination(int skip, int limit) {
        Query query = getSession().createQuery("from " + entity.getName());
        query.setFirstResult(skip);
        query.setMaxResults(limit);
        return query.list();
    }

    public int countAll() {
        return ((Long) getSession().createCriteria(entity.getName()).setProjection(Projections.rowCount()).uniqueResult()).intValue();
    }
}
