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
    private static int idSequence = 0;

    private static final String[] listFields = new String[]{"firstName", "lastName", "company"};
    private static final String[] listFieldCaptions = new String[]{"First name", "Last name", "Company"};

    private static final String[] editableFields = new String[]{"firstName", "lastName",
        "company", "phone", "email", "street", "city", "zip", "state", "country"};
    private static final String[] editableFieldCaptions = new String[]{"First name", "Last name",
        "Company", "Phone", "Email", "Street", "City", "Zip code", "State", "Country"};

    static {

        /* Create dummy data by randomly combining first and last names */
        String[] fnames = {"Peter", "Alice", "John", "Mike", "Olivia",
            "Nina", "Alex", "Rita", "Dan", "Umberto", "Henrik", "Rene",
            "Lisa", "Linda", "Timothy", "Daniel", "Brian", "George", "Scott",
            "Jennifer"};
        String[] lnames = {"Smith",
            "Johnson", "Williams", "Jones", "Brown", "Davis", "Miller",
            "Wilson", "Moore", "Taylor", "Anderson", "Thomas", "Jackson",
            "White", "Harris", "Martin", "Thompson", "Young",
            "King", "Robinson"};
        String[] companies = {"Sinopec Group", "Wal-Mart Stores, Inc",
            "China National Petroleum Corporation", "", "Royal Dutch Shell",
            "ExxonMobil", "BP", "Saudi Aramco", "State Grid Corporation of China",
            "Vitol", "Volkswagen Group",
            "Total", "Toyota", "Glencore Xstrata", "Chevron",
            "Samsung Electronics", "Apple", "Berkshire Hathaway",
            "China Railway Corporation", "Phillips 66", "E.ON"};

        allContacts = new ArrayList<Person>(NUMBER_OF_ENTRIES);
        for (int i = 0; i < NUMBER_OF_ENTRIES; i++) {
            Person person = new Person();
            person.setId(generateNewId());
            person.setFirstName(fnames[(int) (fnames.length * Math.random())]);
            person.setLastName(lnames[(int) (lnames.length * Math.random())]);
            person.setCompany(companies[(int) (companies.length * Math.random())]);
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

    public static String[] getListFieldCaptions() {
        return listFieldCaptions;
    }

    public static String[] getEditableFieldCaptions() {
        return editableFieldCaptions;
    }

    static Integer generateNewId() {
        return idSequence++;
    }

}
