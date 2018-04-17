package cn.chenhuamou.im.server.response;

/**
 * Created by AMing on 16/1/18.
 * Company RongCloud
 */
public class LotteryInfoResponse {


    private String CurrentIssueNo;
    private String PreviewIssueNo;
    private long RemainTime;
    private IssueEntity Issue;

    public String getCurrentIssueNo() {
        return CurrentIssueNo;
    }

    public void setCurrentIssueNo(String currentIssueNo) {
        CurrentIssueNo = currentIssueNo;
    }

    public String getPreviewIssueNo() {
        return PreviewIssueNo;
    }

    public void setPreviewIssueNo(String previewIssueNo) {
        PreviewIssueNo = previewIssueNo;
    }

    public long getRemainTime() {
        return RemainTime;
    }

    public void setRemainTime(long remainTime) {
        RemainTime = remainTime;
    }

    public IssueEntity getIssue() {
        return Issue;
    }

    public void setIssue(IssueEntity issue) {
        Issue = issue;
    }

    public class IssueEntity {

        /**
         * IssueNo : string
         * BeginTime : 2018-04-17T01:19:32.542Z
         * EndTime : 2018-04-17T01:19:32.542Z
         * BeginTime1 : 2018-04-17T01:19:32.542Z
         * EndTime1 : 2018-04-17T01:19:32.542Z
         * LotteryCode : string
         * MaxIssue : 0
         * MinIssue : 0
         * Crossdays : true
         * LotteryTypeCode : string
         * NowIssueDay : 2018-04-17T01:19:32.542Z
         */

        private String IssueNo;
        private String BeginTime;
        private String EndTime;
        private String BeginTime1;
        private String EndTime1;
        private String LotteryCode;
        private int MaxIssue;
        private int MinIssue;
        private boolean Crossdays;
        private String LotteryTypeCode;
        private String NowIssueDay;

        public String getIssueNo() {
            return IssueNo;
        }

        public void setIssueNo(String IssueNo) {
            this.IssueNo = IssueNo;
        }

        public String getBeginTime() {
            return BeginTime;
        }

        public void setBeginTime(String BeginTime) {
            this.BeginTime = BeginTime;
        }

        public String getEndTime() {
            return EndTime;
        }

        public void setEndTime(String EndTime) {
            this.EndTime = EndTime;
        }

        public String getBeginTime1() {
            return BeginTime1;
        }

        public void setBeginTime1(String BeginTime1) {
            this.BeginTime1 = BeginTime1;
        }

        public String getEndTime1() {
            return EndTime1;
        }

        public void setEndTime1(String EndTime1) {
            this.EndTime1 = EndTime1;
        }

        public String getLotteryCode() {
            return LotteryCode;
        }

        public void setLotteryCode(String LotteryCode) {
            this.LotteryCode = LotteryCode;
        }

        public int getMaxIssue() {
            return MaxIssue;
        }

        public void setMaxIssue(int MaxIssue) {
            this.MaxIssue = MaxIssue;
        }

        public int getMinIssue() {
            return MinIssue;
        }

        public void setMinIssue(int MinIssue) {
            this.MinIssue = MinIssue;
        }

        public boolean isCrossdays() {
            return Crossdays;
        }

        public void setCrossdays(boolean Crossdays) {
            this.Crossdays = Crossdays;
        }

        public String getLotteryTypeCode() {
            return LotteryTypeCode;
        }

        public void setLotteryTypeCode(String LotteryTypeCode) {
            this.LotteryTypeCode = LotteryTypeCode;
        }

        public String getNowIssueDay() {
            return NowIssueDay;
        }

        public void setNowIssueDay(String NowIssueDay) {
            this.NowIssueDay = NowIssueDay;
        }
    }
}
