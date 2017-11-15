import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
	
	public static enum Trade {
		BID, ASK
	}
	
	static void checkRoutes(long timeStamp){
		//move into multiple threads?
		//maybe build routes with pathfinding algo if more than 3
		
		//store as local vars to "lock in" rates
		float BNBBTCbidLocal = Storage.BNBBTCbid;
		float BNBBTCbidVolLocal = Storage.BNBBTCbidVol;
		float BNBBTCaskLocal = Storage.BNBBTCask;
		float BNBBTCaskVolLocal = Storage.BNBBTCaskVol;
		
		float BNBETHbidLocal = Storage.BNBETHbid;	
		float BNBETHbidVolLocal = Storage.BNBETHbidVol;
		float BNBETHaskLocal = Storage.BNBETHask;
		float BNBETHaskVolLocal = Storage.BNBETHaskVol;
		
		float ETHBTCbidLocal = Storage.ETHBTCbid;
		float ETHBTCbidVolLocal = Storage.ETHBTCbidVol;
		float ETHBTCaskLocal = Storage.ETHBTCask;
		float ETHBTCaskVolLocal = Storage.ETHBTCaskVol;

		
		checkRoute("ETH", ETHBTCbidLocal, Trade.BID, ETHBTCbidVolLocal, 
				"BTC", BNBBTCaskLocal, Trade.ASK, BNBBTCaskVolLocal, 
				"BNB", BNBETHbidLocal, Trade.BID, BNBETHbidVolLocal, timeStamp);
		
		checkRoute("ETH", BNBETHaskLocal, Trade.ASK, BNBETHaskVolLocal, 
				"BNB", BNBBTCbidLocal, Trade.BID, BNBBTCbidVolLocal, 
				"BTC", ETHBTCaskLocal, Trade.ASK, ETHBTCaskVolLocal, timeStamp);
		
		checkRoute("BTC", BNBBTCaskLocal, Trade.ASK, BNBBTCaskVolLocal, 
				"BNB", BNBETHbidLocal, Trade.BID, BNBETHbidVolLocal, 
				"ETH", ETHBTCbidLocal, Trade.BID, ETHBTCbidVolLocal, timeStamp);
		
		checkRoute("BTC", ETHBTCaskLocal, Trade.ASK, ETHBTCaskVolLocal, 
				"ETH", BNBETHaskLocal, Trade.ASK, BNBETHaskVolLocal, 
				"BNB", BNBBTCbidLocal, Trade.BID, BNBBTCbidVolLocal, timeStamp);
		
		checkRoute("BNB", BNBETHbidLocal, Trade.BID, BNBETHbidVolLocal, 
				"ETH", ETHBTCbidLocal, Trade.BID, ETHBTCbidVolLocal, 
				"BTC", BNBBTCaskLocal, Trade.ASK, BNBBTCaskVolLocal, timeStamp);
		
		checkRoute("BNB", BNBBTCbidLocal, Trade.BID, BNBBTCbidVolLocal, 
				"BTC", ETHBTCaskLocal, Trade.ASK, ETHBTCaskVolLocal, 
				"ETH", BNBETHaskLocal, Trade.ASK, BNBETHaskLocal, timeStamp);
	}

	static void checkRoute(String A, float rate1, Utils.Trade type1, float vol1, 
			String B, float rate2, Utils.Trade type2, float vol2, 
			String C, float rate3, Utils.Trade type3, float vol3, 
			long timeStamp){
		
		//TODO: see if bellman ford approach is faster/cleaner
		
		if (rate1 != 0.0f && vol1 != 0.0f && rate2 != 0.0f && vol2 != 0.0f && rate3 != 0.0f && vol3 != 0.0f){ //refactor into one boolean
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
				System.out.println("Profit minus fees: " + (amountAFinal - amountA) + A);
				System.out.println("Trade1 : " + amountA + A + " to " + amountB + B + " @ " + rate1);
				System.out.println("Trade2 : " + amountB + B + " to " + amountC + C + " @ " + rate2);
				System.out.println("Trade3 : " + amountC + C + " to " + amountAFinal + A + " @ " + rate3);

				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
				System.out.println("Discovered at " + sdf.format( new Date(timeStamp)) + "\n");
				
				//for testing differences
				System.out.println("BNBBTCbid" + Storage.BNBBTCbid);
				System.out.println("BNBBTCbidVol" + Storage.BNBBTCbidVol);
				System.out.println("BNBBTCask" + Storage.BNBBTCask);
				System.out.println("BNBBTCaskVol" + Storage.BNBBTCaskVol);
				
				System.out.println("BNBETHbid" + Storage.BNBETHbid);
				System.out.println("BNBETHbidVol" + Storage.BNBETHbidVol);
				System.out.println("BNBETHask" + Storage.BNBETHask);
				System.out.println("BNBETHaskVol" + Storage.BNBETHaskVol);
				
				System.out.println("ETHBTCbid" + Storage.ETHBTCbid);
				System.out.println("ETHBTCbidVol" + Storage.ETHBTCbidVol);
				System.out.println("ETHBTCask" + Storage.ETHBTCask);
				System.out.println("ETHBTCaskVol" + Storage.ETHBTCaskVol);
			} else {
				System.out.println(A + "->" + B + "->" + C + "->" + A + " losing yield:" + (amountAFinal / amountA));
			}
		} 	
		
	}
	
}
