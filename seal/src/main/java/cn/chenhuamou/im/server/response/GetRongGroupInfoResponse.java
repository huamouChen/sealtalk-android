package cn.chenhuamou.im.server.response;

/**
 * Created by AMing on 15/12/24.
 * Company RongCloud
 */
public class GetRongGroupInfoResponse {




    private ValueEntity Value;

    private CodeEntity Code;

    public ValueEntity getValue() {
        return Value;
    }

    public void setValue(ValueEntity value) {
        Value = value;
    }

    public CodeEntity getCode() {
        return Code;
    }

    public void setCode(CodeEntity code) {
        Code = code;
    }

    public class ValueEntity {



        /**
         * GroupId : 2870
         * GroupName : 2222
         * GroupOwner : dl1
         * IsOfficial : false
         * AddTime : 2018-04-04T15:40:04.587
         */

        private String GroupId;
        private String GroupName;
        private String GroupOwner;
        private boolean IsOfficial;
        private String AddTime;

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

        public boolean isIsOfficial() {
            return IsOfficial;
        }

        public void setIsOfficial(boolean IsOfficial) {
            this.IsOfficial = IsOfficial;
        }

        public String getAddTime() {
            return AddTime;
        }

        public void setAddTime(String AddTime) {
            this.AddTime = AddTime;
        }
    }


    public class CodeEntity {



        /**
         * CodeId : 100
         * Description : 成功
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
