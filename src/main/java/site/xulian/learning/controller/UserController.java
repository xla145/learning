package site.xulian.learning.controller;

import com.jfinal.core.Controller;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.upload.UploadFile;
import org.apache.log4j.Logger;
import site.xulian.learning.base.BaseController;
import site.xulian.learning.model.Student;
import site.xulian.learning.model.User;
import site.xulian.learning.utils.DataObj;
import site.xulian.learning.utils.MyCommon;

import java.util.List;

/**
 * @author Administrator
 *	登录管理
 */
@ControllerBind(controllerKey="/manage/user",viewPath="/")
public class UserController extends BaseController {
	Logger log = Logger.getLogger(this.getClass());
	public void index(){
	 render("login.html");
	}
	/**
	* 用户登录
	* */
	public void login(){
	  DataObj<User> userDataObj = User.dao.login(getPara("name"), getPara("pwd"), getParaToInt("type"));
	  if(userDataObj.isSuccessCode()){
		  setSessionAttr("user", getPara("name"));
		  renderJson(userDataObj);
		  return;
	  }
//	  log.info(userDataObj.getMsg());
	  renderErrorText(userDataObj.getMsg());
	}
	/**
	* 退出登录
	* */
	public void logout(){
      if(getSession().getAttribute("name") != null){
        getSession().removeAttribute("name");
      }
      renderJson();
	}

	/**
	 * 修改密码
	 * */
	public void updatePwd(){
//      User user = getModel(User.class,"");
      String name = getPara("name");
      String newPwd = getPara("newPwd");
      String ordPwd = getPara("ordPwd");
        System.out.println(name);
      if(name == null){
          renderErrorText("找不到用户账号");
      }
      DataObj<User> data = User.dao.updatePwd(name,ordPwd,newPwd);
      if (data.isSuccessCode()) {
          renderNull();
          return;
      }
      renderErrorText(data.getMsg());
	}

	/**
	 * 批量导入学生用户数据
	 * */
	public void bathStudentAdd(){
		UploadFile uploadFile = getFile("users");
		if (uploadFile == null) {
            renderErrorText("请上传文件");
            return;
		}
		List<String[]> userList = MyCommon.readExcel(uploadFile.getFile(),"student");
        DataObj<String> dataObj = Student.dao.bathAdd(userList);
        if (dataObj.isSuccessCode()) {
            renderNull();
        }
        renderErrorText(dataObj.getMsg());
	}
    /**
     * 批量导入老师用户数据
     * */
    public void bathTeacherAdd(){
        UploadFile uploadFile = getFile("users");
        if (uploadFile == null) {
            renderErrorText("请上传文件");
            return;
        }
        List<String[]> userList = MyCommon.readExcel(uploadFile.getFile(),"teacher");

    }
	/**
	 * 获取session
	 * */
	public void getSessions(){
        renderJson(getSession().getAttribute("user"));
	}

    /**
     * 获取用户信息
     * */
    public void getInfo(){
        renderJson(User.dao.getUser(getPara("name"),getParaToInt("type")));
    }

    /**
     * 上传头像
     * */
    public void upload(){
        UploadFile uploadFile =getFile("file");
        if (uploadFile == null){
            renderErrorText("上传错误！");
            return;
        }
        String path = uploadFile.getUploadPath();
        renderJson(path);
    }

    public void save(){
        String user_id = getPara("user_id");
        String img = getPara("img");
        Integer type = getParaToInt("type");
        DataObj<String> dataObj = User.dao.saveUser(user_id,img);
        if (dataObj.isSuccessCode()){
            renderNull();
            return;
        }
        renderErrorText(dataObj.getMsg());
    }

}
