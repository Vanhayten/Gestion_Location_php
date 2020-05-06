package com.example.gestionlocationnew;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
