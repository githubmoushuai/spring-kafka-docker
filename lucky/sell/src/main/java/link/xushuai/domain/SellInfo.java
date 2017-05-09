package link.xushuai.domain;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;
import java.text.SimpleDateFormat;
@Entity
public class SellInfo
{
	@Id
	@GeneratedValue
	private Long sellId;
	private String userId;
	private String sellTime;
	
	
	public Long getSellId()
	{
		return sellId;
	}
	public void setSellId(Long sellId)
	{
		this.sellId = sellId;
	}
	public String getUserId()
	{
		return userId;
	}
	public void setUserId(String userId)
	{
		this.userId = userId;
	}
	public String getSellTime()
	{
		return sellTime;
	}
	public void setSellTime(String sellTime)
	{
		this.sellTime = sellTime;
	}
	public SellInfo()
	{
		super();
	}
	public SellInfo(String userId)
	{
		super();
		this.userId = userId;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		this.sellTime=df.format(new Date());// new Date()为获取当前系统时间
	}	
	
}
