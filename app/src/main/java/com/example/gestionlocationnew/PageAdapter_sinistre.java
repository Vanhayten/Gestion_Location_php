package com.example.gestionlocationnew;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class PageAdapter_sinistre extends BaseAdapter {


    private Context context;
    private ArrayList<liste_sinistre> foodModelArrayList;
    public PageAdapter_sinistre(Context context, ArrayList<liste_sinistre> foodModelArrayList) {

        this.context = context;
        this.foodModelArrayList = foodModelArrayList;
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
        return foodModelArrayList.size();
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
        public TextView date;
        public TextView genre;
        public TextView montant;
        public TextView resp;
        public TextView montant1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.rows_sinistre, null, true);

            holder.date=(TextView)convertView.findViewById(R.id.date);
            holder.genre=(TextView)convertView.findViewById(R.id.genre);
            holder.montant=(TextView)convertView.findViewById(R.id.montant);
            holder.resp=(TextView)convertView.findViewById(R.id.resp);
            holder.montant1=(TextView)convertView.findViewById(R.id.montant1);

            convertView.setTag(holder);
        }else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = (ViewHolder)convertView.getTag();
        }

        holder.date.setText(foodModelArrayList.get(position).getdate());
        holder.genre.setText(foodModelArrayList.get(position).getgenre());
        holder.montant.setText(foodModelArrayList.get(position).getmontant());
        holder.resp.setText(foodModelArrayList.get(position).getresp());
        holder.montant1.setText(foodModelArrayList.get(position).getmontant1());

        return convertView;
    }

}
