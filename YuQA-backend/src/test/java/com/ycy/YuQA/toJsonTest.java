package com.ycy.YuQA;

import cn.hutool.json.JSONUtil;
import com.ycy.YuQA.model.dto.question.QuestionContentDTO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class toJsonTest {


    @Test
    public void test(){
        String str = "[\\n{\"options\":[{\"value\":\"应用名称是自定义MBTI性格测试\",\"key\":\"A\"},{\"value\":\"应用类别是游戏类\",\"key\":\"B\"}],\"title\":\"以下哪项描述正确？\"}\n]";
        System.out.println(str);
//        List<QuestionContentDTO> questionContentDTOList = JSONUtil.toList(str, QuestionContentDTO.class);
//        for (QuestionContentDTO questionContentDTO : questionContentDTOList) {
//            System.out.println(questionContentDTO);
//        }
    }

}