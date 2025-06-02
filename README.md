Track of my approach on learning the fundamentals of parallel, concurrent, and 
distributed programming. I started with the specialization on Coursera taught by [Vivek Sarkar](https://www.linkedin.com/in/vsarkar)
named [Parallel, Concurrent, and Distributed Programming in Java Specialization](https://www.coursera.org/specializations/pcdp) and build Maven 
projects in this repo to showcase key learning outcomes through practical implementation.

![ScreenShot](https://github.com/pushpsood/ParallelConcurrentDistributedProgrammingConcepts/blob/main/Media/project.jpg?raw=true)

### Skills Gained:
- Java Programming
- Computer Science
- Apache Spark
- Servers
- Scalability
- Functional Design
- Performance Tuning
- Data Structures
- Java
- Distributed Computing
- Algorithms
- Network Protocols

### Important Resources:
- [Java 8 Javadocs](https://docs.oracle.com/javase/8/docs/api/)
- [PCDP Javadocs](https://habanero-rice.github.io/PCDP/)
- [PCDP Source Code](https://github.com/habanero-rice/PCDP)
- [RecursiveAction](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/RecursiveAction.html)
- [RecursiveTask](http://docs.oracle.com/javase/8/docs/api/?java/util/concurrent/RecursiveTask.html)
- [ForkJoinPool](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ForkJoinPool.html)
- [Java Streams Javadocs](https://docs.oracle.com/javase/8/docs/api/java/util/stream/package-summary.html)
- [The Java Stream class](https://docs.oracle.com/javase/8/docs/api/java/util/stream/Stream.html)
- [A simple Java tutorial on streams](http://winterbe.com/posts/2014/07/31/java8-stream-tutorial-examples/)
- [A simple Java ForkJoin tutorial](https://docs.oracle.com/javase/tutorial/essential/concurrency/forkjoin.html)
- [Using the JDK's performance profiler](http://docs.oracle.com/javase/7/docs/technotes/guides/visualvm/)

### References:
- [Parallel, Concurrent, and Distributed Programming in Java Specialization](https://www.coursera.org/specializations/pcdp)

### How to use:

1. Clone the repository:
    ```bash 
    gh repo clone pushpsood/ParallelConcurrentDistributedProgrammingConcepts
    ```
2. Open the repository in your favorite IDE(preferably intelliJ) and start learning. 
3. This repository contains different modules that should be traversed in a specific order, module by module, listed below. 
Each module will have a pom.xml file. To make it a workable module, add them as a Maven module in the order listed below:
    ```bash
    ## Command to generate the tree:
    ## If required get the package(macOS): brew install tree
    ## tree -o readmeTree.md

    ├── README.md
    ├── Media
    │  ├── ParallelConcurrentDistributedProgramming.pdf 
    ├──ParallelProgrammingInJava
    │  ├── README.md
    │  ├── ReciprocalArraySumUsingJavaForkJoinFramework
    │  ├── AnalyzingStudentStatisticsUsingJavaParallelStreams
    │  ├── ParallelizingMatrixMatrixMultiplyUsingLoopParallelism
    │  ├── UsingPhasersToOptimizeDataParallelApplications
    ├── ConcurrentProgramminginJava
    │  ├── README.md
    │  ├── LockingandSynchronization
    │  ├── GlobalAndObjectBasedIsolation
    │  ├── SieveofEratosthenesUsingActorParallelism
    │  ├── ParallelizationOfBoruvkaMinimumSpanningTreeAlgorithm
    ├── DistributedProgramminginJava
    │  ├── README.md
    │  ├── FileServer
    │  ├── MatrixMultiplyInMPI
    └  └── PageRankWithSpark

    ```
4. You can test the projects by running the respective Test classes in each module. 
   - For example, to test the `ReciprocalArraySumUsingJavaForkJoinFramework` module, run the `ReciprocalArraySumTest.java` class.

## Troubleshooting:

- If you're getting the below errors while cloning the repository, run the mentioned command:
  ```bash
  ## fatal: the remote end hung up unexpectedly
  ## error: RPC failed; HTTP 400 curl 22 The requested URL returned error: 400
  ## send-pack: unexpected disconnect while reading sideband packet

  git config --global http.postBuffer 157286400
  ```

### Specialization: 

https://www.coursera.org/account/accomplishments/specialization/CJKSM1MQ59H1

### Working Notes:

- Offline: [ParallelConcurrentDistributedProgramming.pdf](Media/ParallelConcurrentDistributedProgramming.pdf)
- Online(Comments allowed here, use this for any rectification request): [Link](https://acrobat.adobe.com/id/urn:aaid:sc:AP:9eeac7d2-f417-4135-bada-d8044c4e416e) 

### Reach-out:

If you need any help while going through the repository, feel free to ping me on LinkedIn [Pushp Sood](https://www.linkedin.com/in/pushpsood/)