package com.lzz.onlineexam.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzz.onlineexam.common.utils.Constant;
import com.lzz.onlineexam.common.utils.CookieUtils;
import com.lzz.onlineexam.common.utils.R;
import com.lzz.onlineexam.common.utils.ValidateCode;
import com.lzz.onlineexam.entity.LoginEntity;
import com.lzz.onlineexam.entity.ScoreEntity;
import com.lzz.onlineexam.entity.StudentAnswerEntity;
import com.lzz.onlineexam.entity.StudentEntity;
import com.lzz.onlineexam.service.LoginService;
import com.lzz.onlineexam.service.StudentAnswerService;
import com.lzz.onlineexam.service.StudentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * 学生信息表
 *
 * @author lzz
 * @email 1399508400@qq.com
 * @date 2021-07-21 13:09:54
 */
@RestController
@RequestMapping("onlineexam/student")
@Api(tags="学生信息")
public class StudentController {
    @Autowired
    private StudentService studentService;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private RedisTemplate redisTemplate;


    @Autowired
    private StudentAnswerService studentAnswerService;

    /**
     * 列表
     */
    @GetMapping("/list/{page}/{size}")
    // @RequiresPermissions("onlineexam:exammanage:list")
    @PreAuthorize("hasAnyAuthority('admin,teacher')")
    public R list(@PathVariable Integer page, @PathVariable Integer size ) {

        return R.ok().put("list", studentService.studentsInfo(page,size));

    }




    /**
     * 保存
     */
    @PostMapping("/save")
    //  @RequiresPermissions("onlineexam:student:save")
    @PreAuthorize("hasAnyAuthority('admin,teacher')")
    public R save(@RequestBody StudentEntity student) {
        studentService.save(student);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    // @RequiresPermissions("onlineexam:student:update")
    @PreAuthorize("hasAuthority('teacher')")
    public R update(@RequestBody StudentEntity student) {
        studentService.updateById(student);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    // @RequiresPermissions("onlineexam:student:delete")
    @PreAuthorize("hasAnyAuthority('admin,teacher')")
    public R delete(@RequestBody Integer[] studentids) {
        studentService.removeByIds(Arrays.asList(studentids));

        return R.ok();
    }



    //学生注册
    @PostMapping("/register")
    @ApiOperation("学生注册")
    @ApiImplicitParams({@ApiImplicitParam(name="vercode" , value="短信验证码") ,@ApiImplicitParam(name="teachername" , value="姓名")
            ,@ApiImplicitParam(name="pwd" , value="密码") , @ApiImplicitParam(name="tel" , value="手机号")})
    public R register(@RequestBody StudentEntity studentEntity) {
        R r=studentService.register(studentEntity);
        String cardid = studentEntity.getCardid();
        if (r.equals(R.error().put("message", 1))) {
            return R.error("号码注册过了");
        }
        if (r.equals(R.error().put("message", 2))) {
            return R.error("短信验证码超时，请重新发送");
        }
        if (r.equals(R.error().put("message", 3))) {
            return R.error("短信验证码错误");
        }

        return R.ok("注册成功，账号为: " + cardid);


    }

    //获取个人信息 （需在登陆后）
    @PostMapping("/studentinfo")
    @ApiOperation("获取学生个人信息")
    @PreAuthorize("hasAuthority('student')")
    public R myinfo(HttpServletRequest request , @RequestBody StudentEntity studentEntity, @ApiIgnore HttpSession session){
        String studentName = studentEntity.getStudentname();
        String token = (String) session.getAttribute("token");
        String studentInfo=studentService.myInfo(token, studentName);
        if (studentInfo !=null){
            return R.ok().put("studentEntity" , studentInfo);
        }
        return R.error().put("msg","用户身份失效，请重新登录");
    }

    /*
    * 查看学生信息（教师）
    * */
    @GetMapping("/info/{studentid}")
    // @RequiresPermissions("onlineexam:student:info")
    @PreAuthorize("hasAnyAuthority('admin,teacher')")
    public R info(@PathVariable("studentid") Long studentid) {
        StudentEntity student=studentService.getById(studentid);
        if (student == null){
            return R.error().put("msg" , "无此学生");
        }
        return R.ok().put("student", student);
    }

    @PostMapping("/questioncorrected")
    @ApiOperation("学生查看已批改试卷")
    @ApiImplicitParams({@ApiImplicitParam(name="studentid" , value="学生id（非账号）") , @ApiImplicitParam(name="paperid" , value="试卷id")})
    @PreAuthorize("hasAuthority('student')")
    public R questionsCorrected(@ApiIgnore @RequestBody StudentAnswerEntity studentAnswerEntity){
        Double studentId = studentAnswerEntity.getStudentid();
        Double paperId = studentAnswerEntity.getPaperid();
        List<StudentAnswerEntity> studentAnswerEntities=studentAnswerService.questionsCorrected(studentId, paperId);
        if (studentAnswerEntities == null){
            return R.error().put("msg" , "考卷暂未批改完成");
        }
        return R.ok().put("paper" , studentAnswerEntities );
    }
}
