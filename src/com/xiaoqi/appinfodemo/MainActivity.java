package com.xiaoqi.appinfodemo;

import java.util.ArrayList;
import java.util.List;

import com.xiaoqi.appinfodemo.bean.PMAppInfo;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener{
	private PackageManager pm;
	private int flag;
	private final int ALL_APP = 0;
	private final int SYSTEM_APP = 1;
	private final int THIRD_APP = 2;
	private final int SDCARD_APP = 3;
	private Button btn_all;
	private Button btn_sys;
	private Button btn_third;
	private Button btn_sd;
	private ListView iv;
	private List<PMAppInfo> list;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }
    
    private void initView(){
    	iv = (ListView) findViewById(R.id.lv_info);
    	btn_all = (Button) findViewById(R.id.btn_all);
    	btn_sys = (Button) findViewById(R.id.btn_sys);
    	btn_third = (Button) findViewById(R.id.btn_third);
    	btn_sd = (Button) findViewById(R.id.btn_sd);
    	btn_all.setOnClickListener(this);
    	btn_sys.setOnClickListener(this);
    	btn_third.setOnClickListener(this);
    	btn_sd.setOnClickListener(this);
    }
    

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_all:
			flag = ALL_APP;
			break;
		case R.id.btn_sys:
			flag = SYSTEM_APP;
			break;
		case R.id.btn_third:
			flag = THIRD_APP;
			break;
		case R.id.btn_sd:
			flag = SDCARD_APP;
			break;
		default:
			break;
		}
		list = getPMAppInfo();
		
		iv.setAdapter(new APPAdapter());
	}
    
	/**
	 * 获取信息的集合
	 * @return
	 */
    private List<PMAppInfo> getPMAppInfo(){
    	pm = getPackageManager();
    	//以ApplicationInfo的形式返回安装的应用
    	List<ApplicationInfo> listAppcations = pm.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
    	List<PMAppInfo> appInfos = new ArrayList<PMAppInfo>();
    	switch (flag) {
		case ALL_APP:
			appInfos.clear();
			for(ApplicationInfo app:listAppcations){
				appInfos.add(getAppInfo(app));
			}
			break;
		//系统应用
		case SYSTEM_APP:
			appInfos.clear();
			for(ApplicationInfo app:listAppcations){
				if((app.flags & ApplicationInfo.FLAG_SYSTEM) != 0){
					appInfos.add(getAppInfo(app));
				}
			}
			break;
		//第三方应用
		case THIRD_APP:
			appInfos.clear();
			for(ApplicationInfo app:listAppcations){
				if((app.flags & ApplicationInfo.FLAG_SYSTEM) <= 0){
					appInfos.add(getAppInfo(app));
				}else if((app.flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) != 0){
					appInfos.add(getAppInfo(app));
				}
			}
			break;
		//安装在sd卡的应用
		case SDCARD_APP:
			appInfos.clear();
			for(ApplicationInfo app:listAppcations){
				if((app.flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) != 0){
					appInfos.add(getAppInfo(app));
				}
			}
			break;
		default:
			break;
		}
    	return appInfos;
    }
    
    /**
     * 获取APP信息
     * @param app
     * @return
     */
    private PMAppInfo getAppInfo(ApplicationInfo app){
    	PMAppInfo info = new PMAppInfo();
    	info.setAppIcon(app.loadIcon(pm));
    	info.setAppLabel((String)app.loadLabel(pm));
    	info.setPkgName(app.packageName);
    	return info;
    }

    class APPAdapter extends BaseAdapter{

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
		public View getView(int position, View convertView, ViewGroup parent) {
			PMAppInfo app = list.get(position);
			convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.item, null);
			ImageView img =(ImageView) convertView.findViewById(R.id.iv_app);
			TextView iv_appName =(TextView) convertView.findViewById(R.id.tv_appName);
			TextView iv_pckName =(TextView) convertView.findViewById(R.id.tv_packageName);
			img.setImageDrawable(app.getAppIcon());
			iv_appName.setText(app.getAppLabel());
			iv_pckName.setText(app.getPkgName());
			return convertView;
		}
    	
    }
}
