package com.ks.streamline;

import com.ks.net.enums.MessageEnums;

public class SendPacket {
	private MessageEnums.MessageType msgType;
	private MessageEnums.SpecialKeys splKeys;
	private String strValue;
	private int intValue1;
	private int intValue2;

	public MessageEnums.MessageType getMsgType() {
		return msgType;
	}

	public void setMsgType(MessageEnums.MessageType msgType) {
		this.msgType = msgType;
	}

	public MessageEnums.SpecialKeys getSplKeys() {
		return splKeys;
	}

	public void setSplKeys(MessageEnums.SpecialKeys splKeys) {
		this.splKeys = splKeys;
	}

	public String getStrValue() {
		return strValue;
	}

	public void setStrValue(String strValue) {
		this.strValue = strValue;
	}

	public int getIntValue1() {
		return intValue1;
	}

	public void setIntValue1(int intValue1) {
		this.intValue1 = intValue1;
	}

	public int getIntValue2() {
		return intValue2;
	}

	public void setIntValue2(int intValue2) {
		this.intValue2 = intValue2;
	}

}
