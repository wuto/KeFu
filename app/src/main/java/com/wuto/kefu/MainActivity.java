package com.wuto.kefu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.alibaba.mobileim.IYWLoginService;
import com.alibaba.mobileim.YWAPI;
import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.YWLoginParam;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.conversation.EServiceContact;
import com.alibaba.mobileim.gingko.model.tribe.YWTribe;
import com.alibaba.mobileim.gingko.model.tribe.YWTribeType;
import com.alibaba.mobileim.tribe.YWTribeCreationParam;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    String userid = "liguiqiang02";
    String password = "123456";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cus_service);

        //此实现不一定要放在Application onCreate中
        //此对象获取到后，保存为全局对象，供APP使用
        //此对象跟用户相关，如果切换了用户，需要重新获取
        mIMKit = YWAPI.getIMKitInstance(userid,BaseApplication.APP_KEY);

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_1://登录
                loginand();
                break;
            case R.id.btn_2://回话列表
                Intent intent2 = mIMKit.getConversationActivityIntent();
                startActivity(intent2);
                break;
            case R.id.btn_3://单聊：普通聊天
                final String target = "liguiqiang01"; //消息接收者ID
                final String appkey = "23437714"; //消息接收者appKey
                Intent intent = mIMKit.getChattingActivityIntent(target, appkey);
                startActivity(intent);
                break;
            case R.id.btn_4://客服聊天

                //userid是客服帐号，第一个参数是客服帐号，第二个是组ID，如果没有，传0
                EServiceContact contact = new EServiceContact("吃藕小店", 0);
//如果需要发给指定的客服帐号，不需要Server进行分流(默认Server会分流)，请调用EServiceContact对象
//的setNeedByPass方法，参数为false。
//contact.setNeedByPass(false);
                Intent intent3 = mIMKit.getChattingActivityIntent(contact);
                startActivity(intent3);

                break;
            case R.id.btn_5://创建群
//IYWTribeService.createTribe，创建一个群，可以指定群名称和群公告，群名称必须不能为空，同时指定群成员列表，参数为List<String>，用户ID号。
//                创建成功后，会返回IYWTribe。
                YWTribeCreationParam tribeCreationParam = new YWTribeCreationParam();
//群名称
                tribeCreationParam.setTribeName("tribeName");
//群公告
                tribeCreationParam.setNotice("notice");
//群类型，普通群
                tribeCreationParam.setTribeType(YWTribeType.CHATTING_GROUP);
                final List<String> userList = new ArrayList<String>();

                userList.add("user2");//群成员
                tribeCreationParam.setUsers(userList);

                tribeService.createTribe(new MyCallback() {
                    @Override
                    public void onSuccess(Object... result) {
                        // 返回值为刚刚成功创建的群
                        YWTribe tribe = (YWTribe) result[0];
                        tribe.getTribeId();// 群ID，用于唯一标识一个群
                    }
                }, tribeCreationParam);

            default:
                break;
        }
    }


    YWIMKit mIMKit;

    private void loginand() {

        //开始登录
        IYWLoginService loginService = mIMKit.getLoginService();
        YWLoginParam loginParam = YWLoginParam.createLoginParam(userid, password);
        loginService.login(loginParam, new IWxCallback() {

            @Override
            public void onSuccess(Object... arg0) {


                System.out.println("成功");
            }

            @Override
            public void onProgress(int arg0) {
                // TODO Auto-generated method stub
                System.out.println("onProgress");
            }

            @Override
            public void onError(int errCode, String description) {
                System.out.println("失败");
                //如果登录失败，errCode为错误码,description是错误的具体描述信息
            }
        });

    }

}