package com.aioute.controller;

import com.aioute.dao.UserDao;
import com.aioute.model.UserModel;
import com.aioute.reflect.ReflectUtil;
import com.aioute.util.CloudError;
import com.aioute.util.CloudParams.TypeEnum;
import com.aioute.util.FileDownUpload;
import com.aioute.util.SendAppJSONUtil;
import com.aioute.util.SendJSONUtil;
import com.aioute.vo.UserVO;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.util.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("user")
public class UserController {
    private Logger logger = Logger.getLogger(UserController.class);
    @Resource
    private UserDao userDao;

    /**
     * 更新用户的头像
     *
     * @param req
     * @param res
     */
//    @RequestMapping("/headPic")
//    public void updateUserHeadPic(MultipartFile file, HttpServletRequest req, HttpServletResponse res) {
//        // 保存文件
//        try {
//            String userId = req.getParameter("userId");
//
//            UserVO currentUser = null;
//            if (userId != null && userId.length() > 0) {
//                currentUser = userDao.obtainUserInfoById(userId);
//            } else {
//                currentUser = userDao.obtainUserInfoByPhone((String) SecurityUtils.getSubject().getPrincipal());
//            }
//
//            logger.info("更新用户：" + currentUser.getName() + "的头像");
//            String fileInfo = FileDownUpload.uploadPicFile(file, req);
//            String filePath = fileInfo.split(",")[0];
//            String fileName = fileInfo.split(",")[1];
//
//            String picUrl = qiNiuUtil.upload(currentUser.getHeadPicUrl(), fileName, filePath + File.separator
//                    + fileName);
//
//            JSONObject jsonObject = null;
//            if (picUrl != null) {
//                // 上传成功
//                currentUser.setHeadPicUrl(picUrl);
//                userDao.updateUser(currentUser);
//                // 更新IM系统用户的头像
//                imOperate.updateUser(currentUser);
//                // 删除本地文件
//                FileDownUpload.deleteFile(new File(filePath, fileName));
//
//                // 返回数据中密码改为******
//                currentUser.setPassword("******");
//                currentUser.setImPassword("******");
//                jsonObject = SendJSONUtil.getNormalObject(currentUser);
//            } else {
//                // 上传失败
//                jsonObject = SendJSONUtil.getFailResultObject(TypeEnum.IOException.getValue(), "上传失败");
//            }
//            logger.info(jsonObject.toString());
//            res.getWriter().write(jsonObject.toString());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * 更新用户的信息
     *
     * @param req
     * @param res
     */
    @RequestMapping("/update")
    public void updateUser(HttpServletRequest req, HttpServletResponse res) {
        try {

            String returnJson = null;
            UserModel user = new UserModel();

            String name = req.getParameter("name");
            String sex = req.getParameter("sex");
            String push_id = req.getParameter("push_id");
            String email = req.getParameter("email");
            String del_flag = req.getParameter("del_flag");
            String login_time = req.getParameter("login_time");
            String login_type = req.getParameter("login_type");
            String login_id = req.getParameter("login_id");

            if (StringUtils.hasText(name)) {
                user.setName(name);
            }
            if (StringUtils.hasText(sex)) {
                user.setSex(sex.equals("0") ? "男" : "女");
            }
            if (StringUtils.hasText(push_id)) {
                user.setPush_id(push_id);
            }
            if (StringUtils.hasText(email)) {
                user.setEmail(email);
            }
            if (StringUtils.hasText(del_flag)) {
                user.setDel_flag(del_flag.equals("0") ? 0 : 1);
            }
            if (StringUtils.hasText(login_time)) {
                user.setLogin_time(login_time);
            }
            if (StringUtils.hasText(login_type)) {
                user.setLogin_type(login_type);
            }
            if (StringUtils.hasText(login_id)) {
                user.setLogin_id(login_id);
            }

            if (userDao.updateUser(user)) {
                returnJson = SendAppJSONUtil.getNormalString("更新成功!");
            } else {
                returnJson = SendAppJSONUtil.getFailResultObject(CloudError.ReasonEnum.SQLEXCEPTION.getValue(), "更新失败!");
            }

            res.getWriter().write(returnJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加用户
     */
    @RequestMapping("/add")
    public void addUser(MultipartFile file, HttpServletRequest req, HttpServletResponse res) {
        try {
            UserVO user = new UserVO();

            ReflectUtil<UserVO> reflectUtil = new ReflectUtil<UserVO>(user);
            List<Method> setMethodList = ReflectUtil.obtainSetMethods(UserVO.class);
            for (Method method : setMethodList) {
                String methodName = method.getName();
                String tempName = methodName.substring(3);
                String pamam = req.getParameter(tempName.substring(0, 1).toLowerCase() + tempName.substring(1));
                reflectUtil.setter(method, pamam);
            }
            user.setUserId(UUID.randomUUID().toString());
            user.setLevel("3");

            UserVO loginUser = userDao.obtainUserInfoByPhone((String) SecurityUtils.getSubject().getPrincipal());

            JSONObject jsonObject = null;
            if (!loginUser.getLevel().equals("1")) {
                jsonObject = SendJSONUtil.getFailResultObject(TypeEnum.NODATA.getValue(), "无权新建账号");
            } else {
                if (user.getPhone() == null || user.getPhone().length() == 0) {
                    jsonObject = SendJSONUtil.getRequireParamsMissingObject("phone为必填字段");
                } else if (user.getPassword() == null || user.getPassword().length() == 0) {
                    jsonObject = SendJSONUtil.getRequireParamsMissingObject("password为必填字段");
                } else {
                    UserVO existUser = new UserVO();
                    existUser.setPhone(user.getPhone());
                    List<UserVO> userList = userDao.obtainUserList(existUser, 0, 0, -1);
                    if (userList != null && userList.size() > 0) {
                        jsonObject = SendJSONUtil.getFailResultObject(TypeEnum.DUPLICATEACCOUNT.getValue(), "手机号被占用");
                    } else {
                        if (file != null) {
                            String fileInfo = FileDownUpload.uploadPicFile(file, req);
                            String filePath = fileInfo.split(",")[0];
                            String fileName = fileInfo.split(",")[1];

                            String picUrl = qiNiuUtil.upload("", fileName, filePath + File.separator + fileName);
                            user.setHeadPicUrl(picUrl);
                        }
                        // 密码加密
                        user.setPassword(passwordHelper.encryptPassword(null, user.getPassword()));
                        // IM系统用户名和密码
                        user.setImUserName(passwordHelper.encryptIMPassword(user.getPhone()));
                        user.setImPassword(passwordHelper.encryptIMPassword(user.getPassword()));

                        userDao.addUser(user);

                        // IM系统中注册用户
                        imOperate.addUser(user);

                        jsonObject = SendJSONUtil.getNormalObject(new JSONObject());
                    }
                }
            }

            logger.info("用户：" + loginUser.getPhone() + "新建用户\n" + "\n" + jsonObject.toString());

            res.getWriter().write(jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 单个用户详情
     *
     * @param req
     * @param res
     */
    @RequestMapping("/detail")
    public void userDetail(HttpServletRequest req, HttpServletResponse res) {
        try {
            JSONObject jsonObject = null;

            String userId = req.getParameter("userId");

            if (userId == null || userId.length() == 0) {
                userId = userDao.obtainUserInfoByPhone((String) SecurityUtils.getSubject().getPrincipal()).getUserId();
            }

            UserVO user = userDao.obtainUserInfoById(userId);

            if (user == null) {
                jsonObject = SendJSONUtil.getNullResultObject();
            } else {
                user.setPassword("******");
                user.setImPassword("******");
                jsonObject = SendJSONUtil.getNormalObject(user);
            }
            res.getWriter().write(jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
