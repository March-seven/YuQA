import Taro from "@tarojs/taro";
import {getBestQuestionResult} from "../../utils/bizUtils";
import {View,Image} from "@tarojs/components";
import {AtButton} from "taro-ui";
import headerBg from '../../assets/headerBg.png';
import GlobalFooter from "../../components/GlobalFooter";
import questions from "../../data/questions.json";
import questionResults from '../../data/question_result.json';

export default ()=>{


  const answerList = Taro.getStorageSync("answerList");
  if(!answerList || answerList.length<1){
    Taro.showToast({
      title: '答案为空',
      icon:'error',
      duration:3000,
    });
  }
  // 获取测试结果
  const result = getBestQuestionResult(answerList,questions,questionResults);
  return (
    <View className='resultPage'>
      <View className='at-article__h1 title'>{result.resultName}</View>
      <View className='at-article__h2 subTitle'>
        {result.resultDesc}
      </View>
      <AtButton type='primary' className='enterBtn' circle onClick={() => {
        Taro.reLaunch({url: '/pages/index/index'})
      }}
      >
        返回主页
      </AtButton>
      <Image className='headerBg' style='width: 100vw' src={headerBg} />
      <GlobalFooter />
    </View>
  )
}
