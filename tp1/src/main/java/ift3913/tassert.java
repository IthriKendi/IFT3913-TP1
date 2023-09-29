package ift3913;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class tassert {
    
    public static void main(String[] args) throws IOException {
        if (args.length < 1){
            System.out.println("Please provide an argument");
        }
        System.out.println(tassert(args[0]));
    }

    /**
     * This method takes in argument a path to a java file and returns the number of assertions within the file
     * @param stringPath
     * @return number of assertions
     * @throws IOException
     */
    public static long tassert (String stringPath) throws IOException{

        long numAsserts = 0;
        String line;
        Boolean read = false;
        
        BufferedReader br = new BufferedReader(new FileReader(stringPath));

        while ((line = br.readLine()) != null) {
            
            // skip single line comments, imports, blank and empty line
            if (line.contains("//") || line.startsWith("import") || line.isBlank() || line.isEmpty()){
                continue;
            }
            
            // Skip multiple line comments
            if(line.contains("/*")) {
                read = false;
            }
            
            if(line.contains("*/")) {
                read = true;
                continue;
            }

             if ((line.contains("assert") || line.contains("fail")) && read) numAsserts++;
        }

        br.close();

        return numAsserts;

    }
}
