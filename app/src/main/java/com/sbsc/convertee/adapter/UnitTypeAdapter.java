package com.sbsc.convertee.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.sbsc.convertee.R;
import com.sbsc.convertee.entities.adapteritems.LocalizedUnitType;
import com.sbsc.convertee.tools.customlayouts.UnitTypeCustomView;
import com.sbsc.convertee.ui.overview.OverviewFragment;

import java.util.Collections;
import java.util.List;

public class UnitTypeAdapter extends RecyclerView.Adapter<UnitTypeAdapter.ViewHolder>{

    private List<LocalizedUnitType> unitTypes;

    // References
    private final OverviewFragment unitOverviewFragment;
    private final Context context;
    private boolean isCategoryTab = false;

    public UnitTypeAdapter(List<LocalizedUnitType> unitTypes , OverviewFragment unitOverviewFragment, boolean isCategoryTab , Context ctx ){
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
        // Get view
        UnitTypeCustomView itemView = (UnitTypeCustomView) holder.itemView;

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

        // Long click for popup menu
        itemView.setOnLongClickListener(v -> {

            // Set style
            itemView.setForcePressed( true );

            //creating a popup menu
            PopupMenu popup = new PopupMenu( context, holder.getTextUnitTypeName() );
            //inflating menu from xml resource
            popup.inflate(R.menu.unit_type_context_menu);

            // Change MenuItem text to unfavourite if it already is a favourite
            if( item.isFavourite() ){
                popup.getMenu().getItem(0).setTitle( context.getString(R.string.adapter_popup_unfavourite) );
                popup.getMenu().getItem(0).setIcon( ContextCompat.getDrawable(context , R.drawable.ic_unfavourite) );
            }else{
                popup.getMenu().getItem(0).setIcon( ContextCompat.getDrawable(context , R.drawable.ic_favourite) );
            }

            //adding click listener
            popup.setOnMenuItemClickListener( selectedItem -> {

            if (selectedItem.getItemId() == R.id.mni_unit_type_favourite) {

                    // If it already is favourite
                    if( item.isFavourite() ){
                        item.setFavourite( false );

                        // Favourites are not applied in category view
                        if( !isCategoryTab ){
                            unitTypes.add(
                                    unitTypes.size()-1,
                                    unitTypes.remove( unitOverviewFragment.getRvUnitTypeSectionedAdapter().sectionedPositionToPosition( holder.getAdapterPosition() ) )
                            );
                            orderAll();
                        }
                        // Move section - 1
                        unitOverviewFragment.unfavouriteUnitType( item.getUnitTypeKey() );

                    // If its not favourite
                    }else{
                        item.setFavourite( true );

                        // Favourites are not applied in category view
                        if( !isCategoryTab ) {
                            unitTypes.add(
                                    unitOverviewFragment.getRvUnitTypeSectionedAdapter().getSectionAt(1).sectionedPosition - 1,
                                    unitTypes.remove(unitOverviewFragment.getRvUnitTypeSectionedAdapter().sectionedPositionToPosition(holder.getAdapterPosition()))
                            );
                            orderFavourites();
                        }
                        // Move section + 1
                        unitOverviewFragment.favouriteUnitType( item.getUnitTypeKey() );
                    }
                    notifyDataSetChanged();
                }
                return true;
            });


            MenuPopupHelper menuHelper = new MenuPopupHelper(context, (MenuBuilder) popup.getMenu(), holder.getTextUnitTypeName());
            menuHelper.setForceShowIcon(true);

            // Remove style when popup closes
            menuHelper.setOnDismissListener(() -> itemView.setForcePressed(false));

            menuHelper.show();

            // popup.show();

            return true;
        });
    }

    /**
     * Order sublist of unitTypes from 0 to index of last favourite item
     */
    public void orderFavourites(){
        int favouritesEnd = unitOverviewFragment.getRvUnitTypeSectionedAdapter().getSectionAt(1).firstPosition;
        Collections.sort( unitTypes.subList( 0 , favouritesEnd+1 ) );
    }

    /**
     * Order sublist of unitTypes from index of last favourite item to list size
     */
    public void orderAll(){
        int favouritesEnd = unitOverviewFragment.getRvUnitTypeSectionedAdapter().getSectionAt(1).firstPosition;
        if ( favouritesEnd-1 < 0 ) return;
        Collections.sort( unitTypes.subList( favouritesEnd-1 , unitTypes.size() ) );
    }

    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        holder.itemView.setOnLongClickListener(null);
        super.onViewRecycled(holder);
    }

    public void removeItemAt( int position ){
        unitTypes.remove( unitOverviewFragment.getRvUnitTypeSectionedAdapter().sectionedPositionToPosition(position) );
        notifyItemRemoved( position );
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
        public String getUnitTypeKey() { return unitTypeKey; }
        public TextView getTextUnitTypeName() { return textUnitTypeName; }
        public ImageView getImageUnitIcon() { return imageUnitIcon; }
        public ImageView getStatusIcon() { return statusIcon; }
    }
}
