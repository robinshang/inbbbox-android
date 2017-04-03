package co.netguru.android.inbbbox.feature.shared.view;

import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.style.ClickableSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/**
 * OnTouchListener which supports clicking on links
 */
public class ClickableLinkOnTouchListener implements View.OnTouchListener {

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        final TextView textView = (TextView) v;
        final Spannable buffer = Spannable.Factory.getInstance().newSpannable(textView.getText());
        final int action = event.getAction();

        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_DOWN) {
            int x = (int) event.getX();
            int y = (int) event.getY();

            x -= textView.getTotalPaddingLeft();
            y -= textView.getTotalPaddingTop();

            x += textView.getScrollX();
            y += textView.getScrollY();

            final Layout layout = textView.getLayout();
            final int line = layout.getLineForVertical(y);
            final int off = layout.getOffsetForHorizontal(line, x);

            final ClickableSpan[] link = buffer.getSpans(off, off, ClickableSpan.class);

            if (link.length != 0) {
                if (action == MotionEvent.ACTION_UP) {
                    link[0].onClick(textView);
                }
                
                return true;
            } else {
                Selection.removeSelection(buffer);
            }
        }

        return false;
    }
}
