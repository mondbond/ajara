package repository;

import org.hibernate.Session;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public abstract class AbstractHome<T> {

    Class<T> entity;

    @PersistenceContext
    private EntityManager entityManager;

    AbstractHome(Class<T> entity) {
        this.entity = entity;
    }

    EntityManager getEntityManager() {
        return entityManager;
    }

    Session getSession() {
        return (Session) getEntityManager().getDelegate();
    }

    public void insert(T object) {
        getSession().save(object);
    }

    public void deleteByPk(long pk) {
        T object = getSession().load(entity, pk);
        getSession().delete(object);
        getSession().flush();
    }
}
