package itx.examples.scopes.cdi;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;

import itx.examples.scopes.utils.IdGenerator;

import java.util.logging.Logger;

@Stateless
@Remote(StatelessRemote.class)
@Local(StatelessLocal.class)
public class StatelessBean implements StatelessRemote, StatelessLocal {

	private static final Logger logger = Logger.getLogger(StatelessBean.class.getName());
	private int myId;
	private String name;
	
	@PostConstruct
	private void init() {
		myId = IdGenerator.getNextId();
		name = "StatelessBean";
		logger.info("init [" + myId + "] ...");
	}
	
	@Override
	public int getId() {
		return myId;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@PreDestroy
	private void deinit() {
		logger.info("deinit [" + myId + "] ...");
	}
	
}
