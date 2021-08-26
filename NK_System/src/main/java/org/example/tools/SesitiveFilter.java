package org.example.tools;


import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;


/**
 * 敏感词过滤,利用前缀树
 */
@Component
public class SesitiveFilter {
    //定义日志记录对象
    private static final Logger logger= LoggerFactory.getLogger(SesitiveFilter.class);

    //替换敏感词的字符串常量
    private static final String REPLACE="***";

    //初始化树的根节点
    private  TrieNode rootNode=new TrieNode();

    //@PostConstruct 标识的方法表示初始化方法,
    //即Spring容器在初始化Bean后,也就是调用构造方法后会调用这个初始化方法
    @PostConstruct
    public void init(){
        try(
            InputStream in = this.getClass().getClassLoader().getResourceAsStream("SensitiveWord.txt");
            BufferedReader reader=new BufferedReader(new InputStreamReader(in));
        ) {
            String keyword;
            while ((keyword=reader.readLine())!=null){
                //添加到前缀树
                this.addTrim(keyword);


            }

        }catch (IOException e){
            logger.error("加载敏感词失败:"+e.getMessage());
        }
    }
   //将敏感词添加到前缀树
    private void addTrim(String keyword){
        TrieNode tempNode=rootNode;
        //遍历敏感词
        for (int i=0;i<keyword.length();i++){
            char c=keyword.charAt(i);
            TrieNode node = tempNode.getNode(c);
            if(node==null){
                //初始化子节点
                node=new TrieNode();
                tempNode.addNode(c,node);
            }
            //指针指向子节点,进入下一轮循环
            tempNode=node;
            //设置结束标记
            if(i==keyword.length()-1){
                tempNode.setKeywordend(true);
            }
        }

    }
   //过滤敏感词的方法
  public String filterString(String text){
        if(StringUtils.isBlank(text)){
            return null;
        }
        //前缀树指针1
        TrieNode tempNode=rootNode;
        //字符串指针2
        int bigin=0;
        //字符串指针3
        int posetion=0;
        //过滤的结果字符串
      StringBuilder bs=new StringBuilder();
      while (posetion<text.length()){ //遍历原来字符串
          char c=text.charAt(posetion);
          if(isSyblol(c)){ //判断是否是特殊字符
              //若指针1处于根节点，将此字符记录结果，让指针往下走
              if(tempNode==rootNode){
                  bs.append(c);
                  bigin++;
              }
              //无论特殊符号在那，指针3都往下走
              posetion++;
              continue;
          }
          //检查下级节点
           tempNode = tempNode.getNode(c);
          if(tempNode==null){
              //以bigin开头的不是敏感字符
              bs.append(text.charAt(bigin));
              posetion=++bigin; //往下走
              //指针1归位
             tempNode=rootNode;
          }else if(tempNode.isKeywordend()){
              //发现敏感词
              bs.append(REPLACE);
              bigin=++posetion;
              tempNode=rootNode;

          }else {
              //继续向下检查
              posetion++;
          }
      }
      //将最后一批字符记录(指针二还没到达终点，指针三到达终点了)
      bs.append(text.substring(bigin));
      return bs.toString();
  }
  //判断特殊符号
  private boolean isSyblol(Character c){
        //0x2E80 ~ c>0x9FFF 是东亚文字范围
        return !CharUtils.isAsciiAlphanumeric(c) && (c<0x2E80 || c>0x9FFF) ;
  }

    //定义前缀树(字典树）
    private  class TrieNode{
        //是否为最后一个节点，结束的标识
        private  boolean isKeywordend=false;

        //子节点==>(key是下级字符，value是下级节点）
        private Map<Character,TrieNode> childNode=new HashMap<>();

        //添加子节点
        public void  addNode(Character c,TrieNode node){
            childNode.put(c,node);

        }
        //获取子节点
        public TrieNode getNode(Character c){
            return childNode.get(c);
        }

        public boolean isKeywordend() {
            return isKeywordend;
        }

        public void setKeywordend(boolean keywordend) {
            isKeywordend = keywordend;
        }
    }
}
