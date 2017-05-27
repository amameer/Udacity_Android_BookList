package com.misk.amna.udacity_android_booklist;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class BookAdapter extends ArrayAdapter<Book> {


    public BookAdapter(Context context, ArrayList<Book> books) {
        super(context, 0, books);
    }


    @Override
    public View getView(int position, @Nullable View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.book_list_item, parent, false);
        }

        Book currentBook = getItem(position);

        TextView bookTitel = (TextView) listItemView.findViewById(R.id.bookTitle);
        bookTitel.setText(currentBook.getmBookTitle());

        TextView authors = (TextView) listItemView.findViewById(R.id.authors);
        authors.setText(currentBook.getmBookAuthers());

        return listItemView;
    }


}
