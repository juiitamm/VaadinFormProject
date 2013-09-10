package vaadinForm;

import com.vaadin.Application;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Form;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Window;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Vector;

/**
 * Simple application that makes a web form. 
 * User fills the form and submits it.
 * The data is then stored to a database and shown in a results window.
 * @author Juho Tammela
 */
@SuppressWarnings("serial")
public class MyVaadinApplication extends Application {

    private Window formWindow;
    private Window resultsWindow;
    private Applicant applicant;

    @Override
    /**
     * Initializing the application, creating windows and selecting the main window.
     */
    public void init() {
        formWindow = createAndGetFormWindow();
        setMainWindow(formWindow);

        resultsWindow = createAndGetResultsWindow();
    }
    
    /**
     * Creates and returns a window with a form.
     * @return Returns a window with a form.
     */
    private Window createAndGetFormWindow(){
        Window window = new Window("Vaadin form application");
        Label heading = new Label("Applicant information form");
        heading.setStyleName("h1");
        window.addComponent(heading);

        Form form = new Form();
        form.setCaption("Applicant information");
        form.setDescription("Please fill in the required data.");
        //form.setImmediate(true);
        window.addComponent(form);

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
                
                if(saveApplicantInformation(applicant) == true){
                    showApplicantData(resultsWindow);
                }
                else {
                    //TODO...
                }
            }

            
        });
        form.getLayout().addComponent(button);

        
        return window;

    }
    
    /**
     * Creates a view to show the submitted form data in a window.
     * @param window The window where the data is shown.
     */
    private void showApplicantData(Window window) {
        Label subHeading = new Label("The following data was sent successfully!");
        subHeading.setStyleName("h2");
        window.addComponent(subHeading);
        
        GridLayout grid = new GridLayout(2, 4);
        window.addComponent(grid);

        grid.addComponent(new Label("First name: "));
        grid.addComponent(new Label(applicant.getFirstName()));

        grid.addComponent(new Label("Last name: "));
        grid.addComponent(new Label(applicant.getLastName()));

        grid.addComponent(new Label("Gender: "));
        grid.addComponent(new Label(applicant.getGender()));

        grid.addComponent(new Label("Arguments: "));
        grid.addComponent(new Label(applicant.getArguments()));
    }
    
    /**
     * Creates and returns a window to show results after submitting form.
     * @return A window to show the results.
     */
    private Window createAndGetResultsWindow(){
        Window window = new Window("Vaadin form application");
        
        Label heading = new Label("Applicant information form");
        heading.setStyleName("h1");
        window.addComponent(heading);

        return window;
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
            ps.setString(3, applicant.getGender());
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
