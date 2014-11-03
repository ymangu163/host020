package com.yn020.host.fragment;



import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yn020.host.R;
import com.yn020.host.utils.ToastUtils;

public class MenuFragment extends BaseFragment implements OnItemClickListener {
	@ViewInject(R.id.listView_menu)
	private ListView listView_menu;
	
	/**
	 * 把view 返回就能在菜单中看到listview了
	 **/
	@Override
	public View initView(LayoutInflater inflater) {
		view=inflater.inflate(R.layout.left_menu_layout, null);
		ViewUtils.inject(this, view);
		listView_menu.setOnItemClickListener(this);
		return view;
	}

	@Override
	public void initData(Bundle savedInstanceState) {
		initMenu();
	}
	private String[] menuStr={" 注  册"," 识  别"," 设  置"};
	private MenuAdapter menuAdapter;
	
	
	
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		menuAdapter.setCurPosition(position);
		slidingMenu.toggle();
		ToastUtils.disToast(ctx, "您选择的是："+menuStr[position]);
		
		
		
		
	}
	public void initMenu(){

		if(menuAdapter==null){
			menuAdapter = new MenuAdapter(ctx, menuStr);	
			listView_menu.setAdapter(menuAdapter);
		}else{
			menuAdapter.notifyDataSetChanged();
		}
		menuAdapter.setCurPosition(0);		
	}
	
	class MenuAdapter extends BaseAdapter{
		private Context ctx;
		private String[] menuStr;
		private int curPosition=0;  //当前选中的项
		public MenuAdapter(Context ctx,String[] menuStr){
			this.ctx=ctx;
			this.menuStr=menuStr;
		} 
		public void setCurPosition(int position){
			this.curPosition=position;
			//选中之后，记得让它刷新 ，不然不会变色
			notifyDataSetChanged();
		}
		
		@Override
		public int getCount() {
			return menuStr.length;
		}

		@Override
		public Object getItem(int position) {
			return menuStr[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView==null){
				convertView=View.inflate(ctx, R.layout.item_menu_layout, null);
			}
			TextView textview = (TextView)convertView.findViewById(R.id.tv_menu_item);
			ImageView imageView=(ImageView)convertView.findViewById(R.id.iv_menu_item);
			textview.setText(menuStr[position]);
		
			
			/**	让选中的为红色，没选中的是白色	**/
			if(curPosition==position){
				textview.setTextColor(getResources().getColor(R.color.red));				
				imageView.setBackgroundResource(R.drawable.menu_arr_select);
				convertView.setBackgroundResource(R.drawable.menu_item_bg_select);
			}else{
				textview.setTextColor(getResources().getColor(R.color.white));
				imageView.setBackgroundResource(R.drawable.menu_arr_normal);
				convertView.setBackgroundResource(R.drawable.abc_tab_indicator_ab_holo);
				
			}
			
			return convertView;
		}		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
