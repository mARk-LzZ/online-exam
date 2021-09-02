package com.lzz.onlineexam.controller;


import java.util.Arrays;



import com.lzz.onlineexam.common.exception.RRException;
import io.swagger.annotations.Api;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.lzz.onlineexam.entity.ExamManageEntity;
import com.lzz.onlineexam.service.ExamManageService;
import com.lzz.onlineexam.common.utils.R;



/**
 * 考试管理表
 *
 * @author lzz
 * @email 1399508400@qq.com
 * @date 2021-07-21 13:09:54
 */
@RestController
@RequestMapping("onlineexam/exammanage")
@Api(tags={"考试控制器"})
public class ExamManageController {
    @Autowired
    private ExamManageService examManageService;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /**
     * 列表 并将数据同步到es中
     */
    @GetMapping("/list/{page}")
    // @RequiresPermissions("onlineexam:exammanage:list")
    @PreAuthorize("hasAnyAuthority('examManage,student')")
    public R list(@PathVariable Integer page ) {

        return R.ok().put("list", examManageService.examsInfo(page));

    }


    /**
     * 信息
     */
    @GetMapping("/info/{examcode}")
   // @RequiresPermissions("onlineexam:exammanage:info")
    @PreAuthorize("hasAnyAuthority('examManage,student')")
    public R info(@PathVariable("examcode") Integer examcode){
		ExamManageEntity examManage = examManageService.getById(examcode);

        return R.ok().put("examManage", examManage);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
  //  @RequiresPermissions("onlineexam:exammanage:save")
    @PreAuthorize("hasAuthority('examManage')")
    public R save(@RequestBody ExamManageEntity examManage) throws RRException {
        if (examManage.getSource()!=null &&
                examManage.getDescription()!=null &&
                examManage.getExamdate()!=null &&
                examManage.getGrade()!=null &&
                examManage.getInstitute()!=null &&
                examManage.getPaperid()!=null &&
                examManage.getMajor()!=null &&
                examManage.getTerm()!=null &&
                examManage.getTotalscore()!=null &&
                examManage.getTotaltime()!=null &&
                examManage.getType()!= null){
            examManageService.save(examManage);

            return R.ok();
        }
        throw new RRException("考试信息信息不完整"  , 1000);
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    @PreAuthorize("hasAuthority('examManage')")
    public R update(@RequestBody ExamManageEntity examManage){
		examManageService.updateById(examManage);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    @PreAuthorize("hasAuthority('examManage')")
    // @RequiresPermissions("onlineexam:exammanage:delete")
    public R delete(@RequestBody Integer[] examcodes){
		examManageService.removeByIds(Arrays.asList(examcodes));

        return R.ok();
    }

}
