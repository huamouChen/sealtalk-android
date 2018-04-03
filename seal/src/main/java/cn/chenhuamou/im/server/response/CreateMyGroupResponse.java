package cn.chenhuamou.im.server.response;

/**
 * Created by AMing on 16/1/18.
 * Company RongCloud
 */
public class CreateMyGroupResponse {




    private ValueEntity Value;

    private CodeEntity Code;

    public CodeEntity getCode() {
        return Code;
    }

    public void setCode(CodeEntity code) {
        Code = code;
    }

    public ValueEntity getValue() {
        return Value;
    }

    public void setValue(ValueEntity value) {
        Value = value;
    }

    public class ValueEntity {

        /**
         * GroupId : 2839
         * GroupName : group2
         */

        private int GroupId;
        private String GroupName;

        public int getGroupId() {
            return GroupId;
        }

        public void setGroupId(int GroupId) {
            this.GroupId = GroupId;
        }

        public String getGroupName() {
            return GroupName;
        }

        public void setGroupName(String GroupName) {
            this.GroupName = GroupName;
        }
    }

    public class CodeEntity {

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
