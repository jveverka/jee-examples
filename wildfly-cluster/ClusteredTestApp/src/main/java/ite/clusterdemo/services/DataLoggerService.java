package ite.clusterdemo.services;

import java.util.Date;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ite.clusterdemo.services.dto.DataLog;
import ite.clusterdemo.services.dto.DataMessage;

@Stateless
public class DataLoggerService {
	
	@PersistenceContext
	private EntityManager em;

	@Asynchronous
	public void logDataMessage(DataMessage message) {
		DataLog dl = new DataLog(message);
		dl.setTimeStamp(new Date());
		em.persist(dl);
	}
	
}
