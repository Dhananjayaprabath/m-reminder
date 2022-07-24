package com.example.mynotes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.icu.text.CaseMap;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.iwgang.countdownview.CountdownView;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> implements Filterable {

    Context context;
    Activity activity;
    List<Model> notesList;
    List<Model> newList;
    TextView text1;

    public Adapter(Context context, Activity activity, List<Model> notesList) {
        this.context = context;
        this.activity = activity;
        this.notesList = notesList;
        newList = new ArrayList<>(notesList);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.title.setText(notesList.get(position).getTitle());
        holder.time.setText(notesList.get(position).getTime());
        holder.date.setText(notesList.get(position).getDate());
        holder.days.setText(notesList.get(position).getDay());
        holder.days.setBackgroundColor(Color.parseColor(notesList.get(position).getColours()));

        holder.layout.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(v.getRootView().getContext());
                bottomSheetDialog.setContentView(R.layout.buttom_sheet);
                bottomSheetDialog.show();

                TextView text1 = bottomSheetDialog.findViewById(R.id.text_sheet);
                text1.setText(notesList.get(position).getTitle());

                CountdownView mCvCountdownView = bottomSheetDialog.findViewById(R.id.mycountdown1);


                String DATE_FORMAT = "yyyy-MM-dd hh:mm aa ";

                SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

                String evdate =(notesList.get(position).getDate());
                String evtime =(notesList.get(position).getTime());

                String eq = evdate+" "+evtime;
               
                try {
                    Date event_date = dateFormat.parse(eq);

                   // Date eventime = dateFormat.parse(notesList.get(position).getTime());
                    
                    Date now =new Date();
                   //long th = eventime.getTime();

                    long currentdate =now.getTime();

                    long cco = event_date.getTime()-currentdate;
                    mCvCountdownView.start(cco);


                } catch (ParseException e) {
                    e.printStackTrace();
                }


                Date now =new Date();





                Button buttons = bottomSheetDialog.findViewById(R.id.start);
                buttons.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String url =(notesList.get(position).getDescription());

                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        activity.startActivity(i);
                        bottomSheetDialog.hide();
                    }
                });





            }
        });

        holder.ebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateNotesActivity.class);

                intent.putExtra("title", notesList.get(position).getTitle());
                intent.putExtra("description", notesList.get(position).getDescription());
                intent.putExtra("time",notesList.get(position).getTime());
                intent.putExtra("date",notesList.get(position).getDate());
                intent.putExtra("day",notesList.get(position).getDay());
                intent.putExtra("colours",notesList.get(position).getColours());
                intent.putExtra("id", notesList.get(position).getId());
                intent.putExtra("noti",notesList.get(position).getNoti());



                activity.startActivity(intent);



            }
        });
// Share meeting button
        holder.sbutton.setOnClickListener(new View.OnClickListener() {

            String datashare =(notesList.get(position).getDate());
            String timeshare =(notesList.get(position).getTime());
            String urlshare =(notesList.get(position).getDescription());

            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT,notesList.get(position).getTitle()+ "\n"+"Date : "+datashare+"\n"+"Time : "+timeshare+"\n"+"Meeting Link : "+urlshare+"\n"+"Share by M Reminder With ❤");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "The title");
                activity.startActivity(shareIntent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Model> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(newList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Model item : newList) {
                    if (item.getTitle().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }

            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            notesList.clear();
            notesList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, description,time,date,days;
        CardView layout;
        ImageButton ebutton,sbutton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            time=itemView.findViewById(R.id.timev);
            date=itemView.findViewById(R.id.datev);
            layout = itemView.findViewById(R.id.note_layout);
            ebutton=itemView.findViewById(R.id.edit_Btt);
            days=itemView.findViewById(R.id.days1);
            sbutton=itemView.findViewById(R.id.share_Btt);


        }

    }


    public List<Model> getList() {
        return notesList;
    }

    public void removeItem(int position) {
        notesList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Model item, int position) {
        notesList.add(position, item);
        notifyItemInserted(position);
    }
}
