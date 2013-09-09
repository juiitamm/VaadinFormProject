/*
 * Copyright 2009 IT Mill Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package vaadinForm;

import com.vaadin.Application;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Form;
import com.vaadin.ui.Window;
import java.util.Vector;

/**
 * The Application's "main" class
 */
@SuppressWarnings("serial")
public class MyVaadinApplication extends Application
{
    private Window window;
    
    @Override
    public void init()
    {
        window = new Window("My Vaadin Application");
        setMainWindow(window);
        
        Form form = new Form();
        form.setCaption("Applicant information");
        window.addComponent(form);
                
        Applicant applicant = new Applicant();
        BeanItem item = new BeanItem(applicant);
        
        Vector order = new Vector();
        order.add("firstName");
        order.add("lastName");
        order.add("gender");
        order.add("arguments");
        
        form.setItemDataSource(item);
        form.setFormFieldFactory(new ApplicantFormFieldFactory());
        form.setVisibleItemProperties(order);
        
        /*
        TextField firstNameField = new TextField("First name");
        firstNameField.setRequired(true);
        form.getLayout().addComponent(firstNameField);
        
        TextField lastNameField = new TextField("Last name");
        lastNameField.setRequired(true);
        form.getLayout().addComponent(lastNameField);
        
        OptionGroup genderSelect = new OptionGroup("Gender");
        genderSelect.addItem("Male");
        genderSelect.addItem("Female");
        //genderSelect.setNullSelectionAllowed(false);
        genderSelect.setNullSelectionItemId("Male");
        form.getLayout().addComponent(genderSelect);
        
        TextArea argumentsTextArea = new TextArea("Why are you applying for this job?");
        form.getLayout().addComponent(argumentsTextArea);

        */
        
        Button button = new Button("Send", form, "commit");
        button.addListener(new Button.ClickListener() {
            public void buttonClick(ClickEvent event) {
//                window.addComponent(new Label("Thank you for clicking"));
                
            }
        });
        form.getLayout().addComponent(button);
        
        
    }
}
