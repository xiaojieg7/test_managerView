package com.property.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.property.entity.Notice;
import com.property.entity.PageResult;
import com.property.mapper.NoticeMapper;
import com.property.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class NoticeServiceImpl implements NoticeService {

    @Autowired
    private NoticeMapper noticeMapper;

    @Override
    public PageResult<Notice> getNoticePage(Integer pageNum, Integer pageSize, String type, String keyword) {
        Page<Notice> page = new Page<>(pageNum, pageSize);
        IPage<Notice> result = noticeMapper.selectNoticePage(page, type, keyword);
        return new PageResult<>(result.getTotal(), result.getRecords());
    }

    @Override
    public Notice getNoticeById(Long id) {
        return noticeMapper.selectById(id);
    }

    @Override
    @Transactional
    public Notice createNotice(Notice notice) {
        notice.setStatus("PUBLISHED");
        notice.setPublishTime(LocalDateTime.now());
        if (notice.getPriority() == null) {
            notice.setPriority(0);
        }
        noticeMapper.insert(notice);
        return notice;
    }

    @Override
    @Transactional
    public Notice updateNotice(Notice notice) {
        noticeMapper.updateById(notice);
        return notice;
    }

    @Override
    @Transactional
    public void deleteNotice(Long id) {
        noticeMapper.deleteById(id);
    }
}
