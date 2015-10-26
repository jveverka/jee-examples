package ite.examples.services.data;

import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

import ite.examples.services.SimpleRowHolder;
import ite.examples.services.Utils;

@Stateful
@SessionScoped
public class UserSessionService {

	private static final Logger logger = Logger.getLogger(UserSessionService.class.getName());
	
	private List<SimpleRowHolder> data;
	
	@Inject
	private DataCacheService dataService;

	@PostConstruct
	private void init() {
		logger.info("init ...");
		data = dataService.getDataCopy();
	}
	
	public List<SimpleRowHolder> getData() {
		return Utils.getDataCopy(data);
	}

	public String getDataAt(int rowId, int colId) {
		SimpleRowHolder row = data.get(rowId);
		return row.getCell(colId);
	}

	public boolean updateData(int row, int col, String text) {
		if (row < data.size()) {
			SimpleRowHolder rowData = data.get(row);
			if (col < rowData.getSize()) {
				rowData.updateValue(col, text);
				return true;
			}
		}
		return false;
	}
	
	@PreDestroy
	private void deinit() {
		logger.info("deinit ...");
	}
	
}
