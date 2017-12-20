package repository;

import org.hibernate.Session;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public abstract class AbstractHome<T> {

    @PersistenceContext
    private EntityManager entityManager;

    AbstractHome() {
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
}
