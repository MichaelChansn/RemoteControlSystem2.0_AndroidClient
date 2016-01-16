package com.ks.net.enums;

public class MessageEnums {

	public enum MessageType {
		HOST_NANME{
			public byte getValue() {
				return (byte) 0x00;
			}
		},
		EXIT{
			public byte getValue() {
				return (byte) 0x02;
			}
		},
		START_PIC {
			public byte getValue() {
				return (byte) 0x10;
			}
		},
		STOP_PIC {
			public byte getValue() {
				return (byte) 0x11;
			}
		},
		MOUSE_LEFT_DOWN {
			public byte getValue() {
				return (byte) 0x30;
			}
		},
		MOUSE_LEFT_UP {
			public byte getValue() {
				return (byte) 0x31;
			}
		},
		MOUSE_RIGHT_DOWN {
			public byte getValue() {
				return (byte) 0x32;
			}
		},
		MOUSE_RIGHT_UP {
			public byte getValue() {
				return (byte) 0x33;
			}
		},
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
		MOUSE_LEFT_DOUBLE_CLICK {
			public byte getValue() {
				return (byte) 0x35;
			}
		},
		MOUSE_LEFT_CLICK {
			public byte getValue() {
				return (byte) 0x34;
			}
		},
		MOUSE_MOVE {
			public byte getValue() {
				return (byte) 0x39;
			}
		},
		MOUSE_WHEEL {
			public byte getValue() {
				return (byte) 0x38;
			}
		},
		KEY_DOWN{
			public byte getValue() {
				return (byte) 0x20;
			}
		},
		KEY_UP{
			public byte getValue() {
				return (byte) 0x21;
			}
		},
		TEXT{
			public byte getValue() {
				return (byte) 0x70;
			}
		},

        /**control PC funs*/
        FUN_SHUTDOWN{
			public byte getValue() {
				return (byte) 0x60;
			}
		},
        FUN_RESTART{
			public byte getValue() {
				return (byte) 0x61;
			}
		},
        FUN_MANAGER{
			public byte getValue() {
				return (byte) 0x62;
			}
		},
        FUN_SLEEP{
			public byte getValue() {
				return (byte) 0x63;
			}
		},
        FUN_LOGOUT{
			public byte getValue() {
				return (byte) 0x64;
			}
		},
        FUN_LOCK{
			public byte getValue() {
				return (byte) 0x65;
			}
		},
        FUN_SHUTDOWN_TIME{
			public byte getValue() {
				return (byte) 0x66;
			}
		},
        FUN_SHUTDOWN_CANCEL{
			public byte getValue() {
				return (byte) 0x67;
			}
		},
        FUN_SHOW_DESKTOP{
			public byte getValue() {
				return (byte) 0x68;
			}
		}
		 
		;
		public abstract byte getValue();

		public static MessageType getMessageType(byte value) {
			MessageType ret = null;
			switch (value) {
			case 0x00:
				ret = MessageType.HOST_NANME;
				break;
			case 0x02:
				ret = MessageType.EXIT;
				break;
			case 0x36:
				ret = MessageType.MOUSE_RIGHT_CLICK;
				break;
			case 0x10:
				ret = MessageType.START_PIC;
				break;
			case 0x11:
				ret = MessageType.STOP_PIC;
				break;
			case 0x30:
				ret = MessageType.MOUSE_LEFT_DOWN;
				break;
			case 0x31:
				ret = MessageType.MOUSE_LEFT_UP;
				break;
			case 0x32:
				ret = MessageType.MOUSE_RIGHT_DOWN;
				break;
			case 0x33:
				ret = MessageType.MOUSE_RIGHT_UP;
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
			case 0x20:
				ret = MessageType.KEY_DOWN;
				break;
			case 0x21:
				ret = MessageType.KEY_UP;
				break;
			case 0x60:
				ret = MessageType.FUN_SHUTDOWN;
				break;
			case 0x61:
				ret = MessageType.FUN_RESTART;
				break;
			case 0x62:
				ret = MessageType.FUN_MANAGER;
				break;
			case 0x63:
				ret = MessageType.FUN_SLEEP;
				break;
			case 0x64:
				ret = MessageType.FUN_LOGOUT;
				break;
			case 0x65:
				ret = MessageType.FUN_LOCK;
				break;
			case 0x66:
				ret = MessageType.FUN_SHUTDOWN_TIME;
				break;
			case 0x67:
				ret = MessageType.FUN_SHUTDOWN_CANCEL;
				break;
			case 0x68:
				ret = MessageType.FUN_SHOW_DESKTOP;
				break;
			case 0x70:
				ret = MessageType.TEXT;
				break;
			
			default:
				break;
			}
			return ret;
		}
	}

	public enum SpecialKeys {
		NONE{
			public byte getValue() {
				return (byte) 0xFF;
			}
		},

		ENTER {
			public byte getValue() {
				return (byte) 0x00;
			}
		},
		BACKSPACE {
			public byte getValue() {
				return (byte) 0x01;
			}
		},
		SPACE {
			public byte getValue() {
				return (byte) 0x02;
			}
		},
		ESC {
			public byte getValue() {
				return (byte) 0x03;
			}
		},
		SHIFT {
			public byte getValue() {
				return (byte) 0x04;
			}
		},
		CTRL {
			public byte getValue() {
				return (byte) 0x05;
			}
		},
		ALT {
			public byte getValue() {
				return (byte) 0x06;
			}
		},
		TAB {
			public byte getValue() {
				return (byte) 0x07;
			}
		},
		WIN {
			public byte getValue() {
				return (byte) 0x08;
			}
		},

		F1 {
			public byte getValue() {
				return (byte) 0x09;
			}
		},
		F2 {
			public byte getValue() {
				return (byte) 0x0A;
			}
		},
		F3 {
			public byte getValue() {
				return (byte) 0x0B;
			}
		},
		F4 {
			public byte getValue() {
				return (byte) 0x0C;
			}
		},
		F5 {
			public byte getValue() {
				return (byte) 0x0D;
			}
		},
		F6 {
			public byte getValue() {
				return (byte) 0x0E;
			}
		},
		F7 {
			public byte getValue() {
				return (byte) 0x0F;
			}
		},
		F8 {
			public byte getValue() {
				return (byte) 0x10;
			}
		},
		F9 {
			public byte getValue() {
				return (byte) 0x11;
			}
		},
		F10 {
			public byte getValue() {
				return (byte) 0x12;
			}
		},
		F11 {
			public byte getValue() {
				return (byte) 0x13;
			}
		},
		F12 {
			public byte getValue() {
				return (byte) 0x14;
			}
		},

		END {
			public byte getValue() {
				return (byte) 0x15;
			}
		},
		HOME {
			public byte getValue() {
				return (byte) 0x16;
			}
		},
		DEL {
			public byte getValue() {
				return (byte) 0x17;
			}
		},
		PRTSC {
			public byte getValue() {
				return (byte) 0x18;
			}
		},
		INSERT {
			public byte getValue() {
				return (byte) 0x19;
			}
		},
		NUMLOCK {
			public byte getValue() {
				return (byte) 0x1A;
			}
		},
		PAGEUP {
			public byte getValue() {
				return (byte) 0x1B;
			}
		},
		PAGEDOWN {
			public byte getValue() {
				return (byte) 0x1C;
			}
		},

		ARROW_UP {
			public byte getValue() {
				return (byte) 0x1D;
			}
		},
		ARROW_DOWN {
			public byte getValue() {
				return (byte) 0x1E;
			}
		},
		ARROW_LEFT {
			public byte getValue() {
				return (byte) 0x1F;
			}
		},
		ARROW_RIGHT {
			public byte getValue() {
				return (byte) 0x20;
			}
		},

		CAPSLOCK {
			public byte getValue() {
				return (byte) 0x21;
			}
		},
		 /**game control*/
        GAME_UP{
			public byte getValue() {
				return (byte) 0x22;
			}
		},
        GAME_DOWN{
			public byte getValue() {
				return (byte) 0x23;
			}
		},
        GAME_LEFT{
			public byte getValue() {
				return (byte) 0x24;
			}
		},
        GAME_RIGHT{
			public byte getValue() {
				return (byte) 0x25;
			}
		},
        GAME_A{
			public byte getValue() {
				return (byte) 0x26;
			}
		},
        GAME_B {
			public byte getValue() {
				return (byte) 0x27;
			}
		},
        GAME_C{
			public byte getValue() {
				return (byte) 0x28;
			}
		},
        GAME_D{
			public byte getValue() {
				return (byte) 0x29;
			}
		},
        GAME_START{
			public byte getValue() {
				return (byte) 0x2A;
			}
		},
        GAME_STOP{
			public byte getValue() {
				return (byte) 0x2B;
			}
		},
        GAME_OTHER1{
			public byte getValue() {
				return (byte) 0x2C;
			}
		},
        GAME_OTHER2{
			public byte getValue() {
				return (byte) 0x2D;
			}
		},
        GAME_OTHER3{
			public byte getValue() {
				return (byte) 0x2E;
			}
		},
        GAME_OTHER4{
			public byte getValue() {
				return (byte) 0x2F;
			}
		};

		public abstract byte getValue();

		public static SpecialKeys getSpecialKeys(byte value) {
			SpecialKeys ret = null;
			switch (value) {
			case 0x00:
				ret = SpecialKeys.ENTER;
				break;
			case 0x01:
				ret = SpecialKeys.BACKSPACE;
				break;
			case 0x02:
				ret = SpecialKeys.SPACE;
				break;
			case 0x03:
				ret = SpecialKeys.ESC;
				break;
			case 0x04:
				ret = SpecialKeys.SHIFT;
				break;
			case 0x05:
				ret = SpecialKeys.CTRL;
				break;
			case 0x06:
				ret = SpecialKeys.ALT;
				break;
			case 0x07:
				ret = SpecialKeys.TAB;
				break;
			case 0x08:
				ret = SpecialKeys.WIN;
				break;
			case 0x09:
				ret = SpecialKeys.F1;
				break;
			case 0x0A:
				ret = SpecialKeys.F2;
				break;
			case 0x0B:
				ret = SpecialKeys.F3;
				break;
			case 0x0C:
				ret = SpecialKeys.F4;
				break;
			case 0x0D:
				ret = SpecialKeys.F5;
				break;
			case 0x0E:
				ret = SpecialKeys.F6;
				break;
			case 0x0F:
				ret = SpecialKeys.F7;
				break;
			case 0x10:
				ret = SpecialKeys.F8;
				break;
			case 0x11:
				ret = SpecialKeys.F9;
				break;
			case 0x12:
				ret = SpecialKeys.F10;
				break;
			case 0x13:
				ret = SpecialKeys.F11;
				break;
			case 0x14:
				ret = SpecialKeys.F12;
				break;
			case 0x15:
				ret = SpecialKeys.END;
				break;
			case 0x16:
				ret = SpecialKeys.HOME;
				break;
			case 0x17:
				ret = SpecialKeys.DEL;
				break;
			case 0x18:
				ret = SpecialKeys.PRTSC;
				break;
			case 0x19:
				ret = SpecialKeys.INSERT;
				break;
			case 0x1A:
				ret = SpecialKeys.NUMLOCK;
				break;
			case 0x1B:
				ret = SpecialKeys.PAGEUP;
				break;
			case 0x1C:
				ret = SpecialKeys.PAGEDOWN;
				break;
			case 0x1D:
				ret = SpecialKeys.ARROW_UP;
				break;
			case 0x1E:
				ret = SpecialKeys.ARROW_DOWN;
				break;
			case 0x1F:
				ret = SpecialKeys.ARROW_LEFT;
				break;
			case 0x20:
				ret = SpecialKeys.ARROW_RIGHT;
				break;
			case 0x21:
				ret = SpecialKeys.CAPSLOCK;
				break;
			case 0x22:
				ret =SpecialKeys.GAME_UP;
				break;
			case 0x23:
				ret = SpecialKeys.GAME_DOWN;
				break;
			case 0x24:
				ret = SpecialKeys.GAME_LEFT;
				break;
			case 0x25:
				ret = SpecialKeys.GAME_RIGHT;
				break;
			case 0x26:
				ret = SpecialKeys.GAME_A;
				break;
			case 0x27:
				ret = SpecialKeys.GAME_B;
				break;
			case 0x28:
				ret = SpecialKeys.GAME_C;
				break;
			case 0x29:
				ret = SpecialKeys.GAME_D;
				break;
			case 0x2A:
				ret = SpecialKeys.GAME_START;
				break;
			case 0x2B:
				ret = SpecialKeys.GAME_STOP;
				break;
			case 0x2C:
				ret = SpecialKeys.GAME_OTHER1;
				break;
			case 0x2D:
				ret = SpecialKeys.GAME_OTHER2;
				break;
			case 0x2E:
				ret = SpecialKeys.GAME_OTHER3;
				break;
			case 0x2F:
				ret = SpecialKeys.GAME_OTHER4;
				break;
			default:
				ret=SpecialKeys.NONE;
				break;
			}
			return ret;
		}
	}

	public static final String UDPSCANMESSAGE = "哥们，在不在啊？";
	public static final String UDPSCANRETURN = "在啊，大哥，咋啦？";
	public static final String NETSEPARATOR = "<##>";// 消息分隔符
	public static final String UDPSEPARATOR = "-->>";// 消息分隔符
	public static final String FINDNOSERVER = "find no server online";

}
