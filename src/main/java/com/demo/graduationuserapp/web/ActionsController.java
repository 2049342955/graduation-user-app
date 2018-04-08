package com.demo.graduationuserapp.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.demo.core.simplification.BranchFilter;
import com.demo.core.simplification.Trimmer;
import com.demo.core.web.BaseController;
import com.demo.core.web.ResponseEntity;
import com.demo.domain.usr.*;
import com.demo.graduationclient.usr.*;
import com.demo.utils.BooleanObject;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/action")
public class ActionsController extends BaseController{
    @Autowired
    private ActionResourceClient actionResourceClient;

    @Autowired
    private ActionApproveResourceClient actionApproveResourceClient;

    @Autowired
    private ActionPictureResourceClient actionPictureResourceClient;

    @Autowired
    private MessageResourceClient messageResourceClient;

    @Autowired
    private PopularActionResourceClient popularActionResourceClient;


    @RequestMapping("/get")
    public ResponseEntity<Actions> get(String id){
        return getTrimmer(actionResourceClient.get(id).getData());
    }

    @RequestMapping("/selectOne")
    public ResponseEntity<Actions> selectOne(Actions actions){
        return getTrimmer(actionResourceClient.selectOne(actions).getData());
    }

    @RequestMapping("/list")
    public ResponseEntity<List<Actions>> list(Actions actions){
        BranchFilter userFilter = BranchFilter.newBranchFilter(Actions.class);
        userFilter.ignoreSystemFields();
        Trimmer trimmer = Trimmer.newTrimmer(actionResourceClient.list(actions).getData());
        trimmer.addBranchFilters(userFilter);
        return success(trimmer.trim());
    }

    @RequestMapping("/query")
    public ResponseEntity<PageInfo<Actions>> query(Actions actions){
        BranchFilter userFilter = BranchFilter.newBranchFilter(Actions.class);
        userFilter.ignoreSystemFields();
        Trimmer trimmer = Trimmer.newTrimmer(actionResourceClient.query(actions).getData());
        trimmer.addBranchFilters(userFilter);
        return success(trimmer.trim());
    }

    @PostMapping("/save")
    public ResponseEntity<Actions> save(@RequestBody Actions actions){
        ActionPictures actionPictures = new ActionPictures();
        actionPictures.setActionId("-1");
        actions = actionResourceClient.save(actions).getData();
        List<ActionPictures> actionPic = actionPictureResourceClient.list(actionPictures).getData();
        if(actionPic!=null && actionPic.size()>0){
            for(ActionPictures ap:actionPic){
                ap.setActionId(actions.getId());
                actionPictureResourceClient.save(ap);
            }
        }
        return getTrimmer(actions);
    }

    @GetMapping("/delete")
    public ResponseEntity<BooleanObject> delete(String id){
        BranchFilter collegeFilter = BranchFilter.newBranchFilter(Actions.class);
        collegeFilter.ignoreSystemFields();
        Trimmer trimmer = Trimmer.newTrimmer(actionResourceClient.delete(id));
        trimmer.addBranchFilters(collegeFilter);
        return success(trimmer.trim());
    }

    @RequestMapping("/approveList")
    public ResponseEntity<List<Actions>> approveList(Actions actions){
        List<Actions> actionSub = actionResourceClient.approveList(actions).getData().subList(0,4);
        ResponseEntity responseEntity = new ResponseEntity();
        responseEntity.setData(actionSub);
        responseEntity.setCode("success");
        JSONArray jsonArray = JSONObject.parseArray(JSONObject.toJSONString(responseEntity.getData()));
        JSONArray result = new JSONArray();
        if (jsonArray != null && !jsonArray.isEmpty()) {
            for (Object e : jsonArray) {
                JSONObject sourceData = JSON.parseObject(JSONObject.toJSONString(e));
                ActionPictures  actionPictures = new ActionPictures();
                actionPictures.setActionId(sourceData.getString("id"));
                List<ActionPictures>  aPic = actionPictureResourceClient.list(actionPictures).getData();
                if(aPic!=null && aPic.size()>0){
                    sourceData.put("pictures",aPic);
                }
                result.add(sourceData);
            }
        }
        responseEntity.setData(result);
        return responseEntity;
    }

    @RequestMapping("/popularAction")
    public ResponseEntity<List<Actions>> popularAction(Actions actions){
        List<Actions> actionSub = actionResourceClient.popularAction(actions).getData();
        ResponseEntity responseEntity = new ResponseEntity();
        responseEntity.setData(actionSub);
        responseEntity.setCode("success");
        JSONArray jsonArray = JSONObject.parseArray(JSONObject.toJSONString(responseEntity.getData()));
        JSONArray result = new JSONArray();
        if (jsonArray != null && !jsonArray.isEmpty()) {
            for (Object e : jsonArray) {
                JSONObject sourceData = JSON.parseObject(JSONObject.toJSONString(e));
                ActionPictures  actionPictures = new ActionPictures();
                actionPictures.setActionId(sourceData.getString("id"));
                List<ActionPictures>  aPic = actionPictureResourceClient.list(actionPictures).getData();
                if(aPic!=null && aPic.size()>0){
                    sourceData.put("pictures",aPic);
                }
                result.add(sourceData);
            }
        }
        responseEntity.setData(result);
        return responseEntity;
    }

    @RequestMapping("/rejectAction")
    public ResponseEntity<List<Map>> rejectAction(Corporation corporation){
        return success(actionResourceClient.rejectAction(corporation).getData());
    }

    @RequestMapping("/getByCorporation")
    public ResponseEntity<List<Actions>> getByCorporation(Corporation corporation){
        List<Actions> actionSub = actionResourceClient.getByCorporation(corporation).getData();
        ResponseEntity responseEntity = new ResponseEntity();
        responseEntity.setData(actionSub);
        responseEntity.setCode("success");
        JSONArray jsonArray = JSONObject.parseArray(JSONObject.toJSONString(responseEntity.getData()));
        JSONArray result = new JSONArray();
        if (jsonArray != null && !jsonArray.isEmpty()) {
            for (Object e : jsonArray) {
                JSONObject sourceData = JSON.parseObject(JSONObject.toJSONString(e));
                ActionPictures  actionPictures = new ActionPictures();
                actionPictures.setActionId(sourceData.getString("id"));
                List<ActionPictures>  aPic = actionPictureResourceClient.list(actionPictures).getData();
                if(aPic!=null && aPic.size()>0){
                    sourceData.put("pictures",aPic);
                }
                result.add(sourceData);
            }
        }
        responseEntity.setData(result);
        return responseEntity;
    }

    @RequestMapping("/getAllApprove")
    public ResponseEntity<List<Actions>> getAllApprove(){
        ResponseEntity responseEntity =actionResourceClient.getAllApprove();
        JSONArray jsonArray = JSONObject.parseArray(JSONObject.toJSONString(responseEntity.getData()));
        JSONArray result = new JSONArray();
        if (jsonArray != null && !jsonArray.isEmpty()) {
            for (Object e : jsonArray) {
                JSONObject sourceData = JSON.parseObject(JSONObject.toJSONString(e));
                ActionPictures  actionPictures = new ActionPictures();
                actionPictures.setActionId(sourceData.getString("id"));
                List<ActionPictures>  aPic = actionPictureResourceClient.list(actionPictures).getData();
                if(aPic!=null && aPic.size()>0){
                    sourceData.put("pictures",aPic);
                }
                result.add(sourceData);
            }
        }
        responseEntity.setData(result);
        return responseEntity;
    }

    @PostMapping("/actionApprove")
    public ResponseEntity<ActionApprove> actionApprove(@RequestBody ActionApprove actionApprove) throws Exception {
        ActionApprove newActionApprove = new ActionApprove();
        newActionApprove.setAid(actionApprove.getAid());
        newActionApprove = actionApproveResourceClient.selectOne(newActionApprove).getData();
        newActionApprove.setStatus(actionApprove.getStatus());
        if(!StringUtil.isEmpty(actionApprove.getDescription())){
            newActionApprove.setDescription(actionApprove.getDescription());
        }

        if(newActionApprove!=null && !StringUtil.isEmpty(newActionApprove.getId())){
            newActionApprove.setApproveDate(new Date());
            newActionApprove = actionApproveResourceClient.save(newActionApprove).getData();
            Actions actions = new Actions();
            actions.setId(newActionApprove.getAid());
            actions = actionResourceClient.selectOne(actions).getData();
            Messages messages = new Messages();
            messages.setName(newActionApprove.getAid());
            if("1".equals(newActionApprove.getStatus())){
                messages.setMessage(actions.getActionName()+"已经通过审核");
            }else{
                messages.setMessage(actions.getActionName()+"未通过审核");
            }
            messages.setDate(new Date());
            messages.setStatus("0");
            messageResourceClient.save(messages);
            return getTrimmer(newActionApprove);
        }else{
            throw new Exception("id不能为null");
        }
    }

    @RequestMapping("/recentList")
    public ResponseEntity<List<Actions>> recentList(Actions actions){
        BranchFilter userFilter = BranchFilter.newBranchFilter(Actions.class);
        userFilter.ignoreSystemFields();
        List<Actions> actionSub = actionResourceClient.approveList(actions).getData();
        if (actionSub.size() > 6) {
            actionSub = actionSub.subList(0,6);
        }
        Trimmer trimmer = Trimmer.newTrimmer(actionSub);
        trimmer.addBranchFilters(userFilter);
        return success(trimmer.trim());
    }

    @GetMapping("/popularSave")
    public ResponseEntity<PopularActions> popularSave(String actionId){
        PopularActions popularActions = new PopularActions();
        popularActions.setAid(actionId);
        popularActions.setStartDate(new Date());
        popularActions.setSortSeq(20);
        return success(popularActionResourceClient.save(popularActions).getData());
    }


    private  ResponseEntity<Actions> getTrimmer(Actions actions){
        BranchFilter userFilter = BranchFilter.newBranchFilter(Actions.class);
        userFilter.ignoreSystemFields();
        Trimmer trimmer = Trimmer.newTrimmer(actions);
        trimmer.addBranchFilters(userFilter);
        return success(trimmer.trim());
    }

    private  ResponseEntity<ActionApprove> getTrimmer(ActionApprove actionApprove){
        BranchFilter userFilter = BranchFilter.newBranchFilter(ActionApprove.class);
        userFilter.ignoreSystemFields();
        Trimmer trimmer = Trimmer.newTrimmer(actionApprove);
        trimmer.addBranchFilters(userFilter);
        return success(trimmer.trim());
    }
}
