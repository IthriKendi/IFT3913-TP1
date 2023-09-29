package ift3913;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class tls {
    
    public static void main(String[] args) throws IOException {

        if (args.length == 1){
            String path = args[0];
            File folder = new File(path);
            System.out.println(tls(folder));
        }
        else if (args.length > 1 && args[0].equals("-o")){

            File output = new File(args[1]);
            File folder = new File(args[2]);
            
            writeCSV(folder, output);
        }
        else{
            System.out.println("Invalid arguments");
            System.out.println("Please enter : tls <input file path> OR tls -o <output file path> <input file path>");
        }

        /* File folder = new File("C:\\Users\\ithri\\Desktop\\IFT3913_TP1\\tp1\\src\\test");
        System.out.println(tls(folder)); */
        
        
    }
    //https://stackoverflow.com/questions/1844688/how-to-read-all-files-in-a-folder-from-java
    public static ArrayList<String> tls(File folder) throws IOException{

        ArrayList<String> data = new ArrayList<String>();
        String line;
        String classPath = "";
        String packageName = "";
        String className = "";
        DecimalFormat df = new DecimalFormat("0.00");

        for (File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                data.addAll(tls(fileEntry));
            } 
            else if (fileEntry.getName().contains("Test") || fileEntry.getName().contains("test")){
                
                className = fileEntry.getName();
                className = className.substring( 0,className.length()-5);
                classPath = fileEntry.getAbsolutePath();
                BufferedReader br = new BufferedReader(new FileReader(fileEntry));

                while ((line = br.readLine()) != null) {

                    if (line.startsWith("package")){
                        packageName = line.substring(8,line.length()-1);
                        break;
                    }   
                }

                long tlc    = tloc.tloc(classPath);
                long tsrt = tassert.tassert(classPath);
                double tcmp    = ((double)tlc / (double)tsrt);

                if (tsrt!=0){

                    tcmp   = ((double)tlc / (double)tsrt);
                

                    df.setRoundingMode(RoundingMode.DOWN);
                    String s = classPath + ", " + packageName + ", " + className + ", " + tlc + ", " + tsrt + ", " + df.format(tcmp);

                    data.add(s);

                    br.close();

                }
                else{
                    continue;
                }

            }
        }

        return data;
        
    }

    public static void writeCSV(File in, File out) throws IOException{
        FileWriter writer = new FileWriter(out);
        
        for(String line : tls(in)){
            writer.write(line + "\n");
        }

        writer.close();
    }
}
