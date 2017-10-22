package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JOptionPane;

public class LogGenerator {

	private static File logFile = new File("log.txt");
	private static FileWriter fw;
	private static BufferedWriter bw;
	private static PrintWriter pw;
	
	public static void log(String str){
		
		try {
			fw = new FileWriter(logFile, true);
			bw = new BufferedWriter(fw);
			pw = new PrintWriter(bw);
			
			
			
			pw.println(str);
			pw.close();
			
			
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Log failed","error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		
	}
	
	
}
