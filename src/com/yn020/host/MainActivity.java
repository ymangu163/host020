package com.yn020.host;



import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.yn020.host.fragment.HomeFragment;
import com.yn020.host.fragment.MenuFragment;

import android.os.Bundle;
import android.view.Window;

public class MainActivity extends SlidingFragmentActivity {
	
	
	private SlidingMenu sm;
	private MenuFragment menuFragment;

	/**
     * 1 得到滑动菜单
     * 2 设置滑动菜单是在左边出来还是右边出来
     * 3 设置滑动菜单出来之后，内容页，显示的剩余宽度
     * 4 设置滑动菜单的阴影 设置阴影，阴影需要在开始的时候，特别暗，慢慢的变淡
     * 5 设置阴影的宽度
     * 6 设置滑动菜单的范围
     */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setBehindContentView(R.layout.menu_layout);
		setContentView(R.layout.content_layout);
		
		
		sm = getSlidingMenu();
		sm.setMode(SlidingMenu.LEFT);
		//3 设置滑动菜单出来之后，内容页，显示的剩余宽度
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
	
		sm.setFadeDegree(0.35f);
	 	sm.setShadowDrawable(R.drawable.slide_shadow);
	 	sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		
		//替换菜单 
		menuFragment = new MenuFragment();
		getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame, menuFragment,"Menu").commit();
			
		
		HomeFragment homeFragment = new HomeFragment();
		getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, homeFragment).commit();
		
		
		
		
	}
                                                                                  


}
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    