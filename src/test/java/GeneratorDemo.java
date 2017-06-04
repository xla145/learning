import javax.sql.DataSource;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.util.JdbcConstants;
import com.alibaba.druid.wall.WallFilter;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.plugin.activerecord.generator.Generator;
import com.jfinal.plugin.activerecord.generator.MetaBuilder;
import com.jfinal.plugin.druid.DruidPlugin;

/**
 * GeneratorDemo
 */
public class GeneratorDemo {
	
	public static DataSource getDataSource() {
		PropKit.use("setting.properties");
		DruidPlugin druidDefault = new DruidPlugin(PropKit.get("db.url"), PropKit.get("db.user"), PropKit.get("db.password"), PropKit.get("db.driver"));
		// StatFilter提供JDBC层的统计信息
		druidDefault.addFilter(new StatFilter());
		// WallFilter的功能是防御SQL注入攻击
		WallFilter wallDefault = new WallFilter();
		wallDefault.setDbType(JdbcConstants.MYSQL);
		druidDefault.addFilter(wallDefault);
//		druidDefault.setInitialSize(PropKit.getInt("db.poolInitialSize"));
//		druidDefault.setMaxPoolPreparedStatementPerConnectionSize(PropKit.getInt("db.poolMaxSize"));
//		druidDefault.setTimeBetweenConnectErrorMillis(PropKit.getInt("db.connectionTimeoutMillis"));
		druidDefault.start();
//		C3p0Plugin c3p0Plugin = new C3p0Plugin(p.get("db.url"), p.get("db.user"), p.get("db.password"));
//		c3p0Plugin.start();
		return druidDefault.getDataSource();
	}
	
	public static void main(String[] args) {
		// base model 所使用的包名
		String baseModelPackageName = "site.xulian.learning.model.base";
		// base model 文件保存路径
		String baseModelOutputDir = PathKit.getWebRootPath() + "/src/main/java/site/xulian/learning/model/base";
		
		// model 所使用的包名 (MappingKit 默认使用的包名)
		String modelPackageName = "site.xulian.learning.model";
		// model 文件保存路径 (MappingKit 与 DataDictionary 文件默认保存路径)
		String modelOutputDir = baseModelOutputDir + "/..";
		
		// 创建生成器
		DataSource dataSource = getDataSource();
		Generator gernerator = new Generator(dataSource, baseModelPackageName, baseModelOutputDir, modelPackageName, modelOutputDir);

//		MetaBuilder metaBuilder = new MetaBuilder(getDataSource());
		MyMetaBuilder metaBuilder = new MyMetaBuilder(getDataSource(),"");
		gernerator.setMetaBuilder(metaBuilder);
		// 设置数据库方言
		gernerator.setDialect(new MysqlDialect());

		// 设置是否在 Model 中生成 dao 对象
		gernerator.setGenerateDaoInModel(true);
		// 设置是否生成字典文件
		gernerator.setGenerateDataDictionary(false);
		// 设置需要被移除的表名前缀用于生成modelName。例如表名 "osc_user"，移除前缀 "osc_"后生成的model名为 "User"而非 OscUser
//		gernerator.setRemovedTableNamePrefixes("t_");
		// 生成
		gernerator.generate();
	}
}




