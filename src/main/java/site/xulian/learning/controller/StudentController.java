package site.xulian.learning.controller;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.JsonKit;
import com.jfinal.upload.UploadFile;
import org.apache.log4j.Logger;
import site.xulian.learning.base.BaseController;
import site.xulian.learning.model.Answer;
import site.xulian.learning.model.Student;
import site.xulian.learning.model.Topic;
import site.xulian.learning.model.User;
import site.xulian.learning.utils.DataObj;
import site.xulian.learning.utils.MyCommon;

import java.util.List;

/**
 * @author Administrator
 *	登录管理
 */
@ControllerBind(controllerKey="/manage/student",viewPath="/")
public class StudentController extends BaseController {
	Logger log = Logger.getLogger(this.getClass());
	public void index(){
	 render("login.html");
	}

    /**
     * 获取列表数据
     */
    public void getData(){
        String student_id = getPara("student_id");
        int pageNum = getParaToInt("pageNum", 1);
        int pageSize = getParaToInt("pageSize", 10);
        String orderBy = getPara("prop");
        boolean isASC = "ascending".equals(getPara("order"));// ascending为升序，其他为降序
        renderJson(Student.dao.getData(student_id, pageNum, pageSize, orderBy, isASC));
    }

    /**
     * 编辑记录
     */
    public void info(){
        int id = getParaToInt("id");
        Student model = Student.dao.findById(id);
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
        Student model = getModel(Student.class,"");
        log.info("---------------"+JsonKit.toJson(model));
        DataObj<Student> data = Student.dao.saveStudent(model);
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
        DataObj<String> result = Student.dao.delete(ids);
        if(result.isSuccessCode()){
            renderNull();
            return;
        }
        renderErrorText(result.getMsg());
    }
	/**
	 * 批量导入学生用户数据
	 * */
	public void bathStudentAdd(){
		UploadFile uploadFile = getFile("file");
		if (uploadFile == null) {
            renderErrorText("请上传文件");
            return;
		}
//        System.out.println(uploadFile.getFile().);
        List<String[]> userList = MyCommon.readExcel(uploadFile.getFile(),uploadFile.getFileName());
        DataObj<String> dataObj = Student.dao.bathAdd(userList);
        if (dataObj.isSuccessCode()) {
            renderNull();
            return;
        }
        renderErrorText(dataObj.getMsg());
	}
}
