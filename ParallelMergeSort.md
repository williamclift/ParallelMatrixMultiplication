William Clift
Data Structures
April 2020

<u>**Parallel Merge Sort Assignment**</u>

*Concepts*
	The given code allowed us to perform a merge-sort algorithm on an array set of integers recursively. The task was then to parallize this algorithm using  `extends RecursiveAction`. We introduced the `ForkJoin` framework so that each time we create a new `MergeSort` object, we could solve the subproblem of each half of the array. Once the array is split down to bits of two, we return and then call `merge()` which sorts by insertion from the two sublists to the big list. This concludes when the entire list is considered in the last recursive return, and the full list is sorted. We take advantage of multiple cores on any given machine by splitting into a two new processes every time we do the recursive step. Let us now examine the implementaton of its parallelization. 

*Implementation*
	The method `compute()` was added to the given code first to override its method in `RecursiveAction`. Then when we start the merge sort, we call `compute()` instead of `mergeSort()` for every new object that is forked in a new process. If it is not multi-threaded, we continue classically with the recursion, else we create two new subprocesses and then use `ForkJoinTask.invokeAll()` to fork the the process into two new ones. I also have the program keeping track of how many cores on the machine are remaining so that we do not exceed that in number of threads, making many threads that do small amounts of work. Once we have split up the tasks in all cores of the machine, we continue with classic `mergeSort`. Finally, they merge and the array comes back sorted.

*The Addition of Generics* 
	For an additional challenge, I introduced generics to the algorithm so that this could be used for any set of data. This required using `<T extends Comparable<? super T>>` and ensuring that each time that we work with the datastructure, we are able to handle any type that it contains. This made the program more complicated because each reference to any datastructure or variable with this type needed to be replace with `T` that would stand in for the type given at runtime. But, given any primitive datatype that extends comparable, we will be able to sort it. 

*Empiricle Analysis*
	As we know from our studies, the merge-sorting algorithm has a runtime of $N Log(N)$ without parallelization. We should see this resulting in our test data. But how does the parallelized version do? I performed my Empirical analayis on a $4$ core machine and the following graph represents the results. The test ran every 100,000 number of integers by randomly generating them in `RandomNumGenerate`, transfering these to a file, and then reading the file into the program and reading the time output. The time, of course, started recording once the merge sort algorithm started running.

​				**Single, Multi-Threaded, and Difference (Runtime vs. Number of Integers)**![Screen Shot 2020-04-14 at 7.51.28 PM](/Users/willclift/Library/Application Support/typora-user-images/Screen Shot 2020-04-14 at 7.51.28 PM.png)		

​	As can be seen above, the blue data-points refer to the single-threaded algorithm, the green is multi-threaded, and the orange is the difference. This shows a speedup going form single to multi-threaded, which generally expands as the number of integers increase. This is added by the fact the the difference (Single Thread - Multiple Threads) is majorly positive values. Let us now observe an analysis of regresion on the datasets.

​								**Trendline Comparison (Runtime vs. Number of Integers)![Screen Shot 2020-04-14 at 7.50.09 PM](/Users/willclift/Library/Application Support/typora-user-images/Screen Shot 2020-04-14 at 7.50.09 PM.png)**

​	Adding the regresssion plots gives us these models. We can see that by using a logarithmic regression, the graphs follow this nicely. These lines are functional  The following equations represent their functions.

For the Single-threaded, labeled in blue:
	$y=-1.117+ -9.488\cdot 10^{-7}(x)+1.007\cdot 10^{-1}(log(x))+1.111\cdot 10^{-7}(x~log(x))$

For the Multi-threaded, labeled in green:
	$y=-1.592\cdot 10-1.799\cdot 10^{-5}(x)+1.371\cdot(log(x))+1.137\cdot 10^{-6}(x~ log(x))$

For the Difference, labeled in orange:
	$y=1.480\cdot 10+1.705\cdot 10^{-5}(x)-1.271(log(x))-1.026\cdot 10^{-6}(x ~log(x))$

​	Clearly, these regression lines properly serve the algorithms runtime as it increases. By this, we can conclude the runtime for our algorithm must be $O(N Log(N))$ for the single threaded. For the multi-threaded we take into account the number of cores in the machine; and as we expected, we see a speedup. However, our trend is still $O(Nlog(N))$. We would see a bigger increase in performance with a greater number of cores. 

*Conclusion*
	In this programming exercise, we were able to see the parallelization of a merge-sorting algorithm that led to a speedup in the runtime when looking at the trend in our analysis. We also got practice using generics in the Java `Comparable` framework as well as `forkJoin`. Getting the chance to thoroughly investigate an elegant sorting algorithm that uses recursive techniques to increase efficiency gives us insight into the problem of sorting. 

