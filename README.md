This repo is made to track and show my approach on learning the fundamentals of parallel, concurrent, and 
distributed programming. I started with the specialization on Coursera taught by [Vivek Sarkar](https://www.linkedin.com/in/vsarkar)
named [Parallel, Concurrent, and Distributed Programming in Java Specialization](https://www.coursera.org/specializations/pcdp).

### Skills Gained:
- Computer Programming
- Algorithms
- Theoretical Computer Science
- Critical Thinking
- Distributed Computing Architecture
- Programming Principles

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
    ├── ParallelProgrammingInJavaWorkingNotes.pdf 
    ├── ParallelProgrammingInJava
    │   ├── README.md
    │   ├── ReciprocalArraySumUsingJavaForkJoinFramework
    │   ├── AnalyzingStudentStatisticsUsingJavaParallelStreams
    │   ├── ParallelizingMatrixMatrixMultiplyUsingLoopParallelism
    │   ├── UsingPhasersToOptimizeDataParallelApplications
    ├── ConcurrentProgramminginJava
    │   ├── README.md
    │   ├── LockingandSynchronization
    │   ├── GlobalAndObjectBasedIsolation
    │   ├── SieveofEratosthenesUsingActorParallelism
    │   ├── ParallelizationOfBoruvkaMinimumSpanningTreeAlgorithm

    ```

## Troubleshooting:

- If you're getting the below errors while cloning the repository, run the mentioned command:
  ```bash
  ## fatal: the remote end hung up unexpectedly
  ## error: RPC failed; HTTP 400 curl 22 The requested URL returned error: 400
  ## send-pack: unexpected disconnect while reading sideband packet

  git config --global http.postBuffer 157286400
  ```

### Working Notes:

- Offline: [ParallelProgrammingInJavaWorkingNotes.pdf](../ParallelConcurrentDistributedProgramming.pdf)
- Online: [Link](https://acrobat.adobe.com/id/urn:aaid:sc:AP:9eeac7d2-f417-4135-bada-d8044c4e416e)

### Reach-out:

If you need any help while going through the repository, feel free to ping me on LinkedIn [Pushp Sood](https://www.linkedin.com/in/pushpsood/)