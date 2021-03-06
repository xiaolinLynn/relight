package com.ittianyu.relight.widget.stateful;

import android.content.Context;
import android.view.View;

import com.ittianyu.relight.widget.Widget;
import com.ittianyu.relight.widget.native_.BaseAndroidWidget;
import com.ittianyu.relight.widget.stateless.StatelessWidget;

public abstract class StatefulWidget<T extends View> implements Widget<T> {
    protected Context context;
    protected AsyncState<T> state;
    protected Widget<T> widget;

    public StatefulWidget(Context context) {
        this.context = context;
    }

    abstract protected AsyncState<T> createState(Context context);

    @Override
    public T render() {
        if (null == state) {
            state = createState(context);
            state.init();
            widget = state.build(context);
        }
        return widget.render();
    }

    public void setState(Runnable func) {
        state.setState(func);
    }

    public void setStateAsync(Runnable func) {
        state.setStateAsync(func);
    }


    public void updateProps(Widget widget) {
        // stateless 的实例 widget 并不是直接渲染的 widget，所以这里对里面的实际 widget 进行获取
        if (widget instanceof StatefulWidget) {
            widget = ((StatefulWidget) widget).widget;
        }

        if (widget instanceof BaseAndroidWidget) {
            ((BaseAndroidWidget) widget).updateProps(widget.render());
        } else if (widget instanceof StatefulWidget) {
            ((StatefulWidget) widget).updateProps(widget);
        } else if (widget instanceof StatelessWidget) {
            ((StatelessWidget) widget).updateProps(widget);
        }
    }
}
