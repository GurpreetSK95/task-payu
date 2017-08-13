package me.gurpreetsk.task_payu.ui.MainActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.sqlbrite2.BriteContentResolver;
import com.squareup.sqlbrite2.SqlBrite;
import com.squareup.sqlbrite2.SqlBrite.Query;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import me.gurpreetsk.task_payu.InitApplication;
import me.gurpreetsk.task_payu.R;
import me.gurpreetsk.task_payu.data.model.Project;
import me.gurpreetsk.task_payu.data.model.ProjectsTable;
import me.gurpreetsk.task_payu.ui.DetailsActivity.DetailsActivity;
import me.gurpreetsk.task_payu.ui.FavouritesActivity.FavouritesActivity;
import me.gurpreetsk.task_payu.util.Constants;
import me.gurpreetsk.task_payu.util.EndlessRecyclerViewScrollListener;
import me.gurpreetsk.task_payu.util.NetworkConnection;


/**
 * The main activity where all the kickstarter projects are loaded and shown to the user.
 * Follows MVP pattern, Dagger2 for DI, ButterKnife for view binding
 * and RxJava and Retrofit for HTTP API calls
 */
public class MainActivity extends AppCompatActivity implements MainView {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.search_view)
    SearchView searchView;

    @Inject
    MainPresenter presenter;
    @Inject
    Gson gson;
    @Inject
    SqlBrite sqlBrite;
    BriteContentResolver resolver;
    Observable<Query> queryObservable;
    List<Project> projects = new ArrayList<>();

    private static final String TAG = MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((InitApplication) getApplication()).getAppComponent().inject(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        presenter.setView(this);

        if (NetworkConnection.isNetworkConnected(MainActivity.this)) {
            presenter.getProjects();
            showLoading();
        } else
            showErrorMessage("Please connect to Internet and try again");

        LinearLayoutManager manager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(manager);
//        recyclerView.addItemDecoration(new DividerItemDecoration(MainActivity.this, DividerItemDecoration.VERTICAL));
        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(manager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                /**
                 * Query the paginated API here and add to the DB.
                 * The recycler view will be updated as SqlBrite is observing the DB for changes
                 */
//                showProjects();
            }
        });

        resolver = sqlBrite.wrapContentProvider(getContentResolver(), Schedulers.io());
        queryObservable = resolver.createQuery(
                ProjectsTable.CONTENT_URI, null, null, null, null, true);

    }

    @Override
    protected void onStart() {
        super.onStart();
        showProjects();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG, "onQueryTextSubmit: " + query);
                List<Project> projects = new ArrayList<>();
                projects.clear();
                Cursor cursor = getContentResolver().query(ProjectsTable.CONTENT_URI, null,
                        "title LIKE \"%" + query + "%\"", null, null);
                if (cursor != null) {
                    cursor.moveToFirst();
                    Log.d(TAG, "onQueryTextSubmit: count: " + cursor.getCount());
                    for (int i = 0; i < cursor.getCount(); i++) {
                        Project project = new Project(cursor.getInt(0), cursor.getInt(1),
                                cursor.getString(2), cursor.getString(3), cursor.getString(4),
                                cursor.getString(5), cursor.getString(6), cursor.getString(7),
                                cursor.getInt(8), cursor.getString(9), cursor.getString(10),
                                cursor.getString(11), cursor.getString(12), cursor.getString(13));
                        projects.add(project);
                        try {
                            cursor.moveToNext();
                        } catch (Exception e) {
                            Log.e(TAG, "showProjects: ", e);
                        }
                    }
                    cursor.close();
                }
                recyclerView.setAdapter(new ProjectsAdapter(projects));
                recyclerView.getAdapter().notifyDataSetChanged();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnCloseListener(() -> {
            queryObservable = resolver.createQuery(
                    ProjectsTable.CONTENT_URI, null, null, null, null, true);
            showProjects();
            return true;
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        showProjects();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case R.id.action_settings:
                Toast.makeText(this, "Created by Gurpreet Singh", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.favourites:
                startActivity(new Intent(MainActivity.this, FavouritesActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void launchProjectDetailActivity(Project project) {
        Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
        intent.putExtra(Constants.KEY_PROJECT, gson.toJson(project));
        startActivity(intent);
    }

//    private Observable<String> getTextChangeObservable() {
//        Observable<String> observable = Observable.create(emitter -> {
//            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//                @Override
//                public boolean onQueryTextSubmit(String query) {
//                    return false;
//                }
//
//                @Override
//                public boolean onQueryTextChange(String newText) {
////                    emitter.onNext(newText);
//                    return true;
//                }
//            });
//            emitter.setCancellable(() -> searchView.setOnQueryTextListener(null));
//        });
//
//        return observable.filter(s -> s.length() > 2).debounce(300, TimeUnit.MILLISECONDS);
//    }

    /**
     * Show loading dialog
     */
    @Override
    public void showLoading() {
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.VISIBLE);
    }

    /**
     * Hide progress dialog when the results have been loaded
     */
    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    /**
     * Fetch all projects from API
     */
    @Override
    public void showProjects() {
        queryObservable.distinct()
                .subscribe(query -> {
                    Cursor cursor = query.run();
                    if (cursor != null) {
                        cursor.moveToFirst();
                        for (int i = 0; i < cursor.getCount(); i++) {
                            Project project = new Project(cursor.getInt(0), cursor.getInt(1),
                                    cursor.getString(2), cursor.getString(3), cursor.getString(4),
                                    cursor.getString(5), cursor.getString(6), cursor.getString(7),
                                    cursor.getInt(8), cursor.getString(9), cursor.getString(10),
                                    cursor.getString(11), cursor.getString(12), cursor.getString(13));
                            projects.add(project);
                            try {
                                cursor.moveToNext();
                            } catch (Exception e) {
                                Log.e(TAG, "showProjects: ", e);
                            }
                        }
                    }
                });
        recyclerView.setAdapter(new ProjectsAdapter(projects));
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void showErrorMessage(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    /**
     * Bulk insert into to DB
     *
     * @param projects projects to insert
     */
    @Override
    public void insertInDb(List<Project> projects) {
        ContentValues[] contentValues = new ContentValues[projects.size()];
        for (int i = 0; i < projects.size(); i++)
            contentValues[i] = ProjectsTable.getContentValues(projects.get(i), true);
        getContentResolver().bulkInsert(ProjectsTable.CONTENT_URI, contentValues);
    }

    /**
     * Adapter to show all projects in the recycler view
     */
    class ProjectsAdapter extends RecyclerView.Adapter<ProjectsAdapter.MyViewHolder> {

        private List<Project> projects;

        ProjectsAdapter(List<Project> projects) {
            this.projects = projects;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            return new MyViewHolder(inflater.inflate(R.layout.layout_project, parent, false));
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.textviewCountry.setText(String.format("Country:\n%s",
                    projects.get(holder.getAdapterPosition()).getCountry()));
            holder.textviewNumBacked.setText(String.format("Number backed:\n%s",
                    projects.get(holder.getAdapterPosition()).getNumBackers()));
            holder.textviewTitle.setText(projects.get(holder.getAdapterPosition()).getTitle());
            holder.textviewDescription.setText(projects.get(holder.getAdapterPosition()).getBlurb());
            holder.linearLayout.setOnClickListener(v ->
                    launchProjectDetailActivity(projects.get(position)));
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
