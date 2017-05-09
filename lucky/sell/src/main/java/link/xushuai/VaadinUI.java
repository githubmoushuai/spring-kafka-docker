package link.xushuai;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import link.xushuai.domain.SellInfo;
import link.xushuai.domain.SellInfoRepo;
import link.xushuai.domain.UserInfoDao;

@SpringUI
@Theme("valo")

public class VaadinUI extends UI
{
	private DiscoveryClient discoveryClient;
	private RestTemplate restTemplate;
	private Embedded embeded = new Embedded();
	private final SellInfoRepo sellInfoMapper;
	private final UserInfoDao userInfoDao;
	private final Seller editor;
	private final passwordChanger changer;

	final Grid<SellInfo> grid;

	final TextField filter;

	private final Button addNewBtn;
	private final Button countSync;

	@Autowired
	public VaadinUI(DiscoveryClient discoveryClient, UserInfoDao userInfoDao, SellInfoRepo sellInfoMapper,
			Seller editor, passwordChanger changer)
	{
		this.discoveryClient = discoveryClient;
		this.restTemplate = new RestTemplate();
		this.sellInfoMapper = sellInfoMapper;
		this.userInfoDao = userInfoDao;
		this.editor = editor;
		this.changer = changer;
		this.grid = new Grid<>(SellInfo.class);
		this.filter = new TextField();
		this.addNewBtn = new Button("New");
		this.countSync = new Button("CountCheck");
	}

	@Override
	protected void init(VaadinRequest request)
	{

		HorizontalLayout actions = new HorizontalLayout(filter, countSync, addNewBtn);
		VerticalLayout subLayout = new VerticalLayout(actions, grid);
		VerticalLayout subLayout_ = new VerticalLayout(changer, editor);
		HorizontalLayout mainLayout = new HorizontalLayout(subLayout, subLayout_);
		setContent(mainLayout);

		grid.setHeight(600, Unit.PIXELS);
		grid.setWidth(700, Unit.PIXELS);
		grid.setColumns("sellId", "userId", "sellTime");

		filter.setPlaceholder("Filter by userId");
		filter.setValueChangeMode(ValueChangeMode.LAZY);
		filter.addValueChangeListener(e -> listUsers(e.getValue()));

		addNewBtn.addClickListener(e -> editor.setVisible(true));
		countSync.addClickListener(e ->
		{
			String uri = "";
			List<ServiceInstance> list = discoveryClient.getInstances("USER");
			if (list != null && list.size() > 0)
			{
				uri = list.get(0).getUri().toString();
				String count1 = restTemplate.getForObject(uri+"/count", String.class);
				String count2 = userInfoDao.count().toString();
				if (count1.equals(count2))
					Notification.show("Check Pass!");
				else
					Notification.show("Something wrong!");
			}
		});

		editor.setChangeHandler(() ->
		{
			editor.setVisible(false);
			listUsers(filter.getValue());
		});

		changer.setChangeHandler(() ->
		{
			listUsers(filter.getValue());
		});

		// Initialize listing
		listUsers(null);
	}

	// tag::listCustomers[]
	void listUsers(String filterText)
	{
		if (StringUtils.isEmpty(filterText))
		{
			grid.setItems(sellInfoMapper.findAll());
		} else
		{
			grid.setItems(sellInfoMapper.findByUserId((filterText)));
		}
	}

}