package com.example.gestionlocationnew;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class Page_Adapter_charge extends BaseAdapter
{
    private Context context;
    private ArrayList<list_charge> foodModelArrayList;
    public String col;
    public Page_Adapter_charge(Context context, ArrayList<list_charge> foodModelArrayList) {
        this.context = context;
        this.foodModelArrayList = foodModelArrayList;
    }

    @Override
    public int getCount() {
        return foodModelArrayList.size();
    }
    public int getViewTypeCount() {
        return getCount();
    }
    @Override
    public int getItemViewType(int position) {

        return position;
    }
    @Override
    public Object getItem(int position) {
        return foodModelArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    static class ViewHolder{
        public TextView matr;
        public TextView color;
        public TextView marq;
        public TextView id;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.charche_rows, null, true);

            holder.matr=(TextView)convertView.findViewById(R.id.matrV);
            holder.marq=(TextView)convertView.findViewById(R.id.marqueV);
            holder.id=(TextView)convertView.findViewById(R.id.Id_charge);





            convertView.setTag(holder);
        }else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = (ViewHolder)convertView.getTag();
        }

        holder.matr.setText(foodModelArrayList.get(position).getMatr());
        holder.marq.setText(foodModelArrayList.get(position).getMarque());
        holder.id.setText(foodModelArrayList.get(position).getColor());
        //holder.color.setText(foodModelArrayList.get(position).getColor());

        return convertView;
    }
}

