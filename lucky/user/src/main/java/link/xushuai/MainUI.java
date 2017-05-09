package link.xushuai;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import com.vaadin.annotations.Theme;

import com.vaadin.server.VaadinRequest;

import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import link.xushuai.domain.User;
import link.xushuai.domain.UserRepo;

@SpringUI(path = "/user")
@Theme("valo")

public class MainUI extends UI
{
	private final Label label;
	private final UserRepo repo;

	private final UserEditor editor;
    
	final Grid<User> grid;

	final TextField filter;

	private final Button addNewBtn;

	@Autowired
	public MainUI(UserRepo repo, UserEditor editor)
	{
		this.label=new Label("用户管理");
		this.repo = repo;
		this.editor = editor;
		this.grid = new Grid<>(User.class);
		this.filter = new TextField();
		this.addNewBtn = new Button("New User");
	}

	@Override
	protected void init(VaadinRequest request)
	{
		
		HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);
		VerticalLayout subLayout = new VerticalLayout( label,actions, grid);
		HorizontalLayout mainLayout = new HorizontalLayout(subLayout, editor);
		setContent(mainLayout);
		
		
		
		grid.setHeight(600, Unit.PIXELS);
		grid.setWidth(700, Unit.PIXELS);
		grid.setColumns("userId", "userPassword", "name", "account", "regTime", "total");

		filter.setPlaceholder("Filter by name");
		filter.setValueChangeMode(ValueChangeMode.LAZY);
		filter.addValueChangeListener(e -> listUsers(e.getValue()));

		grid.asSingleSelect().addValueChangeListener(e ->
		{
			editor.editUser(e.getValue());
		});

		addNewBtn.addClickListener(e ->
		{
			editor.setNew(true);
			editor.editUser(new User("", "", "", ""));
		});

		editor.setChangeHandler(() ->
		{
			editor.setVisible(false);
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
			grid.setItems(repo.findAll());
		} else
		{
			grid.setItems(repo.findByName((filterText)));
		}
	}
	// end::listCustomers[]

}
