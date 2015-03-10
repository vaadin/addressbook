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
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
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
     * Servlet 3.0 style. Naturally you can use the plain old web.xml file as well.
     */
    @WebServlet(urlPatterns = "/*")
    @VaadinServletConfiguration(ui = AddressbookUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }

    private ContactService service = ContactService.createDemoService();

    private TextField filter = new TextField();
    private Button newContact = new Button("New contact",
            new Button.ClickListener() {

                @Override
                public void buttonClick(ClickEvent event) {
                    contactForm.edit(new Contact());
                }
            });

    private Table contactList = new Table();

    private ContactForm contactForm = new ContactForm(this);

    @Override
    protected void init(VaadinRequest request) {
        HorizontalLayout actions = new HorizontalLayout(filter, newContact);
        actions.setWidth("100%");
        filter.setWidth("100%");
        filter.setInputPrompt("Filter contacts...");
        filter.addTextChangeListener(new FieldEvents.TextChangeListener() {

            @Override
            public void textChange(FieldEvents.TextChangeEvent event) {
                listContacts(event.getText());
            }

        });
        actions.setExpandRatio(filter, 1);

        VerticalLayout left = new VerticalLayout(actions, contactList);
        left.setSizeFull();
        contactList.setSizeFull();
        left.setExpandRatio(contactList, 1);

        setContent(new HorizontalSplitPanel(left, contactForm));

        contactForm.setVisible(false);
        listContacts();

        contactList.setSelectable(true);
        contactList.addValueChangeListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                Contact contact = (Contact) event.getProperty().getValue();
                if (contact != null) {
                    contactForm.edit(contact);
                } else {
                    contactForm.setVisible(false);
                }
            }
        });

    }

    private void listContacts() {
        listContacts(filter.getValue());
    }

    private void listContacts(String text) {
        contactList.setContainerDataSource(
                new BeanItemContainer<>(Contact.class, service.findAll(text)),
                Arrays.asList("firstName", "lastName", "email"));
        contactList.setColumnHeaders("First name", "Last name", "email");
        contactForm.setVisible(false);
    }

    void save(Contact contact) {
        service.save(contact);
        listContacts();
    }

    void deselect() {
        contactList.setValue(null);
    }

}
