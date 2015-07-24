import java.io.*;
import java.math.*;
import java.util.*;

class scrabble_word_gen {
	static int NUMBER_OF_TILES = 7;
	static int TOP_N = 5;
	static List<String> all_characters = new ArrayList<String>();
	static List<String> possible_characters = new ArrayList<String>(); 
	
	public static void main(String[] args) {
		try {
			Word_generator word_generator = new Word_generator(args[1], args[2]);			
			SortedMap<Integer, String> possible_words = new TreeMap<Integer, String>(Collections.reverseOrder());
			possible_words = word_generator.get_words(args[0], args[0].length());
			for (int score : possible_words.keySet()) {
				System.out.println(score + " " +possible_words.get(score));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

class Word_generator {
	private String each_character_score_file_path; 
	private String dictionary_path;
	private static String SEPARATOR = " ";
	
	
	Word_generator(String score_file_path, String scrabble_dictionary_path) {
		this.each_character_score_file_path = score_file_path;
		this.dictionary_path = scrabble_dictionary_path; 
	}
	
	SortedMap<Integer, String> get_words(String input_string, int required_word_size) throws Exception {
		Scrabble scrabble_dictionary_checker = new Scrabble();
		scrabble_dictionary_checker.generate_anagrams(dictionary_path, each_character_score_file_path);
		SortedMap<Integer, String> words_and_score = new TreeMap<Integer, String>(Collections.reverseOrder());
		List<String> input_characters = new ArrayList<String>();
		for (char character : input_string.toCharArray()){
			input_characters.add("" + character);
		}
		boolean blank_tile_absent = true;
		if (input_characters.contains(" ")) {
			input_characters.remove(" ");
			blank_tile_absent = false;
		}
		Set<String> permutation_set = generate_permutations(input_characters);
		for (String word_permutation : permutation_set) {
			if (blank_tile_absent) {
				if (scrabble_dictionary_checker.is_valid(word_permutation) && word_permutation.length() <= required_word_size) {
					String[] score_with_word = scrabble_dictionary_checker.get_anagrams(word_permutation).split(" : ");
					int score = Integer.parseInt(score_with_word[0]);
					if (!words_and_score.containsKey(score)) {
						words_and_score.put(score, score_with_word[1]);
					} else {
						String words_in_map = words_and_score.get(score);
						words_and_score.put(score, words_in_map + SEPARATOR + score_with_word[1]);
					}
				}
			} else {
				Set<String> words_without_blank = fill_blank(word_permutation);
				for (String word_with_blank_filled : words_without_blank) {
					if (scrabble_dictionary_checker.is_valid(word_with_blank_filled) && word_with_blank_filled.length() <= required_word_size) {
						String[] score_with_word = scrabble_dictionary_checker.get_anagrams(word_with_blank_filled).split(" : ");
						words_and_score.put(Integer.parseInt(score_with_word[0]), score_with_word[1]);
					}
				}
			}
		}
		return words_and_score;
	}
	
	Set<String> generate_permutations(List<String> players_characters) {
		Collections.sort(players_characters);
		Set<String> possible_permutations = new HashSet<String>();
		for (int i = 0; i < players_characters.size() - 1; i++) {
			String current_prefix = "";
			for(int j = i; j < players_characters.size(); j++) {
				current_prefix = current_prefix + players_characters.get(j);
				for(int k = j + 1; k < players_characters.size(); k++) {
					String current_key = current_prefix + players_characters.get(k);
					possible_permutations.add(current_key);
				}
			}
		}
		return possible_permutations;
	}
	
	Set<String> fill_blank(String word) {		
		Set<String> wordsFormed = new HashSet<String>();
		int index = 0;
		for (char i= 'A'; i <= 'Z'; i++) {
			if (index < word.length()) {
				if (word.charAt(index) <= i) {
					index++;
				}
			}
			String toAdd = word.substring(0, index) + i + word.substring(index, word.length());
			wordsFormed.add(toAdd);
		}
		return wordsFormed;
	}
}

class Scrabble {
	private static String SEPARATOR = " ";
	private static Map<String, String> words_map;
	
	private String sortWord(String unsortedWord) {
		char[] arr = unsortedWord.toCharArray();
		Arrays.sort(arr);
		return String.valueOf(arr);
	}

	public void generate_anagrams(String dictionary_file_path, String alphabets_score_file_path) throws Exception {
		BufferedReader word_reader = new BufferedReader(new FileReader(dictionary_file_path));
		String sCurrentLine = "";
		this.words_map = new HashMap<String, String>();
		Score_sort score_generator = new Score_sort(alphabets_score_file_path);
		while ((sCurrentLine = word_reader.readLine()) != null) {
			String sorted_word = sortWord(sCurrentLine);
			String words = "";
			if (this.words_map.containsKey(sorted_word)) {
				words = this.words_map.get(sorted_word) + SEPARATOR + words;
			} else {
				words = score_generator.calculate_score(sorted_word)+ " : " +sCurrentLine;
			}
			this.words_map.put(sorted_word, words);
		}
		word_reader.close();
	}
	
	public String get_anagrams(String key) {
		return this.words_map.get(key);
	}
	
	public boolean is_valid(String possible_word) {
		return this.words_map.containsKey(sortWord(possible_word));
	}	
}

class Score_sort {
	Map<Character, Integer> scores;
	static String SEPARATOR = " ";
	
	Score_sort(String score_path) {
		try {
			this.scores = new HashMap<Character, Integer>();
			BufferedReader score_reader = new BufferedReader(new FileReader(score_path));
			String sCurrentLine = "";
			while ((sCurrentLine = score_reader.readLine()) != null) {
				String[] word_score = sCurrentLine.split(SEPARATOR);
				this.scores.put(word_score[0].charAt(0), Integer.parseInt(word_score[1]));
			}
			score_reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	int calculate_score(String word) {
		char[] chars_in_word = word.toUpperCase().toCharArray();
		int score = 0;
		for (char current_character : chars_in_word) {
			score += scores.get(current_character);
		}
		return score;
	}
}