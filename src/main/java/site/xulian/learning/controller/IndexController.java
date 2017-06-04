package site.xulian.learning.controller;

import com.jfinal.ext.route.ControllerBind;
import org.apache.log4j.Logger;
import site.xulian.learning.base.BaseController;
import site.xulian.learning.model.User;
import site.xulian.learning.utils.DataObj;

/**
 * @author Administrator
 *	登录管理
 */
@ControllerBind(controllerKey="/",viewPath="/")
public class IndexController extends BaseController {
  Logger log = Logger.getLogger(this.getClass());
  public void index(){
	 render("index.html");
  } 

}
