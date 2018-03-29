package cn.chenhuamou.im.ui.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;

import io.rong.imkit.RongExtension;
import io.rong.imkit.plugin.IPluginModule;

/**
 * Created by Rex on 2018/3/28.
 * Email chenhm4444@gmail.com
 * 自定义聊天界面 加号 按钮的插件
 */

public class MyPlugin implements IPluginModule {
    @Override
    public Drawable obtainDrawable(Context context) {  // 自定义插件要显示的图片
        return null;
    }

    @Override
    public String obtainTitle(Context context) {   // 自定义插件图标下的标题
        return null;
    }

    @Override
    public void onClick(Fragment fragment, RongExtension rongExtension) {   // 点击事件

    }

    @Override
    public void onActivityResult(int i, int i1, Intent intent) {

    }
}
