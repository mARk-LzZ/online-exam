package com.lzz.onlineexam.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lzz.onlineexam.common.utils.PageUtils;
import com.lzz.onlineexam.common.utils.R;
import com.lzz.onlineexam.entity.StudentAnswerEntity;

import java.util.List;
import java.util.Map;

/**
 * @author lzz
 * @email 1399508400@qq.com
 * @date 2021-08-12 00:38:24
 */
public interface StudentAnswerService extends IService<StudentAnswerEntity> {

    PageUtils queryPage(Map<String, Object> params);

    //存放试题
    boolean questionSubmit(StudentAnswerEntity studentAnswerEntity);

    //将题目封装成一组试卷 获取试卷
    Map<String, List<StudentAnswerEntity>> getPaper();

    //学生查看学生已批改的试题
    List<StudentAnswerEntity> questionsCorrected(Double studentId, Double paperId);

    //老师查看学生未批改的试题 等待批改
    List<StudentAnswerEntity> questionsWaitingCorrect(Double studentId, Double paperId);

    //自动判分选择填空
    R autoJudge();

    //手动批改填空和主观题
    R handJudge(List<Double> scores);

    //查看所有为批改的试题
    List<StudentAnswerEntity> allWaitingCorrect(Double paperId);

    IPage<StudentAnswerEntity> studentAnswersInfo(Integer page, Integer size);
}

