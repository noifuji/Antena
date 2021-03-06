package jp.noifuji.antena.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import jp.noifuji.antena.R;
import jp.noifuji.antena.data.entity.Headline;
import jp.noifuji.antena.view.fragment.HeadLineListFragment;

/**
 * Created by Ryoma on 2015/10/24.
 */
public class EntryAdapter extends ArrayAdapter<Headline> {
    private static final String TAG = "EntryAdapter";
    private Context mContext;
    private int mTextViewResourceId;
    private List<Headline> mItems;
    private LayoutInflater mInflater;
    private OnHeadlineDisplayedListener mListener;//TODO このリスナの張り方が問題ないか検証する

    ImageView thumbnailView;


    public EntryAdapter(Context context, int resource, List<Headline> objects, HeadLineListFragment headlineListFragment) {
        super(context, resource, objects);

        mContext = context;
        mTextViewResourceId = resource;
        mItems = objects;
        mListener = headlineListFragment;

        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * 逆順に取得する。
     *
     * @param position
     * @return
     */
    @Override
    public Headline getItem(int position) {
        //TODO 先頭から挿入できるリストを使用してこの記述をなくす↓
        return mItems.get(position);
    }

    /**
     * 一致するアイテムがあれば置き換えます。
     * @param headline
     * @return
     */
    public boolean setItem(Headline headline) {
        for(Headline h : mItems) {
            if(h.getmSysId().equals(headline.getmSysId())) {
                h = headline;
                return true;
            }
        }
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView != null) {
            view = convertView;
        } else {
            view = mInflater.inflate(mTextViewResourceId, null);
        }

        Headline item = getItem(position);
        Log.d(TAG, "getView() [ " + position + "] : " + item.getmTitle());
        TextView tagNewTextView = (TextView) view.findViewById(R.id.tag_new);
        TextView titleTextView = (TextView) view.findViewById(R.id.title);
        TextView publicationDateTextView = (TextView) view.findViewById(R.id.publication_date);
        TextView siteTitleTextView = (TextView) view.findViewById(R.id.site_title);

        /*サムネイル　追加コード*/
         thumbnailView = (ImageView) view.findViewById(R.id.thumbnail);
        //ロード前はグレー
        thumbnailView.setVisibility(View.VISIBLE);
        thumbnailView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.default_thumbnail));
        thumbnailView.setColorFilter(mContext.getResources().getColor(R.color.ripple));
        thumbnailView.setTag(position);
        mListener.onHeadlineDisplayed(item, thumbnailView);
        //TODO 消す 実験用
        /*thumbnailView = (ImageView) view.findViewById(R.id.thumbnail);
        thumbnailView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Rect rect = thumbnailView.getDrawable().getBounds();
                float scaleX = (float) thumbnailView.getWidth() / (float) rect.width();
                float scaleY = (float) thumbnailView.getHeight() / (float) rect.height();
                float scale = Math.min(scaleX, scaleY);
                float width = scale * rect.width();
                float height = scale * rect.height();
                Log.d(TAG, "size:" + width + " x " + height);
            }
        });*/
/*        Bitmap bmp = null;
        byte[] bytes = item.getmThumbnail(); //ここに画像データが入っているものとする
        if (bytes != null) {
            if(bytes.length == 1) {
                thumbnailView.setVisibility(View.INVISIBLE);
            } else {
                Log.d(TAG, "thumbnail size : " + bytes.length);
                bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                thumbnailView.setVisibility(View.VISIBLE);
                thumbnailView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                thumbnailView.setImageBitmap(bmp);
                thumbnailView.setColorFilter(mContext.getResources().getColor(R.color.transparent));
            }
        } else {
            thumbnailView.setVisibility(View.VISIBLE);
            thumbnailView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.default_thumbnail));
            thumbnailView.setColorFilter(mContext.getResources().getColor(R.color.ripple));
        }*/
        /*サムネイル　追加コード*/


        titleTextView.setText(item.getmTitle());
        //publicationDateTextView.setText(item.getFormedPublicationDate("MM/dd HH:mm"));
        publicationDateTextView.setText(item.getTwitterLikeDate());
        siteTitleTextView.setText(item.getmSiteTitle());

        if (item.isNew()) {
            tagNewTextView.setVisibility(View.VISIBLE);
        } else {
            tagNewTextView.setVisibility(View.GONE);
        }

        if (item.isRead()) {
            titleTextView.setTypeface(null, Typeface.NORMAL);
            titleTextView.setTextColor(Color.argb(170, 0, 0, 0));
            publicationDateTextView.setTextColor(Color.argb(170, 0, 0, 0));
            siteTitleTextView.setTextColor(Color.argb(170, 0, 0, 0));
        } else {
            titleTextView.setTypeface(null, Typeface.BOLD);
            titleTextView.setTextColor(mContext.getResources().getColor(R.color.primary_text));
            publicationDateTextView.setTextColor(Color.argb(170, 0, 0, 255));
            siteTitleTextView.setTextColor(mContext.getResources().getColor(R.color.secondary_text));
        }
        //以下にリストに表示したい項目を追加する。

        return view;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    public interface OnHeadlineDisplayedListener {
        void onHeadlineDisplayed(Headline headline, ImageView imageView);
    }
}
