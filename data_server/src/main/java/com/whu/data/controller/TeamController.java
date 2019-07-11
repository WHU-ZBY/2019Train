
//作者：张步云

package com.whu.data.controller;

import com.whu.data.pojo.*;
import com.whu.data.service.TeamService;
import javafx.application.Preloader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.Date;
import java.text.SimpleDateFormat;

import java.io.BufferedReader;
import  java.io.InputStreamReader;



@Controller
public class TeamController
{
    @Autowired
    private TeamService teamService;

    @CrossOrigin
    @ResponseBody
    @RequestMapping("/searchShareByShareNum")
    public Share SearchShareByShareNum(String shareNum){

        return teamService.searchShareByShareNum(shareNum);
    }


    @CrossOrigin
    @ResponseBody
    @RequestMapping("/searchSaveShareByUserName")
    public List<SaveShareInfo> SearchSaveShareByUserName(String username)
    {

        List<SaveShareInfo> myList=new ArrayList<>();
        List<Share> shareList=teamService.searchSaveShareByUserName(username);

        System.out.println(shareList);

//getForecastInfoByShareNum方法返回一个ForecastInfo对象，中获取closeprice
        for(int i = 0 ; i < shareList.size() ; i++) {

            String num = shareList.get(i).shareNum;

            float closeprice=0;

            closeprice = getForecastInfoByShareNum(num).getCloseprice();

            Share share = SearchShareByShareNum(num);
            SaveShareInfo temp = new SaveShareInfo();
            temp.shareNum=share.shareNum;
            temp.shareName=share.shareName;
            temp.market=share.market;
            temp.forecast=closeprice;
            myList.add(temp);

        }

        return myList;
    }

    @CrossOrigin
    @ResponseBody
    @RequestMapping("/registerUser")
    public void RegisterUser(String username){
        User user1 = teamService.GetUserByName(username);

        if( user1 == null)
        {
            teamService.RegisterUser(username);
        }
        else {
            System.out.println("该用户已经创建");
        }
    }


    @CrossOrigin
    @ResponseBody
    @RequestMapping("/getUserByName")
    public User GetUserByName(String username){

        return  teamService.GetUserByName(username);
    }

    @CrossOrigin
    @ResponseBody
    @RequestMapping("/deleteSaveShare")
    public Boolean DeleteSaveShare(String username, String shareNum){

        return  teamService.deleteSaveShare(username, shareNum);
    }

    @CrossOrigin
    @ResponseBody
    @RequestMapping("/getSharesByShareName")
    public List<Share> GetSharesByShareName(String sharename){

        return teamService.getSharesByShareName(sharename);
    }
    @CrossOrigin
    @ResponseBody
    @RequestMapping("/addSaveShare")
    public Boolean AddSaveShare(String username,String sharenum)

    {
        Boolean a =teamService.addSaveShare(username,sharenum);
        Share temp = teamService.searchShareByShareNum(sharenum);
        GetForecastInfo(temp.shareName,temp.shareNum,temp.market);
        if (a)
            return true;
        else
            return false;
    }


    @CrossOrigin
    @ResponseBody
    @RequestMapping("/getMessageByUserName")
    public List<Message> getMessageByUserName(String username)

    {
        List<Message> list= teamService.getMessageByUserName(username);
        Collections.reverse(list);
        return  list;
    }


    @CrossOrigin
    @ResponseBody
    @RequestMapping("/sendSuggestion")
    public Boolean SendSuggestion(String username, String content)
    {
        return teamService.sendSuggestion(username,content);
    }

//    以下主要是和python代码相结合，完成预测股票价格部分
//    首先是从前端获得股票的编码、简称、market，并对其进行处理
    @CrossOrigin
    @ResponseBody
    @RequestMapping("/getForecastInfo")
    public float GetForecastInfo(String sharename, String sharenum, String market)
    {
        String sh="sh";
        String sz="sz";
        String hk="hk";
        int num=0;

        String newShareNum=null;
        String newShareName=sharename;
        String tx="00700";

        try{
             num = getShareIsPrint(sharenum);

        }catch (Exception e)
        {
            System.out.println("无法获取到isprint");
        }


        if (market.equals(sh))
        {
            newShareNum=sharenum+'.'+market;

        }
        else if (market.equals(sz))
        {
            newShareNum=sharenum+'.'+market;

        }
        else if (market.equals(hk))
        {
//

            newShareNum=sharenum+'.'+market;

            if(sharenum.equals(tx))
                newShareName="腾讯";
        }

        if(num!=1)
        {

            usePy(newShareName,newShareNum);
            setShareIsPrint(sharenum,1);

        }


        Float closeprice=getClosePriceFromMysql(sharenum);



//        其中newShareName和newShareNum是要输入python脚本中的数据
//        然后就是调用相关的数据了
        return closeprice;

    }

//    预测的信息应该可以直接发给前端，这两个方法备用
//    将预测的信息存储到数据库中
    @CrossOrigin
    @ResponseBody
    @RequestMapping("/insertForecastInfo")
    public Boolean insertForecastInfo(String sharenum, float closeprice)
    {
        return teamService.insertForecastInfo(sharenum,closeprice);
    }
    @CrossOrigin
    @ResponseBody
    @RequestMapping("/getForecastInfoByShareNum")
    public ForecastInfo getForecastInfoByShareNum(String sharenum)
    {
        return  teamService.getForecastInfoByShareNum(sharenum);
    }
    @CrossOrigin
    @ResponseBody
    @RequestMapping("/insertMessageIntoMysql")
    public Boolean insertMessageIntoMysql(String username, String sharenum, String sharename,String market, String content, String date, int isread)
    {

        return teamService.insertMessageIntoMysql(username,sharenum,sharename,market,content,date,isread);
    }
    @CrossOrigin
    @ResponseBody
    @RequestMapping("/changeIsReadByUserName")
    public Boolean changeIsReadByUserName(String username)
    {
        return teamService.changeIsReadByUserName(username);
    }

    @CrossOrigin
    @ResponseBody
    @RequestMapping("/usePy")
    public void usePy(String sharename,String newsharenum)
    {
        Process proc;
        try{

            String[] args1 = new String[]{"python","C:\\server\\PyServer\\start.py",sharename,newsharenum};

            proc =Runtime.getRuntime().exec(args1);
            BufferedReader in =new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line = null;
            while ((line=in.readLine())!=null)
            {
                System.out.println(line);
            }
            in.close();
            proc.waitFor();



        } catch (IOException e) {
            System.out.println("失败1");
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.out.println("失败2");
            e.printStackTrace();
        }

    }

    public float getClosePriceFromMysql(String sharenum)

    {
        return teamService.getClosePriceFromMysql(sharenum);
    }


    @CrossOrigin
    @ResponseBody
    @RequestMapping("/setShareIsPrint")
    public boolean setShareIsPrint(String sharenum,int isPrint)
    {
        return  teamService.setShareIsPrint(sharenum,isPrint);
    }

    @CrossOrigin
    @ResponseBody
    @RequestMapping("/getShareIsPrint")
    public int getShareIsPrint(String sharenum)
    {
        return teamService.getShareIsPrint(sharenum);
    }

////定时执行，将数据库中的记录IsPrint数据的值全部归为零
    @Scheduled(cron = "0 5 1 * * ?")
//    @ResponseBody
//    @RequestMapping("/fixTimeExecution")
    public Boolean fixTimeExecution(){
        System.out.println("定时执行清除数据库中的isprint数据的操作");
        return teamService.clearDBIsPrint();
    }

//    将用户关注的股票在凌晨的时候预测一遍(注意周日的时候没有数据！)
//    主要注意的主要有，不同用户关注同一只股票，使用isprintdata表判断是否需要在预测？
//    获取到关注列表中不重复的sharenum
    @CrossOrigin
    @ResponseBody
    @RequestMapping("/getAllSaveShare")
    public List<String> getAllSaveShare(){

        List<String> list1= teamService.getAllSaveShare();
        HashSet hashSet1=new HashSet(list1);
        list1.clear();
        list1.addAll(hashSet1);
        return list1;
    }

//    该函数应该是每天晚上凌晨的时候运行，收集数据
    @Scheduled(cron = "0 15 1 * * ?")
//    @ResponseBody
//    @RequestMapping("/proForecastShares")
    public void proForecastShares(){
        System.out.println("定时预测收藏股票的信息");
        List<String> list1 = getAllSaveShare();
        int len = list1.size();
        for( int i=0;i<len;i++)
        {

            Share share = SearchShareByShareNum(list1.get(i));
            GetForecastInfo(share.shareName,share.shareNum,share.market);

        }
    }


//    在此处需要实现，从用户的关注列表里面获取用户关注的股票号码，拿到每一个股票的nowprice(实际上是
    @CrossOrigin
    @ResponseBody
    @RequestMapping("/getUserSaveShareForecastList")
    public List<forecast> getUserSaveShareForecastList(String username)
    {

        //拿到sharenumList

        List<String> list = teamService.getUserSaveShareForecastList(username);


        List<forecast> list1 =new ArrayList<>() ;

        for (int i=0;i<list.size();i++)
        {

            forecast item = teamService.getNowPriceByShareNum(list.get(i));


            list1.add(item);

        }
        System.out.println(list1);
        return list1;

    }
//    获取互联网时间
    public String getWebTime()
    {
        String webUrl="http://www.baidu.com";//百度时间
        String webTime=getNetworkTime(webUrl);
        System.out.println("当前网络时间为："+webTime);
        return webTime;
    }
    public String getNetworkTime(String webUrl) {
        try {
            URL url=new URL(webUrl);
            URLConnection conn=url.openConnection();
            conn.connect();
            long dateL=conn.getDate();
            Date date=new Date(dateL);
            SimpleDateFormat dateFormat=new SimpleDateFormat("YYYY-MM-dd HH:mm");
            return dateFormat.format(date);
        }catch (MalformedURLException e) {
            e.printStackTrace();
        }catch (IOException e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return "";
    }

//    股票的变化值，根据变化值判断发送的消息调用插入mysql方法)
    @CrossOrigin
    @ResponseBody
    @RequestMapping("/manageMessage")
    public void ManageMessage(String username)
    {

//        获取到了关注的股票的信息(sharenum,closeprice,nowprice)
        List<forecast> list1 = getUserSaveShareForecastList(username);

        String webTime = getWebTime();

        String[] time = webTime.split(" ");

        String time1= time[0].replaceAll(time[0].substring(0,5),"");
        for (int i=0;i<list1.size();i++)
        {

            forecast item = list1.get(i);
            Share share = SearchShareByShareNum(item.getSharenum());

            String content1 = time[1]+"  关注的股票价格正在增长！";
            String content2 = time[1]+"  股价正在暴跌！";
            if(item.getNowprice()>1)
            {
                insertMessageIntoMysql(username,item.getSharenum(),share.shareName,share.market,content1,time1,0);

            }

            if (item.getNowprice()<-1)
            {
                insertMessageIntoMysql(username,item.getSharenum(),share.shareName,share.market,content2,time1,0);


            }
        }

    }
    @Scheduled(cron = "0 55 1 * * ?")
//    @ResponseBody
//    @RequestMapping("/fixTimeInsertMessage")
    public void fixTimeInsertMessage()
    {
        System.out.println("定时向数据库中插入message");
        List<User> list  =teamService.getAllUsers();


        for (int i=0;i<list.size();i++)
        {
            String username=list.get(i).getUsername();
            ManageMessage(username);
        }
    }

}
