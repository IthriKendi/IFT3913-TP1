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
    public static long tassert (String stringPath) throws IOException{
        long numAsserts = 0;
        String line;
        Boolean read = false;
        
        BufferedReader br = new BufferedReader(new FileReader(stringPath));

        while ((line = br.readLine()) != null) {
            
            if(line.contains("{")) {
                read = true;
            }

            else if(line.contains("}")) {
                if (line.contains("assert") && read) numAsserts++;
                read = false;
            }
            if (line.contains("assert") && read) numAsserts++;
            
        }

        br.close();

        return numAsserts;

    }
}
