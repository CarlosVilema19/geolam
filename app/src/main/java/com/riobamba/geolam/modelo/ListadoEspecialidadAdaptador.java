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

import java.util.List;

public class ListadoEspecialidadAdaptador extends RecyclerView.Adapter<com.riobamba.geolam.modelo.ListadoEspecialidadAdaptador.ViewHolder> {

    private final Context mCtx;
    private final List<ListadoLugarAdmin> lugarList;
    final com.riobamba.geolam.modelo.ListadoEspecialidadAdaptador.OnItemClickListener listener;



    public interface OnItemClickListener{
        void onItemClick(ListadoLugarAdmin item);
    }


    public ListadoEspecialidadAdaptador(Context mCtx, List<ListadoLugarAdmin> lugarList, com.riobamba.geolam.modelo.ListadoEspecialidadAdaptador.OnItemClickListener listener){
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

