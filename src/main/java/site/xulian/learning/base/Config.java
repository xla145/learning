package site.xulian.learning.base;

import com.jfinal.config.*;
import com.jfinal.ext.plugin.quartz.QuartzPlugin;
import com.jfinal.ext.route.AutoBindRoutes;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.ehcache.EhCachePlugin;
import com.jfinal.template.Engine;
import site.xulian.learning.interceptor.AllowCrossDomain;
import site.xulian.learning.interceptor.AttrInter;
import site.xulian.learning.interceptor.LoginInter;
import site.xulian.learning.model._MappingKit;


/**
 * API引导式配置
 */
public class Config extends JFinalConfig {

	/**
	 * 配置常量
	 */
	public void configConstant(Constants me) {
		// 加载少量必要配置，随后可用PropKit.get(...)获取值
		PropKit.use("setting.properties");// 加载用户配置文件，获取值直接使用：PropKit.get(key值)
		me.setDevMode(PropKit.getBoolean("devMode", false));
		//me.setViewType(ViewType.FREE_MARKER);
	}

	/**
	 * 配置路由
	 */
	public void configRoute(Routes me) {
		AutoBindRoutes routeBind = new AutoBindRoutes();
		routeBind.autoScan(false);
		me.add(routeBind);
	}

	@Override
	public void configEngine(Engine me) {

	}
	/**
	 * 配置插件
	 */
	public void configPlugin(Plugins me) {
		// 配置DruidPlugin数据库连接池插件
		DruidPlugin druidDefault = new DruidPlugin(PropKit.get("db.url"),
				PropKit.get("db.user"), PropKit.get("db.password"),
				PropKit.get("db.driver"));
		me.add(druidDefault);
		ActiveRecordPlugin arp = new ActiveRecordPlugin(druidDefault);
		// 所有映射在 MappingKit 中自动化搞定
		_MappingKit.mapping(arp);
		me.add(arp);
		//添加定时器
		me.add(new QuartzPlugin("job.properties"));
		// 添加缓存
		//me.add(new EhCachePlugin());
	}

	/**
	 * 配置全局拦截器
	 */
	public void configInterceptor(Interceptors me) {
//		me.add(new LoginInter());
		me.add(new AttrInter());
		me.add(new AllowCrossDomain());
	}

	/**
	 * 配置处理器
	 */
	public void configHandler(Handlers me) {

	}
}
