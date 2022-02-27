package com.anand.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;



public class ReadAllFiles {
  public static File folder = new File("C:\\.FTPC\\ProductionCentre\\SOSLogs");
  static String temp = "";
  public  String filepath = "";
  public  ProcessFiles objReadFileExample1 = null;
  private SmartLogFilter date;
  private int n;
  private boolean isExceptionChecked = false;

  
  public ReadAllFiles() throws FileNotFoundException, UnsupportedEncodingException
{

}

  public void listFilesForFolder(final File folder) throws FileNotFoundException, UnsupportedEncodingException, ParseException {
	  System.out.println("Reading files under the folder "+ folder.getAbsolutePath());

		objReadFileExample1 = new ProcessFiles(date, n, isExceptionChecked);
    for (final File fileEntry : folder.listFiles()) {
      if (fileEntry.isDirectory()) {
        // System.out.println("Reading files under the folder "+folder.getAbsolutePath());
        listFilesForFolder(fileEntry);
      } else {
        if (fileEntry.isFile()) {
          temp = fileEntry.getName();
          System.out.println("File= " + folder.getAbsolutePath()+ "\\" + fileEntry.getName());
          
            objReadFileExample1.fileRead(folder.getAbsolutePath()+ "\\" + fileEntry.getName());
        }

      }
    }
  }
  public void processDateTime(SmartLogFilter date, int n, boolean isExceptionChecked) throws FileNotFoundException, UnsupportedEncodingException, ParseException {
	
	  this.date = date;
	  this.n = n;
	  this.isExceptionChecked = isExceptionChecked;
	  this.listFilesForFolder(folder);
//	  ReadAllFiles allFiles = new ReadAllFiles();
}
}