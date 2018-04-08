package com.demo.graduationuserapp.web;

import com.demo.core.simplification.BranchFilter;
import com.demo.core.simplification.Trimmer;
import com.demo.core.web.BaseController;
import com.demo.core.web.ResponseEntity;
import com.demo.domain.usr.ChairmanApprove;
import com.demo.domain.usr.Messages;
import com.demo.domain.usr.User;
import com.demo.graduationclient.usr.ChairmanApproveResourceClient;
import com.demo.graduationclient.usr.MessageResourceClient;
import com.demo.graduationclient.usr.UserRescourceClient;
import com.demo.utils.BooleanObject;
import com.github.pagehelper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/chairmanApprove")
public class ChairmanApproveController extends BaseController{
    @Autowired
    private ChairmanApproveResourceClient chairmanApproveResourceClient;

    @Autowired
    private MessageResourceClient messageResourceClient;

    @Autowired
    private UserRescourceClient userRescourceClient;

//    @RequestMapping(value = "/get",method = GET)
//    public ResponseEntity<ChairmanApprove> get(String id){
//        return getTrimmer(chairmanApproveResourceClient.get(id).getData());
//    }
//
//    @RequestMapping(value = "/selectOne",method = GET)
//    public ResponseEntity<ChairmanApprove> selectOne(ChairmanApprove college){
//        return getTrimmer(chairmanApproveResourceClient.selectOne(college).getData());
//    }
//
//    @RequestMapping(value = "/list",method = GET)
//    public ResponseEntity<List<ChairmanApprove>> list(ChairmanApprove college){
//        return getTrimmer(chairmanApproveResourceClient.list(college).getData());
//    }

    @RequestMapping(value = "/save",method = POST)
    public ResponseEntity<ChairmanApprove> save(@RequestBody ChairmanApprove chairmanApprove){
        return getTrimmer(chairmanApproveResourceClient.save(chairmanApprove).getData());
    }

    @RequestMapping(value = "/approve",method = POST)
    public ResponseEntity<ChairmanApprove> approve(@RequestBody ChairmanApprove chairmanApprove) throws Exception {
        if(StringUtil.isEmpty(chairmanApprove.getId())){
            throw new Exception("id不能为null");
        }
        if(chairmanApprove.getStatus() ==null){
            throw  new Exception("status不能为null");
        }
        if(StringUtil.isEmpty(chairmanApprove.getUid())){
            throw  new Exception("uid不能为null");
        }
        chairmanApprove=chairmanApproveResourceClient.approve(chairmanApprove).getData();
        User user = new User();
        user = userRescourceClient.get(chairmanApprove.getUid()).getData();
        Messages messages = new Messages();
        messages.setName(chairmanApprove.getUid());
        messages.setDate(new Date());
        messages.setStatus("0");
        if(1==chairmanApprove.getStatus()){
            messages.setMessage("恭喜"+user.getName()+"已通过审核");
        }
        else{
            messages.setMessage(user.getName()+"未通过审核");
        }
        messageResourceClient.save(messages);
        return getTrimmer(chairmanApprove);
    }

//    @RequestMapping(value = "/delete",method =GET )
//    public ResponseEntity<BooleanObject> delete(String id){
//        return getTrimmer(chairmanApproveResourceClient.delete(id).getData());
//    }

    private ResponseEntity getTrimmer(ChairmanApprove college){
        BranchFilter collegeFilter = BranchFilter.newBranchFilter(ChairmanApprove.class);
        collegeFilter.ignoreSystemFields();
        Trimmer trimmer = Trimmer.newTrimmer(college);
        trimmer.addBranchFilters(collegeFilter);
        return success(trimmer.trim());
    }

    private ResponseEntity<List<ChairmanApprove>> getTrimmer(List<ChairmanApprove> colleges){
        BranchFilter collegeFilter = BranchFilter.newBranchFilter(ChairmanApprove.class);
        collegeFilter.ignoreSystemFields();
        Trimmer trimmer = Trimmer.newTrimmer(colleges);
        trimmer.addBranchFilters(collegeFilter);
        return success(trimmer.trim());
    }

    private ResponseEntity<BooleanObject> getTrimmer(BooleanObject booleanObject) {
        BranchFilter collegeFilter = BranchFilter.newBranchFilter(ChairmanApprove.class);
        collegeFilter.ignoreSystemFields();
        Trimmer trimmer = Trimmer.newTrimmer(booleanObject);
        trimmer.addBranchFilters(collegeFilter);
        return success(trimmer.trim());
    }
}
