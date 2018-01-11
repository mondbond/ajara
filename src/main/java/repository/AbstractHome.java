package repository;

import org.hibernate.Session;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public abstract class AbstractHome<T> {

    Class<T> entity;

    @PersistenceContext
    private EntityManager entityManager;

    public AbstractHome(Class<T> entity) {
        this.entity = entity;
    }

    EntityManager getEntityManager() {
        return entityManager;
    }

    public void insert(T object) {
//        ((Session) getEntityManager().getDelegate()).save(object);
        getEntityManager().merge(object); // TODO: use persist
        getEntityManager().flush(); // TODO: Why flush???
    }

    public void deleteByPk(long pk) {
        T object = getEntityManager().find(entity, pk);
        getEntityManager().remove(object);
    }

    public void update(T object) {
        getEntityManager().merge(object);
    }
}