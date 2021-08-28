package com.lzz.onlineexam.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.lzz.onlineexam.common.utils.*;
import com.lzz.onlineexam.entity.LoginEntity;
import com.lzz.onlineexam.entity.StudentEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.lzz.onlineexam.entity.TeacherEntity;
import com.lzz.onlineexam.service.TeacherService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 教师信息表
 *
 * @author lzz
 * @email 1399508400@qq.com
 * @date 2021-07-21 13:09:54
 */
@RestController
@RequestMapping("onlineexam/teacher")
@Api(tags="教师信息")

public class TeacherController {
    @Autowired
    private TeacherService teacherService;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /**
     * 列表 并将数据同步到es中
     */
    @GetMapping("/list/{page}/{size}")
    // @RequiresPermissions("onlineexam:exammanage:list")
    public R list(@PathVariable Integer page, @PathVariable Integer size ) {

        return R.ok().put("list", teacherService.teachersInfo(page,size));

    }


    /**
     * 信息
     */
    @GetMapping("/info/{teacherid}")
   // @RequiresPermissions("onlineexam:teacher:info")
    public R info(@PathVariable("teacherid") Integer teacherid){
		TeacherEntity teacher = teacherService.getById(teacherid);
        return R.ok().put("teacher", teacher);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
  //  @RequiresPermissions("onlineexam:teacher:save")
    public R save(@RequestBody TeacherEntity teacher){
		teacherService.save(teacher);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
   // @RequiresPermissions("onlineexam:teacher:update")
    public R update(@RequestBody TeacherEntity teacher){
		teacherService.updateById(teacher);

        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
   // @RequiresPermissions("onlineexam:teacher:delete")
    public R delete(@RequestBody Integer[] teacherids){
		teacherService.removeByIds(Arrays.asList(teacherids));

        return R.ok();
    }

    //教师注册
    @PostMapping("/register")
    @ApiOperation("教师注册")
    @ApiImplicitParams({@ApiImplicitParam(name="vercode" , value="短信验证码") ,@ApiImplicitParam(name="teachername" , value="姓名")
                        ,@ApiImplicitParam(name="pwd" , value="密码") , @ApiImplicitParam(name="tel" , value="手机号")})
    public R register(@RequestBody TeacherEntity teacherEntity) {
        R r=teacherService.register(teacherEntity);
        if (r.equals(R.error().put("message", 1))) {
            return R.error("号码注册过了");
        }
        if (r.equals(R.error().put("message", 2))) {
            return R.error("短信验证码超时，请重新发送");
        }
        if (r.equals(R.error().put("message", 3))) {
            return R.error("短信验证码错误");
        }

        return R.ok("注册成功");


    }

    @GetMapping("/teacherinfo/{teacherName}")
    @ApiOperation("获取教师个人信息")
    public R teacherInfo(HttpServletRequest request ,@PathVariable String teacherName){
        String token = CookieUtils.getCookieValue(request,Constant.COOKIE_LOGIN_KEY);
        if (teacherService.teacherInfo(token , teacherName)!=null){
            return R.ok().put("teacherEntity" , teacherService.teacherInfo(token ,teacherName));
        }
        return R.error().put("msg","用户身份失效，请重新登录");
    }

    @GetMapping("/studentsinfo/{page}/{size}")
    @ApiOperation("查看所有学生")
    public R studentsInfo(@PathVariable Integer page , @PathVariable Integer size){
       return R.ok().put("students", teacherService.studentsInfo(page , size));
    }

}
