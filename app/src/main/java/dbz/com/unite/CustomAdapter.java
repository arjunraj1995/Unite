package dbz.com.unite;

import android.content.Context;
import android.util.Log;
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

public class CustomAdapter extends ArrayAdapter<DataModel> {

    private ArrayList<DataModel> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        TextView txtLang;
        TextView txtOnline;
        ImageView info;
    }

    public CustomAdapter(ArrayList<DataModel> data, Context context) {
        super(context, R.layout.row_item, data);
        this.dataSet = data;
        this.mContext=context;

    }



    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
            DataModel dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.nameg);
            viewHolder.txtLang = (TextView) convertView.findViewById(R.id.type);
            viewHolder.txtOnline = (TextView) convertView.findViewById(R.id.version_number);
            viewHolder.info=(ImageView)convertView.findViewById(R.id.online_pic) ;
            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;
        Log.d("hehelol","Last pos:"+String.valueOf(lastPosition));

        viewHolder.txtName.setText(dataModel.getName());
        viewHolder.txtLang.setText(dataModel.getLang());
        viewHolder.txtOnline.setText(dataModel.getOnline());
        if(dataModel.getOnline().equals("yes")){
            viewHolder.info.setVisibility(View.VISIBLE);
        }
        else viewHolder.info.setVisibility(View.GONE);
        // Return the completed view to render on screen
        return convertView;
    }
}