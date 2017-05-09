package link.xushuai.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


public interface UserInfoRepo extends JpaRepository<UserInfo,String>  
{
	List<UserInfo> findAll();
	UserInfo findByUserId(String userId );


}
