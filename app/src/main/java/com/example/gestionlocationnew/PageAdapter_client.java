package com.example.gestionlocationnew;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class PageAdapter_client extends BaseAdapter
{
    private Context context;
    private ArrayList<list_client> foodModelArrayList;
    public String col;
    public PageAdapter_client(Context context, ArrayList<list_client> foodModelArrayList) {
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
        public TextView cin;
        public TextView nom;
        public TextView prenom;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PageAdapter_client.ViewHolder holder;

        if (convertView == null) {
            holder = new PageAdapter_client.ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.vicucule_rows, null, true);

            holder.cin=(TextView)convertView.findViewById(R.id.matrV);
            holder.nom=(TextView)convertView.findViewById(R.id.marqueV);
          //  holder.prenom=(TextView)convertView.findViewById(R.id.marqueV);




            convertView.setTag(holder);
        }else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = (PageAdapter_client.ViewHolder)convertView.getTag();
        }

        holder.cin.setText(foodModelArrayList.get(position).getCin());


        holder.nom.setText(foodModelArrayList.get(position).getNom());
       // holder.prenom.setText(foodModelArrayList.get(position).getPrenom());

        return convertView;
    }
}
