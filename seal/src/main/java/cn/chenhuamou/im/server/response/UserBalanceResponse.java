package cn.chenhuamou.im.server.response;

/**
 * Created by AMing on 16/1/18.
 * Company RongCloud
 */
public class UserBalanceResponse {


    /**
     * UserMoney :    彩票账户
     * AgMoney : 0    真人账户
     * TexMoney : 0   棋牌账户
     * PtMoney : 0    老虎机账户
     */

    private int UserMoney;
    private int AgMoney;
    private int TexMoney;
    private int PtMoney;

    public int getUserMoney() {
        return UserMoney;
    }

    public void setUserMoney(int UserMoney) {
        this.UserMoney = UserMoney;
    }

    public int getAgMoney() {
        return AgMoney;
    }

    public void setAgMoney(int AgMoney) {
        this.AgMoney = AgMoney;
    }

    public int getTexMoney() {
        return TexMoney;
    }

    public void setTexMoney(int TexMoney) {
        this.TexMoney = TexMoney;
    }

    public int getPtMoney() {
        return PtMoney;
    }

    public void setPtMoney(int PtMoney) {
        this.PtMoney = PtMoney;
    }
}
