package com.lzz.onlineexam.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzz.onlineexam.common.exception.RRException;
import com.lzz.onlineexam.service.StudentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.lzz.onlineexam.entity.ScoreEntity;
import com.lzz.onlineexam.service.ScoreService;
import com.lzz.onlineexam.common.utils.PageUtils;
import com.lzz.onlineexam.common.utils.R;



/**
 * 成绩管理表
 *
 * @author lzz
 * @email 1399508400@qq.com
 * @date 2021-07-21 13:09:54
 */
@RestController
@RequestMapping("onlineexam/score")
@Api(tags="成绩")
public class ScoreController {
    @Autowired
    private ScoreService scoreService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /**
     * 列表
     */
    @GetMapping("/list/{page}/{size}")
    @PreAuthorize("hasAuthority('score')")
    public R list(@PathVariable Integer page, @PathVariable Integer size ) {

        return R.ok().put("list", scoreService.scoresInfo(page,size));

    }

    /**
     * 信息
     */

    @GetMapping("/info/{examcode}/{page}/{size}")
    @ApiOperation("根据考试id查询")
    @PreAuthorize("hasAuthority('score')")
    public R infoByExamCode(@PathVariable("examcode") Double examCode , @PathVariable Integer page ,@PathVariable Integer size){
        IPage<ScoreEntity> iPage=scoreService.infoByExamCode(examCode, page, size);
        if (iPage == null){
            return R.error().put("msg" , "考试记录为空");
        }
        return R.ok().put("info" , iPage);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('score')")
    public R save(@RequestBody ScoreEntity score) throws RRException {
        if (score.getExamcode()!= null
            && score.getScore()!= null
            && score.getStudentid()!= null
            && score.getSubject()!= null){
            scoreService.save(score);

            return R.ok();
        }
		throw new RRException("分数信息不全", 1000);
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('score')")
    // @RequiresPermissions("onlineexam:score:update")
    public R update(@RequestBody ScoreEntity score){
		scoreService.updateById(score);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    @PreAuthorize("hasAuthority('score')")
    // @RequiresPermissions("onlineexam:score:delete")
    public R delete(@RequestBody Integer[] scoreids){
		scoreService.removeByIds(Arrays.asList(scoreids));

        return R.ok();
    }


    @GetMapping("/studentexamrecord/{studentid}/{page}/{size}")
    @ApiOperation("学生考试查询")
    @PreAuthorize("hasAuthority('student')")
    public R myExamRecord(@PathVariable String studentid , @PathVariable Integer page , @PathVariable Integer size){
        Page<ScoreEntity> scoreEntityPage=studentService.myExamRecord(studentid, page, size);
        if (scoreEntityPage == null){
            return R.error().put("msg" , "暂无考试记录");
        }
        return R.ok().put("scores" , scoreEntityPage);
    }

}
