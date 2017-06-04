package site.xulian.learning.controller;

import com.jfinal.ext.render.csv.CsvRender;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Record;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.log4j.Logger;
import site.xulian.learning.base.BaseController;
import site.xulian.learning.model.Answer;
import site.xulian.learning.model.RecordStatistics;
import site.xulian.learning.model.Records;
import site.xulian.learning.utils.DataObj;
import site.xulian.learning.utils.MyCommon;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ControllerBind(controllerKey="/manage/recordStatistics",viewPath="/")
public class RecordsStatisticsController extends BaseController {
	private Logger log = Logger.getLogger(getClass());
	/**
	 * 获取列表数据
	 */
	public void getData(){
		String student_name = getPara("student_name");
		int pageNum = getParaToInt("pageNum", 1);
		int pageSize = getParaToInt("pageSize", 10);
		String orderBy = getPara("prop");
		boolean isASC = "ascending".equals(getPara("order"));// ascending为升序，其他为降序
		renderJson(RecordStatistics.dao.getData(student_name, pageNum, pageSize, orderBy, isASC));
	}

	/**
	 * 导出列表数据
	 */
	public void importData(){
		List<Record> list = RecordStatistics.dao.getData();
		String[] header={"编号","学号","名字","创建次数","回复次数","总次数","提问次数","创建时间","更新时间"};
		String[] column={"id","student_id","student_name","create_times","reply_times","sum_tims","ask_times","create_time","update_time"};
		String fileName = "record_"+ DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss")+".csv";
		try{
			CsvRender me = MyCommon.exportCsvRender(header,column,list,fileName);
			render(me);
		}catch(Exception e){
			renderNull();
		}
	}

}