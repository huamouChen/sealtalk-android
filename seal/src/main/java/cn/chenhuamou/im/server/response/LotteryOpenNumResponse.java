package cn.chenhuamou.im.server.response;

/**
 * Created by AMing on 16/1/18.
 * Company RongCloud
 */
public class LotteryOpenNumResponse {


    /**
     * LotteryOpenNo : string
     * IssueNo : string
     * OpenTime : 2018-04-17T01:19:32.562Z
     * LotteryCode : string
     * LotteryName : string
     * ID : 0
     */

    private String LotteryOpenNo;
    private String IssueNo;
    private String OpenTime;
    private String LotteryCode;
    private String LotteryName;
    private int ID;

    public String getLotteryOpenNo() {
        return LotteryOpenNo;
    }

    public void setLotteryOpenNo(String LotteryOpenNo) {
        this.LotteryOpenNo = LotteryOpenNo;
    }

    public String getIssueNo() {
        return IssueNo;
    }

    public void setIssueNo(String IssueNo) {
        this.IssueNo = IssueNo;
    }

    public String getOpenTime() {
        return OpenTime;
    }

    public void setOpenTime(String OpenTime) {
        this.OpenTime = OpenTime;
    }

    public String getLotteryCode() {
        return LotteryCode;
    }

    public void setLotteryCode(String LotteryCode) {
        this.LotteryCode = LotteryCode;
    }

    public String getLotteryName() {
        return LotteryName;
    }

    public void setLotteryName(String LotteryName) {
        this.LotteryName = LotteryName;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}
