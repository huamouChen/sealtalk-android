package cn.chenhuamou.im.ui.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import cn.chenhuamou.im.ui.activity.BetActivity;
import io.rong.imkit.RongExtension;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imkit.plugin.image.PictureSelectorActivity;
import io.rong.imlib.model.Conversation;

/**
 * Created by Rex on 2018/3/28.
 * Email chenhm4444@gmail.com
 * 自定义聊天界面 加号 按钮的插件
 */

public class MyPlugin implements IPluginModule {

    Conversation.ConversationType conversationType;
    String targetId;

    @Override
    public Drawable obtainDrawable(Context context) {  // 自定义插件要显示的图片
        return ContextCompat.getDrawable(context, io.rong.imkit.R.drawable.rc_ext_plugin_location_selector);
    }

    @Override
    public String obtainTitle(Context context) {   // 自定义插件图标下的标题
        return "玩法";
    }

    @Override
    public void onClick(Fragment fragment, RongExtension rongExtension) {   // 点击事件
        this.conversationType = rongExtension.getConversationType();
        this.targetId = rongExtension.getTargetId();

        Intent intent = new Intent(fragment.getActivity(), BetActivity.class);
        intent.putExtra("targetId", targetId);
        intent.putExtra("conversationType", conversationType);
        rongExtension.startActivityForPluginResult(intent, 100, this);
    }

    @Override
    public void onActivityResult(int i, int i1, Intent intent) {

    }
}
