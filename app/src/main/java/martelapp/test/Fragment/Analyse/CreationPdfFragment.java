package martelapp.test.Fragment.Analyse;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import martelapp.test.Activity.AnalyseActivity;
import martelapp.test.R;

/**
 * Created by Baptiste on 20/05/2018.
 */

public class CreationPdfFragment extends DialogFragment {

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.view_page_creation_pdf, null);

        return view;
    }
}
