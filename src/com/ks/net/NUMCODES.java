package com.ks.net;

public class NUMCODES {
public static enum NETSTATE{
	CONNECTING(0x01),
	CONNECTOK(0x02),
	CONNECTFAILE(0x03),
	CONNECTTIMEOUT(0x04),
	UDPSCANOK(0x05),
	UDPSCANFAIL(0x06),
	SERVERMESSAGE(0x07),
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
			case 0x04:
				ret=CONNECTTIMEOUT;
				break;
			case 0x05:
				ret=UDPSCANOK;
				break;
			case 0x06:
				ret=UDPSCANFAIL;
				break;
			case 0x07:
				ret=SERVERMESSAGE;
				break;
			default:
				ret=WRONGCODE;
				break;
		}
		return ret;
	}
}
}
