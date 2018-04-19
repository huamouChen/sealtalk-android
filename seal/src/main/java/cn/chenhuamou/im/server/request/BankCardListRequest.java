package cn.chenhuamou.im.server.request;

/**
 * Created by AMing on 16/1/25.
 * Company RongCloud
 * 获取银行卡列表
 */
public class BankCardListRequest {


    public BankCardListRequest(int pageIndex, int pageSize) {
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
    }

    private int pageIndex;
    private int pageSize;

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}

