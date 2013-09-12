package vaadinForm;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.server.UserError;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.servlet.annotation.WebServlet;

/**
 * Simple application that makes a web form. 
 * User fills the form and submits it.
 * The data is then stored to a database and shown in a results window.
 * @author Juho Tammela
 */
@Theme("mytheme")
@SuppressWarnings("serial")
public class MyVaadinApplication extends UI {

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = MyVaadinApplication.class, widgetset = "vaadinForm.AppWidgetSet")
    public static class Servlet extends VaadinServlet {
    }
    
    private ApplicantFormLayout formLayout;
    private VerticalLayout resultsLayout;

    @Override
    /**
     * Initializing the application, creating layouts and selecting the layout
     * to show.
     */
    public void init(VaadinRequest request) {
        
        formLayout = new ApplicantFormLayout("Applicant information form");
        setContent(formLayout);
        resultsLayout = createAndGetResultsLayout();

        formLayout.getSendButton().addClickListener(new Button.ClickListener() {
            public void buttonClick(ClickEvent event) {
                try {
                    formLayout.clearErrors();
                    formLayout.commit();
                    
                    if(saveApplicantInformation(formLayout.getApplicant()) == true){
                        showApplicantData(resultsLayout, formLayout.getApplicant());
                        setContent(resultsLayout);
                    }
                    else {
                        Label subHeading = new Label("There was a problem sending the data!");
                        subHeading.setStyleName("h2");
                        resultsLayout.addComponent(subHeading);
                        resultsLayout.addComponent(new Label("Please try again or contact the webmaster."));
                        setContent(resultsLayout);
                    }
                } catch (FieldGroup.CommitException ex) {
                    event.getButton().setComponentError(new UserError(ex.getCause().getMessage()));
                    formLayout.setError(ex.getCause().getMessage());
                }
            }
        });
    }
    
    /**
     * Creates a view to show the submitted form data in a window.
     * @param layout The window where the data is shown.
     */
    private void showApplicantData(VerticalLayout layout, Applicant applicant) {
        Label subHeading = new Label("The following data was sent successfully!");
        subHeading.setStyleName("h2");
        layout.addComponent(subHeading);
        layout.setComponentAlignment(subHeading, Alignment.TOP_CENTER);
        
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
        layout.setDefaultComponentAlignment(Alignment.TOP_CENTER);
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
