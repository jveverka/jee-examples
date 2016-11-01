package ite.examples.jsf.realtime.services.push;

import java.io.StringReader;
import java.io.StringWriter;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonWriter;

import ite.examples.jsf.realtime.services.DataSet;
import ite.examples.jsf.realtime.services.DataSetType;

import org.primefaces.json.JSONException;
import org.primefaces.json.JSONObject;
import org.primefaces.json.JSONArray;

public class DataUtils {
	
	private static final String eventTypeJS = "eventType";
	private static final String dataSetJS = "dataSet";
	private static final String lastRandomJS = "lastRandom";
	private static final String counterJS = "counter";
	private static final String typeJS = "type";
	private static final String maxcounterJS = "maxcounter";
	private static final String chartDataJS = "chartData";
	
	public static String jsonEncodePF(EventMessage message) throws JSONException {
		JSONObject jsonObj = new JSONObject();
		jsonObj.put(eventTypeJS, message.getEventType());
		JSONObject jsonDataSet = new JSONObject();
		jsonObj.put(dataSetJS, jsonDataSet);
		jsonDataSet.put(lastRandomJS, message.getDataSet().getLastRandom());
		jsonDataSet.put(counterJS, message.getDataSet().getCounter());
		jsonDataSet.put(typeJS, message.getDataSet().getType().name());
		jsonDataSet.put(maxcounterJS, message.getDataSet().getMaxcounter());
		JSONArray dataSet = new JSONArray();
		for (int i=0; i<message.getDataSet().getChartData().length; i++) {
			JSONArray dataSetValue = new JSONArray();
			dataSetValue.put(0,message.getDataSet().getChartData()[i][0]);
			dataSetValue.put(1,message.getDataSet().getChartData()[i][1]);
			dataSet.put(i, dataSetValue);
		}
		jsonDataSet.put(chartDataJS, dataSet);
        return jsonObj.toString();
	}

	public static EventMessage jsonDecodePF(String jsonString) throws JSONException {
		JSONObject jsonObj = new JSONObject(jsonString);
		String eventType = jsonObj.getString(eventTypeJS);
		DataSet ds = new DataSet();
		JSONObject jsonDataSet = jsonObj.getJSONObject(dataSetJS);
		ds.setCounter(jsonDataSet.getInt(counterJS));
		ds.setLastRandom(jsonDataSet.getInt(lastRandomJS));
		ds.setMaxcounter(jsonDataSet.getInt(maxcounterJS));
		ds.setType(DataSetType.valueOf(jsonDataSet.getString(typeJS)));
		JSONArray chartData = jsonDataSet.getJSONArray(chartDataJS);
		ds.setChartData(new int[chartData.length()][2]);
		for (int i=0; i<chartData.length(); i++) {
			JSONArray chartDataValue = chartData.getJSONArray(i);
			ds.getChartData()[i][0] = chartDataValue.getInt(0);
			ds.getChartData()[i][1] = chartDataValue.getInt(1);
		}
		return new EventMessage(eventType, ds);
	}

	public static String jsonEncode(EventMessage message) {
		StringWriter writer = new StringWriter();
		JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
		jsonBuilder.add(eventTypeJS, message.getEventType());
		JsonObjectBuilder dataSetBuilder = Json.createObjectBuilder();
		dataSetBuilder.add(lastRandomJS, message.getDataSet().getLastRandom());
		dataSetBuilder.add(counterJS, message.getDataSet().getCounter());
		dataSetBuilder.add(typeJS, message.getDataSet().getType().name());
		dataSetBuilder.add(maxcounterJS, message.getDataSet().getMaxcounter());
		JsonArrayBuilder chartData = Json.createArrayBuilder();
		for (int i=0; i<message.getDataSet().getChartData().length; i++) {
			JsonArrayBuilder chartDataValue = Json.createArrayBuilder();
			chartDataValue.add(message.getDataSet().getChartData()[i][0]);
			chartDataValue.add(message.getDataSet().getChartData()[i][1]);
			chartData.add(chartDataValue.build());
		}
		dataSetBuilder.add(chartDataJS, chartData.build());
		jsonBuilder.add(dataSetJS, dataSetBuilder.build());
		JsonWriter jsonWriter = Json.createWriter(writer);
		jsonWriter.writeObject(jsonBuilder.build());
		return writer.toString();
	}

	public static EventMessage jsonDecode(String jsonString) {
		JsonReader jsonReader = Json.createReader(new StringReader(jsonString));
		JsonObject jsonObject = jsonReader.readObject();
		String eventType = jsonObject.getString(eventTypeJS);
		JsonObject dataSetObj = jsonObject.getJsonObject(dataSetJS);
		DataSet ds = new DataSet();
		ds.setCounter(dataSetObj.getInt(counterJS));
		ds.setLastRandom(dataSetObj.getInt(lastRandomJS));
		ds.setMaxcounter(dataSetObj.getInt(maxcounterJS));
		ds.setType(DataSetType.valueOf(dataSetObj.getString(typeJS)));
		JsonArray chartData = dataSetObj.getJsonArray(chartDataJS);
		ds.setChartData(new int[chartData.size()][2]);
		for (int i=0; i<chartData.size(); i++) {
			JsonArray chartDataValue = chartData.getJsonArray(i);
			ds.getChartData()[i][0] = chartDataValue.getInt(0);
			ds.getChartData()[i][1] = chartDataValue.getInt(1);
		}
		return new EventMessage(eventType, ds);		
	}

	public static void main(String[] args) throws JSONException {
		DataSet ds = new DataSet();
		ds.setCounter(11);
		ds.setLastRandom(111);
		ds.setMaxcounter(44);
		ds.setType(DataSetType.UPDATE);
		ds.setChartData(new int[][] { {0,0}, {1,1}, {2,2}, {3,3}, {4,4}, {5,5} } );
		EventMessage em = new EventMessage("xxx", ds);
		String emJsonString = jsonEncodePF(em);
		System.out.println("ENCODED: " + emJsonString);
		EventMessage emDec = jsonDecodePF(emJsonString);
		String emDecoded = jsonEncodePF(emDec);
		System.out.println("DECODED: " + emDecoded);
		
	}
	
}
