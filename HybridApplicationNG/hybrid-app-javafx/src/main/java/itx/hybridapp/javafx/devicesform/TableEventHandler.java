package itx.hybridapp.javafx.devicesform;

import java.util.logging.Level;
import java.util.logging.Logger;

import itx.hybridapp.javafx.Main;
import itx.hybridapp.javafx.Scenes;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.TableRow;
import javafx.scene.input.MouseEvent;

public class TableEventHandler implements EventHandler<MouseEvent> {
	
	private static final Logger logger = Logger.getLogger(TableEventHandler.class.getName());

	@Override
	public void handle(MouseEvent event) {
		try {
			Node node = ((Node) event.getTarget()).getParent();
			TableRow<DeviceTableInfo> row;
			DeviceTableInfo dti;
			if (node instanceof TableRow<?>) {
				row = (TableRow<DeviceTableInfo>) node;
			} else {
				row = (TableRow<DeviceTableInfo>) node.getParent();
			}
			dti = row.getItem();
			logger.info("selected device Id: " + dti.getDeviceId());
			Main.getInstance().switchScenes(Scenes.DEVICE_DETAILS, dti.getDeviceId());
		} catch (Exception e) {
			logger.log(Level.SEVERE, "TableEventHandler Exception", e.getMessage());
		}
		
	}

}
