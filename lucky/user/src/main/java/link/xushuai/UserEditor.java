package link.xushuai;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import com.vaadin.data.Binder;
import com.vaadin.event.ShortcutAction;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import link.xushuai.domain.User;
import link.xushuai.domain.UserDao;

@SpringComponent
@UIScope
public class UserEditor extends VerticalLayout
{

	private final UserDao userDao;
	private User user;
	private HashMap<String, String> changed = new HashMap<String, String>();
	private boolean isNew = false;

	public boolean isNew()
	{
		return isNew;
	}

	public void setNew(boolean isNew)
	{
		this.isNew = isNew;
	}

	TextField userId = new TextField("UserId");
	TextField userPassword = new TextField("UserPassword");
	TextField name = new TextField("Name");
	TextField account = new TextField("Account");
	TextField regTime = new TextField("RegTime");
	TextField total = new TextField("Total");

	String lastValue = "";
	String lastValue1 = "";

	/* Action buttons */
	Button save = new Button("Save");
	Button cancel = new Button("Cancel");
	Button delete = new Button("Delete");
	CssLayout actions = new CssLayout(save, cancel, delete);

	Binder<User> binder = new Binder<>(User.class);

	@Autowired
	public UserEditor(UserDao userDao)
	{
		this.userDao = userDao;
		addComponents(userId, userPassword, name, account, regTime, total, actions);

		binder.bindInstanceFields(this);

		setSpacing(true);
		actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		save.setStyleName(ValoTheme.BUTTON_PRIMARY);
		save.setClickShortcut(ShortcutAction.KeyCode.ENTER);

		save.addClickListener(e ->
		{
			if (emptyItem())
				Notification.show("No Empty Item Please!");
			else if (isNew)
			{
				if (userDao.save(user).equals("success"))
					;
				else
					Notification.show("This userId already exists!");
				userId.setEnabled(true);
				isNew = false;
			} else
			{
				Iterator iter = changed.entrySet().iterator();
				while (iter.hasNext())
				{
					Map.Entry entry = (Map.Entry) iter.next();
					String value=entry.getValue().toString();
					System.out.println(value);
					if(!value.equals("false"))	userDao.update(userId.getValue(), entry.getKey().toString(),value);
				}
				//userDao.update(userId.getValue(), "userPassword", userPassword.getValue());
			}
			initChanged();
		});
		delete.addClickListener(e ->
		{
			userDao.delete(user);
			initEditor();

		});
		cancel.addClickListener(e ->
		{
			setVisible(false);
			initEditor();

		});

		regTime.setEnabled(false);
		total.setEnabled(false);
		setNumRestriction(account);
		setAlphaRestriction(userId);
		setValueChangedListener(userPassword);
		setValueChangedListener(account);
		setValueChangedListener(name);
		
		setVisible(false);
	}

	public interface ChangeHandler
	{
		void onChange();
	}

	public final void editUser(User c)
	{
		
		if (c == null)
		{
			setVisible(false);
			return;
		}
		final boolean persisted = !(c.getUserId().equals(""));
		if (persisted)
		{
			userId.setEnabled(false);
			user = userDao.findByUserId(c.getUserId());
		} else
		{
			user = c;
		}

		// cancel.setVisible(persisted);
		binder.setBean(user);
		setVisible(true);
		save.focus();
		name.selectAll();
		initChanged();
	}

	public void setChangeHandler(ChangeHandler h)
	{
		save.addClickListener(e -> h.onChange());
		delete.addClickListener(e -> h.onChange());
	}

	public boolean emptyItem()
	{// 判断空输入
		if (userId.getValue().equals("") || userPassword.getValue().equals("") || name.getValue().equals("")
				|| account.getValue().equals(""))
			return true;
		else
			return false;
	}

	public void setAlphaRestriction(TextField textField)
	{// 限制英文输入
		textField.setValueChangeMode(ValueChangeMode.EAGER);
		textField.addValueChangeListener(e ->
		{
			String text = e.getValue();
			if (text.equals(""))
				;
			else
			{
				if (text.matches("[a-zA-Z]+"))
					lastValue1 = text;
				else
					textField.setValue(lastValue1);
			}
		});
	}

	public void setValueChangedListener(TextField textField)
	{
		String attr = textField.getCaption();
		changed.put(attr, "false");
		textField.setValueChangeMode(ValueChangeMode.EAGER);
		textField.addValueChangeListener(e ->
		{
			Method method;
			try
			{
				method = user.getClass().getMethod("get" + attr, new Class[0]);
				if (!textField.getValue().equals(method.invoke(user, new Object[0]).toString()))
				{
					changed.put(attr, textField.getValue());
				}
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e1)
			{
				e1.printStackTrace();
				System.out.println("BUGBUGBUG在获取方法执行的地方");
			}

		});
	}

	public void initChanged()
	{
		Iterator iter = changed.entrySet().iterator();
		while (iter.hasNext())
		{
			Map.Entry entry = (Map.Entry) iter.next();
			entry.setValue("false");
		}
	}

	public void initEditor()
	{
		initChanged();
		userId.setEnabled(true);

	}

	public void setNumRestriction(TextField textField)
	{// 限制数字输入
		textField.setValueChangeMode(ValueChangeMode.EAGER);
		textField.addValueChangeListener(e ->
		{
			String text = e.getValue();
			if (text.equals(""))
				;
			else
			{
				try
				{
					new Integer(text);
					lastValue = text;
				} catch (NumberFormatException e1)
				{
					textField.setValue(lastValue);
				}
			}
		});
	}
	
	

}
