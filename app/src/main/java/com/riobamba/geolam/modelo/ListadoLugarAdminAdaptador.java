package com.riobamba.geolam.modelo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.riobamba.geolam.R;

import java.util.List;

public class ListadoLugarAdminAdaptador extends RecyclerView.Adapter<ListadoLugarAdminAdaptador.ViewHolder> {

    private final Context mCtx;
    private final List<ListadoLugarAdmin> lugarList;
    final ListadoLugarAdminAdaptador.OnItemClickListener listener;
    final ListadoLugarAdminAdaptador.OnClickListener listener2;
    final ListadoLugarAdminAdaptador.OnClickActListener listener3;



    public interface OnItemClickListener{
        void onItemClick(ListadoLugarAdmin item);
    }

    public interface OnClickListener{
        void onClick(ListadoLugarAdmin button);
    }

    public interface OnClickActListener{
        void onClick(ListadoLugarAdmin button);
    }

    public ListadoLugarAdminAdaptador(Context mCtx, List<ListadoLugarAdmin> lugarList, OnItemClickListener listener, OnClickListener listener2, OnClickActListener listener3){
        this.mCtx = mCtx;
        this.lugarList = lugarList;
        this.listener = listener;
        this.listener2 = listener2;
        this.listener3 = listener3;
    }
    View view1;
    public void viewEjem (View v)
    {
        this.view1 = v;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView nombreLugar;
        private final TextView idLugar;
        private final ImageButton btnBorrar;
        private final ImageButton btnActualizar;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            nombreLugar = view.findViewById(R.id.tvNombreLugarLista);
            idLugar = view.findViewById(R.id.tvId);
            btnBorrar = view.findViewById(R.id.ibBorrarLista);
            btnActualizar = view.findViewById(R.id.ibActualizarLista);
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
        View view = inflater.inflate(R.layout.activity_listado_lugar_crud, null);
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
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

        viewHolder.btnBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener2.onClick(listadoLugar);
            }
        });

        viewHolder.btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener3.onClick(listadoLugar);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return lugarList.size();
    }
}



