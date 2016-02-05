package crud.roo.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.PersistenceContext;
import javax.persistence.Version;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Inheritance
public class GenericEntity {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

	@Version
    @Column(name = "version")
    private Integer version;

	public Long getId() {
        return this.id;
    }

	public void setId(Long id) {
        this.id = id;
    }

	public Integer getVersion() {
        return this.version;
    }

	public void setVersion(Integer version) {
        this.version = version;
    }

	public GenericEntity() {
		super();
	}

	public String toString() {
	    return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	@PersistenceContext
    transient EntityManager entityManager;

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("email", "firstName", "lastName");

	public static GenericEntity newInstance(Class<? extends GenericEntity> entityClass) throws InstantiationException, IllegalAccessException {
		return (GenericEntity) entityClass.newInstance();
	}
	
	public static final EntityManager entityManager(Class<? extends GenericEntity> entityClass) throws InstantiationException, IllegalAccessException {
        EntityManager em = newInstance(entityClass).entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countPeople(Class<? extends GenericEntity> entityClass) throws InstantiationException, IllegalAccessException {
        return entityManager(entityClass).createQuery("SELECT COUNT(o) FROM " + getClassName(entityClass) + " o", Long.class).getSingleResult();
    }

	public static List<GenericEntity> findAllPeople(Class<? extends GenericEntity> entityClass) throws InstantiationException, IllegalAccessException {
        return (List<GenericEntity>) entityManager(entityClass).createQuery("SELECT o FROM " + getClassName(entityClass) + " o", entityClass).getResultList();
    }

	public static List<GenericEntity> findAllPeople(String sortFieldName, String sortOrder, Class<? extends GenericEntity> entityClass) throws InstantiationException, IllegalAccessException {
        String jpaQuery = "SELECT o FROM " + getClassName(entityClass) + " o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return (List<GenericEntity>) entityManager(entityClass).createQuery(jpaQuery, entityClass).getResultList();
    }

	public static GenericEntity findPerson(Long id, Class<? extends GenericEntity> entityClass) throws InstantiationException, IllegalAccessException {
        if (id == null) return null;
        return (GenericEntity) entityManager(entityClass).find(entityClass, id);
    }

	public static List<? extends GenericEntity> findPersonEntries(int firstResult, int maxResults, Class<? extends GenericEntity> entityClass) throws InstantiationException, IllegalAccessException {
        return entityManager(entityClass).createQuery("SELECT o FROM " + getClassName(entityClass) + " o", entityClass).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<GenericEntity> findPersonEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder, Class<? extends GenericEntity> entityClass) throws InstantiationException, IllegalAccessException {
        String jpaQuery = "SELECT o FROM " + getClassName(entityClass) + " o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return (List<GenericEntity>) entityManager(entityClass).createQuery(jpaQuery, entityClass).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	@Transactional
    public void persist() {
        this.entityManager.persist(this);
    }

	@Transactional
    public void remove() throws InstantiationException, IllegalAccessException {
        //if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            GenericEntity attached = findPerson(this.id, this.getClass());
            this.entityManager.remove(attached);
        }
    }

	@Transactional
    public void flush() {
        //if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }

	@Transactional
    public void clear() {
        //if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }

	@Transactional
    public Object merge() {
        //if (this.entityManager == null) this.entityManager = entityManager();
        Object merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
	
	private static String getClassName(Class<? extends GenericEntity> entityClass) {
		String[] splitted = entityClass.getName().split("\\.");
		return splitted[splitted.length-1];
	}
}