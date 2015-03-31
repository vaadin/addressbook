package com.vaadin.tutorial.addressbook;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.event.ShortcutAction;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.tutorial.addressbook.backend.Contact;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * The part of the UI modifying a Contact instance.
 *
 * Vaadin contains an efficient data binding mechanism. BeanFieldGroup can bind
 * DTO fields to similarly named UI fields by naming convention or using using
 * PropertyId annotation. Naturally you could do data binding manually as well.
 * You could also auto-generate the fields at runtime using bean introspection,
 * but typically this is not as flexible as defining then in your UI code.
 */
public class ContactForm extends VerticalLayout implements Button.ClickListener {

    private Button save = new Button("Save", this);
    private Button cancel = new Button("Cancel", this);

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

        final HorizontalLayout actions = new HorizontalLayout(save, cancel);
        actions.setSpacing(true);

        addComponent(new FormLayout(actions, firstName, lastName, phone, email,
                birthDate));
        setMargin(new MarginInfo(false, true, false, true));

        /* The core theme has lots of handy styles for components, here we
         * make the save button stand out from other buttons and give it
         * a keyboard shortcut to give a better UX
         */
        save.setStyleName(ValoTheme.BUTTON_PRIMARY);
        save.setClickShortcut(ShortcutAction.KeyCode.ENTER);
    }

    /**
     * Instead of using inner class for event listeners like we did in
     * AddressBookUI, a common practice is to implement listeners in your
     * compositions or in separate controller classes. Clicking save or cancel
     * in this application just passes the control back to the main view.
     *
     * @param event
     */
    @Override
    public void buttonClick(Button.ClickEvent event) {
        if (event.getButton() == save) {
            mainUI.save(contact);
        } else if (event.getButton() == cancel) {
            mainUI.deselect();
        }
    }

    public void edit(Contact contact) {
        this.contact = contact;
        // Bind the properties of the contact POJO to fiels in this form
        BeanFieldGroup.bindFieldsUnbuffered(contact, this);
        setVisible(true);
        firstName.focus();
    }

}
