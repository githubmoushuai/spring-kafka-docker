package link.xushuai.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class UserInfo
{
	@Id
	private String userId;
	private String userPassword;
	private String account;
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

	public String getAccount()
	{
		return account;
	}

	public void setAccount(String account)
	{
		this.account = account;
	}

	public String getTotal()
	{
		return total;
	}

	public void setTotal(String total)
	{
		this.total = total;
	}

	public UserInfo()
	{
		super();
	}

	public UserInfo(String userId)
	{
		super();
		this.userId = userId;
		this.total="0";
	}	
	
}
