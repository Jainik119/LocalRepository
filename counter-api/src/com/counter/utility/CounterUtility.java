package com.counter.utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class CounterUtility {

	private static final int BUFFER_SIZE = 4096;
	@SuppressWarnings("unchecked")
	public String getWordInfoJson(String inputSearch,HttpServletResponse response, HttpServletRequest request) throws IOException {
		JSONObject obj = new JSONObject();
		JSONArray jaArray =new JSONArray();
		ServletContext sc = request.getServletContext();
		String path = sc.getRealPath("/WEB-INF/data.txt"); 
		int count = 0;
		//String filePath = "C:\\Users\\D'Pain\\Desktop\\data.txt";
		BufferedReader br;
		String line = "";
		if (!inputSearch.isEmpty()) {
			String inputFinal[] = inputSearch.split("[^a-zA-Z0-9]");
			for (int i = 2; i < inputFinal.length; i++) {
				String data = inputFinal[i];
				count = 0;
				try {
					br = new BufferedReader(new FileReader(path));
					try {
						while ((line = br.readLine()) != null) {
							String[] words = line.split("[^a-zA-Z0-9]");
							for (String word : words) {
								if (word.equalsIgnoreCase(data)) {
									count++;
								}
							}
						}
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				if (!(data.equalsIgnoreCase("") || data.equalsIgnoreCase("searchText"))) {
					JSONObject o = new JSONObject();
					o.put(data, count);
					jaArray.add(o);
				}
			}
		}
		obj.put("counts", jaArray);
		
		return obj.toJSONString();
	}

	public void getWordbyCount(int number, HttpServletResponse response, HttpServletRequest request) throws IOException {
	
		 ServletContext sc = request.getServletContext();
		 String path = sc.getRealPath("/WEB-INF/data.txt");
		BufferedReader bufferedReader = null;
		bufferedReader = new BufferedReader(new FileReader(path));
		String inputLine = null;
		Map<String, Integer> map = new HashMap<>();
		try {
			while ((inputLine = bufferedReader.readLine()) != null) {
				String[] words = inputLine.split("[ \n\t\r.,;:!?(){}]");
				for (int counter = 0; counter < words.length; counter++) {
					String key = words[counter];
					if (key.length() > 0) {
						if (map.get(key) == null) {
							map.put(key, 1);
						} else {
							int value = map.get(key).intValue();
							value++;
							map.put(key, value);
						}
					}
				}
			}
			
			List<String> myTopOccurrence = crunchifyFindMaxOccurance(map, number);
			String datafile = sc.getRealPath("/WEB-INF/data.csv");
			FileWriter writer = new FileWriter(datafile);
			for (String result : myTopOccurrence) {
				writer.append(result+"\n");	
			}
			writer.flush();
		    writer.close();
		    downloadFile(request, response);
		}

		catch (IOException error) {
			error.printStackTrace();
		} finally {
			bufferedReader.close();
		}
	}

	public static List<String> crunchifyFindMaxOccurance(Map<String, Integer> map, int n) {
		List<CountComparable> l = new ArrayList<>();
		for (Map.Entry<String, Integer> entry : map.entrySet())
			l.add(new CountComparable(entry.getKey(), entry.getValue()));

		Collections.sort(l);
		List<String> list = new ArrayList<>();
		for (CountComparable w : l.subList(0, n))
			list.add(w.wordFromFile + "|" + w.numberOfOccurrence);
		return list;
	}
	
	@SuppressWarnings("unused")
	public void downloadFile(HttpServletRequest request,HttpServletResponse response) throws IOException{
		
		ServletContext context = request.getServletContext();
		String appPath = context.getRealPath("/WEB-INF/data.csv");
		//String fullPath = "C:\\Users\\D'Pain\\Desktop\\Data.csv";
		File downloadFile = new File(appPath);
		FileInputStream inputStream = new FileInputStream(downloadFile);

		// get MIME type of the file
		String mimeType = context.getMimeType(appPath);
		if (mimeType == null) {
			// set to binary type if MIME mapping not found
			mimeType = "application/octet-stream";
		}
		// set content attributes for the response
		response.setContentType(mimeType);
		response.setContentLength((int) downloadFile.length());

		// set headers for the response
		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
		response.setHeader(headerKey, headerValue);

		// get output stream of the response
		OutputStream outStream = response.getOutputStream();

		byte[] buffer = new byte[BUFFER_SIZE];
		int bytesRead = -1;

		// write bytes read from the input stream into the output stream
		while ((bytesRead = inputStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, bytesRead);
		}
		inputStream.close();
		outStream.close();
	}
}
	