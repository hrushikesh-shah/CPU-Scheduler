package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Analyzer {

	private Analyzer() {
	}

	public static void nonPreempFCFS(List<Process> processList) {
		List<Process> copyList = Analyzer.createCopy(processList);
		sortArrivalTime(copyList);
		List<Process> FCFSQueue = new ArrayList<Process>();
		Map<Integer, List<Process>> listIO = Analyzer.amalgamateIOLists(copyList);
		int processFetch = 0;
		double waitTime = 0;
		double turnaroundTime = 0;
		int time = 0;
		int numOfProcessCompleted = 0;
		while (numOfProcessCompleted != processList.size()) {

			while (processFetch < copyList.size() && (copyList.get(processFetch).getArrivalTime() == time)) {
				copyList.get(processFetch).setState(3);
				FCFSQueue.add(copyList.get(processFetch));
				processFetch++;
			}

			if (!FCFSQueue.isEmpty()) {
				for (Process p : FCFSQueue) {
					turnaroundTime++;
					waitTime++;
				}
				waitTime--; // Decreasing it by one because first process did not wait.
				FCFSQueue.get(0).setState(2);
				FCFSQueue.get(0).getSequence().set(0, (FCFSQueue.get(0).getSequence().get(0) - 1));
				if (FCFSQueue.get(0).getSequence().get(0) < 1) {
					FCFSQueue.get(0).getSequence().remove(0);
					/**
					 * Removes processes that have ended.
					 */
					if (FCFSQueue.get(0).getSequence().isEmpty()) {
						FCFSQueue.get(0).setState(5);
						FCFSQueue.remove(0);
						numOfProcessCompleted++;

					} else {
						FCFSQueue.get(0).setState(4);
						double deviceNum = FCFSQueue.get(0).getSequence().get(0);
						deviceNum = (int) ((deviceNum - ((int) deviceNum)) * 100.0);
						listIO.get((int) deviceNum).add(FCFSQueue.get(0));
						FCFSQueue.remove(0);
					}

				}
			}
			if (!listIO.isEmpty()) {
				for (Map.Entry<Integer, List<Process>> entry : listIO.entrySet()) {
					if (!entry.getValue().isEmpty()) {
						for (Process p : entry.getValue()) {
							turnaroundTime++;
						}

						entry.getValue().get(0).getSequence().set(0, entry.getValue().get(0).getSequence().get(0) - 1);

						if (entry.getValue().get(0).getSequence().get(0) < 1) {
							entry.getValue().get(0).getSequence().remove(0);
							if (entry.getValue().get(0).getSequence().isEmpty()) {
								throw new IllegalArgumentException("Program ends with CPU time, not IO time");
							}
							entry.getValue().get(0).setState(3);
							FCFSQueue.add(entry.getValue().get(0));
							entry.getValue().remove(0);
						}
					}
				}
			}
			time++;
		}
		waitTime = waitTime / processList.size();
		turnaroundTime = turnaroundTime / processList.size();
		System.out.printf(
				"Nonpremptive FCFS: The average wait time is %.2f units and the average turnaround time is %.2f units.\n",
				waitTime, turnaroundTime);
	}

	private static Map<Integer, List<Process>> amalgamateIOLists(List<Process> copyList) {
		Map<Integer, List<Process>> amalgamatedMap = new HashMap<Integer, List<Process>>();
		for (Process p : copyList) {
			for (int i : p.getListIO()) {
				if (!amalgamatedMap.containsKey(i)) {
					List<Process> waitList = new ArrayList<Process>();
					amalgamatedMap.put(i, waitList);
				}
			}
		}
		return amalgamatedMap;
	}

	public static void nonPreempSJF(List<Process> processList) {
		List<Process> copyList = Analyzer.createCopy(processList);
		sortArrivalTime(copyList);
		List<Process> SJFQueue = new ArrayList<Process>();
		Map<Integer, List<Process>> listIO = Analyzer.amalgamateIOLists(copyList);
		int processFetch = 0;
		double waitTime = 0;
		double turnaroundTime = 0;
		int time = 0;
		int numOfProcessCompleted = 0;
		while (numOfProcessCompleted != processList.size()) {

			while (processFetch < copyList.size() && (copyList.get(processFetch).getArrivalTime() == time)) {
				copyList.get(processFetch).setState(3);
				SJFQueue.add(copyList.get(processFetch));
				processFetch++;
			}
			if (!SJFQueue.isEmpty()) {
				for (Process p : SJFQueue) {
					turnaroundTime++;
					waitTime++;
				}
				waitTime--; // Decreasing it by one because first process did not wait.

				SJFQueue.get(0).setState(2);
				SJFQueue.get(0).getSequence().set(0, (SJFQueue.get(0).getSequence().get(0) - 1));

				if (SJFQueue.get(0).getSequence().get(0) < 1) {
					SJFQueue.get(0).getSequence().remove(0);
					/**
					 * Removes processes that have ended.
					 */
					if (SJFQueue.get(0).getSequence().isEmpty()) {
						SJFQueue.get(0).setState(5);
						SJFQueue.remove(0);
						numOfProcessCompleted++;

					} else {
						SJFQueue.get(0).setState(4);
						double deviceNum = SJFQueue.get(0).getSequence().get(0);
						deviceNum = (int) ((deviceNum - ((int) deviceNum)) * 100.0);
						listIO.get((int) deviceNum).add(SJFQueue.get(0));
						SJFQueue.remove(0);
					}
					Analyzer.reSortSJF(SJFQueue);
				}
			}
			for (Map.Entry<Integer, List<Process>> entry : listIO.entrySet()) {
				if (!entry.getValue().isEmpty()) {
					for (Process p : entry.getValue()) {
						turnaroundTime++;
					}

					entry.getValue().get(0).getSequence().set(0, entry.getValue().get(0).getSequence().get(0) - 1);

					if (entry.getValue().get(0).getSequence().get(0) < 1) {
						entry.getValue().get(0).getSequence().remove(0);
						if (entry.getValue().get(0).getSequence().isEmpty()) {
							throw new IllegalArgumentException("Program ends with CPU time, not IO time");
						}
						entry.getValue().get(0).setState(3);
						SJFQueue.add(entry.getValue().get(0));
						entry.getValue().remove(0);
					}
				}
			}
			time++;
		}
		waitTime = waitTime / processList.size();
		turnaroundTime = turnaroundTime / processList.size();
		System.out.printf(
				"Nonpreemptive SJF: The average wait time is %.2f units and the average turnaround time is %.2f units.\n",
				waitTime, turnaroundTime);
	}

	public static void preempSJF(List<Process> processList) {
		List<Process> copyList = Analyzer.createCopy(processList);
		sortArrivalTime(copyList);
		List<Process> SJFQueue = new ArrayList<Process>();
		Map<Integer, List<Process>> listIO = Analyzer.amalgamateIOLists(copyList);
		int processFetch = 0;
		double waitTime = 0;
		double turnaroundTime = 0;
		int time = 0;
		int numOfProcessCompleted = 0;
		while (numOfProcessCompleted != processList.size()) {

			while (processFetch < copyList.size() && (copyList.get(processFetch).getArrivalTime() == time)) {
				copyList.get(processFetch).setState(3);
				SJFQueue.add(copyList.get(processFetch));
				processFetch++;
				Analyzer.reSortSJF(SJFQueue);
			}

			if (!SJFQueue.isEmpty()) {
				for (Process p : SJFQueue) {
					turnaroundTime++;
					waitTime++;
				}
				waitTime--; // Decreasing it by one because first process did not wait.
				SJFQueue.get(0).setState(2);
				SJFQueue.get(0).getSequence().set(0, (SJFQueue.get(0).getSequence().get(0) - 1));

				if (SJFQueue.get(0).getSequence().get(0) < 1) {
					SJFQueue.get(0).getSequence().remove(0);

					/**
					 * Removes processes that have ended.
					 */
					if (SJFQueue.get(0).getSequence().isEmpty()) {
						SJFQueue.get(0).setState(5);
						SJFQueue.remove(0);
						numOfProcessCompleted++;

					} else {
						SJFQueue.get(0).setState(4);
						double deviceNum = SJFQueue.get(0).getSequence().get(0);
						deviceNum = (int) ((deviceNum - ((int) deviceNum)) * 100.0);
						listIO.get((int) deviceNum).add(SJFQueue.get(0));
						SJFQueue.remove(0);
					}
					Analyzer.reSortSJF(SJFQueue);
				}
			}
			for (Map.Entry<Integer, List<Process>> entry : listIO.entrySet()) {
				if (!entry.getValue().isEmpty()) {
					for (Process p : entry.getValue()) {
						turnaroundTime++;
					}

					entry.getValue().get(0).getSequence().set(0, entry.getValue().get(0).getSequence().get(0) - 1);

					if (entry.getValue().get(0).getSequence().get(0) < 1) {
						entry.getValue().get(0).getSequence().remove(0);
						if (entry.getValue().get(0).getSequence().isEmpty()) {
							throw new IllegalArgumentException("Program ends with CPU time, not IO time");
						}
						entry.getValue().get(0).setState(3);
						SJFQueue.add(entry.getValue().get(0));
						entry.getValue().remove(0);
						SJFQueue.get(0).setState(3);
						Analyzer.reSortSJF(SJFQueue);
					}
				}
			}
			time++;
		}
		waitTime = waitTime / processList.size();
		turnaroundTime = turnaroundTime / processList.size();
		System.out.printf(
				"Preemptive SJF: The average wait time is %.2f units and the average turnaround time is %.2f units.\n",
				waitTime, turnaroundTime);
	}

	public static void nonPreempPriority(List<Process> processList) {
		List<Process> copyList = Analyzer.createCopy(processList);
		sortArrivalTime(copyList);
		List<Process> queue = new ArrayList<Process>();
		Map<Integer, List<Process>> listIO = Analyzer.amalgamateIOLists(copyList);
		int processFetch = 0;
		double waitTime = 0;
		double turnaroundTime = 0;
		int time = 0;
		int numOfProcessCompleted = 0;
		while (numOfProcessCompleted != processList.size()) {

			while (processFetch < copyList.size() && (copyList.get(processFetch).getArrivalTime() == time)) {
				copyList.get(processFetch).setState(3);
				queue.add(copyList.get(processFetch));
				processFetch++;
			}
			if (!queue.isEmpty()) {
				for (Process p : queue) {
					turnaroundTime++;
					waitTime++;
				}
				waitTime--; // Decreasing it by one because first process did not wait.
				queue.get(0).setState(2);
				queue.get(0).getSequence().set(0, (queue.get(0).getSequence().get(0) - 1));

				if (queue.get(0).getSequence().get(0) < 1) {
					queue.get(0).getSequence().remove(0);
					/**
					 * Removes processes that have ended.
					 */
					if (queue.get(0).getSequence().isEmpty()) {
						queue.get(0).setState(5);
						queue.remove(0);
						numOfProcessCompleted++;

					} else {
						queue.get(0).setState(4);
						double deviceNum = queue.get(0).getSequence().get(0);
						deviceNum = (int) ((deviceNum - ((int) deviceNum)) * 100.0);
						listIO.get((int) deviceNum).add(queue.get(0));
						queue.remove(0);
					}
					Analyzer.resortPriority(queue);
				}
			}
			for (Map.Entry<Integer, List<Process>> entry : listIO.entrySet()) {
				if (!entry.getValue().isEmpty()) {
					for (Process p : entry.getValue()) {
						turnaroundTime++;
					}

					entry.getValue().get(0).getSequence().set(0, entry.getValue().get(0).getSequence().get(0) - 1);

					if (entry.getValue().get(0).getSequence().get(0) < 1) {
						entry.getValue().get(0).getSequence().remove(0);
						if (entry.getValue().get(0).getSequence().isEmpty()) {
							throw new IllegalArgumentException("Program ends with CPU time, not IO time");
						}
						entry.getValue().get(0).setState(3);
						queue.add(entry.getValue().get(0));
						entry.getValue().remove(0);
					}
				}
			}
			time++;
		}
		waitTime = waitTime / processList.size();
		turnaroundTime = turnaroundTime / processList.size();
		System.out.printf(
				"Nonpreemptive Priority: The average wait time is %.2f units and the average turnaround time is %.2f units.\n",
				waitTime, turnaroundTime);
	}

	public static void preempPriority(List<Process> processList) {
		List<Process> copyList = Analyzer.createCopy(processList);
		sortArrivalTime(copyList);
		List<Process> queue = new ArrayList<Process>();
		Map<Integer, List<Process>> listIO = Analyzer.amalgamateIOLists(copyList);
		int processFetch = 0;
		double waitTime = 0;
		double turnaroundTime = 0;
		int time = 0;
		int numOfProcessCompleted = 0;
		while (numOfProcessCompleted != processList.size()) {

			while (processFetch < copyList.size() && (copyList.get(processFetch).getArrivalTime() == time)) {
				copyList.get(processFetch).setState(3);
				queue.add(copyList.get(processFetch));
				processFetch++;
				Analyzer.resortPriority(queue);
			}

			if (!queue.isEmpty()) {
				for (Process p : queue) {
					turnaroundTime++;
					waitTime++;
				}
				waitTime--; // Decreasing it by one because first process did not wait.
				queue.get(0).setState(2);
				queue.get(0).getSequence().set(0, (queue.get(0).getSequence().get(0) - 1));

				if (queue.get(0).getSequence().get(0) < 1) {
					queue.get(0).getSequence().remove(0);
					/**
					 * Removes processes that have ended.
					 */
					if (queue.get(0).getSequence().isEmpty()) {
						queue.get(0).setState(5);
						queue.remove(0);
						numOfProcessCompleted++;

					} else {
						queue.get(0).setState(4);
						double deviceNum = queue.get(0).getSequence().get(0);
						deviceNum = deviceNum - ((int) deviceNum);
						deviceNum = (int) ((deviceNum - ((int) deviceNum)) * 100.0);
						listIO.get((int) deviceNum).add(queue.get(0));
						queue.remove(0);
					}
					Analyzer.resortPriority(queue);
				}
			}
			for (Map.Entry<Integer, List<Process>> entry : listIO.entrySet()) {
				if (!entry.getValue().isEmpty()) {
					for (Process p : entry.getValue()) {
						turnaroundTime++;
					}

					entry.getValue().get(0).getSequence().set(0, entry.getValue().get(0).getSequence().get(0) - 1);

					if (entry.getValue().get(0).getSequence().get(0) < 1) {
						entry.getValue().get(0).getSequence().remove(0);
						if (entry.getValue().get(0).getSequence().isEmpty()) {
							throw new IllegalArgumentException("Program ends with CPU time, not IO time");
						}
						entry.getValue().get(0).setState(3);
						queue.add(entry.getValue().get(0));
						entry.getValue().remove(0);
						queue.get(0).setState(3);
						Analyzer.resortPriority(queue);
					}
				}
			}
			time++;
		}
		waitTime = waitTime / processList.size();
		turnaroundTime = turnaroundTime / processList.size();
		System.out.printf(
				"Preemptive Priority: The average wait time is %.2f units and the average turnaround time is %.2f units.\n",
				waitTime, turnaroundTime);
	}

	public static void RR(List<Process> processList, int timeQuanta) {
		List<Process> copyList = Analyzer.createCopy(processList);
		sortArrivalTime(copyList);
		List<Process> queue = new ArrayList<Process>();
		Map<Integer, List<Process>> listIO = Analyzer.amalgamateIOLists(copyList);
		int processFetch = 0;
		double waitTime = 0;
		double turnaroundTime = 0;
		int time = 0;
		int numOfProcessCompleted = 0;
		int timer = 0;
		while (numOfProcessCompleted != processList.size()) {

			while (processFetch < copyList.size() && (copyList.get(processFetch).getArrivalTime() == time)) {
				copyList.get(processFetch).setState(3);
				queue.add(copyList.get(processFetch));
				processFetch++;
			}

			if (!queue.isEmpty()) {
				for (Process p : queue) {
					turnaroundTime++;
					waitTime++;
				}
				waitTime--; // Decreasing it by one because first process did not wait.
				timer++;

				queue.get(0).setState(2);
				queue.get(0).getSequence().set(0, (queue.get(0).getSequence().get(0) - 1));

				if (queue.get(0).getSequence().get(0) < 1) {
					queue.get(0).getSequence().remove(0);
					timer = 0;
					/**
					 * Removes processes that have ended.
					 */
					if (queue.get(0).getSequence().isEmpty()) {
						queue.get(0).setState(5);
						queue.remove(0);
						numOfProcessCompleted++;

					} else {
						queue.get(0).setState(4);
						double deviceNum = queue.get(0).getSequence().get(0);
						deviceNum = deviceNum - ((int) deviceNum);
						deviceNum = (int) ((deviceNum - ((int) deviceNum)) * 100.0);
						listIO.get((int) deviceNum).add(queue.get(0));
						queue.remove(0);
					}

				}
				if (timeQuanta == timer) {
					queue.get(0).setState(3);
					queue.add(queue.get(0));
					queue.remove(0);
					timer = 0;
				}
			}

			for (Map.Entry<Integer, List<Process>> entry : listIO.entrySet()) {
				if (!entry.getValue().isEmpty()) {
					for (Process p : entry.getValue()) {
						turnaroundTime++;
					}

					entry.getValue().get(0).getSequence().set(0, entry.getValue().get(0).getSequence().get(0) - 1);

					if (entry.getValue().get(0).getSequence().get(0) < 1) {
						entry.getValue().get(0).getSequence().remove(0);
						if (entry.getValue().get(0).getSequence().isEmpty()) {
							throw new IllegalArgumentException("Program ends with CPU time, not IO time");
						}
						entry.getValue().get(0).setState(3);
						queue.add(entry.getValue().get(0));
						entry.getValue().remove(0);
					}
				}
			}
			time++;
		}
		waitTime = waitTime / processList.size();
		turnaroundTime = turnaroundTime / processList.size();
		System.out.printf(
				"Premptive RR: The average wait time is %.2f units and the average turnaround time is %.2f units.\n",
				waitTime, turnaroundTime);
	}

	public static void multilevel(List<Process> processList, int timeQuanta) {
		List<Process> copyList = Analyzer.createCopy(processList);
		List<Process> foreground = new ArrayList<Process>();
		List<Process> background = new ArrayList<Process>();
		sortArrivalTime(copyList);
		Map<Integer, List<Process>> listIO = Analyzer.amalgamateIOLists(copyList);
		int processFetch = 0;
		double waitTime = 0;
		double turnaroundTime = 0;
		int time = 0;
		int timer = 0;
		int numOfProcessCompleted = 0;
		while (numOfProcessCompleted != processList.size()) {

			while (processFetch < copyList.size() && (copyList.get(processFetch).getArrivalTime() == time)) {
				copyList.get(processFetch).setState(3);
				if (copyList.get(processFetch).getProcessPriority() < 4) {
					foreground.add(copyList.get(processFetch));
				} else {
					background.add(copyList.get(processFetch));
				}
				processFetch++;
			}

			if (!foreground.isEmpty()) {
				for (Process p : foreground) {
					turnaroundTime++;
					waitTime++;
				}
				waitTime--; // Decreasing it by one because first process did not wait.
				timer++;

				foreground.get(0).setState(2);
				foreground.get(0).getSequence().set(0, (foreground.get(0).getSequence().get(0) - 1));

				if (foreground.get(0).getSequence().get(0) < 1) {
					foreground.get(0).getSequence().remove(0);
					timer = 0;
					/**
					 * Removes processes that have ended.
					 */
					if (foreground.get(0).getSequence().isEmpty()) {
						foreground.get(0).setState(5);
						foreground.remove(0);
						numOfProcessCompleted++;

					} else {
						foreground.get(0).setState(4);
						double deviceNum = foreground.get(0).getSequence().get(0);
						deviceNum = deviceNum - ((int) deviceNum);
						deviceNum = (int) ((deviceNum - ((int) deviceNum)) * 100.0);
						listIO.get((int) deviceNum).add(foreground.get(0));
						foreground.remove(0);
					}
				}
				if (timeQuanta == timer) {
					foreground.get(0).setState(3);
					foreground.add(foreground.get(0));
					foreground.remove(0);
					timer = 0;
				}
			}

			else if (!background.isEmpty()) {
				for (Process p : background) {
					turnaroundTime++;
					waitTime++;
				}
				waitTime--; // Decreasing it by one because first process did not wait.

				background.get(0).setState(2);
				background.get(0).getSequence().set(0, (background.get(0).getSequence().get(0) - 1));

				if (background.get(0).getSequence().get(0) < 1) {
					background.get(0).getSequence().remove(0);
					/**
					 * Removes processes that have ended.
					 */
					if (background.get(0).getSequence().isEmpty()) {
						background.get(0).setState(5);
						background.remove(0);
						numOfProcessCompleted++;

					} else {
						background.get(0).setState(4);
						double deviceNum = background.get(0).getSequence().get(0);
						deviceNum = deviceNum - ((int) deviceNum);
						deviceNum = (int) ((deviceNum - ((int) deviceNum)) * 100.0);
						listIO.get((int) deviceNum).add(background.get(0));
						background.remove(0);
					}
				}
			}

			for (Map.Entry<Integer, List<Process>> entry : listIO.entrySet()) {
				if (!entry.getValue().isEmpty()) {
					for (Process p : entry.getValue()) {
						turnaroundTime++;
					}

					entry.getValue().get(0).getSequence().set(0, entry.getValue().get(0).getSequence().get(0) - 1);

					if (entry.getValue().get(0).getSequence().get(0) < 1) {
						entry.getValue().get(0).getSequence().remove(0);
						if (entry.getValue().get(0).getSequence().isEmpty()) {
							throw new IllegalArgumentException("Program ends with CPU time, not IO time");
						}
						entry.getValue().get(0).setState(3);
						if (entry.getValue().get(0).getProcessPriority() < 4) {
							foreground.add(entry.getValue().get(0));
						} else {
							background.add(entry.getValue().get(0));
						}
						entry.getValue().remove(0);
					}
				}
			}
			time++;
		}
		waitTime = waitTime / processList.size();
		turnaroundTime = turnaroundTime / processList.size();
		System.out.printf(
				"Multilevel Queue: The average wait time is %.2f units and the average turnaround time is %.2f units.\n",
				waitTime, turnaroundTime);
	}

	public static void multilevelFeedback(List<Process> processList, int timeQuanta0, int timeQuanta1) {
		List<Process> copyList = Analyzer.createCopy(processList);
		List<Process> queue0 = new ArrayList<Process>();
		List<Process> queue1 = new ArrayList<Process>();
		List<Process> queue2 = new ArrayList<Process>();
		sortArrivalTime(copyList);
		Map<Integer, List<Process>> listIO = Analyzer.amalgamateIOLists(copyList);
		int processFetch = 0;
		double waitTime = 0;
		double turnaroundTime = 0;
		int time = 0;
		int timer0 = 0;
		int timer1 = 0;
		int numOfProcessCompleted = 0;
		while (numOfProcessCompleted != processList.size()) {

			while (processFetch < copyList.size() && (copyList.get(processFetch).getArrivalTime() == time)) {
				copyList.get(processFetch).setState(3);
				queue0.add(copyList.get(processFetch));
				processFetch++;
			}

			if (!queue0.isEmpty()) {
				for (Process p : queue0) {
					turnaroundTime++;
					waitTime++;
				}
				waitTime--; // Decreasing it by one because first process did not wait.
				timer0++;

				queue0.get(0).setState(2);
				queue0.get(0).getSequence().set(0, (queue0.get(0).getSequence().get(0) - 1));

				if (queue0.get(0).getSequence().get(0) < 1) {
					queue0.get(0).getSequence().remove(0);
					timer0 = 0;
					/**
					 * Removes processes that have ended.
					 */
					if (queue0.get(0).getSequence().isEmpty()) {
						queue0.get(0).setState(5);
						queue0.remove(0);
						numOfProcessCompleted++;
					} else {
						queue0.get(0).setState(4);
						double deviceNum = queue0.get(0).getSequence().get(0);
						deviceNum = deviceNum - ((int) deviceNum);
						deviceNum = (int) ((deviceNum - ((int) deviceNum)) * 100.0);
						listIO.get((int) deviceNum).add(queue0.get(0));
						queue0.remove(0);
					}
				}
				if (timeQuanta0 == timer0) {
					queue0.get(0).setState(3);
					queue1.add(queue0.get(0));
					queue0.remove(0);
					timer0 = 0;
				}
			}

			else if (!queue1.isEmpty()) {
				for (Process p : queue1) {
					turnaroundTime++;
					waitTime++;
				}
				waitTime--; // Decreasing it by one because first process did not wait.
				timer1++;

				queue1.get(0).setState(2);
				queue1.get(0).getSequence().set(0, (queue1.get(0).getSequence().get(0) - 1));

				if (queue1.get(0).getSequence().get(0) < 1) {
					queue1.get(0).getSequence().remove(0);
					timer1 = 0;
					/**
					 * Removes processes that have ended.
					 */
					if (queue1.get(0).getSequence().isEmpty()) {
						queue1.get(0).setState(5);
						queue1.remove(0);
						numOfProcessCompleted++;

					} else {
						queue1.get(0).setState(4);
						double deviceNum = queue1.get(0).getSequence().get(0);
						deviceNum = deviceNum - ((int) deviceNum);
						deviceNum = (int) ((deviceNum - ((int) deviceNum)) * 100.0);
						listIO.get((int) deviceNum).add(queue1.get(0));
						queue1.remove(0);
					}
				}
				if (timeQuanta1 == timer1) {
					queue1.get(0).setState(3);
					queue2.add(queue1.get(0));
					queue1.remove(0);
					timer1 = 0;
				}
			}

			else if (!queue2.isEmpty()) {
				for (Process p : queue2) {
					turnaroundTime++;
					waitTime++;
				}
				waitTime--; // Decreasing it by one because first process did not wait.
				queue2.get(0).setState(2);
				queue2.get(0).getSequence().set(0, (queue2.get(0).getSequence().get(0) - 1));

				if (queue2.get(0).getSequence().get(0) < 1) {
					queue2.get(0).getSequence().remove(0);

					/**
					 * Removes processes that have ended.
					 */
					if (queue2.get(0).getSequence().isEmpty()) {
						queue2.get(0).setState(5);
						queue2.remove(0);
						numOfProcessCompleted++;

					} else {
						queue2.get(0).setState(4);
						double deviceNum = queue2.get(0).getSequence().get(0);
						deviceNum = deviceNum - ((int) deviceNum);
						deviceNum = (int) ((deviceNum - ((int) deviceNum)) * 100.0);
						listIO.get((int) deviceNum).add(queue2.get(0));
						queue2.remove(0);
					}

				}
			}

			for (Map.Entry<Integer, List<Process>> entry : listIO.entrySet()) {
				if (!entry.getValue().isEmpty()) {
					for (Process p : entry.getValue()) {
						turnaroundTime++;
					}

					entry.getValue().get(0).getSequence().set(0, entry.getValue().get(0).getSequence().get(0) - 1);

					if (entry.getValue().get(0).getSequence().get(0) < 1) {
						entry.getValue().get(0).getSequence().remove(0);
						if (entry.getValue().get(0).getSequence().isEmpty()) {
							throw new IllegalArgumentException("Program ends with CPU time, not IO time");
						}
						entry.getValue().get(0).setState(3);
						queue0.add(entry.getValue().get(0));
						entry.getValue().remove(0);
					}
				}
			}
			time++;
		}
		waitTime = waitTime / processList.size();
		turnaroundTime = turnaroundTime / processList.size();
		System.out.printf(
				"Multilevel Feedback: The average wait time is %.2f units and the average turnaround time is %.2f units.\n",
				waitTime, turnaroundTime);
	}

	private static void resortPriority(List<Process> queue) {
		if (queue.size() == 1) {
			return;
		}
		int outside, inside;
		for (outside = queue.size() - 1; outside > 0; outside--) {
			for (inside = 0; inside < outside; inside++) {
				if (queue.get(inside).getProcessPriority() > queue.get(inside + 1).getProcessPriority()) {
					Process temp = queue.get(inside);
					queue.set(inside, queue.get(inside + 1));
					queue.set(inside + 1, temp);
				}
			}
		}
	}

	private static void sortArrivalTime(List<Process> copyList) {
		if (copyList.size() == 1) {
			return;
		}
		int outside, inside;
		for (outside = copyList.size() - 1; outside > 0; outside--) {
			for (inside = 0; inside < outside; inside++) {
				if (copyList.get(inside).getArrivalTime() > copyList.get(inside + 1).getArrivalTime()) {
					Process temp = copyList.get(inside);
					copyList.set(inside, copyList.get(inside + 1));
					copyList.set(inside + 1, temp);
				}
			}
		}
	}

	private static List<Process> createCopy(List<Process> ogList) {
		List<Process> copyList = new ArrayList<Process>();
		for (Process input : ogList) {
			Process p = new Process(input);
			copyList.add(p);
		}
		return copyList;
	}

	private static void reSortSJF(List<Process> SJFQueue) {
		if (SJFQueue.size() == 1) {
			return;
		}
		int outside, inside;
		for (outside = SJFQueue.size() - 1; outside > 0; outside--) {
			for (inside = 0; inside < outside; inside++) {
				if (SJFQueue.get(inside).getSequence().get(0) > SJFQueue.get(inside + 1).getSequence().get(0)) {
					Process temp = SJFQueue.get(inside);
					SJFQueue.set(inside, SJFQueue.get(inside + 1));
					SJFQueue.set(inside + 1, temp);
				}
			}
		}
	}
}
