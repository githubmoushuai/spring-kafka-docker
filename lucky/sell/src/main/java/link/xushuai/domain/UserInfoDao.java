package link.xushuai.domain;

import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class UserInfoDao
{
	@Autowired
	private UserInfoRepo userInfoRepo;
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	@PersistenceContext
	protected EntityManager em;

	ArrayList<String> masterList = new ArrayList<String>();

	public UserInfoDao()
	{
		super();
		masterList.add("Total");    //属性大写
	}

	public void save(UserInfo userInfo)
	{
		userInfoRepo.save(userInfo);
	}

	@Transactional
	public void updateSync(String userId, String attribute, String value)
	{

		// 转换mysql属性名
		String field = propertyToField(attribute);
		// 设置查询参数
		String sql = "UPDATE user_info  SET " + field + " =?1 WHERE user_id = ?2";
		Query query = em.createNativeQuery(sql, UserInfo.class);
		query.setParameter(1, value);
		query.setParameter(2, userId);
		// 获取结果
		query.executeUpdate();
		em.clear();
	}

	@Transactional
	public void update(String userId, String attribute, String value)
	{
		if (masterList.contains(attribute))
		{
			// 转换mysql属性名
			String field = propertyToField(attribute);
			// 设置查询参数
			String sql = "UPDATE user_info  SET " + field + " =?1 WHERE user_id = ?2";
			Query query = em.createNativeQuery(sql, UserInfo.class);
			query.setParameter(1, value);
			query.setParameter(2, userId);
			// 获取结果
			query.executeUpdate();
			// List contacts = query.getResultList();
			String attr = attribute.substring(0, 1).toLowerCase() + attribute.substring(1);
			kafkaTemplate.send(attr, userId + "," + value);
			em.clear();
		}
		else
		{
			String attr = attribute.substring(0, 1).toLowerCase() + attribute.substring(1)+"Request";
			kafkaTemplate.send(attr, userId + "," + value);
		}
	}

	public void delete(UserInfo user)
	{
		userInfoRepo.delete(user);
	}

	public UserInfo findByUserId(String userId)
	{
		return userInfoRepo.findByUserId(userId);
	}

	String propertyToField(String attr)
	{
		if (null == attr)
		{
			return "";
		}
		String property = attr.substring(0, 1).toLowerCase() + attr.substring(1);
		char[] chars = property.toCharArray();
		StringBuffer buffer = new StringBuffer();
		for (char c : chars)
		{

			if (c >= 'A' && c <= 'Z')
			{
				buffer.append("_" + String.valueOf(c).toLowerCase());
			} else
			{
				buffer.append(c);
			}
		}
		return buffer.toString();
	}
	public Long count()
	{
		return userInfoRepo.count();
	}
}
