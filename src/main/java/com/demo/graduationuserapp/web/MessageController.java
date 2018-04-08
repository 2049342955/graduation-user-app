package com.demo.graduationuserapp.web;

import com.demo.core.simplification.BranchFilter;
import com.demo.core.simplification.Trimmer;
import com.demo.core.web.BaseController;
import com.demo.core.web.ResponseEntity;
import com.demo.domain.usr.Messages;
import com.demo.graduationclient.usr.MessageResourceClient;
import com.demo.utils.BooleanObject;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/message")
public class MessageController extends BaseController{
    @Autowired
    private MessageResourceClient messageResourceClient;

    @RequestMapping("/get")
    public ResponseEntity<Messages> get(String id){
        return getTrimmer(messageResourceClient.get(id).getData());
    }

    @RequestMapping("/selectOne")
    public ResponseEntity<Messages> selectOne(Messages messages){
        return getTrimmer(messageResourceClient.selectOne(messages).getData());
    }

    @RequestMapping("/list")
    public ResponseEntity<List<Messages>> list(Messages messages){
        BranchFilter noticeFilter = BranchFilter.newBranchFilter(Messages.class);
        noticeFilter.ignoreSystemFields();
        Trimmer trimmer = Trimmer.newTrimmer(messageResourceClient.list(messages).getData());
        trimmer.addBranchFilters(noticeFilter);
        return success(trimmer.trim());
    }

    @RequestMapping("/query")
    public ResponseEntity<PageInfo<Messages>> query(Messages messages){
        BranchFilter noticeFilter = BranchFilter.newBranchFilter(Messages.class);
        noticeFilter.ignoreSystemFields();
        Trimmer trimmer = Trimmer.newTrimmer(messageResourceClient.query(messages).getData());
        trimmer.addBranchFilters(noticeFilter);
        return success(trimmer.trim());
    }

    @PostMapping("/save")
    public ResponseEntity<Messages> save(@RequestBody Messages messages){
        if(messages.getDate()==null){
            messages.setDate(new Date());
        }
        return getTrimmer(messageResourceClient.save(messages).getData());
    }

    @GetMapping("/delete")
    public ResponseEntity<BooleanObject> delete(String id){
        BranchFilter noticeFilter = BranchFilter.newBranchFilter(Messages.class);
        noticeFilter.ignoreSystemFields();
        Trimmer trimmer = Trimmer.newTrimmer(messageResourceClient.delete(id));
        trimmer.addBranchFilters(noticeFilter);
        return success(trimmer.trim());
    }

    @RequestMapping("/getByName")
    public ResponseEntity<List<Messages>> getByName(String userId){
        BranchFilter noticeFilter = BranchFilter.newBranchFilter(Messages.class);
        noticeFilter.ignoreSystemFields();
        Trimmer trimmer = Trimmer.newTrimmer(messageResourceClient.getByName(userId).getData());
        trimmer.addBranchFilters(noticeFilter);
        return success(trimmer.trim());
    }

    @RequestMapping("/getByAction")
    public ResponseEntity<List<Messages>> getByAction(String userId){
        BranchFilter noticeFilter = BranchFilter.newBranchFilter(Messages.class);
        noticeFilter.ignoreSystemFields();
        Trimmer trimmer = Trimmer.newTrimmer(messageResourceClient.getByAction(userId).getData());
        trimmer.addBranchFilters(noticeFilter);
        return success(trimmer.trim());
    }

    private  ResponseEntity<Messages> getTrimmer(Messages Messages){
        BranchFilter NoticeFilter = BranchFilter.newBranchFilter(Messages.class);
        NoticeFilter.ignoreSystemFields();
        Trimmer trimmer = Trimmer.newTrimmer(Messages);
        trimmer.addBranchFilters(NoticeFilter);
        return success(trimmer.trim());
    }
}
