package itx.hybridapp.javafx.messaging.events;

import itx.hybridapp.javafx.Scenes;

public class SceneLeaveEvent extends AppEvent {
	
	private Scenes sceneId;
	
	private SceneLeaveEvent(Scenes sceneId) {
		this.sceneId = sceneId;
	}
	
	public Scenes getSceneId() {
		return sceneId;
	}
	
	public static Builder newBuilder() {
		return new Builder();
	}

	public static class Builder {
		
		private Scenes sceneId;
		
		public Builder setSceneId(Scenes sceneId) {
			this.sceneId = sceneId;
			return this;
		}
		
		public SceneLeaveEvent build() {
			return new SceneLeaveEvent(sceneId);
		}
		
	}

}
