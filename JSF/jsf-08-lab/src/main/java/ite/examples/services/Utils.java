package ite.examples.services;

import java.util.ArrayList;
import java.util.List;

public class Utils {

	public static List<SimpleRowHolder> getDataCopy(List<SimpleRowHolder> cleanData) {
		List<SimpleRowHolder> result = new ArrayList<>(cleanData.size());
		for (SimpleRowHolder row: cleanData) {
			result.add(row.getCopy());
		}
		return result;
	}
	
}
