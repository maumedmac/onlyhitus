package bo.umsa.deseo.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import bo.umsa.deseo.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class
StationAdapter extends BaseAdapter {
    private final Context mContext;
    private final List<StationInstancer> mList;

    public StationAdapter(Context context, List<StationInstancer> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_radios, parent, false);
            holder.textView = convertView.findViewById(R.id.textView);
            holder.imageView = convertView.findViewById(R.id.imageView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //set text and image
        holder.textView.setText(mList.get(position).getName());
        Picasso.get().load(mList.get(position).getImage()).into(holder.imageView);

        return convertView;
    }

    static class ViewHolder {
        TextView textView;
        ImageView imageView;

    }

}