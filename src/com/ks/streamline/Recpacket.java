package com.ks.streamline;

import java.util.List;

public class Recpacket {
	/**
	 * 消息结构
	 * 
	 * _____________________________________________________________________________________________________________________________________________________________________
	 * PTYPE | LEN | BTYPE | C_POSITION_X | C_POSITION_Y | DIF_NUM | DIF_LIST |
	 * BITMAP_DATA |
	 * -------------------------------------------------------------------------
	 * -------------------------------------------------------------------------
	 * ------------------- PTYPE:byte 表示图像包，还是文字数据包
	 * LEN:4bytes,int型，表示BITMAP_DATA的大小，不包括消息头（LEN,TYPE,POSITION_X,POSITION_Y）
	 * TYPE:bitmap的类型，详见下列枚举 C_POSITION_X: 鼠标的坐标 D_POSITION_X: 差异图形开始坐标
	 * DIF_NUM:各个图形块的起始坐标，用于恢复图形。 BITMAP_DATA:要发送的bitmap数据
	 */

	public static enum BitmapType {
		BLOCK {
			public byte getValue() {
				return (byte) 0x00;
			}
		}, // s帧，补帧，用于图形复原
		COMPLETE {
			public byte getValue() {
				return (byte) 0x01;
			}
		};// k帧，用于直接显示
		public abstract byte getValue();

		public static BitmapType getBitmapType(byte value) {
			BitmapType ret = null;
			switch (value) {
			case 0x00:
				ret = BitmapType.BLOCK;
				break;
			case 0x01:
				ret = BitmapType.COMPLETE;
				break;
			default:
				break;
			}
			return ret;
		}
	}

	public static enum PacketType {
		BITMAP {
			public byte getValue() {
				return (byte) 0x00;
			}
		}, // s帧，补帧，用于图形复原
		TEXT {
			public byte getValue() {
				return (byte) 0x01;
			}
		};// k帧，用于直接显示
		public abstract byte getValue();

		public static PacketType getPacketType(byte value) {
			PacketType ret = null;
			switch (value) {
			case 0x00:
				ret = PacketType.BITMAP;
				break;
			case 0x01:
				ret = PacketType.TEXT;
				break;
			default:
				
				break;
			}
			return ret;
		}
	}

	/** 要发送的图像的数据大小(不包括消息头)最大表示4G文件2^32 */
	private int bitmapBytesLength = 0;

	/**
	 * 图像的类型 0x00:普通图像差一块，用于拼接到主图
	 * 0x01:key帧，一副完整的图像帧，直接显示，为了抵消解码错误引起的图像显示不正确，相当于H264内部的I帧
	 *
	 */
	private BitmapType bitmapType = BitmapType.BLOCK;
	/** 鼠标的坐标 */
	private ShortPoint cursorPoint = null;
	/** 差异图形的起点坐标 */
	private List<ShortRec> difPointsList = null;
	/** 要发送的图形数据 */
	private byte[] bitmapBytes = null;

	private PacketType packetType;
	private String stringValue;
	private int intValue1;
	private int intValue2;

	public PacketType getPacketType() {
		return packetType;
	}

	public void setPacketType(PacketType packetType) {
		this.packetType = packetType;
	}

	public String getStringValue() {
		return stringValue;
	}

	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
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

	public void setBitByts(byte[] bytes) {
		this.bitmapBytes = bytes;
	}

	public byte[] getBitByts() {
		return this.bitmapBytes;
	}

	public void setBitmapBytesLength(int length) {
		this.bitmapBytesLength = length;
	}

	public int getbitmapBytesLength() {
		return this.bitmapBytesLength;
	}

	public void setBitmapType(BitmapType type) {
		this.bitmapType = type;
	}

	public BitmapType getBitmapType() {
		return this.bitmapType;
	}

	public ShortPoint getCursorPoint() {
		return this.cursorPoint;
	}

	public void setCursorPoint(ShortPoint cursorPoint) {
		this.cursorPoint = cursorPoint;
	}

	public List<ShortRec> getDifPointsList() {
		return this.difPointsList;
	}

	public void setDifPointsList(List<ShortRec> difPointsList) {
		this.difPointsList = difPointsList;
	}
}
