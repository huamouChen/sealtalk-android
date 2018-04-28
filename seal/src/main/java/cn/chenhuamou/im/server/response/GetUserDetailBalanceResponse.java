package cn.chenhuamou.im.server.response;

/**
 * Created by Rex on 2018/4/28.
 * Email chenhm4444@gmail.com
 */
public class GetUserDetailBalanceResponse {

    /**
     * UserMoney : 0
     * AgMoney : 0
     * TexMoney : 0
     * PtMoney : 0
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
