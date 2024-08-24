package com.nhn.fitness.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nhn.fitness.R;
import com.nhn.fitness.data.model.GroupSectionModel;
import com.nhn.fitness.data.model.GroupViewModel;
import com.nhn.fitness.data.repositories.SectionRepository;
import com.nhn.fitness.ui.base.BaseFragment;
import com.nhn.fitness.ui.customViews.GroupSections;

import java.util.ArrayList;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class GroupAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private FragmentManager fragmentManager;
    private ArrayList<GroupViewModel> groups;

    public GroupAdapter(FragmentManager fragmentManager, ArrayList<GroupViewModel> list) {
        this.fragmentManager = fragmentManager;
        this.groups = list;
    }

    @Override
    public int getItemViewType(int position) {
        return groups.get(position).getType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row;
        switch (viewType) {
            case GroupViewModel.TYPE_GROUP_SECTION:
                row = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group_section_layout, parent, false);
                return new GroupSectionViewHolder(row);
            case GroupViewModel.TYPE_FRAGMENT:
                row = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group_fragment, parent, false);
                return new GroupFragmentViewHolder(row);
            case GroupViewModel.TYPE_LAYOUT:
                row = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group_view_layout, parent, false);
                return new GroupLayoutView(row);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        GroupViewModel groupViewModel = groups.get(position);
        switch (groupViewModel.getType()) {
            case GroupViewModel.TYPE_GROUP_SECTION:
                ((GroupSectionViewHolder) holder).bind((GroupSectionModel) groupViewModel.getData());
                break;
            case GroupViewModel.TYPE_FRAGMENT:
                ((GroupFragmentViewHolder) holder).bind(groupViewModel);
                break;
            case GroupViewModel.TYPE_LAYOUT:
                ((GroupLayoutView) holder).bind(groupViewModel.getData());
                break;
        }

    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    public class GroupSectionViewHolder extends RecyclerView.ViewHolder {
        GroupSections groupSections;

        GroupSectionViewHolder(@NonNull View itemView) {
            super(itemView);
            groupSections = itemView.findViewById(R.id.group_section);
        }

        void bind(GroupSectionModel data) {
            groupSections.setTitle(data.getTitle());
            switch (data.getType()) {
                case 0: // normal
                    addDisposable(SectionRepository.getInstance()
                            .getAllSectionUserByIdsWithFullData(data.getSections())
                            .subscribe(response -> groupSections.setSections(response)));
                    break;
                case 1: // favorite
                    addDisposable(SectionRepository.getInstance()
                            .getAllSectionUserFavoriteWithFullData()
                            .subscribe(response -> groupSections.setSections(response)));
                    break;
                case 2: // recent
                    break;
            }
        }

    }

    public class GroupFragmentViewHolder extends RecyclerView.ViewHolder {
        private FrameLayout layout;

        GroupFragmentViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.container);
        }

        void bind(GroupViewModel groupViewModel) {
//            Log.e("status", "bind");
            fragmentManager.beginTransaction()
                    .replace(layout.getId(), (BaseFragment) groupViewModel.getData(), null)
                    .commitAllowingStateLoss();
        }
    }

    public class GroupLayoutView extends RecyclerView.ViewHolder {
        private LinearLayout layout;

        public GroupLayoutView(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.container);
        }

        void bind(Object object) {
            View view;
            if (object instanceof View) {
                view = (View) object;
            } else {
                view = LayoutInflater.from(itemView.getContext()).inflate((Integer) object, (ViewGroup) itemView, false);
            }
            layout.addView(view);
        }
    }

    private void addDisposable(Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    public void destroy() {
        compositeDisposable.clear();
    }
}
