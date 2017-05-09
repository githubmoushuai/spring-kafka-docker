package link.xushuai;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
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
import link.xushuai.domain.UserInfo;
import link.xushuai.domain.UserInfoDao;

@SpringComponent
@UIScope
public class passwordChanger extends VerticalLayout
{

	private UserInfo userInfo;

	TextField userId = new TextField("用户名");
	TextField userPasswordOld = new TextField("旧密码");
	TextField userPasswordNew = new TextField("新密码");
	String lastValue = "";
	String lastValue1 = "";


	Button confirm = new Button("Confirm");
	Button cancel = new Button("Cancel");

	CssLayout actions = new CssLayout(confirm, cancel);

	private UserInfoDao userInfoDao;

	@Autowired
	public passwordChanger(UserInfoDao userInfoDao, KafkaTemplate<String, String> kafkaTemplate)
	{
		
		this.userInfoDao = userInfoDao;
		addComponents(userId, userPasswordOld,userPasswordNew, actions);
		setSpacing(true);
		actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		confirm.setStyleName(ValoTheme.BUTTON_PRIMARY);
		confirm.setClickShortcut(ShortcutAction.KeyCode.ENTER);

		confirm.addClickListener(e ->
		{
			if (emptyItem())
				Notification.show("No Empty Item Please!");
			else if (userInfoDao.findByUserId(userId.getValue()) == null)
				Notification.show("This userId does not exist!");
			else
			{
				userInfo = userInfoDao.findByUserId(userId.getValue());
				if (userInfo.getUserPassword().equals(userPasswordOld.getValue()) == false)
					Notification.show("Wrong Old Password!");
				else
				{
					
					userInfoDao.update(userInfo.getUserId(), "UserPassword", userPasswordNew.getValue());
					Notification.show("Your New Passwd is "+userPasswordNew.getValue());
					userId.setValue("");
					userPasswordOld.setValue("");
					userPasswordNew.setValue("");
					

				}
			}

		});
		cancel.addClickListener(e ->{
			 userId.setValue("");
			 userPasswordNew.setValue("");
			 userPasswordOld.setValue("");}
			 
		);

		setAlphaRestriction(userId);

		setVisible(true);
	}

	public interface ChangeHandler
	{
		void onChange();
	}

	public void setChangeHandler(ChangeHandler h)
	{
		confirm.addClickListener(e -> h.onChange());
	}

	public boolean emptyItem()
	{// 判断空输入
		if (userId.getValue().equals("") || userPasswordOld.getValue().equals("")||userPasswordNew.getValue().equals(""))

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
