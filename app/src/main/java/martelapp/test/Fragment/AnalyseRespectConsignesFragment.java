package martelapp.test.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import martelapp.test.R;

/**
 * Created by cimin on 24/04/2018.
 */

public class AnalyseRespectConsignesFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_page_respect_consignes, null);

        return view;
    }
}
