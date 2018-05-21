package martelapp.test.Class;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.print.PrintAttributes;
import android.print.pdf.PrintedPdfDocument;
import android.support.annotation.RequiresApi;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import martelapp.test.Activity.AnalyseActivity;
import martelapp.test.Activity.MainActivity;
import martelapp.test.Fragment.Analyse.CreationPdfFragment;
import martelapp.test.R;

/**
 * Created by Baptiste on 20/05/2018.
 */

public class PdfCreator extends AsyncTask<View, String, String>{

    Context context;
    CreationPdfFragment fragment;


    public PdfCreator(Context context, CreationPdfFragment fragment){
        this.context = context;
        this.fragment = fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected String doInBackground(View... views) {

        creationPdfRecap(views);
        creationPdfEnSavoirPlus(views);

        return "";
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void creationPdfRecap(View... views){
        DatabaseHelper dbHelper;
        Cursor cur;

        dbHelper = new DatabaseHelper(context);

        String nomEquipe;
        String lieu;

        double surface;
        int nbTiges;
        float volume;

        cur = dbHelper.getAllDataFromTable(DatabaseHelper.CONSTANTES_TABLE);
        cur.moveToFirst();
        nomEquipe = cur.getString(cur.getColumnIndex(DatabaseHelper.NOM_EQUIPE));
        surface = cur.getDouble(cur.getColumnIndex(DatabaseHelper.SURFACE_PARCELLE));
        lieu = cur.getString(cur.getColumnIndex(DatabaseHelper.LIEU_PARCELLE));

        cur = dbHelper.getAllDataFromTable(DatabaseHelper.ARBRES_PARCELLE_TABLE);
        cur.moveToFirst();
        nbTiges = cur.getCount();

        cur = dbHelper.getDataFromTable("SUM(" + DatabaseHelper.VOLUME_COMMERCIAL + ")", DatabaseHelper.ARBRES_PARCELLE_TABLE);
        cur.moveToFirst();
        volume = cur.getFloat(0);

        cur.close();
        dbHelper.close();

        DecimalFormat df = new DecimalFormat("#0.00");

        PrintAttributes attributes = new PrintAttributes.Builder()
                .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
                .setResolution(new PrintAttributes.Resolution("id", Context.PRINT_SERVICE, 200, 200))
                .setColorMode(PrintAttributes.COLOR_MODE_COLOR)
                .setMinMargins(PrintAttributes.Margins.NO_MARGINS)
                .build();

        PrintedPdfDocument document = new PrintedPdfDocument(context, attributes);

        /*
         *******************************************************************************************
         ***************************************** PAGE 0 ******************************************
         *******************************************************************************************
         */

        PdfDocument.Page page = document.startPage(0);

        Canvas pageCanvas = page.getCanvas();
        float pageWidth = pageCanvas.getWidth();
        float pageHeight = pageCanvas.getHeight();

        Rect src;
        RectF dst;

        float top;
        float bottom;
        float left;
        float right;

        float marginLeft = 20f;
        float marginRight = 20f;

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(context.getResources().getColor(R.color.colorPrimary));
        paint.setTypeface(ResourcesCompat.getFont(context, R.font.digital_medium));
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(30f);

        top = 40f;
        pageCanvas.drawText("Marteloscope biodiversité de " + lieu, pageWidth/2, top, paint);

        paint.setTextSize(24f);
        top = top + 30f;
        pageCanvas.drawText("Fiche récapitulative", pageWidth/2, top, paint);

        // Rectangle en-tête "Marteloscope du parc des Massifs des Bauges"
        paint = new Paint();
        paint.setColor(context.getResources().getColor(R.color.colorPrimary));
        paint.setStrokeWidth(5f);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        top = top + 30f;
        bottom = top + 100f;
        pageCanvas.drawRect(marginLeft,top,pageWidth/2 - marginLeft,bottom, paint);
        pageCanvas.drawRect(pageWidth/2 + marginLeft,top,pageWidth - marginRight,bottom, paint);

        // Texte "Equipe : <nomEquipe>"
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(context.getResources().getColor(R.color.colorWhite));
        paint.setTypeface(ResourcesCompat.getFont(context, R.font.lato_regular));
        paint.setTextSize(18f);

        bottom = top + 20f;

        pageCanvas.drawText("Equipe :", marginLeft +5f,bottom, paint);


        bottom = bottom + 70/2;

        pageCanvas.drawText(nomEquipe, marginLeft+5f, bottom, paint);

        // Texte "<date>"
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();

        bottom = bottom + 70/2;

        pageCanvas.drawText(dateFormat.format(date), marginLeft+5f, bottom, paint);


        // Texte "Commentaire :" à l'intérieur du rectangle

        paint.setTextSize(14f);

        bottom = top + 20f; // 170f
        pageCanvas.drawText("Caractéristiques de la parcelle :", pageWidth/2 + marginLeft + 5f, bottom, paint);

        bottom = bottom + 20f;
        pageCanvas.drawText("■ Surface : " + df.format(surface) + " ha", pageWidth/2 + marginLeft*2, bottom, paint);

        bottom = bottom + 25f;
        pageCanvas.drawText("■ Nombre de tiges : " + Integer.toString(nbTiges), pageWidth/2 + marginLeft*2, bottom, paint);

        bottom = bottom + 25f;
        pageCanvas.drawText("■ Volume total de bois : " + df.format(volume) + " m3", pageWidth/2 + marginLeft*2, bottom, paint);


        paint = new Paint();
        paint.setColor(context.getResources().getColor(R.color.colorPrimary));
        paint.setStrokeWidth(5f);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        top = bottom + 40f;
        bottom = top + 20f;
        pageCanvas.drawRect(marginLeft,top,pageCanvas.getWidth()-marginRight,bottom, paint);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(context.getResources().getColor(R.color.colorWhite));
        paint.setTypeface(ResourcesCompat.getFont(context, R.font.digital_medium));
        paint.setTextSize(15f);

        bottom = bottom - 5f;
        pageCanvas.drawText("Ai-je respecté les consignes ?", marginLeft + 5f, bottom, paint);


        /*
         ****************************************
         ******** View RESPECT CONSIGNE *********
         ****************************************
         */

        View mView = views[0];
        Bitmap bitmap = Bitmap.createBitmap(mView.getWidth(), mView.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        mView.draw(canvas);

        // Création d'un rectangle avec les dimensions de la View
        src = new Rect(0, 0, mView.getWidth(), mView.getHeight());

        // Mise à l'échelle de la View par rapport à la taille de la page
        float scale = Math.min(pageWidth / src.width(), pageHeight / src.height());

        // Positionnement de la View sur la page

        top = bottom + 10f;
        right = (float) ((src.width() * scale)/ 1.4);
        left = marginLeft*3 + 8f;
        right = right + left;
        bottom = (float) ((src.height() * scale)/ 1.4 + top);

        // Rectangle emplacement de la View sur la page
        dst = new RectF(left, top, right, bottom);

        pageCanvas.drawBitmap(bitmap, src, dst, null);


        paint = new Paint();
        paint.setColor(context.getResources().getColor(R.color.colorPrimary));
        paint.setStrokeWidth(5f);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);

        top = bottom + 20f;
        bottom = top + 20f;
        pageCanvas.drawRect(marginLeft,top,pageCanvas.getWidth()-marginRight,bottom, paint);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(context.getResources().getColor(R.color.colorWhite));
        paint.setTypeface(ResourcesCompat.getFont(context, R.font.digital_medium));
        paint.setTextSize(15f);

        bottom = bottom - 5f;
        pageCanvas.drawText("Résultat de votre martelage", marginLeft + 5f, bottom, paint);


        /*
         ****************************************
         ******** View SYNTHESE PICTO *********
         ****************************************
         */

        mView = views[1];
        bitmap = Bitmap.createBitmap(mView.getWidth(), mView.getHeight(),
                Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        mView.draw(canvas);

        // Création d'un rectangle avec les dimensions de la View
        src = new Rect(0, 0, mView.getWidth(), mView.getHeight());

        // Mise à l'échelle de la View par rapport à la taille de la page
        scale = Math.min(pageWidth / src.width(), pageHeight / src.height());

        // Positionnement de la View sur la page

        top = bottom + 10f;
        right = (float) ((src.width() * scale)/ 1.4);
        left = marginLeft*3 + 8f;;
        right = right + left;
        bottom = (float) ((src.height() * scale)/ 1.4 + top);

        // Rectangle emplacement de la View sur la page
        dst = new RectF(left, top, right, bottom);

        pageCanvas.drawBitmap(bitmap, src, dst, null);


        top = (pageHeight - 40);
        bottom = pageHeight - 10f;

        left = marginLeft;
        right = left + 30f;

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.logo_iseta_2);
        Drawable d = new BitmapDrawable(bitmap);
        d.setBounds((int)left,(int)top,(int)right,(int)bottom);
        d.draw(pageCanvas);

        left = right+5;
        right = left + 50f;

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.logo_ufp74_bauges);
        d = new BitmapDrawable(bitmap);
        d.setBounds((int)left,(int)top,(int)right,(int)bottom);
        d.draw(pageCanvas);

        left = right+5;
        right = left + 30f;

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.logo_crpf_3);
        d = new BitmapDrawable(bitmap);
        d.setBounds((int)left,(int)top,(int)right,(int)bottom);
        d.draw(pageCanvas);

        left = right+5;
        right = left + 70f;

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.logo_parc_geoparks_unesco_signature);
        d = new BitmapDrawable(bitmap);
        d.setBounds((int)left,(int)top,(int)right,(int)bottom);
        d.draw(pageCanvas);



        paint.setColor(context.getResources().getColor(R.color.colorBlack));
        paint.setTypeface(Typeface.DEFAULT);
        paint.setTextSize(12f);
        bottom = pageHeight - marginLeft;
        pageCanvas.drawText("1", pageWidth - marginRight, bottom, paint);

        document.finishPage(page);

        String name = "[Recapitulatif]" + nomEquipe + ".pdf";

        dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String dateDir = dateFormat.format(date);



        File root = Environment.getExternalStorageDirectory();
        File dir = new File(root.getAbsolutePath() + "/Documents/Martelapp/" + dateDir);
        dir.mkdirs();

        File file = new File(dir, name);
        FileOutputStream f;
        try {
            f = new FileOutputStream(file);

            document.writeTo(f);

        } catch (IOException e) {
            e.printStackTrace();
        }

        document.close();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void creationPdfEnSavoirPlus(View... views){
        DatabaseHelper dbHelper;
        Cursor cur;

        dbHelper = new DatabaseHelper(context);

        String nomEquipe;
        String lieu;

        double surface;
        int nbTiges;
        float volume;

        cur = dbHelper.getAllDataFromTable(DatabaseHelper.CONSTANTES_TABLE);
        cur.moveToFirst();
        nomEquipe = cur.getString(cur.getColumnIndex(DatabaseHelper.NOM_EQUIPE));
        surface = cur.getDouble(cur.getColumnIndex(DatabaseHelper.SURFACE_PARCELLE));
        lieu = cur.getString(cur.getColumnIndex(DatabaseHelper.LIEU_PARCELLE));

        cur = dbHelper.getAllDataFromTable(DatabaseHelper.ARBRES_PARCELLE_TABLE);
        cur.moveToFirst();
        nbTiges = cur.getCount();

        cur = dbHelper.getDataFromTable("SUM(" + DatabaseHelper.VOLUME_COMMERCIAL + ")", DatabaseHelper.ARBRES_PARCELLE_TABLE);
        cur.moveToFirst();
        volume = cur.getFloat(0);

        cur.close();
        dbHelper.close();

        DecimalFormat df = new DecimalFormat("#0.00");

        PrintAttributes attributes = new PrintAttributes.Builder()
                .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
                .setResolution(new PrintAttributes.Resolution("id", Context.PRINT_SERVICE, 200, 200))
                .setColorMode(PrintAttributes.COLOR_MODE_COLOR)
                .setMinMargins(PrintAttributes.Margins.NO_MARGINS)
                .build();

        PrintedPdfDocument document = new PrintedPdfDocument(context, attributes);

        /*
         *******************************************************************************************
         ***************************************** PAGE 0 ******************************************
         *******************************************************************************************
         */

        PdfDocument.Page page = document.startPage(0);

        Canvas pageCanvas = page.getCanvas();
        float pageWidth = pageCanvas.getWidth();
        float pageHeight = pageCanvas.getHeight();

        Rect src;
        RectF dst;

        float top;
        float bottom;
        float left;
        float right;

        float marginLeft = 20f;
        float marginRight = 20f;

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(context.getResources().getColor(R.color.colorPrimary));
        paint.setTypeface(ResourcesCompat.getFont(context, R.font.digital_medium));
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(30f);

        top = 40f;
        pageCanvas.drawText("Marteloscope biodiversité de " + lieu, pageWidth/2, top, paint);

        paint.setTextSize(24f);
        top = top + 30f;
        pageCanvas.drawText("Pour en savoir plus", pageWidth/2, top, paint);

        // Rectangle en-tête "Marteloscope du parc des Massifs des Bauges"
        paint = new Paint();
        paint.setColor(context.getResources().getColor(R.color.colorPrimary));
        paint.setStrokeWidth(5f);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        top = top + 30f;
        bottom = top + 100f;
        pageCanvas.drawRect(marginLeft,top,pageWidth/2 - marginLeft,bottom, paint);
        pageCanvas.drawRect(pageWidth/2 + marginLeft,top,pageWidth - marginRight,bottom, paint);

        // Texte "Equipe : <nomEquipe>"
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(context.getResources().getColor(R.color.colorWhite));
        paint.setTypeface(ResourcesCompat.getFont(context, R.font.lato_regular));
        paint.setTextSize(18f);

        bottom = top + 20f;

        pageCanvas.drawText("Equipe :", marginLeft +5f,bottom, paint);


        bottom = bottom + 70/2;

        pageCanvas.drawText(nomEquipe, marginLeft+5f, bottom, paint);

        // Texte "<date>"
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();

        bottom = bottom + 70/2;

        pageCanvas.drawText(dateFormat.format(date), marginLeft+5f, bottom, paint);


        // Texte "Commentaire :" à l'intérieur du rectangle

        paint.setTextSize(14f);

        bottom = top + 20f; // 170f
        pageCanvas.drawText("Caractéristiques de la parcelle :", pageWidth/2 + marginLeft + 5f, bottom, paint);

        bottom = bottom + 20f;
        pageCanvas.drawText("■ Surface : " + df.format(surface) + " ha", pageWidth/2 + marginLeft*2, bottom, paint);

        bottom = bottom + 25f;
        pageCanvas.drawText("■ Nombre de tiges : " + Integer.toString(nbTiges), pageWidth/2 + marginLeft*2, bottom, paint);

        bottom = bottom + 25f;
        pageCanvas.drawText("■ Volume total de bois : " + df.format(volume) + " m3", pageWidth/2 + marginLeft*2, bottom, paint);

        paint = new Paint();
        paint.setColor(context.getResources().getColor(R.color.colorPrimary));
        paint.setStrokeWidth(5f);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        top = bottom + 40f;
        bottom = top + 20f;
        pageCanvas.drawRect(marginLeft,top,pageCanvas.getWidth()-marginRight,bottom, paint);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(context.getResources().getColor(R.color.colorWhite));
        paint.setTypeface(ResourcesCompat.getFont(context, R.font.digital_medium));
        paint.setTextSize(15f);


        bottom = bottom - 5f;
        pageCanvas.drawText("Répartition du prélèvement, Volume / Diamètre", marginLeft + 5f, bottom, paint);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(context.getResources().getColor(R.color.pdfGrey));
        paint.setTypeface(Typeface.DEFAULT);
        paint.setTextSize(10f);

        bottom = bottom + 38f;
        pageCanvas.drawText("Volume (m3)", marginLeft + 5f, bottom, paint);

        View mView = views[2];
        Bitmap bitmap = Bitmap.createBitmap(mView.getWidth(), mView.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        mView.draw(canvas);

        // Création d'un rectangle avec les dimensions de la View
        src = new Rect(0, 0, mView.getWidth(), mView.getHeight());

        // Mise à l'échelle de la View par rapport à la taille de la page
        float scale = Math.min(pageWidth / src.width(), pageHeight / src.height());

        // Positionnement de la View sur la page

        top = bottom + 2f;
        left = marginLeft;
        right = ((src.width() * scale) - marginRight);
        bottom =  ((src.height() * scale) + top - marginLeft);

        // Rec
        dst = new RectF(left, top, right, bottom);

        pageCanvas.drawBitmap(bitmap, src, dst, null);

        paint.setColor(context.getResources().getColor(R.color.colorBlack));
        paint.setTypeface(Typeface.DEFAULT);
        paint.setTextSize(12f);
        bottom = pageHeight - marginLeft;
        pageCanvas.drawText("1", pageWidth - marginRight, bottom, paint);

        document.finishPage(page);


        /*
         *******************************************************************************************
         ***************************************** PAGE 1 ******************************************
         *******************************************************************************************
         */

        page = document.startPage(1);
        pageCanvas = page.getCanvas();

        /*
         ***************************************************************************
         ******** View GRAPHE EVOLUTION NB TIGES PAR CATEGORIE DE DIAMETRE *********
         ***************************************************************************
         */

        paint = new Paint();
        paint.setColor(context.getResources().getColor(R.color.colorPrimary));
        paint.setStrokeWidth(5f);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        top = 40f;
        bottom = top + 20f;
        pageCanvas.drawRect(marginLeft,top,pageCanvas.getWidth()-marginRight,bottom, paint);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(context.getResources().getColor(R.color.colorWhite));
        paint.setTypeface(ResourcesCompat.getFont(context, R.font.digital_medium));
        paint.setTextSize(15f);


        bottom = bottom - 5f;
        pageCanvas.drawText("Répartition du prélèvement, Nombre de tiges / Essence", marginLeft + 5f, bottom, paint);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(context.getResources().getColor(R.color.pdfGrey));
        paint.setTypeface(Typeface.DEFAULT);
        paint.setTextSize(10f);

        bottom = bottom + 38f;
        pageCanvas.drawText("Nombre de tiges", marginLeft + 5f, bottom, paint);

        mView = views[3];
        bitmap = Bitmap.createBitmap(mView.getWidth(), mView.getHeight(),
                Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        mView.draw(canvas);

        // Création d'un rectangle avec les dimensions de la View
        src = new Rect(0, 0, mView.getWidth(), mView.getHeight());

        // Mise à l'échelle de la View par rapport à la taille de la page
        scale = Math.min(pageWidth / src.width(), pageHeight / src.height());

        // Positionnement de la View sur la page

        top = bottom + 2f;
        left = marginLeft;
        right = ((src.width() * scale) - marginRight);
        bottom =  ((src.height() * scale) + top - marginLeft);

        // Rec
        dst = new RectF(left, top, right, bottom);

        pageCanvas.drawBitmap(bitmap, src, dst, null);


        /*
         *************************************************
         ******** View GRAPHE NB TIGES / ESSENCE *********
         *************************************************
         */

        paint = new Paint();
        paint.setColor(context.getResources().getColor(R.color.colorPrimary));
        paint.setStrokeWidth(5f);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);

        top = bottom + 60f;
        bottom = top + 20f;
        pageCanvas.drawRect(marginLeft,top,pageCanvas.getWidth()-marginRight,bottom, paint);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(context.getResources().getColor(R.color.colorWhite));
        paint.setTypeface(ResourcesCompat.getFont(context, R.font.digital_medium));
        paint.setTextSize(15f);

        bottom = bottom - 5f;
        pageCanvas.drawText("Répartition du prélèvement, Nombre de tiges / Diamètre", marginLeft + 5f, bottom, paint);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(context.getResources().getColor(R.color.pdfGrey));
        paint.setTypeface(Typeface.DEFAULT);
        paint.setTextSize(10f);

        bottom = bottom + 38f;
        pageCanvas.drawText("Nombre de tiges", marginLeft + 5f, bottom, paint);

        mView = views[4];
        bitmap = Bitmap.createBitmap(mView.getWidth(), mView.getHeight(),
                Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        mView.draw(canvas);

        // Création d'un rectangle avec les dimensions de la View
        src = new Rect(0, 0, mView.getWidth(), mView.getHeight());

        // Mise à l'échelle de la View par rapport à la taille de la page
        scale = Math.min(pageWidth / src.width(), pageHeight / src.height());

        // Positionnement de la View sur la page

        top = bottom + 2f;
        left = marginLeft;
        right = ((src.width() * scale) - marginRight);
        bottom =  ((src.height() * scale) + top - marginLeft);

        // Rec
        dst = new RectF(left, top, right, bottom);

        pageCanvas.drawBitmap(bitmap, src, dst, null);


        paint.setColor(context.getResources().getColor(R.color.colorBlack));
        paint.setTypeface(Typeface.DEFAULT);
        paint.setTextSize(12f);
        bottom = pageHeight - marginLeft;
        pageCanvas.drawText("2", pageWidth - marginRight, bottom, paint);

        document.finishPage(page);

        /*
         *******************************************************************************************
         ***************************************** PAGE 2 ******************************************
         *******************************************************************************************
         */

        page = document.startPage(2);
        pageCanvas = page.getCanvas();



        /*
         ***************************************************************************
         ******** View GRAPHE EVOLUTION NB TIGES PAR CATEGORIE DE DIAMETRE *********
         ***************************************************************************
         */

        paint = new Paint();
        paint.setColor(context.getResources().getColor(R.color.colorPrimary));
        paint.setStrokeWidth(5f);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        top = 40f;
        bottom = top + 20f;
        pageCanvas.drawRect(marginLeft,top,pageCanvas.getWidth()-marginRight,bottom, paint);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(context.getResources().getColor(R.color.colorWhite));
        paint.setTypeface(ResourcesCompat.getFont(context, R.font.digital_medium));
        paint.setTextSize(15f);


        bottom = bottom - 5f;
        pageCanvas.drawText("Répartition du prélèvement, Nombre de tiges / Note écologique", marginLeft + 5f, bottom, paint);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(context.getResources().getColor(R.color.pdfGrey));
        paint.setTypeface(Typeface.DEFAULT);
        paint.setTextSize(10f);

        bottom = bottom + 38f;
        pageCanvas.drawText("Nombre de tiges", marginLeft + 5f, bottom, paint);

        mView = views[5];
        bitmap = Bitmap.createBitmap(mView.getWidth(), mView.getHeight(),
                Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        mView.draw(canvas);

        // Création d'un rectangle avec les dimensions de la View
        src = new Rect(0, 0, mView.getWidth(), mView.getHeight());

        // Mise à l'échelle de la View par rapport à la taille de la page
        scale = Math.min(pageWidth / src.width(), pageHeight / src.height());

        // Positionnement de la View sur la page

        top = bottom + 2f;
        left = marginLeft;
        right = ((src.width() * scale) - marginRight);
        bottom =  ((src.height() * scale) + top - marginLeft);

        // Rec
        dst = new RectF(left, top, right, bottom);

        pageCanvas.drawBitmap(bitmap, src, dst, null);



        /*
         ***************************************************************************
         ******** View GRAPHE EVOLUTION NB TIGES PAR CATEGORIE DE DIAMETRE *********
         ***************************************************************************
         */

        paint = new Paint();
        paint.setColor(context.getResources().getColor(R.color.colorPrimary));
        paint.setStrokeWidth(5f);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);

        top = bottom + 60f;
        bottom = top + 20f;
        pageCanvas.drawRect(marginLeft,top,pageCanvas.getWidth()-marginRight,bottom, paint);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(context.getResources().getColor(R.color.colorWhite));
        paint.setTypeface(ResourcesCompat.getFont(context, R.font.digital_medium));
        paint.setTextSize(15f);

        bottom = bottom - 5f;
        pageCanvas.drawText("Evolution du nombre de tiges par classe de diamètre", marginLeft + 5f, bottom, paint);


        mView = views[6];
        bitmap = Bitmap.createBitmap(mView.getWidth(), mView.getHeight(),
                Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        mView.draw(canvas);

        // Création d'un rectangle avec les dimensions de la View
        src = new Rect(0, 0, mView.getWidth(), mView.getHeight());

        // Mise à l'échelle de la View par rapport à la taille de la page
        scale = Math.min(pageWidth / src.width(), pageHeight / src.height());

        // Positionnement de la View sur la page

        top = bottom + 40f;
        left = marginLeft;
        right = ((src.width() * scale) - marginRight);
        bottom =  ((src.height() * scale) + top - marginLeft);

        // Rec
        dst = new RectF(left, top, right, bottom);

        pageCanvas.drawBitmap(bitmap, src, dst, null);

        paint.setColor(context.getResources().getColor(R.color.colorBlack));
        paint.setTypeface(Typeface.DEFAULT);
        paint.setTextSize(12f);
        bottom = pageHeight - marginLeft;
        pageCanvas.drawText("3", pageWidth - marginRight, bottom, paint);

        document.finishPage(page);


        /*
         *******************************************************************************************
         ***************************************** PAGE 3 ******************************************
         *******************************************************************************************
         */

        page = document.startPage(3);
        pageCanvas = page.getCanvas();



        /*
         ***************************************************************************
         ******** View GRAPHE EVOLUTION NB TIGES PAR CATEGORIE DE DIAMETRE *********
         ***************************************************************************
         */

        paint = new Paint();
        paint.setColor(context.getResources().getColor(R.color.colorPrimary));
        paint.setStrokeWidth(5f);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        top = 40f;
        bottom = top + 20f;
        pageCanvas.drawRect(marginLeft,top,pageCanvas.getWidth()-marginRight,bottom, paint);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(context.getResources().getColor(R.color.colorWhite));
        paint.setTypeface(ResourcesCompat.getFont(context, R.font.digital_medium));
        paint.setTextSize(15f);


        bottom = bottom - 5f;
        pageCanvas.drawText("Pourcentage des raisons de martelage", marginLeft + 5f, bottom, paint);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(context.getResources().getColor(R.color.pdfGrey));
        paint.setTypeface(Typeface.DEFAULT);
        paint.setTextSize(10f);

        bottom = bottom + 38f;
        pageCanvas.drawText("Pourcentage (Nombre raison / Total arbres martelés)", marginLeft + 5f, bottom, paint);

        mView = views[7];
        bitmap = Bitmap.createBitmap(mView.getWidth(), mView.getHeight(),
                Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        mView.draw(canvas);

        // Création d'un rectangle avec les dimensions de la View
        src = new Rect(0, 0, mView.getWidth(), mView.getHeight());

        // Mise à l'échelle de la View par rapport à la taille de la page
        scale = Math.min(pageWidth / src.width(), pageHeight / src.height());

        // Positionnement de la View sur la page

        top = bottom  + 2f;
        left = marginLeft;
        right = ((src.width() * scale) - marginRight);
        bottom =  ((src.height() * scale) + top - marginLeft);

        // Rec
        dst = new RectF(left, top, right, bottom);

        pageCanvas.drawBitmap(bitmap, src, dst, null);



        /*
         *************************************************
         ******** View GRAPHE NB TIGES / ESSENCE *********
         *************************************************
         */

        paint = new Paint();
        paint.setColor(context.getResources().getColor(R.color.colorPrimary));
        paint.setStrokeWidth(5f);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);

        top = bottom + 30f;
        bottom = top + 20f;
        pageCanvas.drawRect(marginLeft,top,pageCanvas.getWidth()-marginRight,bottom, paint);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(context.getResources().getColor(R.color.colorWhite));
        paint.setTypeface(ResourcesCompat.getFont(context, R.font.digital_medium));
        paint.setTextSize(15f);

        bottom = bottom - 5f;
        pageCanvas.drawText("Récapitulatif géographique de l'exercice", marginLeft + 5f, bottom, paint);

        mView = views[8];
        bitmap = Bitmap.createBitmap(mView.getWidth(), mView.getHeight(),
                Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        mView.draw(canvas);

        // Création d'un rectangle avec les dimensions de la View
        src = new Rect(0, 0, mView.getWidth(), mView.getHeight());

        // Mise à l'échelle de la View par rapport à la taille de la page
        scale = Math.min(pageWidth / src.width(), pageHeight / src.height());

        // Positionnement de la View sur la page

        top = bottom + 40f;
        left = marginLeft;
        right = ((src.width() * scale) - marginRight);
        bottom =  ((src.height() * scale) + top - marginLeft);

        // Rec
        dst = new RectF(left, top, right, bottom);

        pageCanvas.drawBitmap(bitmap, src, dst, null);

        paint.setColor(context.getResources().getColor(R.color.colorBlack));
        paint.setTypeface(Typeface.DEFAULT);
        paint.setTextSize(12f);
        bottom = pageHeight - marginLeft;
        pageCanvas.drawText("4", pageWidth - marginRight, bottom, paint);

        document.finishPage(page);

        String name = "[EnSavoirPlus]" + nomEquipe + ".pdf";

        dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String dateDir = dateFormat.format(date);



        File root = Environment.getExternalStorageDirectory();
        File dir = new File(root.getAbsolutePath() + "/Documents/Martelapp/" + dateDir);
        dir.mkdirs();

        File file = new File(dir, name);
        FileOutputStream f;
        try {
            f = new FileOutputStream(file);

            document.writeTo(f);

        } catch (IOException e) {
            e.printStackTrace();
        }

        document.close();
    }
}
