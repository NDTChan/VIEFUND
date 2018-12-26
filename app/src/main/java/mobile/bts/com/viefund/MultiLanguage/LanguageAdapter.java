package mobile.bts.com.viefund.MultiLanguage;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;

import java.util.ArrayList;
import java.util.List;

import mobile.bts.com.viefund.Model.Language;
import mobile.bts.com.viefund.R;
import mobile.bts.com.viefund.databinding.ItemLanguageBinding;

/**
 * BT Company
 * Created by Administrator on 3/15/2018.
 */

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.LanguageHolder> {
    public static final String TAG = LanguageAdapter.class.getSimpleName();
    private List<Language> mLanguageList = new ArrayList<>();
    private ItemClickListener<Language> mListener;
    private Language mCurrentLanguage = LanguageUtils.getCurrentLanguage();

    public LanguageAdapter(List<Language> languageList) {
        mLanguageList = languageList;
        Log.d(TAG, "LanguageAdapter: "+mLanguageList.size());
    }

    public void setListener(ItemClickListener<Language> listener) {
        mListener = listener;
    }

    public void setCurrentLanguage(Language language) {
        mCurrentLanguage = language;
        notifyDataSetChanged();
    }

    @Override
    public LanguageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemLanguageBinding binding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.item_language, parent, false);
        return new LanguageHolder(binding, mListener);
    }

    @Override
    public void onBindViewHolder(LanguageHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: "+mLanguageList.get(position).getName());
        holder.mBinding.radioItemLanguage.setChecked(mCurrentLanguage.getId() == position);
        holder.bindLanguage(mLanguageList.get(position));

//        holder.mAvatar.setText(mLanguageList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mLanguageList.size();
    }

    public class LanguageHolder extends RecyclerView.ViewHolder {
        public ObservableField<String> name = new ObservableField<>();
        private ItemLanguageBinding mBinding;
        private Language mLanguage;

        LanguageHolder(ItemLanguageBinding binding, final ItemClickListener<Language> listener) {
            super(binding.getRoot());
            mBinding = binding;
            mBinding.setHolder(this);
            mBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onClickItem(getAdapterPosition(), mLanguage);
                    }
                }
            });
        }

        void bindLanguage(Language language) {
            mLanguage = language;
            name.set(language.getName());
        }
    }

}
