package com.yn020.host;



import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.lidroid.xutils.util.LogUtils;
import com.yn020.host.fragment.HomeFragment;
import com.yn020.host.fragment.MenuFragment;
import com.yn020.host.utils.FingerManager;
import com.yn020.host.utils.ToastUtils;

public class MainActivity extends SlidingFragmentActivity {
	
	
	private SlidingMenu sm;
	private MenuFragment menuFragment;
	private HomeFragment homeFragment;
	private boolean fullScreen=false;
	private int screenWidth;
	private int screenHeight;

	private Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			String str=(String) msg.obj;
			ToastUtils.custLocationToast(MainActivity.this, str);
			
		};
	};
	
	
	
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
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		
		//替换菜单 
		menuFragment = new MenuFragment();
		getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame, menuFragment,"Menu").commit();
			
		
		homeFragment = new HomeFragment();
		getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, homeFragment,"Home").commit();
		
		screenWidth = getWindowManager().getDefaultDisplay().getWidth();
		screenHeight = getWindowManager().getDefaultDisplay().getHeight();
		LogUtils.d("screenWidth:"+screenWidth+" screenHeight:"+screenHeight);
		
		
		new Thread(new Runnable(){
			@Override
			public void run() {
				Message msg=Message.obtain();
				String str="";
				if(FingerManager.getSharedInstance().FPM_stopFP()){
					if(FingerManager.getSharedInstance().FPM_initFP()){
						str="初始化成功!";						
					}else{
						str="初始化失败!";
					}
				}else{
					str="停止失败!";
				}
				msg.obj=str;
				handler.sendMessage(msg);
			}			
		}).start();
		
		
		
	}
	
     public HomeFragment getHomeFragment(){
    	 homeFragment=(HomeFragment)getSupportFragmentManager().findFragmentByTag("Home");
    	 return homeFragment;    	 
     }
                                                                            


}
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    