package vaadinForm;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.UserError;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.ui.PageState;
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
    @VaadinServletConfiguration(productionMode = false, 
            ui = MyVaadinApplication.class, widgetset = "vaadinForm.AppWidgetSet")
    public static class Servlet extends VaadinServlet {
    }
    
    private ApplicantFormLayout formLayout;
    private ApplicantResultsLayout resultsLayout;

    @Override
    /**
     * Initializing the application, creating layouts and selecting the layout
     * to show.
     */
    public void init(VaadinRequest request) {
        Page p = new Page(this, new PageState());
        p.setTitle("Vaadin form application");
        
        formLayout = new ApplicantFormLayout("Applicant information form");
        buildAndShowContent(formLayout);
        
        resultsLayout = new ApplicantResultsLayout("Applicant information form");

        formLayout.getSendButton().addClickListener(new Button.ClickListener() {
            public void buttonClick(ClickEvent event) {
                try {
                    formLayout.clearErrors();
                    formLayout.commit();
                    
                    if(saveApplicantInformation(formLayout.getApplicant()) == true){
                        resultsLayout.addApplicantData(formLayout.getApplicant(),
                                "The following data was sent successfully!"); 
                        buildAndShowContent(resultsLayout);
                    }
                    else {
                        Label subHeading = new Label("There was a problem sending the data!");
                        subHeading.setStyleName("h2");
                        resultsLayout.addComponent(subHeading);
                        resultsLayout.addComponent(new Label("Please try again or contact the webmaster."));
                        buildAndShowContent(resultsLayout);
                    }
                } catch (FieldGroup.CommitException ex) {
                    event.getButton().setComponentError(new UserError(ex.getCause().getMessage()));
                    formLayout.setError(ex.getCause().getMessage());
                }
            }
        });
    }
    
    /**
     * Method to take care of setting the page content. Adds a footer to the
     * bottom of the page.
     * @param content The VerticalLayout that holds the content to be rendered.
     */
    private void buildAndShowContent(VerticalLayout content){
        setContent(content);
        addFooter(content);
    }
    
    /**
     * Adds a footer to the given layout.
     * @param content The VerticalLayout where the footer is inserted.
     */
    private void addFooter(VerticalLayout content){
        VerticalLayout footer = new VerticalLayout();
        footer.addStyleName("footer-margin-top");
        
        content.addComponent(footer);
        content.setExpandRatio(footer, 2);
        content.setComponentAlignment(footer, Alignment.BOTTOM_CENTER);
        
        Label author = new Label("Juho Tammela");
        author.addStyleName("footerlabel");
        footer.addComponent(author);

        Label email = new Label("juho.i.tammela@student.jyu.fi");
        email.addStyleName("footerlabel");
        footer.addComponent(email);
        
        ThemeResource vaadinIcon = new ThemeResource("img/vaadin-logo2.png");
        Link vaadinLink = new Link(null, new ExternalResource("http://vaadin.com/"));
        vaadinLink.setIcon(vaadinIcon);
        footer.addComponent(vaadinLink);
        footer.setComponentAlignment(vaadinLink, Alignment.BOTTOM_CENTER);
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

            //System.out.println("Attempting connection!");
            
            connection = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/ApplicantsDB", "postgres",
                    "admin");

            String sql = "INSERT INTO applicants(firstname, lastname, gender, "
                    + "arguments) VALUES(?, ?, ?, ?)";
            ps = connection.prepareStatement(sql);
            
            ps.setString(1, applicant.getFirstName());
            ps.setString(2, applicant.getLastName());
            ps.setString(3, applicant.getGender().toString());
            ps.setString(4, applicant.getArguments());
            
            ps.executeUpdate();
            
            //System.out.println("DATA INSERTED");
            
            
        } catch (SQLException e) {

            //System.out.println("Connection Failed!");
            //e.printStackTrace();
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
                //Ignored...
                //System.out.println("Closing Failed!");
                //return false;
            }
        }
        return true;
    }
}
