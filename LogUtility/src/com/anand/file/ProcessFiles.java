package com.anand.file;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProcessFiles {

	private SmartLogFilter date;
	private int n;
	private boolean isExceptionChecked = false;
	private String str_date;
	private String timestamp;
	private Calendar date1;
	private Calendar date2;
	private DateFormat sm = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	private HashMap<String, Integer> map = new HashMap<String, Integer>();
	  
	  public ProcessFiles(SmartLogFilter date, int n, boolean isExceptionChecked)
	{
		  this.date = date;
		  this.n = n+1;
		  this.isExceptionChecked = isExceptionChecked;
		  this.str_date = sm.format(this.date.getDate());
		  System.out.println("ISEXCEPTIONCHECKED: "+this.isExceptionChecked);
		  
		  this.date1 = Calendar.getInstance();
		  this.date1.setTime(this.date.getDate());
		  this.date1.set(Calendar.SECOND,(this.date1.get(Calendar.SECOND)-this.n));
		  System.out.println("LOWER DATE: "+sm.format(this.date1.getTime()));
		  
		  System.out.println("Selected DATE: "+this.str_date);
		  
		  this.date2 = Calendar.getInstance();
		  this.date2.setTime(this.date.getDate());
		  this.date2.add(Calendar.SECOND,this.n);
		  System.out.println("UPPER DATE: "+sm.format(this.date2.getTime()));

	}

	public void fileRead(String filePath) throws FileNotFoundException, UnsupportedEncodingException, ParseException {

		BufferedReader br = null;
		FileReader fr = null;
		String size;
		String sCurrentLine;
		String result = new String();

			//br = new BufferedReader(new FileReader(FILENAME));
			fr = new FileReader(filePath);
			br = new BufferedReader(fr);
			
		try {
				String filename = "C:/Users/AMohan7/Desktop/Tool/DATA/filteredLogs.log";
				/*PrintWriter writer = new PrintWriter(filename , "UTF-8");*/
				//FileWriter writer2 = new FileWriter(filename, true);
				FileOutputStream fos = new FileOutputStream(filename, true);
				String patternString = "Exception:";
				String exception = "";
				fos.write(("READING FILE: "+filePath).getBytes());
				fos.write(System.getProperty("line.separator").getBytes());
				while((sCurrentLine = br.readLine()) != null){
	
					this.timestamp = sCurrentLine.substring(20, 39);
					Date logDate = sm.parse(this.timestamp);
					
					if(logDate.after(this.date1.getTime()) && logDate.before(this.date2.getTime())) {
						result = sCurrentLine;
						fos.write(result.getBytes());
						fos.write(System.getProperty("line.separator").getBytes());
						result = null;
						if(sCurrentLine.contains(patternString) ) {
							exception = sCurrentLine.substring(42);
							if(map.containsKey(exception)) {
								map.put(exception, map.get(exception)+1);
							}
							else {
								map.put(exception, 1);
							}
						}
					}
					
				
			}
				if(this.isExceptionChecked && !this.map.isEmpty()) {
					fos.write(System.getProperty("line.separator").getBytes());
					fos.write("EXCEPTIONS:".getBytes());
					fos.write(System.getProperty("line.separator").getBytes());
					for (Map.Entry entry : map.entrySet()) {
			            fos.write((entry.getKey()+" OCCURED "+entry.getValue()+" TIMES").getBytes());
						fos.write(System.getProperty("line.separator").getBytes());
			        }
					fos.write(System.getProperty("line.separator").getBytes());
					fos.write(System.getProperty("line.separator").getBytes());
				}
			
			/*writer.close();*/
		}
		 catch (IOException e) {

				e.printStackTrace();

			} finally {
				this.map.clear();
				try {

					if (br != null)
						br.close();

					if (fr != null)
						fr.close();

				} catch (IOException ex) {

					ex.printStackTrace();

				}

			}

		}
}

			
			
		
	
	

