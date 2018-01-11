package repository;

import lombok.Getter;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

public abstract class AbstractHome<T> {

    Class<T> entity;

    @PersistenceContext
    private @Getter EntityManager entityManager;

    public AbstractHome(Class<T> entity) {
        this.entity = entity;
    }

    public void insert(T object) {
//        ((Session) getEntityManager().getDelegate()).save(object);
        entityManager.persist(object);
    }

    public void deleteByPk(long pk) {
        T object = entityManager.find(entity, pk);
        entityManager.remove(object);
    }

    public void deleteList(List<Long> list) {
//        Query query = entityManager.createQuery("DELETE FROM " + entity.getName() + " WHERE ID IN (:list)");
        Query query = entityManager.createQuery("DELETE FROM " + entity.getName() +" WHERE id IN (:list)");
        query.setParameter("list", list);
        query.executeUpdate();
    }

    public void update(T object) {
        entityManager.merge(object);
    }
}