package com.yn020.host.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;

import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yn020.host.R;
import com.yn020.host.page.BasePage;
import com.yn020.host.page.EnrollPage;
import com.yn020.host.page.IdentifyPage;
import com.yn020.host.page.SettingPage;
import com.yn020.host.utils.FingerUtils;
import com.yn020.host.utils.ToastUtils;

public class HomeFragment extends BaseFragment implements OnItemClickListener, OnClickListener {
	private View view;
	@ViewInject(R.id.viewpager)
	public ViewPager viewPager;
	@ViewInject(R.id.fp_listview)
	private ListView fp_listview;
	@ViewInject(R.id.fp_image)
//	private com.yn020.host.page.CircleImage fp_image;
	private ImageView fp_image;
	
	@ViewInject(R.id.clear_btn)
	private Button clear_btn;
	
	@Override
	public View initView(LayoutInflater inflater) {
		view = inflater.inflate(R.layout.home_frag_layout, null);
		ViewUtils.inject(this, view);	
		fp_listview.setOnItemClickListener(this);	
		clear_btn.setOnClickListener(this);
		return view;
	}

	List<BasePage> list = new ArrayList<BasePage>();
	private HomeBaseAdapter homeBaseAdapter;
	private int deletePosition=1;

	@Override
	public void initData(Bundle savedInstanceState) {	
		enrollPage = new EnrollPage(ctx);
		identifyPage = new IdentifyPage(ctx);
		list.add(enrollPage);
		list.add(identifyPage);
		list.add(new SettingPage(ctx));

		HomePageAdapter homePageAdapter = new HomePageAdapter(ctx, list);
		viewPager.setAdapter(homePageAdapter);
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				if (0 == position) {
					slidingMenu
							.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
				} else {
					slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
				}

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}
	
	public void freshListViewData(List<Map<String,Long>> list){
		
		fpDataList = list;
		if(homeBaseAdapter==null){
			homeBaseAdapter = new HomeBaseAdapter(ctx, fpDataList);
			fp_listview.setAdapter(homeBaseAdapter);		
		}else{
			homeBaseAdapter.notifyDataSetChanged();
		}	
		
		
	}
	
//	  @SuppressLint("NewApi")
//	public void DrawFpImage(Drawable drawable){
//		  int i=0;
//		  LogUtils.d("DrawFpImage-->"+ i++);
//		  if(drawable!=null){
//			  fp_image.setImageDrawable(drawable);	  
//		  }
//		  
//	  }
	
	public Handler imageHandler=new Handler(){		
		public void handleMessage(android.os.Message msg) {
			Drawable fingerDrawable = (Drawable) msg.obj;
			 fp_image.setImageDrawable(fingerDrawable);	  
			
		};
	};
	public EnrollPage enrollPage;
	public IdentifyPage identifyPage;
	public List<Map<String,Long>> fpDataList;
	public Long fp_Id=(long) 1;  //用于1:1 验证时传递的id值
	private AlertDialog deleteDialog;
	
	
	class HomePageAdapter extends PagerAdapter {
		private Context ctx;
		private List<BasePage> list;

		public HomePageAdapter(Context ctx, List<BasePage> list) {
			this.ctx = ctx;
			this.list = list;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container)
					.removeView(list.get(position).getRootView());
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			((ViewPager) container)
					.addView(list.get(position).getRootView(), 0);

			return list.get(position).getRootView();

		}

	}
	
	
	
	class HomeBaseAdapter extends BaseAdapter{
		private List<Map<String,Long>> list;	
		private Context ctx;
		private int curPosition=0;  //当前选中的项
		public HomeBaseAdapter(Context ctx,List<Map<String,Long>> list) {
			super();
			this.list=list;
			this.ctx = ctx;
		}

		public void setCurPosition(int position){
			this.curPosition=position;
			//选中之后，记得让它刷新 ，不然不会变色
			notifyDataSetChanged();
		}
		@Override
		public int getCount() {			
			return list.size();
		}

		@Override
		public Object getItem(int position) {			
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {			
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			if(convertView==null){
				convertView=View.inflate(ctx, R.layout.item_fp_list, null);
			}
			TextView tvFpNo=(TextView) convertView.findViewById(R.id.tv_fp_no);
			TextView tvFpId=(TextView) convertView.findViewById(R.id.tv_fp_id);
			tvFpNo.setText(list.get(position).get("fp_No").toString());
			tvFpId.setText(list.get(position).get("fp_Id").toString());
			LogUtils.d("getView-->"+list.get(position).get("fp_No")+"-->"+list.get(position).get("fp_Id"));
			
			/**	让选中的为红色，没选中的是白色	**/
			if(curPosition==position){
				tvFpNo.setTextColor(getResources().getColor(R.color.red));		
				tvFpId.setTextColor(getResources().getColor(R.color.red));				
				convertView.setBackgroundResource(R.drawable.menu_item_bg_select);
			}else{
				tvFpNo.setTextColor(getResources().getColor(R.color.black));
				tvFpId.setTextColor(getResources().getColor(R.color.black));
				convertView.setBackgroundResource(R.drawable.abc_tab_indicator_ab_holo);
			}	
			
			ImageView ivDelete=(ImageView) convertView.findViewById(R.id.iv_fp_lv);
			ivDelete.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					homeBaseAdapter.setCurPosition(position);
					homeBaseAdapter.notifyDataSetChanged();
					AlertDialog.Builder builder=new AlertDialog.Builder(ctx);
					builder.setIcon(android.R.drawable.ic_dialog_alert);
					builder.setTitle("删除");
					builder.setMessage("确认要删除该ID的指纹吗？");
					builder.setPositiveButton("是的", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Long id=fpDataList.get(position).get("fp_Id");						
							if(FingerUtils.Delete_FP(id)){
								fpDataList.remove(position);
								homeBaseAdapter.setCurPosition(0);
								homeBaseAdapter.notifyDataSetChanged();
								ToastUtils.disToast(ctx, "指纹ID="+id+"删除成功！");
							}else{
								ToastUtils.disToast(ctx, "删除失败！");
							}
							deleteDialog.dismiss();
						}
					});
					builder.setNegativeButton("取消", null);
					deleteDialog = builder.create();
					deleteDialog.show();
					/**
					 * 功能：设置对话框参数，要在show()之后调用 
					 **/					
					WindowManager.LayoutParams params=deleteDialog.getWindow().getAttributes();
					params.width=300;
					params.height=251;
					deleteDialog.getWindow().setAttributes(params);					
				
					
				}
			});
			
			return convertView;			
		}

	}


	/**
	 * 功能：listview Item被点击的监听事件
	 **/
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
		homeBaseAdapter.setCurPosition(position);
		fp_Id = fpDataList.get(position).get("fp_Id");		
		this.deletePosition=position;
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.clear_btn:
			AlertDialog.Builder builder=new AlertDialog.Builder(ctx);
			builder.setIcon(android.R.drawable.ic_dialog_alert);
			builder.setTitle("删除");
			builder.setMessage("确认要清空指纹吗？");
			builder.setPositiveButton("是的", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {				    
					new ClearTask().execute();
					deleteDialog.dismiss();
				}
			});
			builder.setNegativeButton("取消", null);
			
			deleteDialog = builder.create();
			deleteDialog.show();
			
			WindowManager.LayoutParams params=deleteDialog.getWindow().getAttributes();
			params.width=300;
			params.height=251;
			deleteDialog.getWindow().setAttributes(params);	
			break;

		default:
			break;
		}
		
	}

	class ClearTask extends AsyncTask<Void, Void, Boolean>{

		@Override
		protected Boolean doInBackground(Void... params) {
			synchronized (enrollPage.fpSynchrLock) {
			return	FingerUtils.ClearALlFP();
								
			}
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			LogUtils.d("enter onPostExecute");
			if(result){
				ToastUtils.disToast(ctx, "清空指纹成功！");
				if(fpDataList!=null){
					fpDataList.removeAll(fpDataList);			
					homeBaseAdapter.notifyDataSetChanged();	
				}
			}else{
				ToastUtils.disToast(ctx, "清空指纹失败！");
				
			}			
			
		}		
	}
	
}
