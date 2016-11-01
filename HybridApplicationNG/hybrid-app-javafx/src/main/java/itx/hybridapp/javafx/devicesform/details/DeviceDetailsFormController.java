package itx.hybridapp.javafx.devicesform.details;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import itx.hybridapp.common.protocols.CommonAccessProtocol.WrapperMessage;
import itx.hybridapp.common.protocols.DeviceServiceProtocol.ButtonEvent;
import itx.hybridapp.common.protocols.DeviceServiceProtocol.ControlOutputEvent;
import itx.hybridapp.common.protocols.DeviceServiceProtocol.DeviceEvent;
import itx.hybridapp.common.protocols.DeviceServiceProtocol.GetStatusRequest;
import itx.hybridapp.common.protocols.DeviceServiceProtocol.SensorEvent;
import itx.hybridapp.common.protocols.DeviceServiceProtocol.TimeSeriesDataRequest;
import itx.hybridapp.common.protocols.UserAccessProtocol.TopicSubscribe;
import itx.hybridapp.common.protocols.UserAccessProtocol.TopicUnsubscribe;
import itx.hybridapp.javafx.Scenes;
import itx.hybridapp.javafx.messaging.Messaging;
import itx.hybridapp.javafx.messaging.events.DeviceDataEvent;
import itx.hybridapp.javafx.messaging.events.DeviceStatusEvent;
import itx.hybridapp.javafx.messaging.events.SceneEnterEvent;
import itx.hybridapp.javafx.messaging.events.SceneLeaveEvent;
import itx.hybridapp.javafx.messaging.events.TimeSeriesDataEvent;
import itx.hybridapp.javafx.websockets.WSService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import net.engio.mbassy.listener.Handler;

public class DeviceDetailsFormController implements Initializable {
	
	private static final Logger logger = Logger.getLogger(DeviceDetailsFormController.class.getName());
	private static DeviceDetailsFormController SELF;
	private static final String NA = "NA";
	private static final String DEVICES_TOPIC_BASE = "/devices/";
	
	@FXML private Label deviceIdLabel;
	@FXML private Button closeDeviceInfoButton;
	@FXML private Label temperatureIdLabel;
	@FXML private Label humidityIdLabel;
	@FXML private Label pressureIdLabel;
	@FXML private Label control0Value;
	@FXML private Label control1Value;
	@FXML private Label button0Value;
	@FXML private Label button1Value;
	@FXML private Button control0Button;
	@FXML private Button control1Button;
	//@FXML private LineChart<Number, Number> timeChart;

	private String deviceId;
	private boolean[] controls;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		logger.info("initialize ...");
		SELF = this;
		controls = new boolean[4];
		closeDeviceInfoButton.setOnAction(new CloseButtonHandler());
		control0Button.setOnAction(new SetControl0ButtonHandler(this, 0));
		control1Button.setOnAction(new SetControl1ButtonHandler(this, 1));
		Messaging.getInstance().subscribe(this);
	}
	
	@Handler
	public void onSceneEnter(SceneEnterEvent event) {
		if (Scenes.DEVICE_DETAILS.equals(event.getSceneId())) {
			logger.info("onSceneEnter: DEVICE_DETAILS " + event.getSceneContext());
			deviceId = (String)event.getSceneContext();
			deviceIdLabel.setText(deviceId);
			TopicSubscribe ts = TopicSubscribe.newBuilder().setTopicId(DEVICES_TOPIC_BASE + deviceId).build();
			WSService.getInstance().sendMessage(WrapperMessage.newBuilder().setTopicSubscribe(ts).build());
			GetStatusRequest getStatus = GetStatusRequest.newBuilder().setDeviceId(deviceId).build();
			WSService.getInstance().sendMessage(WrapperMessage.newBuilder().setGetStatusRequest(getStatus).build());
			TimeSeriesDataRequest tsdr = TimeSeriesDataRequest.newBuilder().setDeviceId(deviceId).build();
			WSService.getInstance().sendMessage(WrapperMessage.newBuilder().setTimeSeriesDataRequest(tsdr).build());
		}
	}

	@Handler
	public void onSceneLeave(SceneLeaveEvent event) {
		if (Scenes.DEVICE_DETAILS.equals(event.getSceneId())) {
			logger.info("onSceneLeave: DEVICE_DETAILS ");
			deviceIdLabel.setText(NA);
			TopicUnsubscribe tu = TopicUnsubscribe.newBuilder().setTopicId(DEVICES_TOPIC_BASE + deviceId).build();
			WSService.getInstance().sendMessage(WrapperMessage.newBuilder().setTopicUnsubscribe(tu).build());
		}
	}
	
	@Handler
	public void onDeviceStatus(DeviceStatusEvent event) {
		logger.info("onDeviceStatus:");
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				temperatureIdLabel.setText(String.format("%.2f", event.getData().getTemperature()));
				humidityIdLabel.setText(String.format("%.2f", event.getData().getRelativeHumidity()));
				pressureIdLabel.setText(String.format("%.2f", event.getData().getPressure()));
				controls[0] = event.getData().getControlOutputsList().get(0);
				controls[1] = event.getData().getControlOutputsList().get(1);
				showBooleanState(control0Value, controls[0]);
				showBooleanState(control1Value, controls[1]);
			}
			
		});
	}

	@Handler
	public void onDeviceData(DeviceDataEvent event) {
		logger.info("onDeviceData:");
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				int ordinal = event.getData().getMsgCase().getNumber();
				if (DeviceEvent.SENSOREVENT_FIELD_NUMBER == ordinal) {
					SensorEvent se = event.getData().getSensorEvent();
					temperatureIdLabel.setText(String.format("%.2f", se.getTemperature()));
					humidityIdLabel.setText(String.format("%.2f", se.getRelativeHumidity()));
					pressureIdLabel.setText(String.format("%.2f", se.getPressure()));
					//TO-DO - implement time series chart live update
				} else if (DeviceEvent.CONTROLOUTPUTEVENT_FIELD_NUMBER == ordinal) {
					ControlOutputEvent ce = event.getData().getControlOutputEvent();
					if (ce.getPinId() == 0) {
						showBooleanState(control0Value, ce.getState());
						controls[0] = ce.getState();
					}
					if (ce.getPinId() == 1) {
						showBooleanState(control1Value, ce.getState());
						controls[1] = ce.getState();
					}
				} else if (DeviceEvent.BUTTONEVENT_FIELD_NUMBER == ordinal) {
					ButtonEvent be = event.getData().getButtonEvent();
					if (be.getButtonId() == 0) {
						showBooleanState(button0Value, be.getState());
					}
					if (be.getButtonId() == 1) {
						showBooleanState(button1Value, be.getState());
					}
				}
			}
		});
	}
	
	@Handler
	public void onTimeSeriesData(TimeSeriesDataEvent event) {
		logger.info("onTimeSeriesData:");
		//TO-DO - implement time series chart
	}
	
	private void showBooleanState(Label label, boolean state) {
		if (state) {
			label.setText(" ON  ");
			label.getStyleClass().clear();
			label.getStyleClass().add("control-on");
		} else {
			label.setText(" OFF ");
			label.getStyleClass().clear();
			label.getStyleClass().add("control-off");
		}
	}
	
	protected boolean getControlState(int pinId) {
		return controls[pinId];
	}
	
	protected String getDeviceId() {
		return deviceId;
	}

}
