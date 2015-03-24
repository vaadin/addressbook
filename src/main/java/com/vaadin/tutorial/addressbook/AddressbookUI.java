package com.vaadin.tutorial.addressbook;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.FieldEvents;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.tutorial.addressbook.backend.Contact;
import com.vaadin.tutorial.addressbook.backend.ContactService;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import java.util.Arrays;
import javax.servlet.annotation.WebServlet;

/* 
 * UI class is the starting point for your app. You may deploy it with VaadinServlet
 * or VaadinPortlet by giving your UI class name a parameter. When you browse to your
 * app a web page showing your UI is automatically generated. Or you may choose to 
 * embed your UI to an existing web page. 
 */
@Title("Addressbook")
@Theme("valo")
public class AddressbookUI extends UI {

	/**
	 * Vaadin applications are basically just Serlvlets, so lets define one with
	 * Servlet 3.0 style. Naturally you can use the plain old web.xml file as
	 * well.
	 */
	@WebServlet(urlPatterns = "/*")
	@VaadinServletConfiguration(ui = AddressbookUI.class, productionMode = false)
	public static class MyUIServlet extends VaadinServlet {
	}

	/*
	 * ContactService mimics a real world DAO, that you'd typically implement as
	 * EJB or Spring Data based service.
	 */
	private ContactService service = ContactService.createDemoService();

	private ContactForm contactForm = new ContactForm(this);

	private MainLayout mainLayout;

	/**
	 * The UI.init is the "public static void main(String... args)" for your
	 * Vaadin application. It is the entry point method executed for each user
	 * who enters your application.
	 *
	 * @param request
	 *            the request object contains some low level web request data
	 *            that you can typically ignore.
	 */
	@Override
	protected void init(VaadinRequest request) {
		mainLayout = new MainLayout();
		setContent(mainLayout);

		// Add the form but hide it from the user until a row is selected in the
		// contact list
		mainLayout.setSecondComponent(contactForm);
		contactForm.setVisible(false);

		// Configure components and wire logic to them
		mainLayout.newContact.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				editContact(new Contact());
			}
		});

		mainLayout.filter
		.addTextChangeListener(new FieldEvents.TextChangeListener() {
			@Override
			public void textChange(FieldEvents.TextChangeEvent event) {
				listContacts(event.getText());
			}
		});

		mainLayout.contactList
		.addValueChangeListener(new Property.ValueChangeListener() {
			@Override
			public void valueChange(Property.ValueChangeEvent event) {
				editContact((Contact) event.getProperty().getValue());
			}
		});

		// List initial content from the "backend"
		listContacts();
	}

	private void listContacts() {
		listContacts(mainLayout.filter.getValue());
	}

	private void listContacts(String text) {
		mainLayout.contactList.setContainerDataSource(new BeanItemContainer<>(
				Contact.class, service.findAll(text)), Arrays.asList(
				"firstName", "lastName", "email"));
		mainLayout.contactList.setColumnHeaders("First name", "Last name",
				"email");
		contactForm.setVisible(false);
	}

	private void editContact(Contact contact) {
		if (contact != null) {
			contactForm.edit(contact);
		} else {
			contactForm.setVisible(false);
		}
	}

	/*
	 * These methods are public and are called by ContactForm when user wants to
	 * persist or reset changes to the edited contact.
	 */
	public void save(Contact contact) {
		service.save(contact);
		listContacts();
	}

	public void deselect() {
		mainLayout.contactList.setValue(null);
	}

}
