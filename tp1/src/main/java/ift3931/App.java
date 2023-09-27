package ift3931;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException
    {
        /* System.out.println(tloc("tp1\\src\\test\\java\\ift3931\\AppTest.java"));
        System.out.println(tassert("tp1\\src\\test\\java\\ift3931\\AppTest.java"));
        tls("tp1\\src\\test\\java\\ift3931\\TitleTest.java"); */

        File folder = new File("tp1/src/test");
        
        long startTime = System.nanoTime();
        tls(folder);
        long endTime = System.nanoTime();

        long duration = (endTime - startTime);
        System.out.println(duration/1000000);
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

    //https://stackoverflow.com/questions/1844688/how-to-read-all-files-in-a-folder-from-java
    public static void tls(File folder) throws IOException{

        String line;
        String classPath = "";
        String packageName = "";
        String className = "";
        DecimalFormat df = new DecimalFormat("0.00");

        for (File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                tls(fileEntry);
            } else {
                className = fileEntry.getName();
                className = className.substring( 0,className.length()-5);
                classPath = fileEntry.getPath();
                BufferedReader br = new BufferedReader(new FileReader(fileEntry));

                while ((line = br.readLine()) != null) {

                    if (line.startsWith("package")){
                        packageName = line.substring(8,line.length()-1);
                        break;
                    }   
                }

                long tloc    = tloc(classPath);
                long tassert = tassert(classPath);
                double tcmp    = ((double)tloc / (double)tassert);

                df.setRoundingMode(RoundingMode.DOWN);
                System.out.println(classPath + ", " + packageName + ", " + className + ", " + tloc + ", " + tassert + ", " + df.format(tcmp));

                        br.close();

            }
        }
            

        
    }


   
   

    
}
