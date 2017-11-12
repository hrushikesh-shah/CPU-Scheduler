package Test;

import java.util.ArrayList;
import java.util.List;

import main.Analyzer;
import main.Process;

public class Testing {

	public static void main(String[] args) {
		List<Process> list = new ArrayList<Process>();
		for (int i = 0; i < 6; i++) {
			Process p = new Process("Potato" + i, i, i); // Process("Name", (int) arrival time, (int) priority
			p.addCPUtime(20 - i);
			p.addIOtime(0, 25);
			p.addCPUtime(20 - i);
			list.add(p);
		}
		System.out.println(list.toString());
		Analyzer.nonPreempFCFS(list);
		Analyzer.nonPreempSJF(list);
		Analyzer.preempSJF(list);
		Analyzer.nonPreempPriority(list);
		Analyzer.preempPriority(list);
		Analyzer.RR(list, 5); // It takes the list and the time quanta as the input
		Analyzer.multilevel(list, 5); // It takes the list and the time quanta as the input
		Analyzer.multilevelFeedback(list, 8, 16); // It takes the list and two time quantas as the input
		System.out.println(
				"------------------------------------------------------------------------------------------------------------");

		List<Process> list2 = new ArrayList<Process>();
		Process p1 = new Process("p1", 0, 0);
		p1.addCPUtime(3);
		Process p2 = new Process("p2", 0, 0);
		p2.addCPUtime(3);
		Process p3 = new Process("p3", 0, 0);
		p3.addCPUtime(24);
		list2.add(p3);
		list2.add(p2);
		list2.add(p1);
		Analyzer.nonPreempFCFS(list2);
	}
}
