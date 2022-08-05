package com.riobamba.geolam.modelo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.riobamba.geolam.BuscarEspecialidades_LM;
import com.riobamba.geolam.Buscar_Especialidades;
import com.riobamba.geolam.R;

import java.util.ArrayList;

public class BuscarEspecialidades_LM_Adaptador extends ArrayAdapter<BuscarEspecialidades_LM> {
    private ArrayList<BuscarEspecialidades_LM> arrayList= new ArrayList<>();
    private Context context;

    public  BuscarEspecialidades_LM_Adaptador(@NonNull Context context){

        super(context, R.layout.item_view2);
        this.context=context;


    }
    @Override
    public int getCount(){
        return  arrayList.size();

    }

   @Nullable
    @Override
    public  BuscarEspecialidades_LM getItem(int position){
        return arrayList.get(position);


   }

   @Override
    public long getItemId(int position){
        return position;
   }

   @Override
    public  void add(@Nullable BuscarEspecialidades_LM object)
   {
       arrayList.add(object);
       notifyDataSetChanged();
   }

   @Override
    public void clear(){
       // arrayList= new ArrayList<>();
        arrayList.clear();
        notifyDataSetChanged();
   }

   @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View view =convertView;
        if (view==null) {

            view=LayoutInflater.from(context).inflate(R.layout.item_view2, parent,false);
            TextView item_text = view.findViewById(R.id.viewItem2);
            BuscarEspecialidades_LM espeLugar1 = getItem(position);
            item_text.setText(espeLugar1.getNombreLugar());

        }
       return view;
   }
}
