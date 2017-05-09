package link.xushuai.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import link.xushuai.domain.User;
import link.xushuai.domain.UserDao;


public class Listener
{
	@Autowired
	private UserDao userDao;
	@KafkaListener(topics = "total")
	public void listen1(String data) 
	{
		String[] s = data.split(",");
	     User user= userDao.findByUserId(s[0]);
	     if(user!=null)
	     {
	    	userDao.updateSync(s[0], "Total", s[1]);
	     }
	     
	}
	
	@KafkaListener(topics = "userPasswordRequest")  //针对userPassword的修改请求
	public void listen2(String data) 
	{
		String[] s = data.split(",");
	     User user= userDao.findByUserId(s[0]);
	     if(user!=null)
	     {
	    	userDao.update(s[0], "UserPassword", s[1]);
	     }
	}
}
