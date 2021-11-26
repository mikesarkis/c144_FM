package com.mycompany.flooringmastery.UserIO;

import java.util.regex.Pattern;

/**Interface for reading and writing to the console
 *
 * @author john, steven, mikaeil
 */
public interface UserIO {
    void print(String msg);
    
    double readDouble(String prompt);
    
    double readDouble(String promp, double min, double max);
    
    float readFloat(String prompt);
    
    float readFloat(String prompt, float min, float max);
    
    int readInt(String prompt);
    
    int readInt(String prompt, int min, int max);
    
    long readLong(String prompt);
    
    long readLong(String prompt, long min, long max);
    
    String readString(String prompt);
    
    String readString(String prompt, Pattern regexPattern);
}
