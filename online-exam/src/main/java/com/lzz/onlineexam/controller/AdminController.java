package com.lzz.onlineexam.controller;


import java.util.Arrays;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lzz.onlineexam.common.exception.RRException;
import com.lzz.onlineexam.common.utils.*;
import com.lzz.onlineexam.entity.StudentEntity;
import com.lzz.onlineexam.entity.TeacherEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import com.lzz.onlineexam.entity.AdminEntity;
import com.lzz.onlineexam.service.AdminService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 管理员信息表
 *
 * @author lzz
 * @email 1399508400@qq.com
 * @date 2021-07-21 13:09:54
 */
@Api(tags={"管理员"})
@RestController
@RequestMapping("onlineexam/admin")
@PreAuthorize("hasAuthority('admin')")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @Autowired
    private RestHighLevelClient restHighLevelClient;


    @GetMapping("/list/{page}/{size}")
    public R list(@PathVariable Integer page, @PathVariable Integer size ) {

        return R.ok().put("list", adminService.adminsInfo(page,size));

    }
    /*
    * 登录
    */
    @PostMapping("/login")
    @ApiOperation("登录")
    @ApiImplicitParams({@ApiImplicitParam(name="adminname" ,value="管理员名") , @ApiImplicitParam(name="pwd" , value="密码")})
    public R login(@RequestBody AdminEntity adminEntity , HttpServletRequest request , HttpServletResponse response ){

        R r = adminService.login(adminEntity);

        if (r.equals(R.ok())){
            //把touken写入cookie
            String token = DigestUtils.md5DigestAsHex(adminEntity.getAdminname().getBytes());
            request.setAttribute("token" , token);
            CookieUtils.setCookie(request , response , Constant.COOKIE_LOGIN_KEY , token , 10800, true);
            return R.ok("登录成功");
        }
        return R.error("登陆失败");
    }

    //获取个人信息 （需在登陆后）
    @GetMapping("/admininfo")
    @ApiOperation("根据token获取管理员个人信息")
    public R admininfo(HttpServletRequest request , String adminName){
        String token = CookieUtils.getCookieValue(request,Constant.COOKIE_LOGIN_KEY);
        String adminInfo=adminService.adminInfo(token, adminName);
        if (adminInfo !=null){
               return R.ok().put("AdminEntity" , adminInfo);
           }
            return R.error().put("msg","用户身份失效，请重新登录");
    }

    @GetMapping("teacherinfo/{page}/{size}")
    @ApiOperation("获取所有教师信息")
    public IPage<TeacherEntity> teacherInfo(@PathVariable Integer page , @PathVariable Integer size){
           return adminService.teacherInfo(page , size);
    }

    @GetMapping("studentinfo/{page}/{size}")
    @ApiOperation("获取所有学生信息")
    public IPage<StudentEntity> studentInfo(@PathVariable Integer page , @PathVariable Integer size){
        return adminService.studentInfo(page , size);
    }
    @GetMapping("/teacherinfobyname/{page}/{size}")
    @ApiOperation("根据姓名获取教师信息")
    public R teacherInfoByName( @PathVariable Integer page ,
                                                   @PathVariable Integer size,
                                                        String teacherName
                                                   ){
         return R.ok().put("teacherInfo" , adminService.teacherInfoByname(teacherName, page, size));
    }
    /**
     * 注册管理员
     */
    @PostMapping("/save")
  //  @RequiresPermissions("onlineexam:admin:save")
    @ApiImplicitParams({@ApiImplicitParam(name="adminname") , @ApiImplicitParam(name="pwd") , @ApiImplicitParam(name="tel")})
    public R save(@RequestBody AdminEntity admin) throws RRException {
        if(admin.getAdminname()!= null && admin.getPwd()!=null && admin.getTel()!= null){
            adminService.save(admin);
            return R.ok();
        }
        throw new RRException("管理员信息不完整"  , 1000);

    }

    /**
     * 修改
     */
    @PostMapping("/update")
   // @RequiresPermissions("onlineexam:admin:update")
    public R update(@RequestBody AdminEntity admin){
		adminService.updateById(admin);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
   // @RequiresPermissions("onlineexam:admin:delete")
    public R delete(@RequestBody Integer[] adminids){
		adminService.removeByIds(Arrays.asList(adminids));

        return R.ok();
    }


}
