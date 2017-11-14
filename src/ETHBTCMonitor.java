import javax.websocket.ClientEndpoint;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@ClientEndpoint
public class ETHBTCMonitor {
	//only watches this pair and updates relevant values to this pair
    @OnOpen
    public void onOpen(Session session) {
    }

    @OnMessage
    public void onMessage(String message) throws JSONException {

        JSONObject jso = new JSONObject(message);
        JSONArray bids = (JSONArray) jso.get("b");
        JSONArray asks = (JSONArray) jso.get("a");
        long timeStamp = (Long) jso.get("E");
        if(bids.length() > 0){
        	float bidPrice = Float.valueOf((String) ((JSONArray) bids.get(0)).get(0));
        	float bidVol = Float.valueOf((String) ((JSONArray) bids.get(0)).get(1));
        	if (bidVol > 0){
        		Storage.ETHBTCbid = bidPrice;
        		Storage.ETHBTCbidVol = bidVol;
        		Utils.checkRoutes(timeStamp);
        	}
        	
        }
        if (asks.length() > 0){
        	float askPrice = Float.valueOf((String) ((JSONArray) asks.get(0)).get(0));
        	float askVol = Float.valueOf((String) ((JSONArray) asks.get(0)).get(1));
        	if (askVol > 0){
        		Storage.ETHBTCask = askPrice;
        		Storage.ETHBTCaskVol = askVol;
        		Utils.checkRoutes(timeStamp);
        	}
        }

    }

    @OnError
    public void onError(Throwable t) {
        t.printStackTrace();
        Main.messageLatch.countDown();
    }
}
