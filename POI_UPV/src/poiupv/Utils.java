
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poiupv;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import model.Navegacion;

/**
 *
 * @author sovacu
 */
public class Utils {
    /**
     *  A password is considered valid if follows an accepted email syntax:
     *  name@domain.com
     * @param email String which contains the email to check  
     * @return  True if the email is valid. False otherwise.  
     */
    
    //list for special characters
    static ArrayList<String> listSpecial = new ArrayList<String>(); //list of special characters
    static ArrayList<String> listDigits = new ArrayList<String>(); //list of digits
    // return if pass input is valid
    public static Boolean checkPassword (String pass){
        //initialization of the list of special characters
        listSpecial.add("!");
        listSpecial.add("@");
        listSpecial.add("#");
        listSpecial.add("$");
        listSpecial.add("%");
        listSpecial.add("&");
        listSpecial.add("*");
        listSpecial.add("(");
        listSpecial.add(")");
        listSpecial.add("-");
        listSpecial.add("+");
        listSpecial.add("=");
        listSpecial.add("_");
        //initialization for all the digits
        for(int i=1; i<=9; i++){
            listDigits.add("" + i);
        }
        if(pass.length() >= 8 && pass.length() <= 20 && containsSpecialCharacters(pass)
                && containsDigits(pass) && containsUpperCase(pass) && containsLowerCase(pass)){
            return true;
        } else {
            return false;
        }
    }
    //check if email is correct
    public static  Boolean checkEmail (String email)
    {   if(email == null){
          return false; 
        }
       // Regex to check valid email. 
        String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        // Compile the ReGex 
        Pattern pattern = Pattern.compile(regex);
        // Match ReGex with value to check
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    //check if userName is correct
    public static Boolean checkUser (String user, Navegacion navigation){
        if(user.length() < 6 || user.length() > 15 || user.contains(" ")
                || navigation.exitsNickName(user)){
            return false;
        } else {
            return true;
        }
    }
    
     /**
     * 
     * A password is considered valid if it is combination of 8 to 20 numbers or 
     * letters, without blank spaces.
     *
     * @param password String which contains the password to check  
     * @return  True if the password is valid. False otherwise.   
     */ 
    /*public static Boolean checkPassword(String password){     
  
        // If the password is empty , return false 
        if (password == null) { 
            return false; 
        } 
        // Regex to check valid password. 
        String regex =  "^[A-Za-z0-9]{8,20}$"; 
  
        // Compile the ReGex 
        Pattern pattern = Pattern.compile(regex); 
        // Match ReGex with value to check
        Matcher matcher = pattern.matcher(password); 
        return matcher.matches();
    
    }*/
    
    //check if date input is < 12 years from now; return FALSE if user is too young (< 12 years old)
    public static Boolean checkBirthDate (LocalDate date){
        // Birthdate picked is < 12 years from now
        if(date == null){
            return false;
        } else {
            return date.isBefore(LocalDate.now().minusYears(16));
        }
    }
    
    //check if string contains any special character
    private static boolean containsSpecialCharacters(String pass){
        boolean result = false;
        for(int i=0; i<listSpecial.size();i++){
            if(pass.contains(listSpecial.get(i))){
                result = true;
            }
        }
        return result;
    }
    //check if string contains any digits
    private static boolean containsDigits(String pass){
        boolean result = false;
        for(int i=0; i<listDigits.size();i++){
            if(pass.contains(listDigits.get(i))){
                result = true;
            }
        }
        return result;
    }
    //check if string contains any upperCase character
    private static boolean containsUpperCase(String pass){
        boolean result = false;
        for(int i=0; i < pass.length(); i++){
            if(Character.isUpperCase(pass.charAt(i))){
                result = true;
            }
        }
        return result;
    }
    //check if string contains any lowerCase character
    private static boolean containsLowerCase(String pass){
        boolean result = false;
        for(int i=0; i < pass.length(); i++){
            if(Character.isLowerCase(pass.charAt(i))){
                result = true;
            }
        }
        return result;
    }
    
}
