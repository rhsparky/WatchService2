package com.yash.workpay;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class WatchWorkPay {

	
	public static void main(String[] args)
	{
		try(WatchService service = FileSystems.getDefault().newWatchService())
		{
			Map<WatchKey, Path> keyMap = new HashMap<>();
			Path path = Paths.get("C:\\Users\\rhspa\\OneDrive\\Documents\\work");
			keyMap.put(path.register(service, 
					StandardWatchEventKinds.ENTRY_MODIFY), 
					path);
			WatchKey watchKey;
			
			do {
				
				watchKey = service.take();
				
				Path eventDir = keyMap.get(watchKey);
				
				for(WatchEvent<?> event: watchKey.pollEvents())
				{
					WatchEvent.Kind<?> kind = event.kind();
					Path eventPath = (Path)event.context();
					
					String stKind = kind.toString();
					String stEvent = eventPath.toString();
					if(stKind.equals("ENTRY_MODIFY") && stEvent.equals("WorkPay.xlsx"))
					{
						File application = new File("C:\\Users\\rhspa\\OneDrive\\Documents\\work\\workpay.kjb");
				        String applicationName = application.getName();

				        if (!isProcessRunning(applicationName))
				        {
				        	Runtime rt =  Runtime.getRuntime(); 
							rt.exec("cmd /c start C:\\Users\\rhspa\\OneDrive\\Documents\\work\\workpay.bat");
				            
				        }
						System.out.println(eventDir + ": " + kind +": " + eventPath);
						
					}
				}
			} while (watchKey.reset());
			
		}catch(Exception ex)
		{
			ex.printStackTrace();
			
		}
	}
	
	 private static boolean isProcessRunning(String processName) throws IOException, InterruptedException
	    {
	        ProcessBuilder processBuilder = new ProcessBuilder("tasklist.exe");
	        Process process = processBuilder.start();
	        String tasksList = toString(process.getInputStream());
	        
	        return tasksList.contains(processName);
	    }
	 
	 private static String toString(InputStream inputStream)
	    {
	        Scanner scanner = new Scanner(inputStream, "UTF-8").useDelimiter("\\A");
	        String string = scanner.hasNext() ? scanner.next() : "";
	        scanner.close();

	        return string;
	    }
}
