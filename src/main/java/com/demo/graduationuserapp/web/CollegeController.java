package com.demo.graduationuserapp.web;

import com.demo.core.simplification.BranchFilter;
import com.demo.core.simplification.Trimmer;
import com.demo.core.web.BaseController;
import com.demo.core.web.ResponseEntity;
import com.demo.domain.usr.College;
import com.demo.graduationclient.usr.CollegeResourceClient;
import com.demo.graduationclient.usr.MajorResourceClient;
import com.demo.utils.BooleanObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

//@SuppressWarnings("ALL")
@RestController
@RequestMapping("/college")
public class CollegeController extends BaseController{
    @Resource
    private CollegeResourceClient collegeResourceClient;

//    @Autowired
//    private MajorResourceClient majorResourceClient;

    @RequestMapping(value = "/get",method = GET)
    public ResponseEntity<College> get(String id){
        return getTrimmer(collegeResourceClient.get(id).getData());
    }

    @RequestMapping(value = "/selectOne",method = GET)
    public ResponseEntity<College> selectOne(College college){
        return getTrimmer(collegeResourceClient.selectOne(college).getData());
    }

    @RequestMapping(value = "/list",method = GET)
    public ResponseEntity<List<College>> list(College college){
        return getTrimmer(collegeResourceClient.list(college).getData());
    }

    @RequestMapping(value = "/save",method = POST)
    public ResponseEntity<College> save(College college){
        return getTrimmer(collegeResourceClient.save(college).getData());
    }

    @RequestMapping(value = "/delete",method =GET )
    public ResponseEntity<BooleanObject> delete(String id){
        return getTrimmer(collegeResourceClient.delete(id).getData());
    }

    private ResponseEntity getTrimmer(College college){
        BranchFilter collegeFilter = BranchFilter.newBranchFilter(College.class);
        collegeFilter.ignoreSystemFields();
        Trimmer trimmer = Trimmer.newTrimmer(college);
        trimmer.addBranchFilters(collegeFilter);
        return success(trimmer.trim());
    }

    private ResponseEntity<List<College>> getTrimmer(List<College> colleges){
        BranchFilter collegeFilter = BranchFilter.newBranchFilter(College.class);
        collegeFilter.ignoreSystemFields();
        Trimmer trimmer = Trimmer.newTrimmer(colleges);
        trimmer.addBranchFilters(collegeFilter);
        return success(trimmer.trim());
    }

    private ResponseEntity<BooleanObject> getTrimmer(BooleanObject booleanObject){
        BranchFilter collegeFilter = BranchFilter.newBranchFilter(College.class);
        collegeFilter.ignoreSystemFields();
        Trimmer trimmer = Trimmer.newTrimmer(booleanObject);
        trimmer.addBranchFilters(collegeFilter);
        return success(trimmer.trim());
    }
}
