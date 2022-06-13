package com.dam.appfirebase;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;


public class MainActivity_2 extends AppCompatActivity {
    /** Attribut TAG en autocomplétion avec logt **/
    private static final String TAG = "MainActivity 2";

    /** Varaibles Globales des clés de notre bases **/
    private static final String KEY_TITRE = "titre";
    private static final String KEY_NOTE = "note";

    /** Attributs globaux **/
    private EditText et_titre, et_note;
    private TextView tv_saveNote, tv_showNote;

    /** Référence de la db de FireBase **/
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    //on fait appelle a notre bd dans laquelle on va ajouter une collection appelée liste de message
    //et un document comprenant nos données

    private DocumentReference noteRef = db.document("listeDeNotes/Ma premiére note");

    @Override
    protected void onStart() {
        super.onStart();
        noteRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    Toast.makeText(MainActivity_2.this, "Erreur au chargements", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, error.toString());
                    return;
                }
                if (value.exists()){
                    String titre = value.getString(KEY_TITRE);
                    String note = value.getString(KEY_NOTE);
                    tv_showNote.setText("Titre de la note "+ titre+ "\n"+ "Note : "+ note);
                }else {
                    tv_showNote.setText("");
                }
            }
        });
    }

    public void initUI(){
        et_titre = (EditText) findViewById(R.id.et_titre);
        et_note = (EditText) findViewById(R.id.et_note);
        tv_saveNote = (TextView) findViewById(R.id.tv_saveNote);
        tv_showNote = (TextView) findViewById(R.id.tv_showNote);
    }

    public void saveNote(View view){
        String titre = et_titre.getText().toString();
        String note = et_note.getText().toString();

        Note contenueNote = new Note(titre, note);

//        Map<String, Object> contenueNote = new HashMap<>();
//        contenueNote.put(KEY_TITRE, titre);
//        contenueNote.put(KEY_NOTE, note);

        noteRef.set(contenueNote)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(MainActivity_2.this, "Note enregistree", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity_2.this, "Erreur lors de lenvoi !", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, e.toString());
                    }
                });

    }

    public void showNote(View view){
        noteRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            //String titre = documentSnapshot.getString(KEY_TITRE);
                            //String note = documentSnapshot.getString(KEY_NOTE);
                            Note contenueNote = documentSnapshot.toObject(Note.class);
                            tv_saveNote.setText("Titre de la note : "+ contenueNote.getTitre() + "\n"+ "Note : "+ contenueNote.getNote() );
                        }else {
                            Toast.makeText(MainActivity_2.this, "Le document n'existe pas ! ", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity_2.this, "Erreur de lecture !", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, e.toString());
                    }
                });
    }

    public void updateNote(View view){
        String textNote = et_note.getText().toString();
        noteRef.update(KEY_NOTE, textNote);
    }
    public void deleteNote(View view){
        noteRef.update(KEY_NOTE, FieldValue.delete())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(MainActivity_2.this, "La note est bien supprimé !", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity_2.this, "Erreur lors de la suppression !", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, e.toString());
                    }
                });
    }
    public void deleteAll(View view){
        noteRef.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(MainActivity_2.this, "Toute la note est bien supprimé !", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity_2.this, "Erreur lors de la suppression !", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, e.toString());
                    }
                });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        initUI();

        /**  // Create a new user with a first and last name
         Map<String, Object> user = new HashMap<>();
         user.put("first", "Ada");
         user.put("last", "Lovelace");
         user.put("born", 1815);

         // Add a new document with a generated ID
         db.collection("users")
         .add(user)
         .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
        @Override
        public void onSuccess(DocumentReference documentReference) {
        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
        }
        })
         .addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
        Log.w(TAG, "Error adding document", e);
        }
        }); **/
    }





}