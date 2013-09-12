package vaadinForm;

import com.google.gwt.i18n.server.testing.Gender;
import java.io.Serializable;

/**
 * JavaBean class for the applicant.
 * @author Juho
 */
public class Applicant implements Serializable{
    
    private String firstName;
    private String lastName;
    //private String gender;
    private String arguments;
    
    private Gender gender;
    
    public Applicant(){
        //Set default gender to fix problem with optiongroup default value.
        this.gender = Gender.MALE;
    }
    
    public void setFirstName(String firstName){
        this.firstName = firstName;
    }
    
    public String getFirstName(){
        return this.firstName;
    }
        
    public void setLastName(String lastName){
        this.lastName = lastName;
    }
    
    public String getLastName(){
        return this.lastName;
    }
    
    public void setGender(Gender gender){
        this.gender = gender;
    }
    
    public Gender getGender(){
        return this.gender;
    }
    
    public void setArguments(String arguments){
        this.arguments = arguments;
    }
    
    public String getArguments(){
        return this.arguments;
    }
    
}
