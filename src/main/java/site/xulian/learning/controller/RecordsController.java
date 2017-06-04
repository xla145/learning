package site.xulian.learning.controller;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.oreilly.servlet.multipart.LimitedServletInputStream;
import org.apache.log4j.Logger;
import site.xulian.learning.base.BaseController;
import site.xulian.learning.model.Records;
import site.xulian.learning.model.Topic;
import site.xulian.learning.utils.DataObj;

import java.util.List;

@ControllerBind(controllerKey="/manage/record",viewPath="/")
public class RecordsController extends BaseController {
	private Logger log = Logger.getLogger(getClass());

	/**
	 * 编辑记录
	 */
	public void info(){
		int id = getParaToInt("id");
		List<Record> list = Records.dao.getData(id);
		if(list.isEmpty()){
			renderErrorText("该记录不存在");
		}else{
			renderJson(list);
		}
	}
	/**
	 * 保存修改或者添加的数据
	 */
	public void save(){
		log.info("保存数据："+JsonKit.toJson(getParaMap()));
		String name = (String) getSession().getAttribute("user");
		Records model = getModel(Records.class,"");
		log.info("---------------"+JsonKit.toJson(model));
		DataObj<Records> data = Records.dao.saveRecords(model);
		if (data.isSuccessCode()) {
			renderNull();
			return;
		}
		renderErrorText(data.getMsg());
	}

}