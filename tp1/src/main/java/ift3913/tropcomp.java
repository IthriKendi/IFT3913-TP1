package ift3913;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
        
        tlsReturn = tls.tls(folder);

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

    public static void writeCSV(File in, double seuil, File out) throws IOException{
        FileWriter writer = new FileWriter(out);
        
        for(String line : tropcomp(in,seuil)){
            writer.write(line + "\n");
        }

        writer.close();
    }
}
