package com.example.personal.project_android;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<String> mDataset;
    private Context context;
    Notes_DbAdapter nba;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View mView;
        ImageButton ib,ib1;

        public ViewHolder(View v) {
            super(v);
            mView = v;
            ib=(ImageButton)v.findViewById(R.id.delete_image_button);
            ib1=(ImageButton)v.findViewById(R.id.edit_image_button);
        }
    }

    public MyAdapter(Context context,List<String> myDataset) {
        mDataset = new ArrayList<String>(myDataset);
        nba=new Notes_DbAdapter(context);
        //Log.d("tag",mDataset.get(0));
        this.context=context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview, parent,false);
        parent.addView(v);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {


        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final TextView mTextView = (TextView) holder.mView.findViewById(R.id.info_text);
        mTextView.setText(mDataset.get(position));
        final ImageButton ib=(ImageButton)holder.mView.findViewById(R.id.delete_image_button);
        holder.ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "OnLongClick Called at position " + holder.getAdapterPosition(), Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setMessage("delete messag?");
                builder1.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                String s=mDataset.get(holder.getAdapterPosition());
                                mDataset.remove(holder.getAdapterPosition());
                                notifyItemRemoved(holder.getAdapterPosition());
                                nba.delete(s);
                                notifyItemRangeChanged(holder.getAdapterPosition(), mDataset.size());

                            }
                        });
                builder1.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(context, "reminder deletion cancelled", Toast.LENGTH_LONG).show();
                            }
                        });
                AlertDialog dialog1=builder1.create();
                dialog1.show();
            }
        });
        final ImageButton ib1=(ImageButton)holder.mView.findViewById(R.id.edit_image_button);
        holder.ib1.setOnClickListener(new View.OnClickListener() {
        String oldstr,newstr;EditText et;
            @Override
            public void onClick(View v) {

                Toast.makeText(context, "OnLongClick Called at position " + holder.getAdapterPosition(), Toast.LENGTH_SHORT).show();
                oldstr= mTextView.getText().toString();
                final LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                final View myView = inflater.inflate(R.layout.edit_text, null);
                builder.setTitle("Edit Text");
                et = (EditText) myView.findViewById(R.id.edit_text_note);
                et.setText(oldstr);
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        et = (EditText) myView.findViewById(R.id.edit_text_note);
                        newstr = et.getText().toString();
                        Toast.makeText(context, "saving edited notes", Toast.LENGTH_LONG).show();
                        mDataset.set(holder.getAdapterPosition(),newstr);
                        notifyDataSetChanged();
                        nba.updateName(oldstr,newstr);
                    }
                });
                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(context, "Notes updation cancelled", Toast.LENGTH_LONG).show();
                            }
                        });
                builder.setView(myView);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

    }
        @Override
    public int getItemCount() {
        return mDataset.size();
    }

}

