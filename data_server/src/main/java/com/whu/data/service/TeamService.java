
//作者：张步云

package com.whu.data.service;

import com.whu.data.mapper.TeamMapper;
import com.whu.data.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import java.util.Date;

@Service
public class TeamService
{
    @Autowired
    private TeamMapper teamMapper;

    public Share searchShareByShareNum(String shareNum)
    {
        return teamMapper.getShareByShareNum(shareNum);
    }


    public List<Share> searchSaveShareByUserName(String username){
        return teamMapper.getSaveShareByUserName(username);
    }

    public Boolean RegisterUser(String username){

        return teamMapper.registerUser(username);
    }

    public  User GetUserByName(String username)

    {
        return  teamMapper.getUserByName(username);
    }

    public Boolean deleteSaveShare(String username, String shareNum)
    {
       return teamMapper.deleteSaveShare(username, shareNum);
    }

    public List<Share> getSharesByShareName(String sharename){

        return teamMapper.getSharesByShareName(sharename);
    }

    public Boolean addSaveShare(String username, String sharenum){
        return teamMapper.addSaveShare(username,sharenum);
    }

    public List<Message> getMessageByUserName(String username)
    {
        return teamMapper.getMessageList(username);
    }


    public Boolean sendSuggestion(String username,String content)
    {
        return teamMapper.sendSuggestion(username,content);
    }

    public Boolean insertForecastInfo(String sharenum, float closeprice)
    {
        return  teamMapper.insertForecastInfo(sharenum,closeprice);
    }

    public ForecastInfo getForecastInfoByShareNum(String sharenum)
    {
         ForecastInfo mycase = new ForecastInfo();
         mycase.setCloseprice(0);
         mycase.setSharenum("000000");
         ForecastInfo temp=teamMapper.getForecastInfoByShareNum(sharenum);
         if (temp==null)
         {
             return mycase;
         }
         else
             return temp;
    }

    public Boolean insertMessageIntoMysql(String username, String sharenum, String sharename,String market, String content, String date, int isread)
    {
        return teamMapper.insertMessageIntoMysql(username,sharenum,sharename,market,content,date,isread);
    }

    public Boolean changeIsReadByUserName(String username)

    {
        return  teamMapper.changeIsReadByUserName(username);
    }

    public Float getClosePriceFromMysql(String sharenum)
    {
        return teamMapper.getClosePriceFromMysql(sharenum);
    }

    public boolean setShareIsPrint(String sharenum, int isPrint)
    {
        return teamMapper.setShareIsPrint(sharenum,isPrint);
    }

    public int getShareIsPrint(String sharenum)
    {
        return teamMapper.getShareIsPrint(sharenum);
    }

    public Boolean clearDBIsPrint(){
        return teamMapper.clearDBIsPrint();
    }

    public List<String> getAllSaveShare(){
        return teamMapper.getAllSaveShareList();
    }

    public List<String> getUserSaveShareForecastList(String username)
    {
        return teamMapper.getUserSaveShareForecastList(username);
    }
    public forecast getNowPriceByShareNum(String sharenum)
    {
        return teamMapper.getNowPriceByShareNum(sharenum);
    }

    public List<User> getAllUsers(){
        return teamMapper.getAllUsers();
    }

}
