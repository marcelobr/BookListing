package com.example.android.booklisting;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    /** Tag for the log messages */
    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    private BookAdapter mAdapter;

    /** TextView that is displayed when the list is empty */
    private TextView mEmptyStateTextView;

    private View mLoadingIndicator;

    private NetworkInfo mNetworkInfo;

    private ConnectivityManager mConnMgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoadingIndicator = findViewById(R.id.loading_indicator);
        mLoadingIndicator.setVisibility(View.GONE);

        android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) findViewById(R.id.search);
        searchView.setIconifiedByDefault(false);

        searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mLoadingIndicator = findViewById(R.id.loading_indicator);
                mLoadingIndicator.setVisibility(View.VISIBLE);

                BookAsyncTask task = new BookAsyncTask();
                task.execute(query.trim());
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mEmptyStateTextView.setText("");

                if (newText.isEmpty()) {
                    mAdapter.clear();
                }
                return false;
            }
        });

        ListView bookListView = (ListView) findViewById(R.id.list_view);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        bookListView.setEmptyView(mEmptyStateTextView);

        mAdapter = new BookAdapter(this, new ArrayList<Book>());

        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Find the current book that was clicked on
                Book currentBook = mAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri googleBooksUri = Uri.parse(currentBook.getInfoLink());

                // Create a new intent to view the book URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, googleBooksUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

        bookListView.setAdapter(mAdapter);

        // Get a reference to the ConnectivityManager to check state of network connectivity
        mConnMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    private class BookAsyncTask extends AsyncTask<String, Void, List<Book>> {

        private static final String GOOGLE_BOOKS_REQUEST_URL = "https://www.googleapis.com/books/v1/volumes?q=";

        @Override
        protected List<Book> doInBackground(String... urls) {
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }

            // Get details on the currently active default data network
            mNetworkInfo = mConnMgr.getActiveNetworkInfo();

            // If there is a network connection, fetch data
            if (mNetworkInfo != null && mNetworkInfo.isConnected()) {
                // Extract relevant fields from the JSON response and create an {@link Book} object
                List<Book> book = QueryUtils.fetchBooksData(GOOGLE_BOOKS_REQUEST_URL + urls[0]);

                // Return the {@link Event} object as the result fo the {@link TsunamiAsyncTask}
                return book;
            } else {
                return null;
            }
        }


        @Override
        protected void onPostExecute(List<Book> books) {
            mLoadingIndicator = findViewById(R.id.loading_indicator);
            mLoadingIndicator.setVisibility(View.GONE);

            mAdapter.clear();

            if (books == null) {
                mEmptyStateTextView.setText(R.string.no_internet_connection);
            } else if (books.isEmpty()) {
                mEmptyStateTextView.setText(R.string.no_books);
            }
            else {
                mAdapter.addAll(books);
            }
        }
    }
}
