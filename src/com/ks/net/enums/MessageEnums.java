package com.ks.net.enums;

public class MessageEnums {

	public enum MessageType {
		MOUSE_RIGHT_CLICK {
			public byte getValue() {
				return (byte) 0x36;
			}
		},
		MOUSE_SET {
			public byte getValue() {
				return (byte) 0x3A;
			}
		},
		MOUSE_LEFT_DOUBLE_CLICK{
			public byte getValue() {
				return (byte) 0x35;
			}
		},
		MOUSE_LEFT_CLICK{
			public byte getValue() {
				return (byte) 0x34;
			}
		},
		MOUSE_MOVE{
			public byte getValue() {
				return (byte) 0x39;
			}
		},
		MOUSE_WHEEL{
			public byte getValue() {
				return (byte) 0x38;
			}
		}
		;
		public abstract byte getValue();

		public static MessageType getMessageType(byte value) {
			MessageType ret = null;
			switch (value) {
			case 0x00:
				ret = MessageType.MOUSE_RIGHT_CLICK;
				break;
			case 0x34:
				ret = MessageType.MOUSE_LEFT_CLICK;
				break;
			case 0x3A:
				ret = MessageType.MOUSE_SET;
				break;
			case 0x35:
				ret = MessageType.MOUSE_LEFT_DOUBLE_CLICK;
				break;
			case 0x39:
				ret = MessageType.MOUSE_MOVE;
				break;
			case 0x38:
				ret = MessageType.MOUSE_WHEEL;
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
