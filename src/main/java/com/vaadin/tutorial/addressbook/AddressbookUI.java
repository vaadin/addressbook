package com.vaadin.tutorial.addressbook;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.tutorial.addressbook.backend.Contact;
import com.vaadin.tutorial.addressbook.backend.ContactService;
import com.vaadin.tutorial.addressbook.backend.User;
import com.vaadin.tutorial.addressbook.backend.UserService;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.Grid;
import com.vaadin.v7.ui.TextField;

/* User Interface written in Java.
 *
 * Define the user interface shown on the Vaadin generated web page by extending the UI class.
 * By default, a new UI instance is automatically created when the page is loaded. To reuse
 * the same instance, add @PreserveOnRefresh.
 */

//CHANGES 1


@Title("Eventstagram")
@Theme("valo")
@Widgetset("com.vaadin.v7.Vaadin7WidgetSet")
public class AddressbookUI extends UI {
	
	protected boolean showingProfilePage = false;
	protected boolean showingLoginForm = false;
	protected boolean showingLoginButton = true;

    /*
     * Hundreds of widgets. Vaadin's user interface components are just Java
     * objects that encapsulate and handle cross-browser support and
     * client-server communication. The default Vaadin components are in the
     * com.vaadin.ui package and there are over 500 more in
     * vaadin.com/directory.
     */
    TextField filter = new TextField();
    Grid contactList = new Grid();
    Button newContact = new Button("New Editor");
    
    Button profilePageButton = new Button("Profile Page");
    Button loginButton = new Button("Login");
    Button logoutButton = new Button("Logout");

    // ContactForm is an example of a custom component class
    ContactForm contactForm = new ContactForm();
    LoginForm loginForm = new LoginForm();
    
    ProfilePageUI profilePageUI = new ProfilePageUI();

    // ContactService is a in-memory mock DAO that mimics
    // a real-world datasource. Typically implemented for
    // example as EJB or Spring Data based service.
    ContactService service = ContactService.createDemoService();
    UserService userService = UserService.createDemoService();

    /*
     * The "Main method".
     *
     * This is the entry point method executed to initialize and configure the
     * visible user interface. Executed on every browser reload because a new
     * instance is created for each web page loaded.
     */
    @Override
    protected void init(VaadinRequest request) {
        configureComponents();
        buildLayout();
    }

    private void configureComponents() {
        /*
         * Synchronous event handling.
         *
         * Receive user interaction events on the server-side. This allows you
         * to synchronously handle those events. Vaadin automatically sends only
         * the needed changes to the web page without loading a new page.
         */
        newContact.addClickListener(e -> contactForm.edit(new Contact()));
        loginButton.addClickListener(e -> openLoginPage());
        logoutButton.setVisible(!showingLoginButton);       //Set the visibility of the logout button opposite of the login button
        profilePageButton.setVisible(!showingLoginButton);  //Set the visibility of the profile button opposite of the login button
        
        profilePageButton.addClickListener(e -> openProfilePage());

        filter.setInputPrompt("Filter Editors...");
        filter.addTextChangeListener(e -> refreshContacts(e.getText()));

        contactList.setContainerDataSource(new BeanItemContainer<>(Contact.class));
        contactList.setColumnOrder("event");
        contactList.removeColumn("id");
        contactList.setSelectionMode(Grid.SelectionMode.SINGLE);
        contactList.addSelectionListener(e -> contactForm.edit((Contact) contactList.getSelectedRow()));
        refreshContacts();
    }

    /*
     * Robust layouts.
     *
     * Layouts are components that contain other components. HorizontalLayout
     * contains TextField and Button. It is wrapped with a Grid into
     * VerticalLayout for the left side of the screen. Allow user to resize the
     * components with a SplitPanel.
     *
     * In addition to programmatically building layout in Java, you may also
     * choose to setup layout declaratively with Vaadin Designer, CSS and HTML.
     */
    private void buildLayout() {
        HorizontalLayout actions = new HorizontalLayout(filter, newContact, profilePageButton,loginButton, logoutButton);
        
        actions.setWidth("100%");
        filter.setWidth("100%");
        actions.setExpandRatio(filter, 1);

        VerticalLayout left = new VerticalLayout(actions, contactList);
        left.setSizeFull();
        contactList.setSizeFull();
        left.setExpandRatio(contactList, 1);

        HorizontalLayout mainLayout = new HorizontalLayout(left, contactForm, profilePageUI, loginForm);
        mainLayout.setSizeFull();
        mainLayout.setExpandRatio(left, 1);

        // Split and allow resizing
        setContent(mainLayout);
    }

    /*
     * Choose the design patterns you like.
     *
     * It is good practice to have separate data access methods that handle the
     * back-end access and/or the user interface updates. You can further split
     * your code into classes to easier maintenance. With Vaadin you can follow
     * MVC, MVP or any other design pattern you choose.
     */
    void refreshContacts() {
        refreshContacts(filter.getValue());
    }

    private void refreshContacts(String stringFilter) {
        contactList.setContainerDataSource(new BeanItemContainer<>(Contact.class, service.findAll(stringFilter)));
        contactForm.setVisible(false);
        profilePageUI.setVisible(false);
        loginForm.setVisible(showingLoginForm);
        
    }
    
    /**!
     * @brief openProfilePage
     */
    private void openProfilePage()
    {
    	showingProfilePage = !showingProfilePage;
    	
    	profilePageUI.setVisible(showingProfilePage);
    }
    private void openLoginPage()
    {
    	showingLoginForm = !showingLoginForm;
    	
    	loginForm.setVisible(showingLoginForm);
    }

    /*
     * Deployed as a Servlet or Portlet.
     *
     * You can specify additional servlet parameters like the URI and UI class
     * name and turn on production mode when you have finished developing the
     * application.
     */
    @WebServlet(urlPatterns = "/*")
    @VaadinServletConfiguration(ui = AddressbookUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }

}
