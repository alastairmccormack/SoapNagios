package uk.co.mccnet.soapNagios;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;

import org.apache.xmlbeans.XmlException;

import uk.co.mccnet.simplenrpe.enums.NRPEStatus;
import uk.co.mccnet.simplenrpe.handlers.command.Command;
import uk.co.mccnet.simplenrpe.handlers.command.CommandRequest;
import uk.co.mccnet.simplenrpe.handlers.command.CommandResponse;

import com.eviware.soapui.impl.support.http.HttpRequestTestStep;
import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.impl.wsdl.teststeps.RestTestRequest;
import com.eviware.soapui.impl.wsdl.teststeps.RestTestRequestStep;
import com.eviware.soapui.impl.wsdl.teststeps.WsdlTestRequestStep;
import com.eviware.soapui.model.support.PropertiesMap;
import com.eviware.soapui.model.testsuite.Assertable;
import com.eviware.soapui.model.testsuite.TestAssertion;
import com.eviware.soapui.model.testsuite.TestCaseRunner;
import com.eviware.soapui.model.testsuite.TestStepResult;
import com.eviware.soapui.model.testsuite.TestStepResult.TestStepStatus;
import com.eviware.soapui.model.testsuite.TestSuite;
import com.eviware.soapui.model.testsuite.TestSuiteRunner;
import com.eviware.soapui.support.SoapUIException;
import com.eviware.soapui.support.action.swing.ActionList;

public class SoapNagiosCommand implements Command {
	
	String projectFilename;
	ArrayList<TestSuiteRequest> testSuiteRequests = new ArrayList<TestSuiteRequest>();
	NRPEStatus returnStatus = NRPEStatus.CRITICAL;
	
	public SoapNagiosCommand(String projectFilename) {
		this.projectFilename = projectFilename;
	}
	
	public CommandResponse execute(CommandRequest commandRequest) {
		 
		WsdlProject wsdlProject = null;
		
		try {
			wsdlProject = new WsdlProject(projectFilename);
		} catch (Exception e) {
			return new CommandResponse(NRPEStatus.UNKNOWN, e.getMessage() );
		}
		
		for (TestSuite ts : wsdlProject.getTestSuiteList() ) {
			if (ts == null) {
				return new CommandResponse(NRPEStatus.UNKNOWN, 
						String.format("Test Suite: %s does not exist", ts.getName() ));
			}
			
			TestSuiteRunner tsr = ts.run(new PropertiesMap(), false);
			
			StatusMap statusMap = new StatusMap();
			NRPEStatus nrpeStatus = (NRPEStatus) statusMap.get(tsr.getStatus());
			
			CommandResponse commandResponse = new CommandResponse(nrpeStatus, 
					String.format("%s: %s - Completed in %d milliseconds",  
							nrpeStatus.getName(),
							ts.getName(), tsr.getTimeTaken()));
			
			commandResponse.setLongMessage(resultFormater(tsr));
			return commandResponse;	
		}
		return null;
	}
	
	private String resultFormater(TestSuiteRunner testSuiteRunner) {
		String projectName = testSuiteRunner.getTestSuite().getProject().getName();
		StringBuilder resultString = new StringBuilder();
		
		String testSuiteName = testSuiteRunner.getTestSuite().getName();
		resultString.append(String.format("%s:%s\n", projectName, testSuiteName));
		
		for ( TestCaseRunner testCaseRunner : testSuiteRunner.getResults()) {
			String testCaseName = testCaseRunner.getTestCase().getName();
			
			resultString.append(
					String.format("\t%s - SoapUI Status: %s\n", testCaseName, testCaseRunner.getStatus())
					);
			
			for ( TestStepResult testStepResult : testCaseRunner.getResults() ) {
				String testStepName = testStepResult.getTestStep().getName();

				Assertable assertableTestStep = (Assertable) testStepResult.getTestStep();
				
				resultString.append(
						String.format("\t\t%s - SoapUI Status: %s\n", testStepName, testStepResult.getStatus())
						);				
				
				resultString.append("\t\t\tResults:\n");
				for (TestAssertion testAssertion : assertableTestStep.getAssertionList() ) {
					resultString.append(
							String.format("\t\t\t\t%s : %s\n", testAssertion.getName(), testAssertion.getStatus())
							);	
				}
				
				// If something failed
				if (! testStepResult.getStatus().equals(TestStepStatus.OK)) {
					resultString.append("\t\t\tErrors:\n");
					for (String message : testStepResult.getMessages() ) {
						resultString.append(String.format("\t\t\t\t%s\n", message));
					}
				}
			}
		} 
		
		return resultString.toString();
	}

	public ArrayList<TestSuiteRequest> getTestSuiteRequests() {
		return testSuiteRequests;
	}

	public void setTestSuiteRequests(ArrayList<TestSuiteRequest> testSuiteRequests) {
		this.testSuiteRequests = testSuiteRequests;
	}


}
