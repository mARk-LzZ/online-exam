package com.lzz.onlineexam.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lzz.onlineexam.common.exception.RRException;
import com.lzz.onlineexam.entity.FillQuestionEntity;
import io.swagger.annotations.Api;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.lzz.onlineexam.entity.JudgeQuestionEntity;
import com.lzz.onlineexam.service.JudgeQuestionService;
import com.lzz.onlineexam.common.utils.PageUtils;
import com.lzz.onlineexam.common.utils.R;



/**
 * 判断题题库表
 *
 * @author lzz
 * @email 1399508400@qq.com
 * @date 2021-07-21 13:09:54
 */
@RestController
@RequestMapping("onlineexam/judgequestion")
@Api(tags="判断题")
@PreAuthorize("hasAnyAuthority('admin,teacher')")
public class JudgeQuestionController {
    @Autowired
    private JudgeQuestionService judgeQuestionService;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /**
     * 列表 并将数据同步到es中
     */
    @GetMapping("/list/{page}/{size}")
    // @RequiresPermissions("onlineexam:exammanage:list")
    public R list(@PathVariable Integer page, @PathVariable Integer size ) {

        return R.ok().put("list", judgeQuestionService.judgeQuestionsInfo(page,size));

    }


    /**
     * 信息
     */
    @GetMapping("/info/{questionid}")
   // @RequiresPermissions("onlineexam:judgequestion:info")
    public R info(@PathVariable("questionid") Integer questionid){
		JudgeQuestionEntity judgeQuestion = judgeQuestionService.getById(questionid);

        return R.ok().put("judgeQuestion", judgeQuestion);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    //  @RequiresPermissions("onlineexam:fillquestion:save")
    public R save(@RequestBody JudgeQuestionEntity judgeQuestionEntity) throws RRException {
        if (judgeQuestionEntity.getQuestion()!=null &&
                judgeQuestionEntity.getAnswer()!= null &&
                judgeQuestionEntity.getLevel() != null &&
                judgeQuestionEntity.getSubject() != null &&
                judgeQuestionEntity.getScore() !=null){
            judgeQuestionService.save(judgeQuestionEntity);
            List<JudgeQuestionEntity> judgeQuestionEntities=judgeQuestionService.list(new QueryWrapper<JudgeQuestionEntity>()
                    .eq("question", judgeQuestionEntity.getQuestion())
                    .eq("answer", judgeQuestionEntity.getAnswer()));


            return R.ok().put("new JudgeQuestions" , judgeQuestionEntities);
        }
        throw new RRException("题库信息不完整"  , 1000);
    }

    /**
     * 修改
     */
    @PutMapping("/update")
   // @RequiresPermissions("onlineexam:judgequestion:update")
    public R update(@RequestBody JudgeQuestionEntity judgeQuestion){
		judgeQuestionService.updateById(judgeQuestion);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
   // @RequiresPermissions("onlineexam:judgequestion:delete")
    public R delete(@RequestBody Integer[] questionids){
		judgeQuestionService.removeByIds(Arrays.asList(questionids));

        return R.ok();
    }

}
