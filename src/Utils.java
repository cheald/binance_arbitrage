import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
	
	public static enum Trade {
		BID, ASK
	}
	
	static void checkRoutes(){
		//move into multiple threads?
		//maybe build routes with pathfinding algo if more than 3
		checkRoute("ETH", Storage.ETHBTCbid, Trade.BID, Storage.ETHBTCbidVol, 
				"BTC", Storage.BNBBTCask, Trade.ASK, Storage.BNBBTCaskVol, 
				"BNB", Storage.BNBETHbid, Trade.BID, Storage.BNBETHbidVol);
		
		checkRoute("ETH", Storage.BNBETHask, Trade.ASK, Storage.BNBETHaskVol, 
				"BNB", Storage.BNBBTCbid, Trade.BID, Storage.BNBBTCbidVol, 
				"BTC", Storage.ETHBTCask, Trade.ASK, Storage.ETHBTCaskVol);
		
		checkRoute("BTC", Storage.BNBBTCask, Trade.ASK, Storage.BNBBTCaskVol, 
				"BNB", Storage.BNBETHbid, Trade.BID, Storage.BNBETHbidVol, 
				"ETH", Storage.ETHBTCbid, Trade.BID, Storage.ETHBTCbidVol);
		
		checkRoute("BTC", Storage.ETHBTCask, Trade.ASK, Storage.ETHBTCaskVol, 
				"ETH", Storage.BNBETHask, Trade.ASK, Storage.BNBETHaskVol, 
				"BNB", Storage.BNBBTCbid, Trade.BID, Storage.BNBBTCbidVol);
		
		checkRoute("BNB", Storage.BNBETHbid, Trade.BID, Storage.BNBETHbidVol, 
				"ETH", Storage.ETHBTCbid, Trade.BID, Storage.ETHBTCbidVol, 
				"BTC", Storage.BNBBTCask, Trade.ASK, Storage.BNBBTCaskVol);
		
		checkRoute("BNB", Storage.BNBBTCbid, Trade.BID, Storage.BNBBTCbidVol, 
				"BTC", Storage.ETHBTCask, Trade.ASK, Storage.ETHBTCaskVol, 
				"ETH", Storage.BNBETHask, Trade.ASK, Storage.BNBETHask);
		
		System.out.println("---");
	}

	static synchronized void checkRoute(String A, float rate1, Utils.Trade type1, float vol1, 
			String B, float rate2, Utils.Trade type2, float vol2, 
			String C, float rate3, Utils.Trade type3, float vol3){
		
		//TODO: see if bellman ford approach is faster/cleaner
		
		if (Storage.ETHBTCbid != 0.0f && Storage.BNBBTCask != 0.0f && Storage.BNBETHbid != 0.0f){
			//calculate through the trade, diminish previous trade volumes if volume bottlenecks are found
			//only do this calculation if the 1.0f test works, saving processing power
			float amountA = (type1.equals(Trade.BID) ? vol1 : vol1 * rate1);
			
			float amountB = (type1.equals(Trade.BID) ? amountA * rate1 : vol1);

			//find a ratio between possible trade size for next step and what current amount is
			float volRatioBC = (type2.equals(Trade.ASK) ? vol2 * rate2 : vol2) / amountB;

			float amountC;
			if (volRatioBC < 1){
				//reduce the size of the entire trade based on volume bottleneck
				amountA *= volRatioBC;
				amountB *= volRatioBC;
				amountC = (type2.equals(Trade.BID) ? vol2 * rate2 : vol2);
			} else {
				amountC = (type2.equals(Trade.BID) ? amountB * rate2 : amountB / rate2);
			}
			
			float volRatioCA = (type3.equals(Trade.ASK) ? vol3 * rate3 : vol3) / amountC;
			
			float amountAFinal;
			if (volRatioCA < 1){
				amountA *= volRatioCA;
				amountB *= volRatioCA;
				amountC *= volRatioCA;
				amountAFinal = (type3.equals(Trade.BID) ? vol3 * rate3 : vol3);
			} else {
				amountAFinal = (type3.equals(Trade.BID) ? amountC * rate3 : amountC / rate3);
			}
			
			if (amountAFinal > amountA){
				System.out.println("Arbitrage opportunity: " + amountA + " to " + amountAFinal + " in " + A);
				System.out.println("Profit minus fees: " + (amountAFinal - amountA));
				System.out.println("Trade1 : " + A + " to " + B + " @ " + rate1);
				System.out.println("Trade2 : " + B + " to " + C + " @ " + rate2);
				System.out.println("Trade3 : " + C + " to " + A + " @ " + rate3);

				SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm:ss z");
				System.out.println("Discovered at " + sdf.format( new Date(Storage.lastUpdate * 1000L)) + "\n");
				
				//for testing
//				System.out.println("BNBBTCbid" + Storage.BNBBTCbid);
//				System.out.println("BNBBTCbidVol" + Storage.BNBBTCbidVol);
//				System.out.println("BNBBTCask" + Storage.BNBBTCask);
//				System.out.println("BNBBTCaskVol" + Storage.BNBBTCaskVol);
//				
//				System.out.println("BNBETHbid" + Storage.BNBETHbid);
//				System.out.println("BNBETHbidVol" + Storage.BNBETHbidVol);
//				System.out.println("BNBETHask" + Storage.BNBETHask);
//				System.out.println("BNBETHaskVol" + Storage.BNBETHaskVol);
//				
//				System.out.println("ETHBTCbid" + Storage.ETHBTCbid);
//				System.out.println("ETHBTCbidVol" + Storage.ETHBTCbidVol);
//				System.out.println("ETHBTCask" + Storage.ETHBTCask);
//				System.out.println("ETHBTCaskVol" + Storage.ETHBTCaskVol);
			} else {
				System.out.println(A + "->" + B + "->" + C + "->" + A + " yield:" + (amountAFinal / amountA));
			}
			System.out.println("-");
			

		} 	
		
	}
	
}
