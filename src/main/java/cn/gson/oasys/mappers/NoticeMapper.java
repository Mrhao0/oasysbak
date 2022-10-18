package cn.gson.oasys.mappers;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface NoticeMapper {
	
	//默认根据置顶、修改时间排序
	List<Map<String, Object>> findMyNotice(@Param("userId") Long userId);

	//与上面一直，限制条数为5条
	List<Map<String, Object>> findMyNoticeLimit(@Param("userId") Long userId);
	
	//进行逻辑判断，来根据那个排序，类型、状态、修改时间
	List<Map<String, Object>> sortMyNotice(@Param("userId") Long userId,@Param("baseKey") String baseKey,@Param("type") Integer type,@Param("status") Integer status,@Param("time") Integer time);

	//当前用户通知
	List<Map<String, Object>> findMyNotices1(@Param("userId") Long userId);
	//管理员通知
	List<Map<String, Object>> findMyNotices2();
}
