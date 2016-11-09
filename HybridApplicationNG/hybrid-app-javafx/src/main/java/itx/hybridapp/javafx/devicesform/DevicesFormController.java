package itx.hybridapp.javafx.devicesform;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import itx.hybridapp.common.protocols.CommonAccessProtocol.WrapperMessage;
import itx.hybridapp.common.protocols.DeviceServiceProtocol.DeviceListRequest;
import itx.hybridapp.common.protocols.UserAccessProtocol.TopicSubscribe;
import itx.hybridapp.common.protocols.UserAccessProtocol.TopicUnsubscribe;
import itx.hybridapp.javafx.Main;
import itx.hybridapp.javafx.Scenes;
import itx.hybridapp.javafx.messaging.Messaging;
import itx.hybridapp.javafx.messaging.events.DeviceInfoListChangedEvent;
import itx.hybridapp.javafx.messaging.events.DeviceInfoListEvent;
import itx.hybridapp.javafx.messaging.events.SceneEnterEvent;
import itx.hybridapp.javafx.messaging.events.SceneLeaveEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import net.engio.mbassy.listener.Handler;

public class DevicesFormController implements Initializable {
	
	private static final Logger logger = Logger.getLogger(DevicesFormController.class.getName());
	private static DevicesFormController SELF;
	private static final String DEVICES_TOPIC_ID = "/devices";
	
	@FXML private Pane devicesPane;
	@FXML private TableView<DeviceTableInfo> devicesTable;
	@FXML private TableColumn<DeviceTableInfo, String> deviceIdCol;
	@FXML private TableColumn<DeviceTableInfo, String> wsSessionIdCol;
	@FXML private TableColumn<DeviceTableInfo, String> connectedCol;
	
	public DevicesFormController() {
		SELF = this;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		logger.info("initialize ...");
		SELF = this;
		deviceIdCol.setCellValueFactory(new PropertyValueFactory<DeviceTableInfo, String>("deviceId"));
		wsSessionIdCol.setCellValueFactory(new PropertyValueFactory<DeviceTableInfo, String>("wsSessionId"));
		connectedCol.setCellValueFactory(new PropertyValueFactory<DeviceTableInfo, String>("connected"));
		devicesTable.setOnMousePressed(new TableEventHandler());
		Messaging.getInstance().subscribe(this);
	}
	
	@Handler
	public void onSceneEnter(SceneEnterEvent event) {
		if (Scenes.DEVICES.equals(event.getSceneId())) {
			logger.info("onSceneEnter: DEVICES");
			TopicSubscribe ts = TopicSubscribe.newBuilder().setTopicId(DEVICES_TOPIC_ID).build();
			Main.getInstance().sendMessage(WrapperMessage.newBuilder().setTopicSubscribe(ts).build());
			DeviceListRequest dlr = DeviceListRequest.newBuilder().build();
			Main.getInstance().sendMessage(WrapperMessage.newBuilder().setDeviceListRequest(dlr).build());
		}
	}
	
	@Handler 
	public void onSceneLeave(SceneLeaveEvent event) {
		if (Scenes.DEVICES.equals(event.getSceneId())) {
			logger.info("onSceneLeave: DEVICES");
			TopicUnsubscribe tu = TopicUnsubscribe.newBuilder().setTopicId(DEVICES_TOPIC_ID).build();
			Main.getInstance().sendMessage(WrapperMessage.newBuilder().setTopicUnsubscribe(tu).build());
		}
	}
	
	@Handler
	public void onDeviceInfoList(DeviceInfoListEvent event) {
		logger.info("onDeviceInfoList: " + event.getDeviceInfoList().size());
		devicesTable.getItems().clear();
		List<DeviceTableInfo> dtil = new ArrayList<>();
		event.getDeviceInfoList().forEach(i -> {
			DeviceTableInfo dti = new DeviceTableInfo(i.getDeviceId(), i.getSessionId(), Long.toString(i.getConnected()));
			dtil.add(dti);
		});
		devicesTable.getItems().setAll(dtil);
		devicesTable.refresh();
	}
	
	@Handler
	public void onDeviceInfoListChanged(DeviceInfoListChangedEvent event) {
		DeviceListRequest dlr = DeviceListRequest.newBuilder().build();
		Main.getInstance().sendMessage(WrapperMessage.newBuilder().setDeviceListRequest(dlr).build());
	}

}
