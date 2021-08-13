


import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class WordPacking {
	public static String getAuthorName() {
		return "Abdulaziz, George";
	}
	
	public static String getStudentID() {
		return "XXXXXXX";
	}
	//1: backtracking search algorithm 
	//2: Recursive backtracking that gets called with the first one
	//3: method to select new variables to be tested or assigned 
	//4: heuristics that helps the method for selecting all the good values
		// first we choose the value that has the least remaining bins available 
		// second, if all "words" have the same degree of remaining bins then we
			//choose the value with the highest links or incompatibility with other
			//words 
	//5: forward checking, we look at the "words" that has only one bin available
		//then we terminate when we learn that it has no legal bind or we indicate
		//the need to create new bins
	//6: arc consistency,if a "word" loses the ability to fit in a bin then 
		//its neighbors need to be rechecked to see if the can still fit in other bins
	//7: we set the minimum amount of bins then we start to increase upon failure 
	
	//when you are filling the array and lets say the very last element has no possible
	//values then you back jump to the element that it causing the problem 
	

	
	
	//iterative deepening: first you find the minimum number of bins needed to fit the words, you
	//find the most occurred letter in the same position with all the letters and that tells you
	//the minimum number of letters you need 
	
	
	//this is the a hash map of all the words and an array of their linked words
	private static Map<String, ArrayList<String>> domain = new HashMap<String, ArrayList<String>>();
	private static int numberOfBins = 0;
	public static void setNumOfBins(int number) {
		numberOfBins = number;
	}
	
	public static void clear() {
		domain = new HashMap<String, ArrayList<String>>();
		setNumOfBins(0);
		maxLetterOccurence = new HashMap<String, Integer>();
	}
	public static final long timeAllowed = 5000;
	
	
	private static Map<String, Integer> maxLetterOccurence = new HashMap<String, Integer>();
	
	public static HashMap<String, ArrayList<Integer>> copyHashMap(HashMap<String, ArrayList<Integer>> original)
		{
		HashMap<String, ArrayList<Integer>> copy = new HashMap<String, ArrayList<Integer>>();
		    for (Map.Entry<String, ArrayList<Integer>> entry : original.entrySet())
		    {
		        copy.put(entry.getKey(), new ArrayList<Integer>(entry.getValue()));
		    }
		    return copy;
		}
	
	public static boolean performBinsRemoving(String word, HashMap<String, ArrayList<Integer>> cAssinment, Integer binIndex, Long startTime) {
		Long endTime = System.currentTimeMillis();
		if((endTime-startTime)>timeAllowed) {
			//System.out.println("time consumed %%perform%%"+(endTime-startTime));
			return false;
		}
		cAssinment.remove(word);
		ArrayList<String> neighbors = domain.get(word);
		for(int i = 0; i < neighbors.size(); i++) {
			String neighbor = neighbors.get(i);
			if (cAssinment.containsKey(neighbor)){
				if(cAssinment.get(neighbor).get(binIndex)==1) {
					//if it is already zero then no need to decrease the number of available bins
					cAssinment.get(neighbor).set(0, cAssinment.get(neighbor).get(0) - 1);
					cAssinment.get(neighbor).set(binIndex, 0);
					if(cAssinment.get(neighbor).get(0)==0) {
//						System.out.print("terminated");
						return false;
					}
				}
			}	
		}
		return neighboresAreOkay(word, cAssinment, startTime);
	}

	
	public static boolean neighboresAreOkay(String word, HashMap<String, ArrayList<Integer>> assignments, Long startTime) {
		Long endTime = System.currentTimeMillis();
		if((endTime-startTime)>timeAllowed) {
			//System.out.println("time consumed %%neighborCheck%%"+(endTime-startTime));
			return false;
		}
		ArrayList<String> neighbors = domain.get(word);
		for(int i = 0; i < neighbors.size(); i++) {
			String neighbor = neighbors.get(i);
			if(assignments.containsKey(neighbor)){ 
				if(assignments.get(neighbor).get(0)==0) { //if the neighbor is still not assigned and it has no bins available then we return false
					return false;
				}
				if(assignments.get(neighbor).get(0)==1) { //if the neighbor is still not assigned and it has no bins available then we return false
					for(int c=1; c<assignments.get(neighbor).size();c++) {
						if(assignments.get(neighbor).get(c)==1) { //the index of the available bin
							HashMap<String, ArrayList<Integer>> ccAssignments = copyHashMap(assignments);
							if(!performBinsRemoving(neighbor, ccAssignments, c, startTime)) {
								//System.out.print("terminated");
								return false;
							}
						}
					}
				}
			}
		}
		return true;
	}
	
	public static String selectWord(List<String> words, HashMap<String, ArrayList<Integer>> assignments, List<List<String>> bins) {
		//we have to select the word with the fewest bins available left
		//if all the words have equal amount of bins then we choose the one that has the highest links with other words
	
		int maxLinks = 0;
		String bestWord = "";
		for(Map.Entry<String, ArrayList<Integer>> entry : assignments.entrySet()) {
			String word = entry.getKey();
			if(domain.get(word).size()>maxLinks) {
				maxLinks = domain.get(word).size();
				bestWord = word;
			}
		}
		return bestWord;
	}
	
	public static List<List<String>> recursiveWordPack(List<String> words, HashMap<String, ArrayList<Integer>> assignments, List<List<String>> bins, Long startTime){
		Long endTime = System.currentTimeMillis();
		if((endTime-startTime)>timeAllowed) {
			//System.out.println("time consumed %%%%"+(endTime-startTime));
			return null;
		}
		if(assignments.size()==0) { //if we used all the words then we stop 
			return bins;
		}
		String word = selectWord(words, assignments, bins);
		for(int binIndex = 1 ; binIndex < assignments.get(word).size(); binIndex++) {
			Long endTime1 = System.currentTimeMillis();
			if((endTime1-startTime)>timeAllowed) {
				return null;
			}
			if(assignments.get(word).get(binIndex)==0) {
				continue;
			}
				
			bins.get(binIndex-1).add(word); //we add the word to the bin if the bin contains no neighbor
			HashMap<String, ArrayList<Integer>> cAssinment = copyHashMap(assignments);
			if(performBinsRemoving(word, cAssinment, binIndex, startTime)){
				List<List<String>> result = recursiveWordPack(words, cAssinment, bins, startTime);	
				if(result!=null) {
					return result;
				}
			}
			bins.get(binIndex-1).remove(word);
			
		}
		

		return null;

	}
	
	
	public static List<List<String>> wordPack(List<String> words){
		long startTime = System.currentTimeMillis(); //if the time gets more than 9000 ms which is 9 sec then cut
		clear();
		domain = fillDomain(words); //the domain of all the words with their links
		int addingBins = 1;
		List<List<String>> result = wordPackIntermediate(words, numberOfBins, startTime);
		while(result==null) {
			//System.out.println(" **adding bins***"+addingBins);
			long startTime1 = System.currentTimeMillis();
			result = wordPackIntermediate(words, numberOfBins+addingBins, startTime1);
			addingBins += 1;
			
		}

		return result;
	}
	
	public static List<List<String>> wordPackIntermediate(List<String> words, int numOfBins, long startTime){
		 
		
		//the estimation for the number of bins needed is maybe the number of words divided by two initially
		List<List<String>> bins = new ArrayList<List<String>>();
		for(int i=0; i<(numOfBins); i++) {//TODO
			bins.add(new ArrayList());
		}
		
		//if the word is assigned then it is already in a bin
		//we need to include the bin that each word was assigned to and the neighbor will look at all its other neighbors and see
		//if it would still have bins available 
		//when we assign a word we should remove the bins available
		
		//initially all the words have all the available bins and when you assign a word you remove it from assignment and remove all the bins from its neigbores 
		//I will make the word have the first element as a counter to indicated the number of available bins left
		// and the rest are elements of 0 and 1 where 0 means is not available and 1 means it is still 
		//and the index of the 0 and 1 points to the bin
		
		HashMap<String, ArrayList<Integer>> assignments = new HashMap<String, ArrayList<Integer>>();
		for(int k = 0 ; k < words.size(); k++) {
			String word = words.get(k);
			assignments.put(word , new ArrayList<Integer>(Collections.nCopies(numOfBins+1, 1)));
			assignments.get(word).set(0, numOfBins); //making the first element indicate the number of available bins
		}
		//note: bins in the assignments are not deep copied, hence when you edit the assignments you edit the bins 
		return recursiveWordPack(words, assignments, bins, startTime);
	}
	
	public static HashMap<String, ArrayList<String>> fillDomain(List<String> words){
		HashMap<String, ArrayList<String>> result = new HashMap<String, ArrayList<String>>();
		for(int i = 0; i< words.size(); i++) {
			String word = words.get(i);
			result.put(word, new ArrayList());
			
			for(int j=1; j< words.size(); j++) {
				String possibleNeighbor = words.get((i+j) % words.size());
				if(neighbors(word, possibleNeighbor)) {
					result.get(word).add(possibleNeighbor);
				}
			}
		}
		//setting the number of bins
		//System.out.println(maxLetterOccurence);
		int maxBinsNeeded= 0;
		for(Map.Entry<String, Integer> entry: maxLetterOccurence.entrySet()) {
			if(entry.getValue()>maxBinsNeeded) {
				maxBinsNeeded = entry.getValue();
			}
		}
		setNumOfBins(maxBinsNeeded+1); //we add 1 because we need to consider the word as well
		//System.out.println(numberOfBins);
		
		return result;
	}
	
	public static boolean neighbors(String word1,String word2) {
		for(var i = 0 ; i< word1.length(); i++) {
			String letter1 = word1.substring(i, i+1);
			String letter2 = word2.substring(i, i+1);
			if(letter1.equals(letter2)) {
				if(maxLetterOccurence.containsKey(word1+letter1+String.valueOf(i))){
					maxLetterOccurence.replace(word1+letter1+String.valueOf(i), maxLetterOccurence.get(word1+letter1+String.valueOf(i))+1);
					//we need to divide the maximum by 2 since it is commutative word1<>worde2
				}
				else {
					maxLetterOccurence.put(word1+letter1+String.valueOf(i), 1);
				}
				
				return true;
			}
		}
		return false;
	}
	
}




