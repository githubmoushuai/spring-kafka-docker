package link.xushuai.domain;

import java.util.List;

import org.apache.ibatis.annotations.*;
import org.springframework.data.jpa.repository.JpaRepository;



public interface SellInfoRepo extends JpaRepository<SellInfo,Long>  
{
	
	List<SellInfo> findAll();
	
	SellInfo findBySellId(String sellId );
	
	List<SellInfo> findByUserId(@Param("userId") String userId);


}
