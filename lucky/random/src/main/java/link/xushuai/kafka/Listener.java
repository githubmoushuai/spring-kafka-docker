package link.xushuai.kafka;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import link.xushuai.domain.Lottery;
import link.xushuai.domain.LotteryDao;



public class Listener
{
	@Autowired
	private LotteryDao lotteryDao;
	
	@KafkaListener(topics = "sellId")
	public void listen1(String data)
	{
		String[] s = data.split(",");
		Lottery lottery=lotteryDao.findBySellId(new Long(s[0]));
				if (lottery==null) lotteryDao.save(new Lottery(new Long(s[0]),s[1]));
			
		
	}

	@KafkaListener(topics = "sellTime")
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
			Lottery lottery = lotteryDao.findBySellId(Long.valueOf(s[0]));
			if (lottery != null)
			{
				lotteryDao.updateSync(s[0], "SellTime", s[1]);
			} else
			{
				if(flag) new Thread(new BufferThread(this, 2, "$," + data)).start();
			}
	}
	
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
					
			Thread.sleep(5000);
			
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
			
			}

		}

	}
}
