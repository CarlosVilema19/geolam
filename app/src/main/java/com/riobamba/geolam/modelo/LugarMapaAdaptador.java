package com.riobamba.geolam.modelo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.riobamba.geolam.Listado;
import com.riobamba.geolam.R;

import java.util.List;

public class LugarMapaAdaptador extends RecyclerView.Adapter<LugarMapaAdaptador.ViewHolder> {

    private final Context mCtx;
    private final List<ListadoLugar> lugarList;
    final LugarMapaAdaptador.OnItemClickListener listener;

    public interface OnItemClickListener{
        void onItemClick(ListadoLugar item);
    }

    public interface OnClickListener{
        void onClick(ListadoLugar item);
    }

    public LugarMapaAdaptador(Context mCtx, List<ListadoLugar> lugarList, LugarMapaAdaptador.OnItemClickListener listener){
        this.mCtx = mCtx;
        this.lugarList = lugarList;
        this.listener = listener;
    }
    View view1;
    public void viewEjem (View v)
    {
        this.view1 = v;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView nombreLugar;
        private final TextView direccionLugar;
        private final TextView telefonoLugar;
        private final ImageView imagenLugar;
        private final TextView idLugar;




        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            nombreLugar = view.findViewById(R.id.tvNombreLugarLista);
            direccionLugar = view.findViewById(R.id.tvDireccionLista);
            telefonoLugar = view.findViewById(R.id.tvTelefonoLista);
            imagenLugar = view.findViewById(R.id.ivImagenLugarLista);
            idLugar = view.findViewById(R.id.tvId);
        }

        /*public TextView getTextView() {
            return textView;
        }*/
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.activity_listado, null);
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        ListadoLugar listadoLugar = lugarList.get(position);
        //Cargar Imagen
        Glide.with(mCtx)
                .load(listadoLugar.getImagenLugar())
                .into(viewHolder.imagenLugar);
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.nombreLugar.setText(listadoLugar.getNombreLugar());
        viewHolder.direccionLugar.setText(listadoLugar.getDireccionLugar());
        viewHolder.telefonoLugar.setText(listadoLugar.getTelefonoLugar());
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
