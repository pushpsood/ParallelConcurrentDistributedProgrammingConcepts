Please refer to the top-level [`README.md`](../README.md) file located at the root of the repository first to understand
the overall structure and approach of this repository. Once you have gone through the top-level `README.md`, 
you can start reading the details below.

Distributed programming enables developers to use multiple nodes in a data center to increase throughput and/or 
reduce latency of selected applications.  I used popular distributed programming frameworks for Java programs, 
including Hadoop, Spark, Sockets, Remote Method Invocation (RMI), Multicast, Sockets, Kafka, Message Passing Interface
(MPI), as well as different approaches to combine distribution with multithreading.

### Skills Gained:
- Apache Kafka
- Software Architecture
- Algorithms
- Apache Hadoop
- Servers
- Java
- Apache Spark
- Programming Principles
- Systems Architecture
- Distributed Computing
- Network Protocols

### The desired learning outcomes of this course are as follows:

- Distributed map-reduce programming in Java using the Hadoop and Spark frameworks
- Client-server programming using Java's Socket and Remote Method Invocation (RMI) interfaces
- Message-passing programming in Java using the Message Passing Interface (MPI)
- Approaches to combine distribution with multithreading, including processes and threads, distributed actors, and reactive programming

### Certification: 

https://www.coursera.org/account/accomplishments/certificate/VZGUUS34J1FY

### Bugs:


#### 1. Error running Spark on specific Java versions

When running Spark on Java 11+ versions, you may encounter the following error:


``
    java.lang.IllegalAccessError: class org.apache.spark.storage.StorageUtils$ (in unnamed module @0x3f761059) cannot
     access class sun.nio.ch.DirectBuffer (in module java.base) because module java.base does not export sun.nio.ch to 
     unnamed module @0x3f761059
``

**Fix: Use Java 11 or Java 8 to run the maven project**