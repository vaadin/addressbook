package com.vaadin.demo.simpleaddressbook;

import java.util.Collection;

import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.IndexedContainer;
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
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

// UI class is the starting point for your app. You may deploy it with VaadinServlet
// or VaadinPortlet by giving your UI class name a parameter. When you browse to your
// app a web page showing your UI is automatically generated. Or you may choose to 
// embed your UI to an existing web page. 
public class AddressbookUI extends UI {

	HorizontalSplitPanel splitPanel = new HorizontalSplitPanel();
	VerticalLayout leftLayout = new VerticalLayout();
	HorizontalLayout bottomLeftLayout = new HorizontalLayout();
	FormLayout editorLayout = new FormLayout();
	FieldGroup editorFields = new FieldGroup();

	// User interface components are stored in session.
	Table contactList = new Table();
	TextField searchField = new TextField();
	Button addNewContactButton = new Button("New");
	Button removeContactButton = new Button("Remove this contact");

	static final String FNAME = "First Name";
	static final String LNAME = "Last Name";
	static final String COMPANY = "Company";
	static final String[] fieldNames = new String[] { FNAME, LNAME, COMPANY,
			"Mobile Phone", "Work Phone", "Home Phone", "Work Email",
			"Home Email", "Street", "City", "Zip", "State", "Country" };

	// Any component can be bound to an external data source. This example uses
	// just a dummy in-memory list, but there are many more practical
	// implementations.
	IndexedContainer dummyDataSource = createDummyData();

	// After UI class is created, init() is executed. You should build and wire
	// up your user interface here.
	protected void init(VaadinRequest request) {
		initLayout();
		initContactList();
		initEditor();
		initSearch();
		initAddRemoveButtons();
	}

	private void initLayout() {
		// Root of the user interface component tree is set
		setContent(splitPanel);

		// In this example layouts are programmed in Java. You may choose use a
		// visual editor, CSS or HTML templates for layout instead.
		splitPanel.addComponent(leftLayout);
		leftLayout.setSizeFull();
		leftLayout.addComponent(contactList);
		contactList.setSizeFull();
		leftLayout.setExpandRatio(contactList, 1);
		bottomLeftLayout.setWidth("100%");
		leftLayout.addComponent(bottomLeftLayout);
		bottomLeftLayout.addComponent(searchField);
		searchField.setWidth("100%");
		bottomLeftLayout.setExpandRatio(searchField, 1);
		bottomLeftLayout.addComponent(addNewContactButton);

		splitPanel.addComponent(editorLayout);
		editorLayout.setMargin(true);
	}

	private void initEditor() {

		// User interface can be created dynamically to reflect underlying data.
		for (String fieldName : fieldNames) {
			TextField field = new TextField(fieldName);
			editorLayout.addComponent(field);
			field.setWidth("100%");

			// We use a FieldGroup to connect multiple components to a data
			// source at once.
			editorFields.bind(field, fieldName);
		}
		editorLayout.addComponent(removeContactButton);

		// Data can be buffered in the user interface. When doing so, commit()
		// writes the changes to the data source. Here we choose to write the
		// changes automatically without calling commit()
		editorFields.setBuffered(false);
	}

	private void initSearch() {

		// We want to show a subtle prompt in the search field. We could also
		// set a caption that would be shown above the field or description to
		// be shown in a tooltip.
		searchField.setInputPrompt("Search contacts");

		// Granularity for sending events over the wire can be controlled. By
		// default simple changes like writing a text in TextField are sent to
		// server with the next Ajax call. You can set your component to be
		// immediate to send the changes to server immediately after focus
		// leaves the field. Here we choose to send the text over the wire as
		// soon as user stops writing for a moment.
		searchField.setTextChangeEventMode(TextChangeEventMode.LAZY);

		// When the event happens, we handle it in the anonymous inner class.
		// You may choose to use separate controllers (in MVC) or presenters (in
		// MVP) instead. In the end, the preferred application architecture is
		// up to you.
		searchField.addTextChangeListener(new TextChangeListener() {
			public void textChange(final TextChangeEvent event) {
				dummyDataSource.removeAllContainerFilters();

				// We may add filters directly to the data source to show only
				// contacts that match to our search string.
				dummyDataSource.addContainerFilter(new Filter() {
					public boolean passesFilter(Object itemId, Item item) {
						return ("" + item.getItemProperty(FNAME).getValue()
								+ item.getItemProperty(LNAME).getValue() + item
								.getItemProperty(COMPANY).getValue())
								.toLowerCase().contains(
										event.getText().toLowerCase());
					}

					public boolean appliesToProperty(Object id) {
						return true;
					}
				});
			}
		});
	}

	private void initAddRemoveButtons() {
		addNewContactButton.addClickListener(new ClickListener() {
			public void buttonClick(ClickEvent event) {

				// Rows in the Container data model are called Item. Here we add
				// a new row in the beginning of the list.
				Object contactId = dummyDataSource.addItemAt(0);

				// Each Item has a set of Properties that hold values. Here we
				// set a couple of those.
				contactList.getContainerProperty(contactId, FNAME).setValue(
						"New");
				contactList.getContainerProperty(contactId, LNAME).setValue(
						"Contact");

				// Lets choose the newly created contact to edit it.
				contactList.select(contactId);
			}
		});

		removeContactButton.addClickListener(new ClickListener() {
			public void buttonClick(ClickEvent event) {
				Object contactId = contactList.getValue();
				contactList.removeItem(contactId);
			}
		});
	}

	private void initContactList() {
		contactList.setContainerDataSource(dummyDataSource);
		contactList.setVisibleColumns(new String[] { FNAME, LNAME, COMPANY });
		contactList.setSelectable(true);
		contactList.setImmediate(true);

		contactList.addValueChangeListener(new Property.ValueChangeListener() {
			public void valueChange(ValueChangeEvent event) {
				Object contactId = contactList.getValue();

				// When a contact is selected from the list, we want to show
				// that in our editor on the right. This is nicely done by the
				// FieldGroup that binds all the fields to the correcponding
				// Properties in our contact at once.
				editorFields.setItemDataSource(contactId == null ? null
						: contactList.getItem(contactId));
				removeContactButton.setVisible(contactId != null);
			}
		});
	}

	// Generate some in-memory example data to play with. In a real application
	// we could be using SQLContainer, JPAContainer or some other to persist
	// the data.
	private static IndexedContainer createDummyData() {
		IndexedContainer ic = new IndexedContainer();

		for (String p : fieldNames) {
			ic.addContainerProperty(p, String.class, "");
		}

		// Create dummy data by randomly combining first and last names
		String[] fnames = { "Peter", "Alice", "Joshua", "Mike", "Olivia",
				"Nina", "Alex", "Rita", "Dan", "Umberto", "Henrik", "Rene",
				"Lisa", "Marge" };
		String[] lnames = { "Smith", "Gordon", "Simpson", "Brown", "Clavel",
				"Simons", "Verne", "Scott", "Allison", "Gates", "Rowling",
				"Barks", "Ross", "Schneider", "Tate" };
		for (int i = 0; i < 1000; i++) {
			Object id = ic.addItem();
			ic.getContainerProperty(id, FNAME).setValue(
					fnames[(int) (fnames.length * Math.random())]);
			ic.getContainerProperty(id, LNAME).setValue(
					lnames[(int) (lnames.length * Math.random())]);
		}

		return ic;
	}

}
