package co.netguru.android.inbbbox.feature.shared.view;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import co.netguru.android.inbbbox.R;

import static butterknife.ButterKnife.findById;

public class TabLayoutWithNumberIndicator extends TabLayout {

    public TabLayoutWithNumberIndicator(Context context) {
        super(context);
    }

    public TabLayoutWithNumberIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TabLayoutWithNumberIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setupWithViewPager(@Nullable ViewPager viewPager) {
        super.setupWithViewPager(viewPager);
        for (int i = 0; i < getTabCount(); i++) {
            final Tab tab = getTabAt(i);
            if (tab != null) {
                tab.setCustomView(R.layout.tab_layout_with_number_indicator_view);
            }
        }
    }

    public void setTabMainText(int tabNumber, @StringRes int tabText) {
        setTextOnTabCustomView(tabNumber, R.id.tab_layout_main_text_view,
                getResources().getString(tabText));
    }

    public void setTabNumberIndicatorText(int tabNumber, int tabNumberIndicator) {
        if (tabNumberIndicator > 0) {
            setTextOnTabCustomView(tabNumber, R.id.tab_layout_number_indicator_text_view,
                    String.valueOf(tabNumberIndicator));
        }
    }

    private void setTextOnTabCustomView(int tabNumber, @IdRes int textViewRes, String text) {
        final Tab tab = getTabAt(tabNumber);
        if (tab != null) {
            final View view = tab.getCustomView();
            if (view != null) {
                final TextView customTextView = findById(view, textViewRes);
                customTextView.setText(text);
                customTextView.setTextColor(getTabTextColors());
            }
        }
    }
}
