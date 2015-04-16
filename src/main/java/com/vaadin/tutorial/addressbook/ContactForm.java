package com.vaadin.tutorial.addressbook;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.event.ShortcutAction;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.tutorial.addressbook.backend.Contact;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.ui.Notification.Type;

/*
 * Reusable custom ui component.
 *
 * Create your own Vaadin components by inheritance and composition.
 * This is a form component inherited from VerticalLayout. Use
 * Use BeanFieldGroup to bind data fields from DTO to UI fields.
 * Similarly named field by naming convention or customized
 * with @PropertyId annotation.
 */
public class ContactForm extends VerticalLayout {

	private Button save = new Button("Save", this::saveClick);
	private Button cancel = new Button("Cancel", this::cancelClicked);

	private TextField firstName = new TextField("First name");
	private TextField lastName = new TextField("Last name");
	private TextField phone = new TextField("Phone");
	private TextField email = new TextField("Email");
	private DateField birthDate = new DateField("Birth date");

	private final AddressbookUI mainUI;
	private Contact contact;

	public ContactForm(AddressbookUI mainUI) {
		this.mainUI = mainUI;
		setVisible(false);

		/* Highlight primary actions.
		 * With Vaadin built-in styles you can highlight the primary save button
		 * and give it a keyboard shortcut for a better UX.
		 */
		save.setStyleName(ValoTheme.BUTTON_PRIMARY);
		save.setClickShortcut(ShortcutAction.KeyCode.ENTER);


		final HorizontalLayout actions = new HorizontalLayout(save, cancel);
		actions.setSpacing(true);

		addComponent(new FormLayout(actions, firstName, lastName, phone, email,
				birthDate));
		setMargin(new MarginInfo(false, true, false, true));

	}

	/*
	 * Instead of using inline lambdas for event listeners like in
	 * AddressbookUI, you can implement listener methods in your
	 * compositions or in separate controller classes and receive
	 * to various Vaadin component events, like button clicks.
	 */
	public void saveClick(Button.ClickEvent event) {
		// Place to call business logic.
		mainUI.save(contact);
		Notification.show("Saved: " + contact.getFirstName() + " " + contact.getLastName(),
				Type.TRAY_NOTIFICATION);
	}

	public void cancelClicked(Button.ClickEvent event) {
		// Place to call business logic.
		Notification.show("Cancelled", Type.TRAY_NOTIFICATION);
		mainUI.deselect();
	}

	public void edit(Contact contact) {
		this.contact = contact;
		// Bind the properties of the contact POJO to fiels in this form
		BeanFieldGroup.bindFieldsUnbuffered(contact, this);
		setVisible(true);
		firstName.focus();
	}

}
