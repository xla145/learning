package site.xulian.learning.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseStudent<M extends BaseStudent<M>> extends Model<M> implements IBean {

	public void setId(java.lang.Integer id) {
		set("id", id);
	}

	public java.lang.Integer getId() {
		return get("id");
	}

	public void setName(java.lang.String name) {
		set("name", name);
	}

	public java.lang.String getName() {
		return get("name");
	}

	public void setClassName(java.lang.String className) {
		set("class_name", className);
	}

	public java.lang.String getClassName() {
		return get("class_name");
	}

	public void setPhone(java.lang.String phone) {
		set("phone", phone);
	}

	public java.lang.String getPhone() {
		return get("phone");
	}

	public void setSex(java.lang.Integer sex) {
		set("sex", sex);
	}

	public java.lang.Integer getSex() {
		return get("sex");
	}

	public void setGradeName(java.lang.String gradeName) {
		set("grade_name", gradeName);
	}

	public java.lang.String getGradeName() {
		return get("grade_name");
	}

	public void setStudentId(java.lang.String studentId) {
		set("student_id", studentId);
	}

	public java.lang.String getStudentId() {
		return get("student_id");
	}

	public void setUpdateTime(java.util.Date updateTime) {
		set("update_time", updateTime);
	}

	public java.util.Date getUpdateTime() {
		return get("update_time");
	}

	public void setCreateTime(java.util.Date createTime) {
		set("create_time", createTime);
	}

	public java.util.Date getCreateTime() {
		return get("create_time");
	}

	public void setStatus(java.lang.Integer status) {
		set("status", status);
	}

	public java.lang.Integer getStatus() {
		return get("status");
	}

}
