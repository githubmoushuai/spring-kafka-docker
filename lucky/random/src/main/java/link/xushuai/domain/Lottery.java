package link.xushuai.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Lottery
{
	@Id
	
	private Long sellId;
	private String sellTime;
	
	
	
	public Long getSellId()
	{
		return sellId;
	}

	public void setSellId(Long sellId)
	{
		this.sellId = sellId;
	}

	public String getSellTime()
	{
		return sellTime;
	}

	public void setSellTime(String sellTime)
	{
		this.sellTime = sellTime;
	}
	public Lottery()
	{
		super();
	}
	public Lottery(Long sellId,String sellTime)
	{
		super();
		this.sellId=sellId;
		this.sellTime = sellTime;
	}
	
}
