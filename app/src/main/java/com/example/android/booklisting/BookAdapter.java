package com.example.android.booklisting;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by marcelo on 20/11/17.
 */

public class BookAdapter extends ArrayAdapter<Book> {

    public BookAdapter(@NonNull Context context, ArrayList<Book> books) {
        super(context, 0, books);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        Book currentBook = getItem(position);

        ImageView imageView = (ImageView) listItemView.findViewById(R.id.list_item_image);
        Glide.with(getContext())
                .load(currentBook.getSmallThumbnail())
                .into(imageView);

        TextView titleTextView = (TextView) listItemView.findViewById(R.id.title_text_view);
        titleTextView.setText(currentBook.getTitle());

        TextView authorTextView = (TextView) listItemView.findViewById(R.id.authors_text_view);
        if (currentBook.getAuthors().length > 1) {
            authorTextView.setText(currentBook.getAuthors()[0] + parent.getResources().getString(R.string.et_al));
        }
        else {
            authorTextView.setText(currentBook.getAuthors()[0]);
        }

        return listItemView;
    }
}
