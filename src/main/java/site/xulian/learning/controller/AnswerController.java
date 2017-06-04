package site.xulian.learning.controller;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.JsonKit;
import org.apache.log4j.Logger;
import site.xulian.learning.base.BaseController;
import site.xulian.learning.model.Answer;
import site.xulian.learning.model.Topic;
import site.xulian.learning.utils.DataObj;

@ControllerBind(controllerKey="/manage/answer",viewPath="/")
public class AnswerController extends BaseController {
	private Logger log = Logger.getLogger(getClass());
	
	/**
	 * 获取列表数据
	 */
	public void getData(){
		String title = getPara("title");
		Integer type = getParaToInt("type");
		Integer status = getParaToInt("status");
		int pageNum = getParaToInt("pageNum", 1);
		int pageSize = getParaToInt("pageSize", 10);
		String orderBy = getPara("prop");
		boolean isASC = "ascending".equals(getPara("order"));// ascending为升序，其他为降序
		String name = getPara("student_id");
		renderJson(Answer.dao.getData(title,type,status,name, pageNum, pageSize, orderBy, isASC));
	}
	
	/**
	 * 编辑记录
	 */
	public void info(){
		int id = getParaToInt("id");
		Answer model = Answer.dao.findById(id);
		if(model == null){
			renderErrorText("该记录不存在，无法编辑");
		}else{
			renderJson(model);
		}
	}
	
	/**
	 * 保存修改或者添加的数据
	 */
	public void save(){
		log.info("保存数据："+JsonKit.toJson(getParaMap()));
		Answer model = getModel(Answer.class,"");
		log.info("---------------"+JsonKit.toJson(model));
		DataObj<Answer> data = Answer.dao.saveAnswer(model);
		if (data.isSuccessCode()) {
			renderNull();
			return;
		}
		renderErrorText(data.getMsg());
	}
	
	/**
	 * 删除记录
	 */
	public void delete(){
		String[] ids  = getParaValues("topicId");
		if(ids == null || ids.length == 0){
			renderErrorText("参数错误");
			return;
		}
		DataObj<String> result = Topic.dao.delete(ids);
		if(result.isSuccessCode()){
			renderNull();
			return;
		}
        renderErrorText(result.getMsg());
	}

	/**
	 * 解答
	 */
	public void deply(){
		log.info("保存数据："+JsonKit.toJson(getParaMap()));
		Answer model = getModel(Answer.class,"");
		log.info("---------------"+JsonKit.toJson(model));
		DataObj<Answer> data = Answer.dao.updateAnswer(model);
		if (data.isSuccessCode()) {
			renderNull();
			return;
		}
		renderErrorText(data.getMsg());
	}

}