package martelapp.test.Class;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.pdf.PdfDocument;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.print.PrintAttributes;
import android.print.pdf.PrintedPdfDocument;
import android.support.annotation.RequiresApi;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import martelapp.test.R;

/**
 * Created by Baptiste on 20/05/2018.
 */

public class PdfCreator extends AsyncTask<View, String, String>{

    Context context;

    public PdfCreator(Context context){
        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected String doInBackground(View... views) {
        DatabaseHelper dbHelper;
        Cursor cur;

        dbHelper = new DatabaseHelper(context);

        String nomEquipe;

        double surface;
        int nbTiges;
        float volume;

        cur = dbHelper.getAllDataFromTable(DatabaseHelper.CONSTANTES_TABLE);
        cur.moveToFirst();
        nomEquipe = cur.getString(cur.getColumnIndex(DatabaseHelper.NOM_EQUIPE));
        surface = cur.getDouble(cur.getColumnIndex(DatabaseHelper.SURFACE_PARCELLE));

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

        // Rectangle en-tête "Marteloscope du parc des Massifs des Bauges"
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(5f);
        paint.setStyle(Paint.Style.STROKE);
        top = 5f;
        bottom = 100f;
        pageCanvas.drawRect(marginLeft,top,pageCanvas.getWidth()-marginRight,bottom, paint);

        // Texte "Equipe : <nomEquipe>"
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(context.getResources().getColor(R.color.colorBlack));
        paint.setTextSize(14f);

        bottom = bottom + 30f; // 120f

        pageCanvas.drawText("Equipe : " + nomEquipe, marginLeft ,bottom, paint);

        // Texte "<date>"
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        pageCanvas.drawText(dateFormat.format(date), pageCanvas.getWidth() - 100, bottom, paint);


        // Texte "Commentaire :" à l'intérieur du rectangle
        bottom = bottom + 30f; // 170f
        pageCanvas.drawText("Caractéristiques de la parcelle :", marginLeft, bottom, paint);

        bottom = bottom + 25f;
        pageCanvas.drawText("• Surface : " + df.format(surface) + " ha", marginLeft*2, bottom, paint);

        bottom = bottom + 25f;
        pageCanvas.drawText("• Nombre de tiges : " + Integer.toString(nbTiges), marginLeft*2, bottom, paint);

        bottom = bottom + 25f;
        pageCanvas.drawText("• Volume total de bois : " + df.format(volume) + " m3", marginLeft*2, bottom, paint);


        // Rectangle retour sur l'exercice, commentaire/appréciation
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(1f);
        paint.setStyle(Paint.Style.STROKE);

        bottom = bottom + 30f; // 350f
        pageCanvas.drawLine(marginLeft * 3, bottom, pageWidth - (marginRight * 3), bottom, paint);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(context.getResources().getColor(R.color.colorBlack));
        paint.setTextSize(16f);

        bottom = bottom + 40f; // 385f
        pageCanvas.drawText("Ai-je respecté les consignes ?", marginLeft, bottom, paint);


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

        top = bottom;
        right = (float) ((src.width() * scale)/ 1.4);
        left = marginLeft*3 + 8f;
        right = right + left;
        bottom = (float) ((src.height() * scale)/ 1.4 + top);

        // Rectangle emplacement de la View sur la page
        dst = new RectF(left, top, right, bottom);

        pageCanvas.drawBitmap(bitmap, src, dst, null);


        bottom = bottom + 30f;
        pageCanvas.drawText("Votre martelage permet de :", marginLeft, bottom, paint);


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

        top = bottom;
        right = (float) ((src.width() * scale)/ 1.4);
        left = marginLeft*3 + 8f;;
        right = right + left;
        bottom = (float) ((src.height() * scale)/ 1.4 + top);

        // Rectangle emplacement de la View sur la page
        dst = new RectF(left, top, right, bottom);

        pageCanvas.drawBitmap(bitmap, src, dst, null);

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

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(context.getResources().getColor(R.color.colorBlack));
        paint.setTextSize(18f);

        bottom = 50f;
        pageCanvas.drawText("II) Pour en savoir plus", marginLeft, bottom, paint);


        paint.setTextSize(14f);




        /*
         *************************************************
         ******** View GRAPHE NB TIGES / ESSENCE *********
         *************************************************
         */


        bottom = bottom + 40f; // 420f
        pageCanvas.drawText("1) Répartition du prélèvement, volume/diamètre\n", marginLeft*2, bottom, paint);


        mView = views[2];
        bitmap = Bitmap.createBitmap(mView.getWidth(), mView.getHeight(),
                Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        mView.draw(canvas);

        // Création d'un rectangle avec les dimensions de la View
        src = new Rect(0, 0, mView.getWidth(), mView.getHeight());

        // Mise à l'échelle de la View par rapport à la taille de la page
        scale = Math.min(pageWidth / src.width(), pageHeight / src.height());

        // Positionnement de la View sur la page

        top = bottom + 20f;
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

        bottom = bottom + 50f;
        pageCanvas.drawText("2) Graphes Nombre de tiges", marginLeft*2, bottom, paint);

        bottom = bottom + 40f;
        pageCanvas.drawText("a) Répartition du prélèvement, nombre de tiges/essence\n", marginLeft*3, bottom, paint);


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

        top = bottom + 20f;
        left = marginLeft;
        right = ((src.width() * scale) - marginRight);
        bottom =  ((src.height() * scale) + top - marginLeft);

        // Rec
        dst = new RectF(left, top, right, bottom);

        pageCanvas.drawBitmap(bitmap, src, dst, null);

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

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(context.getResources().getColor(R.color.colorBlack));

        paint.setTextSize(14f);




        /*
         *************************************************
         ******** View GRAPHE NB TIGES / ESSENCE *********
         *************************************************
         */


        bottom = 50f;
        pageCanvas.drawText("b) Répartition du prélèvement, Nombre de tiges / Diamètre", marginLeft*3, bottom, paint);

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

        top = bottom + 20f;
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

        bottom = bottom + 50f;
        pageCanvas.drawText("c) Répartition du prélèvement, Nombre de tiges / Note écologique", marginLeft*3, bottom, paint);


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

        top = bottom + 20f;
        left = marginLeft;
        right = ((src.width() * scale) - marginRight);
        bottom =  ((src.height() * scale) + top - marginLeft);

        // Rec
        dst = new RectF(left, top, right, bottom);

        pageCanvas.drawBitmap(bitmap, src, dst, null);

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

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(context.getResources().getColor(R.color.colorBlack));
        paint.setTextSize(18f);

        paint.setTextSize(14f);




        /*
         *************************************************
         ******** View GRAPHE NB TIGES / ESSENCE *********
         *************************************************
         */


        bottom = 50f; // 420f
        pageCanvas.drawText("d) Evolution du nombre de tiges par classe de diamètre", marginLeft*3, bottom, paint);

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

        top = bottom + 20f;
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

        bottom = bottom + 50f;
        pageCanvas.drawText("3) Pourcentage des raisons de martelage", marginLeft*2, bottom, paint);


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

        top = bottom + 20f;
        left = marginLeft;
        right = ((src.width() * scale) - marginRight);
        bottom =  ((src.height() * scale) + top - marginLeft);

        // Rec
        dst = new RectF(left, top, right, bottom);

        pageCanvas.drawBitmap(bitmap, src, dst, null);

        paint.setTextSize(12f);
        bottom = pageHeight - marginLeft;
        pageCanvas.drawText("4", pageWidth - marginRight, bottom, paint);

        document.finishPage(page);

        /*
         *******************************************************************************************
         ***************************************** PAGE 4 ******************************************
         *******************************************************************************************
         */

        page = document.startPage(1);
        pageCanvas = page.getCanvas();

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(context.getResources().getColor(R.color.colorBlack));
        paint.setTextSize(18f);

        paint.setTextSize(14f);




        /*
         *************************************************
         ******** View GRAPHE NB TIGES / ESSENCE *********
         *************************************************
         */


        bottom = 50f; // 420f
        pageCanvas.drawText("4) Carte de la parcelle", marginLeft*3, bottom, paint);

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

        top = bottom + 20f;
        left = marginLeft;
        right = ((src.width() * scale) - marginRight);
        bottom =  ((src.height() * scale) + top - marginLeft);

        // Rec
        dst = new RectF(left, top, right, bottom);

        pageCanvas.drawBitmap(bitmap, src, dst, null);

        paint.setTextSize(12f);
        bottom = pageHeight - marginLeft;
        pageCanvas.drawText("5", pageWidth - marginRight, bottom, paint);

        document.finishPage(page);

        File root = Environment.getExternalStorageDirectory();
        File dir = new File(root.getAbsolutePath() + "/documents");
        dir.mkdirs();

        File file = new File(dir, "test.pdf");
        FileOutputStream f;
        try {
            f = new FileOutputStream(file);

            document.writeTo(f);
            System.out.println("C'est bon sa passe");

        } catch (IOException e) {
            e.printStackTrace();
        }

        document.close();

        return "";
    }
}
