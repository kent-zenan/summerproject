package ac.uk.nottingham.ningboport.controller;

import java.util.ArrayList;

import ac.uk.nottingham.ningboport.R;
import ac.uk.nottingham.ningboport.model.AndroidTaskItem;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * The ListView adaptor.
 * 
 * @author Jiaqi LI
 *
 */
public class TaskArrayAdapter extends ArrayAdapter<AndroidTaskItem> {

	private final LayoutInflater layoutInflater;
	
	public TaskArrayAdapter(Context context, ArrayList<AndroidTaskItem> taskItems) {
		super(context, 0, taskItems);
		
		layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View view;
		if (convertView instanceof View) {
			view = (View) convertView;
		} else {
			view = layoutInflater.inflate(R.layout.task_item, null);
			
		}
		
		AndroidTaskItem item = getItem(position);
		((TextView)view.findViewById(R.id.textVarFrom)).setText(item.getFrom());
		((TextView)view.findViewById(R.id.textVarTo)).setText(item.getTo());
		((TextView)view.findViewById(R.id.textVarETA)).setText(item.getExpectedTimeForArrival().toString());
		((TextView)view.findViewById(R.id.textStatus)).setText(item.getStatusInString());
		return view;
	}
}
