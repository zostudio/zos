package com.zos.activiti.dao.common;

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

/**
 * 基础 DAO
 * 
 * @author 01Studio
 */
public class CommonDAO<T> {

	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * 根据 CriteriaQuery 所给出的条件查询出所有的数据
	 * 
	 * @param criteriaQuery 查询条件
	 * @return List&lt;T&gt;结果集
	 */
	public List<T> findAllByCriteria(CriteriaQuery<T> criteriaQuery) {
		return entityManager.createQuery(criteriaQuery).getResultList();
	}

	/**
	 * 获取 CriteriaBuilder 对象
	 * 
	 * @return CriteriaBuilder
	 */
	public CriteriaBuilder getCriteriaBuilder() {
		return this.entityManager.getCriteriaBuilder();
	}
	
	/**
	 * 提取 EntityManager 对象
	 * 
	 * @return EntityManager
	 */
	public EntityManager getEntityManager() {
		return entityManager;
	}

	/**
	 * 保存指定的对象数据
	 * 
	 * @param entity 对象
	 * @throws Exception
	 */
	public void saveObject(Object entity) throws Exception {
		entityManager.persist(entity);
	}

	/**
	 * 删除指定的对象
	 * 
	 * @param entity 对象信息
	 * @throws Exception
	 */
	public void deleteObject(Object entity) throws Exception {
		entityManager.remove(entity);
	}

	/**
	 * 依据主键查询指定的结果对象
	 * 
	 * @param entityClass 实体类
	 * @param id 主键
	 * @return 实体对象
	 */
	public T findById(Class<T> entityClass, Long id) {
		return entityManager.find(entityClass, id);
	}

	/**
	 * 保存指定的对象
	 * 
	 * @param entity 实体对象
	 * @param entityClass 实体类
	 * @return 实体对象
	 * @throws Exception
	 */
	public T saveOrUpdate(T entity, Class<T> entityClass) throws Exception {
		JpaEntityInformation<T, ?> entityInfomation = JpaEntityInformationSupport.getEntityInformation(entityClass, entityManager);
		if (entityInfomation.isNew(entity)) {
			entityManager.persist(entity);
			return entity;
		} else {
			return entityManager.merge(entity);
		}
	}

	/**
	 * 批量保存指定的结果集(建议数据总量控制在100条以内)
	 * 
	 * @param entitys 实体对象集合
	 * @param entityClass 实体类
	 * @return List&lt;T&gt;结果集
	 * @throws Exception
	 */
	public List<T> saveOrUpdates(List<T> entitys, Class<T> entityClass) throws Exception {
		List<T> rtnList = new ArrayList<T>();
		for (T entity : entitys) {
			rtnList.add(this.saveOrUpdate(entity, entityClass));
		}
		return rtnList;
	}

	/**
	 * 删除或更新 SQL 指定的对象
	 * 
	 * @param sql delete user_info u where u.u_id=1 || update user_info u set u.user_name='01Studio',u.age=18 where u.u_id=1
	 * @return 执行总行数
	 * @throws Exception
	 */
	public Integer deleteOrUpdateBySql(String sql) throws Exception {
		entityManager.clear();
		return entityManager.createNativeQuery(sql).executeUpdate();
	}

	/**
	 * 删除或更新 SQL 指定的对象
	 * 
	 * @param sql delete user_info u where u.u_id=:uId || update user_info u set u.user_name=:userName,u.age=:age where u.u_id=1
	 * @param params 参数 Map&lt;String, Object&gt;{"uId":1L} || Map&lt;String, Object&gt;{"uId":1L,"userName":"01Studio","age":18} 
	 * @return 执行总行数
	 * @throws Exception
	 */
	public Integer deleteOrUpdateBySql(String sql, Map<String, Object> params) throws Exception {
		entityManager.clear();
		Query query = entityManager.createNativeQuery(sql);
		if (params != null) {
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				query.setParameter(entry.getKey(), entry.getValue());
			}
		}
		return query.executeUpdate();
	}

	/**
	 * 原生 SQL 查询指定类型的数据
	 * 
	 * @param sql select * from user_info where u_id=:uId
	 * @param param 参数 Map&lt;String, Object&gt;{"uId":1L}
	 * @return List<T> 结果集
	 * @throws Exception
	 */
	public List<T> findBySql(String sql, Map<String, Object> param) throws Exception {
		Query query = entityManager.createNativeQuery(sql);
		if (param != null) {
			for (Object key : param.keySet()) {
				query.setParameter(key.toString(), param.get(key.toString()));
			}
		}
		@SuppressWarnings("unchecked")
		List<T> list = query.getResultList();
		if(list == null) {
			list = new ArrayList<>();
		}
		return list;
	}

	/**
	 * 原生 SQL 查询指定类型的数据
	 * 
	 * @param sql select * from user_info where u_id=:uId
	 * @param param 参数 Map&lt;String, Object&gt;{"uId":1L}
	 * @param entity UserInfo.class
	 * @return List<T> 结果集
	 * @throws Exception
	 */
	public List<T> findBySql(String sql, Map<String, Object> param, Class<T> entity) throws Exception {
		Query query = entityManager.createNativeQuery(sql, entity);
		if (param != null) {
			for (Object key : param.keySet()) {
				query.setParameter(key.toString(), param.get(key.toString()));
			}
		}
		@SuppressWarnings("unchecked")
		List<T> result = query.getResultList();
		if(result == null) {
			result = new ArrayList<>();
		}
		return result;
	}

	/**
	 * 删除或更新 JPA 指定的数据
	 * 
	 * @param jpaql JPA delete UserInfo u where u.uId=1 || update UserInfo u set u.userName='01Studio',u.age=18 where u.uId=1
	 * @return 执行总行数
	 * @throws Exception
	 */
	public Integer deleteOrUpdateByJPAQL(String jpaql) throws Exception {
		entityManager.clear();
		return entityManager.createQuery(jpaql).executeUpdate();
	}

	/**
	 * 删除或更新 JPA 指定的数据
	 * 
	 * @param jpaql JPA delete UserInfo u where u.uId=:uId || update UserInfo u set u.userName=:userName,u.age=:age where u.uId=:uId
	 * @param params 参数 Map&lt;String, Object&gt;{"uId":1L} || Map&lt;String, Object&gt;{"uId":1L,"userName":"01Studio","age":18}
	 * @return 执行总行数
	 * @throws Exception
	 */
	public Integer deleteOrUpdateByJPAQL(String jpaql, Map<String, Object> params) throws Exception {
		entityManager.clear();
		Query query = entityManager.createQuery(jpaql);
		if (params != null) {
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				query.setParameter(entry.getKey(), entry.getValue());
			}
		}
		return query.executeUpdate();
	}

	/**
	 * 根据 JPA 查询指定的数据
	 * 
	 * @param jpaql JPA select u from UserInfo u where u.uId=:uId
	 * @param param 参数 Map&lt;String, Object&gt;{"uId":1L}
	 * @return List&lt;T&gt;结果集
	 * @throws Exception
	 */
	public List<T> findByJPAQL(String jpaql, Map<String, Object> param) throws Exception {
		Query query = entityManager.createQuery(jpaql);
		if (param != null) {
			for (Object key : param.keySet()) {
				query.setParameter(key.toString(), param.get(key.toString()));
			}
		}
		@SuppressWarnings("unchecked")
		List<T> list = query.getResultList();
		if(list == null) {
			list = new ArrayList<>();
		}
		return list;
	}

	/**
	 * 根据 JPA 查询指定的分页数据(是否缓存数据)
	 * 
	 * @param jpaql JPA select u from UserInfo u where u.uId=:uId
	 * @param param 参数 Map&lt;String, Object&gt;{"uId":1L}
	 * @param needCache 是否缓存
	 * @return List&lt;T&gt;结果集
	 * @throws Exception
	 */
	public List<T> findByJPAQL(String jpaql, Map<String, Object> param, boolean needCache)
			throws Exception {
		Query query = entityManager.createQuery(jpaql);
		if (needCache == true)
			query.setHint("org.hibernate.cacheable", true);
		if (param != null) {
			for (Object key : param.keySet()) {
				query.setParameter(key.toString(), param.get(key.toString()));
			}
		}
		@SuppressWarnings("unchecked")
		List<T> list = query.getResultList();
		if(list == null) {
			list = new ArrayList<>();
		}
		return list;
	}

	/**
	 * 根据 JPA 查询指定的分页数据
	 * 
	 * @param jpaql JPA select u from UserInfo u where u.uId=:uId
	 * @param param 参数 Map&lt;String, Object&gt;{"uId":1L}
	 * @param start 页码
	 * @param pageSize 每页个数
	 * @return List&lt;T&gt;结果集
	 */
	public List<T> findByJPAQL(String jpaql, Map<String, Object> param, Integer start, Integer pageSize) {
		Query query = entityManager.createQuery(jpaql);
		if (param != null) {
			for (Object key : param.keySet()) {
				query.setParameter(key.toString(), param.get(key.toString()));
			}
		}
		query.setFirstResult((start - 1) * pageSize);
		query.setMaxResults(pageSize);
		@SuppressWarnings("unchecked")
		List<T> list = query.getResultList();
		if(list == null) {
			list = new ArrayList<>();
		}
		return list;
	}

	/**
	 * 根据 JPA 查询指定的分页数据(是否缓存数据)
	 * 
	 * @param jpaql JPA select u from UserInfo u where u.uId=:uId
	 * @param param 参数 Map&lt;String, Object&gt;{"uId":1L}
	 * @param start 页码
	 * @param pageSize 每页个数
	 * @param needCache 是否缓存
	 * @return List&lt;T&gt;结果集
	 */
	public List<T> findByJPAQL(String jpaql, Map<String, Object> param, Integer start, Integer pageSize, boolean needCache) {
		Query query = entityManager.createQuery(jpaql);
		if (needCache == true)
			query.setHint("org.hibernate.cacheable", true);
		if (param != null) {
			for (Object key : param.keySet()) {
				query.setParameter(key.toString(), param.get(key.toString()));
			}
		}
		query.setFirstResult((start - 1) * pageSize);
		query.setMaxResults(pageSize);
		@SuppressWarnings("unchecked")
		List<T> list = query.getResultList();
		if(list == null) {
			list = new ArrayList<>();
		}
		return list;
	}
	
	/**
	 * 根据 JPA 查询指定的一条数据
	 * 
	 * @param jpaql JPA select u from UserInfo u where u.uId=:uId
	 * @param param 参数 Map&lt;String, Object&gt;{"uId":1L}
	 * @return 任意结果值
	 * @throws Exception
	 */
	public Object findSingleResultByJPAQL(String jpaql, Map<String, Object> params) throws Exception {
		Query query = entityManager.createQuery(jpaql);
		if (params != null) {
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				query.setParameter(entry.getKey(), entry.getValue());
			}
		}
		return query.getSingleResult();
	}

	/**
	 * 依据主键删除指定的数据
	 * 
	 * @param id 主键
	 * @param entityClass 实体类
	 */
	public void delete(Long id, Class<T> entityClass) {
		this.getRepository(entityClass).deleteById(id);
	}

	/**
	 * 依据主键查询指定的数据
	 * 
	 * @param id 主键
	 * @param entityClass 实体类
	 * @return 执行的一个结果集
	 */
	public T findOne(Long id, Class<T> entityClass) {
		return this.getRepository(entityClass).getOne(id);
	}

	/**
	 * 查询所有的数据
	 * 
	 * @param entityClass 实体类
	 * @return List&lt;T&gt;结果集
	 */
	public List<T> findAll(Class<T> entityClass) {
		return this.getRepository(entityClass).findAll();
	}

	/**
	 * 查询所有的分页数据
	 * 
	 * @param pageable 分页参数
	 * @param entityClass 实体类
	 * @return 分页数据
	 */
	public Page<T> getPage(Pageable pageable, Class<T> entityClass) {
		return this.getRepository(entityClass).findAll(pageable);
	}

	/**
	 * 查询所有的数据
	 * 
	 * @param specification 查询条件
	 * @param entityClass 实体类
	 * @return List&lt;T&gt;结果集
	 */
	public List<T> findAll(Specification<T> specification, Class<T> entityClass) {
		return this.getRepository(entityClass).findAll(specification);
	}

	/**
	 * 条件查询分页数据
	 * 
	 * @param pageable 分页参数
	 * @param specification 查询条件
	 * @param entityClass 实体类
	 * @return 分页数据
	 */
	public Page<T> getPageByFilter(Pageable pageable, Specification<T> specification, Class<T> entityClass) {
		return this.getRepository(entityClass).findAll(specification, pageable);
	}

	/**
	 * 获取 SimpleJpaRepository&lt;UserInfo, Serializable&gt;
	 * @param entityClass UserInfo.class
	 * @return SimpleJpaRepository
	 */
	protected SimpleJpaRepository<T, Serializable> getRepository(Class<T> entityClass) {
		return new SimpleJpaRepository<T, Serializable> (JpaEntityInformationSupport.getEntityInformation(entityClass, entityManager), entityManager);
	}

	/**
	   <pre>
	   @NamedQueries({
	       @NamedQuery(name="findAllUser",query="SELECT u FROM User u"),
	       @NamedQuery(name="findUserWithId",query="SELECT u FROM User u WHERE u.u_id = :uId"),
	       @NamedQuery(name="findUserWithName",query="SELECT u FROM User u WHERE u.user_name = :userName")
	   })
	   @NamedNativeQueries({
	       @NamedNativeQuery(name="getNativeUserInfo", query="{call user_info(:uId, userName:)}",resultSetMapping="ReturnUserInfoList")
	       @NamedNativeQuery(name="getNativeUserColumn",query="select u_id,user_name from NutShellInfo where u_id=:uId and user_name=:userName",resultSetMapping="ReturnUserColumnList")
	   })
	   @SqlResultSetMappings({
	       @SqlResultSetMapping(
	           name="ReturnUserInfoList",
	           entities={
	               @EntityResult(entityClass=UserInfo.class,fields={@FieldResult(name="uId",column="u_id"),@FieldResult(name="userName",column="user_name")})
	           }
	       ),
	       @SqlResultSetMapping(
	           name="ReturnUserColumnList",
	           entities={},
	           columns={@ColumnResult(name="u_id"),@ColumnResult(name="user_name")}
	       )
	   })
	   </pre>
	 * @param sql 原生SQL语句
	 * @param param 参数 Map&lt;String, Object&gt;{"uId":1L, "userName":"01Studio"}
	 * @param resultMappingName 结果集映射名称
	 * @return List<T> 结果集
	 * @throws Exception
	 */
	public List<T> findByNameNativeSql(String sql, Map<String, Object> param, String resultMappingName) throws Exception {
		Query query = entityManager.createNativeQuery(sql, resultMappingName);
		if (param != null) {
			for (Object key : param.keySet()) {
				query.setParameter(key.toString(), param.get(key.toString()));
			}
		}
		@SuppressWarnings("unchecked")
		List<T> result = query.getResultList();
		if(result == null) {
			result = new ArrayList<>();
		}
		return result;
	}

	/**
	   <pre>
	   @NamedQueries({
	       @NamedQuery(name="findAllUser",query="SELECT u FROM User u"),
	       @NamedQuery(name="findUserWithId",query="SELECT u FROM User u WHERE u.u_id = :uId"),
	       @NamedQuery(name="findUserWithName",query="SELECT u FROM User u WHERE u.user_name = :userName")
	   })
	   @NamedNativeQueries({
	       @NamedNativeQuery(name="getNativeUserInfo", query="{call user_info(:uId, userName:)}",resultSetMapping="ReturnUserInfoList")
	       @NamedNativeQuery(name="getNativeUserColumn",query="select u_id,user_name from NutShellInfo where u_id=:uId and user_name=:userName",resultSetMapping="ReturnUserColumnList")
	   })
	   @SqlResultSetMappings({
	       @SqlResultSetMapping(
	           name="ReturnUserInfoList",
	           entities={
	               @EntityResult(entityClass=UserInfo.class,fields={@FieldResult(name="uId",column="u_id"),@FieldResult(name="userName",column="user_name")})
	           }
	       ),
	       @SqlResultSetMapping(
	           name="ReturnUserColumnList",
	           entities={},
	           columns={@ColumnResult(name="u_id"),@ColumnResult(name="user_name")}
	       )
	   })
	   </pre>
	 * @param name 名称 getNativeUser
	 * @param param 参数 Map&lt;String, Object&gt;{"uId":1L, "userName":"01Studio"}
	 * @return List<T> 结果集
	 * @throws Exception
	 */
	public List<T> findByNameNativeQuery(String name, Map<String, Object> param) throws Exception {
		Query query = entityManager.createNamedQuery(name);
		if (param != null) {
			for (Object key : param.keySet()) {
				query.setParameter(key.toString(), param.get(key.toString()));
			}
		}
		@SuppressWarnings("unchecked")
		List<T> result = query.getResultList();
		if(result == null) {
			result = new ArrayList<>();
		}
		return result;
	}

	/**
	 * 
	 * @param entity 实体类
	 * @param lockMode 锁定模式
	 * @throws Exception
	 */
	public void addLockModel(Object entity, LockModeType lockMode) throws Exception {
		entityManager.lock(entity, lockMode);
	}

	/**
	 * @throws Exception
	 */
	public void clearData() throws Exception {
		entityManager.flush();
		entityManager.clear();
	}
}
