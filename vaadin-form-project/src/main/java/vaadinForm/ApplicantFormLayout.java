/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vaadinForm;

import com.google.gwt.i18n.server.testing.Gender;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.server.UserError;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 *
 * @author Juho
 */
public class ApplicantFormLayout extends VerticalLayout {
    
    private Applicant applicant;
    private BeanFieldGroup fieldGroup;
    private FormLayout form;
    
    private Button button;
    private Label errorField;
    private TextField firstNameField;
    private TextField lastNameField;
    
    private final int maxTextFieldLength = 50;
    private final int maxTextAreaLength = 2500;
    
    public ApplicantFormLayout(String headingText){
        
        Label heading = new Label(headingText);
        heading.setStyleName("h1");
        this.addComponent(heading);
        
        applicant = new Applicant();
        BeanItem item = new BeanItem(applicant);
        
        fieldGroup = new BeanFieldGroup<Applicant>(Applicant.class);
        fieldGroup.setItemDataSource(item);
        
        form = new FormLayout();
        this.addComponent(form);
        
        firstNameField = getBuildAndBindTextField("First name", "firstName", "First name is missing");
        //firstNameField = fieldGroup.buildAndBind("First name", "firstName", TextField.class);
        //firstNameField.setNullRepresentation("");
        //firstNameField.setRequiredError("First name is missing");
        //firstNameField.setRequired(true);
        //firstNameField.setMaxLength(maxTextFieldLength);
        //firstNameField.setValidationVisible(false);
        //firstNameField.addValidator(new BeanValidator(Applicant.class, "firstName"));
        //firstNameField.setComponentError(new UserError("FIRST NAME ERROR"));
        
        
        lastNameField = getBuildAndBindTextField("Last name", "lastName", "Last name is missing");
        //lastNameField = fieldGroup.buildAndBind("Last name", "lastName", TextField.class);
        //lastNameField.setNullRepresentation("");
        //lastNameField.setRequiredError("Last name is missing");
        //lastNameField.setRequired(true);
        //lastNameField.setMaxLength(maxTextFieldLength);
        //lastNameField.setValidationVisible(false);
        
        OptionGroup genderOption = fieldGroup.buildAndBind("Gender", "gender", OptionGroup.class);
        genderOption.removeItem(Gender.UNKNOWN);
        genderOption.setItemCaptionMode(AbstractSelect.ItemCaptionMode.EXPLICIT);
        genderOption.setItemCaption(Gender.MALE, "Male");
        genderOption.setItemCaption(Gender.FEMALE, "Female");
        
        TextArea argumentsArea = fieldGroup.buildAndBind("Why are you applying for this job?", "arguments", TextArea.class);
        argumentsArea.setNullRepresentation("");
        argumentsArea.setMaxLength(maxTextAreaLength);
        
        form.addComponent(firstNameField);
        form.addComponent(lastNameField);
        form.addComponent(genderOption);
        form.addComponent(argumentsArea);
        
        button = new Button("Send");
        form.addComponent(button);
        
        errorField = new Label();
        errorField.setVisible(false);
        form.addComponent(errorField);
    }
    
    private TextField getBuildAndBindTextField(String caption, String propertyId, String requiredErrorMessage){
        TextField field = fieldGroup.buildAndBind(caption, propertyId, TextField.class);
        field.setNullRepresentation("");
        field.setRequiredError(requiredErrorMessage);
        field.setRequired(true);
        field.setMaxLength(maxTextFieldLength);
        field.setValidationVisible(false);
        return field;
    }
    
    public Applicant getApplicant(){
        return this.applicant;
    }
    
    public Button getSendButton(){
        return this.button;
    }
    
    public void commit() throws CommitException{
        fieldGroup.commit();
    }
    
    public void setError(String error){
        errorField.setValue(error);
        errorField.setVisible(true);
        
        if(firstNameField.getValue() == null){
            firstNameField.setComponentError(new UserError(firstNameField.getRequiredError()));
        }
        if(lastNameField.getValue() == null){
            lastNameField.setComponentError(new UserError(lastNameField.getRequiredError()));
        }
        
        /*
        for (int i = 0; i < this.getComponentCount(); i++) {
            Component c = this.getComponent(i);
            if(c.)
        }
        */ 
    }
    
    public void clearErrors(){
        errorField.setValue(null);
        errorField.setVisible(false);
        
        firstNameField.setComponentError(null);
        lastNameField.setComponentError(null);
        button.setComponentError(null);
    }
    
}
