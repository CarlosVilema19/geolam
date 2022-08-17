package com.riobamba.geolam.modelo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Environment;
import android.util.Log;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.riobamba.geolam.ViewPDF;

import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;

import org.w3c.dom.DocumentFragment;
import org.w3c.dom.DocumentType;
import org.w3c.dom.EntityReference;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;
import org.w3c.dom.UserDataHandler;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class TemplatePDF {
    private Context context;
    private File pdfArchivo;
    private Document document;
    private PdfWriter pdfWriter;
    private Paragraph paragraph;
    private Font fTitle = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.BOLD);
    private Font fsubTitle = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD,new BaseColor(255,255,255));
    private Font fsubTitle2 = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
    private Font fText = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
    private Font fTextNormal = new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.NORMAL);
    private Font fHighText = new Font(Font.FontFamily.TIMES_ROMAN, 15, Font.BOLD, BaseColor.GREEN);
    private Font fDate = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);

    public TemplatePDF(Context context) {
        this.context = context;
    }

    public void abrirDocumento() {
        crearArchivo();
        try {
            document = new Document(PageSize.A4);
            pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(pdfArchivo));
            document.open();
        } catch (Exception e) {
            Log.e("abrirDocumento", e.toString());
        }
    }

    private void crearArchivo() {
        File folder = new File (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"Reportes GEOLAM");
        //File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "Geolam_ReportesPDF");
        if (!folder.exists())//{ //verifica si ya existe la carpeta

            folder.mkdirs();

            pdfArchivo = new File(folder, "Reporte Usuarios Registrados.pdf");


    }

    public void cerrarDocumento() {
        document.close();
    }

    public void addMetada(String title, String subject, String autor) {
        document.addTitle(title);
        document.addSubject(subject);
        document.addAuthor(autor);

    }

    public void addTitles(String title, String subTitle, String date) {
        try {
            paragraph = new Paragraph();
            addChildP(new Paragraph(title, fTitle));
            addChildP(new Paragraph(subTitle, fsubTitle2));
            addChildP(new Paragraph("Generado: " + date, fDate));
            paragraph.setSpacingAfter(30);
            document.add(paragraph);
        } catch (Exception e) {
            Log.e("addTitles", e.toString());
        }
    }

    private void addChildP(Paragraph childParagraph) {
        childParagraph.setAlignment(Element.ALIGN_CENTER);
        paragraph.add(childParagraph); //Agrega párrafos hijos a párrafo grande
    }

    public void addParagraph(String text){
        try {
            paragraph = new Paragraph(text, fTextNormal);
            paragraph.setSpacingAfter(15);
            paragraph.setSpacingBefore(5);
            document.add(paragraph);
        } catch (Exception e) {
            Log.e("addParagraph", e.toString());
        }
    }

    public void crearTabla (String[] header, ArrayList<String[]>lugares){
        try{

        paragraph= new Paragraph(); // va la tabla
        paragraph.setFont(fText); // formato de la tabla
             paragraph.setSpacingBefore(15);

            PdfPTable pdfPTable=new PdfPTable(header.length); //columnas que va a tener
        pdfPTable.setWidthPercentage(100); //porcentaje del ancho de la tabla

        PdfPCell pdfPCell; //celdas de la tabla
        int indexC=0; //representa la columna
        while(indexC<header.length){ // menor al total de columnas que tiene la tabla
            pdfPCell=new PdfPCell(new Phrase(header[indexC++], fsubTitle));// enviamos el texto que va a ir en la celda
            pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            pdfPCell.setFixedHeight(35);
            //pdfPCell.setBackgroundColor(new BaseColor(#FF017FCC));
            pdfPCell.setBackgroundColor(new BaseColor(0,140,255));

            pdfPTable.addCell(pdfPCell);

        }
        //llenar el resto de la tabla
        for(int indexR=0; indexR<lugares.size();indexR++){
            //obtener la fila
            String [] row=lugares.get(indexR);
            for( indexC=0; indexC<header.length;indexC++){ //Columnas
                pdfPCell= new PdfPCell(new Phrase(row[indexC]));
                pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                pdfPCell.setFixedHeight(35);
                pdfPTable.addCell(pdfPCell);
            }
        }

        paragraph.add(pdfPTable);

        document.add(paragraph);
        } catch (Exception e) {
            Log.e("crearTabla", e.toString());
        }
         }
    public void addImgName(Bitmap grafi) {
        try{

            /*Drawable drawable = ContextCompat.getDrawable(context, R.drawable.descarga);
            Image image = Image.getInstance(drawable + "Geo" + ".jpg");
           */
            //ReportesAdminUsuarios rep= new ReportesAdminUsuarios();
            //rep.graf();
           /* Drawable d = context.getResources().getDrawable(R.drawable.geo);
           BitmapDrawable bitDw = ((BitmapDrawable) d);
            Bitmap bmp = bitDw.getBitmap();*/

            Bitmap bmp = grafi;
            //bmp.sameAs(rep.grafi());
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Image image = Image.getInstance(stream.toByteArray());

            image.setSpacingBefore(5);

            image.setSpacingAfter(5);
            image.scaleToFit(400,400);
            image.setAlignment(Element.ALIGN_CENTER);
            document.add(image);
        }catch (Exception e){
            Log.e("addImgName ", e.toString());
        }
    }
public void viewPDF(){
    Intent intent = new Intent(context, ViewPDF.class);
    intent.putExtra("path", pdfArchivo.getAbsolutePath());
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(intent);
}

    }


