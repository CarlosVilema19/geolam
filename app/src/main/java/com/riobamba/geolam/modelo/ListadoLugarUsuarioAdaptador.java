package com.riobamba.geolam.modelo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.riobamba.geolam.Listado;
import com.riobamba.geolam.R;

import org.w3c.dom.Text;

import java.util.List;

public class ListadoLugarUsuarioAdaptador extends RecyclerView.Adapter<ListadoLugarUsuarioAdaptador.ViewHolder> {

    private final Context mCtx;
    private final List<ListadoLugarUsuario> lugarList;
    final ListadoLugarUsuarioAdaptador.OnItemClickListener listener;
    final ListadoLugarUsuarioAdaptador.OnClickEspeListener listener2;
    final ListadoLugarUsuarioAdaptador.OnClickListener listener3;


    public interface OnItemClickListener{
        void onItemClick(ListadoLugarUsuario item);
    }

    public interface OnClickEspeListener{
        void onClick2(ListadoLugarUsuario item);
    }

    public interface OnClickListener{
        void onClick(ListadoLugarUsuario item);
    }

    public ListadoLugarUsuarioAdaptador(Context mCtx, List<ListadoLugarUsuario> lugarList, ListadoLugarUsuarioAdaptador.OnItemClickListener listener,ListadoLugarUsuarioAdaptador.OnClickEspeListener listener2,ListadoLugarUsuarioAdaptador.OnClickListener listener3){
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
        private final TextView direccionLugar;
        private final TextView telefonoLugar;
        private final ImageView imagenLugar;
        private final TextView informacionLugar;
        private final TextView categoriaLugar;
        private final TextView tipologiaLugar;
        private final TextView idLugar;
        private final CardView btnMedicoUsuario;
        private final CardView btnEspecialidadUsuario;
        private final CardView btnCalificar;
        private final TextView whatsapp;
        private final TextView paginaWeb;
        private final TextView calificacion;
        private final LinearLayout telefonoLL;
        private final LinearLayout whatsappLL;
        private final LinearLayout paginaWebLL;
        private final LinearLayout descripcionLL;










        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            nombreLugar = view.findViewById(R.id.tvNombreLugar);
            direccionLugar = view.findViewById(R.id.tvDireccion);
            telefonoLugar = view.findViewById(R.id.tvTelefono);
            imagenLugar = view.findViewById(R.id.imagenlugar);
            informacionLugar = view.findViewById(R.id.tvInformacion);
            categoriaLugar = view.findViewById(R.id.tvCategoria);
            tipologiaLugar = view.findViewById(R.id.tvTipologia);
            btnMedicoUsuario = view.findViewById(R.id.btnMedicoUsuario);
            btnCalificar = view.findViewById(R.id.btnCalificar);
            idLugar = view.findViewById(R.id.tvIdLugar);
            btnEspecialidadUsuario = view.findViewById(R.id. btnEspecialidadUsuario);
            whatsapp = view.findViewById(R.id.tvWhatsapp);
            paginaWeb = view.findViewById(R.id.tvWeb);
            calificacion = view.findViewById(R.id.tvCalificacion);
            telefonoLL = view.findViewById(R.id.llTelefono);
            whatsappLL = view.findViewById(R.id.llWhatsapp);
            paginaWebLL = view.findViewById(R.id.llPaginaWeb);
            descripcionLL = view.findViewById(R.id.llDescripcion);



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
        View view = inflater.inflate(R.layout.activity_lugar_medico, null);
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        ListadoLugarUsuario listadoLugar = lugarList.get(position);
        //Cargar Imagen
        Glide.with(mCtx)
                .load(listadoLugar.getImagenLugar())
                .into(viewHolder.imagenLugar);
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        //Controla que si el campo esta vacio se oculta el titulo
        if(listadoLugar.getWhastapp().equals("")){viewHolder.whatsappLL.setVisibility(View.GONE);}
        if(listadoLugar.getPaginaWeb().equals("")){viewHolder.paginaWebLL.setVisibility(View.GONE);}
        if(listadoLugar.getTelefonoLugar().equals("")){viewHolder.telefonoLL.setVisibility(View.GONE);}
        if(listadoLugar.getDireccionLugar().equals("")){viewHolder.descripcionLL.setVisibility(View.GONE);}

        viewHolder.nombreLugar.setText(listadoLugar.getNombreLugar());
        viewHolder.direccionLugar.setText(listadoLugar.getDireccionLugar());
        viewHolder.telefonoLugar.setText(listadoLugar.getTelefonoLugar());
        viewHolder.informacionLugar.setText(listadoLugar.getInformacionLugar());
        viewHolder.categoriaLugar.setText(listadoLugar.getCategoriaLugar());
        viewHolder.tipologiaLugar.setText(listadoLugar.getTipologiaLugar());
        viewHolder.whatsapp.setText(listadoLugar.getWhastapp());
        viewHolder.paginaWeb.setText(listadoLugar.getPaginaWeb());
        viewHolder.calificacion.setText(String.valueOf(listadoLugar.getCalificacion()));
        viewHolder.idLugar.setText(String.valueOf(listadoLugar.getIdLugar()));

       // viewHolder.btnEspecialidadUsuario.setCardBackgroundColor(ContextCompat.getColor(this.mCtx, R.color.teal_700));


        viewHolder.btnMedicoUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(listadoLugar);
            }
        });
        viewHolder.btnEspecialidadUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener2.onClick2(listadoLugar);
            }
        });
        viewHolder.btnCalificar.setOnClickListener(new View.OnClickListener() {
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



