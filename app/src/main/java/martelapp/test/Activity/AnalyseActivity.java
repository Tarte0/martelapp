package martelapp.test.Activity;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Environment;
import android.print.PrintAttributes;
import android.print.pdf.PrintedPdfDocument;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import martelapp.test.Class.DatabaseHelper;
import martelapp.test.Fragment.Analyse.AnalyseGrapheCarteFragment;
import martelapp.test.Fragment.Analyse.AnalyseGrapheRaisonsFragment;
import martelapp.test.Fragment.Analyse.AnalyseGrapheVolumeFragment;
import martelapp.test.Fragment.Analyse.AnalyseListeArbresSelectionnesFragment;
import martelapp.test.Fragment.Analyse.AnalyseResultatFragment;
import martelapp.test.Fragment.Analyse.AnalyseGrapheNombreTigesFragment;
import martelapp.test.R;


public class AnalyseActivity extends AppCompatActivity {
    /**
     * Viewpager de l'activité utilisé pour creer plusieurs fragments
     */
    ViewPager viewPager;

    AnalyseResultatFragment analyseResultatFragment;
    AnalyseListeArbresSelectionnesFragment analyseListeArbresSelectionnesFragment;
    AnalyseGrapheNombreTigesFragment analyseGrapheNombreTigesFragment;

    View listeViewPdf[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyse);



        // On configure notre ViewPager pour chaque onglet
        // Un fragment pour chaque onglet
        viewPager = findViewById(R.id.viewpagerAnalyse);
        setupViewPager(viewPager);

        // On associe nos onglets avec le viewpager
        final TabLayout tabs = (TabLayout) findViewById(R.id.tabsAnalyse);
        tabs.setupWithViewPager(viewPager);

        listeViewPdf = new View[4];

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

    // Ajout et associations des Fragments aux onglets
    private void setupViewPager(ViewPager viewPager) {
        ExerciceActivity.Adapter adapter = new ExerciceActivity.Adapter(getSupportFragmentManager());
        analyseResultatFragment = new AnalyseResultatFragment();
        analyseResultatFragment.setVp(viewPager);

        analyseListeArbresSelectionnesFragment = new AnalyseListeArbresSelectionnesFragment();
        analyseListeArbresSelectionnesFragment.setVp(viewPager);

        analyseGrapheNombreTigesFragment = new AnalyseGrapheNombreTigesFragment();
        analyseGrapheNombreTigesFragment.setVp(viewPager);

        adapter.addFragment(analyseResultatFragment, "Résultats");
        adapter.addFragment(analyseListeArbresSelectionnesFragment, "Arbres Sélectionnes");
        adapter.addFragment(new AnalyseGrapheVolumeFragment(), "Graphe volume");
        adapter.addFragment(analyseGrapheNombreTigesFragment, "Graphe nombre de tiges");
        adapter.addFragment(new AnalyseGrapheRaisonsFragment(), "Graphe raisons");
        adapter.addFragment(new AnalyseGrapheCarteFragment(), "Carte parcelle");
        viewPager.setAdapter(adapter);
    }


    static class Adapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void onBackPressed() {

    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void createPdf(){
        DatabaseHelper dbHelper;
        Cursor cur;

        dbHelper = new DatabaseHelper(getApplicationContext());

        String nomEquipe;

        cur = dbHelper.getAllDataFromTable(DatabaseHelper.CONSTANTES_TABLE);
        cur.moveToFirst();
        nomEquipe = cur.getString(cur.getColumnIndex(DatabaseHelper.NOM_EQUIPE));
        cur.close();
        dbHelper.close();

        PrintAttributes attributes = new PrintAttributes.Builder()
                .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
                .setResolution(new PrintAttributes.Resolution("id", Context.PRINT_SERVICE, 200, 200))
                .setColorMode(PrintAttributes.COLOR_MODE_COLOR)
                .setMinMargins(PrintAttributes.Margins.NO_MARGINS)
                .build();

        PrintedPdfDocument document = new PrintedPdfDocument(getApplicationContext(), attributes);

        /*
         ************************************************************************
         ******************************** PAGE 0 ********************************
         ************************************************************************
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
        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(5f);
        paint.setStyle(Paint.Style.STROKE);
        top = 5f;
        bottom = 100f;
        pageCanvas.drawRect(marginLeft,top,pageCanvas.getWidth()-marginRight,bottom, paint);

        // Texte "Equipe : <nomEquipe>"
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);
        paint.setTextSize(14f);

        bottom = bottom + 20f; // 120f

        pageCanvas.drawText("Equipe : " + nomEquipe, marginLeft ,bottom, paint);

        // Texte "<date>"
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        pageCanvas.drawText(dateFormat.format(date), pageCanvas.getWidth() - 100, bottom, paint);


        // Texte "Commentaire :" à l'intérieur du rectangle
        bottom = bottom + 50f; // 170f
        pageCanvas.drawText("Commentaire sur l'exercice : ", 25f, bottom, paint);


        // Rectangle retour sur l'exercice, commentaire/appréciation
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(1f);
        paint.setStyle(Paint.Style.STROKE);

        bottom = bottom + 100f; // 350f
        pageCanvas.drawRect(marginLeft, 150f,pageCanvas.getWidth() - marginRight, bottom, paint);


        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);
        paint.setTextSize(18f);

        bottom = bottom + 35f; // 385f
        pageCanvas.drawText("I) Synthèse de l'exercice", marginLeft, bottom, paint);

        paint.setTextSize(14f);

        bottom = bottom + 35f; // 420f
        pageCanvas.drawText("1) Respect des consignes", marginLeft*3, bottom, paint);


        /*
         ****************************************
         ******** View RESPECT CONSIGNE *********
         ****************************************
         */

        View mView = listeViewPdf[0];
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
        right = (float) ((src.width() * scale)/ 1.5);
        left = marginLeft*3 + 8f;
        right = right + left;
        bottom = (float) ((src.height() * scale)/ 1.5 + top);

        // Rectangle emplacement de la View sur la page
        dst = new RectF(left, top, right, bottom);

        pageCanvas.drawBitmap(bitmap, src, dst, null);


        bottom = bottom + 10f;
        pageCanvas.drawText("2) Résultat du martelage", marginLeft*3, bottom, paint);


        /*
         ****************************************
         ******** View SYNTHESE PICTO *********
         ****************************************
         */

        mView = listeViewPdf[1];
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
        right = (float) ((src.width() * scale)/ 1.5);
        left = marginLeft*3 + 8f;;
        right = right + left;
        bottom = (float) ((src.height() * scale)/ 1.5 + top);

        // Rectangle emplacement de la View sur la page
        dst = new RectF(left, top, right, bottom);

        pageCanvas.drawBitmap(bitmap, src, dst, null);

        document.finishPage(page);


        /*
         ************************************************************************
         ******************************** PAGE 1 ********************************
         ************************************************************************
         */

        page = document.startPage(1);
        pageCanvas = page.getCanvas();


        // View graphe Nombre de tiges / Diamètre
        mView = listeViewPdf[2];
        bitmap = Bitmap.createBitmap(mView.getWidth(), mView.getHeight(),
                Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        mView.draw(canvas);

        // Création d'un rectangle avec les dimensions de la View
        src = new Rect(0, 0, mView.getWidth(), mView.getHeight());

        // Mise à l'échelle de la View par rapport à la taille de la page
        scale = Math.min(pageWidth / src.width(), pageHeight / src.height());

        // Positionnement de la View sur la page

        top = 20f;
        left = marginLeft / 2;
        right = (src.width() * scale)/2 + left - (marginRight / 2);
        bottom = (src.height() * scale)/2 + top - (marginRight / 2);

        // Rec
        dst = new RectF(left, top, right, bottom);

        pageCanvas.drawBitmap(bitmap, src, dst, null);


        // View graphe Nombre de tiges / Essence
        mView = listeViewPdf[3];
        bitmap = Bitmap.createBitmap(mView.getWidth(), mView.getHeight(),
                Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        mView.draw(canvas);

        // Création d'un rectangle avec les dimensions de la View
        src = new Rect(0, 0, mView.getWidth(), mView.getHeight());

        // Mise à l'échelle de la View par rapport à la taille de la page
        scale = Math.min(pageWidth / src.width(), pageHeight / src.height());

        // Positionnement de la View sur la page


        left = right;
        right = (src.width() * scale)/2 + left - 10f;
        bottom = (src.height() * scale)/2 + top - 10f;

        // Rec
        dst = new RectF(left, top, right, bottom);

        pageCanvas.drawBitmap(bitmap, src, dst, null);

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
    }

    public void addViewPdf(View view, int index){
        listeViewPdf[index] = view;
    }
}

