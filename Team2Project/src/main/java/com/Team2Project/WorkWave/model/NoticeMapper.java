package com.Team2Project.WorkWave.model;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NoticeMapper {
	
	List<NoticeDTO> noticeList(Page pdto);
	
	int countNotices();
	
	NoticeDTO noticeCont(int no);
	
}
