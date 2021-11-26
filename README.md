# CSP ([Constraint Satisfaction Problem](https://en.wikipedia.org/wiki/Constraint_satisfaction_problem))

During my Artificial Intelligence course, my professor assigned us with this problem to do. Using combinatorial search and the techniques of [arc consistency](https://en.wikipedia.org/wiki/Local_consistency#Arc_consistency) and [forward checking](https://en.wikipedia.org/wiki/Look-ahead_(backtracking)), students must fit a given list of words into the minimum number of bins where no two words with a **link** between them must go in the same bin. The definition of **link** here is the existence of any letter for two words at the same position, for example: "share" and "toast" both have the letter "a" in the third position which make them linked, thus, the two words can not fit into one bin. 


## Input and Output

The Class WordPacking have the function **wordPack** that takes a list of words and returns list of lists that represents the bins and the words inside it. The function estimates the **minimum number of bins** at least needed and starts to increase the number of bins if the previous number did not work.


## About My Approach to Solve the Problem

-Filling the domain: the domain is a hash map of <key: word, value: the domain of the word> where the domain of the word contains all the words that have a link with the word.
-Then the intermediate word pack function gets all the parameters: the number of bins, start time, and the list of words. It then creates a hash map of all the words with their available bins as a list of 1s and 0s where 1 indicates the bins that are still open and 0 the opposite, and the first position in the list indicates the number of available bins (this is going to be very useful when we check if the words will still have available bins left when we take any action in the future). The function also creates the list of lists that represents the bins and passes its parameters and the newly created objects to a recursive function called recuresiveWordPack.
-The recursive function then takes one word each time and applies the arc consistency and forward checking on each recursive call to eliminate branching and backtracking when one word is placed poorly in a bin that eliminates the possibility of fitting the rest of the words in the given number of bins.
