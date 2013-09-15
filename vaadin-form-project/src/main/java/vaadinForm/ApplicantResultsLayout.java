package vaadinForm;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * A layout class to display the information of an applicant.
 * @author Juho
 */
public class ApplicantResultsLayout extends VerticalLayout {
    
    /**
     * Creates a new ApplicantResultsLayout with a main heading.
     * @param headingText The level 1 heading text for the layout.
     */
    public ApplicantResultsLayout(String headingText){
        this.setHeight("100%");
        this.setDefaultComponentAlignment(Alignment.TOP_CENTER);
        Label heading = new Label(headingText);
        heading.setStyleName("h1");
        this.addComponent(heading);
        
    }
    
    /**
     * Adds the given applicant's data into a gridlayout.
     * @param applicant The applicant whose data will be shown.
     * @param headingText Text for a level 2 heading.
     */
    public void addApplicantData(Applicant applicant, String headingText){
        Label subHeading = new Label(headingText);
        subHeading.setStyleName("h2");
        this.addComponent(subHeading);
        this.setComponentAlignment(subHeading, Alignment.TOP_CENTER);
        
        GridLayout grid = new GridLayout(2, 4);
        this.addComponent(grid);
        grid.setWidth("450px");
        
        grid.setRowExpandRatio(3, 5);
        grid.setColumnExpandRatio(0, 1);
        grid.setColumnExpandRatio(1, 5);

        grid.addComponent(new Label("First name: "));
        grid.addComponent(new Label(applicant.getFirstName()));

        grid.addComponent(new Label("Last name: "));
        grid.addComponent(new Label(applicant.getLastName()));

        grid.addComponent(new Label("Gender: "));
        grid.addComponent(new Label(applicant.getGender().toString()));

        grid.addComponent(new Label("Arguments: "));
        Label argumentsLabel = new Label(applicant.getArguments());
        grid.addComponent(argumentsLabel);
    }
    
}
