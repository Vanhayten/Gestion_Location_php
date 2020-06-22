/*package com.example.gestionlocationnew;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class adapter_reparation extends BaseAdapter {
    private Context context;
    private ArrayList<class_reparation> listReparation;

    public adapter_reparation(Context context, ArrayList<class_reparation> objects) {

        this.context = context;
        this.listReparation = objects;
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }
    @Override
    public int getItemViewType(int position) {

        return position;
    }


    @Override
    public int getCount() {
        return listReparation.size ();
    }

    @Override
    public Object getItem(int position) {
        return listReparation.get (position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    static class ViewHolder{
        public TextView text1;
        public TextView text2;
        public TextView text3;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        adapter_reparation.ViewHolder holder;

        if (convertView == null) {
            holder = new adapter_reparation.ViewHolder ();
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.adapter_liste_reparation, null, true);

            holder.text1=(TextView)convertView.findViewById(R.id.text1);
            holder.text2=(TextView)convertView.findViewById(R.id.text2);
            holder.text3=(TextView)convertView.findViewById(R.id.text3);

            convertView.setTag(holder);
        }else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = (adapter_reparation.ViewHolder)convertView.getTag();
        }

        holder.text1.setText(listReparation.get(position).getStr1());
        holder.text2.setText(listReparation.get(position).getStr2 ());
        holder.text3.setText(listReparation.get(position).getStr3 ());

        return convertView;
}}



/**
 *
 * test adapter
 */


package com.example.gestionlocationnew;
        import android.app.Activity;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.ImageView;
        import android.widget.LinearLayout;
        import android.widget.TextView;
        import android.widget.Toast;

        import androidx.core.content.ContextCompat;

        import com.example.gestionlocationnew.R;
        import com.example.gestionlocationnew.list_client;

        import java.util.ArrayList;

public class adapter_reparation extends ArrayAdapter<class_reparation> {

    private int mColorResourceId;


    private static final String LOG_TAG = adapter_reparation.class.getSimpleName();

    public adapter_reparation(Activity context, ArrayList<class_reparation> resource) {
        super(context, 0, resource);

    }


    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.adapter_liste_reparation, parent, false);
        }


        final class_reparation currentAndroidFlavor = getItem(position);



        TextView str1TextView = (TextView) listItemView.findViewById(R.id.text1);

        str1TextView.setText(currentAndroidFlavor.getStr1());


        TextView str2TextView = (TextView) listItemView.findViewById(R.id.text2);

        str2TextView.setText(currentAndroidFlavor.getStr2());
        TextView str3TextView = (TextView) listItemView.findViewById(R.id.text3);

        str3TextView.setText(currentAndroidFlavor.getStr3());

        return listItemView;
    }


}

