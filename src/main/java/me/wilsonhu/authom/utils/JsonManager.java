package me.wilsonhu.authom.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;

import com.google.gson.Gson;

public class JsonManager {
	
	public void writeJson(File dir, String filename, Object object) {
		try {
            Gson gson = new Gson();
            String json = gson.toJson(object);
            if(!new File(dir.getAbsolutePath()).exists())new File(dir.getAbsolutePath()).mkdirs();
            FileWriter fw = new FileWriter(dir.getAbsolutePath() + File.separator + filename + ".json");
            fw.write(json);
            fw.flush();
            fw.close();
        } catch (Exception ex) {}
	}
	
	public <T> T readJson(File dir, String filename, Type type) {
		Gson gson = new Gson();
        FileReader fileReader = null;
        BufferedReader buffered = null;
		try {
            fileReader = new FileReader(dir.getAbsolutePath() + File.separator + filename + ".json");
            buffered = new BufferedReader(fileReader);
            Type t = type;
           	return gson.fromJson(fileReader, t);
        } catch (Exception ex) {
            ex.printStackTrace();
        }finally {
        	try {
				buffered.close();
				fileReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
		return null;
	}
}
