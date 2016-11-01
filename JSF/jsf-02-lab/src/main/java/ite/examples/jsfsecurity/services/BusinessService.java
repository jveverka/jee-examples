package ite.examples.jsfsecurity.services;

import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

@Stateless
public class BusinessService {
	
	private static final Logger logger = Logger.getLogger(BusinessService.class.getName());
	
	@Resource
	private SessionContext ctx;
	
	public void doAuthorizedAction() {
		logger.info("doAuthorizedAction ...");
		if (ctx.isCallerInRole("guest") || ctx.isCallerInRole("admin")) {
			logger.info("AUTH OK for user: " + ctx.getCallerPrincipal().getName() + " !");
		} else {
			logger.info("AUTH ERROR !");
		}
	}

}
