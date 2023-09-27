package ift3931;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class App 
{
    public static void main( String[] args ) throws IOException
    {
        /* System.out.println(tloc("tp1\\src\\test\\java\\ift3931\\AppTest.java"));
        System.out.println(tassert("tp1\\src\\test\\java\\ift3931\\AppTest.java"));
        tls("tp1\\src\\test\\java\\ift3931\\TitleTest.java"); */

        File folder = new File("tp1/src/test");
        
        long startTime = System.nanoTime();
        System.out.println(tls(folder));
        tropcomp(folder, 2);
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
    public static ArrayList<String> tls(File folder) throws IOException{

        ArrayList<String> data = new ArrayList<String>();
        String s="";
        String line;
        String classPath = "";
        String packageName = "";
        String className = "";
        DecimalFormat df = new DecimalFormat("0.00");

        for (File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                data.addAll(tls(fileEntry));
                
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
                s=classPath + ", " + packageName + ", " + className + ", " + tloc + ", " + tassert + ", " + df.format(tcmp);
                data.add(s);

                System.out.println(s);
                br.close();
            }
            
        }
         
        return data;
    }
    public static void tropcomp(File folder,int seuil) throws IOException{
        ArrayList<String> tlsReturn = new ArrayList<String>();
        
        tlsReturn=tls(folder);

        Map<Integer, List<Double>> mp = new HashMap<>();
        System.out.println(mp); 
        int i =0;
        while (i< tlsReturn.size()){
            String parts[]=tlsReturn.get(i).split(",");
            List<Double> data = new ArrayList<>();
            data.add(Double.parseDouble(parts[3].replaceAll(" ", "")));
            data.add(Double.parseDouble(parts[5].replaceAll(" ", "")));
            mp.put(i, data);
            i++;
        }
        System.out.println(mp);
        double sumTlocs=0.00;
        double sumTcmps=0.00;
        for(int j=0 ;j<tlsReturn.size();j++){
            Double tlocsVal=mp.get(j).get(0);
            sumTlocs+=tlocsVal;
            Double tcmpVal=mp.get(j).get(1);
            sumTcmps+=tcmpVal;
        }
        double moyTloc=sumTlocs/tlsReturn.size();
        double moyTcmp=sumTcmps/tlsReturn.size();
        double tlocSeuil=(seuil*moyTloc)/100.0+moyTloc;
        double tcmpSeuil=(seuil*moyTcmp)/100.0+moyTcmp;

        ArrayList<Integer> indices =new ArrayList<Integer>();
        int ind=0;
        while(ind <mp.values().size()){
            Double tlocsVal=mp.get(ind).get(0);
            Double tcmpVal=mp.get(ind).get(1);
            if (tlocsVal >tlocSeuil && (tcmpVal >tcmpSeuil)){
                    indices.add(ind);
                }
            ind++;
        }
        for(int k=0;i<tlsReturn.size();k++){
            if (indices.contains(k)){
                String parts[]=tlsReturn.get(k).split(",");
                System.out.println(parts[2]);}
    
        }
    }

}