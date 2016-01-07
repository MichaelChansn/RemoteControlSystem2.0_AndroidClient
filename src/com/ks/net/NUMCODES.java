package com.ks.net;

public class NUMCODES {
public enum NETSTATE{
	CONNECTING(0x01),
	CONNECTOK(0x02),
	CONNECTFAILE(0x03),
	WRONGCODE(0xffffffff),
	;
	private int code;
	private NETSTATE(int code)
	{
		this.code=code;
	}
	public int getValue()
	{
		return this.code;
	}
	public static NETSTATE getNETSTATEByValue(int value)
	{
		NETSTATE ret=null;
		switch(value)
		{
			case 0x01:
				ret=CONNECTING;
			break;
			case 0x02:
				ret=CONNECTOK;
				break;
			case 0x03:
				ret=CONNECTFAILE;
				break;
			default:
				ret=WRONGCODE;
				break;
		}
		return ret;
	}
}
}
