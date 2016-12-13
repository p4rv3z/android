package bd.com.parvez.viewpager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by ParveZ on 9/12/2016.
 */
public class CustomPage extends PagerAdapter {

    Context context;
    LayoutInflater inflater;
    public CustomPage(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return ImageResorces.images.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view==(View)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.page_view,container,false);
        TextView textView = (TextView) itemView.findViewById(R.id.textView);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
        textView.setText("Image: "+(position+1));
        imageView.setImageResource(ImageResorces.images[position]);
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
