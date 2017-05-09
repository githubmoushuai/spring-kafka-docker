package link.xushuai.domain;

import org.springframework.data.jpa.repository.JpaRepository;



public interface LotteryRepo extends JpaRepository<Lottery,Long>  
{

	Lottery findBySellId(Long sellId );     

}

