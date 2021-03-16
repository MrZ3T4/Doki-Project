package dev.mrz3t4.literatureclub.Utils;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

public class ExpandableCardView {

    Context c;
    CardView cardView;

    public ExpandableCardView(@NonNull Context context, CardView cardView) {
        this.c = context;
        this.cardView = cardView;
    }

    public void expand() {
        int initialHeight = cardView.getHeight();

        cardView.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int targetHeight = cardView.getMeasuredHeight();

        int distanceToExpand = targetHeight - initialHeight;

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1){
                    // Do this after expanded
                }

                cardView.getLayoutParams().height = (int) (initialHeight + (distanceToExpand * interpolatedTime));
                cardView.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        a.setDuration(300);
        cardView.startAnimation(a);
    }

    public void collapse(int collapsedHeight) {
        int initialHeight = cardView.getMeasuredHeight();

        int distanceToCollapse = initialHeight - collapsedHeight;

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1){
                    // Do this after collapsed
                }


                Log.i("TAG", "Collapse | InterpolatedTime = " + interpolatedTime);

                cardView.getLayoutParams().height = (int) (initialHeight - (distanceToCollapse * interpolatedTime));
                cardView.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        a.setDuration(300);
        cardView.startAnimation(a);
    }

}
