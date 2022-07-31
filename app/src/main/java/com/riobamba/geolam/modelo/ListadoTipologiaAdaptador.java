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
    private ArrayList<ListadoTipologia> arrayList2= new ArrayList<>();
private Context context;

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

   /* private Spinner spCategoria;
    private AsyncHttpClient cliente;

    public void OnCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingreso_lugar_medico);
        cliente = new AsyncHttpClient();
        spCategoria = (Spinner) findViewById(R.id.spinnerCategoria);
        LlenarSpinner();
    }

    private void LlenarSpinner(){
        String url = WebService.urlRaiz + WebService.servicioListCategoria;
        cliente.post(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(statusCode==200)
                {
                    cargarSpinner(new String (responseBody));

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

    }
    private void cargarSpinner(String respuesta)
    {
        ArrayList<ListadoCategoria>lista= new ArrayList<ListadoCategoria>();
        try {
            JSONArray jsonArray = new JSONArray(respuesta);
            for (int i = 0; i < jsonArray.length(); i++) {
                ListadoCategoria categoria=new ListadoCategoria();
                categoria.setDescripcion(jsonArray.getJSONObject(i).getString("descripcion_categoria"));
                lista.add(categoria);

            }

            ArrayAdapter<ListadoCategoria> c=new ArrayAdapter<ListadoCategoria>(this, android.R.layout.simple_dropdown_item_1line,lista);
            spCategoria.setAdapter(c);
        }catch (Exception e)
        {
            e.printStackTrace();
        }



    }
    */


}

