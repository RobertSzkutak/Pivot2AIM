package com.publicwork.pivot2aim;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class Main 
{
	public static void main(String[] args) 
	{
		final String directory = "W:/Public/PivotTest";
		
		File[] theFiles = new File(directory).listFiles();
		
		if(theFiles.length <= 0)
			return;
		
		for (File aFile : theFiles)
		{
			if(aFile.getAbsolutePath().substring(aFile.getAbsolutePath().length()-4, aFile.getAbsolutePath().length()).compareTo(".csv") == 0)
			{
				String filename = aFile.getAbsolutePath();
					
				//////////////////////////////////////////
				//   Build and populate data structures //
				//////////////////////////////////////////
				
				//////////////////////////////////////////
				//         Build list of groups         //
				//////////////////////////////////////////
				List <String> groupNames = new ArrayList<String>();
				
				BufferedReader reader = null;
				try 
				{
					reader = new BufferedReader(new FileReader(filename));
					String line = reader.readLine();
					
					String group = null;
					
					while ((line = reader.readLine()) != null) 
					{
						//Grab group
						group = line.substring(0, line.indexOf(","));
						
						//Insert into list of groups
						if(groupNames.contains(group) == false)
							groupNames.add(group);
					}
				} 
				catch (FileNotFoundException e) 
				{
					e.printStackTrace();
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
				
				////////////////////////////////////////////
				// Place handles into correct group lists //
				////////////////////////////////////////////
				reader = null;
				int num = groupNames.size();
				List <String> groups [] = new ArrayList[num];
				for(int i = 0; i < num; i++)
				{
					groups[i] = new ArrayList<String>(); 
				}
				try 
				{
					reader = new BufferedReader(new FileReader(filename));
					String line = reader.readLine();
					
					String group = null;
					String handle = null;
					
					while ((line = reader.readLine()) != null) 
					{
						//Grab group
						group = line.substring(0, line.indexOf(","));
						
						//Grab handle
						int blah = 0;
						for(int i = 0; i < 4; i++)
							blah = line.indexOf(",", blah)+1;
						handle = line.substring(blah, line.indexOf(",", blah));
						
						//Place handle in correct group list
						groups[groupNames.indexOf(group)].add(handle);
					}
				} 
				catch (FileNotFoundException e) 
				{
					e.printStackTrace();
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
				
				///////////////////////////////////////////////
				//               Create BLT                  //
				///////////////////////////////////////////////
				
				String output = null;
				output = "Config { Version 1 } User { screenName dummyAIMScreenName } Buddy { list {";
				
				for(int i = 0; i < groupNames.size(); i++)
				{
					output += " \"" + groupNames.get(i) + "\"" + "{ ";
					for(int j = 0; j < groups[i].size(); j++)
					{
						output += " " + groups[i].get(j);
					}
					output += " }";
				}
				
				output += "} }";
				
				//////////////////////////////////////////
				//               OUTPUT BLT             //
				//////////////////////////////////////////
				
				//System.out.println(output); //DEBUG
				
				PrintWriter writer;
				try 
				{
					writer = new PrintWriter(filename.substring(0, filename.length()-4) + ".blt", "UTF-8");
					writer.println(output);
					writer.close();
				} catch (FileNotFoundException e) 
				{
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) 
				{
					e.printStackTrace();
				}
			}
		}
	}
}
