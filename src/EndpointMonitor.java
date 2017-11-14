
import javax.websocket.ClientEndpoint;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@ClientEndpoint
public class EndpointMonitor {
    @OnOpen
    public void onOpen(Session session) {
    }

    @OnMessage
    public void onMessage(String message) throws JSONException {
        System.out.println("Got: " + message);
    }

    @OnError
    public void onError(Throwable t) {
        t.printStackTrace();
    }
}