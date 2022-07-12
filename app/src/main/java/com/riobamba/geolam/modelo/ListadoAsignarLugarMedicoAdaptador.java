package com.riobamba.geolam.modelo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.riobamba.geolam.R;

import java.util.ArrayList;

public class ListadoAsignarLugarMedicoAdaptador extends ArrayAdapter<ListadoAsignarLugarMedico> {
    private ArrayList<ListadoAsignarLugarMedico> arrayList= new ArrayList<>();
    private Context context;
    public ListadoAsignarLugarMedicoAdaptador(@NonNull Context context){
        super(context, R.layout.item_view);
        this.context=context;

    }
    @Override
    public int getCount(){
        return arrayList.size();
    }

    @Nullable
    @Override
    public ListadoAsignarLugarMedico getItem(int position){
        return arrayList.get(position);

    }
    @Override
    public long getItemId(int position){
        return position;
    }
    @Override
    public void add(@Nullable ListadoAsignarLugarMedico object)
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

            //TextView item_text = view.findViewById(R.id.viewItem);
            TextView item_text = view.findViewById(R.id.viewItem);
            ListadoAsignarLugarMedico listAsigLugarMedico= getItem(position);
            item_text.setText(listAsigLugarMedico.toString());


        }

        return view;


    }
}
