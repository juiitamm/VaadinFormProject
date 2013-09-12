package vaadinForm;

import com.google.gwt.i18n.server.testing.Gender;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.annotation.WebServlet;

/**
 * Simple application that makes a web form. 
 * User fills the form and submits it.
 * The data is then stored to a database and shown in a results window.
 * @author Juho Tammela
 */
@Theme("runo")
@SuppressWarnings("serial")
public class MyVaadinApplication extends UI {

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = MyVaadinApplication.class, widgetset = "vaadinForm.AppWidgetSet")
    public static class Servlet extends VaadinServlet {
    }
    
    private VerticalLayout formLayout;
    private VerticalLayout resultsLayout;
    private BeanFieldGroup fg;
    
    private final int maxTextFieldLength = 50;
    private final int maxTextAreaLength = 2500;
    
    private Applicant applicant;

    @Override
    /**
     * Initializing the application, creating layouts and selecting the layout
     * to show.
     */
    public void init(VaadinRequest request) {
        
        formLayout = createAndGetFormLayout();
        setContent(formLayout);
        resultsLayout = createAndGetResultsLayout();
    }
    
    
    /**
     * Creates and returns a window with a form.
     * @return Returns a window with a form.
     */
    private VerticalLayout createAndGetFormLayout(){
        
        VerticalLayout layout = new VerticalLayout();
        
        Label heading = new Label("Applicant information form");
        heading.setStyleName("h1");
        layout.addComponent(heading);

        applicant = new Applicant();
        BeanItem item = new BeanItem(applicant);
        
        fg = new BeanFieldGroup<Applicant>(Applicant.class);
        fg.setItemDataSource(item);
        
        TextField firstNameField = fg.buildAndBind("First name", "firstName", TextField.class);
        firstNameField.setNullRepresentation("");
        firstNameField.setRequiredError("First name is missing");
        firstNameField.setRequired(true);
        firstNameField.setMaxLength(maxTextFieldLength);
        
        TextField lastNameField = fg.buildAndBind("Last name", "lastName", TextField.class);
        lastNameField.setNullRepresentation("");
        lastNameField.setRequiredError("Last name is missing");
        lastNameField.setRequired(true);
        lastNameField.setMaxLength(maxTextFieldLength);
        
        OptionGroup genderOption = fg.buildAndBind("Gender", "gender", OptionGroup.class);
        genderOption.removeItem(Gender.UNKNOWN);
        
        TextArea argumentsArea = fg.buildAndBind("Why are you applying for this job?", "arguments", TextArea.class);
        argumentsArea.setNullRepresentation("");
        argumentsArea.setMaxLength(maxTextAreaLength);
        
        layout.addComponent(firstNameField);
        layout.addComponent(lastNameField);
        layout.addComponent(genderOption);
        layout.addComponent(argumentsArea);
        
        Button button = new Button("Send");
        button.addClickListener(new Button.ClickListener() {
            public void buttonClick(ClickEvent event) {
                try {
                    fg.commit();
                    
                    if(saveApplicantInformation(applicant) == true){
                        showApplicantData(resultsLayout);
                        setContent(resultsLayout);
                    }
                    else {
                        //TODO... what happens?
                    }
                } catch (FieldGroup.CommitException ex) {
                    //TODO: handle showing error message
                    Logger.getLogger(MyVaadinApplication.class.getName()).log(Level.SEVERE, null, ex);
                }
                
               
               
            }
        });
        layout.addComponent(button);
        return layout;
    }
    
    
    /**
     * Creates a view to show the submitted form data in a window.
     * @param layout The window where the data is shown.
     */
    private void showApplicantData(VerticalLayout layout) {
        Label subHeading = new Label("The following data was sent successfully!");
        subHeading.setStyleName("h2");
        layout.addComponent(subHeading);
        
        GridLayout grid = new GridLayout(2, 4);
        layout.addComponent(grid);

        grid.addComponent(new Label("First name: "));
        grid.addComponent(new Label(applicant.getFirstName()));

        grid.addComponent(new Label("Last name: "));
        grid.addComponent(new Label(applicant.getLastName()));

        grid.addComponent(new Label("Gender: "));
        grid.addComponent(new Label(applicant.getGender().toString()));

        grid.addComponent(new Label("Arguments: "));
        grid.addComponent(new Label(applicant.getArguments()));
    }
    
    
    /**
     * Creates and returns a layout to show results after submitting form.
     * @return A layout to show the results.
     */
    private VerticalLayout createAndGetResultsLayout(){
        VerticalLayout layout = new VerticalLayout();
        
        Label heading = new Label("Applicant information form");
        heading.setStyleName("h1");
        layout.addComponent(heading);

        return layout;
    }

    /**
     * Inserts the form data to a database.
     * @param applicant The applicant whose information will be stored.
     * @return  Returns true if everything went well, false otherwise.
     */
    private boolean saveApplicantInformation(Applicant applicant) {

        Connection connection = null;
        PreparedStatement ps = null;

        try {

            System.out.println("Attempting connection!");
            
            connection = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/ApplicantsDB", "postgres",
                    "admin");

            String sql = "INSERT INTO applicants(firstname, lastname, gender, arguments) VALUES(?, ?, ?, ?)";
            ps = connection.prepareStatement(sql);
            
            ps.setString(1, applicant.getFirstName());
            ps.setString(2, applicant.getLastName());
            ps.setString(3, applicant.getGender().toString());
            ps.setString(4, applicant.getArguments());
            
            ps.executeUpdate();
            
            System.out.println("DATA INSERTED");
            
            
        } catch (SQLException e) {

            System.out.println("Connection Failed!");
            e.printStackTrace();
            return false;

        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (connection != null) {
                    connection.close();
                }

            } catch (SQLException ex) {
                System.out.println("Closing Failed!");
                return false;
            }
        }
        return true;
    }
}
