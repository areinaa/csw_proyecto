package com.Project.socket.Project.servers;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class TestRunnerPartitions {
	
	public static void main (String[] args) {
		Result result = JUnitCore.runClasses(TestPartition1.class, TestPartition2.class, TestPartition3.class);
	
		for(Failure failure : result.getFailures())
			System.out.println(failure.toString());
		
		System.out.println("Exito?:");
		System.out.println(result.wasSuccessful());
		
	}

}
