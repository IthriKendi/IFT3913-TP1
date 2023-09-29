package ift3913;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class tloc {
    
    public static void main(String[] args) throws IOException {
        if (args.length < 1){
            System.out.println("Please provide an argument");
        }
        System.out.println(tloc(args[0]));
    }

    /**
     * This methods takes in argument a path to a java file and returns the number of non-blank and non-comments lines
     * @param stringPath
     * @return number of lines
     * @throws IOException
     */
     public static long tloc(String stringPath) throws IOException{

        long numLines = 0;
        String line;
        Boolean read = true;

        BufferedReader br = new BufferedReader(new FileReader(stringPath));

        while ((line = br.readLine()) != null) {

            // Skip the signle line comments and the blank or empty lines
            if (line.contains("//") || line.isBlank() || line.isEmpty()){
                continue;
            }
            
            // Start of a multiple lines comment
            else if(line.contains("/*")) {
                read = false;
            }

            // End of a multiple lines comment
            else if(line.contains("*/")) {
                read = true;
                continue;
            }

            if (!read) continue; 

            numLines++;
        };

        br.close();
        
        return numLines;
    }
}
