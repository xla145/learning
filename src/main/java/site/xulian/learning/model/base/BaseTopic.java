package site.xulian.learning.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseTopic<M extends BaseTopic<M>> extends Model<M> implements IBean {

	public void setId(java.lang.Integer id) {
		set("id", id);
	}

	public java.lang.Integer getId() {
		return get("id");
	}

	public void setTitle(java.lang.String title) {
		set("title", title);
	}

	public java.lang.String getTitle() {
		return get("title");
	}

	public void setContent(java.lang.String content) {
		set("content", content);
	}

	public java.lang.String getContent() {
		return get("content");
	}

	public void setNumber(java.lang.Integer number) {
		set("number", number);
	}

	public java.lang.Integer getNumber() {
		return get("number");
	}

	public void setWeight(java.lang.Integer weight) {
		set("weight", weight);
	}

	public java.lang.Integer getWeight() {
		return get("weight");
	}

	public void setStatus(java.lang.Integer status) {
		set("status", status);
	}

	public java.lang.Integer getStatus() {
		return get("status");
	}

	public void setStudentId(java.lang.String studentId) {
		set("student_id", studentId);
	}

	public java.lang.String getStudentId() {
		return get("student_id");
	}

	public void setStudentName(java.lang.String studentName) {
		set("student_name", studentName);
	}

	public java.lang.String getStudentName() {
		return get("student_name");
	}

	public void setCreateTime(java.util.Date createTime) {
		set("create_time", createTime);
	}

	public java.util.Date getCreateTime() {
		return get("create_time");
	}

	public void setUpdateTime(java.util.Date updateTime) {
		set("update_time", updateTime);
	}

	public java.util.Date getUpdateTime() {
		return get("update_time");
	}

}
