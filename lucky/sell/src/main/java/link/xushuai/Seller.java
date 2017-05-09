package link.xushuai;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
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

import link.xushuai.domain.SellInfo;
import link.xushuai.domain.SellInfoDao;
import link.xushuai.domain.UserInfo;
import link.xushuai.domain.UserInfoDao;


@SpringComponent
@UIScope
public class Seller extends VerticalLayout
{

	private UserInfo userInfo;

	TextField userId = new TextField("用户名");
	TextField userPassword = new TextField("密码");
	String lastValue = "";
	String lastValue1 = "";

	/* Action buttons */
	Button buy = new Button("Buy");
	Button cancel = new Button("Cancel");

	CssLayout actions = new CssLayout(buy, cancel);

	Binder<SellInfo> binder = new Binder<>(SellInfo.class);

	private SellInfoDao sellInfoDao;

	private UserInfoDao userInfoDao;

	@Autowired
	public Seller(UserInfoDao userInfoDao, SellInfoDao sellInfoDao, KafkaTemplate<String, String> kafkaTemplate)
	{
		this.sellInfoDao = sellInfoDao;
		this.userInfoDao = userInfoDao;
		addComponents(userId, userPassword, actions);

		binder.bindInstanceFields(this);

		setSpacing(true);
		actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		buy.setStyleName(ValoTheme.BUTTON_PRIMARY);
		buy.setClickShortcut(ShortcutAction.KeyCode.ENTER);

		buy.addClickListener(e ->
		{
			if (emptyItem())
				Notification.show("No Empty Item Please!");
			else if (userInfoDao.findByUserId(userId.getValue()) == null)
				Notification.show("This userId does not exist!");
			else
			{
				userInfo = userInfoDao.findByUserId(userId.getValue());
				if (userInfo.getUserPassword().equals(userPassword.getValue()) == false)
					Notification.show("Wrong Password!");
				else
				{
					SellInfo sellInfo = new SellInfo(userInfo.getUserId());
					sellInfoDao.save(sellInfo);
					Integer sum = new Integer(userInfo.getTotal()) + 1;
					userInfoDao.update(userInfo.getUserId(), "Total", sum.toString());
					userId.setValue("");
					userPassword.setValue("");

				}
			}

		});
		cancel.addClickListener(e ->

		setVisible(false));

		setAlphaRestriction(userId);

		setVisible(false);
	}

	public interface ChangeHandler
	{
		void onChange();
	}

	public void setChangeHandler(ChangeHandler h)
	{
		buy.addClickListener(e -> h.onChange());
	}

	public boolean emptyItem()
	{// 判断空输入
		if (userId.getValue().equals("") || userPassword.getValue().equals(""))

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
