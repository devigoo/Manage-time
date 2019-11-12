package main;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.TimerTask;
import java.util.TreeMap;

public class Xml1 {

	
	
	public void saveTxt(TreeMap<Integer, String> textTree, File file) {

		try {
			XMLEncoder x = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(file)));
			x.writeObject(textTree);
			x.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	public void saveTasks(TreeMap<Integer, TimerTask> tasks, File file) {

		try {
			XMLEncoder x = new XMLEncoder(new BufferedOutputStream( new FileOutputStream(file)));
			x.writeObject(tasks);
			x.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	public TreeMap<Integer, TimerTask> readTasks(File file) throws IOException {

	
		XMLDecoder d = new XMLDecoder(new BufferedInputStream(new FileInputStream(file)));
		TreeMap<Integer, TimerTask> tasks = (TreeMap<Integer, TimerTask>) d.readObject();
		d.close();
		return tasks;
	

	}

	public TreeMap<Integer, String> readTasksTxt(File file) throws IOException {
		
		XMLDecoder d = new XMLDecoder(new BufferedInputStream(new FileInputStream(file)));
		TreeMap<Integer, String> textTree = (TreeMap<Integer, String>) d.readObject();
		return textTree;

	}

}
