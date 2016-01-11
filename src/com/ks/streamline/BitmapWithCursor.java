package com.ks.streamline;

import java.util.List;

import com.ks.streamline.Recpacket.BitmapType;
import com.ks.streamline.Recpacket.PacketType;

import android.graphics.Bitmap;

public class BitmapWithCursor {
	 private Bitmap difBitmap;
     private ShortPoint cursorPoint;
     private List<ShortRec> difPointsList;
     private BitmapType type;
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
	private PacketType packetType;
     private String stringValue;
     private int intValue1;
     private int intValue2;
	public Bitmap getDifBitmap() {
		return difBitmap;
	}
	public void setDifBitmap(Bitmap difBitmap) {
		this.difBitmap = difBitmap;
	}
	public ShortPoint getCursorPoint() {
		return cursorPoint;
	}
	public void setCursorPoint(ShortPoint cursorPoint) {
		this.cursorPoint = cursorPoint;
	}
	public List<ShortRec> getDifPointsList() {
		return difPointsList;
	}
	public void setDifPointsList(List<ShortRec> difPointsList) {
		this.difPointsList = difPointsList;
	}
	public BitmapType getType() {
		return type;
	}
	public void setType(BitmapType type) {
		this.type = type;
	}

}
