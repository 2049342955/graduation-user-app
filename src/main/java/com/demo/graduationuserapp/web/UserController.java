package com.demo.graduationuserapp.web;

import com.alibaba.druid.sql.visitor.functions.Char;
import com.alibaba.fastjson.JSONObject;
import com.demo.core.simplification.BranchFilter;
import com.demo.core.simplification.Trimmer;
import com.demo.core.web.BaseController;
import com.demo.core.web.ResponseEntity;
import com.demo.domain.usr.*;
import com.demo.graduationclient.common.UploadToServerClient;
import com.demo.graduationclient.usr.*;
import com.demo.graduationuserapp.common.FileUploadUtil;
import com.demo.graduationuserapp.common.FileUtil;
import com.demo.graduationuserapp.common.PinYinUtil;
import com.demo.utils.BooleanObject;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.StringUtil;
import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apdplat.qa.SharedQuestionAnsweringSystem;
import org.apdplat.qa.model.CandidateAnswer;
import org.apdplat.qa.model.Question;
import org.apdplat.word.WordFrequencyStatistics;
import org.apdplat.word.WordSegmenter;
import org.apdplat.word.analysis.CosineTextSimilarity;
import org.apdplat.word.analysis.TextSimilarity;
import org.apdplat.word.lucene.ChineseWordAnalyzer;
import org.apdplat.word.segmentation.SegmentationAlgorithm;
import org.apdplat.word.segmentation.Word;
import org.apdplat.word.segmentation.WordRefiner;
import org.apdplat.word.tagging.AntonymTagging;
import org.apdplat.word.tagging.PartOfSpeechTagging;
import org.apdplat.word.tagging.PinyinTagging;
import org.apdplat.word.tagging.SynonymTagging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

//import com.demo.graduationuserapp.common.Sender;

//@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController extends BaseController{

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRescourceClient userRescourceClient;

    @Autowired
    private RoleResourceClient roleResourceClient;

    @Autowired
    private MenuResourceClient menuResourceClient;

    @Autowired
    private UserRoleResourceClient userRoleResourceClient;

    @Resource
    private CollegeResourceClient collegeResourceClient;

    @Autowired
    private MajorResourceClient majorResourceClient;

    @Autowired
    private UploadToServerClient uploadToServerClient;

    @Autowired
    private PinYinUtil pinYinUtil;

    @Autowired
    private ActionResourceClient actionResourceClient;

    @Autowired
    private ActionPictureResourceClient actionPictureResourceClient;

    @Autowired
    private HomeResourceClient homeResourceClient;

    @Autowired
    private CorporationResourceClient corporationResourceClient;


    @GetMapping("/login")
    public ResponseEntity<User> login(User user,HttpServletRequest request) throws Exception {
        user = userRescourceClient.selectOne(user).getData();
        if(StringUtils.isEmpty(user)){
            throw new Exception("登录失败");
        }
        ResponseEntity responseEntity = (ResponseEntity) getTrimmer(user);
        JSONObject data = JSONObject.parseObject(JSONObject.toJSONString(responseEntity.getData()));

        List<Menu> menus = menuResourceClient.getRoleMenus(user).getData();
        List<Role> roles = roleResourceClient.getRoles(user).getData();
        data.put("menus",menus);
        data.put("roles",roles);
        responseEntity.setData(data);
        request.getSession().setAttribute("user",user);
        return responseEntity;

    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user){
        user=userRescourceClient.save(user).getData();
        UserRoles userRoles = new UserRoles();
        userRoles.setStatus("0");
        userRoles.setUserId(user.getId());
        userRoles.setRoleId("f6c0c106329111e8920354a050ae6420");
        userRoleResourceClient.save(userRoles);
        return getTrimmer(user);
    }

    @RequestMapping("/get")
    public ResponseEntity<User> get(String id){
        return getTrimmer(userRescourceClient.get(id).getData());
    }

    @RequestMapping("/selectOne")
    public ResponseEntity<User> selectOne(User user){
        return getTrimmer(userRescourceClient.selectOne(user).getData());
    }

    @RequestMapping("/list")
    public ResponseEntity<List<User>> list(User user){
        BranchFilter userFilter = BranchFilter.newBranchFilter(User.class);
        userFilter.ignoreSystemFields();
        Trimmer trimmer = Trimmer.newTrimmer(userRescourceClient.list(user).getData());
        trimmer.addBranchFilters(userFilter);
        return success(trimmer.trim());
    }

    @RequestMapping("/query")
    public ResponseEntity<PageInfo<User>> query(User user){
        BranchFilter userFilter = BranchFilter.newBranchFilter(User.class);
        userFilter.ignoreSystemFields();
        Trimmer trimmer = Trimmer.newTrimmer(userRescourceClient.query(user).getData());
        trimmer.addBranchFilters(userFilter);
        return success(trimmer.trim());
    }

    @PostMapping("/save")
    public ResponseEntity<User> save(@RequestBody User user){
        return getTrimmer(userRescourceClient.save(user).getData());
    }

    @GetMapping("/delete")
    public ResponseEntity<BooleanObject> delete(String id){
        BranchFilter collegeFilter = BranchFilter.newBranchFilter(User.class);
        collegeFilter.ignoreSystemFields();
        Trimmer trimmer = Trimmer.newTrimmer(userRescourceClient.delete(id));
        trimmer.addBranchFilters(collegeFilter);
        return success(trimmer.trim());
    }

    @RequestMapping("/getByCorporation")
    public ResponseEntity<List<User>> getByCorporation(Corporation corporation) throws Exception {
        if(StringUtil.isEmpty(corporation.getId())){
            throw new Exception("id不能为空");
        }
        List<Map> maps =userRescourceClient.getByCorporation(corporation).getData();
        return success(setCollegeAndMajor(maps));
    }

    @RequestMapping("/getByName")
    public ResponseEntity<User> getByName(User user){
        return getTrimmer(userRescourceClient.getByName(user).getData());
    }

    @RequestMapping("/getUnApprove")
    public ResponseEntity<List<Map>> getUnApprove(){
        List<Map> maps = userRescourceClient.getUnApprove().getData();
        return success(setCollegeAndMajor(maps));
    }

//    @PostMapping(value = "/upload")
//    public ResponseEntity<Boolean> upload(@RequestParam("file") MultipartFile multipartFile, @RequestParam("newName") String newName){
//        return success(uploadToServerClient.upload(multipartFile, newName));
//    }

    @RequestMapping(value="/upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity uploadImg(@RequestParam("file") MultipartFile file,
                                          HttpServletRequest request) throws FileNotFoundException {
        String contentType = file.getContentType();
        String fileName = file.getOriginalFilename();
        //String userId = request.getHeader("Authorization");
        User user = (User) request.getSession().getAttribute("user");
        String tempUserName="";
        if(user ==null){
            tempUserName="临时";
        }else{
            tempUserName =user.getName();
        }
        String newName = pinYinUtil.getStringPinYin(tempUserName);
        fileName = toPinYin(fileName);
        newName = newName+"_action_"+fileName;
        FileUploadUtil fileUploadUtil = new FileUploadUtil();
        fileUploadUtil.uploadFile(file,newName,"/baoliyang/");
        //Actions actions = actionResourceClient.getByUserId(user).getData();
        ActionPictures actionPictures = new ActionPictures();
        actionPictures.setActionId("-1");
        actionPictures.setPurl("http://120.79.143.237/baoliyang/"+newName);
        actionPictureResourceClient.save(actionPictures);
        return success(false);
    }


    @RequestMapping(value="/uploadHome",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity uploadHome(@RequestParam("file") MultipartFile file,
                                    HttpServletRequest request) throws FileNotFoundException {
        String contentType = file.getContentType();
        String fileName = file.getOriginalFilename();
        //String userId = request.getHeader("Authorization");
        User user = (User) request.getSession().getAttribute("user");
        String tempUserName="";
        if(user ==null){
            tempUserName="临时";
        }else{
            tempUserName =user.getName();
        }
        String newName = pinYinUtil.getStringPinYin(tempUserName);
        fileName = toPinYin(fileName);
        newName = newName+"_hoem_picture_one_"+fileName;
        FileUploadUtil fileUploadUtil = new FileUploadUtil();
        fileUploadUtil.uploadFile(file,newName,"/baoliyang/");
        //Actions actions = actionResourceClient.getByUserId(user).getData();
        HomePictures homePictures = new HomePictures();
        homePictures.setId("aea043dd395011e8a53354a050ae6420");
        homePictures.setUrl("http://120.79.143.237/baoliyang/"+newName);
        homePictures.setDate(new Date());
        homePictures.setStatus("0");
        homePictures.setSortSeq(0);
        homeResourceClient.save(homePictures);
        return success(false);
    }

    @RequestMapping(value="/uploadHome1",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity uploadHome1(@RequestParam("file") MultipartFile file,
                                     HttpServletRequest request) throws FileNotFoundException {
        String contentType = file.getContentType();
        String fileName = file.getOriginalFilename();
        //String userId = request.getHeader("Authorization");
        User user = (User) request.getSession().getAttribute("user");
        String tempUserName="";
        if(user ==null){
            tempUserName="临时";
        }else{
            tempUserName =user.getName();
        }
        String newName = pinYinUtil.getStringPinYin(tempUserName);
        fileName = toPinYin(fileName);
        newName = newName+"_action_"+fileName;
        FileUploadUtil fileUploadUtil = new FileUploadUtil();
        fileUploadUtil.uploadFile(file,newName,"/baoliyang/");
        HomePictures homePictures = new HomePictures();
        homePictures.setId("b30f2dd1395011e8a53354a050ae6420");
        homePictures.setUrl("http://120.79.143.237/baoliyang/"+newName);
        homePictures.setDate(new Date());
        homePictures.setStatus("0");
        homePictures.setSortSeq(1);
        homeResourceClient.save(homePictures);
        return success(false);
    }

    @RequestMapping(value="/uploadHome2",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity uploadHome2(@RequestParam("file") MultipartFile file,
                                     HttpServletRequest request) throws FileNotFoundException {
        String contentType = file.getContentType();
        String fileName = file.getOriginalFilename();
        //String userId = request.getHeader("Authorization");
        User user = (User) request.getSession().getAttribute("user");
        String tempUserName="";
        if(user ==null){
            tempUserName="临时";
        }else{
            tempUserName =user.getName();
        }
        String newName = pinYinUtil.getStringPinYin(tempUserName);
        fileName = toPinYin(fileName);
        newName = newName+"_action_"+fileName;
        FileUploadUtil fileUploadUtil = new FileUploadUtil();
        fileUploadUtil.uploadFile(file,newName,"/baoliyang/");
        HomePictures homePictures = new HomePictures();
        homePictures.setId("b64c05cc395011e8a53354a050ae6420");
        homePictures.setUrl("http://120.79.143.237/baoliyang/"+newName);
        homePictures.setDate(new Date());
        homePictures.setStatus("0");
        homePictures.setSortSeq(2);
        homeResourceClient.save(homePictures);
        return success(false);
    }


    @RequestMapping("/getAllHomePicture")
    public ResponseEntity<List<User>> getAllHomePicture() throws Exception {
        return success(homeResourceClient.list(null).getData());
    }


    @RequestMapping("/search")
    public ResponseEntity<List<User>> search(String keyString) throws Exception {
        Result result =ToAnalysis.parse(keyString);
//        String questionStr = "APDPlat的作者是谁？";
//        Question question = SharedQuestionAnsweringSystem.getInstance().answerQuestion(keyString);
//        if (question != null) {
//            List<CandidateAnswer> candidateAnswers = question.getAllCandidateAnswer();
//            int i=1;
//            for(CandidateAnswer candidateAnswer : candidateAnswers){
//                System.out.println((i++)+"、"+candidateAnswer.getAnswer()+":"+candidateAnswer.getScore());
//            }
//        }
//        TextSimilarity textSimilarity = new CosineTextSimilarity();
//        Analyzer analyzer = new ChineseWordAnalyzer();
//        TokenStream tokenStream = analyzer.tokenStream("text", keyString);
//        tokenStream.reset();
//        while(tokenStream.incrementToken()){
//            CharTermAttribute charTermAttribute = tokenStream.getAttribute(CharTermAttribute.class);
//            OffsetAttribute offsetAttribute = tokenStream.getAttribute(OffsetAttribute.class);
//            PositionIncrementAttribute positionIncrementAttribute = tokenStream.getAttribute(PositionIncrementAttribute.class);
//            LOGGER.info(charTermAttribute.toString()+" ("+offsetAttribute.startOffset()+" - "+offsetAttribute.endOffset()+") "+positionIncrementAttribute.getPositionIncrement());
//        }
//        tokenStream.close();
//        List<Word> words = WordSegmenter.segWithStopWords(keyString);
//        //词性标注
//        PartOfSpeechTagging.process(words);
//        //同义标注
//        SynonymTagging.process(words);
//        //切分
//        words = WordRefiner.refine(words);
//        //反义标注
//        //AntonymTagging.process(words);
//        //拼音标注
//        PinyinTagging.process(words);
//        //过滤代词，动词，时间词
//        List<String> temp = new ArrayList<>();
//        if(words!=null && words.size()>0){
//            for(Word word:words){
//                if("t".equals(word.getPartOfSpeech().getPos()) || "r".equals(word.getPartOfSpeech().getPos())
//                        ||"v".equals(word.getPartOfSpeech().getPos())){
//                    words.remove(word);
//                    if(words.size()==0){
//                        break;
//                    }
//                }else{
//                    temp.add(word.getText());
//                    if(word.getSynonym()!=null && word.getSynonym().size()!=0){
//                        for(Word syn:word.getSynonym()){
//                            temp.add(syn.getText());
//                        }
//                    }
//                }
//            }
//        }
//        Map map = new HashMap();
//        if(temp!=null && temp.size()>0){
//            List<User> users = userRescourceClient.searchUser(temp).getData();
//            List<Corporation> corporations= corporationResourceClient.searchCorporation(temp).getData();
//            List<Actions> actions = actionResourceClient.searchAction(temp).getData();
//            map.put("users",users);
//            map.put("corporations",corporations);
//            map.put("actions",actions);
//        }
//        return success(map);

        //System.out.println(words);
        //词频统计设置
//        WordFrequencyStatistics wordFrequencyStatistics = new WordFrequencyStatistics();
//        wordFrequencyStatistics.setRemoveStopWord(false);
//        wordFrequencyStatistics.setResultPath("E:\\Workspace\\BiYeSheJi\\word-frequency-statistics.txt");
//        wordFrequencyStatistics.setSegmentationAlgorithm(SegmentationAlgorithm.MaxNgramScore);
//        wordFrequencyStatistics.seg(keyString);
        ArrayList<String> temp = new ArrayList<>();
        String content = "";
        for(Term term:result){
            if("n".equals(term.getNatureStr())){
//                temp.add(term.getName());
                content+=term.getName()+",";
            }
        }
        Map map = new HashMap();
//        if(temp!=null && temp.size()>0){
        if(content!=""){
            List<User> users = userRescourceClient.searchUser(content).getData();
            List<Corporation> corporations= corporationResourceClient.searchCorporation(content).getData();
            List<Actions> actions = actionResourceClient.searchAction(content).getData();
            map.put("users",users);
            map.put("corporations",corporations);
            map.put("actions",actions);
        }
        return success(map);
        //return success(words);
    }

    @RequestMapping("/getMonths")
    public ResponseEntity<List<Map>> getMonths(){
        return success(actionResourceClient.getMonths().getData());
    }


    private  ResponseEntity<User> getTrimmer(User user){
        BranchFilter userFilter = BranchFilter.newBranchFilter(User.class);
        userFilter.ignoreSystemFields();
        Trimmer trimmer = Trimmer.newTrimmer(user);
        trimmer.addBranchFilters(userFilter);
        return success(trimmer.trim());
    }

    private List<Map> setCollegeAndMajor(List<Map> maps){
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
        return maps;
    }

    public String toPinYin(String fileName){
        char[] chars = fileName.toCharArray();
        String result ="";
        for (char a : chars) {
            // 判断是否为汉字字符
            if (Character.toString(a).matches("[\\u4E00-\\u9FA5]+")) {
                String temp = pinYinUtil.getCharPinYin(a);
                result+=temp;
            }
            else{
                result+=a;
            }
        }
        return result;
    }
}
