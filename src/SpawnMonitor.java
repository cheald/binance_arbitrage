import java.io.IOException;
import java.net.URI;

import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.WebSocketContainer;

public class SpawnMonitor implements Runnable{

	WebSocketContainer container;
	Class monitorClass;
	URI Uri;
	
	public SpawnMonitor(Class monitorClass, URI Uri){
		this.container = ContainerProvider.getWebSocketContainer();
		this.monitorClass = monitorClass;
		this.Uri = Uri;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			container.connectToServer(this.monitorClass, this.Uri);
		} catch (Exception e) {
			e.printStackTrace(); 
		}
	}

}
