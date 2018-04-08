package com.demo.graduationuserapp.web;

import com.demo.core.simplification.BranchFilter;
import com.demo.core.simplification.Trimmer;
import com.demo.core.web.BaseController;
import com.demo.core.web.ResponseEntity;
import com.demo.domain.usr.Notice;
import com.demo.graduationclient.usr.NoticeResourceClient;
import com.demo.utils.BooleanObject;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/notice")
public class NoticeController extends BaseController{
    @Autowired
    private NoticeResourceClient noticeResourceClient;

    @RequestMapping("/get")
    public ResponseEntity<Notice> get(String id){
        return getTrimmer(noticeResourceClient.get(id).getData());
    }

    @RequestMapping("/selectOne")
    public ResponseEntity<Notice> selectOne(Notice notice){
        return getTrimmer(noticeResourceClient.selectOne(notice).getData());
    }

    @RequestMapping("/list")
    public ResponseEntity<List<Notice>> list(Notice notice){
        BranchFilter noticeFilter = BranchFilter.newBranchFilter(Notice.class);
        noticeFilter.ignoreSystemFields();
        Trimmer trimmer = Trimmer.newTrimmer(noticeResourceClient.list(notice).getData());
        trimmer.addBranchFilters(noticeFilter);
        return success(trimmer.trim());
    }

    @RequestMapping("/query")
    public ResponseEntity<PageInfo<Notice>> query(Notice notice){
        BranchFilter noticeFilter = BranchFilter.newBranchFilter(Notice.class);
        noticeFilter.ignoreSystemFields();
        Trimmer trimmer = Trimmer.newTrimmer(noticeResourceClient.query(notice).getData());
        trimmer.addBranchFilters(noticeFilter);
        return success(trimmer.trim());
    }

    @PostMapping("/save")
    public ResponseEntity<Notice> save(@RequestBody Notice notice){
        if(notice.getDate()==null){
            notice.setDate(new Date());
        }
        return getTrimmer(noticeResourceClient.save(notice).getData());
    }

    @GetMapping("/delete")
    public ResponseEntity<BooleanObject> delete(String id){
        BranchFilter noticeFilter = BranchFilter.newBranchFilter(Notice.class);
        noticeFilter.ignoreSystemFields();
        Trimmer trimmer = Trimmer.newTrimmer(noticeResourceClient.delete(id));
        trimmer.addBranchFilters(noticeFilter);
        return success(trimmer.trim());
    }

    private  ResponseEntity<Notice> getTrimmer(Notice Notice){
        BranchFilter NoticeFilter = BranchFilter.newBranchFilter(Notice.class);
        NoticeFilter.ignoreSystemFields();
        Trimmer trimmer = Trimmer.newTrimmer(Notice);
        trimmer.addBranchFilters(NoticeFilter);
        return success(trimmer.trim());
    }


}
