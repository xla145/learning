package site.xulian.learning.quartz;

import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Record;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import site.xulian.learning.model.Answer;
import site.xulian.learning.model.Records;
import site.xulian.learning.utils.Common;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 每天定时统计资讯的点击量
 * */
public class RecordStatisticsJob implements Job{
	Logger log = Logger.getLogger(this.getClass());
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.info("每天定时统计资讯的点击量开始。。。。。。。。。。。。。。");
	    final List<Record> topicList = Answer.dao.getAnswer();
        System.out.println(JsonKit.toJson(topicList));
        if(topicList.isEmpty()) {
	    	log.error("获取统计询问次数，返回为空！");
	    	return;
	    }
		final List<Record> recordsList = Records.dao.getRecords();
        System.out.println(JsonKit.toJson(recordsList));
		if(recordsList.isEmpty()) {
			log.error("获取统计询问次数，返回为空！");
			return;
		}
        System.out.println("-----------------------------------");
        Db.tx(new IAtom() {
            @Override
            public boolean run() throws SQLException {
                try{
                    StringBuffer sql = new StringBuffer();
                    List<Object[]> params = new ArrayList<Object[]>();
                    sql.append("INSERT INTO record_statistics(student_id,student_name,sum_times,ask_times,create_time,update_time) VALUES(?,?,?,?,now(),now()) ON DUPLICATE KEY UPDATE ask_times=?,sum_times =?,update_time=now() ");
                    for (Record r:topicList) {
                        Object[] param = new Object[6];
                        param[0] = r.get("student_id");
                        param[1] = r.get("student_name");
                        param[2] = r.getBigDecimal("sum_times");
                        param[3] = r.getLong("ask_times");
                        param[4] = r.getBigDecimal("sum_times");
                        param[5] = r.getLong("ask_times");
                        params.add(param);
                    }
                    // 空数据判断
                    if(params.size() > 1){
                        Db.batch(sql.toString(),
                                Common.listTo2Array(params), params.size());
                    }
                    String sqls = "INSERT INTO record_statistics(student_id,student_name,create_times,create_times,create_time,update_time) VALUES(?,?,?,?,now(),now()) ON DUPLICATE KEY UPDATE create_times=?,reply_times =?,update_time=now()";
                    List<Object[]> paramss = new ArrayList<Object[]>();
                    for (Record r:recordsList) {
                        Object[] param = new Object[6];
                        param[0] = r.get("student_id");
                        param[1] = r.get("student_name");
                        param[2] = r.getLong("create_times");
                        param[3] = r.getLong("reply_times");
                        param[4] = r.getLong("create_times");
                        param[5] = r.getLong("reply_times");
                        paramss.add(param);
                    }
                    // 空数据判断
                    if(params.size() > 1){
                        Db.batch(sqls,
                                Common.listTo2Array(paramss), params.size());
                    }
                    Db.update("update record_statistics set sum_times = (create_times + reply_times + ask_times)");
                }catch (Exception e){
                    log.error(e.getMessage());
                    e.printStackTrace();
                    return false;
                }
                return true;
            }
        });
	    log.info("每天定时统计资讯的点击量结束。。。。。。。。。。。。。。");
	}
}
