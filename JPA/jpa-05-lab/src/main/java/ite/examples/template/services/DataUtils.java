package ite.examples.template.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class DataUtils {
	
	public static Date createDate(String dateString) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
			Date date = sdf.parse(dateString);
			return date;
		} catch (ParseException e) {
		}
		return null;
	}

}
