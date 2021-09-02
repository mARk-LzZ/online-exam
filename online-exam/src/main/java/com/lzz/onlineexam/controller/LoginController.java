package com.lzz.onlineexam.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lzz.onlineexam.common.utils.*;
import com.lzz.onlineexam.entity.LoginEntity;
import com.lzz.onlineexam.entity.StudentEntity;
import com.lzz.onlineexam.entity.TeacherEntity;
import com.lzz.onlineexam.service.LoginService;
import com.lzz.onlineexam.service.StudentService;
import com.lzz.onlineexam.service.TeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * @author lzz
 * @email 1399508400@qq.com
 * @date 2021-07-25 16:30:03
 */
@RestController
@RequestMapping("onlineexam/login")
@Api(tags="登录")
public class LoginController {
    @Autowired
    private LoginService loginService;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /**
     * 手机号和注册验证码map集合
     */
    public static Map<String, String> phonecodemap1=new HashMap<>();
    /**
     * 手机号和重置密码验证码map集合
     */
    public static Map<String, String> phonecodemap2=new HashMap<>();

    @Autowired
    private StudentService studentService;

    @Autowired
    private TeacherService teacherService;


    /**
     * 列表 并将数据同步到es中
     */
    @GetMapping("/list/{page}/{size}")
    @PreAuthorize("hasAuthority('login')")
    // @RequiresPermissions("onlineexam:exammanage:list")
    public R list(@PathVariable Integer page, @PathVariable Integer size ) {

        return R.ok().put("list", loginService.loginsInfo(page,size));

    }


    /**
     * 信息
     */
    @GetMapping("/info/{loginid}")
    // @RequiresPermissions("onlineexam:login:info")
    @PreAuthorize("hasAuthority('login')")
    public R info(@PathVariable("loginid") String loginid) {
        LoginEntity login=loginService.getById(loginid);

        return R.ok().put("login", login);
    }



    /**
     * 修改
     */
    @PostMapping("/update")
    // @RequiresPermissions("onlineexam:login:update")
    @PreAuthorize("hasAuthority('login')")
    public R update(@RequestBody LoginEntity login) {
        loginService.updateById(login);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    // @RequiresPermissions("onlineexam:login:delete")
    @PreAuthorize("hasAuthority('login')")
    public R delete(@RequestBody String[] loginids) {
        loginService.removeByIds(Arrays.asList(loginids));

        return R.ok();
    }

    /**
     * 图片验证码
     */
    @ApiOperation("图片验证码")
    @RequestMapping(value="/images", method={RequestMethod.GET, RequestMethod.POST})
    public void images(HttpServletResponse response) throws IOException {
        response.setContentType("image/jpeg");
        //禁止图像缓存。
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        ValidateCode vCode=new ValidateCode(820, 200, 5, 80);
        vCode.write(response.getOutputStream());
    }

    @PostMapping("/user/sendregcode")
    @ApiOperation(value="注册时发送短信验证码")
    @ApiImplicitParams({@ApiImplicitParam(name="mobilephone", value="电话号码"), @ApiImplicitParam(name="type", value="是否违规 0不违规")})

    public R sendregcode(HttpServletRequest request) throws IOException {
        JSONObject jsonObject=JsonReader.receivePost(request);
        final String mobilephone=jsonObject.getString("mobilephone");
        int type=jsonObject.getInteger("type");
        if (type != 0) {
            return R.error("违规操作");
        }
        //查询手机号是否已经注册
        StudentEntity studentIsExist=studentService.getOne(new QueryWrapper<StudentEntity>().eq("tel", mobilephone));
        TeacherEntity teacherIsExist=teacherService.getOne(new QueryWrapper<TeacherEntity>().eq("tel", mobilephone));

        if (!StringUtils.isEmpty(studentIsExist) || !StringUtils.isEmpty(teacherIsExist)) {//用户账号已经存在
            return R.error("该手机号已经注册过了");
        }
        String code=GetCode.phonecode();
        Integer result=new SmsUtil().SendMsg(mobilephone, code, type);//发送验证码
        if (result == 1) {//发送成功
            phonecodemap1.put(mobilephone, code);//放入map集合进行对比

            //执行定时任务
            ScheduledThreadPoolExecutor executorService=new ScheduledThreadPoolExecutor(1,
                    new BasicThreadFactory.Builder().namingPattern("example-schedule-pool-%d").daemon(true).build());
            executorService.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    phonecodemap1.remove(mobilephone);
                    executorService.remove(this);
                }
            }, 10 * 1000, 10 * 1000, TimeUnit.HOURS);
            return R.ok("验证码发送成功");
        } else if (result == 2) {
            return R.error("请输入正确格式的手机号");
        }
        return R.error("验证码发送失败");
    }

    @PostMapping("/login")
    @ApiOperation("登录")
    @ApiImplicitParams({@ApiImplicitParam(name="vercode", value="图片验证码"), @ApiImplicitParam(name=" tel or userid", value="手机号 或 账号")})
    public R login(@ApiIgnore @RequestBody LoginEntity loginEntity, @ApiIgnore HttpSession session) {

        if (!ValidateCode.code.equalsIgnoreCase(loginEntity.getVercode())) {
            return R.error().put("message", "请输入正确的验证码");
        }

        R r=loginService.login(loginEntity);


        if (r.equals(R.ok("1"))) {
            //把touken写入session
            QueryWrapper<TeacherEntity> wrapper=new QueryWrapper<TeacherEntity>()
                    .eq("tel", loginEntity.getTel())
                    .or()
                    .eq("cardId", loginEntity.getUserid());
            TeacherEntity teacherEntity=teacherService.getOne(wrapper);
            //使用手机号登录
            if (loginEntity.getTel()!= null){
                String token=DigestUtils.md5DigestAsHex(teacherEntity.getTel().getBytes());
                session.setAttribute("token" ,token);
            }
            //使用账号登录
            else if (loginEntity.getUserid() != null){
                String token=DigestUtils.md5DigestAsHex(teacherEntity.getCardid().getBytes());
                session.setAttribute("token" ,token);
            }
            session.setAttribute("userid",teacherEntity.getTeacherid());
            session.setAttribute("username",teacherEntity.getTeachername());
            return R.ok("教师登录成功").put("Teacher", teacherEntity);
        }

        if (r.equals(R.ok("2"))) {
            //把touken写入session
            QueryWrapper<StudentEntity> wrapper=new QueryWrapper<StudentEntity>()
                    .eq("tel", loginEntity.getTel())
                    .or()
                    .eq("cardId", loginEntity.getUserid());
            StudentEntity studentEntity=studentService.getOne(wrapper);
            //使用手机号登录
            if (loginEntity.getTel()!= null){
                String token = DigestUtils.md5DigestAsHex(studentEntity.getTel().getBytes());
                session.setAttribute("token" ,token);
            }
            //使用账号登录
            else if (loginEntity.getUserid() != null){
                String token = DigestUtils.md5DigestAsHex(studentEntity.getCardid().getBytes());
                session.setAttribute("token" ,token);
            }
            session.setAttribute("userid",studentEntity.getStudentid());
            session.setAttribute("username",studentEntity.getStudentname());
            return R.ok("学生登录成功").put("Student", studentEntity);
        }

        if (r.equals(R.error().put("message", 1))) {
            return R.error("密码错误");
        }

        if (r.equals(R.error().put("message", 2))) {
            return R.error("用户不存在");
        }
        return R.error("登陆失败");
    }


    //退出登录
    @PostMapping("/logout")
    @ApiOperation(value="退出登录")
    public R logout(HttpServletRequest request,@ApiIgnore HttpSession session){
        Double userid = (Double) session.getAttribute("userid");
        String username = (String)session.getAttribute("username");
        String token = (String) session.getAttribute("token");
        if(StringUtils.isEmpty(userid) && StringUtils.isEmpty(username) && StringUtils.isEmpty(token)){
            return R.error().put("msg" , "非法退出登录");
        }
        request.getSession().removeAttribute("userid");
        request.getSession().removeAttribute("username");
        request.getSession().removeAttribute("token");
        return R.ok().put("msg", "退出登录");
    }

}
