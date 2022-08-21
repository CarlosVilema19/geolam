package com.riobamba.geolam.modelo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.riobamba.geolam.R;
import com.riobamba.geolam.modelo.ListadoLugarAdmin;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EspecialidadInicioAdaptador extends RecyclerView.Adapter<com.riobamba.geolam.modelo.EspecialidadInicioAdaptador.ViewHolder> {

    private final Context mCtx;
    private final List<ListadoLugarAdmin> lugarList;
    final com.riobamba.geolam.modelo.EspecialidadInicioAdaptador.OnItemClickListener listener;
    private final List<ListadoLugarAdmin> espeListOriginal;
    private final List<ListadoLugarAdmin> espeList;


    public interface OnItemClickListener{
        void onItemClick(ListadoLugarAdmin item);
    }


    public EspecialidadInicioAdaptador(Context mCtx, List<ListadoLugarAdmin> lugarList, EspecialidadInicioAdaptador.OnItemClickListener listener){
        this.mCtx = mCtx;
        this.lugarList = lugarList;
        this.listener = listener;
        espeListOriginal = new ArrayList<>();
        espeListOriginal.addAll(lugarList);
        espeList = new ArrayList<>();
        espeList.addAll(espeListOriginal);
    }
    View view1;
    public void viewEjem (View v)
    {
        this.view1 = v;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView nombreLugar;
        private final TextView idLugar;
        private final ImageView imagenEspe;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            nombreLugar = view.findViewById(R.id.tvNombreEspecialidadLista);
            idLugar = view.findViewById(R.id.tvIdEspecialidadLista);
            imagenEspe = view.findViewById(R.id.ivImagenEspecialidad);
        }

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
    public com.riobamba.geolam.modelo.EspecialidadInicioAdaptador.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.activity_especialidad_inicio, null);
        return new com.riobamba.geolam.modelo.EspecialidadInicioAdaptador.ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(EspecialidadInicioAdaptador.ViewHolder viewHolder, int position) {
        ListadoLugarAdmin listadoLugar = lugarList.get(position);
        // Get element from your dataset at this position and replace the
        // contents of the view with that element

        Glide.with(mCtx)
                .load(listadoLugar.getImagen())
                .into(viewHolder.imagenEspe);

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


