
public class Storage {

	static volatile long lastUpdate; //last time data was changed
	
	//BNB/BTC
	static volatile float BNBBTCbid;
	static volatile float BNBBTCbidVol;
	
	static volatile float BNBBTCask;
	static volatile float BNBBTCaskVol;
	
	//BNB/ETH
	static volatile float BNBETHbid;	
	static volatile float BNBETHbidVol;
	
	static volatile float BNBETHask;
	static volatile float BNBETHaskVol;
	
	//ETH/BTC
	static volatile float ETHBTCbid;
	static volatile float ETHBTCbidVol;
	
	static volatile float ETHBTCask;
	static volatile float ETHBTCaskVol;
	
}
