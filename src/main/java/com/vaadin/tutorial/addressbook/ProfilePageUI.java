/*!
 * @org Team Wilhelm
 * 
 * @author Brendan and Catherine
 * 
 * Last updated Feb 23rd 2017 by Brendan and Catherine
 * 
 * @brief ProfilePageUI		Used to display the logged in users information to them
 */

package com.vaadin.tutorial.addressbook;

import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;

import com.vaadin.event.ShortcutAction;
import com.vaadin.tutorial.addressbook.backend.Contact;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.TextField;

public class ProfilePageUI extends FormLayout 
{	
	Label userNameLabel = new Label("your username");
	Label userNameContent = new Label("");
	Label userInterestsLabel = new Label("your interests");
	Label locationLabel = new Label("your are here");
	
	Button changeInterestButton = new Button("Modify Interests");
	TextField interestsTextField = new TextField("my interests");
	Button submitNewInterestsButton = new Button("Submit Interests");
	
	Button changePasswordButton = new Button("Change Password");
	Button submitNewPasswordButton = new Button("Submit New Password");
	TextField oldPasswordTextField = new TextField("Old Password:");
	TextField newPasswordTextField = new TextField("New Password");
	TextField confirmNewPasswordTextField = new TextField("Confirm New Password:");
	
	ProfilePageUI()
	{
        configureComponents();
        buildLayout(0);
	}
	
	private void modifyUserInterests()
	{
		interestsTextField.setValue(userInterestsLabel.getValue().toString());
		
		clearLayout();
		
		buildLayout(1);
	}
	
	private void changePassword()
	{
		clearLayout();
		
		buildLayout(2);
	}
	
	private void updateInterests()
	{
		userInterestsLabel.setValue(interestsTextField.getValue().toString());
		
		clearLayout();
		
		buildLayout(0);
	}
	
	private void configureComponents()
	{		
		changeInterestButton.addClickListener(e -> modifyUserInterests());
		submitNewInterestsButton.addClickListener(e -> updateInterests());
		changePasswordButton.addClickListener(e -> changePassword());
	}
	
	private void buildLayout(int extraPieces)
	{
        setSizeUndefined();
        setMargin(true);

        if (extraPieces == 0)
        	addComponents(userNameLabel, userNameContent, userInterestsLabel, locationLabel, 
        			  	  changeInterestButton, changePasswordButton);
        if (extraPieces == 1)
        	addComponents(userNameLabel, userNameContent, userInterestsLabel, locationLabel, 
        			  	  changeInterestButton, changePasswordButton, changeInterestButton,
        			  	  interestsTextField, submitNewInterestsButton);
        else if (extraPieces == 2)
        	addComponents(userNameLabel, userNameContent, userInterestsLabel, locationLabel, 
  			  	  		  changeInterestButton, changePasswordButton, oldPasswordTextField,
  			  	  		  newPasswordTextField, confirmNewPasswordTextField, submitNewPasswordButton);
	}
	
	private void clearLayout()
	{
		removeAllComponents();
	}
	
    @Override
    public AddressbookUI getUI() {
        return (AddressbookUI) super.getUI();
    }
}
