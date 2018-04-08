package com.demo.graduationuserapp.web;

import com.demo.core.simplification.BranchFilter;
import com.demo.core.simplification.Trimmer;
import com.demo.core.web.BaseController;
import com.demo.core.web.ResponseEntity;
import com.demo.domain.usr.Corporation;
import com.demo.domain.usr.User;
import com.demo.graduationclient.usr.CorporationResourceClient;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/corporation")
public class CorporationController extends BaseController{
    @Autowired
    private CorporationResourceClient corporationResourceClient;

    @RequestMapping("/get")
    public ResponseEntity<Corporation> get(String id){
        return getTrimmer(corporationResourceClient.get(id).getData());
    }

    @RequestMapping("/list")
    public ResponseEntity<List<Corporation>> list(Corporation corporation){
        return getTrimmer(corporationResourceClient.list(corporation).getData());
    }

    @RequestMapping("/query")
    public ResponseEntity<PageInfo<Corporation>> query(Corporation corporation){
        BranchFilter userFilter = BranchFilter.newBranchFilter(Corporation.class);
        userFilter.ignoreSystemFields();
        Trimmer trimmer = Trimmer.newTrimmer(corporationResourceClient.query(corporation).getData());
        trimmer.addBranchFilters(userFilter);
        return success(trimmer.trim());
    }

    @PostMapping("/save")
    public ResponseEntity<Corporation> save(@RequestBody Corporation corporation){
        if(corporation.getCreateddate()==null){
            corporation.setCreateddate(new Date());
        }
        return getTrimmer(corporationResourceClient.save(corporation).getData());
    }

    @RequestMapping("/myCorporation")
    public ResponseEntity<List<Map>> myCorporation(User user){
        return success(corporationResourceClient.myCorporation(user).getData());
    }


    private  ResponseEntity<Corporation> getTrimmer(Corporation corporation){
        BranchFilter userFilter = BranchFilter.newBranchFilter(Corporation.class);
        userFilter.ignoreSystemFields();
        Trimmer trimmer = Trimmer.newTrimmer(corporation);
        trimmer.addBranchFilters(userFilter);
        return success(trimmer.trim());
    }

    private ResponseEntity<List<Corporation>> getTrimmer(List<Corporation> colleges){
        BranchFilter collegeFilter = BranchFilter.newBranchFilter(Corporation.class);
        collegeFilter.ignoreSystemFields();
        Trimmer trimmer = Trimmer.newTrimmer(colleges);
        trimmer.addBranchFilters(collegeFilter);
        return success(trimmer.trim());
    }
}
