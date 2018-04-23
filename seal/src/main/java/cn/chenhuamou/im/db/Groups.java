package cn.chenhuamou.im.db;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit.
/**
 * Entity mapped to table GROUPS.
 */
public class Groups extends UserInfoBean {
    private String displayName;
    private String role;
    private String bulletin;   // 公告
    private String timestamp;
    private String nameSpelling;

    // rong
//    private int Id;
    private String GroupId;
    private String GroupName;
    private String GroupOwner;
    private String AddTime;
    private String UserName;
    private String GroupImage;
    private boolean IsOnLine;
    private boolean IsOfficial;
    private String NickName;
    private String HeaderImage;
    private boolean CanBetting;





    public Groups() {
        super();
    }

    public Groups(String groupsId) {
        super(groupsId);
    }

    public Groups(String groupsId, String name, String portraitUri, String displayName, String role, String bulletin, String timestamp) {
        super(groupsId, name, portraitUri);
        this.displayName = displayName;
        this.role = role;
        this.bulletin = bulletin;
        this.timestamp = timestamp;
    }

    public Groups(String timestamp, String role, String displayName, String portraitUri, String name, String groupsId) {
        super(groupsId, name, portraitUri);
        this.timestamp = timestamp;
        this.role = role;
        this.displayName = displayName;
    }

    public Groups(String groupsId, String name, String portraitUri, String role) {
        super(groupsId, name, portraitUri);
        this.role = role;
    }

    public Groups(String groupsId, String name, String portraitUri) {
        super(groupsId, name, portraitUri);
    }

    public Groups(String groupsId, String name, String portraitUri, String displayName, String role, String bulletin, String timestamp, String nameSpelling) {
        super(groupsId, name, portraitUri);
        this.displayName = displayName;
        this.role = role;
        this.bulletin = bulletin;
        this.timestamp = timestamp;
        this.nameSpelling = nameSpelling;
    }
    /** Not-null value. */
    public String getGroupsId() {
        return getUserId();
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setGroupsId(String groupsId) {
        setUserId(groupsId);
    }


    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getBulletin() {
        return bulletin;
    }

    public void setBulletin(String bulletin) {
        this.bulletin = bulletin;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getNameSpelling() {
        return nameSpelling;
    }

    public void setNameSpelling(String nameSpelling) {
        this.nameSpelling = nameSpelling;
    }

//    public int getId() {
//        return Id;
//    }

//    public void setId(int Id) {
//        this.Id = Id;
//    }

    public String getGroupId() {
        return GroupId;
    }

    public void setGroupId(String GroupId) {
        this.GroupId = GroupId;
        super.setUserId(GroupId);
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String GroupName) {
        super.setName(GroupName);
        setDisplayName(GroupName);
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

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getGroupImage() {
        return GroupImage;
    }

    public void setGroupImage(String groupImage) {
        GroupImage = groupImage;
    }

    public boolean isOnLine() {
        return IsOnLine;
    }

    public void setOnLine(boolean onLine) {
        IsOnLine = onLine;
    }

    public boolean isOfficial() {
        return IsOfficial;
    }

    public void setOfficial(boolean official) {
        IsOfficial = official;
    }

    public String getNickName() {
        return NickName;
    }

    public void setNickName(String nickName) {
        NickName = nickName;
    }

    public String getHeaderImage() {
        return HeaderImage;
    }

    public void setHeaderImage(String headerImage) {
        HeaderImage = headerImage;
    }

    public boolean isCanBetting() {
        return CanBetting;
    }

    public void setCanBetting(boolean canBetting) {
        CanBetting = canBetting;
    }
}
