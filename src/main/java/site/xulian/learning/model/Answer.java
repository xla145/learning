package site.xulian.learning.model;

import com.jfinal.kit.JsonKit;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import org.apache.commons.lang3.StringUtils;
import site.xulian.learning.model.base.BaseAnswer;
import site.xulian.learning.utils.Common;
import site.xulian.learning.utils.DataObj;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class Answer extends BaseAnswer<Answer> {
    public static final Answer dao = new Answer().dao();
    private Logger log = Logger.getLogger(getClass());

    /**
     * 分页获取数据
     *
     * @param title
     * @param pageSize
     * @param orderBy
     * @param isASC
     * @return
     */
    public Page<Answer> getData(String title, Integer type, Integer status, String name, int pageNum, int pageSize, String orderBy, boolean isASC) {
        log.info("title:" + title + ",name" + name);
        List<Object> params = new ArrayList<Object>();
        StringBuffer sql = new StringBuffer();
        sql.append("FROM answer WHERE 1=1 AND status != -5 ");
        if (null != name) {
            User user = getUserByUserName(name);
            if (user.getType() == 0) { // 学生
                if (type == 1) {
                    sql.append("AND student_id = ? AND type = ? ");
                    params.add(name);
                    params.add(type);
                } else {
                    sql.append("AND type = ? ");
                    params.add(type);
                }
            } else {
                sql.append("AND type = ? ");
                params.add(type);
            }
        }
        if (null != status) {
            sql.append("AND status = ? ");
            params.add(status);
        }
        if (StringUtils.isNotBlank(Common.nullToBlank(title))) {
            sql.append("AND title like ?");
            params.add(Common.queryLike(title));
        }
        orderBy = StrKit.isBlank(orderBy) ? "create_time" : orderBy;
        sql.append("ORDER BY " + orderBy + " " + (isASC ? "" : "DESC "));
        log.info(sql.toString());
        return paginate(pageNum, pageSize, "SELECT * ", sql.toString(), params.toArray());
    }

    /**
     * 删除数据
     */
    public DataObj<String> delete(String[] ids) {
        log.info("编号信息" + ids);
        StringBuffer sql = new StringBuffer();
        sql.append("UPDATE answer SET status = -5 , update_time = now()");
        sql.append("WHERE id in (");
        sql.append(Common.arrayToSqlIn(ids));
        sql.append(")");
        Integer num = Db.update(sql.toString());
        if (num > 0) {
            return DataObj.getSuccessData("");
        }
        return new DataObj<>("删除话题信息失败！");
    }

    /**
     * 删除数据
     */
    public DataObj<Answer> saveAnswer(Answer answer) {
        log.info("保存数据" + JsonKit.toJson(answer));
        log.info(answer.getStudentId());
        Record record = Db.findFirst("SELECT * FROM student where student_id = ?", answer.getStudentId());
        log.info("------" + JsonKit.toJson(record));
        if (null != record) {
            log.info(record.get("name").toString());
            answer.setStudentName(record.get("name").toString());
        }
        answer.setType(1);
        answer.setStatus(0);
        try {
            boolean success = false;
            if (null == answer.getId()) {
                answer.setCreateTime(new Date());
                answer.setUpdateTime(new Date());
                success = answer.save();
            } else {
                answer.setUpdateTime(new Date());
                success = answer.update();
            }
            if (!success) {
                return new DataObj<>("保存数据失败，稍后请重试");
            }
        } catch (Exception e) {
            log.warn("保存话题类型异常", e);
            return new DataObj<>("添加数据失败，稍后请重试");
        }
        return DataObj.getSuccessData(answer);
    }

    public User getUserByUserName(String userName) {
        return User.dao.findFirst("SELECT * FROM user WHERE name = ?", userName);
    }

    /**
     * 老师解答
     */
    public DataObj<Answer> updateAnswer(Answer answer) {
//	    String sql = "UPFATE answer set teacher_name =?, type =?, status = ?, explanation = ?, update_time = now()";
//	    List<Object> list = new ArrayList<Object>();
        try {
            boolean success = false;
            if (null != answer.getTeacherName()) {
                Record record = Db.findFirst("SELECT * FROM teacher where teacher_id = ?", answer.getTeacherName());
                answer.setTeacherName(record.get("name").toString());
            }
            answer.setStatus(5);
            answer.setUpdateTime(new Date());
            success = answer.update();
            if (!success) {
                return new DataObj<>("更新数据失败，稍后请重试");
            }
        } catch (Exception e) {
            log.warn("保存话题类型异常", e);
            return new DataObj<>("更新数据失败，稍后请重试");
        }
        return DataObj.getSuccessData(answer);
    }

    public List<Record> getAnswer(){
        return Db.find("SELECT SUM(id) sum_times ,COUNT(a.student_id) ask_times, a.student_id,a.student_name\n" +
                "FROM answer a\n" +
                "GROUP BY a.student_id");
    }
}
