package uk.co.mccnet.soapNagios;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import uk.co.mccnet.simplenrpe.NRPEServer;
import uk.co.mccnet.simplenrpe.handlers.command.CommandFactory;
import uk.co.mccnet.simplenrpe.handlers.command.NRPECommandHandler;


public class SoapNRPEServer {
	
	final static Logger logger = LoggerFactory.getLogger(SoapNRPEServer.class);

	public static String projectXML = "\\\\rbmgfs1015\\home$\\McCorA01\\My Documents\\Boxtv-soapui-project.xml";
	
	public static void main(String[] args) throws Exception {
		
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("/application-context.xml");    
        logger.info("Spring context initialized.");
		
        NRPEServer nrpeServer = 
        		applicationContext.getBean("server", NRPEServer.class);
        
        CommandFactory commandFactory = 
        		applicationContext.getBean("commandFactory", CommandFactory.class);
                
        NRPECommandHandler nrpeCommandHandler = new NRPECommandHandler(commandFactory);
		nrpeServer.setMainServerHandler( nrpeCommandHandler );
		nrpeServer.setMainServerHandler( nrpeCommandHandler );
		
		nrpeServer.run();	
		
	}

}
