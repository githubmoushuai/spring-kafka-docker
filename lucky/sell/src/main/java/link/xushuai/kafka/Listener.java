package link.xushuai.kafka;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.*;
import link.xushuai.domain.UserInfo;
import link.xushuai.domain.UserInfoDao;

public class Listener
{
	@Autowired
	private UserInfoDao userInfoDao;
	@PersistenceContext
	protected EntityManager em;
	@KafkaListener(topics="userId")
	public void listen1(String data)
	{
		String[] s = data.split(",");
		UserInfo userInfo = userInfoDao.findByUserId(s[0]);
		if (s[1].equals("$"))
		{
			if (userInfo != null)
				userInfoDao.delete(userInfo);
		} else
		{
			if (userInfo == null)
				userInfoDao.save(new UserInfo(s[1]));
		}

	}

	@KafkaListener(topics = "userPassword")
	public void listen2(String data)
	{
		boolean flag=true;   //true是第一次到来
		String[] s = data.split(",");
		if (s[0].equals("$"))
		{
			flag=false;
			data=data.substring(2);
		} 
		s = data.split(",");
			UserInfo userInfo = userInfoDao.findByUserId(s[0]);
			if (userInfo != null)
			{
				userInfoDao.updateSync(s[0], "UserPassword", s[1]);
			} else
			{
				if(flag) new Thread(new BufferThread(this, 2, "$," + data)).start();
			}
		
	}
	@KafkaListener(topics = "account")
	public void listen3(String data)
	{
		boolean flag=true;   //true是第一次到来
		String[] s = data.split(",");
		if (s[0].equals("$"))
		{
			flag=false;
			data=data.substring(2);
		} 
		s = data.split(",");
			UserInfo userInfo = userInfoDao.findByUserId(s[0]);
			if (userInfo != null)
			{
				userInfoDao.updateSync(s[0], "Account", s[1]);
			} else
			{
				if(flag) new Thread(new BufferThread(this, 3, "$," + data)).start();
			}
		
	}
	
	@KafkaListener(topics="userId")
	public void listen4(String data)
	{
		String[] s = data.split(",");
		UserInfo userInfo = userInfoDao.findByUserId(s[0]);
		if (s[1].equals("$"))
		{
			if (userInfo != null)
				userInfoDao.delete(userInfo);
		} else
		{
			if (userInfo == null)
				userInfoDao.save(new UserInfo(s[1]));
		}

	}
	@KafkaListener(topics="userId")
	public void listen5(String data)
	{
		String[] s = data.split(",");
		UserInfo userInfo = userInfoDao.findByUserId(s[0]);
		if (s[1].equals("$"))
		{
			if (userInfo != null)
				userInfoDao.delete(userInfo);
		} else
		{
			if (userInfo == null)
				userInfoDao.save(new UserInfo(s[1]));
		}

	}
	@KafkaListener(topics="userId")
	public void listen6(String data)
	{
		String[] s = data.split(",");
		UserInfo userInfo = userInfoDao.findByUserId(s[0]);
		if (s[1].equals("$"))
		{
			if (userInfo != null)
				userInfoDao.delete(userInfo);
		} else
		{
			if (userInfo == null)
				userInfoDao.save(new UserInfo(s[1]));
		}

	}
	@KafkaListener(topics="userId")
	public void listen7(String data)
	{
		String[] s = data.split(",");
		UserInfo userInfo = userInfoDao.findByUserId(s[0]);
		if (s[1].equals("$"))
		{
			if (userInfo != null)
				userInfoDao.delete(userInfo);
		} else
		{
			if (userInfo == null)
				userInfoDao.save(new UserInfo(s[1]));
		}

	}
//	@KafkaListener(topics = "account")
//	public void listen3(String data)
//	{
//		String[] s = data.split(",");
//		UserInfo userInfo = userInfoMapper.findByUserId(s[0]);
//		if (userInfo != null)
//		{
//			userInfo.setAccount(s[1]);
//			userInfoMapper.save(userInfo);
//		}
//	}

}

class BufferThread implements Runnable
{
	Listener listener;
	String data;
	int which;

	public BufferThread()
	{
		super();
	}

	public BufferThread(Listener listenner, int which, String data)
	{
		this.listener = listenner;
		this.data = data;
		this.which = which;
	}

	@Override
	public void run()
	{
		try
		{
			//System.out.println(String.valueOf(which)+"进入缓冲");
			
			Thread.sleep(5000);
			
			//System.out.println(String.valueOf(which)+"重试");
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally
		{
			switch (which)
			{
			case 1:
				listener.listen1(data);
				break;
			case 2:
				listener.listen2(data);
				break;
			case 3:
				listener.listen3(data);
				break;
			}

		}

	}
}
