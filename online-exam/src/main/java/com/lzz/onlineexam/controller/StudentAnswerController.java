package com.lzz.onlineexam.controller;

import java.util.Arrays;
import java.util.List;

import com.lzz.onlineexam.common.exception.RRException;
import com.lzz.onlineexam.common.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.lzz.onlineexam.entity.StudentAnswerEntity;
import com.lzz.onlineexam.service.StudentAnswerService;
import springfox.documentation.annotations.ApiIgnore;


/**
 * 
 *
 * @author lzz
 * @email 1399508400@qq.com
 * @date 2021-08-12 00:38:24
 */
@RestController
@Api(tags="试卷保存")
@RequestMapping("onlineexam/studentanswer")
public class StudentAnswerController {
    @Autowired
    private StudentAnswerService studentAnswerService;


    /**
     * 列表
     */
    @GetMapping("/list/{page}/{size}")
    @PreAuthorize("hasAuthority('studentAnswer')")
    // @RequiresPermissions("onlineexam:exammanage:list")
    public R list(@PathVariable Integer page, @PathVariable Integer size ) {

        return R.ok().put("list", studentAnswerService.studentAnswersInfo(page,size));

    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
   // @RequiresPermissions("onlineexam:studentanswer:info")
    @PreAuthorize("hasAuthority('studentAnswer')")
    public R info(@PathVariable("id") Integer id){
		StudentAnswerEntity studentAnswer = studentAnswerService.getById(id);

        return R.ok().put("studentAnswer", studentAnswer);
    }


    /**
     * 修改
     */
    @PostMapping("/update")
   // @RequiresPermissions("onlineexam:studentanswer:update")
    @PreAuthorize("hasAuthority('studentAnswer')")
    public R update(@RequestBody StudentAnswerEntity studentAnswer){
		studentAnswerService.updateById(studentAnswer);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
   // @RequiresPermissions("onlineexam:studentanswer:delete")
    @PreAuthorize("hasAuthority('studentAnswer')")
    public R delete(@RequestBody Integer[] ids){
		studentAnswerService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    /*
    * 試題提交
    * */
    @PostMapping("/questionsubmit")
    @ApiOperation("试题提交")
    @ApiImplicitParams({@ApiImplicitParam(name="studentid" , value="学生id(非账号)") , @ApiImplicitParam(name="paperid" , value="试卷id"),
    @ApiImplicitParam(name="questionType" , value="1填空 2判断 3选择 4主观"), @ApiImplicitParam(name="questionid" ,value="题目id"),
    @ApiImplicitParam(name="userAnswer" ,value="用户答案")})
    public R questionSubmit(@ApiIgnore @RequestBody StudentAnswerEntity studentAnswerEntity){
        if (studentAnswerEntity.getStudentid()!= null &&
            studentAnswerEntity.getPaperid()!= null &&
            studentAnswerEntity.getQuestionType()!=0 &&
            studentAnswerEntity.getQuestionid()!=null &&
            studentAnswerEntity.getUserAnswer()!= null ){
            studentAnswerService.questionSubmit(studentAnswerEntity);
            return R.ok();
        }
        throw new RRException("试题提交信息不完整" ,1000);
    }

    @GetMapping("/getpaper")
    @ApiOperation("做完查看试卷")
    @PreAuthorize("hasAnyAuthority('studentAnswer,student')")
    public R getPaper(){
        return R.ok().put("paper" , studentAnswerService.getPaper());
    }

    @PostMapping("/handjudge")
    @ApiOperation("手改填空和主观(先请求教师查看未批改试卷)")
    @PreAuthorize("hasAuthority('studentAnswer')")
    public R handJudge(List<Double> scores){
        R r=studentAnswerService.handJudge(scores);
        if (r.equals(R.ok())){
            return R.ok();
        }
        throw  new RRException("无法手改");
    }

    @PostMapping("/autojudge")
    @ApiOperation("自动改选择和判断（先请求教师查看未批改试卷）")
    @PreAuthorize("hasAuthority('studentAnswer')")
    public R autoJudge(){
        R r=studentAnswerService.autoJudge();
        if (r.equals(R.ok())){
            return R.ok();
        }
        throw new RRException("自动改卷失败");
    }

    @PostMapping("questioncorrected")
    @ApiOperation("学生查看已批改试卷")
    @ApiImplicitParams({@ApiImplicitParam(name="studentid" , value="学生id（非账号）") , @ApiImplicitParam(name="paperid" , value="试卷id")})
    @PreAuthorize("hasAuthority('studentAnswer,student')")
    public R questionsCorrected(@ApiIgnore @RequestBody StudentAnswerEntity studentAnswerEntity){
        Double studentId = studentAnswerEntity.getStudentid();
        Double paperId = studentAnswerEntity.getPaperid();
        return R.ok().put("paper" , studentAnswerService.questionsCorrected(studentId,paperId));
    }

    @PostMapping("questionswaitingcorrect")
    @ApiOperation("教师查看未批改试卷")
    @PreAuthorize("hasAuthority('studentAnswer')")
    @ApiImplicitParams({@ApiImplicitParam(name="studentid" , value="学生id（非账号）") , @ApiImplicitParam(name="paperid" , value="试卷id")})
    public R questionsWaitingCorrect(@ApiIgnore @RequestBody StudentAnswerEntity studentAnswerEntity){
        Double studentId = studentAnswerEntity.getStudentid();
        Double paperId = studentAnswerEntity.getPaperid();
        return R.ok().put("paper" , studentAnswerService.questionsWaitingCorrect(studentId,paperId));
    }

    @PostMapping("allwaitingcorrect")
    @ApiOperation("教师查看所有未批改试题")
    @ApiImplicitParams({@ApiImplicitParam(name="paperid" , value="试卷id")})
    @PreAuthorize("hasAuthority('studentAnswer')")
    public R allWaitingCorrect(@ApiIgnore @RequestBody StudentAnswerEntity studentAnswerEntity){
        Double paperId = studentAnswerEntity.getPaperid();
        return R.ok().put("paper" , studentAnswerService.allWaitingCorrect(paperId));
    }

}
