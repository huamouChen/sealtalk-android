package cn.chenhuamou.im.server.request;

/**
 * Created by AMing on 16/1/25.
 * Company RongCloud
 */
public class GetUserCashRequest {

    public GetUserCashRequest(String userName, String startDate, String endDate, int pageSize, int pageIndex) {
        this.userName = userName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.pageSize = pageSize;
        this.pageIndex = pageIndex;
    }

    private String userName;
    private String startDate;
    private String endDate;
    private int pageSize;
    private int pageIndex;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }
}

