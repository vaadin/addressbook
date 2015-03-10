package com.vaadin.tutorial.addressbook.backend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.beanutils.BeanUtils;

/**
 * Dummy backend implementation for the demo, with "detached entities"
 * simulating real world DAO. Typically you'd use e.g. Spring Data based backend
 * or similar EJB facade for your persistency layers.
 */
public class ContactService {

    /* Create dummy data by randomly combining first and last names */
    static String[] fnames = {"Peter", "Alice", "John", "Mike", "Olivia",
        "Nina", "Alex", "Rita", "Dan", "Umberto", "Henrik", "Rene",
        "Lisa", "Linda", "Timothy", "Daniel", "Brian", "George", "Scott",
        "Jennifer"};
    static String[] lnames = {"Smith",
        "Johnson", "Williams", "Jones", "Brown", "Davis", "Miller",
        "Wilson", "Moore", "Taylor", "Anderson", "Thomas", "Jackson",
        "White", "Harris", "Martin", "Thompson", "Young",
        "King", "Robinson"};

    private static ContactService instance;

    public static ContactService createDemoService() {
        if (instance == null) {

            final ContactService contactService = new ContactService();

            Random r = new Random(0);
            for (int i = 0; i < 100; i++) {
                Contact contact = new Contact();
                contact.setFirstName(fnames[r.nextInt(fnames.length)]);
                contact.setLastName(lnames[r.nextInt(fnames.length)]);
                contact.setEmail(
                        contact.getFirstName().toLowerCase() + "@" + contact.
                        getLastName().toLowerCase() + ".com");
                contact.setPhone("+ 358 555 " + (100 + r.nextInt(900)));

                contact.setBirthDate(new Date(30 + r.nextInt(70), r.nextInt(
                        11), r.nextInt(28)));
                contactService.save(contact);
            }
            instance = contactService;
        }

        return instance;
    }

    private HashMap<Long, Contact> contacts = new HashMap<>();
    private long nextId = 0;

    public synchronized List<Contact> findAll(String filter) {
        ArrayList arrayList = new ArrayList();
        for (Contact contact : contacts.values()) {
            try {
                boolean passesFilter = (filter == null || filter.isEmpty())
                        || contact.toString().toLowerCase().contains(filter.
                                toLowerCase());
                if (passesFilter) {
                    arrayList.add(contact.clone());
                }
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(ContactService.class.getName()).
                        log(Level.SEVERE, null, ex);
            }
        }
        Collections.sort(arrayList, new Comparator<Contact>() {

            @Override
            public int compare(Contact o1, Contact o2) {
                return (int) (o2.getId() - o1.getId());
            }
        });
        return arrayList;
    }

    public synchronized long count() {
        return contacts.size();
    }

    public synchronized void delete(Contact value) {
        contacts.remove(value.getId());
    }

    public synchronized void save(Contact entry) {
        if (entry.getId() == null) {
            entry.setId(nextId++);
        }
        try {
            entry = (Contact) BeanUtils.cloneBean(entry);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        contacts.put(entry.getId(), entry);
    }

}
