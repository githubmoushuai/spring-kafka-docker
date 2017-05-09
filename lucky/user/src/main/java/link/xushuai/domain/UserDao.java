package link.xushuai.domain;

import java.util.List;
import java.util.Random;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class UserDao
{
	@Autowired
	private UserRepo userRepo;
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	Random r=new Random();
	@PersistenceContext
	protected EntityManager em;
	
	public String save(User user)
	{
		User u = userRepo.findByUserId(user.getUserId());
		if (u != null)
		{
			return "fail";
		} else
		{
			userRepo.save(user);
			int partition = r.nextInt(5);
			kafkaTemplate.send("userId" ,partition, user.getUserId() + "," + user.getUserId());
				kafkaTemplate.send("userPassword",partition, user.getUserId() + "," + user.getUserPassword());
				kafkaTemplate.send("name",partition, user.getUserId() + "," + user.getName());
				kafkaTemplate.send("account", partition,user.getUserId() + "," + user.getAccount());
				kafkaTemplate.send("regTime", partition,user.getUserId() + "," + user.getRegTime());
			return "success";
		}

	}

	@Transactional
	public void update(String userId, String attribute, String value)
	{

		//转换mysql属性名
		String field=propertyToField(attribute);
		// 设置查询参数
		String	sql = "UPDATE user  SET "+field+" =?1 WHERE user_id = ?2";			
		Query query = em.createNativeQuery(sql, User.class);
		query.setParameter(1, value);
		query.setParameter(2, userId);
		// 获取结果
		query.executeUpdate();
		// List contacts = query.getResultList();
		String attr = attribute.substring(0, 1).toLowerCase() + attribute.substring(1);
		kafkaTemplate.send(attr, userId + "," + value);
		em.clear();
	}
	
	@Transactional
	public void updateSync(String userId, String attribute, String value)
	{

		//转换mysql属性名
		String field=propertyToField(attribute);
		// 设置查询参数
		String	sql = "UPDATE user  SET "+field+" =?1 WHERE user_id = ?2";			
		Query query = em.createNativeQuery(sql, User.class);
		query.setParameter(1, value);
		query.setParameter(2, userId);
		// 获取结果
		query.executeUpdate();
		em.clear();
	}

	public void delete(User user)
	{
		userRepo.delete(user);
		kafkaTemplate.send("userId", user.getUserId() + ",$");
	}

	public User findByUserId(String userId)
	{
		return userRepo.findByUserId(userId);
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
			
			if (c>='A'&&c<='Z')
			{
				buffer.append("_" + String.valueOf(c).toLowerCase());
			} else
			{
				buffer.append(c);
			}
		}
		return buffer.toString();
	}

	public void deleteAll()
	{
		// TODO Auto-generated method stub
		userRepo.deleteAll();
	}

	public List<User> findAll()
	{
		return userRepo.findAll();
	}
	
	public Long count()
	{
		return userRepo.count();
	}

}
