package com.example.rafae.collabup;

/**
 * Created by Rafae on 13/04/2017.
 */

public class Config {

public static final String IP= "collabup.org";

public static final String IPaudit= "192.168.43.71:8000";//"collabup.org:81";

//public static final String audit_url = "http://"+IPaudit+"/mobile/admin/getaudit";

public static final String login_url = "http://"+IP+"/mobile/login";

 public static final String connect_url = "http://"+IP+"/testthis";

 public static final String register_url = "http://"+IP+"/mobile/register";
    public static final String fetch_url = "http://"+IP+"mobile/getgroups/chat";
    public static final String sendok_url = "http://"+IP+"mobile/setok";
public static final String activate_account_url = "http://"+IP+"/mobile/activate/account";
public static final String send_forget_code_url = "http://"+IP+"/mobile/sendforget";
public static final String change_password_url = "http://"+IP+"/mobile/changepassword";
public static final String change_password_in_url = "http://"+IP+"/mobile/changeinpassword";
public static final String add_post_url = "http://"+IP+"/mobile/addpost";
public static final String add_comment_url = "http://"+IP+"/mobile/addcomment";
public static final String get_activities_url = "http://"+IP+"/mobile/getgroupactivities";
public static final String get_activities_own_url = "http://"+IP+"/mobile/getactivities/own";
public static final String fetch_post_url = "http://"+IP+"/mobile/fetchpost";
public static final String fetch_comments_url = "http://"+IP+"/mobile/fetchcomment";
public static final String fetch_events_url = "http://"+IP+"/mobile/fetchevents";
public static final String fetch_folders_url = "http://"+IP+"/mobile/fetchfoldersgroup_id= ";


   //public static final String get_activities_url = "http://"+IP+"/mobile/getactivities";
    public static final String list_members_url = "http://"+IP+"/mobile/fetchmembers";

    public static final String url_image_upload = "http:"+IP+"mobile/upload/image";

    public static final String URL_GET_ALL_GROUP = "http://"+IP+"/mobile/getgroups";
    public static final String URL_GET_ALL_ANNOUNCEMENT = "http://"+IP+"/mobile/announcement";
    public static final String URL_GET_ALL_POST = "http://"+IP+"/";
    public static final String URL_DELETE_POST = "http://"+IP+"/mobile/deletepost";
    public static final String URL_UPDATE_POST = "http://"+IP+"/mobile/updatepost";
    public static final String URL_GET_POST = "http://"+IP+"/mobile/fetchownpost";
   public static final String fetchfile_url = "http://"+IP+"/mobile/fetchfiles";


 //ADMIN
 public static final String URL_ADDANNOUNCEMENT="http://"+IP+"/mobile/admin/addannouncement";
 public static final String URL_GET_ALLUSERS = "http://"+IP+"/mobile/admin/getusers";
 public static final String URL_GET_ALLANNOUNCEMENT = "http://"+IP+"/mobile/announcement/";
 public static final String URL_GET_ALLADMINS = "http://"+IP+"/mobile/admin/alladmins";
 public static final String URL_GET_USER = "http://"+IP+"/mobile/admin/getuser";
 public static final String URL_GET_ANNOUNCEMENT = "http://"+IP+"/mobile/announcement/";
 public static final String URL_GET_ANNOUNCEMENTONE = "http://"+IP+"/mobile/announcement/one";

 public static final String URL_UPDATE_ANNOUNCEMENT = "http://"+IP+"/mobile/admin/updateannouncement";
 public static final String URL_DELETE_ANNOUNCEMENT = "http://"+IP+"/mobile/admin/deleteannouncement";
 public static final String URL_Activate = "http://"+IP+"/mobile/admin/activateuser";
 public static final String URL_Deactivate = "http://"+IP+"/mobile/admin/deactivateuser";
 public static final String URL_fetchuserinfo = "http://"+IP+"/mobile/admin/fetchuserinfo";

 public static final String audit_url = "http://"+IP+"/mobile/admin/getaudit";




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
