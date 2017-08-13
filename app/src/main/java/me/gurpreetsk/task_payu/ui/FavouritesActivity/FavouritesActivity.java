package me.gurpreetsk.task_payu.ui.FavouritesActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.sqlbrite2.BriteContentResolver;
import com.squareup.sqlbrite2.SqlBrite;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import me.gurpreetsk.task_payu.InitApplication;
import me.gurpreetsk.task_payu.R;
import me.gurpreetsk.task_payu.data.model.FavoritesTable;
import me.gurpreetsk.task_payu.data.model.Favourite;
import me.gurpreetsk.task_payu.ui.DetailsActivity.DetailsActivity;
import me.gurpreetsk.task_payu.util.Constants;

public class FavouritesActivity extends AppCompatActivity implements FavouritesView {

    @BindView(R.id.recyclerview_favourites)
    RecyclerView recyclerView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @Inject
    SqlBrite sqlBrite;
    @Inject
    Gson gson;
    @Inject
    FavouritesPresenter presenter;

    private static final String TAG = FavouritesActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((InitApplication) getApplication()).getAppComponent().inject(this);
        setContentView(R.layout.activity_favourites);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);

        presenter.setView(this);
        presenter.getFavourites();
        setTitle("Favourites");

        recyclerView.setLayoutManager(new LinearLayoutManager(FavouritesActivity.this));

    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    @Override
    public void showLoading() {
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showFavourites() {
        Cursor cursor = getContentResolver().query(FavoritesTable.CONTENT_URI, null, null, null, null);
        List<Favourite> favourites = new ArrayList<>();
        if (cursor != null) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                Favourite favourite = new Favourite(cursor.getInt(0), cursor.getInt(1),
                        cursor.getString(2), cursor.getString(3), cursor.getString(4),
                        cursor.getString(5), cursor.getString(6), cursor.getString(7),
                        cursor.getInt(8), cursor.getString(9), cursor.getString(10),
                        cursor.getString(11), cursor.getString(12), cursor.getString(13));
                favourites.add(favourite);
                try {
                    cursor.moveToNext();
                } catch (Exception e) {
                    Log.e(TAG, "showFavs: ", e);
                }
            }
        }
//        });
        Log.d(TAG, "showFavourites: " + favourites.size());
        recyclerView.setAdapter(new FavouritesAdapter(favourites));
        recyclerView.getAdapter().notifyDataSetChanged();
        if (cursor != null)
            cursor.close();

    }

    @Override
    public void showErrorMessage(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    public void launchFavouritesDetailActivity(Favourite project) {
        Intent intent = new Intent(FavouritesActivity.this, DetailsActivity.class);
        intent.putExtra(Constants.KEY_PROJECT, gson.toJson(project));
        startActivity(intent);
    }


    /**
     * Adapter to show all favourites in the recycler view
     */
    class FavouritesAdapter extends RecyclerView.Adapter<FavouritesAdapter.MyViewHolder> {

        private List<Favourite> projects;

        FavouritesAdapter(List<Favourite> projects) {
            this.projects = projects;
        }

        @Override
        public FavouritesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            return new FavouritesAdapter.MyViewHolder(inflater.inflate(R.layout.layout_project, parent, false));
        }

        @Override
        public void onBindViewHolder(FavouritesAdapter.MyViewHolder holder, int position) {
            holder.textviewCountry.setText(String.format("Country:\n%s",
                    projects.get(holder.getAdapterPosition()).getCountry()));
            holder.textviewNumBacked.setText(String.format("Number backed:\n%s",
                    projects.get(holder.getAdapterPosition()).getNumBackers()));
            holder.textviewTitle.setText(projects.get(holder.getAdapterPosition()).getTitle());
            holder.textviewDescription.setText(projects.get(holder.getAdapterPosition()).getBlurb());
            holder.linearLayout.setOnClickListener(v ->
                    launchFavouritesDetailActivity(projects.get(position)));
        }

        @Override
        public int getItemCount() {
            return projects.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.textview_title)
            TextView textviewTitle;
            @BindView(R.id.textview_country)
            TextView textviewCountry;
            @BindView(R.id.textview_num_backed)
            TextView textviewNumBacked;
            @BindView(R.id.textview_description)
            TextView textviewDescription;
            @BindView(R.id.project_info)
            LinearLayout linearLayout;

            MyViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }

    }

}
