package com.ks.activitys;

import com.ks.application.R;
import com.ks.net.TcpNet;
import com.ks.net.enums.MessageEnums.MessageType;
import com.ks.net.enums.MessageEnums.SpecialKeys;
import com.ks.streamline.SendPacket;

import android.view.View;
import android.view.View.OnClickListener;

public class SpecialKeyLinstener implements OnClickListener
{

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		SpecialKeys key = null;
		switch (v.getId()) {
		case R.id.button_Alt:
			key = SpecialKeys.ALT;
			break;
		case R.id.button_CapsLock:
			key = SpecialKeys.CAPSLOCK;
			break;
		case R.id.button_Ctrl:
			key = SpecialKeys.CTRL;
			break;
		case R.id.button_Delete:
			key = SpecialKeys.DEL;
			break;
		case R.id.button_Down:
			key = SpecialKeys.ARROW_DOWN;
			break;
		case R.id.button_Up:
			key = SpecialKeys.ARROW_UP;
			break;
		case R.id.button_End:
			key = SpecialKeys.END;
			break;
		case R.id.button_Esc:
			key = SpecialKeys.ESC;
			break;
		case R.id.button_F1:
			key = SpecialKeys.F1;
			break;
		case R.id.button_F10:
			key = SpecialKeys.F10;
			break;
		case R.id.button_F11:
			key = SpecialKeys.F11;
			break;
		case R.id.button_F12:
			key = SpecialKeys.F12;
			break;
		case R.id.button_F2:
			key = SpecialKeys.F2;
			break;
		case R.id.button_F3:
			key = SpecialKeys.F3;
			break;
		case R.id.button_F4:
			key = SpecialKeys.F4;
			break;
		case R.id.button_F5:
			key = SpecialKeys.F5;
			break;
		case R.id.button_F6:
			key = SpecialKeys.F6;
			break;
		case R.id.button_F7:
			key = SpecialKeys.F7;
			break;
		case R.id.button_F8:
			key = SpecialKeys.F8;
			break;
		case R.id.button_F9:
			key = SpecialKeys.F9;
			break;
		case R.id.button_Home:
			key = SpecialKeys.HOME;
			break;
		case R.id.button_Insert:
			key = SpecialKeys.INSERT;
			break;
		case R.id.button_Left:
			key = SpecialKeys.ARROW_LEFT;
			break;
		case R.id.button_NumLock:
			key = SpecialKeys.NUMLOCK;
			break;
		case R.id.button_PageDown:
			key = SpecialKeys.PAGEDOWN;
			break;
		case R.id.button_PageUp:
			key = SpecialKeys.PAGEUP;
			break;
		case R.id.button_PrtSc:
			key = SpecialKeys.PRTSC;
			break;
		case R.id.button_Right:
			key = SpecialKeys.ARROW_RIGHT;
			break;
		case R.id.button_Shift:
			key = SpecialKeys.SHIFT;
			break;
		case R.id.button_Space:
			key = SpecialKeys.SPACE;
			break;
		case R.id.button_Tab:
			key = SpecialKeys.TAB;
			break;
		case R.id.button_Windows:
			key = SpecialKeys.WIN;
			break;
		case R.id.button_Backspace:
			key = SpecialKeys.BACKSPACE;
			break;
		case R.id.button_Enter:
			key = SpecialKeys.ENTER;
			break;
		default:
			key = SpecialKeys.NONE;
		}
		
		clickKey(key);
	}
	
	private void clickKey(SpecialKeys key) {
		SendPacket senPacket = new SendPacket();
		senPacket.setMsgType(MessageType.KEY_DOWN);
		senPacket.setSplKeys(key);
		TcpNet.getInstance().sendMessage(senPacket);
		SendPacket senPacket2 = new SendPacket();
		senPacket2.setMsgType(MessageType.KEY_UP);
		senPacket2.setSplKeys(key);
		TcpNet.getInstance().sendMessage(senPacket2);
	}
}





/*
OnTouchListener {

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		SpecialKeys key = null;
		switch (v.getId()) {
		case R.id.button_Alt:
			key = SpecialKeys.ALT;
			break;
		case R.id.button_CapsLock:
			key = SpecialKeys.CAPSLOCK;
			break;
		case R.id.button_Ctrl:
			key = SpecialKeys.CTRL;
			break;
		case R.id.button_Delete:
			key = SpecialKeys.DEL;
			break;
		case R.id.button_Down:
			key = SpecialKeys.ARROW_DOWN;
			break;
		case R.id.button_Up:
			key = SpecialKeys.ARROW_UP;
			break;
		case R.id.button_End:
			key = SpecialKeys.END;
			break;
		case R.id.button_Esc:
			key = SpecialKeys.ESC;
			break;
		case R.id.button_F1:
			key = SpecialKeys.F1;
			break;
		case R.id.button_F10:
			key = SpecialKeys.F10;
			break;
		case R.id.button_F11:
			key = SpecialKeys.F11;
			break;
		case R.id.button_F12:
			key = SpecialKeys.F12;
			break;
		case R.id.button_F2:
			key = SpecialKeys.F2;
			break;
		case R.id.button_F3:
			key = SpecialKeys.F3;
			break;
		case R.id.button_F4:
			key = SpecialKeys.F4;
			break;
		case R.id.button_F5:
			key = SpecialKeys.F5;
			break;
		case R.id.button_F6:
			key = SpecialKeys.F6;
			break;
		case R.id.button_F7:
			key = SpecialKeys.F7;
			break;
		case R.id.button_F8:
			key = SpecialKeys.F8;
			break;
		case R.id.button_F9:
			key = SpecialKeys.F9;
			break;
		case R.id.button_Home:
			key = SpecialKeys.HOME;
			break;
		case R.id.button_Insert:
			key = SpecialKeys.INSERT;
			break;
		case R.id.button_Left:
			key = SpecialKeys.ARROW_LEFT;
			break;
		case R.id.button_NumLock:
			key = SpecialKeys.NUMLOCK;
			break;
		case R.id.button_PageDown:
			key = SpecialKeys.PAGEDOWN;
			break;
		case R.id.button_PageUp:
			key = SpecialKeys.PAGEUP;
			break;
		case R.id.button_PrtSc:
			key = SpecialKeys.PRTSC;
			break;
		case R.id.button_Right:
			key = SpecialKeys.ARROW_RIGHT;
			break;
		case R.id.button_Shift:
			key = SpecialKeys.SHIFT;
			break;
		case R.id.button_Space:
			key = SpecialKeys.SPACE;
			break;
		case R.id.button_Tab:
			key = SpecialKeys.TAB;
			break;
		case R.id.button_Windows:
			key = SpecialKeys.WIN;
			break;
		case R.id.button_Backspace:
			key = SpecialKeys.BACKSPACE;
			break;
		case R.id.button_Enter:
			key = SpecialKeys.ENTER;
			break;
		default:
			key = SpecialKeys.NONE;
		}

		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			sendKeyMessage(MessageType.KEY_DOWN, key);
			break;
		case MotionEvent.ACTION_UP:
			sendKeyMessage(MessageType.KEY_UP, key);
			break;
		}
		return true;
	}

	private void sendKeyMessage(MessageType keyType, SpecialKeys key) {
		SendPacket senPacket = new SendPacket();
		senPacket.setMsgType(keyType);
		senPacket.setSplKeys(key);
		TcpNet.getInstance().sendMessage(senPacket);
	}
}*/
