import {useEffect, useState} from "react";
import {View} from "@tarojs/components";
import {AtButton, AtRadio} from "taro-ui";
import Taro from "@tarojs/taro";
import GlobalFooter from "../../components/GlobalFooter";
import questions from "../../data/questions.json";
import './index.scss';
export default ()=>{
  // const question = questions[0];

  // 当前题目序号（从1开始）
  const [current,setCurrent]=useState<number>(1);

  // 当前题目
  const [currentQuestion,setCurrentQuestion]=useState<Question>(questions[0]);
  const questionOptions=currentQuestion.options.map(option=>{
    return {label:`${option.key}.${option.value}`,value:option.key}
  });

  // 当前答案
  const [currentAnswer,setCurrentAnswer]=useState<string>('');

  // 回答列表
  const [answerList]=useState<string[]>([]);
  // 序号变化时，切换当前题目和当前回答
  useEffect(()=>{
    setCurrentQuestion(questions[current-1]);
    setCurrentAnswer(answerList[current-1]);
  },[answerList, current]);


  return (
    <View className='doQuestionPage'>
      {/*{JSON.stringify(answerList)}*/}

      <View className='at-article__h1 title'>
        {current}.{currentQuestion.title}
      </View>
      <View className='options-wrapper'>
        <AtRadio
          options={questionOptions}
          value={currentAnswer}
          onClick={(value)=>{
            setCurrentAnswer(value);
            //记录仪回答
            answerList[current-1]=value;
          }}
        />
      </View>

      {current <questions.length && (
        <AtButton
          type='primary'
          circle
          className='controlBtn'
          disabled={!currentAnswer}
          onClick={()=>setCurrent(current+1)}
        >
          下一题
        </AtButton>
      )}

      {
        current == questions.length &&(
          <AtButton
            type='primary'
            circle
            className='controlBtn'
            disabled={!currentAnswer}
            onClick={()=>{
              // 传递答案
              Taro.setStorageSync('answerList',answerList);
              Taro.navigateTo({
                url: '/pages/result/index',
              })
            }}
          >
            查看结果
          </AtButton>
        )}
      {current > 1 && (
        <AtButton
          className='controlBtn'
          circle
          onClick={()=>setCurrent(current-1)}
        >
          上一题
        </AtButton>
      )}
      <GlobalFooter />
    </View>
  )
}
