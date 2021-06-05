package com.sbsc.convertee.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.sbsc.convertee.R;
import com.sbsc.convertee.entities.adapteritems.LocalizedUnitType;
import com.sbsc.convertee.ui.categories.UnitCategoriesFragment;

import java.util.Collections;
import java.util.List;

public class UnitTypeAdapter extends RecyclerView.Adapter<UnitTypeAdapter.ViewHolder>{

    private List<LocalizedUnitType> unitTypes;

    // References
    private final UnitCategoriesFragment unitOverviewFragment;
    private final Context context;
    private final boolean isCategoryTab;

    public UnitTypeAdapter(List<LocalizedUnitType> unitTypes , UnitCategoriesFragment unitOverviewFragment, boolean isCategoryTab , Context ctx ){
        this.unitTypes = unitTypes;
        this.unitOverviewFragment = unitOverviewFragment;
        this.context = ctx;
        this.isCategoryTab=isCategoryTab;
        if( !isCategoryTab ) Collections.sort( unitTypes );
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_unit_type_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        // Get current unit item
        LocalizedUnitType item = unitTypes.get(position);

        // Set item visuals
        holder.getTextUnitTypeName().setText(item.getUnitTypeName());
        holder.getImageUnitIcon().setBackgroundResource(item.getIconId());
        holder.setUnitTypeKey( item.getUnitTypeKey() );

        // Only show status if this list is inside the category tab
        if( isCategoryTab ){
            Drawable statusIcon = null;
            if( item.isFavourite() ){
                statusIcon = ContextCompat.getDrawable(context,R.drawable.ic_favourite);
            }
            holder.getStatusIcon().setImageDrawable( statusIcon );
        }

    }

    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        holder.itemView.setOnLongClickListener(null);
        super.onViewRecycled(holder);
    }

    public void setUnitTypes(List<LocalizedUnitType> unitTypes) {
        this.unitTypes = unitTypes;
        if( !isCategoryTab ) Collections.sort( this.unitTypes );
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() { return unitTypes.size(); }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private final TextView textUnitTypeName;
        private final ImageView imageUnitIcon;
        private String unitTypeKey;
        private final ImageView statusIcon;

        public ViewHolder(View view) {
            super(view);
            textUnitTypeName = view.findViewById(R.id.textUnitTypeName);
            imageUnitIcon = view.findViewById(R.id.imgUnitIcon);
            statusIcon = view.findViewById(R.id.ivStatus);

            view.setOnClickListener( this );
        }

        @Override
        public void onClick(View v) {
            unitOverviewFragment.handleUnitTypeClick( unitTypeKey );
        }

        // Setter
        public void setUnitTypeKey(String unitTypeKey) { this.unitTypeKey = unitTypeKey; }

        // Getter
        public TextView getTextUnitTypeName() { return textUnitTypeName; }
        public ImageView getImageUnitIcon() { return imageUnitIcon; }
        public ImageView getStatusIcon() { return statusIcon; }
    }
}
