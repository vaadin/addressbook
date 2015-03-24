package com.vaadin.tutorial.addressbook;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.tutorial.addressbook.backend.Contact;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

/**
 * The part of the UI modifying a Contact instance.
 *
 * Vaadin contains an efficient data binding mechanism. BeanFieldGroup can bind
 * DTO fields to similarly named UI fields by naming convention or using using
 * PropertyId annotation. Naturally you could do data binding manually as well.
 * You could also auto-generate the fields at runtime using bean introspection,
 * but typically this is not as flexible as defining then in your UI code.
 */
public class ContactForm extends ContactFormLayout {

	private final AddressbookUI mainUI;
	private Contact contact;

	public ContactForm(AddressbookUI addressbookUID) {
		this.mainUI = addressbookUID;
		save.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				mainUI.save(contact);
			}
		});
		cancel.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				mainUI.deselect();
			}
		});
	}

	public void edit(Contact contact) {
		this.contact = contact;
		// Bind the properties of the contact POJO to fiels in this form
		BeanFieldGroup.bindFieldsUnbuffered(contact, this);
		setVisible(true);
		firstName.focus();
	}

}
