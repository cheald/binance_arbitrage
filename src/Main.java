import java.net.URI;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.WebSocketContainer;

public class Main {
	
	private static final String URIbnbbtc = "wss://stream.binance.com:9443/ws/bnbbtc@depth@0ms";
	private static final String URIbnbeth = "wss://stream.binance.com:9443/ws/bnbeth@depth@0ms";
	private static final String URIethbtc = "wss://stream.binance.com:9443/ws/ethbtc@depth@0ms";
	
	static CountDownLatch messageLatch = new CountDownLatch(1);

    public static void main(String[] args) {
        try {
        	Thread t1 = new Thread(new SpawnMonitor(BNBBTCMonitor.class, URI.create(URIbnbbtc)));
        	Thread t2 = new Thread(new SpawnMonitor(BNBETHMonitor.class, URI.create(URIbnbeth)));
        	Thread t3 = new Thread(new SpawnMonitor(ETHBTCMonitor.class, URI.create(URIethbtc)));

        	t1.start();
        	t2.start();
        	t3.start();
            
        	messageLatch.await();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
}