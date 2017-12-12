package dbz.com.unite;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by kcarj on 24-03-2017.
 */

public class CustomAdapterGroup extends ArrayAdapter<DataModelGroup> {

    private ArrayList<DataModelGroup> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView grpName;
        TextView grpAdmin;

    }

    public CustomAdapterGroup(ArrayList<DataModelGroup> data, Context context) {
        super(context, R.layout.rowgroup_item, data);
        this.dataSet = data;
        this.mContext=context;

    }



    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        DataModelGroup dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.rowgroup_item, parent, false);
            viewHolder.grpName = (TextView) convertView.findViewById(R.id.nameg);
            viewHolder.grpAdmin = (TextView) convertView.findViewById(R.id.ad_nameg);
            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;
        viewHolder.grpName.setText(dataModel.getgName());
        viewHolder.grpAdmin.setText(dataModel.getgadname());

        // Return the completed view to render on screen
        return convertView;
    }
}