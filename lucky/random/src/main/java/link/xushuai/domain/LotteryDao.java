package link.xushuai.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
@Repository
public class LotteryDao
{
	@Autowired
	private LotteryRepo lotteryRepo;
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	@PersistenceContext
	protected EntityManager em;
	public void save(Lottery lottery)
	{
		lotteryRepo.save(lottery);
	}
	public List<Lottery> findAll()
	{
		return lotteryRepo.findAll();
	}
	public Lottery findBySellId(Long id)
	{
		return lotteryRepo.findBySellId(id);
	}
	public List<Lottery> findBySellDay(String filterText)
	{
		List <Lottery> list=lotteryRepo.findAll();
		List <Lottery> _list=new ArrayList<Lottery>();
		for(Lottery lottery:list )
		{
			String[] s = lottery.getSellTime().split(" ");
			if(filterText.equals(s[0]))
			{
				_list.add(lottery);
			}
		}
		return _list;
	}
	@Transactional
	public void updateSync(String userId, String attribute, String value)
	{

		// 转换mysql属性名
		String field = propertyToField(attribute);
		// 设置查询参数
		String sql = "UPDATE lottery  SET " + field + " =?1 WHERE sell_id = "+userId;
		Query query = em.createNativeQuery(sql, Lottery.class);
		query.setParameter(1, value);
		
		// 获取结果
		query.executeUpdate();
		em.clear();
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

}
