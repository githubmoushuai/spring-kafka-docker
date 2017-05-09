package link.xushuai.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@org.springframework.web.bind.annotation.RestController
public class RestController
{
	@Autowired
	UserInfoDao userInfoDao;
	@RequestMapping("/rup/{a}_{b}")
	@ResponseBody
	public void test(@PathVariable("a") String a,@PathVariable("b") String b)
	{
		
		userInfoDao.update(a, "UserPassword", b);
		
	}
	
	
}
