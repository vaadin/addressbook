package com.vaadin.demo.simpleaddressbook;

import java.util.Random;

import com.vaadin.data.Container.Filter;
import com.vaadin.data.*;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;

/**
 * UI class is the starting point for your app. You may deploy it with VaadinServlet or
 * VaadinPortlet by giving your UI class name a parameter. When you browse to your app a
 * web page showing your UI is automatically generated. Or you may choose to embed your UI
 * to an existing web page.
 */
public class AddressbookUI extends UI {
    private static final String FIRST_NAME = "First Name";
    private static final String LAST_NAME = "Last Name";
    private static final String COMPANY = "Company";
    private static final String[] fieldNames = new String[] {FIRST_NAME, LAST_NAME,
            COMPANY, "Mobile Phone", "Work Phone", "Home Phone", "Work Email",
            "Home Email", "Street", "City", "Zip", "State", "Country"};

    /**
     * Any component can be bound to an external data source. This example uses just a
     * dummy in-memory list, but there are many more practical implementations.
     */
    private final DummyDataContainer dummyDataSource = new DummyDataContainer();

    private final FormLayout editorLayout = new EditorLayout();
    private final FieldGroup editorFields = new FieldGroup();

    /* User interface components are stored in session. */
    private final ContactList contactList = new ContactList(dummyDataSource);
    private final TextField searchField = new SearchField();
    private final Button addNewContactButton = new AddNewContactButton();
    private final Button removeContactButton = new RemoveContactButton();

    /**
     * After UI class is created, init() is executed. You should build and wire up your
     * user interface here.
     */
    @Override
    protected void init(VaadinRequest request) {
        /*
         * In this example layouts are programmed in Java. You may choose use a visual
         * editor, CSS or HTML templates for layout instead.
         */
        HorizontalSplitPanel splitPanel = new HorizontalSplitPanel();
        VerticalLayout leftLayout = new VerticalLayout();
        HorizontalLayout bottomLeftLayout = new HorizontalLayout();

        /* Root of the user interface component tree is set */
        setContent(splitPanel);

        /* Build the component tree */
        splitPanel.addComponent(leftLayout);
        splitPanel.addComponent(editorLayout);
        leftLayout.addComponent(contactList);
        leftLayout.addComponent(bottomLeftLayout);
        bottomLeftLayout.addComponent(searchField);
        bottomLeftLayout.addComponent(addNewContactButton);

        /* Set the contents in the left of the split panel to use all the space */
        leftLayout.setSizeFull();

        /*
         * On the left side, expand the size of the contactList so that it uses all the
         * space left after from bottomLeftLayout
         */
        leftLayout.setExpandRatio(contactList, 1);
        contactList.setSizeFull();

        /*
         * In the bottomLeftLayout, searchField takes all the width there is after adding
         * addNewContactButton. The height of the layout is defined by the tallest
         * component
         */
        bottomLeftLayout.setWidth("100%");
        searchField.setWidth("100%");
        bottomLeftLayout.setExpandRatio(searchField, 1);

        /* Put a little margin around the fields in the right side editor */
        editorLayout.setMargin(true);
    }

    private class EditorLayout extends FormLayout {
        public EditorLayout() {
            for (String fieldName : fieldNames) {
                TextField field = new TextField(fieldName);
                addComponent(field);
                field.setWidth("100%");

                /*
                 * We use a FieldGroup to connect multiple components to a data source at
                 * once.
                 */
                editorFields.bind(field, fieldName);
            }
            addComponent(removeContactButton);

            /*
             * Data can be buffered in the user interface. When doing so, commit() writes
             * the changes to the data source. Here we choose to write the changes
             * automatically without calling commit()
             */
            editorFields.setBuffered(false);
        }
    }

    /*
     * Generate some in-memory example data to play with. In a real application we could
     * be using SQLContainer, JPAContainer or some other to persist the data.
     */
    private class DummyDataContainer extends IndexedContainer {
        public DummyDataContainer() {
            for (String fieldName : fieldNames) {
                addContainerProperty(fieldName, String.class, "");
            }

            /* Create dummy data by randomly combining first and last names */
            String[] firstNames = {"Peter", "Alice", "Joshua", "Mike", "Olivia", "Nina",
                    "Alex", "Rita", "Dan", "Umberto", "Henrik", "Rene", "Lisa", "Marge"};
            String[] lastNames = {"Smith", "Gordon", "Simpson", "Brown", "Clavel",
                    "Simons", "Verne", "Scott", "Allison", "Gates", "Rowling", "Barks",
                    "Ross", "Schneider", "Tate"};
            Random random = new Random();

            for (int i = 0; i < 1000; i++) {
                Object id = addItem();
                getContainerProperty(id, FIRST_NAME).setValue(
                        firstNames[random.nextInt(firstNames.length)]);
                getContainerProperty(id, LAST_NAME).setValue(
                        lastNames[random.nextInt(firstNames.length)]);
            }
        }
    }

    private class ContactList extends Table {
        public ContactList(DummyDataContainer dataSource) {
            setContainerDataSource(dataSource);
            setVisibleColumns(new String[] {FIRST_NAME, LAST_NAME, COMPANY});
            setSelectable(true);
            setImmediate(true);

            addValueChangeListener(new Property.ValueChangeListener() {
                @Override
                public void valueChange(Property.ValueChangeEvent event) {
                    Object contactId = getValue();

                    /*
                     * When a contact is selected from the list, we want to show that in
                     * our editor on the right. This is nicely done by the FieldGroup that
                     * binds all the fields to the correcponding Properties in our contact
                     * at once.
                     */
                    editorFields.setItemDataSource(contactId == null
                            ? null : getItem(contactId));
                    removeContactButton.setVisible(contactId != null);
                }
            });
        }
    }

    private class SearchField extends TextField {
        public SearchField() {
            /*
             * We want to show a subtle prompt in the search field. We could also set a
             * caption that would be shown above the field or description to be shown in a
             * tooltip.
             */
            setInputPrompt("Search contacts");

            /*
             * Granularity for sending events over the wire can be controlled. By default
             * simple changes like writing a text in TextField are sent to server with the
             * next Ajax call. You can set your component to be immediate to send the
             * changes to server immediately after focus leaves the field. Here we choose
             * to send the text over the wire as soon as user stops writing for a moment.
             */
            setTextChangeEventMode(TextChangeEventMode.LAZY);

            /*
             * When the event happens, we handle it in the anonymous inner class. You may
             * choose to use separate controllers (in MVC) or presenters (in MVP) instead.
             * In the end, the preferred application architecture is up to you.
             */
            addTextChangeListener(new TextChangeListener() {
                @Override
                public void textChange(final TextChangeEvent event) {
                    dummyDataSource.removeAllContainerFilters();

                    /*
                     * We may add filters directly to the data source to show only
                     * contacts that match to our search string.
                     */
                    dummyDataSource.addContainerFilter(new Filter() {
                        @Override
                        public boolean passesFilter(Object itemId, Item item) {
                            String needle = event.getText().toLowerCase();
                            String haystack = ("" + item.getItemProperty(FIRST_NAME).getValue() + item.getItemProperty(
                                    LAST_NAME).getValue() + item.getItemProperty(COMPANY).getValue()).toLowerCase();
                            return haystack.contains(needle);
                        }

                        @Override
                        public boolean appliesToProperty(Object id) {
                            return true;
                        }
                    });
                }
            });
        }
    }

    private class AddNewContactButton extends Button {
        public AddNewContactButton() {
            super("New");
            addClickListener(new ClickListener() {
                @Override
                public void buttonClick(ClickEvent event) {

                    /*
                     * Rows in the Container data model are called Item. Here we add a new
                     * row in the beginning of the list.
                     */
                    Object contactId = dummyDataSource.addItemAt(0);

                    /*
                     * Each Item has a set of Properties that hold values. Here we set a
                     * couple of those.
                     */
                    contactList.getContainerProperty(contactId, FIRST_NAME).setValue(
                            "New");
                    contactList.getContainerProperty(contactId, LAST_NAME).setValue(
                            "Contact");

                    /* Lets choose the newly created contact to edit it. */
                    contactList.select(contactId);
                }
            });
        }
    }

    private class RemoveContactButton extends Button {
        public RemoveContactButton() {
            super("Remove this contact");
            addClickListener(new ClickListener() {
                @Override
                public void buttonClick(ClickEvent event) {
                    Object contactId = contactList.getValue();
                    contactList.removeItem(contactId);
                }
            });
        }
    }
}
