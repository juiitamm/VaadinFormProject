package vaadinForm;

import com.vaadin.Application;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Form;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Window;
import java.util.Vector;

/**
 * The Application's "main" class
 */
@SuppressWarnings("serial")
public class MyVaadinApplication extends Application
{
    private Window formWindow;
    private Window resultsWindow;
    private Applicant applicant;
    
    @Override
    public void init()
    {
        formWindow = new Window("Vaadin form application");
        setMainWindow(formWindow);
        
        resultsWindow = new Window("Vaadin form application");
        
        
        Label heading = new Label("Applicant information form");
        heading.setStyleName("h1");
        formWindow.addComponent(heading);

        Form form = new Form();
        form.setCaption("Applicant information");
        form.setDescription("Please fill in the required data.");
        //form.setImmediate(true);
        formWindow.addComponent(form);
                
        applicant = new Applicant();
        BeanItem item = new BeanItem(applicant);
        
        Vector order = new Vector();
        order.add("firstName");
        order.add("lastName");
        order.add("gender");
        order.add("arguments");
        
        form.setItemDataSource(item);
        form.setFormFieldFactory(new ApplicantFormFieldFactory());
        form.setVisibleItemProperties(order);
        
        Button button = new Button("Send", form, "commit");
        button.addListener(new Button.ClickListener() {
            public void buttonClick(ClickEvent event) {
                removeWindow(formWindow);
                setMainWindow(resultsWindow);

                Label heading = new Label("Applicant information form");
                heading.setStyleName("h1");
                resultsWindow.addComponent(heading);

                
                Label subHeading = new Label("The following data was sent successfully!");
                subHeading.setStyleName("h2");
                resultsWindow.addComponent(subHeading);
                
                GridLayout grid = new GridLayout(2, 4);
                resultsWindow.addComponent(grid);
                
                grid.addComponent(new Label("First name: "));
                grid.addComponent(new Label(applicant.getFirstName()));
                
                grid.addComponent(new Label("Last name: "));
                grid.addComponent(new Label(applicant.getLastName()));
                
                grid.addComponent(new Label("Gender: "));
                grid.addComponent(new Label(applicant.getGender()));
                
                grid.addComponent(new Label("Arguments: "));
                grid.addComponent(new Label(applicant.getArguments()));
            }
        });
        form.getLayout().addComponent(button);
        
        
    }
}
