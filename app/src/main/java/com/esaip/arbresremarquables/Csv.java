package com.esaip.arbresremarquables;

import android.os.Environment;

import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.util.List;

//Contient le code source correspondant à la création d'un CSV
public class Csv {

    public Csv(){}

    public void createCSV(List<String[]> data,String name){
        //Chemin du CSV
        java.io.File csv = new java.io.File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Download/"+name+".csv");
        // java.io.File csv = new java.io.File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Android/data/com.esaip.arbresremarquables/files/Pictures");
        //  File csv = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);

        //obligé de mettre dans un try catch pour gérer les erreurs.
        try {
            //Création
            CSVWriter writer = null;

            //on initialise, en affectant le chemin du CSV
            writer = new CSVWriter(new FileWriter(csv));
            //On écrit les données (liste récupéré)
            writer.writeAll(data);
            //On ferme
            writer.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
