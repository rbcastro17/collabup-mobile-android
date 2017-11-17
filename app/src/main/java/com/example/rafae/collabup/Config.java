package com.example.rafae.collabup;

/**
 * Created by Rafae on 13/04/2017.
 */

public class Config {

public static final String IP= "collabup.org";

public static final String IPaudit= "192.168.43.71:8000";//"collabup.org:81";

//public static final String audit_url = "http://"+IPaudit+"/mobile/admin/getaudit";

public static final String login_url = "https://"+IP+"/mobile/login";

 public static final String connect_url = "https://"+IP+"/testthis";

 public static final String register_url = "https://"+IP+"/mobile/register";
    public static final String fetch_url = "https://"+IP+"/mobile/getgroups/chat";
    public static final String sendok_url = "https://"+IP+"/mobile/setok";
public static final String activate_account_url = "https://"+IP+"/mobile/activate/account";
public static final String send_forget_code_url = "https://"+IP+"/mobile/sendforget";
public static final String change_password_url = "https://"+IP+"/mobile/changepassword";
public static final String change_password_in_url = "https://"+IP+"/mobile/changeinpassword";
public static final String add_post_url = "https://"+IP+"/mobile/addpost";
public static final String add_comment_url = "https://"+IP+"/mobile/addcomment";
public static final String get_activities_url = "https://"+IP+"/mobile/getgroupactivities";
public static final String get_activities_own_url = "https://"+IP+"/mobile/getactivities/own";
public static final String fetch_post_url = "https://"+IP+"/mobile/fetchpost";
public static final String fetch_comments_url = "https://"+IP+"/mobile/fetchcomment";
public static final String fetch_events_url = "https://"+IP+"/mobile/fetchevents";
public static final String fetch_folders_url = "https://"+IP+"/mobile/fetchfolders?group_id= ";


   //public static final String get_activities_url = "http://"+IP+"/mobile/getactivities";
    public static final String list_members_url = "https://"+IP+"/mobile/fetchmembers";

    public static final String url_image_upload = "https:"+IP+"mobile/upload/image";

    public static final String URL_GET_ALL_GROUP = "https://"+IP+"/mobile/getgroups";
    public static final String URL_GET_ALL_ANNOUNCEMENT = "https://"+IP+"/mobile/announcement";
    public static final String URL_GET_ALL_POST = "https://"+IP+"/";
    public static final String URL_DELETE_POST = "https://"+IP+"/mobile/deletepost";
    public static final String URL_UPDATE_POST = "https://"+IP+"/mobile/updatepost";
    public static final String URL_GET_POST = "https://"+IP+"/mobile/fetchownpost";
   public static final String fetchfile_url = "https://"+IP+"/mobile/fetchfiles";


 //ADMIN
 public static final String URL_ADDANNOUNCEMENT="https://"+IP+"/mobile/admin/addannouncement";
 public static final String URL_GET_ALLUSERS = "https://"+IP+"/mobile/admin/getusers";
 public static final String URL_GET_ALLANNOUNCEMENT = "https://"+IP+"/mobile/announcement/";
 public static final String URL_GET_ALLADMINS = "https://"+IP+"/mobile/admin/alladmins";
 public static final String URL_GET_USER = "https://"+IP+"/mobile/admin/getuser";
 public static final String URL_GET_ANNOUNCEMENT = "https://"+IP+"/mobile/announcement/";
 public static final String URL_GET_ANNOUNCEMENTONE = "https://"+IP+"/mobile/announcement/one";

 public static final String URL_UPDATE_ANNOUNCEMENT = "https://"+IP+"/mobile/admin/updateannouncement";
 public static final String URL_DELETE_ANNOUNCEMENT = "https://"+IP+"/mobile/admin/deleteannouncement";
 public static final String URL_Activate = "https://"+IP+"/mobile/admin/activateuser";
 public static final String URL_Deactivate = "https://"+IP+"/mobile/admin/deactivateuser";
 public static final String URL_fetchuserinfo = "https://"+IP+"/mobile/admin/fetchuserinfo";

 public static final String audit_url = "https://"+IP+"/mobile/admin/getaudit";
 public static final String getuserinfo_url = "https://"+IP+"/mobile/getuserinfo";




    public static final String KEY_ID = "id";
    public static final String KEY_FNAME = "first_name";
    public static final String KEY_LNAME = "last_name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_GROUPNAME = "group_name";
    public static final String KEY_CODE = "code";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_GROUPOWNER = "group_owner";
    public static final String KEY_ANNOUNCEMENT = "body";
    public static final String KEY_USERID = "user_id";
    public static final String KEY_BODY = "body";
    public static final String KEY_EMP_ID = "id";

    //JSON Tags
    public static final String TAG_JSON_ARRAY="result";
    public static final String TAG_ID = "id";
    public static final String TAG_NAME = "name";
    public static final String TAG_GROUPNAME = "group_name";
    public static final String TAG_CODE = "code";
    public static final String TAG_DESCRIPTION = "description";
    public static final String TAG_GROUPOWNER = "group_owner";
    public static final String TAG_ANNOUNCEMENT = "body";
    public static final String TAG_DATE = "created_at";
    public static final String TAG_USER = "user_id";
    public static final String TAG_FNAME = "first_name";
    public static final String TAG_MNAME = "middle_name";
    public static final String TAG_LNAME = "last_name";
    public static final String TAG_EMAIL = "email";
    public static final String TAG_BODY = "body";
    public static final String TAG_ACTIVE = "active";
    public static final String TAG_ROLE = "role";
    public static final String TAG_TITLE = "title";
    public static final String TAG_FOLDERNAME = "name";
    public static final String TAG_FOLDERDESC = "description";
    public static final String TAG_USERNAME = "username";

    public static final String ID = "id";

}
