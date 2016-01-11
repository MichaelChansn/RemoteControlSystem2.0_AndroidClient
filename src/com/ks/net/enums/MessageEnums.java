package com.ks.net.enums;

public class MessageEnums {

	public enum MessageType {
		BLOCK {
			public byte getValue() {
				return (byte) 0x00;
			}
		},
		COMPLETE {
			public byte getValue() {
				return (byte) 0x01;
			}
		};
		public abstract byte getValue();

		public static MessageType getBitmapType(byte value) {
			MessageType ret = null;
			switch (value) {
			case 0x00:
				ret = MessageType.BLOCK;
				break;
			case 0x01:
				ret = MessageType.COMPLETE;
				break;
			default:
				break;
			}
			return ret;
		}
	}

	public enum SpecialKeys {
		BLOCK {
			public byte getValue() {
				return (byte) 0x00;
			}
		},
		COMPLETE {
			public byte getValue() {
				return (byte) 0x01;
			}
		};

		public abstract byte getValue();

		public static MessageType getBitmapType(byte value) {
			MessageType ret = null;
			switch (value) {
			case 0x00:
				ret = MessageType.BLOCK;
				break;
			case 0x01:
				ret = MessageType.COMPLETE;
				break;
			default:
				break;
			}
			return ret;
		}
	}

	public static final String UDPSCANMESSAGE = "在不在啊？请回答。。。";
	public static final String NETSEPARATOR="+";//消息分隔符


}
