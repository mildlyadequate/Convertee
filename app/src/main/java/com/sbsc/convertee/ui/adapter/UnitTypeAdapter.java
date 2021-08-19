package com.sbsc.convertee.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.sbsc.convertee.R;
import com.sbsc.convertee.entities.adapteritems.LocalizedUnitType;
import com.sbsc.convertee.tools.CompatibilityHandler;
import com.sbsc.convertee.ui.typeslist.UnitTypesFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UnitTypeAdapter extends RecyclerView.Adapter<UnitTypeAdapter.ViewHolder> implements Filterable {

    private List<LocalizedUnitType> unitTypesInitial;
    private List<LocalizedUnitType> unitTypes;

    // References
    private final UnitTypesFragment unitOverviewFragment;
    private final Context context;

    public UnitTypeAdapter(List<LocalizedUnitType> unitTypes , UnitTypesFragment unitOverviewFragment, Context ctx ){
        Collections.sort( unitTypes );
        this.unitTypes = unitTypes;
        this.unitTypesInitial = new ArrayList<>(unitTypes);
        this.unitOverviewFragment = unitOverviewFragment;
        this.context = ctx;
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

        Drawable statusIcon = null;
        if( item.isFavourite() ){
            statusIcon = ContextCompat.getDrawable(context,R.drawable.ic_favourite);
        }
        holder.getStatusIcon().setImageDrawable( statusIcon );

    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults results = new FilterResults();

                if (constraint == null) {
                    return results;
                }

                List<LocalizedUnitType> filtered = new ArrayList<>();
                String query = constraint.toString().trim().toLowerCase();

                if (query.isEmpty()) {
                    return results;
                } else {
                    for ( LocalizedUnitType unitType : unitTypesInitial ) {

                        if ( unitType.getUnitTypeName().toLowerCase().contains(query) || CompatibilityHandler.containsIgnoreCase( unitType.getTags() , query ) ){
                            filtered.add(unitType);
                        }

                    }
                }

                results.count = filtered.size();
                results.values = filtered;

                return results;
            }

            @SuppressLint("NotifyDataSetChanged")
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.values != null) {
                    unitTypes.clear();
                    unitTypes.addAll((List<LocalizedUnitType>) results.values);
                } else {
                    unitTypes.clear();
                    unitTypes.addAll(unitTypesInitial);
                }
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        holder.itemView.setOnLongClickListener(null);
        super.onViewRecycled(holder);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setUnitTypes(List<LocalizedUnitType> unitTypes) {
        Collections.sort( unitTypes );
        this.unitTypes = unitTypes;
        this.unitTypesInitial = new ArrayList<>(unitTypes);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() { return unitTypes.size(); }

    public List<LocalizedUnitType> getUnitTypes() { return unitTypes; }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private final TextView textUnitTypeName;
        private final ImageView imageUnitIcon;
        private String unitTypeKey;
        private final ImageView statusIcon;

        public ViewHolder(View view) {
            super(view);
            textUnitTypeName = view.findViewById(R.id.tvUnitTypeName);
            imageUnitIcon = view.findViewById(R.id.ivUnitIcon);
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
