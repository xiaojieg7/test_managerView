package com.property.controller;

import com.property.entity.ApiResponse;
import com.property.entity.Notice;
import com.property.entity.PageResult;
import com.property.entity.security.UserDetailsImpl;
import com.property.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notice")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @GetMapping
    public ApiResponse<PageResult<Notice>> getNoticePage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String keyword) {
        PageResult<Notice> result = noticeService.getNoticePage(pageNum, pageSize, type, keyword);
        return ApiResponse.success(result);
    }

    @GetMapping("/{id}")
    public ApiResponse<Notice> getNoticeById(@PathVariable Long id) {
        Notice notice = noticeService.getNoticeById(id);
        return ApiResponse.success(notice);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Notice> createNotice(@RequestBody Notice notice,
                                             @AuthenticationPrincipal UserDetailsImpl userDetails) {
        notice.setPublisherId(userDetails.getUser().getId());
        Notice result = noticeService.createNotice(notice);
        return ApiResponse.success("发布成功", result);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Notice> updateNotice(@PathVariable Long id, @RequestBody Notice notice) {
        notice.setId(id);
        Notice result = noticeService.updateNotice(notice);
        return ApiResponse.success("更新成功", result);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> deleteNotice(@PathVariable Long id) {
        noticeService.deleteNotice(id);
        return ApiResponse.success("删除成功", null);
    }
}
