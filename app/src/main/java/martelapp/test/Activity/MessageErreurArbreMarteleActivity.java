package martelapp.test.Activity;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import martelapp.test.R;


/**
 * MessageErreurArbreMarteleActivité est l'activité sur laquelle tombe l'utilisateur lorsqu'il a martelé
 * un arbre à note écologique haute (>6). Marteler un arbre important pour la biodiversité est une erreur,
 * c'est pourquoi on l'indique à l'utilisateur.
 */
public class MessageErreurArbreMarteleActivity extends AppCompatActivity {
    AnimationDrawable insecteAnimation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_erreur_arbre_martele);

        ImageView insecteImage = (ImageView) findViewById(R.id.insecte);
        insecteImage.setBackgroundResource(R.drawable.insecte_animation_drawable);
        insecteAnimation = (AnimationDrawable) insecteImage.getBackground();

        Button mButton = (Button) findViewById(R.id.badMoveButton);

        /*
         *#####################################
         *######  Bouton "badMoveButton" ######
         *#####################################
         *
         * Bouton permettant de retourner dans le fragment "SelectionArbreFragment"
         */
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MessageErreurArbreMarteleActivity.this.finish();
            }
        });


    }

    @Override
    protected void onStart() {

        super.onStart();
        insecteAnimation.start();
    }
}
