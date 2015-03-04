package com.vaadin.tutorial.addressbook;

import com.vaadin.data.util.IndexedContainer;
import java.util.ArrayList;
import java.util.List;

/**
 * Addressbook API is the data source for the application.
 *
 */
public class Addressbook {

    /**
     * Change this to test with different number of entries.
     */
    private static final int NUMBER_OF_ENTRIES = 1000;

    private static final List<Person> allContacts;

    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String COMPANY = "company";

    private static final String[] listFields = new String[]{FIRST_NAME, LAST_NAME};
    private static final String[] editableFields = new String[]{FIRST_NAME, LAST_NAME,
        COMPANY, "phone", "email", "street", "city", "zip", "state", "country"};

    static {

        /* Create dummy data by randomly combining first and last names */
        String[] fnames = {"Peter", "Alice", "Joshua", "Mike", "Olivia",
            "Nina", "Alex", "Rita", "Dan", "Umberto", "Henrik", "Rene",
            "Lisa", "Marge"};
        String[] lnames = {"Smith", "Gordon", "Simpson", "Brown", "Clavel",
            "Simons", "Verne", "Scott", "Allison", "Gates", "Rowling",
            "Barks", "Ross", "Schneider", "Tate"};

        allContacts = new ArrayList<Person>(NUMBER_OF_ENTRIES);
        for (int i = 0; i < NUMBER_OF_ENTRIES; i++) {
            Person person = new Person();
            person.setId(i);
            person.setFirstName(fnames[(int) (fnames.length * Math.random())]);
            person.setLastName(lnames[(int) (lnames.length * Math.random())]);
            allContacts.add(person);
        }

    }

    /**
     * Get list of all contacts in the address book.
     *
     * @return
     */
    public static List<Person> getAllContacts() {
        return allContacts;
    }

    /**
     * Get a list of properties that are editable in the address book UI.
     *
     * @return
     */
    public static String[] getEditableFields() {
        return editableFields;
    }

    /**
     * Get list of properties that are visible in the address book list.
     *
     * @return
     */
    public static String[] getListFields() {
        return listFields;
    }
}
