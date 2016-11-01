package itx.hybridapp.javafx.messaging.events;

import itx.hybridapp.javafx.Scenes;

public class SceneEnterEvent extends AppEvent {

	private Scenes sceneId;
	private Object sceneContext;
	
	private SceneEnterEvent(Scenes sceneId, Object sceneContext) {
		this.sceneId = sceneId;
		this.sceneContext = sceneContext;
	}
	
	public Scenes getSceneId() {
		return sceneId;
	}
	
	public Object getSceneContext() {
		return sceneContext;
	}
	
	public static Builder newBuilder() {
		return new Builder();
	}

	public static class Builder {
		
		private Scenes sceneId;
		private Object sceneContext;
		
		public Builder setSceneId(Scenes sceneId) {
			this.sceneId = sceneId;
			return this;
		}
		
		public Builder setSceneContext(Object context) {
			this.sceneContext = context;
			return this;
		}
		
		public SceneEnterEvent build() {
			return new SceneEnterEvent(sceneId, sceneContext);
		}
		
	}
}
