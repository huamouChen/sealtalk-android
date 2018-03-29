package cn.chenhuamou.im.server.response;

import java.util.List;

import cn.chenhuamou.im.db.Groups;

/**
 * Created by AMing on 15/12/24.
 * Company RongCloud
 */
public class GetRongGroupResponse {


    private List<Groups> Value;

    private ResultEntity Code;

    public List<Groups> getValue() {
        return Value;
    }

    public void setValue(List<Groups> value) {
        Value = value;
    }

    public ResultEntity getCode() {
        return Code;
    }

    public void setCode(ResultEntity code) {
        Code = code;
    }


    public static class ValueEntity {
        /**
         * Id : 0
         * GroupId : string
         * GroupName : string
         * GroupOwner : string
         * AddTime : 2018-03-22T10:12:05.737Z
         */

        private int Id;
        private String GroupId;
        private String GroupName;
        private String GroupOwner;
        private String AddTime;

        public int getId() {
            return Id;
        }

        public void setId(int Id) {
            this.Id = Id;
        }

        public String getGroupId() {
            return GroupId;
        }

        public void setGroupId(String GroupId) {
            this.GroupId = GroupId;
        }

        public String getGroupName() {
            return GroupName;
        }

        public void setGroupName(String GroupName) {
            this.GroupName = GroupName;
        }

        public String getGroupOwner() {
            return GroupOwner;
        }

        public void setGroupOwner(String GroupOwner) {
            this.GroupOwner = GroupOwner;
        }

        public String getAddTime() {
            return AddTime;
        }

        public void setAddTime(String AddTime) {
            this.AddTime = AddTime;
        }
    }

    public static class ResultEntity {
        /**
         * CodeId : string
         * Description : string
         */

        private String CodeId;
        private String Description;

        public String getCodeId() {
            return CodeId;
        }

        public void setCodeId(String CodeId) {
            this.CodeId = CodeId;
        }

        public String getDescription() {
            return Description;
        }

        public void setDescription(String Description) {
            this.Description = Description;
        }
    }

}
