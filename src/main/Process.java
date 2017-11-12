package main;

import java.util.ArrayList;
import java.util.List;

public class Process {

	public Process(String processName, int arrivalTime, int processPriority) {
		this.processName = processName;
		this.setArrivalTime(arrivalTime);
		this.setProcessPriority(processPriority);
		this.sequence = new ArrayList<Double>();
		this.setState(0);
		this.listIO = new ArrayList<Integer>();
	}

	public Process(Process input) {
		this.processName = input.getProcessName();
		;
		this.setArrivalTime(input.getArrivalTime());
		this.setProcessPriority(input.getProcessPriority());
		this.sequence = new ArrayList<Double>(input.sequence);
		this.setState(0);
		this.listIO = new ArrayList<Integer>(input.getListIO());
	}

	private int arrivalTime;
	private int processPriority;
	private String processName;

	/**
	 * @param state
	 *            1 = New 2 = Running 3 = Ready 4 = Waiting for I/O 5 = Terminated
	 */
	protected int state;

	private List<Double> sequence;
	private List<Integer> listIO;

	/**
	 * @return the processName
	 */
	protected String getProcessName() {
		return this.processName;
	}

	/**
	 * @prints the processName
	 */
	public void printProcessName() {
		System.out.println("Process_Name = " + processName);
	}

	/**
	 * @print the arrivalTime
	 */
	public void printArrivalTime() {
		System.out.println("Arrival_Time(" + this.processName + ") = " + arrivalTime);
	}

	/**
	 * @print the arrivalTime
	 */
	public int getArrivalTime() {
		return this.arrivalTime;
	}

	/**
	 * @param arrivalTime
	 *            the arrivalTime to set
	 */
	public void setArrivalTime(int arrivalTime) {
		if (arrivalTime < 0) {
			throw new IllegalArgumentException("Arrival time cannot be negative");
		}
		this.arrivalTime = arrivalTime;
	}

	/**
	 * @return the processPriority
	 */
	public int getProcessPriority() {
		return this.processPriority;
	}

	/**
	 * @prints the processPriority
	 */
	public void printProcessPriority() {
		System.out.println("Process_Priority(" + this.processName + ") = " + this.processPriority);
	}

	/**
	 * @param processPriority
	 *            the processPriority to set
	 */
	public void setProcessPriority(int processPriority) {
		if (processPriority < 0) {
			throw new IllegalArgumentException("Process priority cannot be negative");
		}
		this.processPriority = processPriority;
	}

	/**
	 * @return
	 * @return the sequence
	 */
	protected List<Double> getSequence() {
		return this.sequence;
	}

	public void printSequence() {
		System.out.println(sequence);
	}

	public void addCPUtime(int duration) {
		if (duration < 0) {
			throw new IllegalArgumentException("Duration cannot be less than 0");
		}
		if ((this.sequence.size() % 2) != 0) {
			throw new IllegalArgumentException("You need to input IO Time now.");
		}
		this.sequence.add((double) duration);
	}

	public void addIOtime(int device, int duration) {
		if (duration < 0) {
			throw new IllegalArgumentException("Duration cannot be less than 0");
		}
		if ((this.sequence.size() % 2) == 0) {
			throw new IllegalArgumentException("You need to input CPU Time now.");
		}

		if (!this.getListIO().contains(device)) {
			this.getListIO().add(device);
		}
		this.sequence.add(duration + (device / 100.0));
	}

	private void getState() {
		String currState;
		switch (this.state) {
		case 1:
			currState = "New";
			break;
		case 2:
			currState = "Running";
			break;
		case 3:
			currState = "Ready";
			break;
		case 4:
			currState = "Waiting for I/O";
			break;
		case 5:
			currState = "Terminated";
			break;
		default:
			currState = "Unreachable";
			break;
		}
		System.out.println(currState);
	}

	/**
	 * @param state
	 *            the state to set
	 */
	protected void setState(int state) {
		if (state < 0 || state > 5) {
			throw new IllegalArgumentException("Illegal State");
		}
		this.state = state;
	}

	/**
	 * @return the list Of IO devices
	 */
	protected List<Integer> getListIO() {
		return listIO;
	}

	@Override
	public String toString() {
		return "Name: " + this.processName + ", " + "Arrival time: " + this.arrivalTime + ", Priority: "
				+ this.processPriority;
	}

}
