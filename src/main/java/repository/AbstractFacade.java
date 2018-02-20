package repository;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * AbstractFacade is a class for finding entities from database
 */
public abstract class AbstractFacade<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractFacade.class);

    private Class<T> entity;

    @PersistenceContext
    private @Getter
    EntityManager entityManager;

    /**
     * Constructor to inject Class type object
     *
     * @param entity Class type param
     */
    AbstractFacade(Class<T> entity) {
        this.entity = entity;
    }

    /**
     * Find entity of it's pk
     *
     * @param pk pk of entity
     * @return entity
     */
    public T findByPk(long pk) {
        LOGGER.info("IN findByPk:(entity = [{}], pk = [{}])", entity, pk);
        T result = entityManager.find(entity, pk);
        LOGGER.debug("OUT findByPk: returned [{}]", result);
        return result;
    }

    /**
     * Find entities by pks
     *
     * @param pks pks of pks
     */
    public List<T> findByPks(List<Long> pks) {
        LOGGER.info("IN findByPks:(pks = [{}])", pks);
        Query query = entityManager.createQuery("from " + entity.getName() + " t where t.id in (:list)");
        query.setParameter("list", pks);
        List<T> result = query.getResultList();
        LOGGER.debug("OUT findByPks():returned pks of [{}], size = [{}]", entity.getSimpleName(), result.size());
        return result;
    }

    /**
     * Get all rows
     *
     * @return List of rows
     **/
    public List<T> findAll() {
        LOGGER.info("IN findAll(entity = [{}])", entity);
        List<T> result = entityManager.createQuery("from " + entity.getName(), entity).getResultList();
        LOGGER.debug("OUT findAll:returned list of [{}], size = [{}]", entity.getSimpleName(), result.size());
        return result;
    }

    /**
     * Get paginating list of entities with sort params
     *
     * @param skip       count of rows to skip
     * @param limit      count of rows to select
     * @param sortColumn sorting column
     * @param isAsc      is sorting order ASC
     * @return list of entities
     */
    public List<T> getPagination(int skip, int limit, String sortColumn, boolean isAsc) {
        LOGGER.info("IN getPagination:(skip = [{}], limit = [{}], sort column = [{}], is ASC = [{}])", skip, limit,
                sortColumn, isAsc);
        String sqlString = "from " + entity.getName() + " order by " + sortColumn + " " + ((isAsc) ? "ASC" : "DESC");
        Query query = entityManager.createQuery(sqlString, entity);
        query.setFirstResult(skip);
        query.setMaxResults(limit);
        List<T> result = query.getResultList();
        LOGGER.debug("OUT getPagination:returned list of [{}], size = [{}]", entity.getSimpleName(), result.size());
        return result;
    }

    /**
     * Count all rows. It's needed for pagination logic
     *
     * @return count of results
     */
    public int countAll() {
        LOGGER.info("IN countAllByBookId:(entity = [{}])", entity);
        int result = ((Long) entityManager.createQuery("SELECT count(*) from " +
                entity.getName()).getSingleResult()).intValue();
        LOGGER.debug("OUT countAllByBookId: entity = [{}], count = [{}]", entity.getSimpleName(), result);
        return result;
    }

    /**
     * @param columnName name of the column to check
     * @param value      checked value
     * @return boolean value of is it value unique
     */
    public boolean isUnique(String columnName, String value) {
        LOGGER.info("IN isUnique:(column name = [{}], value = [{}])", columnName, value);
        String sqlString = "select count (*) from " + entity.getName() + " where " + columnName + " = " + value;
        Query query = entityManager.createQuery(sqlString);
        Long result = (Long) query.getSingleResult();
        LOGGER.info("OUT isUnique:(count = [{}])", result);
        return result == 0;
    }
}
