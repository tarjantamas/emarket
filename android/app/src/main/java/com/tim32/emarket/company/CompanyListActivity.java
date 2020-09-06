package com.tim32.emarket.company;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import com.tim32.emarket.R;
import com.tim32.emarket.apiclients.dto.Company;
import com.tim32.emarket.service.CompaniesService;
import org.androidannotations.annotations.*;

import java.util.ArrayList;
import java.util.List;

/**
 * An activity representing a list of Companies. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link CompanyDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
@EActivity(R.layout.activity_company_list)
public class CompanyListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @ViewById(R.id.company_detail_container)
    FrameLayout companyDetailContainer;

    @ViewById(R.id.company_list)
    RecyclerView companyList;

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @Bean
    CompaniesService companiesService;
    private SimpleItemRecyclerViewAdapter companyListAdapter;

    @AfterViews
    void afterViews() {
        if (companyDetailContainer != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
        assert companyList != null;

        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());
        setupRecyclerView(companyList);
        loadCompanies();
    }

    @Background
    void loadCompanies() {
        List<Company> companies = companiesService.getCompanies();
        updateCompanyList(companies);
    }

    @UiThread
    void updateCompanyList(List<Company> companies) {
        companyListAdapter.updateItems(companies);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        companyListAdapter = new SimpleItemRecyclerViewAdapter(this, new ArrayList<>(), mTwoPane, recyclerView);
        recyclerView.setAdapter(companyListAdapter);
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final CompanyListActivity mParentActivity;
        private final List<Company> mValues;
        private final boolean mTwoPane;
        private final RecyclerView mRecyclerView;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Company company = (Company) view.getTag();
                if (mTwoPane) {
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.company_detail_container, CompanyDetailFragment_.builder()
                                    .arg(CompanyDetailFragment_.ARG_ITEM_ID, company.getId())
                                    .build())
                            .commit();
                } else {
                    Context context = view.getContext();
                    CompanyDetailActivity_
                            .intent(context)
                            .extra(CompanyDetailActivity.ARG_ITEM_ID, company.getId())
                            .start();
                }
            }
        };

        SimpleItemRecyclerViewAdapter(CompanyListActivity parent,
                                      List<Company> items,
                                      boolean twoPane, RecyclerView recyclerView) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
            mRecyclerView = recyclerView;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.company_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mIdView.setText(Long.toString(mValues.get(position).getId()));
            holder.mContentView.setText(mValues.get(position).getName());

            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public void updateItems(List<Company> companies) {
            mValues.clear();
            mRecyclerView.removeAllViews();
            mValues.addAll(companies);
            notifyDataSetChanged();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mIdView;
            final TextView mContentView;

            ViewHolder(View view) {
                super(view);
                mIdView = view.findViewById(R.id.id_text);
                mContentView = view.findViewById(R.id.content);
            }
        }
    }
}
