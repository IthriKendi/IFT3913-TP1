package ift3913;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class tropcomp {
    
    public static void main(String[] args) throws IOException {


        if (args.length == 2){

            String path  = args[0];
            File folder  = new File(path);
            double seuil = Double.parseDouble(args[1]);
            tropcomp(folder, seuil);
        }
        else if (args.length > 2 && args[0].equals("-o")){

            File output  = new File(args[1]);
            File folder  = new File(args[2]);
            double seuil = Double.parseDouble(args[3]);

            
            writeCSV(folder, seuil, output);
        }
        else{
            System.out.println("Invalid arguments");
            System.out.println("Please enter : tls <input file path> OR tls -o <output file path> <input file path>");
        }
    }

    public static ArrayList<String> tropcomp(File folder,double seuil) throws IOException{
        
        ArrayList<String> tlsReturn = new ArrayList<String>();
        ArrayList<String> tropcompReturn = new ArrayList<String>();
        
        tlsReturn = tls(folder);

        Map<Integer, List<Double>> mp = new HashMap<>();

        int i = 0;

        while (i < tlsReturn.size()){
            String parts[] = tlsReturn.get(i).split(",");
            List<Double> data = new ArrayList<>();
            data.add(Double.parseDouble(parts[3].replaceAll(" ", "")));
            data.add(Double.parseDouble(parts[5].replaceAll(" ", "")));
            mp.put(i, data);
            i++;
        }

        ArrayList<Double> numTlocs = new ArrayList<Double>();
        ArrayList<Double> numTcmps = new ArrayList<Double>();
        
        for(int j = 0 ; j < tlsReturn.size(); j++){

            Double tlocsVal=mp.get(j).get(0);
            numTlocs.add(tlocsVal);

            Double tcmpVal=mp.get(j).get(1);
            numTcmps.add(tcmpVal);
        }

        ArrayList<Double> sortTlocs = new ArrayList<>(numTlocs);
        ArrayList<Double> sortTcmps = new ArrayList<>(numTcmps);
        Collections.sort(sortTlocs);
        Collections.sort(sortTcmps);

        double sl = (seuil * numTcmps.size()) / 100;
        int seuilInd = numTlocs.size() - (int) Math.ceil(sl);

        ArrayList<Integer> indices = new ArrayList<Integer>();
        int ind = 0;
        double s1 = sortTlocs.get(seuilInd);
        double s2 = sortTcmps.get(seuilInd);

        while(ind < mp.values().size()){
            Double tlocsVal = mp.get(ind).get(0);
            
            Double tcmpVal = mp.get(ind).get(1);
            
            if ((tlocsVal >= s1) && (tcmpVal >= s2)){
                indices.add(ind);
            }

            ind++;
        }


        
        for(int l = 0; l < tlsReturn.size(); l++){
            if (indices.contains(l)){
               
                tropcompReturn.add(tlsReturn.get(l));
                System.out.println(tlsReturn.get(l));
            }
        }

        return tropcompReturn;

    }

    /**
     * This method takes in argument an input and an output path and seuil and writes a csv of all the tropcomp lines in the output path
     * @param in
     * @param out
     * @throws IOException
     */
    public static void writeCSV(File in, double seuil, File out) throws IOException{
        FileWriter writer = new FileWriter(out);
        
        for(String line : tropcomp(in,seuil)){
            writer.write(line + "\n");
        }

        writer.close();
    }

    /**
     * This method takes in argument a folder and goes through that folder in a recursive to find all the test files within that folder and returns the information of the test file in a csv format
     * @param folder
     * @return an arraylist containing the lines of path, package name, class name, tloc, tassert, tcmp
     * @throws IOException
     */
    public static ArrayList<String> tls(File folder) throws IOException{

        ArrayList<String> data = new ArrayList<String>();
        String line;
        String classPath = "";
        String packageName = "";
        String className = "";
        DecimalFormat df = new DecimalFormat("0.00");


        for (File fileEntry : folder.listFiles()) {

            // if directory dive recursively 
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

                long tlc    = tloc(classPath);
                long tsrt = tassert(classPath);
                double tcmp    = ((double)tlc / (double)tsrt);

                // if tassert = 0, we do not cosider this file since it does not have any assertion
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

    /**
     * This method takes in argument an input and an output path and writes a csv of all the tsl lines in the output path
     * @param in
     * @param out
     * @throws IOException
     */
    public static void writeCSV(File in, File out) throws IOException{
        FileWriter writer = new FileWriter(out);
        
        for(String line : tls(in)){
            writer.write(line + "\n");
        }

        writer.close();
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
