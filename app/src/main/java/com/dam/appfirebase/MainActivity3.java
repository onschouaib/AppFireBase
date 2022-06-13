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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity3 extends AppCompatActivity {
    private static final String TAG = "Main Activity 3";

    private EditText et_titre, et_note;
    private TextView tv_showNote;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String KEY_TITRE = "titre";
    private static final String KEY_NOTE = "note";

    /** Ajout de la reference a la collection comprenant toutes les  notes : NoteBook **/

    private CollectionReference notbookRef = db.collection("Notebook");

    public void initUI(){
        et_titre = (EditText) findViewById(R.id.et_titre);
        et_note = (EditText) findViewById(R.id.et_note);
        tv_showNote = (TextView) findViewById(R.id.tv_showNote);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        initUI();
    }

    public void addNote(View view){
        String titre = et_titre.getText().toString();
        String note = et_note.getText().toString();

        Note contenueNote = new Note(titre, note);

        notbookRef.add(contenueNote)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(MainActivity3.this, "Enregistrement de "+titre, Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity3.this, "Erreur lors de l'ajout ! ", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, e.toString());
                    }
                });
        et_titre.setText("");
        et_note.setText("");
    }

    public void onDeleteNote(View view){
        //Delete all
        Log.i(TAG, "info : "+notbookRef.getId().toString());
        //notbookRef.getFirestore();

        notbookRef.document("YMkzEiyd5L3hv4eiqVZJ")
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(MainActivity3.this, "Toute la note est bien supprimée ! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity3.this, "Erreur lors de la suppression ! ", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    public void updateNote(View view){
        Log.i(TAG, "info : "+notbookRef.getId().toString());
        notbookRef.document().update(KEY_NOTE, et_note.getText().toString());
    }

    @Override
    protected void onStart() {
        super.onStart();
        notbookRef.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error!=null){
                    return;
                }
                String notes = "";
                for (QueryDocumentSnapshot documentSnapshot : value){

                    Note contenuNote = documentSnapshot.toObject(Note.class);
                    contenuNote.setDocumentId(documentSnapshot.getId());

                    String documentId = contenuNote.getDocumentId();
                    String titre = contenuNote.getTitre();
                    String note = contenuNote.getNote();

                    notes += documentId + "\n Titre : "+titre+" \n Note : "+note+ "\n\n";
                }
                tv_showNote.setText(notes);
            }
        });
    }

    public void loadNotes(View view){
        notbookRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        String notes ="";
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            Note contenueNote = documentSnapshot.toObject(Note.class);
                            contenueNote.setDocumentId(documentSnapshot.getId());

                            String documentId = contenueNote.getDocumentId();
                            String titre = contenueNote.getTitre();
                            String note = contenueNote.getNote();

                            notes += documentId + "\n Titre : "+ titre + "\n Note : "+note+ "\n\n";
                        }
                        tv_showNote.setText(notes);
                    }
                });
    }
}