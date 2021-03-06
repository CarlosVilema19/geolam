package com.riobamba.geolam.modelo;



import android.content.Context;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.widget.Spinner;
import android.widget.TextView;

import com.loopj.android.http.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.riobamba.geolam.Listado;
import com.riobamba.geolam.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class ListadoCategoriaAdaptador extends ArrayAdapter<ListadoCategoria> {
private ArrayList<ListadoCategoria> arrayList= new ArrayList<>();
private Context context;

public ListadoCategoriaAdaptador(@NonNull Context context){
    super(context, R.layout.item_view);
    this.context=context;

}
@Override
    public int getCount(){
    return arrayList.size();
}

@Nullable
    @Override
    public ListadoCategoria getItem(int position){
    return arrayList.get(position);

}
@Override
    public long getItemId(int position){
    return position;
}
@Override
    public void add(@Nullable ListadoCategoria object)
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

            TextView tvCat = view.findViewById(R.id.viewItem);
            ListadoCategoria listCate = getItem(position);
            tvCat.setText(listCate.toString());


        }

        return view;


}



}

