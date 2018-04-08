package com.demo.graduationuserapp.web;

import com.demo.core.simplification.BranchFilter;
import com.demo.core.simplification.Trimmer;
import com.demo.core.web.BaseController;
import com.demo.core.web.ResponseEntity;
import com.demo.domain.usr.Major;
import com.demo.graduationclient.usr.MajorResourceClient;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/major")
public class MajorController extends BaseController{
    @Autowired
    private MajorResourceClient majorResourceClient;

    @RequestMapping("/get")
    public ResponseEntity<Major> get(String id){
        return getTrimmer(majorResourceClient.get(id).getData());
    }

    @RequestMapping("/list")
    public ResponseEntity<List<Major>> list(Major major){
//        BranchFilter userFilter = BranchFilter.newBranchFilter(Major.class);
//        userFilter.ignoreSystemFields();
//        Trimmer trimmer = Trimmer.newTrimmer(majorResourceClient.list(major).getData());
//        trimmer.addBranchFilters(userFilter);
//        return success(trimmer.trim());
        return getTrimmer(majorResourceClient.list(major).getData());
    }

    @RequestMapping("/query")
    public ResponseEntity<PageInfo<Major>> query(Major major){
        BranchFilter userFilter = BranchFilter.newBranchFilter(Major.class);
        userFilter.ignoreSystemFields();
        Trimmer trimmer = Trimmer.newTrimmer(majorResourceClient.query(major).getData());
        trimmer.addBranchFilters(userFilter);
        return success(trimmer.trim());
    }


    private  ResponseEntity<Major> getTrimmer(Major major){
        BranchFilter userFilter = BranchFilter.newBranchFilter(Major.class);
        userFilter.ignoreSystemFields();
        Trimmer trimmer = Trimmer.newTrimmer(major);
        trimmer.addBranchFilters(userFilter);
        return success(trimmer.trim());
    }

    private ResponseEntity<List<Major>> getTrimmer(List<Major> colleges){
        BranchFilter collegeFilter = BranchFilter.newBranchFilter(Major.class);
        collegeFilter.ignoreSystemFields();
        Trimmer trimmer = Trimmer.newTrimmer(colleges);
        trimmer.addBranchFilters(collegeFilter);
        return success(trimmer.trim());
    }
}
