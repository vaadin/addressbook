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
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;

import javax.servlet.annotation.WebServlet;
import java.util.Arrays;

/** The user interface class.
 * This is the user interface that is displayed in the browser.
 * New instance of this class is created for every user that browses
 * to the application URL.
 *
 */
@Title("Addressbook")
@Theme("valo")
public class AddressbookUI extends UI {


	// ContactService mimics a real world DAO, that you'd typically implement as
	// EJB or Spring Data based service.
	private ContactService service = ContactService.createDemoService();

	/** Plenty of built-in components.
	 * Import the default Vaadin components from in com.vaadin.ui package.
	 */
	private TextField filter = new TextField();
	private Button newContact = new Button("New contact");

	private Table contactList = new Table();

	private ContactForm contactForm = new ContactForm(this);

	/** Initialize the visible content.
	 * The UI.init is the "main method" for you Vaadin application.
     * It is the entry point method executed to initialize the visible user interface.
	 * Use built-in Vaadin components, build your own, or import add-ons form vaadin.com/directory.
	 *
	 */
	@Override
	protected void init(VaadinRequest request) {
		// Configure components and wire logic to them
		newContact.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				editContact(new Contact());
			}
		});

		filter.setInputPrompt("Filter contacts...");
		filter.addTextChangeListener(new FieldEvents.TextChangeListener() {
			@Override
			public void textChange(FieldEvents.TextChangeEvent event) {
				listContacts(event.getText());
			}
		});

		contactList.setSelectable(true);
		contactList.addValueChangeListener(new Property.ValueChangeListener() {
			@Override
			public void valueChange(Property.ValueChangeEvent event) {
				editContact((Contact) event.getProperty().getValue());
			}
		});

		// Build main layout
		HorizontalLayout actions = new HorizontalLayout(filter, newContact);
		actions.setWidth("100%");
		filter.setWidth("100%");
		actions.setExpandRatio(filter, 1);

		VerticalLayout left = new VerticalLayout(actions, contactList);
		left.setSizeFull();
		contactList.setSizeFull();
		left.setExpandRatio(contactList, 1);

		setContent(new HorizontalSplitPanel(left, contactForm));

		// List initial content from the "backend"
		listContacts();
	}

	private void listContacts() {
		listContacts(filter.getValue());
	}

	private void listContacts(String text) {
		contactList.setContainerDataSource(new BeanItemContainer<>(
				Contact.class, service.findAll(text)), Arrays.asList(
				"firstName", "lastName", "email"));
		contactList.setColumnHeaders("First name", "Last name", "email");
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
		contactList.setValue(null);
	}

	/** Define the application URI.
	 *
	 *  Vaadin applications are basically just Serlvlets, and you can specify here
	 *  additional parameters like the URI and UI class name and turn on production mode.
	 *
	 */
	@WebServlet(urlPatterns = "/*")
	@VaadinServletConfiguration(ui = AddressbookUI.class, productionMode = false)
	public static class MyUIServlet extends VaadinServlet {
	}


}
