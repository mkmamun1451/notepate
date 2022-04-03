package com.example.notepateapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.internal.SafeIterableMap;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NoteActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener {

    private FloatingActionButton fbttun;
    private FirebaseAuth auth;
    private RecyclerView recycler;
    private List<modelclass> list;
    private StaggeredGridLayoutManager manager;
    private FirebaseUser user;
    private FirebaseFirestore firestore;
    private FirestoreRecyclerAdapter<modelclass, NoteViewHolder>adapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        fbttun = findViewById(R.id.flbttn);

        auth = FirebaseAuth.getInstance();

        user = FirebaseAuth.getInstance().getCurrentUser();
        firestore = FirebaseFirestore.getInstance();


        fbttun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(NoteActivity.this);
                builder.setTitle("Create a New Notes");
                builder.setMessage("Are you sure you Create A New Notes?");
                builder.setIcon(R.drawable.ic_add_24);
                builder.setCancelable(false);
                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(NoteActivity.this,CreateNote.class);
                                intent.putExtra("placeId",1);
                                startActivity(intent);

                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                /*startActivity(new Intent(NoteActivity.this, CreateNote.class));*/
            }
        });


        Query query = firestore.collection("notes").document(user.getUid()).collection("myNotes").orderBy("title", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<modelclass> options = new FirestoreRecyclerOptions.Builder<modelclass>().setQuery(query, modelclass.class).build();
        adapter = new FirestoreRecyclerAdapter<modelclass, NoteViewHolder>(options) {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            protected void onBindViewHolder(@NonNull NoteViewHolder noteViewHolder, int position, @NonNull modelclass model) {


                ImageView popubutton = noteViewHolder.itemView.findViewById(R.id.menupopbttn);


                int colourcode = getRandomColor();
                noteViewHolder.mnote.setBackgroundColor(noteViewHolder.itemView.getResources().getColor(colourcode, null));

                noteViewHolder.notetitle.setText(model.getTitle());
                noteViewHolder.notecontent.setText(model.getContent());


                String docId = adapter.getSnapshots().getSnapshot(0).getId();

                noteViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(view.getContext(), NoteDetilsActivity.class);
                        intent.putExtra("title", model.getTitle());
                        intent.putExtra("content", model.getContent());
                        intent.putExtra("noetId", docId);
                        view.getContext().startActivity(intent);
                        Toast.makeText(NoteActivity.this, "This is Clicked", Toast.LENGTH_SHORT).show();
                    }
                });

                popubutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
                        popupMenu.getMenu().add("Edit").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {

                                /*Intent intent = new Intent(NoteActivity.this, EditNoteActivity.class);
                                intent.putExtra("title", model.getTitle());
                                intent.putExtra("content", model.getContent());
                                intent.putExtra("noetId", docId);
                                view.getContext().startActivity(intent);*/

                                AlertDialog.Builder builder = new AlertDialog.Builder(NoteActivity.this);
                                builder.setTitle("Edit Notes");
                                builder.setMessage("Are you sure you want to Edit Notes?");
                                builder.setIcon(R.drawable.ic_adds_4);
                                builder .setCancelable(false);
                                builder  .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                Intent intent = new Intent(NoteActivity.this,EditNoteActivity.class);
                                                /*intent.putExtra("placeId",1);*/
                                                intent.putExtra("title", model.getTitle());
                                                intent.putExtra("content", model.getContent());
                                                intent.putExtra("noetId", docId);
                                                startActivity(intent);

                                            }
                                        })
                                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {

                                                dialog.cancel();
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();
                                return false;
                            }
                        });

                        popupMenu.getMenu().add("Delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                DocumentReference Dreference = firestore.collection("notes").document(user.getUid()).collection("myNotes").document(docId);
                                Dreference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(NoteActivity.this);
                                        builder.setTitle("DELETE!");
                                        builder.setMessage("Are you sure you want to Delete these Notes?");
                                        builder.setIcon(R.drawable.ic_delete_24);
                                        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        NoteActivity.setDialogResult(true);
                                                        dialog.dismiss();
                                                    }
                                                })
                                                .setNegativeButton(R.string.cancel,new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        NoteActivity.setDialogResult(false);
                                                        dialog.dismiss();
                                                    }
                                                });
                                        AlertDialog alert = builder.create();
                                        alert.show();

                                        Toast.makeText(NoteActivity.this, "This Note is Delete", Toast.LENGTH_SHORT).show();
                                    }
                                })
                            .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        Toast.makeText(NoteActivity.this, "Failed to Delete", Toast.LENGTH_SHORT).show();
                                    }
                                });


                                return false;
                            }
                        });

                        popupMenu.show();
                    }
                });

            }

            @NonNull
            @Override
            public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_layout,parent,false);
                return new NoteViewHolder(view);
            }
        };


        recycler = findViewById(R.id.recycler);
        /*recycler.setHasFixedSize(true);*/
        /*manager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);*/
        recycler.setLayoutManager(new GridLayoutManager(this,2));
        recycler.setAdapter(adapter);

    }

    private static void setDialogResult(boolean b) {

    }


    private int getRandomColor() {
        List<Integer> colorcode = new ArrayList<>();
         colorcode.add(R.color.gray);
         colorcode.add(R.color.pink);
         colorcode.add(R.color.lightgreen);
         colorcode.add(R.color.skyblue);
         colorcode.add(R.color.color1);
         colorcode.add(R.color.color2);
         colorcode.add(R.color.color3);
         colorcode.add(R.color.color4);
         colorcode.add(R.color.color5);
         colorcode.add(R.color.green);
         colorcode.add(R.color.red);
         colorcode.add(R.color.slateblue);
         colorcode.add(R.color.crimson);
         colorcode.add(R.color.indianred);
         colorcode.add(R.color.coral);
         colorcode.add(R.color.orange);
         colorcode.add(R.color.tomato);
         colorcode.add(R.color.darksalmon);
         colorcode.add(R.color.salmon);


        Random random = new Random();
        int number = random.nextInt(colorcode.size());
        return colorcode.get(number);

    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

    }

    public class NoteViewHolder extends RecyclerView.ViewHolder{

        private TextView notetitle;
        private TextView notecontent;
        private LinearLayout mnote;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);

            notetitle = itemView.findViewById(R.id.notetitle);
            notecontent = itemView.findViewById(R.id.noteCont);
            mnote = itemView.findViewById(R.id.note);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){

        switch (item.getItemId()){
            case R.id.logout:
                AlertDialog.Builder builder = new AlertDialog.Builder(NoteActivity.this);

                builder.setTitle("Logout!");
                builder.setMessage("Are You Sure to Log Out of This Application!");
                builder.setIcon(R.drawable.ic_login_24);
                builder.setCancelable(false);
                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(NoteActivity.this,MainActivity.class);
                                intent.putExtra("placeId",1);
                                startActivity(intent);

                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                auth.signOut();
                /*finish();*/
                /*startActivity(new Intent(NoteActivity.this,MainActivity.class));*/
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
        FirebaseAuth.getInstance().addAuthStateListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        FirebaseAuth.getInstance().removeAuthStateListener(this);
        if (adapter !=null){
            adapter.stopListening();
        }
    }









    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    
}