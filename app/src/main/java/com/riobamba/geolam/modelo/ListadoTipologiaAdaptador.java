package com.riobamba.geolam.modelo;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.riobamba.geolam.R;

import java.util.ArrayList;

public class ListadoTipologiaAdaptador extends ArrayAdapter<ListadoTipologia> {
private ArrayList<ListadoTipologia> arrayList= new ArrayList<>();
private final ArrayList<ListadoTipologia> arrayList2= new ArrayList<>();
private final Context context;

public ListadoTipologiaAdaptador(@NonNull Context context){
    super(context, R.layout.item_view);
    this.context=context;

}
@Override
    public int getCount(){
    return arrayList.size();
}


@Nullable
    @Override
    public ListadoTipologia getItem(int position){
    return arrayList.get(position);

}
@Override
    public long getItemId(int position){
    return position;
}
//@Override
    public void add(@Nullable ListadoTipologia object)
{
    arrayList.add(object);

    notifyDataSetChanged();
}
@Override
    public void clear()
{
    arrayList= new ArrayList<>();

    notifyDataSetChanged();
}

@NonNull
    @Override
    public View getView (int position, @Nullable View convertView, @NonNull ViewGroup parent){

        View view =convertView;
        if(view==null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_view, parent, false);

            TextView tvTipo = view.findViewById(R.id.viewItem);
            ListadoTipologia listTipo = getItem(position);
            tvTipo.setText(listTipo.toString());
            //TextView id= view.findViewById(R.id.viewItem2);
           // id.setText(listTipo.ofInt());
        }

        return view;


}

}

