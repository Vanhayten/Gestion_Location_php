package com.example.gestionlocationnew;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class PageAdapter_vihucle extends BaseAdapter
{
    private Context context;
    private ArrayList<list_vihcule> foodModelArrayList;

    public PageAdapter_vihucle(Context context, ArrayList<list_vihcule> foodModelArrayList) {
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

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.vicucule_rows, null, true);

            holder.matr=(TextView)convertView.findViewById(R.id.matrV);
            holder.marq=(TextView)convertView.findViewById(R.id.marqueV);
           // holder.color=(TextView)convertView.findViewById(R.id.);

            convertView.setTag(holder);
        }else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = (ViewHolder)convertView.getTag();
        }

        holder.matr.setText(foodModelArrayList.get(position).getMatr());
        holder.marq.setText(foodModelArrayList.get(position).getMarque());
       // holder.color.setText(foodModelArrayList.get(position).getColor());

        return convertView;
    }
    }
}
