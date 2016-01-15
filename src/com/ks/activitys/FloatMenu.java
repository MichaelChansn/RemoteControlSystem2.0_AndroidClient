package com.ks.activitys;

import com.ks.application.R;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class FloatMenu {

	private Context context;
	private static final int ID1 = 0x01;
	private static final int ID2 = 0x02;
	private static final int ID3 = 0x03;
	private static final int ID4 = 0x04;
	private static final int ID5 = 0x05;

	public FloatMenu(Context context) {
		this.context = context;
	}

	public void createFloatMenu() {
		// Set up the white button on the lower right corner
		// more or less with default parameter
		final ImageView fabIconNew = new ImageView(context);
		fabIconNew.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_action_new_light));
		int bigActionButtonSize = context.getResources().getDimensionPixelSize(R.dimen.big_action_button_size);
		int bigActionButtonMargin = context.getResources().getDimensionPixelOffset(R.dimen.big_action_button_margin);
		int bigActionButtonContentSize = context.getResources()
				.getDimensionPixelSize(R.dimen.big_action_button_content_size);
		int bigActionButtonContentMargin = context.getResources()
				.getDimensionPixelSize(R.dimen.big_action_button_content_margin);
		int bigActionMenuRadius = context.getResources().getDimensionPixelSize(R.dimen.big_action_menu_radius);
		int smallSubActionButtonSize = context.getResources()
				.getDimensionPixelSize(R.dimen.small_sub_action_button_size);
		int smallSubActionButtonContentMargin = context.getResources()
				.getDimensionPixelSize(R.dimen.small_sub_action_button_content_margin);

		FloatingActionButton.LayoutParams bigParams = new FloatingActionButton.LayoutParams(bigActionButtonSize,
				bigActionButtonSize);
		bigParams.setMargins(bigActionButtonMargin, bigActionButtonMargin, bigActionButtonMargin,
				bigActionButtonMargin);
		fabIconNew.setLayoutParams(bigParams);

		FloatingActionButton.LayoutParams fabIconNewParams = new FloatingActionButton.LayoutParams(
				bigActionButtonContentSize, bigActionButtonContentSize);
		fabIconNewParams.setMargins(bigActionButtonContentMargin, bigActionButtonContentMargin,
				bigActionButtonContentMargin, bigActionButtonContentMargin);
		final FloatingActionButton rightLowerButton = new FloatingActionButton.Builder(context)
				.setContentView(fabIconNew, fabIconNewParams)
				.setLayoutParams(bigParams)/*
											 * .setBackgroundDrawable(R.drawable
											 * .button_action_red_selector)
											 * .setPosition(FloatingActionButton
											 * .POSITION_BOTTOM_CENTER)
											 */
				.build();

		SubActionButton.Builder rLSubBuilder = new SubActionButton.Builder(context);
		/*
		 * rLSubBuilder.setBackgroundDrawable(getResources().getDrawable(R.
		 * drawable.button_action_blue_selector));
		 */

		FrameLayout.LayoutParams smallContentParams = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
		smallContentParams.setMargins(smallSubActionButtonContentMargin, smallSubActionButtonContentMargin,
				smallSubActionButtonContentMargin, smallSubActionButtonContentMargin);
		rLSubBuilder.setLayoutParams(smallContentParams);
		FrameLayout.LayoutParams smallParams = new FrameLayout.LayoutParams(smallSubActionButtonSize,
				smallSubActionButtonSize);
		rLSubBuilder.setLayoutParams(smallParams);

		ImageView rlIcon1 = new ImageView(context);
		ImageView rlIcon2 = new ImageView(context);
		ImageView rlIcon3 = new ImageView(context);
		ImageView rlIcon4 = new ImageView(context);
		ImageView rlIcon5 = new ImageView(context);
		rlIcon1.setId(ID1);
		rlIcon2.setId(ID2);
		rlIcon3.setId(ID3);
		rlIcon4.setId(ID4);
		rlIcon5.setId(ID5);

		
		rlIcon1.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_action_chat_light));
		rlIcon2.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_action_camera_light));
		rlIcon3.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_action_video_light));
		rlIcon4.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_action_place_light));
		rlIcon5.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_action_place_light));

		
		// Build the menu with default options: light theme, 90 degrees, 72dp
		// radius.
		// Set 4 default SubActionButtons
		final FloatingActionMenu rightLowerMenu = new FloatingActionMenu.Builder(context)
				.addSubActionView(rLSubBuilder.setContentView(rlIcon1, smallContentParams).build())
				.addSubActionView(rLSubBuilder.setContentView(rlIcon2, smallContentParams).build())
				.addSubActionView(rLSubBuilder.setContentView(rlIcon3, smallContentParams).build())
				.addSubActionView(rLSubBuilder.setContentView(rlIcon4, smallContentParams).build())
				.addSubActionView(rLSubBuilder.setContentView(rlIcon5, smallContentParams).build())
				.setRadius(bigActionMenuRadius)
				/*
				 * .setStartAngle(-20) .setEndAngle(-160)
				 */
				.attachTo(rightLowerButton).build();

		
		/**Listener must be add after build*/
		final OnClickMenuListener onClickMenuListener = new OnClickMenuListener();
		rlIcon1.setOnClickListener(onClickMenuListener);
		rlIcon2.setOnClickListener(onClickMenuListener);
		rlIcon3.setOnClickListener(onClickMenuListener);
		rlIcon4.setOnClickListener(onClickMenuListener);
		rlIcon5.setOnClickListener(onClickMenuListener);

		// Listen menu open and close events to animate the button content view
		rightLowerMenu.setStateChangeListener(new FloatingActionMenu.MenuStateChangeListener() {
			@Override
			public void onMenuOpened(FloatingActionMenu menu) {
				// Rotate the icon of rightLowerButton 45 degrees clockwise
				fabIconNew.setRotation(0);
				PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, 45);
				ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder(fabIconNew, pvhR);
				animation.start();
			}

			@Override
			public void onMenuClosed(FloatingActionMenu menu) {
				// Rotate the icon of rightLowerButton 45 degrees
				// counter-clockwise
				fabIconNew.setRotation(45);
				PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, 0);
				ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder(fabIconNew, pvhR);
				animation.start();
			}
		});

	}

	private  class OnClickMenuListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			System.out.println("ID"+v.getId());
			switch (v.getId()) {
			case ID1:
				Intent intent = new Intent(context, MouseActivity.class);
				context.startActivity(intent);
				break;
			case ID2:
				((MainActivity)context).showIME();
				break;
			case ID3:
				if(!((MainActivity)context).isSpecialShow())
				((MainActivity)context).showSpecialKeys();
				else
				((MainActivity)context).closeSpecialKeys();	
				break;
			case ID4:
				Intent intent2 = new Intent(context, GameControlActivity.class);
				context.startActivity(intent2);
				break;
			case ID5:
				Intent intent3 = new Intent(context, ControlPCActivity.class);
				context.startActivity(intent3);
				break;
			default:
				break;
			}
		}

	}

}
