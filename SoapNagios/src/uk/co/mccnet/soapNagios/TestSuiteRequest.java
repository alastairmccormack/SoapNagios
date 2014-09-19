package uk.co.mccnet.soapNagios;

import uk.co.mccnet.simplenrpe.enums.NRPEStatus;

public class TestSuiteRequest {
	private NRPEStatus nrpeStatus;
	private String projectName;
	private String testCaseName;
	public NRPEStatus getNrpeStatus() {
		return nrpeStatus;
	}
	public void setNrpeStatus(NRPEStatus nrpeStatus) {
		this.nrpeStatus = nrpeStatus;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getTestCaseName() {
		return testCaseName;
	}
	public void setTestCaseName(String testCaseName) {
		this.testCaseName = testCaseName;
	}

}
