package com.vaadin.tutorial.addressbook.backend;

import org.apache.commons.beanutils.BeanUtils;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/** Separate Java service class.
 * Backend implementation for the address book application, with "detached entities"
 * simulating real world DAO. Typically these something that the Java EE
 * or Spring backend services provide.
 */
// Backend service class. This is just a typical Java backend implementation
// class and nothing Vaadin specific.

public class UserService {

    // Create dummy data by randomly combining first and last names
    static String[] usernames = { "Peter", "Aliee", "John", "Mike", "Olivia",
            "Nina", "Alex", "Rita", "Dan", "Umberto", "Henrik", "Rene", "Lisa",
            "Linda", "Timothy", "Daniel", "Brian", "George", "Scott",
            "Jennifer" };
    static String[] passwords = { "Smith", "Johnson", "Williams", "Jones",
            "Brown", "Davis", "Miller", "Wilson", "Moore", "Taylor",
            "Anderson", "Thomas", "Jackson", "White", "Harris", "Martin",
            "Thompson", "Young", "King", "Robinson" };

    private static UserService instance;

    public static UserService createDemoService() {
        if (instance == null) {

            final UserService userService = new UserService();

            //Random r = new Random(0);
            Calendar cal = Calendar.getInstance();
            for (int i = 0; i < 20; i++) {
                User user = new User();
                user.setUsername(usernames[i]);
                user.setPassword(passwords[i]);
                userService.save(user);
            }
            instance = userService;
        }

        return instance;
    }

    private HashMap<Long, User> users = new HashMap<>();
    private long nextId = 0;

    //old header = public synchronized List<User> findAll(String stringFilter) {
    public synchronized ArrayList<User> findAll(String stringFilter) {
        ArrayList arrayList = new ArrayList();
        for (User contact : users.values()) {
            try {
                boolean passesFilter = (stringFilter == null || stringFilter.isEmpty())
                        || contact.toString().toLowerCase()
                                .contains(stringFilter.toLowerCase());
                if (passesFilter) {
                    arrayList.add(contact.clone());
                }
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(UserService.class.getName()).log(
                        Level.SEVERE, null, ex);
            }
        }
        Collections.sort(arrayList, new Comparator<User>() {

            @Override
            public int compare(User o1, User o2) {
                return (int) (o2.getId() - o1.getId());
            }
        });
        return arrayList;
    }

    public synchronized long count() {
        return users.size();
    }

    public synchronized void delete(User value) {
        users.remove(value.getId());
    }

    public synchronized void save(User entry) {
        if (entry.getId() == null) {
            entry.setId(nextId++);
        }
        try {
            entry = (User) BeanUtils.cloneBean(entry);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        users.put(entry.getId(), entry);
    }

}
