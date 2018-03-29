package cn.chenhuamou.im.ui.widget;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.DefaultExtensionModule;
import io.rong.imkit.emoticon.IEmoticonTab;
import io.rong.imkit.plugin.CombineLocationPlugin;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imkit.plugin.ImagePlugin;
import io.rong.imkit.widget.provider.LocationPlugin;
import io.rong.imlib.model.Conversation;

/**
 * Created by Rex on 2018/3/28.
 * Email chenhm4444@gmail.com
 */

public class MyExtensionModule extends DefaultExtensionModule {

    private MyPlugin myPlugin;

    public MyExtensionModule() {

    }

    @Override
    public List<IPluginModule> getPluginModules(Conversation.ConversationType conversationType) {
        List<IPluginModule> pluginModules = new ArrayList<>();

        CombineLocationPlugin locationPlugin =  new CombineLocationPlugin();
        ImagePlugin imagePlugin = new ImagePlugin();

        pluginModules.add(imagePlugin);
        pluginModules.add(locationPlugin);


        return pluginModules;
    }

    @Override
    public List<IEmoticonTab> getEmoticonTabs() {
        return super.getEmoticonTabs();
    }
}
