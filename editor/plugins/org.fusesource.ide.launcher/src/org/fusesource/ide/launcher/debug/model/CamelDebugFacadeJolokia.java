package org.fusesource.ide.launcher.debug.model;

import java.util.Set;

import javax.management.MalformedObjectNameException;

import org.fusesource.ide.camel.model.service.core.jmx.camel.ICamelDebuggerMBeanFacade;
import org.jolokia.client.J4pClient;
import org.jolokia.client.exception.J4pException;
import org.jolokia.client.request.J4pReadRequest;
import org.jolokia.client.request.J4pReadResponse;

public class CamelDebugFacadeJolokia implements ICamelDebuggerMBeanFacade{
	
	J4pClient j4pClient;
	
	public CamelDebugFacadeJolokia() {
		j4pClient = new J4pClient("http://localhost:8080/jolokia");
        
	}

	@Override
	public String getContextId() {
		return null;
	}

	@Override
	public void updateContext(String xmlDump) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getLoggingLevel() {
		try {
			J4pReadRequest req = new J4pReadRequest(CAMEL_DEBUGGER_MBEAN_DEFAULT, "getLoggingLevel");
			J4pReadResponse resp = j4pClient.execute(req);
			return resp.getValue();
		} catch (MalformedObjectNameException | J4pException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void setLoggingLevel(String level) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void enableDebugger() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disableDebugger() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addBreakpoint(String nodeId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addConditionalBreakpoint(String nodeId, String language, String predicate) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeBreakpoint(String nodeId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeAllBreakpoints() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resumeBreakpoint(String nodeId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMessageBodyOnBreakpoint(String nodeId, Object body) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMessageBodyOnBreakpoint(String nodeId, Object body, String type) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeMessageBodyOnBreakpoint(String nodeId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMessageHeaderOnBreakpoint(String nodeId, String headerName, Object value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeMessageHeaderOnBreakpoint(String nodeId, String headerName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMessageHeaderOnBreakpoint(String nodeId, String headerName, Object value, String type) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resumeAll() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stepBreakpoint(String nodeId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isSingleStepMode() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void step() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Set<String> getBreakpoints() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> getSuspendedBreakpointNodeIds() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void disableBreakpoint(String nodeId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enableBreakpoint(String nodeId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getBodyMaxChars() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setBodyMaxChars(int bodyMaxChars) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isBodyIncludeStreams() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setBodyIncludeStreams(boolean bodyIncludeStreams) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isBodyIncludeFiles() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setBodyIncludeFiles(boolean bodyIncludeFiles) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String dumpTracedMessagesAsXml(String nodeId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getDebugCounter() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void resetDebugCounter() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getContextXmlDump() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRouteId(String processorId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCamelId(String processorId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getCompletedExchanges(String processorId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getFailedExchanges(String processorId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getTotalExchanges(String processorId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getRedeliveries(String processorId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getExternalRedeliveries(String processorId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getHandledFailures(String processorId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getLastProcessingTime(String processorId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getMinProcessingTime(String processorId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getMaxProcessingTime(String processorId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getAverageProcessingTime(String processorId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getTotalProcessingTime(String processorId) {
		// TODO Auto-generated method stub
		return 0;
	}

}
