package ite.examples.scopes.cdi;

import ite.examples.scopes.utils.IdGenerator;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJBException;
import javax.ejb.Init;
import javax.ejb.Local;
import javax.ejb.PostActivate;
import javax.ejb.PrePassivate;
import javax.ejb.Remote;
import javax.ejb.Remove;
import javax.ejb.SessionSynchronization;
import javax.ejb.Stateful;

import java.rmi.RemoteException;
import java.util.logging.Logger;

@Stateful(passivationCapable=true)
@Remote(StatefulRemote.class)
@Local(StatefulLocal.class)
public class StatefulBean implements StatefulRemote, StatefulLocal, SessionSynchronization {

	private static final Logger logger = Logger.getLogger(StatefulBean.class.getName());
	private int myId;
	private String name;
	
    @PostConstruct
    public void init () {
    	myId = IdGenerator.getNextId();
    	name = "StatefulBean";
    	logger.info("init [" + myId + "]");
    }

	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public int getId() {
		return myId;
	}

    @PreDestroy
    public void deinit () {
    	logger.info("deinit [" + myId + "] ...");
    }
 
    @PrePassivate
    public void beforePassivate () {
    	logger.info("beforePassivate [" + myId + "] ...");
    }
 
 
    @PostActivate
    public void afterActivation () {
    	logger.info("afterActivation [" + myId + "] ...");
    }
 
    @Init
    public void initialize () {
    	logger.info("initialize [" + myId + "] ...");
    }
 
    @Remove
    public void stopSession () {
    	logger.info("stopSession [" + myId + "] ...");
    } 
    
	@Override
	public void afterBegin() throws EJBException, RemoteException {
		logger.info("afterBegin [" + myId + "]");
		
	}

	@Override
	public void beforeCompletion() throws EJBException, RemoteException {
		logger.info("beforeCompletion [" + myId + "]");
		
	}

	@Override
	public void afterCompletion(boolean committed) throws EJBException, RemoteException {
		logger.info("afterCompletion [" + myId + "]");
		
	}
	
}
