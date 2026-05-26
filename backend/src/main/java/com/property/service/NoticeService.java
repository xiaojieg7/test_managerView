package com.property.service;

import com.property.entity.Notice;
import com.property.entity.PageResult;

public interface NoticeService {
    PageResult<Notice> getNoticePage(Integer pageNum, Integer pageSize, String type, String keyword);
    Notice getNoticeById(Long id);
    Notice createNotice(Notice notice);
    Notice updateNotice(Notice notice);
    void deleteNotice(Long id);
}
