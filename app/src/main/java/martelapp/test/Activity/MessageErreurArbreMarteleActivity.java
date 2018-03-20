package martelapp.test.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import martelapp.test.R;

public class MessageErreurArbreMarteleActivity extends AppCompatActivity {
    TextView mTextView;
    Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_erreur_arbre_martele);

        mTextView = (TextView) findViewById(R.id.textView);
        mButton = (Button) findViewById(R.id.button);


        mTextView.setText("Il ne fallait pas couper cet Arbre !!!");

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MessageErreurArbreMarteleActivity.this.finish();
            }
        });


    }
}
