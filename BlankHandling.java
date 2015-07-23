package first;

import java.util.List;
import java.util.ArrayList;

public class BlankHandling {

	public static List<String> addBlank(String word){
		
		List<String> wordsFormed = new ArrayList<String>();
		
		int index = 0;
		for(char i= 'a'; i <= 'z'; i++){
			if(index < word.length()){
				if(word.charAt(index) <= i){
					index++;
				}
			}
			String toAdd = word.substring(0, index) + i + word.substring(index, word.length());
			//System.out.println("toPut: " + toPut);
			wordsFormed.add(toAdd);
		}
		return wordsFormed;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String word = "adgmvz";
		List<String> formedWordsList = addBlank(word);
		System.out.println(formedWordsList);
	}

}
