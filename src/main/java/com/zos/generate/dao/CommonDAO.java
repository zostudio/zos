package com.zos.generate.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author 刘超
 */
@Repository("commonDao")
@SuppressWarnings("rawtypes")
public class CommonDAO {

	@PersistenceContext
	private EntityManager em;

	public void save(Object entity) throws Exception {
		em.persist(entity);
	}

	public void delete(Object entity) throws Exception {
		em.remove(entity);
	}

	public Integer delete(String sql) throws Exception {
		em.clear();
		return em.createNativeQuery(sql).executeUpdate();
	}

	public Integer delete(String sql, Map<String, Object> params) throws Exception {
		em.clear();
		Query query = em.createNativeQuery(sql);
		if (params != null) {
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				query.setParameter(entry.getKey(), entry.getValue());
			}
		}
		return query.executeUpdate();
	}

	public Integer deleteByJPAQL(String jpaql, Map<String, Object> params) throws Exception {
		em.clear();
		Query query = em.createQuery(jpaql);
		if (params != null) {
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				query.setParameter(entry.getKey(), entry.getValue());
			}
		}
		return query.executeUpdate();
	}

	public <T> T findById(Class<T> classType, long id) {
		return em.find(classType, id);
	}

	public List findByJPAQL(String jpaql, Map param) throws Exception {
		Query query = em.createQuery(jpaql);
		if (param != null) {
			for (Object key : param.keySet()) {
				query.setParameter(key.toString(), param.get(key.toString()));
			}
		}
		List list = query.getResultList();
		if(list == null) {
			list = new ArrayList();
		}
		return list;
	}

	public Object findSingleResultByJPAQL(String jpaql, Map<String, Object> params) throws Exception {
		Query query = em.createQuery(jpaql);
		if (params != null) {
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				query.setParameter(entry.getKey(), entry.getValue());
			}
		}
		return query.getSingleResult();
	}

	public List findByJPAQL(String jpaql, Map param, boolean needCache)
			throws Exception {
		Query query = em.createQuery(jpaql);
		if (needCache == true)
			query.setHint("org.hibernate.cacheable", true);
		if (param != null) {
			for (Object key : param.keySet()) {
				query.setParameter(key.toString(), param.get(key.toString()));
			}
		}
		List list = query.getResultList();
		if(list == null) {
			list = new ArrayList();
		}
		return list;
	}

	public <T> T saveOrUpdate(T entity, Class<T> entityClass) throws Exception {
		JpaEntityInformation<T, ?> entityInfomation = JpaEntityInformationSupport.getEntityInformation(entityClass, em);
		if (entityInfomation.isNew(entity)) {
			em.persist(entity);
			return entity;
		} else {
			T t = em.merge(entity);
			return t;
		}
	}

	public List findByJPAQL(String jpaql, Map param, int start, int pageSize) {
		Query query = em.createQuery(jpaql);
		if (param != null) {
			for (Object key : param.keySet()) {
				query.setParameter(key.toString(), param.get(key.toString()));
			}
		}
		query.setFirstResult((start - 1) * pageSize);
		query.setMaxResults(pageSize);
		List list = query.getResultList();
		if(list == null) {
			list = new ArrayList();
		}
		return list;
	}

	public List findByJPAQL(String jpaql, Map param, int start, int pageSize,
							boolean needCache) {
		Query query = em.createQuery(jpaql);
		if (needCache == true)
			query.setHint("org.hibernate.cacheable", true);
		if (param != null) {
			for (Object key : param.keySet()) {
				query.setParameter(key.toString(), param.get(key.toString()));
			}
		}
		query.setFirstResult((start - 1) * pageSize);
		query.setMaxResults(pageSize);
		List list = query.getResultList();
		if(list == null) {
			list = new ArrayList();
		}
		return list;
	}

	public <T, ID extends Serializable> T findOne(ID id, Class<T> entityClass) {
		return this.getRepository(entityClass).getOne(id);
	}

	public <T> List<T> findAll(Class<T> entityClass) {
		return this.getRepository(entityClass).findAll();
	}

	public <T> Page<T> getPage(Pageable request, Class<T> entityClass) {
		return this.getRepository(entityClass).findAll(request);
	}

	public <T> List<T> findAll(Specification<T> spec, Class<T> entityClass) {
		return this.getRepository(entityClass).findAll(spec);
	}

	public <T> Page<T> getPageByFilter(Pageable request, Specification<T> spec, Class<T> entityClass) {
		return this.getRepository(entityClass).findAll(spec, request);
	}

	private <T, ID extends Serializable> SimpleJpaRepository<T, ID> getRepository(Class<T> entityClass) {
		SimpleJpaRepository<T, ID> repository = new SimpleJpaRepository<T, ID>(JpaEntityInformationSupport.getEntityInformation(entityClass, em), em);
		return repository;
	}

	public <T> List<T> saveOrUpdates(List<T> entitys, Class<T> entityClass) throws Exception {
		List<T> rtnList = new ArrayList<T>();
		for (T entity : entitys) {
			T saveEntity = this.saveOrUpdate(entity, entityClass);
			rtnList.add(saveEntity);
		}
		return rtnList;
	}

	public <T> List<T> findAllByCriteria(CriteriaQuery<T> cq) {
		return em.createQuery(cq).getResultList();
	}

	public CriteriaBuilder getCriteriaBuilder() {
		return this.em.getCriteriaBuilder();
	}

	public List findBySql(String sql, Map param) throws Exception {
		Query query = em.createNativeQuery(sql);
		if (param != null) {
			for (Object key : param.keySet()) {
				query.setParameter(key.toString(), param.get(key.toString()));
			}
		}
		List list = query.getResultList();
		if(list == null) {
			list = new ArrayList();
		}
		return list;
	}

	public <T> List<T> findBySql(String sql, Map param, Class<T> entity) throws Exception {
		Query query = em.createNativeQuery(sql, entity);
		if (param != null) {
			for (Object key : param.keySet()) {
				query.setParameter(key.toString(), param.get(key.toString()));
			}
		}
		@SuppressWarnings("unchecked")
		List<T> result = query.getResultList();
		return result;
	}

	public List findBySql(String sql, Map param, String resultMappingName) throws Exception {
		Query query = em.createNativeQuery(sql, resultMappingName);
		if (param != null) {
			for (Object key : param.keySet()) {
				query.setParameter(key.toString(), param.get(key.toString()));
			}
		}
		List result = query.getResultList();
		return result;
	}

	public List findByNameNativeQuery(String name, Map param) throws Exception {
		Query query = em.createNamedQuery(name);
		if (param != null) {
			for (Object key : param.keySet()) {
				query.setParameter(key.toString(), param.get(key.toString()));
			}
		}
		List result = query.getResultList();
		return result;
	}

	public void addLockModel(Object entity, LockModeType lockMode) throws Exception {
		em.lock(entity, lockMode);
	}

	public void clearData() throws Exception {
		em.flush();
		em.clear();
	}

	/**
	 * @param hql   "update alm_requisition set cur_phase=?, cur_status=? where req_id=?"
	 * @param param Map<Integer, Object>
	 * @throws Exception
	 */
	public int updateBySql(String hql, Map<Integer, Object> param) throws Exception {
		Query query = em.createNativeQuery(hql);
		for (int i = 1; param != null && param.size() > 0 && i <= param.size(); i++) {
			query.setParameter(i, param.get(i));
		}
		return query.executeUpdate();
	}

}
