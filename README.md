# CPU-Scheduler

System for Deterministic Modeling of CPU Scheduling Algorithms:
1. Nonpreemptive First-Come, First-Served (FCFS) Scheduling
2. Nonpreemptive Shortest-Job-First (SJF) Scheduling
3. Preemptive SJF (Shortest-Remaining-Time-First) Scheduling
4. Nonpreemptive Priority Scheduling
5. Preemptive Priority Scheduling
6. Preemptive Round-Robin (RR) Scheduling
7. Multilevel Queue Scheduling
8. Multilevel Feedback Queue Scheduling

For Multilevel Queue scheduling (7), you may assume that there exist a total of two queues:
1. One foreground queue which is scheduled by RR scheduling;
2. One background queue which is scheduled by FCFS scheduling;
3. The foreground queue has absolute priority over the background queue.

For Multilevel Feedback Queue scheduling (8), you may assume that there exist a total of three queues:
1. Queue 0 which is scheduled by RR scheduling with a first given time quantum.
2. Queue 1 which is scheduled by RR scheduling with a second given timen quantum.
3. Queue 2 which is scheduled by FCFS scheduling.

===============================================================================

Each process P in the system can have the following attributes:
1. Arrival Time: the time when process P first makes a request to the CPU Scheduler to execute on the CPU. Each Arrival Time can be expressed as a nonnegative integer.
2. Sequence of CPU Computation Time and I/O Time Requirements which can each be expressed as a nonnegative integer (see example below).
3. Process Priority, expressed as a nonnegative number.
(Process Priority Numbers will only be used when Priority Scheduling is applied. Lower Process Priority Numbers correspond to higher process priorities.)

For example, for some process P:
Arrival_Time(P) = 8;
The Sequence of CPU Computation Time and I/O Time Requirements of process P could have 5 components as follows:
1. CPU_time(P) = 10; (P needs to execute 10 time units on the CPU at first)
2. I/O_time(P, 0) = 15; (then P needs to do I/O for 15 time units on I/O device 0)
3. CPU_time(P) = 16; (then P needs to execute 16 time units on the CPU)
4. I/O_time(P, 1) = 20; (then P needs to do I/O for 20 time units on I/O device 1)
5. CPU_time(P) = 8. (then P needs to execute 8 time units on the CPU before terminating)

Process_Priority(P) = 2.

Please note that the I/O time requirements only includes the time that is needed when a process P is actually doing I/O; it DOES NOT include the time that it may need to spend waiting in an I/O queue before the I/O device becomes available.

===============================================================================

At any point in time, each process P in the system can be in any one of the following states:
1. New: P has not arrived yet
2. Running: P has been selected by the CPU scheduler for execution on the CPU, and is currently executing on the CPU
3. Ready: P has already arrived, and P is currently waiting in the ready queue to be scheduled by the CPU Scheduler for execution on the CPU
4. Waiting for I/O: P had previously made an I/O request to the I/O scheduler in order to perform execution on the CPU, and P is either currently performing I/O on an I/O device or is currently waiting in an I/O queue so that it can be scheduled by the I/O Scheduler to perform I/O on an I/O device
5. Terminated: P has completed its sequence of CPU computation time and I/O time requirements.

===============================================================================

To compile and run the program:
1. Create two packages: main and Test.
2. Copy Analyzer.java and Process.java to main, Testing.java to Test.
3. The program should compile and run properly.
4. For testing purposes, use Analyzer.java to create Process objects, add them to a list and run analyzer's methods on the list. An example can be found in Testing.java file.
