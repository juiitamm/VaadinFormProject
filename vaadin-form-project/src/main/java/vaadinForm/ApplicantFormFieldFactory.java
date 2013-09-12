package vaadinForm;

import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroupFieldFactory;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

/**
 * Field factory class for generating proper fields to the form.
 * @author Juho
 */
public class ApplicantFormFieldFactory implements FieldGroupFieldFactory {
    
    private final int maxTextFieldLength = 50;
    private final int maxTextAreaLength = 2500;
    
    /**
     * Creates a field based on the item's property id.
     * @param item The item where the property belongs to.
     * @param propertyId The Id of the property.
     * @param uiContext The component where the field is presented.
     * @return Field for the specified property.
     */
    public Field createField(Item item, Object propertyId, Component uiContext){
        String pid = propertyId.toString();
        
        if("firstName".equals(pid)){
            return getRequiredTextField("First name", "First name is missing");
        }
        else if("lastName".equals(pid)){
            return getRequiredTextField("Last name", "Last name is missing");                        
        }
        else if("gender".equals(pid)){
            OptionGroup genderSelect = new OptionGroup("Gender");
            genderSelect.addItem("Male");
            genderSelect.addItem("Female");
            //Doesnt work: shows "Male" as selected but value is still null
            //genderSelect.setNullSelectionItemId("Male");
            
            return genderSelect;
        }
        else if("arguments".equals(pid)){
            TextArea argumentsArea = new TextArea("Why are you applying for this job?");
            argumentsArea.setNullRepresentation("");
            argumentsArea.setMaxLength(maxTextAreaLength);
            return argumentsArea;
        }

        return null;
    
    }
    
    /**
     * Creates a text field that is marked required in the form.
     * @param caption The caption for the text field.
     * @return Returns a TextField object.
     */
    private TextField getRequiredTextField(String caption, String requiredErrorMessage){
        TextField field = new TextField(caption);
        field.setNullRepresentation("");
        field.setRequiredError(requiredErrorMessage);
        field.setRequired(true);
        field.setMaxLength(maxTextFieldLength);
//        field.setValidationVisible(true);
        //field.setImmediate(true);
        return field;
    }

    @Override
    public <T extends Field> T createField(Class<?> dataType, Class<T> fieldType) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
