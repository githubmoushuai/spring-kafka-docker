package link.xushuai.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

import java.util.Date;
import java.text.SimpleDateFormat;
@Entity
public class User
{
	@Id
	private String userId;
	private String userPassword;
	private String name;
	private String account;
	private String regTime;
	private String total;
	
	public String getUserId()
	{
		return userId;
	}
	public void setUserId(String userId)
	{
		this.userId = userId;
	}
	public String getUserPassword()
	{
		return userPassword;
	}
	public void setUserPassword(String userPassword)
	{
		this.userPassword = userPassword;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getAccount()
	{
		return account;
	}
	public void setAccount(String account)
	{
		this.account = account;
	}
	public String getRegTime()
	{
		return regTime;
	}
	public void setRegTime(String regTime)
	{
		this.regTime = regTime;
	}
	public String getTotal()
	{
		return total;
	}
	public void setTotal(String total)
	{
		this.total = total;
	}
	
	public User()
	{
		super();
	}

	public User(String userId, String userPassword, String name, String account)
	{
		super();
		this.userId = userId;
		this.userPassword = userPassword;
		this.name = name;
		this.account = account;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
		this.regTime=df.format(new Date());// new Date()为获取当前系统时间
		this.total="0";
	}	
	
}
