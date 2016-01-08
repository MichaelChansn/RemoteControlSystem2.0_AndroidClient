package com.ks.streamline;

import java.util.List;

import com.ks.streamline.Recpacket.BitmapType;

import android.graphics.Bitmap;

public class BitmapWithCursor {
	 private Bitmap difBitmap;
     private ShortPoint cursorPoint;
     private List<ShortRec> difPointsList;
     private BitmapType type;
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
