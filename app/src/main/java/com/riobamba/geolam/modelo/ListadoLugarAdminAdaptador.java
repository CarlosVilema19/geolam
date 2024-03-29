package com.riobamba.geolam.modelo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.riobamba.geolam.R;
import com.riobamba.geolam.Tipologia;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ListadoLugarAdminAdaptador extends RecyclerView.Adapter<ListadoLugarAdminAdaptador.ViewHolder> {

    private final Context mCtx;
    private final List<ListadoLugarAdmin> lugarList;
    final ListadoLugarAdminAdaptador.OnItemClickListener listener;
    final ListadoLugarAdminAdaptador.OnClickListener listener2;
    final ListadoLugarAdminAdaptador.OnClickActListener listener3;
    private final List<ListadoLugarAdmin> TipoListOriginal;
    Integer tipo;

    public void filtrado(String txtBuscar) {
        if(txtBuscar.length() == 0)
        {
            lugarList.clear();
            lugarList.addAll(TipoListOriginal);
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
                for (ListadoLugarAdmin l :TipoListOriginal) {
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


    public interface OnItemClickListener{
        void onItemClick(ListadoLugarAdmin item);
    }

    public interface OnClickListener{
        void onClick(ListadoLugarAdmin button);
    }

    public interface OnClickActListener{
        void onClick(ListadoLugarAdmin button);
    }

    public ListadoLugarAdminAdaptador(Context mCtx,
                                      List<ListadoLugarAdmin> lugarList,
                                      OnItemClickListener listener,
                                      OnClickListener listener2,
                                      OnClickActListener listener3,
                                      Integer tipo){
        this.mCtx = mCtx;
        this.lugarList = lugarList;
        this.listener = listener;
        this.listener2 = listener2;
        this.listener3 = listener3;
        TipoListOriginal=new ArrayList<>();
        TipoListOriginal.addAll(lugarList);
        this.tipo = tipo;
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
        public ImageView imagenLugar;
        public CardView cardLugar;


        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            nombreLugar = view.findViewById(R.id.tvNombreLugarLista);
            idLugar = view.findViewById(R.id.tvId);
            btnBorrar = view.findViewById(R.id.ibBorrarLista);
            btnActualizar = view.findViewById(R.id.ibActualizarLista);
            imagenLugar = view.findViewById(R.id.ivLogo);
            cardLugar = view.findViewById(R.id.cvLogo);
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
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        ListadoLugarAdmin listadoLugar = lugarList.get(position);
        // Get element from your dataset at this position and replace the
        // contents of the view with that element

       /* Glide.with(mCtx)
                .load(listadoLugar.getImagen())
                .into(viewHolder.imagenLugar);*/
        if(!listadoLugar.getImagen().equals("") && !(tipo == 3))
        {
            Picasso.get().load(listadoLugar.getImagen()).fit().centerCrop().networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .into(viewHolder.imagenLugar);
        }
        else
        {
            Glide.with(mCtx)
                    .load(listadoLugar.getImagen())
                    .into(viewHolder.imagenLugar);
        }


        viewHolder.nombreLugar.setText(listadoLugar.getNombreLugar());
        viewHolder.idLugar.setVisibility(View.VISIBLE);
        String texto = "Id: " + listadoLugar.getId();
        viewHolder.idLugar.setText(texto);
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

        if(tipo == 1){viewHolder.btnActualizar.setVisibility(View.GONE);
            viewHolder.idLugar.setVisibility(View.GONE);}
        if(tipo == 2){viewHolder.idLugar.setVisibility(View.GONE);
            viewHolder.cardLugar.setVisibility(View.VISIBLE);}
        if(tipo == 3){viewHolder.idLugar.setVisibility(View.GONE);}

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



