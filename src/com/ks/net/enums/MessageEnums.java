package com.ks.net.enums;

public class MessageEnums {

	public enum MessageType {
		MOUSE_RIGHT_CLICK {
			public byte getValue() {
				return (byte) 0x36;
			}
		},
		COMPLETE {
			public byte getValue() {
				return (byte) 0x01;
			}
		};
		public abstract byte getValue();

		public static MessageType getMessageType(byte value) {
			MessageType ret = null;
			switch (value) {
			case 0x00:
				ret = MessageType.MOUSE_RIGHT_CLICK;
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

		public static SpecialKeys getSpecialKeys(byte value) {
			SpecialKeys ret = null;
			switch (value) {
			case 0x00:
				ret = SpecialKeys.BLOCK;
				break;
			case 0x01:
				ret = SpecialKeys.COMPLETE;
				break;
			default:
				break;
			}
			return ret;
		}
	}

	public static final String UDPSCANMESSAGE = "哥们，在不在啊？";
    public static final String UDPSCANRETURN = "在啊，大哥，咋啦？";
	public static final String NETSEPARATOR="<##>";//消息分隔符
	public static final String UDPSEPARATOR="-->>";//消息分隔符
	public static final String FINDNOSERVER="find no server online";
	


}
