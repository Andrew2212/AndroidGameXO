package org.hexlet.gamexo.ai.gardnerway;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import org.hexlet.gamexo.GameActivity;
import org.hexlet.gamexo.ai.utils.LoggerAI;

/**
 * @author KOlegA Date: 01.10.13 Time: 22:18
 */
public class FileMaster {

	private File file;
	private RandomAccessFile fileWriter;

	public FileMaster(String baseDir, String filename) {

		// ---------Get 'filePath' for desktop--------------
		// String path = "src/org/hexlet/gamexo/ai/gardnerway/bin/" + baseDir;

		// ---------Get 'filePath' for android--------------
		String path = GameActivity.getContext().getFilesDir() + baseDir;

		// LoggerAI.p("path = " + path);

		File f1 = new File(path);

		f1.mkdirs();
		file = new File(f1, filename);
		// LoggerAI.p(file.getPath());
		try {
			fileWriter = new RandomAccessFile(file, "rw");
		} catch (FileNotFoundException e) {
			System.out.println("#############");
		}
	}

	public void writeFile(String position) {
		try {

			fileWriter.seek(file.length());
			fileWriter.writeBytes(position + '\n');
			fileWriter.close();

		} catch (FileNotFoundException ex) {
			System.out.println("File " + file.getName() + " not found");

		} catch (IOException e) {
			System.out.println("IOException");
		}
	}

	public String readFile() {

		try {
			return fileWriter.readLine();
		} catch (IOException e) {
			System.out.println("Read File error");
		}
		return null;
	}

	public void readFromScratch() {
		try {
			fileWriter.seek(0);
		} catch (IOException e) {
			System.out.println("Set reading at 0 error");
		}
	}

	public void closeReading() {

		try {
			fileWriter.close();
		} catch (IOException e) {
			System.out.println("Close error");
		}
	}
}
