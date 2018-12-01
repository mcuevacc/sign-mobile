package pe.edu.uni.www.vitalsign.Adapter;

import android.app.Activity;
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

    private List<Contact> contacts;
    private int layout;
    private Activity activity;
    private OnItemClickListener listener;

    public ContactAdapter(List<Contact> contacts, int layout, Activity activity, OnItemClickListener listener) {
        this.contacts = contacts;
        this.layout = layout;
        this.activity = activity;
        this.listener = listener;
    }

    public void updateList(List<Contact> contacts) {
        this.contacts = contacts;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(activity).inflate(layout, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(contacts.get(position));
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        public TextView textViewName;
        public TextView textViewNumber;

        public ViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            textViewNumber = (TextView) itemView.findViewById(R.id.textViewNumber);
            itemView.setOnCreateContextMenuListener(this);
        }

        public void bind(final Contact contact) {
            this.textViewName.setText(contact.getName());
            this.textViewNumber.setText(contact.getNumber());

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            listener.onItemClick(contacts.get(position), position);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            Contact contactSelected = contacts.get(this.getAdapterPosition());
            contextMenu.setHeaderTitle(contactSelected.getName());
            MenuInflater inflater = activity.getMenuInflater();
            inflater.inflate(R.menu.context_menu_default, contextMenu);
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

    public interface OnItemClickListener {
        void onItemClick(Contact contact, int position);
    }
}