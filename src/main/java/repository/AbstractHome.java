package repository;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

public abstract class AbstractHome<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractHome.class);

    private Class<T> entity;

    @PersistenceContext
    private @Getter EntityManager entityManager;

    /**
     * Constructor to inject Class type object
     * @param entity Class type param
     * */
    public AbstractHome(Class<T> entity) {
        this.entity = entity;
    }

    /**
     * Insert entity in database
     * @param entity any entity
     * */
    public void insert(T entity) {
        LOGGER.info("IN insert(entity = [{}])", entity);
        if(entityManager.contains(entity)) {
            entityManager.persist(entity);
        } else{
            entityManager.merge(entity);
        }
    }

    /**
     * Delete entity by pk
     * @param pk pk of an entity
     * */
    public void deleteByPk(long pk) {
        LOGGER.info("IN deleteByPk:(pk = [{}])", pk);
        T object = entityManager.find(entity, pk);
        entityManager.remove(object);
    }

    /**
     * Delete entities by list of pks
     * @param list list of pks
`    * */
    public void deleteList(List<Long> list) {
        LOGGER.info("IN deleteList:(list = [{}])", list);
        if(!list.isEmpty()) {
            Query query = entityManager.createQuery("DELETE FROM " + entity.getName() + " o WHERE o.id IN (?1)");
             query.setParameter(1, list);
            query.executeUpdate();
        }
    }

    /**
     * Update entity by entity object
     * @param entity object for updating
     * */
    public void update(T entity) {
        LOGGER.info("IN update:(entity = [{}])", entity);
        entityManager.merge(entity);
    }
}