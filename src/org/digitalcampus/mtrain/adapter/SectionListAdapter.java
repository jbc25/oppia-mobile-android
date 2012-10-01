package org.digitalcampus.mtrain.adapter;

import java.util.ArrayList;
import java.util.Locale;

import org.digitalcampus.mtrain.R;
import org.digitalcampus.mtrain.model.Section;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SectionListAdapter extends ArrayAdapter<Section> {

	public static final String TAG = "SectionListAdapter";

	private final Context ctx;
	private final ArrayList<Section> sectionList;
	private SharedPreferences prefs;

	public SectionListAdapter(Activity context, ArrayList<Section> sectionList) {
		super(context, R.layout.section_list_row, sectionList);
		this.ctx = context;
		this.sectionList = sectionList;
		prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
	    LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    
	    View rowView = inflater.inflate(R.layout.section_list_row, parent, false);
	    TextView sectionTitle = (TextView) rowView.findViewById(R.id.section_title);
	    
	    Section s = sectionList.get(position);
	    sectionTitle.setText(s.getTitle(prefs.getString("prefLanguage", Locale.getDefault().getLanguage())));
	    
	    TextView sectionNo = (TextView) rowView.findViewById(R.id.section_number);
	    sectionNo.setText(String.valueOf(s.getSectionId()));
	    
	    ProgressBar pb = (ProgressBar) rowView.findViewById(R.id.section_progress_bar);
	    pb.setProgress((int) s.getProgress());
	    
	    /*LinearLayout ll = (LinearLayout) rowView.findViewById(R.id.section_activities);
	    ArrayList<org.digitalcampus.mtrain.model.Activity> activities = s.getActivities();
	    for(org.digitalcampus.mtrain.model.Activity a: activities){
	    	Log.d(TAG,a.getTitle(prefs.getString("prefLanguage", Locale.getDefault().getLanguage())));
	    	
	    	TextView tv = new TextView(ctx);
	    	tv.setTag(s);
	    	tv.setMinWidth(75);
	    	tv.setMinHeight(50);
	    	tv.setText(a.getTitle(prefs.getString("prefLanguage", Locale.getDefault().getLanguage())));
	    	tv.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Section s = (Section) v.getTag();
					Log.d(TAG,"clicked..."+s.getSectionId());
				}
			} );
	    	ll.addView(tv);
	    }*/
	    
	    rowView.setTag(sectionList.get(position));
	    return rowView;
	}
	
}

