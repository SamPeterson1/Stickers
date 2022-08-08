package com.github.sampeterson1.renderEngine.loaders;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class ResourceLoader {
	
	public static InputStream getResourceStream(String name) throws FileNotFoundException {
		InputStream resource = ResourceLoader.class.getResourceAsStream("/" + name);
		if(resource == null) 
			throw new FileNotFoundException();
		
		return resource;
	}
	
}
