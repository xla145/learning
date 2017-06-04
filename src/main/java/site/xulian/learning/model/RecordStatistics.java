package site.xulian.learning.model;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import org.apache.commons.lang3.StringUtils;
import site.xulian.learning.model.base.BaseRecordStatistics;
import site.xulian.learning.utils.Common;

import java.util.ArrayList;
import java.util.List;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class RecordStatistics extends BaseRecordStatistics<RecordStatistics> {
	public static final RecordStatistics dao = new RecordStatistics().dao();
	/**
	 * 分页获取数据
	 *
	 * @param name
	 * @param pageSize
	 * @param orderBy
	 * @param isASC
	 * @return
	 */
	public Page<RecordStatistics> getData(String name, int pageNum, int pageSize, String orderBy, boolean isASC) {
        System.out.println(name);
        List<Object> params = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("FROM record_statistics WHERE 1=1 ");
		if (StringUtils.isNotBlank(Common.nullToBlank(name))) {
			sql.append("AND student_name like ?");
			params.add(Common.queryLike(name));
		}
		orderBy = StrKit.isBlank(orderBy) ? "create_time" : orderBy;
		sql.append("ORDER BY " + orderBy + " " + (isASC ? "" : "DESC "));
		return paginate(pageNum, pageSize, "SELECT * ", sql.toString(), params.toArray());
	}

	public List<Record> getData() {
		return Db.find("select * from record_statistics");
	}
}