package com.lzz.onlineexam.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lzz.onlineexam.common.exception.RRException;
import com.lzz.onlineexam.common.utils.PageUtils;
import com.lzz.onlineexam.common.utils.R;
import com.lzz.onlineexam.entity.MultiQuestionEntity;
import com.lzz.onlineexam.entity.SubjectiveQuestionEntity;
import com.lzz.onlineexam.service.SubjectiveQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


/**
 * 判断题题库表
 *
 * @author lzz
 * @email 1399508400@qq.com
 * @date 2021-08-12 19:25:11
 */
@RestController
@RequestMapping("onlineexam/subjectivequestion")
public class SubjectiveQuestionController {
    @Autowired
    private SubjectiveQuestionService subjectiveQuestionService;

    /**
     * 列表
     */
    @GetMapping("/list/{page}/{size}")
    // @RequiresPermissions("onlineexam:exammanage:list")
    @PreAuthorize("hasAnyAuthority('subjectiveQuestion,student')")
    public R list(@PathVariable Integer page, @PathVariable Integer size ) {

        return R.ok().put("list", subjectiveQuestionService.subjectiveQuestionsInfo(page,size));

    }


    /**
     * 信息
     */
    @RequestMapping("/info/{questionid}")
   // @RequiresPermissions("onlineexam:subjectivequestion:info")
    @PreAuthorize("hasAnyAuthority('subjectiveQuestion,student')")
    public R info(@PathVariable("questionid") String questionid){
		SubjectiveQuestionEntity subjectiveQuestion = subjectiveQuestionService.getById(questionid);

        return R.ok().put("subjectiveQuestion", subjectiveQuestion);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    //  @RequiresPermissions("onlineexam:fillquestion:save")
    @PreAuthorize("hasAuthority('subjectiveQuestion')")
    public R save(@RequestBody SubjectiveQuestionEntity subjectiveQuestionEntity) throws RRException {
        if (subjectiveQuestionEntity.getQuestion()!=null &&
                subjectiveQuestionEntity.getAnswer()!= null &&
                subjectiveQuestionEntity.getLevel() != null &&
                subjectiveQuestionEntity.getSubject() != null &&
                subjectiveQuestionEntity.getScore() !=null){
            subjectiveQuestionService.save(subjectiveQuestionEntity);
            List<SubjectiveQuestionEntity> subjectiveQuestionEntities=subjectiveQuestionService.list(new QueryWrapper<SubjectiveQuestionEntity>()
                    .eq("question", subjectiveQuestionEntity.getQuestion())
                    .eq("answer", subjectiveQuestionEntity.getAnswer()));


            return R.ok().put("new multiQuestions" , subjectiveQuestionEntities);
        }
        throw new RRException("题库信息不全" ,1000);
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @PreAuthorize("hasAuthority('subjectiveQuestion')")
    // @RequiresPermissions("onlineexam:subjectivequestion:update")
    public R update(@RequestBody SubjectiveQuestionEntity subjectiveQuestion){
		subjectiveQuestionService.updateById(subjectiveQuestion);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @PreAuthorize("hasAuthority('subjectiveQuestion')")
    // @RequiresPermissions("onlineexam:subjectivequestion:delete")
    public R delete(@RequestBody String[] questionids){
		subjectiveQuestionService.removeByIds(Arrays.asList(questionids));

        return R.ok();
    }

}
