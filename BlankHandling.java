import java.util.*;
import java.lang.*;
import java.io.*;

import java.util.List;
import java.util.ArrayList;

class BlankHandling {

public static List<String> addBlank(List<String> formedWordsList, String word, int spaceCount){
		
		if(spaceCount== 0){
			formedWordsList.add(word);
		}
		else
		{
			int index = 0;
			for(char i= 'a'; i <= 'z'; i++){
				if(index < word.length()){
					if(word.charAt(index) <= i){
						index++;
					}
				}
				String toAdd = word.substring(0, index) + i + word.substring(index, word.length());
				formedWordsList = addBlank(formedWordsList, toAdd, spaceCount-1);
			}
		}
		
		return formedWordsList;
	}
	
	public static void main(String[] args) {
		String word = "cnt";
		
		List<String> formedWordsList = new ArrayList<String>();
		formedWordsList = addBlank(formedWordsList, word, 2);
		
		System.out.println(formedWordsList);
	}

}
