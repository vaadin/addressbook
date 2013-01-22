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

public class AddressbookUI extends UI {

	private HorizontalSplitPanel splitPanel = new HorizontalSplitPanel();
	VerticalLayout leftLayout = new VerticalLayout();
	private HorizontalLayout bottomLeftLayout = new HorizontalLayout();
	FormLayout editorLayout = new FormLayout();
	FieldGroup editorFields = new FieldGroup();

	private Table contactList = new Table();
	private TextField searchField = new TextField();
	private Button addNewContactButton = new Button("New");
	private Button removeContactButton = new Button("Remove this contact");

	private IndexedContainer dummyDataSource = createDummyData();

	protected void init(VaadinRequest request) {
		initLayout();
		initContactList();
		initEditor();
		initSearch();
		initAddRemoveButtons();
	}

	private void initLayout() {
		setContent(splitPanel);

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
		for (String fieldName : (Collection<String>) contactList
				.getContainerPropertyIds()) {
			TextField field = new TextField(fieldName);
			field.setWidth("100%");
			editorFields.bind(field, fieldName);
			editorLayout.addComponent(field);
		}
		editorLayout.addComponent(removeContactButton);
		editorFields.setBuffered(false);
	}

	private void initSearch() {
		searchField.setInputPrompt("Search contacts");
		searchField.setTextChangeEventMode(TextChangeEventMode.LAZY);

		searchField.addTextChangeListener(new TextChangeListener() {
			public void textChange(final TextChangeEvent event) {
				dummyDataSource.removeAllContainerFilters();
				dummyDataSource.addContainerFilter(new Filter() {
					public boolean passesFilter(Object itemId, Item item) {
						return ("" + item.getItemProperty(FNAME).getValue()
								+ "," + item.getItemProperty(LNAME).getValue()
								+ "," + item.getItemProperty(COMPANY)
								.getValue()).toLowerCase().contains(
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
				Object contactId = contactList.addItemAfter(null);
				contactList.select(contactId);
				contactList.getContainerProperty(contactId, FNAME).setValue(
						"New");
				contactList.getContainerProperty(contactId, LNAME).setValue(
						"Contact");
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
				editorFields.setItemDataSource(contactId == null ? null
						: contactList.getItem(contactId));
				removeContactButton.setVisible(contactId != null);
			}
		});
	}

	private static String FNAME = "First Name";
	private static String LNAME = "Last Name";
	private static String COMPANY = "Company";

	private static IndexedContainer createDummyData() {
		IndexedContainer ic = new IndexedContainer();

		// Add fields to container
		for (String p : new String[] { FNAME, LNAME, COMPANY, "Mobile Phone",
				"Work Phone", "Home Phone", "Work Email", "Home Email",
				"Street", "City", "Zip", "State", "Country" }) {
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
