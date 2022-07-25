package com.riobamba.geolam.modelo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.riobamba.geolam.R;
import com.riobamba.geolam.modelo.ListadoLugarAdmin;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ListadoEspecialidadAdaptador extends RecyclerView.Adapter<com.riobamba.geolam.modelo.ListadoEspecialidadAdaptador.ViewHolder> {

    private final Context mCtx;
    private final List<ListadoLugarAdmin> lugarList;
    final com.riobamba.geolam.modelo.ListadoEspecialidadAdaptador.OnItemClickListener listener;
    private final List<ListadoLugarAdmin> espeListOriginal;


    public interface OnItemClickListener{
        void onItemClick(ListadoLugarAdmin item);
    }


    public ListadoEspecialidadAdaptador(Context mCtx, List<ListadoLugarAdmin> lugarList, ListadoEspecialidadAdaptador.OnItemClickListener listener){
        this.mCtx = mCtx;
        this.lugarList = lugarList;
        this.listener = listener;
        espeListOriginal = new ArrayList<>();
        espeListOriginal.addAll(lugarList);
    }
    View view1;
    public void viewEjem (View v)
    {
        this.view1 = v;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView nombreLugar;
        private final TextView idLugar;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            nombreLugar = view.findViewById(R.id.tvNombreEspecialidadLista);
            idLugar = view.findViewById(R.id.tvIdEspecialidadLista);
        }

        /*public TextView getTextView() {
            return textView;
        }*/
    }


    public void filtrado(final String txtBuscar)
    {
        // lugarList.clear();

        if(txtBuscar.length() == 0)
        {
            lugarList.clear();
            lugarList.addAll(espeListOriginal);
        }else{
            if(txtBuscar.length()!=0) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {

                    List<ListadoLugarAdmin> collection = lugarList.stream()
                            .filter(i -> i.getNombreLugar().toLowerCase().contains(txtBuscar.toLowerCase()))
                            .collect(Collectors.toList());
                    lugarList.clear();
                    lugarList.addAll(collection);
                } //else {
                lugarList.clear();
                for (ListadoLugarAdmin l : espeListOriginal) {
                    if (l.getNombreLugar().toLowerCase().contains(txtBuscar.toLowerCase())) {

                        lugarList.add(l);
                    }
                    // }
                }
            }
        }
        //medicListOriginal.clear();
        // lugarList.clear();
        notifyDataSetChanged();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public com.riobamba.geolam.modelo.ListadoEspecialidadAdaptador.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.activity_especialidad_usuario, null);
        return new com.riobamba.geolam.modelo.ListadoEspecialidadAdaptador.ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(com.riobamba.geolam.modelo.ListadoEspecialidadAdaptador.ViewHolder viewHolder, int position) {
        ListadoLugarAdmin listadoLugar = lugarList.get(position);
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.nombreLugar.setText(listadoLugar.getNombreLugar());
        viewHolder.idLugar.setText(String.valueOf(listadoLugar.getId()));
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(listadoLugar);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return lugarList.size();
    }
}

