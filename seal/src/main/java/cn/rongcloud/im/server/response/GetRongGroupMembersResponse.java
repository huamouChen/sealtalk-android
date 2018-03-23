package cn.rongcloud.im.server.response;

import java.util.List;

import cn.rongcloud.im.db.Friend;
import cn.rongcloud.im.db.GroupMember;
import cn.rongcloud.im.db.Groups;

/**
 * Created by AMing on 15/12/24.
 * Company RongCloud
 */
public class GetRongGroupMembersResponse {


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
