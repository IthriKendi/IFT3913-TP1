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
     public static long tloc(String stringPath) throws IOException{

        long numLines = 0;
        String line;
        Boolean read = true;

        BufferedReader br = new BufferedReader(new FileReader(stringPath));

        while ((line = br.readLine()) != null) {
            if (line.contains("//") || line.isBlank() || line.isEmpty()){
                continue;
            }
            else if(line.contains("/*")) {
                read = false;
            }
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
