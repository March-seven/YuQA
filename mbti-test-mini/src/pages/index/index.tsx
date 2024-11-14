import {Image,View} from "@tarojs/components";
import {AtButton} from "taro-ui";
import Taro from "@tarojs/taro";

import './index.scss'
import GlobalFooter from "../../components/GlobalFooter";
import headerBg from '../../assets/headerBg.png';

export default ()=>{
  return (
    <View className='indexPage'>
      <View className='at-article__h1 title'>MBTI 性格测试</View>
      <View className='at-article__h2 subTitle'>
        只需要两分钟，就能非常准去的描述你的性格
      </View>
      <AtButton type='primary' className='enterBtn' circle onClick={()=>{
        Taro.navigateTo({
          url: '/pages/doQuestion/index'
        })
      }}
      >
        开始测试
      </AtButton>
      <Image className='headerBg' style='width: 100vw' src={headerBg}/>
      <GlobalFooter />
    </View>
  )
}
