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

/*
 * One UI instance per user.
 * The UI class is the starting point for your app. When a user browse to your
 * application URL, new instance of this class is created for.
 *
 */
@Title("Addressbook")
@Theme("valo")
public class AddressbookUI extends UI {

	/** Define the the URI.
     *
	 *  Vaadin applications are basically just Serlvlets, and you can specify here
     *  additional parameters like the URI and UI class anme and turn on production mode.
     *
	 */
	@WebServlet(urlPatterns = "/*")
	@VaadinServletConfiguration(ui = AddressbookUI.class, productionMode = false)
	public static class MyUIServlet extends VaadinServlet {
	}

	/* ContactService mimics a real world DAO, that you'd typically implement as
     * EJB or Spring Data based service.
     */
	private ContactService service = ContactService.createDemoService();

	private TextField filter = new TextField();
	private Button newContact = new Button("New contact");

	private Table contactList = new Table();

	private ContactForm contactForm = new ContactForm(this);

	/** Initialize the user interface.
	 * The UI.init is the "main method" for you Vaadin application.
     * It is the entry point method executed for a newly created
     * instance to initialize the visible user interface.
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

}
