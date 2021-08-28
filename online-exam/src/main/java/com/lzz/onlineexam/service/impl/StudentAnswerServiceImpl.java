package com.lzz.onlineexam.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzz.onlineexam.common.utils.PageUtils;
import com.lzz.onlineexam.common.utils.Query;
import com.lzz.onlineexam.common.utils.R;
import com.lzz.onlineexam.entity.*;
import com.lzz.onlineexam.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


import com.lzz.onlineexam.dao.StudentAnswerDao;


@Service("studentAnswerService")
public class StudentAnswerServiceImpl extends ServiceImpl<StudentAnswerDao, StudentAnswerEntity> implements StudentAnswerService{
    //储存答案 自动批改前查看
    private final List<StudentAnswerEntity> paper = new ArrayList<>();
    //储存所有未批改试题
    private final List<StudentAnswerEntity> questionsWaitingCorrect = new ArrayList<>();
    //计算总分
    public Double etScore= 0.0;

    @Autowired
    private FillQuestionService fillQuestionService;

    @Autowired
    private JudgeQuestionService judgeQuestionService;

    @Autowired
    private MultiQuestionService multiQuestionService;

    @Autowired
    private SubjectiveQuestionService subjectiveQuestionService;

    //map存放试卷
    Map<String , List<StudentAnswerEntity>> map = new HashMap<>();
    //填空题列表
    List<StudentAnswerEntity> fillQuestionEntities = new ArrayList<>();
    //选择题列表
    List<StudentAnswerEntity> multiQuestionEntities = new ArrayList<>();
    //判断题列表
    List<StudentAnswerEntity> judgeQuestionEntities = new ArrayList<>();
    //主观题列表
    List<StudentAnswerEntity> subjectiveQuestionEntities = new ArrayList<>();


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<StudentAnswerEntity> page = this.page(
                new Query<StudentAnswerEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    public boolean questionSubmit(StudentAnswerEntity studentAnswerEntity) {
        if (studentAnswerEntity.getQuestionType() == 1){
            QueryWrapper<FillQuestionEntity> wrapper = new QueryWrapper<>();
            wrapper.eq("questionId" , studentAnswerEntity.getQuestionid());
            studentAnswerEntity.setRightAnswer(fillQuestionService.getOne(wrapper).getAnswer());
            fillQuestionEntities.add(studentAnswerEntity);
        }else if (studentAnswerEntity.getQuestionType() == 2){
            QueryWrapper<JudgeQuestionEntity> wrapper = new QueryWrapper<>();
            JudgeQuestionEntity judgeQuestionEntity = judgeQuestionService.getOne(wrapper.eq("questionId", studentAnswerEntity.getQuestionid()));
            studentAnswerEntity.setRightAnswer(judgeQuestionEntity.getAnswer());
            judgeQuestionEntities.add(studentAnswerEntity);
        }else if (studentAnswerEntity.getQuestionType() == 3){
            QueryWrapper<MultiQuestionEntity> wrapper = new QueryWrapper<>();
            MultiQuestionEntity multiQuestionEntity = multiQuestionService.getOne(wrapper.eq("questionId", studentAnswerEntity.getQuestionid()));
            studentAnswerEntity.setRightAnswer(multiQuestionEntity.getRightanswer());
            multiQuestionEntities.add(studentAnswerEntity);
        }else if (studentAnswerEntity.getQuestionType() == 4){
            QueryWrapper<SubjectiveQuestionEntity> wrapper = new QueryWrapper<>();
            wrapper.eq("questionId" , studentAnswerEntity.getQuestionid());
            studentAnswerEntity.setRightAnswer(subjectiveQuestionService.getOne(wrapper).getAnswer());
            subjectiveQuestionEntities.add(studentAnswerEntity);
        }
        studentAnswerEntity.setQuestionStatus(0);

       return this.save(studentAnswerEntity);
    }

    @Override
    public Map<String , List<StudentAnswerEntity>> getPaper() {
        map.put("fillQuestions", fillQuestionEntities);
        map.put("judgeQuestions",judgeQuestionEntities);
        map.put("multiQuestions",multiQuestionEntities);
        map.put("subjectiveQuestions", subjectiveQuestionEntities);
        return map;
    }


    @Override
    public List<StudentAnswerEntity> questionsCorrected(Double studentId , Double paperId) {
        QueryWrapper<StudentAnswerEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("studentId" , studentId)
                .eq("paperId" , paperId)
                .eq("question_status" , 1);
        return this.list(wrapper);
    }

    @Override
    public List<StudentAnswerEntity> questionsWaitingCorrect(Double studentId , Double paperId) {
        QueryWrapper<StudentAnswerEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("studentId" , studentId)
                .eq("paperId" , paperId)
                .eq("question_status" , 0);
        List<StudentAnswerEntity> studentAnswerEntities = this.list(wrapper);
        paper.addAll(studentAnswerEntities);
        return paper;
    }

    @Override
    public R autoJudge() {
        for (StudentAnswerEntity studentAnswerEntity: paper) {
            if (studentAnswerEntity.getQuestionType() == 1 ||studentAnswerEntity.getQuestionType() == 4){
                continue;
            }
            //自动改判断题
            if (studentAnswerEntity.getQuestionType() == 2){
                QueryWrapper<JudgeQuestionEntity> wrapper = new QueryWrapper<>();
                JudgeQuestionEntity judgeQuestionEntity = judgeQuestionService.getOne(wrapper.eq("questionId", studentAnswerEntity.getQuestionid()));
                if (studentAnswerEntity.getUserAnswer().equalsIgnoreCase(studentAnswerEntity.getRightAnswer())){
                    studentAnswerEntity.setUserScore(judgeQuestionEntity.getScore());
                }else {
                    studentAnswerEntity.setUserScore(0.0);
                }
                etScore+=studentAnswerEntity.getUserScore();
                studentAnswerEntity.setQuestionStatus(1);
                this.update(studentAnswerEntity,new QueryWrapper<StudentAnswerEntity>().eq("id" , studentAnswerEntity.getId()));
            }
            //自动改选择题
            else if (studentAnswerEntity.getQuestionType() == 3){
                QueryWrapper<MultiQuestionEntity> wrapper = new QueryWrapper<>();
                MultiQuestionEntity multiQuestionEntity = multiQuestionService.getOne(wrapper.eq("questionId", studentAnswerEntity.getQuestionid()));
                if (studentAnswerEntity.getUserAnswer().equalsIgnoreCase(studentAnswerEntity.getRightAnswer())){
                    studentAnswerEntity.setUserScore(multiQuestionEntity.getScore());
                }else {
                    studentAnswerEntity.setUserScore(0.0);
                }
                etScore+=studentAnswerEntity.getUserScore();
                studentAnswerEntity.setQuestionStatus(1);
                this.update(studentAnswerEntity,new QueryWrapper<StudentAnswerEntity>().eq("id" , studentAnswerEntity.getId()));
            }
        }
        return R.ok();
    }

    @Override
    public R handJudge(List<Double> scores) {
        for (StudentAnswerEntity studentAnswerEntity: paper) {
            if (studentAnswerEntity.getQuestionType() == 2 || studentAnswerEntity.getQuestionType() == 3){
                continue;
            }
            studentAnswerEntity.setUserScore(scores.get(0));
            etScore+=scores.get(0);
            scores.remove(0);
            studentAnswerEntity.setQuestionStatus(1);
            this.update(studentAnswerEntity,new QueryWrapper<StudentAnswerEntity>().eq("id" , studentAnswerEntity.getId()));
        }
        return R.ok();
    }

    @Override
    public List<StudentAnswerEntity> allWaitingCorrect(Double paperId) {
        QueryWrapper<StudentAnswerEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("paperId" , paperId)
                .eq("question_status" , 0);
        List<StudentAnswerEntity> studentAnswerEntities = this.list(wrapper);
        questionsWaitingCorrect.addAll(studentAnswerEntities);
        return questionsWaitingCorrect;
    }

    @Override
    public IPage<StudentAnswerEntity> studentAnswersInfo(Integer page, Integer size) {
        return this.page(new Page<>(page,size));
    }


}