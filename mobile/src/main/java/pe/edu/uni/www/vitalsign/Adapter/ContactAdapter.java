package pe.edu.uni.www.vitalsign.Adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import pe.edu.uni.www.vitalsign.Model.Contact;
import pe.edu.uni.www.vitalsign.R;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(View v, Contact contact, int position);
    }

    private List<Contact> contacts;
    private int layout_item;
    private Activity activity;
    private OnItemClickListener itemClickListener;

    public ContactAdapter(List<Contact> contacts, int layout, Activity activity) {
        this.contacts = contacts;
        this.layout_item = layout;
        this.activity = activity;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public void updateList(List<Contact> contacts) {
        this.contacts = contacts;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ContactAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(activity).inflate(layout_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        if (viewHolder instanceof ViewHolder) {

            Contact contact = contacts.get(position);

            ViewHolder holder = (ViewHolder) viewHolder;
            holder.textViewName.setText(contact.getName());
            holder.textViewNumber.setText(contact.getNumber());
        }
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        public CardView cardViewContact;
        public TextView textViewName;
        public TextView textViewNumber;

        public ViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            cardViewContact = itemView.findViewById(R.id.cardViewContact);
            textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            textViewNumber = (TextView) itemView.findViewById(R.id.textViewNumber);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            itemClickListener.onItemClick(view, contacts.get(position), position);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            Contact contactSelected = contacts.get(this.getAdapterPosition());
            contextMenu.setHeaderTitle(contactSelected.getName());
            contextMenu.setHeaderIcon(R.drawable.baseline_info_black_24);
            MenuInflater inflater = activity.getMenuInflater();
            inflater.inflate(R.menu.menu_item_default, contextMenu);
            for (int i = 0; i < contextMenu.size(); i++)
                contextMenu.getItem(i).setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.delete:
                    contacts.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                    return true;
                case R.id.edit:
                    //editarrrrrr
                    notifyItemChanged(getAdapterPosition());
                    return true;
                default:
                    return false;
            }
        }
    }
}