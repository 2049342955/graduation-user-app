package com.demo.graduationuserapp.web;

import com.demo.core.web.BaseController;
import com.demo.core.web.ResponseEntity;
import com.demo.domain.usr.*;
import com.demo.graduationclient.usr.CollegeResourceClient;
import com.demo.graduationclient.usr.MajorResourceClient;
import com.demo.graduationclient.usr.UserRescourceClient;
import com.demo.graduationclient.usr.UserRoleResourceClient;
import com.github.pagehelper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/userRole")
public class UserRolesController extends BaseController{
    @Autowired
    private UserRoleResourceClient userRoleResourceClient;

    @Autowired
    private UserRescourceClient userRescourceClient;

    @Resource
    private CollegeResourceClient collegeResourceClient;

    @Autowired
    private MajorResourceClient majorResourceClient;

    @PostMapping("/save")
    public ResponseEntity<UserRoles> save(@RequestBody UserRoles userRoles) throws Exception {
        UserRoles copy = new UserRoles();
        copy.setUserId(userRoles.getUserId());
        copy.setRoleId(userRoles.getRoleId());
        copy.setCid(userRoles.getCid());
        userRoles = userRoleResourceClient.selectOne(copy).getData();
        userRoles.setStatus("N");
        if(userRoles!=null && StringUtil.isEmpty(userRoles.getId())){
            throw new Exception("数据内部错误");
        }
        return success(userRoleResourceClient.save(userRoles).getData());
    }

    @PostMapping("/insertUserRole")
    public ResponseEntity<List<Map>> insertUserRole(@RequestBody UserRoles userRoles) throws Exception {
        if(StringUtil.isEmpty(userRoles.getUserId())){
            throw new Exception("用户id不能为null");
        }
        if(StringUtil.isEmpty(userRoles.getCid())){
            throw new Exception("cid不能为null");
        }
        userRoles.setRoleId("f6c0c106329111e8920354a050ae6420");
        userRoles.setStatus("Y");
        userRoleResourceClient.save(userRoles);
        Corporation corporation = new Corporation();
        corporation.setId(userRoles.getCid());
        List<Map> maps = userRescourceClient.getByCorporation(corporation).getData();
        for(Map map:maps){
            if(map.get("college")!=null && map.get("college")!=""){
                College college = collegeResourceClient.get((String)map.get("college")).getData();
                if(college!=null && !StringUtil.isEmpty(college.getName())){
                    map.put("collegeName",college.getName());
                }
            }
            if(map.get("major")!=null && map.get("major")!=""){
                Major major = majorResourceClient.get((String)map.get("major")).getData();
                if(major!=null && !StringUtil.isEmpty(major.getName())){
                    map.put("majorName",major.getName());
                }
            }
        }
        return success(maps);
    }
}
