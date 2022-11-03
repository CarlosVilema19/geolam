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
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ListadoUsuariosAdminAdaptador extends RecyclerView.Adapter<ListadoUsuariosAdminAdaptador.ViewHolder> {

    private final Context mCtx;
    private final List<ListadoUsuariosAdmin> usuariosList;
    final ListadoUsuariosAdminAdaptador.OnItemClickListener listener;
    final ListadoUsuariosAdminAdaptador.OnClickListener listener2;
    private final List<ListadoUsuariosAdmin> usuariosListOriginal;
    public void filtrado(String txtBuscar) {
        // lugarList.clear();

        if(txtBuscar.length() == 0)
        {
            usuariosList.clear();
            usuariosList.addAll(usuariosListOriginal);
        }else{
            if(txtBuscar.length()!=0) {

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {

                    List<ListadoUsuariosAdmin> collection = usuariosList.stream()
                            .filter(i -> i.getNombreUsuarios().toLowerCase().contains(txtBuscar.toLowerCase()))
                            .collect(Collectors.toList());
                    usuariosList.clear();
                    usuariosList.addAll(collection);
                } //else {
                usuariosList.clear();
                for (ListadoUsuariosAdmin l : usuariosListOriginal) {
                    if (l.getNombreUsuarios().toLowerCase().contains(txtBuscar.toLowerCase())) {

                        usuariosList.add(l);
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
        void onItemClick(ListadoUsuariosAdmin item);
    }

    public interface OnClickListener{
        void onClick(ListadoUsuariosAdmin button);
    }

    public ListadoUsuariosAdminAdaptador(Context mCtx, List<ListadoUsuariosAdmin> usuariosList, OnItemClickListener listener, OnClickListener listener2){
        this.mCtx = mCtx;
        this.usuariosList = usuariosList;
        this.listener = listener;
        this.listener2 = listener2;
        usuariosListOriginal = new ArrayList<>();
        usuariosListOriginal.addAll(usuariosList);
    }
    View view1;
    public void viewEjem (View v)
    {
        this.view1 = v;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView nombreUsuarios;
        private final TextView emailUsuarios;
        private final TextView idTipoUsuarios;
        private final TextView descripcionTipo;
        private final ImageButton btnBorrar;
        private final ImageView imagenUsuarios;
        private final ImageButton btnActualizar;





        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            nombreUsuarios = view.findViewById(R.id.tvNombreUsuariosLista);
            emailUsuarios = view.findViewById(R.id.tvEmail);
            btnBorrar = view.findViewById(R.id.ibBorrarLista);
            idTipoUsuarios = view.findViewById(R.id.tvIdTipo);
            descripcionTipo = view.findViewById(R.id.tvDescripcionTipo);
            imagenUsuarios = view.findViewById(R.id.ivImagenUsuariosLista);
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
        View view = inflater.inflate(R.layout.activity_listado_usuarios_admin, null);
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        ListadoUsuariosAdmin listadousuarios = usuariosList.get(position);
        // Get element from your dataset at this position and replace the
        // contents of the view with that element

       /* Glide.with(mCtx)
                .load(listadousuarios.getImagenUsuarios())
                .into(viewHolder.imagenUsuarios);*/

        Picasso.get().load(listadousuarios.getImagenUsuarios()).fit().centerCrop().networkPolicy(NetworkPolicy.NO_CACHE)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .into(viewHolder.imagenUsuarios);

        viewHolder.nombreUsuarios.setText(listadousuarios.getNombreUsuarios());
        viewHolder.emailUsuarios.setText(listadousuarios.getEmailUsuarios());
        viewHolder.descripcionTipo.setText(listadousuarios.getDescripcionTipo());
        viewHolder.idTipoUsuarios.setText(String.valueOf(listadousuarios.getIdTipoUsuarios()));
        viewHolder.btnActualizar.setVisibility(View.GONE);

        if(listadousuarios.getIdTipoUsuarios() == 1)
        {
            viewHolder.btnBorrar.setVisibility(View.GONE);
        }else{
            viewHolder.btnBorrar.setVisibility(View.VISIBLE);
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(listadousuarios);
            }
        });

        viewHolder.btnBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener2.onClick(listadousuarios);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return usuariosList.size();
    }
}

