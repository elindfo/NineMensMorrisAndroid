package com.example.erik.ninemensmorrisassignment.model;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.example.erik.ninemensmorrisassignment.model.NineMensMorrisGame.PlayfieldPosition;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SaveTool {
    private static final String FILE_NAME = "model.ser";
    private static final String TAG = "SaveTool";
    private static SaveTool saveTool;
    private File file;

    public SaveTool(Context context) {
        file = new File(context.getFilesDir(), FILE_NAME);
    }

    public static SaveTool getInstance (Context context){
        if(saveTool == null){
            saveTool = new SaveTool(context);
        }
        return saveTool;
    }

    public void saveModel(PlayfieldPosition[] morrisGame) {
        FileOutputStream fileOutputStream;
        ObjectOutputStream objectOutputStream;

        ArrayList<PlayfieldPosition> listOfPLayfieldPos = new ArrayList<>();

        for (int i = 0; i < morrisGame.length; i++) {
            listOfPLayfieldPos.add(morrisGame[i]);
        }

        try {
            fileOutputStream = new FileOutputStream(FILE_NAME, false);
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            for (PlayfieldPosition p : morrisGame) {
                objectOutputStream.writeObject(p);
            }
            objectOutputStream.close();

        } catch (FileNotFoundException e) {
            Log.d(TAG, "FileNotFound");
            e.printStackTrace();
        } catch (IOException e) {
            Log.d(TAG, "IOException");
            e.printStackTrace();
        }
    }

    public PlayfieldPosition[] loadModel() {
        FileInputStream fileInputStream;
        ObjectInputStream objectInputStream;
        try {
            fileInputStream = new FileInputStream(FILE_NAME);
            objectInputStream = new ObjectInputStream(fileInputStream);
            PlayfieldPosition p;
            PlayfieldPosition[] playfieldPositions = new PlayfieldPosition[]{};
            int i = 0;
            do{
                p = (PlayfieldPosition) objectInputStream.readObject();
                playfieldPositions[i] = p;
                i++;
            }while (p != null);
            return playfieldPositions;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
