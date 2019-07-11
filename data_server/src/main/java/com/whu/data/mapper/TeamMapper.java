
//作者：张步云

package com.whu.data.mapper;
import java.util.Date;
import com.sun.org.apache.xpath.internal.operations.Bool;
import com.whu.data.pojo.*;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

@Mapper
public interface TeamMapper
{
    Boolean addSaveShare(String username, String sharenum);

    Share getShareByShareNum(String shareNum);

    List<Share> getSaveShareByUserName(String username);

    Boolean registerUser(String username);

    User getUserByName(String username);

    Boolean deleteSaveShare(String username, String shareNum);

    List<Share> getSharesByShareName(String shareName);

    List<Message> getMessageList(String username);

    Boolean sendSuggestion(String username, String content);

    Boolean insertForecastInfo(String sharenum, float closeprice);

    //此方法不是和前面设置mapping为getForecastInfo方法
    //是和向数据库中插入数预测数据的那个方法相互匹配的方法
    ForecastInfo getForecastInfoByShareNum(String sharenum);

    Boolean insertMessageIntoMysql(String username, String sharenum, String sharename,String market, String content, String date, int isread);

    Boolean changeIsReadByUserName(String username);

    Float getClosePriceFromMysql(String sharenum);

    boolean setShareIsPrint(String sharenum, int isPrint);

    int getShareIsPrint(String sharenum);

    Boolean clearDBIsPrint();

    List<String> getAllSaveShareList();

    List<String> getUserSaveShareForecastList(String username);

    forecast getNowPriceByShareNum(String sharenum);

    List<User> getAllUsers();



}
