package ite.example.services;

import java.util.Date;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ite.example.services.entities.DataRecord;

@Stateless
public class EntityService {
	
	@PersistenceContext
	private EntityManager em;
	
	public void saveDataRecord(long timestamp, String message) {
		DataRecord record = new DataRecord();
		record.setTimeStamp(new Date(timestamp));
		record.setMessage(message);
		em.persist(record);
	}

}
