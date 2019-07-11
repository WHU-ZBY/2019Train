package com.whu.data;

//作者：张步云

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import com.whu.data.controller.TeamController;
import com.whu.data.pojo.*;
import java.util.*;



@RunWith(SpringRunner.class)
@SpringBootTest
public class TeamControllerTest {
    @Autowired
    TeamController teamController;

    @Before
    public void setUp() throws Exception {
        System.out.println("执行初始化");
    }


    @Test
    public void testSearchShareByShareNum() throws Exception {
//TODO: Test goes here...
        String sharenum="000711";
        Share share=teamController.SearchShareByShareNum(sharenum);
        System.out.println(share);
        if (share!=null)
        {
            System.out.println("get the share!");
        }
        else
        {
            System.out.println("failed to get the share!");
        }

    }

    /**
     *
     * Method: SearchSaveShareByUserName(String username)
     *
     */
    @Test
    public void testSearchSaveShareByUserName() throws Exception {
//TODO: Test goes here...
        String username = "a";
//        数据库中存在a这个user，并且有saveshare，所以可以搜索到
        List<SaveShareInfo> shares =teamController.SearchSaveShareByUserName(username);
        System.out.println(shares);
        if (shares!=null)
        {
            System.out.println("get the saveshares!");

        }
        else{
            System.out.println("not get the saveshares");
        }
        username="b";//数据库中没有这个username，所以应该搜索不到
        try{
            teamController.SearchSaveShareByUserName(username);
        }catch (Exception e){
            System.out.println("not get this user");
        }

    }

    /**
     *
     * Method: RegisterUser(String username)
     *
     */
    @Test
    public void testRegisterUser() throws Exception {
//TODO: Test goes here...
        String username="abc";//数据库中不存在该用户名
        teamController.RegisterUser(username);
        username="a";//数据库中已经存在该用户名
        teamController.RegisterUser(username);
    }

    /**
     *
     * Method: GetUserByName(String username)
     *
     */
    @Test
    public void testGetUserByName() throws Exception {
//TODO: Test goes here...
        String username="aaa";//数据库中已经存在该用户
        User user1=teamController.GetUserByName(username);
        System.out.println(user1);
        username="bbb";//数据库中不存在该用户
        User user2=teamController.GetUserByName(username);
        System.out.println(user2);
        Assert.assertEquals(null,user2);

    }

    /**
     *
     * Method: DeleteSaveShare(String username, String shareNum)
     *
     */
    @Test
    public void testDeleteSaveShare() throws Exception {
//TODO: Test goes here...
        String username="a";
        String sharenum="00700";
        Boolean a=teamController.DeleteSaveShare(username,sharenum);
        Assert.assertEquals(true,a);
        sharenum="999999";
        Boolean b=teamController.DeleteSaveShare(username,sharenum);
        Assert.assertEquals(false,b);
    }

    /**
     *
     * Method: GetSharesByShareName(String sharename)
     *
     */
    @Test
    public void testGetSharesByShareName() throws Exception {
//TODO: Test goes here...
        String sharename="腾讯";
        List<Share> shares = teamController.GetSharesByShareName(sharename);
        System.out.println(shares);
        Assert.assertNotEquals(null,shares);

    }

    /**
     *
     * Method: AddSaveShare(String username, String sharenum)
     *
     */
    @Test
    public void testAddSaveShare() throws Exception {
//TODO: Test goes here...
        String username="a";
        String sharenum="00700";
        Boolean a =teamController.AddSaveShare(username,sharenum);
        Assert.assertEquals(true,a);

    }

    /**
     *
     * Method: getMessageByUserName(String username)
     *
     */
    @Test
    public void testGetMessageByUserName() throws Exception {
//TODO: Test goes here...
        String username="aaa";
        List<Message> list = teamController.getMessageByUserName(username);
        Assert.assertNotEquals(null,list);

    }

    /**
     *
     * Method: SendSuggestion(String username, String content)
     *
     */
    @Test
    public void testSendSuggestion() throws Exception {
//TODO: Test goes here...
        String username="aaa";
        String content= "today is very good!";
        Boolean a=teamController.SendSuggestion(username,content);
        Assert.assertEquals(true,a);

    }

    /**
     *
     * Method: GetForecastInfo(String sharename, String sharenum, String market)
     *
     */
    @Test
    public void testGetForecastInfo() throws Exception {
//TODO: Test goes here...
        String sharename="腾讯";
        String sharenum="00700";
        String market="hk";

        Float a=teamController.GetForecastInfo(sharename,sharenum,market);
        Assert.assertNotEquals(null,a);

    }

    /**
     *
     * Method: insertForecastInfo(String sharenum, float closeprice)
     *
     */
    @Test
    public void testInsertForecastInfo() throws Exception {
//TODO: Test goes here...
        String sharenum="99999";
        float closeprice= (float) 11.24301434;
        Boolean a =teamController.insertForecastInfo(sharenum,closeprice);
        Assert.assertEquals(true,a);

    }


    /**
     *
     * Method: getForecastInfoByShareNum(String sharenum)
     *
     */
    @Test
    public void testGetForecastInfoByShareNum() throws Exception {
//TODO: Test goes here...
        String sharenum="00700";
        ForecastInfo a = teamController.getForecastInfoByShareNum(sharenum);
        System.out.println(a);
        Assert.assertNotEquals(null,a);

    }

    /**
     *
     * Method: insertMessageIntoMysql(String username, String sharenum, String sharename, String market, String content, String date, int isread)
     *
     */
    @Test
    public void testInsertMessageIntoMysql() throws Exception {
//TODO: Test goes here...
        String username="a",sharenum="00700",sharename="腾讯", market="hk",  content="涨",  date="7-10";
        int isread=0;
        Boolean a = teamController.insertMessageIntoMysql(username,sharenum,sharename,market,content,date,isread);
        Assert.assertEquals(true,a);

    }

    /**
     *
     * Method: changeIsReadByUserName(String username)
     *
     */
    @Test
    public void testChangeIsReadByUserName() throws Exception {
//TODO: Test goes here...
        String username="a";
        Boolean a = teamController.changeIsReadByUserName(username);
        Assert.assertEquals(true,a);

    }

    /**
     *
     * Method: usePy(String sharename, String newsharenum)
     *
     */
    @Test
    public void testUsePy() throws Exception {
//TODO: Test goes here...
        String sharename="京蓝科技";
        String newsharenum="000711.sz";
        teamController.usePy(sharename,newsharenum);

    }

    /**
     *
     * Method: getClosePriceFromMysql(String sharenum)
     *
     */
    @Test
    public void testGetClosePriceFromMysql() throws Exception {
//TODO: Test goes here...
        String sharenum="00700";
        float a= teamController.getClosePriceFromMysql(sharenum);
        Assert.assertNotEquals(null,a);

    }

    /**
     *
     * Method: setShareIsPrint(String sharenum, int isPrint)
     *
     */
    @Test
    public void testSetShareIsPrint() throws Exception {
//TODO: Test goes here...
        String sharenum="00700";
        int isPrint=1;
        Boolean a = teamController.setShareIsPrint(sharenum,isPrint);
        Assert.assertEquals(true,a);

    }

    /**
     *
     * Method: getShareIsPrint(String sharenum)
     *
     */
    @Test
    public void testGetShareIsPrint() throws Exception {
//TODO: Test goes here...
        String sharenum="00700";
        int a = teamController.getShareIsPrint(sharenum);
        System.out.println(a);
        Assert.assertEquals(1,a);

    }

    /**
     *
     * Method: fixTimeExecution()
     *
     */
    @Test
    public void testFixTimeExecution() throws Exception {
//TODO: Test goes here...
        teamController.fixTimeExecution();
        System.out.println("数据库中IsPrint表中的数据全部变为0");
    }

    /**
     *
     * Method: getAllSaveShare()
     *
     */
    @Test
    public void testGetAllSaveShare() throws Exception {
//TODO: Test goes here...
        List<String> list = teamController.getAllSaveShare();
        System.out.println(list);
        Assert.assertNotEquals(null,list);
    }

    /**
     *
     * Method: proForecastShares()
     *
     */
    @Test
    public void testProForecastShares() throws Exception {
//TODO: Test goes here...
        teamController.proForecastShares();

    }

    /**
     *
     * Method: getUserSaveShareForecastList(String username)
     *
     */
    @Test
    public void testGetUserSaveShareForecastList() throws Exception {
//TODO: Test goes here...
        String username="a";//存在此用户，并且关注了股票
        List<forecast> list = teamController.getUserSaveShareForecastList(username);
        System.out.println(list);
        Assert.assertNotEquals(null,list);
        username="d";//不存在此用户
        try
        {
            list = teamController.getUserSaveShareForecastList(username);
        }
        catch (Exception e)
        {
            System.out.println("username is not true！");
        }

    }

    /**
     *
     * Method: getWebTime()
     *
     */
    @Test
    public void testGetWebTime() throws Exception {
//TODO: Test goes here...
        String time = teamController.getWebTime();
        System.out.println(time);
        Assert.assertNotEquals(null,time);
    }

    /**
     *
     * Method: getNetworkTime(String webUrl)
     *
     */
    @Test
    public void testGetNetworkTime() throws Exception {
//TODO: Test goes here...
        String webUrl= "http://www.baidu.com";
        String a = teamController.getNetworkTime(webUrl);
        System.out.println(a);
        Assert.assertNotEquals(null,a);

    }

    /**
     *
     * Method: ManageMessage(String username)
     *
     */
    @Test
    public void testManageMessage() throws Exception {
//TODO: Test goes here...
            String username="a";
            teamController.ManageMessage(username);
             System.out.println("数据库中的message表中的内容增加了相应的message");
    }

    /**
     *
     * Method: fixTimeInsertMessage()
     *
     */
    @Test
    public void testFixTimeInsertMessage() throws Exception {
//TODO: Test goes here...
        teamController.fixTimeInsertMessage();
        //通过查看数据库可以看到相应的数据已经插入了数据库（message的插入依据是股票的波动）
    }

}
