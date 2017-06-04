package site.xulian.learning.model;

import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Db;
import site.xulian.learning.model.base.BaseUser;
import site.xulian.learning.utils.DataObj;
import site.xulian.learning.utils.MD5Salt;

import java.util.List;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class User extends BaseUser<User> {
	public static final User dao = new User().dao();
	Logger logger = Logger.getLogger(this.getClass());
	/**
	 * 用户登录
	 * */
	public DataObj<User> login(String userName, String password, Integer type){
		logger.info("用户信息：用户名"+userName+",类型："+type);
		password = MD5Salt.md5Salt(password, false);//将密码加密
		User user = User.dao.findFirst("SELECT * FROM user where name = ? and pwd = ? and type = ? ",userName,password,type);
		if(null == user){
			return new DataObj<>("用户名或密码错误！请重新输入");
		}
		Db.update("UPDATE user set latest_login = now() where id = ?",user.getId());//记录登录时间
		return DataObj.getSuccessData(user);
	}

	/**
	 * 修改密码
	 * */
	public DataObj<User> updatePwd(String userName,String ordPwd,String newPwd){
		User user = User.dao.findFirst("SELECT * FROM user WHERE name = ?",userName);
		if (user == null) {
			return new DataObj<>("原密码有误！");
		}
		try {
			newPwd = MD5Salt.md5Salt(newPwd, false);//将密码加密
			int num = Db.update("update user set pwd = ? where name = ?",newPwd,userName);
			if (num == 0) {
				return new DataObj<>("修改密码失败！！");
			}
		}catch (Exception e){
			logger.error("修改密码失败！失败原因："+e.getMessage());
			return new DataObj<>("修改密码失败！！");
		}
		return DataObj.getSuccessData(user);
	}

	/**
	 *
	 * 获取用户信息
	 */
	public User getUserByName(String name) {
		return findFirst("select * from user where name = ?",name);
	}
}
