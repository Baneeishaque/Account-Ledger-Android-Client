package ndk.personal.account_ledger.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ndk.personal.account_ledger.R;
import ndk.personal.account_ledger.models.Account;

/**
 * A custom adapter to use with the RecyclerView widget.
 */
public class List_Accounts_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private String mHeaderTitle;
    private Boolean header_presence = true;

    private OnHeaderClickListener mHeaderClickListener;

    private OnHeaderLongClickListener mHeaderLongClickListener;

    private ArrayList<Account> modelList;

    private OnItemClickListener mItemClickListener;


    public List_Accounts_Adapter(ArrayList<Account> modelList, String headerTitle) {
        this.modelList = modelList;
        this.mHeaderTitle = headerTitle;
    }

    public List_Accounts_Adapter(ArrayList<Account> modelList) {
        this.modelList = modelList;
        this.header_presence = false;
    }

    public void updateList(ArrayList<Account> modelList) {
        this.modelList = modelList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_header, parent, false);
            return new HeaderViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_account_list, parent, false);
            return new ViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof HeaderViewHolder) {
            HeaderViewHolder headerHolder = (HeaderViewHolder) holder;

            headerHolder.txt_Title_Header.setText(mHeaderTitle);

        } else if (holder instanceof ViewHolder) {
            final Account model;
            if (header_presence) {
                model = getItem(position - 1);
            } else {
                model = getItem(position);
            }
            ViewHolder genericViewHolder = (ViewHolder) holder;
            genericViewHolder.item_Txt_Account_Name.setText(model.getName());
            genericViewHolder.item_Txt_Account_Balance.setText(model.getCommodityType());

        }
    }

    @Override
    public int getItemViewType(int position) {

        if (header_presence) {
            if (isPositionHeader(position)) {
                return TYPE_HEADER;
            }
            return TYPE_ITEM;
        }
        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }


    @Override
    public int getItemCount() {

        if (header_presence) {
            return modelList.size() + 1;
        } else {
            return modelList.size();
        }
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public void SetOnHeaderClickListener(final OnHeaderClickListener headerClickListener) {
        this.mHeaderClickListener = headerClickListener;
    }

    public void SetOnHeaderLongClickListener(final OnHeaderLongClickListener headerLongClickListener) {
        this.mHeaderLongClickListener = headerLongClickListener;
    }

    private Account getItem(int position) {
        return modelList.get(position);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position, Account model);
    }

    public interface OnHeaderClickListener {
        void onHeaderClick(View view, String headerTitle);
    }

    public interface OnHeaderLongClickListener {
        void onHeaderClick(View view, String headerTitle);
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView txt_Title_Header;

        HeaderViewHolder(final View itemView) {
            super(itemView);
            this.txt_Title_Header = itemView.findViewById(R.id.txt_header);

            itemView.setOnClickListener(view -> mHeaderClickListener.onHeaderClick(itemView, mHeaderTitle));

            itemView.setOnLongClickListener(v -> {
                mHeaderLongClickListener.onHeaderClick(itemView, mHeaderTitle);
                return true;
            });

        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView item_Txt_Account_Name;
        private TextView item_Txt_Account_Balance;

        // @BindView(R.id.img_user)
        // ImageView imgUser;
        // @BindView(R.id.item_txt_title)
        // TextView item_Txt_Account_Name;
        // @BindView(R.id.item_txt_message)
        // TextView item_Txt_Account_Balance;
        // @BindView(R.id.radio_list)
        // RadioButton item_Txt_Account_Balance;
        // @BindView(R.id.check_list)
        // CheckBox itemCheckList;

        ViewHolder(final View itemView) {
            super(itemView);

            // ButterKnife.bind(this, itemView);

            this.item_Txt_Account_Name = itemView.findViewById(R.id.item_txt_account_name);
            this.item_Txt_Account_Balance = itemView.findViewById(R.id.item_txt_account_balance);

            itemView.setOnClickListener(view -> {

                if (header_presence) {
                    mItemClickListener.onItemClick(itemView, getAdapterPosition(), modelList.get(getAdapterPosition() - 1));
                } else {
                    mItemClickListener.onItemClick(itemView, getAdapterPosition(), modelList.get(getAdapterPosition()));
                }
            });

        }
    }
}

