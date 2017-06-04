package site.xulian.learning.model;

import com.jfinal.kit.JsonKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.omg.PortableInterceptor.INACTIVE;
import site.xulian.learning.model.base.BaseStudent;
import site.xulian.learning.utils.Common;
import site.xulian.learning.utils.DataObj;
import site.xulian.learning.utils.MD5Salt;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class Student extends BaseStudent<Student> {
	public static final Student dao = new Student().dao();
	Logger logger = Logger.getLogger(this.getClass());
	/**
	 * 分页获取数据
	 *
	 * @param student_id
	 * @param pageSize
	 * @param orderBy
	 * @param isASC
	 * @return
	 */
	public Page<Student> getData(String student_id, int pageNum, int pageSize, String orderBy, boolean isASC) {
		logger.info("student_id:" +student_id);
		List<Object> params = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("FROM student WHERE 1=1 AND status != -5 ");
		if (StringUtils.isNotBlank(Common.nullToBlank(student_id))) {
			sql.append("AND student_id = ? ");
			params.add(student_id);
		}
		orderBy = StrKit.isBlank(orderBy) ? "create_time" : orderBy;
		sql.append("ORDER BY " + orderBy + " " + (isASC ? "" : "DESC "));
		logger.info(sql.toString());
		return paginate(pageNum, pageSize, "SELECT * ", sql.toString(), params.toArray());
	}

	/**
	 * 删除数据
	 */
	public DataObj<String> delete(String[] ids) {
		logger.info("编号信息" + ids);
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE student SET status = -5 , update_time = now()");
		sql.append("WHERE id in (");
		sql.append(Common.arrayToSqlIn(ids));
		sql.append(")");
		Integer num = Db.update(sql.toString());
		if (num > 0) {
			return DataObj.getSuccessData("");
		}
		return new DataObj<>("删除学生信息失败！");
	}

	/**
	 * 保存数据
	 */
	public DataObj<Student> saveStudent(Student student) {
		logger.info("保存数据" + JsonKit.toJson(student));
		try {
			boolean success = false;
            if (null == student.getId()) {
                User user = new User();
                student.setCreateTime(new Date());
                student.setUpdateTime(new Date());
                success = student.save();
                user.setName(student.getStudentId());
                user.setPwd(MD5Salt.md5("123456"));
                user.setType(0);
                user.save();
            } else {
                student.setUpdateTime(new Date());
                success = student.update();
            }
			if (!success) {
				return new DataObj<>("保存数据失败，稍后请重试");
			}
		} catch (Exception e) {
			logger.warn("保存学生数据异常", e);
			return new DataObj<>("添加数据失败，稍后请重试");
		}
		return DataObj.getSuccessData(student);
	}
	/**
	 * 批量插入学号信息
	 * */
	public DataObj<String> bathAdd(final List<String[]> list) {
		logger.info("学生信息: "+ JsonKit.toJson(list));
		final Record record = new Record();
		boolean isSuccess = Db.tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {
				try{
					StringBuilder sql = new StringBuilder();
					StringBuilder sql_1 = new StringBuilder("INSERT INTO user(name,pwd,type,create_time,update_time) VALUES");
					List<Object> parmas =new ArrayList<Object>();
					List<Object> parmas_1 =new ArrayList<Object>();
					sql.append("INSERT INTO student(name,class_name,phone,sex,grade_name,student_id,update_time,create_time) VALUES");
					for (int i = 1; i < list.size() ; i++) {//从第二行开始遍历 去除列表名
						sql_1.append("(?,?,?,now(),now()),");
						parmas_1.add(list.get(i)[5]);
                        parmas_1.add(MD5Salt.md5("123456"));
                        parmas_1.add(0);
						sql.append("(?,?,?,?,?,?,now(),now()),");
						saveFlowCoupon(list.get(i)[0],list.get(i)[1],list.get(i)[2],list.get(i)[3],list.get(i)[4],list.get(i)[5],parmas);
					}
					Db.update(sql.deleteCharAt(sql.length()-1).toString(),parmas.toArray());
					Db.update(sql_1.deleteCharAt(sql_1.length()-1).toString(),parmas_1.toArray());
				}catch(Exception e){
                    e.printStackTrace();
					logger.info("插入错误: "+e.getMessage());
					record.set("msg", "插入失败,请重新上传");
					return false;
				}
				return true;
			}
		});
		if(isSuccess){
			return DataObj.getSuccessData("");
		}
		return new DataObj<String>(record.get("msg").toString());
	}

	/**
	 * 保存学生数据
	 * @throws ParseException
	 * */
	private void saveFlowCoupon(String name, String class_name, String phone, String sex, String grade_name, String student_id, List<Object> parmas) throws ParseException{
		parmas.add(Common.nullToBlank(name));
		parmas.add(Common.nullToBlank(class_name));// 此处转换为int是为了防止读取的uid为double类型，导致自动添加.00后缀
		parmas.add(Common.nullToBlank(phone));
		parmas.add(Integer.parseInt(sex));
		parmas.add(grade_name);
		parmas.add(student_id);
	}
}
