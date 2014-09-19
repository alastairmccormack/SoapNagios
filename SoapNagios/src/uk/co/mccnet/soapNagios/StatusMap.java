package uk.co.mccnet.soapNagios;

import org.apache.commons.collections.bidimap.TreeBidiMap;

import uk.co.mccnet.simplenrpe.enums.NRPEStatus;

import com.eviware.soapui.model.testsuite.TestRunner;

public class StatusMap extends TreeBidiMap {
	
	public StatusMap() {
		put(TestRunner.Status.CANCELED, NRPEStatus.CRITICAL);
		put(TestRunner.Status.FAILED, NRPEStatus.CRITICAL);
		put(TestRunner.Status.FINISHED, NRPEStatus.OK);
		put(TestRunner.Status.INITIALIZED, NRPEStatus.UNKNOWN);
		put(TestRunner.Status.RUNNING, NRPEStatus.UNKNOWN);
		put(TestRunner.Status.WARNING, NRPEStatus.WARNING);
	}

}
