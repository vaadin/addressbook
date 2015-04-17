package com.vaadin.tutorial.addressbook;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.event.ShortcutAction;
import com.vaadin.tutorial.addressbook.backend.Contact;
import com.vaadin.ui.*;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;

/*
 * Create custom ui components.
 *
 * Create your own Vaadin components by inheritance and composition.
 * This is a form component inherited from FormLayout. Use BeanFieldGroup
 * to bind data fields from DTO to UI fields. Similarly named field by
 * naming convention or customized with @PropertyId annotation.
 */
public class ContactForm extends FormLayout {

	private Button save = new Button("Save", this::save);
	private Button cancel = new Button("Cancel", this::cancel);

	private TextField firstName = new TextField("First name");
	private TextField lastName = new TextField("Last name");
	private TextField phone = new TextField("Phone");
	private TextField email = new TextField("Email");
	private DateField birthDate = new DateField("Birth date");

	private final AddressbookUI mainUI;
	private Contact contact;

	// Easily bind forms to beans and manage validation and buffering
	BeanFieldGroup<Contact> formFieldBindings;

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

		addComponents(firstName, lastName, phone, email,
				birthDate, actions);
		setMargin(true);

	}

	/*
	 * Instead of using inline lambdas for event listeners like in
	 * AddressbookUI, you can implement listener methods in your
	 * compositions or in separate controller classes and receive
	 * to various Vaadin component events, like button clicks.
	 */
	public void save(Button.ClickEvent event) {
		try {
			// Place to call business logic.
			formFieldBindings.commit();
			mainUI.getService().save(contact);
			mainUI.refreshContacts();
			Notification.show("Saved: " + contact.getFirstName() + " " + contact.getLastName(),
				Type.TRAY_NOTIFICATION);
		} catch (FieldGroup.CommitException e) {
			e.printStackTrace();
		}
	}

	public void cancel(Button.ClickEvent event) {
		// Place to call business logic.
		Notification.show("Cancelled", Type.TRAY_NOTIFICATION);
		mainUI.deselect();
	}

	public void edit(Contact contact) {
		this.contact = contact;
		// Bind the properties of the contact POJO to fiels in this form
		formFieldBindings= BeanFieldGroup.bindFieldsBuffered(contact, this);
		setVisible(true);
		firstName.focus();
	}

}
