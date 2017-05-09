package link.xushuai.domain;

import java.util.List;

import org.apache.ibatis.annotations.*;
import org.springframework.data.jpa.repository.JpaRepository;


//@Mapper
public interface UserRepo extends JpaRepository<User,String>  
{
//	@Select("SELECT * FROM user")
//	List<User> findAll();
//	
	
	//@Select("SELECT * FROM user WHERE userId = #{userId};")
	User findByUserId(String userId );
	
	//@Select("SELECT * FROM user WHERE name = #{name};")
	List<User> findByName(@Param("name") String name);

//
//	
//
//	@Insert("INSERT INTO user VALUES(#{name},'','','','','');")
//	int insert(@Param("name") String name);

}
