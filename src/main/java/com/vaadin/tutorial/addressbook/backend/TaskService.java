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
public class TaskService {

    // Create dummy data by randomly combining first and last names 
	//Also added dumby data for generating random tasks
    static String[] fnames = { "Peter", "Alice", "John", "Mike", "Olivia",
            "Nina", "Alex", "Rita", "Dan", "Umberto", "Henrik", "Rene", "Lisa",
            "Linda", "Timothy", "Daniel", "Brian", "George", "Scott",
            "Jennifer" };
    static String[] lnames = { "Smith", "Johnson", "Williams", "Jones",
            "Brown", "Davis", "Miller", "Wilson", "Moore", "Taylor",
            "Anderson", "Thomas", "Jackson", "White", "Harris", "Martin",
            "Thompson", "Young", "King", "Robinson" };
    static String[] dumbTasks = { "Walk the dog", "Finish homework", "Rake leafs", "Do laundry",
            "Go for a run", "Finish Software Engineering assignment", "Take a nap", "Do jumping jacks", "Have a standup meeting", "Do some pair programming",
            "Push Changes to branch" };
    

    private static TaskService instance;

    public static TaskService createDemoService() {
        if (instance == null) {

            final TaskService contactService = new TaskService();

            Random r = new Random(0);
            Calendar cal = Calendar.getInstance();
            for (int i = 0; i < 100; i++) {
                Task task = new Task();
                task.setFirstName(fnames[r.nextInt(fnames.length)]);
                task.setLastName(lnames[r.nextInt(fnames.length)]);
                task.setTask(dumbTasks[r.nextInt(dumbTasks.length)]);
                cal.set(2016,
                        r.nextInt(11), r.nextInt(28));
                task.setStart(cal.getTime());
                cal.set(2017,
                        r.nextInt(11), r.nextInt(28));
                task.setEnd(cal.getTime());
                contactService.save(task);
            }
            instance = contactService;
        }

        return instance;
    }

    private HashMap<Long, Task> tasks = new HashMap<>();
    private long nextId = 0;

    public synchronized List<Task> findAll(String stringFilter) {
        ArrayList arrayList = new ArrayList();
        for (Task taski : tasks.values()) {
            try {
                boolean passesFilter = (stringFilter == null || stringFilter.isEmpty())
                        || taski.toString().toLowerCase()
                                .contains(stringFilter.toLowerCase());
                if (passesFilter) {
                    arrayList.add(taski.clone());
                }
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(TaskService.class.getName()).log(
                        Level.SEVERE, null, ex);
            }
        }
        Collections.sort(arrayList, new Comparator<Task>() {

            @Override
            public int compare(Task o1, Task o2) {
                return (int) (o2.getId() - o1.getId());
            }
        });
        return arrayList;
    }

    public synchronized long count() {
        return tasks.size();
    }

    public synchronized void delete(Task value) {
        tasks.remove(value.getId());
    }

    public synchronized void save(Task entry) {
        if (entry.getId() == null) {
            entry.setId(nextId++);
        }
        try {
            entry = (Task) BeanUtils.cloneBean(entry);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
       
        tasks.put(entry.getId(), entry);
    }

}
