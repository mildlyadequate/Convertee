package com.sbsc.convertee.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sbsc.convertee.R;
import com.sbsc.convertee.entities.adapteritems.LocalizedUnitType;
import com.sbsc.convertee.tools.CompatibilityHandler;
import com.sbsc.convertee.tools.customlayouts.UnitTypeCustomView;
import com.sbsc.convertee.ui.appsetup.AppSetupUnitFragment;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class SetupUnitTypeAdapter extends RecyclerView.Adapter<SetupUnitTypeAdapter.ViewHolder>{

        private final List<LocalizedUnitType> unitTypes;
        private final HashSet<Integer> selectedRows;

        // References
        private final Context context;
        private final AppSetupUnitFragment fragment;

        public SetupUnitTypeAdapter(List<LocalizedUnitType> unitTypes, Context ctx, AppSetupUnitFragment appSetupUnitFragment){
            this.unitTypes = unitTypes;
            this.context = ctx;
            this.fragment = appSetupUnitFragment;

            selectedRows = new HashSet<>();
            Collections.sort( unitTypes );
        }

        public void setSelectedRows( String[] selectedUnits ){

            if( selectedUnits == null ) return;
            selectedRows.clear();

            for (String selectedUnit : selectedUnits) {

                for (int i = 0; i < unitTypes.size(); i++) {
                    if (selectedUnit.equals(unitTypes.get(i).getUnitTypeKey())) {
                        selectedRows.add(i);
                        break;
                    }
                }

            }
        }

        @NonNull
        @Override
        public SetupUnitTypeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from( viewGroup.getContext() ).inflate(R.layout.adapter_unit_type_item, viewGroup, false);
            return new ViewHolder(view);
        }

        @SuppressLint("RestrictedApi")
        @Override
        public void onBindViewHolder(@NonNull SetupUnitTypeAdapter.ViewHolder holder, int position) {

            // Get current unit item
            LocalizedUnitType item = unitTypes.get(position);

            // Set item visuals
            holder.getTextUnitTypeName().setText(item.getUnitTypeName());
            holder.getImageUnitIcon().setBackgroundResource(item.getIconId());
            holder.getImageUnitIcon().setBackgroundTintList( ColorStateList.valueOf( CompatibilityHandler.getColor(context, R.color.themeSecondaryDark) ) );

            // Handle selection
            holder.rowView.setOnClickListener(v -> {

                if (selectedRows.contains(position)) {
                    selectedRows.remove(position);
                } else {
                    if (selectedRows.size() >= 3) return;
                    selectedRows.add(position);
                }

                // Get the IDs of every selected unitType, then send it to the activity
                String[] selectedUnitTypes = new String[selectedRows.size()];
                Iterator<Integer> it = selectedRows.iterator();
                int index = 0;
                while (it.hasNext()) {
                    selectedUnitTypes[index] = unitTypes.get(it.next()).getUnitTypeKey();
                    index++;
                }
                fragment.setSelectedUnitTypes(selectedUnitTypes);

                notifyItemChanged(position);
            });

            // Change colours based on selection
            if (selectedRows.contains(position)){
                holder.getRowView().setForcePressed(true);
                holder.getRowView().setBackgroundColor( CompatibilityHandler.getColor(context, R.color.themePrimary) );
                holder.getTextUnitTypeName().setTextColor( CompatibilityHandler.getColor(context, R.color.themeDayLightText) );
                holder.getImageUnitIcon().setBackgroundTintList( ColorStateList.valueOf( CompatibilityHandler.getColor(context, R.color.themeDayLightText) ) );
            }else{
                holder.getRowView().setForcePressed(false);
                holder.getRowView().setBackgroundColor( CompatibilityHandler.getColor(context, R.color.themeDayWhiteBackground) );
                holder.getTextUnitTypeName().setTextColor( CompatibilityHandler.getColor(context, R.color.themeDayDarkText) );
                holder.getImageUnitIcon().setBackgroundTintList( ColorStateList.valueOf( CompatibilityHandler.getColor(context, R.color.themeDayDarkText) ) );
            }
        }

        @Override
        public int getItemCount() { return unitTypes.size(); }

        public static class ViewHolder extends RecyclerView.ViewHolder{

            private final UnitTypeCustomView rowView;
            private final TextView textUnitTypeName;
            private final ImageView imageUnitIcon;

            public ViewHolder(View view) {
                super(view);
                textUnitTypeName = view.findViewById(R.id.tvUnitTypeName);
                imageUnitIcon = view.findViewById(R.id.ivUnitIcon);
                rowView = view.findViewById(R.id.unitTypeRow);
            }

            // Getter
            public UnitTypeCustomView getRowView() { return rowView; }
            public TextView getTextUnitTypeName() { return textUnitTypeName; }
            public ImageView getImageUnitIcon() { return imageUnitIcon; }
        }
    }
