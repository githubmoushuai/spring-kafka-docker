package link.xushuai.domain;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@org.springframework.web.bind.annotation.RestController
public class RestController
{
	@Autowired
	UserDao userDao;

	@RequestMapping("/add/{a}_{b}_{c}_{d}")
	@ResponseBody
	public String test(@PathVariable("a") String a, @PathVariable("b") String b, @PathVariable("c") String c,
			@PathVariable("d") String d)
	{
		User user = new User(a, b, c, d);
		return userDao.save(user);

	}

	@RequestMapping("/alluser")
	@ResponseBody
	public List<User> listAllUser()
	{
		return userDao.findAll();
	}

	@RequestMapping("/deleteall")
	public void test2()
	{
		userDao.deleteAll();
	}
	@RequestMapping("/count")
	public String count()
	{
		return userDao.count().toString();
	}
}
