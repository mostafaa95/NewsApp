package com.example.android.newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class NewsAdapter extends ArrayAdapter<News> {
    public NewsAdapter(@NonNull Context context, List<News> news) {
        super(context, 0, news);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        News currentNews = getItem(position);

        TextView titleView = (TextView) listItemView.findViewById(R.id.TitleTextView);
        titleView.setText(currentNews.getTitle());

        TextView informationView = (TextView) listItemView.findViewById(R.id.InformationTextView);
        informationView.setText(currentNews.getInformation());

        TextView authorNameView = (TextView) listItemView.findViewById(R.id.AuthorNameTextView);
        authorNameView.setText(currentNews.getAuthorName());

        TextView dateView = (TextView) listItemView.findViewById(R.id.DateTextView);
        dateView.setText(currentNews.getDate());

        return listItemView;
    }


}
