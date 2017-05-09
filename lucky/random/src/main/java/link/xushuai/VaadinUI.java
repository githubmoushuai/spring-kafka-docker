package link.xushuai;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import link.xushuai.domain.Lottery;
import link.xushuai.domain.LotteryDao;


@SpringUI
@Theme("valo")

public class VaadinUI extends UI
{

	private final LotteryDao lotteryDao;

	final Grid<Lottery> grid;

	final TextField filter;
	final TextField lucky;

	private final Button luckyBtn;

	@Autowired
	public VaadinUI(LotteryDao lotteryDao)
	{
		this.lotteryDao = lotteryDao;
		this.grid = new Grid<>(Lottery.class);
		this.filter = new TextField();
		this.lucky=new TextField();
		this.luckyBtn = new Button("Lucky~GO!");
	}

	@Override
	protected void init(VaadinRequest request)
	{
		
		HorizontalLayout actions = new HorizontalLayout(filter, lucky,luckyBtn);
		VerticalLayout mainLayout = new VerticalLayout( actions, grid);
		setContent(mainLayout);

		grid.setHeight(600, Unit.PIXELS);
		grid.setWidth(700, Unit.PIXELS);
		grid.setColumns("sellId", "sellTime");

		filter.setPlaceholder("Filter by sellTime");
		filter.setValueChangeMode(ValueChangeMode.LAZY);
		filter.addValueChangeListener(e -> listUsers(e.getValue()));
		
		lucky.setEnabled(false);

		luckyBtn.addClickListener(e ->
		{
              //抽奖程序
			List <String> date=getLastSevendays();
			List <Lottery> list=new ArrayList<Lottery>();
			for(String s:date)
			{
				list.addAll(lotteryDao.findBySellDay(s));
			}
			
			int num=list.size();
			Random r=new Random();
			
			if(!list.isEmpty()) lucky.setValue(list.get(r.nextInt(num)).getSellId().toString());
		});

		// Initialize listing
		listUsers(null);
	}

	// tag::listCustomers[]
	void listUsers(String filterText)
	{
		if (StringUtils.isEmpty(filterText))
		{
			grid.setItems(lotteryDao.findAll());
		} else
		{
			grid.setItems(lotteryDao.findBySellDay(filterText));
		}
	}
	// 得到最近七天的日期字符串
	List<String> getLastSevendays()
	{
		List<String>list =new ArrayList<String>();
		SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
		Date beginDate = new Date();
		Calendar date = Calendar.getInstance();
		date.setTime(beginDate);
		for(int i=0;i<7;i++)
		{
			list.add(dft.format(date.getTime()));
			date.set(Calendar.DATE, date.get(Calendar.DATE)-1);
		}
		return list;
		
	}

}