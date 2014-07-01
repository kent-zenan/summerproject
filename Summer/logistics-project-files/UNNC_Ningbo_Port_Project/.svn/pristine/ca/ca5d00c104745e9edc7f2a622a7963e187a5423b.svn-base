package ac.uk.nottingham.ningboport.controller;

import java.util.ArrayList;
import ac.uk.nottingham.ningboport.R;
import ac.uk.nottingham.ningboport.model.AndroidSettingItem;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * The list view adapter for settings activity.
 * 
 * @see http://developer.android.com/reference/android/widget/Adapter.html
 * @see http://developer.android.com/reference/android/widget/ListView.html
 * @author Jiaqi LI
 *
 */
public class SettingsArrayAdapter extends ArrayAdapter<AndroidSettingItem> {

	private final LayoutInflater layoutInflater;
	
	public SettingsArrayAdapter(Context context, ArrayList<AndroidSettingItem> objects) {
		
		super(context, 0, objects);
		layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View view;
		if (convertView instanceof View) {
			view = (View) convertView;
		} else {
			view = layoutInflater.inflate(R.layout.settings_item, null);
		}
		
		AndroidSettingItem item = getItem(position);
		((TextView)view.findViewById(R.id.textSetTitle)).setText(item.getTitle());
		((TextView)view.findViewById(R.id.textSetContent)).setText(item.getContent());
		return view;
	}
}
