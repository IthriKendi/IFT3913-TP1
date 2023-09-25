package ift3931;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException
    {
        System.out.println(tloc("tp1\\src\\test\\java\\ift3931\\AppTest.java"));
        System.out.println(tassert("tp1\\src\\test\\java\\ift3931\\AppTest.java"));
        tls("tp1\\src\\test\\java\\ift3931\\TitleTest.java");
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

    public static void tls(String stringPath) throws IOException{

        BufferedReader br = new BufferedReader(new FileReader(stringPath));
        String line;
        String packageName = "";
        String className = "";

        while ((line = br.readLine()) != null) {

            if (line.startsWith("package")){
                packageName = line.substring(8,line.length()-1);
            }   
        }
        
        int lastIndex = stringPath.lastIndexOf('\\');        
        className = stringPath.substring(lastIndex + 1,stringPath.length() - 5);   
            
        

        long tloc    = tloc(stringPath);
        long tassert = tassert(stringPath);
        double tcmp    = Math.round((double)tloc / (double)tassert);

        System.out.println(packageName + ", " + className + ", " + tloc + ", " + tassert + ", " + tcmp);
        
        br.close();
    }

}
