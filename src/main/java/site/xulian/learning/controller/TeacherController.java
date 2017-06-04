package site.xulian.learning.controller;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.JsonKit;
import com.jfinal.upload.UploadFile;
import org.apache.log4j.Logger;
import org.apache.poi.ss.formula.functions.T;
import site.xulian.learning.base.BaseController;
import site.xulian.learning.model.Student;
import site.xulian.learning.model.Teacher;
import site.xulian.learning.model.User;
import site.xulian.learning.utils.DataObj;
import site.xulian.learning.utils.MyCommon;

import java.util.List;

/**
 * @author Administrator
 *	登录管理
 */
@ControllerBind(controllerKey="/manage/teacher",viewPath="/")
public class TeacherController extends BaseController {
	Logger log = Logger.getLogger(this.getClass());
	/**
	 * 获取列表数据
	 */
	public void getData(){
		String teacher_id = getPara("teacher_id");
		int pageNum = getParaToInt("pageNum", 1);
		int pageSize = getParaToInt("pageSize", 10);
		String orderBy = getPara("prop");
		boolean isASC = "ascending".equals(getPara("order"));// ascending为升序，其他为降序
		renderJson(Teacher.dao.getData(teacher_id, pageNum, pageSize, orderBy, isASC));
	}

	/**
	 * 编辑记录
	 */
	public void info(){
		int id = getParaToInt("id");
		Teacher model = Teacher.dao.findById(id);
		System.out.println(JsonKit.toJson(model));
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
		log.info("保存数据："+ JsonKit.toJson(getParaMap()));
		Teacher model = getModel(Teacher.class,"");
		log.info("---------------"+JsonKit.toJson(model));
		DataObj<Teacher> data = Teacher.dao.saveTeacher(model);
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
		String[] ids  = getParaValues("ids");
		if(ids == null || ids.length == 0){
			renderErrorText("参数错误");
			return;
		}
		DataObj<String> result = Teacher.dao.delete(ids);
		if(result.isSuccessCode()){
			renderNull();
			return;
		}
		renderErrorText(result.getMsg());
	}
	/**
	 * 批量导入学生用户数据
	 * */
	public void bathTeacherAdd(){
		UploadFile uploadFile = getFile("file");
		if (uploadFile == null) {
			renderErrorText("请上传文件");
			return;
		}
		List<String[]> userList = MyCommon.readExcel(uploadFile.getFile(),uploadFile.getFileName());
		DataObj<String> dataObj = Teacher.dao.bathAdd(userList);
		if (dataObj.isSuccessCode()) {
			renderNull();
		}
		renderErrorText(dataObj.getMsg());
	}
}
