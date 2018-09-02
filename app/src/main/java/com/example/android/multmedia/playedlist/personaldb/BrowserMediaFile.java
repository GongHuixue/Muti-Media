package com.example.android.multmedia.playedlist.personaldb;

import com.example.android.multmedia.GlobalApplication;
import com.mediaload.bean.BaseItem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class BrowserMediaFile {
    public void saveMediaFile(ArrayList<BaseItem> mediaList, String fileName) {
        FileOutputStream fileOutputStream = null;
        ObjectOutputStream objectOutputStream = null;

        try {
            File file = new File(GlobalApplication.getGlobalContext().getFilesDir().getAbsoluteFile(), fileName);
            fileOutputStream = new FileOutputStream(file.toString());  //新建一个内容为空的文件
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(mediaList);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (objectOutputStream != null) {
            try {
                objectOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (fileOutputStream != null) {
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public ArrayList<BaseItem> getMediaFile(String fileName) {
        ObjectInputStream objectInputStream = null;
        FileInputStream fileInputStream = null;
        ArrayList<BaseItem> savedArrayList = new ArrayList<>();
        try {
            File file = new File(GlobalApplication.getGlobalContext().getFilesDir().getAbsoluteFile(), fileName);
            if(file.exists()) {
                fileInputStream = new FileInputStream(file.toString());
                objectInputStream = new ObjectInputStream(fileInputStream);
                savedArrayList = (ArrayList<BaseItem>) objectInputStream.readObject();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (objectInputStream != null) {
                    objectInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return savedArrayList;
    }
}
