package com.vaadin.tutorial.addressbook;

import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;

import com.vaadin.event.ShortcutAction;
import com.vaadin.tutorial.addressbook.backend.Contact;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.TextField;

public class ProfilePageUI extends FormLayout 
{	
	TextField hi = new TextField("hi");
	
	ProfilePageUI()
	{
        configureComponents();
        buildLayout();
	}
	
	private void configureComponents()
	{
		
	}
	
	private void buildLayout()
	{
        setSizeUndefined();
        setMargin(true);

        addComponents(hi);
	}
	
    @Override
    public AddressbookUI getUI() {
        return (AddressbookUI) super.getUI();
    }
}
