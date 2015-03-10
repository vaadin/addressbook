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
 */
public class ContactForm extends VerticalLayout implements Button.ClickListener {

    private Button save = new Button("Save", this);
    private Button cancel = new Button("Cancel", this);
    private final AddressbookUI mainUI;
    private Contact contact;

    /* Vaadin contains an efficient data binding mechanism. DTO fields are bound
     to similarly named UI fields by naming convention, using @PropertyId annotation
     or manually. You can also autogenerate the fields, but typically this is not as 
     flexible as defining then in your UI code. */
    private TextField firstName = new TextField("First name");
    private TextField lastName = new TextField("Last name");
    private TextField phone = new TextField("Phone");
    private TextField email = new TextField("Email");
    private DateField birthDate = new DateField("Birth date");

    public ContactForm(AddressbookUI mainUI) {
        this.mainUI = mainUI;
        final HorizontalLayout horizontalLayout = new HorizontalLayout(save,
                cancel);
        horizontalLayout.setSpacing(true);
        
        addComponent(new FormLayout(horizontalLayout, firstName, lastName, phone, email, birthDate));
        setMargin(new MarginInfo(false,true,false,true));
        
        firstName.setNullRepresentation("");
        lastName.setNullRepresentation("");
        phone.setNullRepresentation("");
        email.setNullRepresentation("");
        
        save.setStyleName(ValoTheme.BUTTON_PRIMARY);
        save.setClickShortcut(ShortcutAction.KeyCode.ENTER);
    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
        if(event.getButton() == save) {
            mainUI.save(contact);
        } else {
            mainUI.deselect();
        }
    }

    public void edit(Contact contact) {
        this.contact = contact;
        BeanFieldGroup.bindFieldsUnbuffered(contact, this);
        setVisible(true);
        firstName.focus();
    }

}
