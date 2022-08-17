package com.riobamba.geolam.modelo;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telecom.Call;
import android.telecom.CallScreeningService;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.riobamba.geolam.Listado;
import com.riobamba.geolam.R;

import org.w3c.dom.Text;

import java.net.URI;
import java.text.DecimalFormat;
import java.util.List;

public class ListadoLugarUsuarioAdaptador extends RecyclerView.Adapter<ListadoLugarUsuarioAdaptador.ViewHolder> {

    private final Context mCtx;
    private final List<ListadoLugarUsuario> lugarList;
    private final List<ListadoLugar> lugarList2;
    final ListadoLugarUsuarioAdaptador.OnItemClickListener listener;
    final ListadoLugarUsuarioAdaptador.OnClickEspeListener listener2;
    final ListadoLugarUsuarioAdaptador.OnClickListener listener3;
    final ListadoLugarUsuarioAdaptador.OnClickComenListener listener4;
    final ListadoLugarUsuarioAdaptador.OnClickFavDesListener listener5;
    final ListadoLugarUsuarioAdaptador.OnClickFavAcListener listener6;
    String url;


    public interface OnItemClickListener{
        void onItemClick(ListadoLugarUsuario item);
    }

    public interface OnClickEspeListener{
        void onClick2(ListadoLugarUsuario item);
    }

    public interface OnClickListener{
        void onClick(ListadoLugar item);
    }

    public interface OnClickComenListener{
        void onClick3(ListadoLugarUsuario item);
    }
    public interface OnClickFavDesListener{
        void onClick4(ListadoLugarUsuario item);
    }
    public interface OnClickFavAcListener{
        void onClick5(ListadoLugarUsuario item);
    }

    public ListadoLugarUsuarioAdaptador(Context mCtx, List<ListadoLugarUsuario> lugarList,
                                        List<ListadoLugar> lugarList2,
                                        ListadoLugarUsuarioAdaptador.OnItemClickListener listener,
                                        ListadoLugarUsuarioAdaptador.OnClickEspeListener listener2,
                                        ListadoLugarUsuarioAdaptador.OnClickListener listener3,
                                        ListadoLugarUsuarioAdaptador.OnClickComenListener listener4,
                                        ListadoLugarUsuarioAdaptador.OnClickFavDesListener listener5,
                                        ListadoLugarUsuarioAdaptador.OnClickFavAcListener listener6){
        this.mCtx = mCtx;
        this.lugarList = lugarList;
        this.lugarList2 = lugarList2;
        this.listener = listener;
        this.listener2 = listener2;
        this.listener3 = listener3;
        this.listener4 = listener4;
        this.listener5 = listener5;
        this.listener6 = listener6;
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
        private final CardView btnComentario;
        private final TextView whatsapp;
        private final TextView paginaWeb;
        private final TextView calificacion;
        private final LinearLayout telefonoLL;
        private final LinearLayout whatsappLL;
        private final LinearLayout paginaWebLL;
        private final LinearLayout descripcionLL;
        private final LinearLayout calificacionLL;
        private final Button btnFavDes;
        private final Button btnFavAct;










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
            btnComentario = view.findViewById(R.id.btnVerComentario);
            idLugar = view.findViewById(R.id.tvIdLugar);
            btnEspecialidadUsuario = view.findViewById(R.id. btnEspecialidadUsuario);
            whatsapp = view.findViewById(R.id.tvWhatsapp);
            paginaWeb = view.findViewById(R.id.tvWeb);
            calificacion = view.findViewById(R.id.tvCalificacion);
            telefonoLL = view.findViewById(R.id.llTelefono);
            whatsappLL = view.findViewById(R.id.llWhatsapp);
            paginaWebLL = view.findViewById(R.id.llPaginaWeb);
            descripcionLL = view.findViewById(R.id.llDescripcion);
            calificacionLL = view.findViewById(R.id.llCalificacion);
            btnFavDes = view.findViewById(R.id.btnFavoritoDes);
            btnFavAct  = view.findViewById(R.id.btnFavoritoAct);



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
        ListadoLugar listadoLugar2 = lugarList2.get(position);
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
        if(listadoLugar.getCalificacion().equals(0F)){viewHolder.calificacionLL.setVisibility(View.GONE);}

        viewHolder.paginaWebLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(mCtx, "Pagina web", Toast.LENGTH_SHORT).show();
                 Uri link = Uri.parse(listadoLugar.getPaginaWeb());
                Intent intent = new Intent(Intent.ACTION_VIEW,link);
                mCtx.startActivity(intent);
            }
        });
        viewHolder.telefonoLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(mCtx, "Pagina web", Toast.LENGTH_SHORT).show();
                /*Uri telefono = Uri.parse(listadoLugar.getTelefonoLugar());
                Intent intent = new Intent(Intent.ACTION_CALL, telefono);
                mCtx.startActivity(intent);*/
            }
        });

        viewHolder.whatsappLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(mCtx, "Pagina web", Toast.LENGTH_SHORT).show();
                /*Uri whatsapp = Uri.parse(listadoLugar.getWhastapp());
                Intent intent = new Intent(Intent.ACTION_SENDTO, whatsapp);
                mCtx.startActivity(intent);*/
            }
        });

        viewHolder.nombreLugar.setText(listadoLugar.getNombreLugar());
        viewHolder.direccionLugar.setText(listadoLugar.getDireccionLugar());
        viewHolder.telefonoLugar.setText(listadoLugar.getTelefonoLugar());
        viewHolder.informacionLugar.setText(listadoLugar.getInformacionLugar());
        viewHolder.categoriaLugar.setText(listadoLugar.getCategoriaLugar());
        viewHolder.tipologiaLugar.setText(listadoLugar.getTipologiaLugar());
        viewHolder.whatsapp.setText(listadoLugar.getWhastapp());
        viewHolder.paginaWeb.setText(listadoLugar.getPaginaWeb());

        DecimalFormat formato1 = new DecimalFormat("#0.0");
        String distancia = formato1.format(listadoLugar.getCalificacion());

        viewHolder.calificacion.setText(distancia);
        viewHolder.idLugar.setText(String.valueOf(listadoLugar.getIdLugar()));

       // viewHolder.btnEspecialidadUsuario.setCardBackgroundColor(ContextCompat.getColor(this.mCtx, R.color.teal_700));

        if (listadoLugar.getFavorito().equals(1))
        {
            viewHolder.btnFavAct.setVisibility(View.VISIBLE);
            viewHolder.btnFavDes.setVisibility(View.GONE);
        }else
        {
            viewHolder.btnFavAct.setVisibility(View.GONE);
            viewHolder.btnFavDes.setVisibility(View.VISIBLE);
        }

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
                listener3.onClick(listadoLugar2);
            }
        });
        viewHolder.btnComentario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener4.onClick3(listadoLugar);
            }
        });
        viewHolder.btnFavDes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener5.onClick4(listadoLugar);
                viewHolder.btnFavAct.setVisibility(View.VISIBLE);
                viewHolder.btnFavDes.setVisibility(View.GONE);
                Toast.makeText(mCtx, "Se ha añadido a lugares favoritos", Toast.LENGTH_SHORT).show();
            }
        });
        viewHolder.btnFavAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener6.onClick5(listadoLugar);
                viewHolder.btnFavAct.setVisibility(View.GONE);
                viewHolder.btnFavDes.setVisibility(View.VISIBLE);
                Toast.makeText(mCtx, "Se ha quitado de lugares favoritos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return lugarList.size();
    }
}



