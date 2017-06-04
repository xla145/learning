package site.xulian.learning.controller;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Db;
import org.apache.log4j.Logger;
import site.xulian.learning.base.BaseController;
import site.xulian.learning.model.Topic;
import site.xulian.learning.model.User;
import site.xulian.learning.utils.DataObj;

@ControllerBind(controllerKey="/manage/topic",viewPath="/")
public class TopicController extends BaseController {
	private Logger log = Logger.getLogger(getClass());
	
	/**
	 * 获取列表数据
	 */
	public void getData(){
		String title = getPara("title");
		int pageNum = getParaToInt("pageNum", 1);
		int pageSize = getParaToInt("pageSize", 10);
		String orderBy = getPara("prop");
		boolean isASC = "ascending".equals(getPara("order"));// ascending为升序，其他为降序
		String name = getPara("student_id");
		renderJson(Topic.dao.getData(title,name, pageNum, pageSize, orderBy, isASC));
	}
	
	/**
	 * 编辑记录
	 */
	public void info(){
		int id = getParaToInt("id");
		Topic model = Topic.dao.findById(id);
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
		String name = (String) getSession().getAttribute("user");
		Topic model = getModel(Topic.class,"");
		log.info("---------------"+JsonKit.toJson(model));
		DataObj<Topic> data = Topic.dao.saveTopic(model);
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
		String name = getPara("name");
        System.out.println(name);
        if(name == null) {
            renderErrorText("获取用户信息失败！");
            return;
        }
		User user = User.dao.getUserByName(name);
		if(user.getType() == 0) {
			renderErrorText("你没有权限删除话题！");
			return;
		}
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
	 * 置顶话题
	 * */
	public void stick() {
		Integer id = getParaToInt("topicId");
        Integer type = getParaToInt("type");
        log.info(type);
        if(type == 0) {
            renderErrorText("置顶失败！,没有置顶权限");
            return;
        }
		int num = Db.update("update topic set isStick = 1 , update_time = now() WHERE id = ?",id);
		if(num > 0){
			renderNull();
			return;
		}
		renderErrorText("置顶失败！");
	}
    /**
     * 获取列表数据
     */
    public void getAllData(){
        String title = getPara("title");
        int pageNum = getParaToInt("pageNum", 1);
        int pageSize = getParaToInt("pageSize", 10);
        String orderBy = getPara("prop");
        System.out.println(orderBy);
        boolean isASC = "ascending".equals(getPara("order"));// ascending为升序，其他为降序
        renderJson(Topic.dao.getData(title,pageNum, pageSize,orderBy, isASC));
    }

}