package cn.chenhuamou.im.server.response;

import java.util.List;

/**
 * Created by AMing on 15/12/24.
 * Company RongCloud
 */
public class GetRongGroupInfoResponse {

    public GetRongGroupInfoResponse(CodeEntity code, List<ValueEntity> value) {
        this.Code = code;
        this.Value = value;
    }

    public GetRongGroupInfoResponse() {

    }

    private CodeEntity Code;

    private List<ValueEntity> Value;


    public CodeEntity getCode() {
        return Code;
    }

    public void setCode(CodeEntity code) {
        Code = code;
    }

    public List<ValueEntity> getValue() {
        return Value;
    }

    public void setValue(List<ValueEntity> value) {
        Value = value;
    }

    public class ValueEntity {

        public ValueEntity(String groupId, String groupName, String groupOwner, boolean isOfficial, String addTime) {
            GroupId = groupId;
            GroupName = groupName;
            GroupOwner = groupOwner;
            IsOfficial = isOfficial;
            AddTime = addTime;
        }

        /**
         * GroupId : string
         * GroupName : string
         * GroupOwner : string
         * IsOfficial : true
         * AddTime : 2018-04-04T02:54:04.108Z
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


    public static class CodeEntity {
        public CodeEntity(String codeId, String description) {
            CodeId = codeId;
            Description = description;
        }

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
