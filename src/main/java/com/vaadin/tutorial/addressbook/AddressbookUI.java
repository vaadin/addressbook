package com.vaadin.tutorial.addressbook;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.AbstractTextField.TextChangeEventMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;


/* 
 * UI class is the starting point for your app. You may deploy it with VaadinServlet
 * or VaadinPortlet by giving your UI class name a parameter. When you browse to your
 * app a web page showing your UI is automatically generated. Or you may choose to 
 * embed your UI to an existing web page. 
 */
@Title("Addressbook")
@Theme("valo")
public class AddressbookUI extends UI {

	/* User interface components are stored in session. */
	private final Table contactTable = new Table();
	private final TextField searchField = new TextField();
	private final Button addNewContactButton = new Button("New contact...");
	private final Button removeContactButton = new Button("Remove this contact");
	private final FormLayout editorLayout = new FormLayout();
	private final FieldGroup editorFields = new FieldGroup();

	/* We use an in-memory datasource of POJOs, but this could be even
	 * an injected EJB, JDBC SQL datasource, or a JAX-RS datasource.
	 */
        private BeanContainer contactContainer;
        

	/*
	 * After UI class is created, init() is executed. You should build and wire
	 * up your user interface here.
	 */
        @Override
	protected void init(VaadinRequest request) {
		initLayout();
		initContactList();
		initEditor();
		initSearch();
		initAddRemoveButtons();
	}

	/*
	 * In this example layouts are programmed in Java. You may choose use a
	 * visual editor, CSS or HTML templates for layout instead.
	 */
	private void initLayout() {

		/* Root of the user interface component tree is set */
		HorizontalSplitPanel splitPanel = new HorizontalSplitPanel();
		setContent(splitPanel);

		/* Build the component tree */
		VerticalLayout leftLayout = new VerticalLayout();
		splitPanel.addComponent(leftLayout);
		splitPanel.addComponent(editorLayout);
		HorizontalLayout topLeftLayout = new HorizontalLayout();
		leftLayout.addComponent(topLeftLayout);
		topLeftLayout.addComponent(searchField);
		topLeftLayout.addComponent(addNewContactButton);
		leftLayout.addComponent(contactTable);

		/* Set the contents in the left of the split panel to use all the space */
		leftLayout.setSizeFull();

		/*
		 * On the left side, expand the size of the contactList so that it uses
		 * all the space left after from bottomLeftLayout
		 */
		leftLayout.setExpandRatio(contactTable, 1);
		contactTable.setSizeFull();

		/*
		 * In the bottomLeftLayout, searchField takes all the width there is
		 * after adding addNewContactButton. The height of the layout is defined
		 * by the tallest component.
		 */
		topLeftLayout.setWidth("100%");
                topLeftLayout.setMargin(true);
                topLeftLayout.setSpacing(true);
		searchField.setWidth("100%");                
		topLeftLayout.setExpandRatio(searchField, 1);

		/* Put a little margin around the fields in the right side editor */
		editorLayout.setMargin(true);
		editorLayout.setVisible(false);
	}

	private void initEditor() {

                removeContactButton.setStyleName(ValoTheme.BUTTON_DANGER);
		editorLayout.addComponent(removeContactButton);

		/* User interface can be created dynamically to reflect underlying data. */
                String[] fieldIds= Addressbook.getEditableFields();
                String[] fieldCaptions = Addressbook.getEditableFieldCaptions();
		for (int i= 0; i <fieldIds.length ; i++) {
			final TextField field = new TextField(fieldCaptions[i]);
                        field.setNullRepresentation("");
                        field.setTextChangeEventMode(TextChangeEventMode.LAZY);
                        field.addTextChangeListener(new TextChangeListener() {
                            @Override
                            public void textChange(TextChangeEvent event) {
                                field.setValue(event.getText());
                            }
                        });
			editorLayout.addComponent(field);
			field.setWidth("100%");
 			editorFields.bind(field, fieldIds[i]);
		}

		/*
		 * Data can be buffered in the user interface. When doing so, commit()
		 * writes the changes to the data source. Here we choose to write the
		 * changes automatically without calling commit().
		 */
		editorFields.setBuffered(false);
	}

	private void initSearch() {

		/*
		 * We want to show a subtle prompt in the search field. We could also
		 * set a caption that would be shown above the field or description to
		 * be shown in a tooltip.
		 */
		searchField.setInputPrompt("Search contacts");

		/*
		 * Granularity for sending events over the wire can be controlled. By
		 * default simple changes like writing a text in TextField are sent to
		 * server with the next Ajax call. You can set your component to be
		 * immediate to send the changes to server immediately after focus
		 * leaves the field. Here we choose to send the text over the wire as
		 * soon as user stops writing for a moment.
		 */
		searchField.setTextChangeEventMode(TextChangeEventMode.LAZY);

		/*
		 * When the event happens, we handle it in the anonymous inner class.
		 * You may choose to use separate controllers (in MVC) or presenters (in
		 * MVP) instead. In the end, the preferred application architecture is
		 * up to you.
		 */
		searchField.addTextChangeListener(new TextChangeListener() {
                        @Override
			public void textChange(final TextChangeEvent event) {

				/* Reset the filter for the contactContainer. */
				contactContainer.removeAllContainerFilters();
				contactContainer.addContainerFilter(new ContactFilter(event
						.getText()));
			}
		});
	}

	/*
	 * A custom filter for searching names and companies in the
	 * contactContainer.
	 */
	private class ContactFilter implements Filter {
		private final String needle;

		public ContactFilter(String needle) {
			this.needle = needle.toLowerCase();
		}

                @Override
		public boolean passesFilter(Object itemId, Item item) {                        
                        Person person =  (Person) contactContainer.getItem(itemId).getBean();
			String haystack = ("" + person.getFirstName()+
                                " "+ person.getLastName() +
                                " "+ person.getCompany()).toLowerCase();
			return haystack.contains(needle);
		}

                @Override
		public boolean appliesToProperty(Object id) {
			return true;
		}
	}

	private void initAddRemoveButtons() {
		addNewContactButton.addClickListener(new ClickListener() {
                    private Person currentPerson;
                    
                        @Override
			public void buttonClick(ClickEvent event) {

                                // Create a new person and select that
                                Person person = new Person();
                                person.setId(Addressbook.generateNewId());
				person.setFirstName("New");
				person.setLastName("Contact");
                                contactContainer.addBean(person);
  				contactTable.select(person.getId());
                                contactTable.setCurrentPageFirstItemId(person.getId());
			}
		});

		removeContactButton.addClickListener(new ClickListener() {
                        @Override
			public void buttonClick(ClickEvent event) {
				contactContainer.removeItem(contactTable.getValue());
			}
		});
	}
        
	private void initContactList() {
		
                /* Bind the data to the list using simple BeanContainer.
                 * Containers are used for databinding and you have plenty of 
                 * different implementations to choose from. This is an
                 * easy one for reasonable amount of data but check the
                 * vaadin.com/directory for lazy loading options.            
                 */
                contactContainer = new BeanContainer(Person.class);
                contactContainer.setBeanIdProperty(Person.ID_PROPERTY);
                contactContainer.addAll(Addressbook.getAllContacts());                
                contactTable.setContainerDataSource(contactContainer);
                
                // Configure visible fields and their captions
		contactTable.setVisibleColumns((Object[])Addressbook.getListFields());
		contactTable.setColumnHeaders(Addressbook.getListFieldCaptions());
                
                // Make the list selectable
		contactTable.setSelectable(true);
		contactTable.setImmediate(true);
		contactTable.addValueChangeListener(new Property.ValueChangeListener() {
                        @Override
			public void valueChange(ValueChangeEvent event) {
				Object contactId = contactTable.getValue();

				/*
				 * When a contact is selected from the list, we want to show
				 * that in our editor on the right. This is nicely done by the
				 * FieldGroup that binds all the fields to the corresponding
				 * Properties in our contact at once.
				 */
				if (contactId != null)
					editorFields.setItemDataSource(contactTable
							.getItem(contactId));
				
				editorLayout.setVisible(contactId != null);
			}
		});
	}

}
