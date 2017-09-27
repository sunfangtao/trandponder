package com.aioute.controller;

import com.aioute.model.AppointmentModel;
import com.aioute.service.AppointmentService;
import com.aioute.util.CloudError;
import com.aioute.util.PagingUtil;
import com.aioute.util.SendAppJSONUtil;
import org.apache.log4j.Logger;
import org.apache.shiro.util.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("appointment")
public class AppointmentController {

    private Logger logger = Logger.getLogger(AppointmentController.class);

    @Resource
    private AppointmentService appointmentService;

    /**
     * 添加预约
     *
     * @param res
     */
    @RequestMapping("add")
    public void add(AppointmentModel appointmentModel, HttpServletResponse res) {
        try {
            String returnJson = null;
            if (appointmentService.addAppointment(appointmentModel)) {
                returnJson = SendAppJSONUtil.getNormalString("添加成功!");
            } else {
                returnJson = SendAppJSONUtil.getFailResultObject(CloudError.ReasonEnum.SQLEXCEPTION.getValue(), "添加失败!");
            }
            res.getWriter().write(returnJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 取消预约
     *
     * @param req
     * @param res
     */
    @RequestMapping("cancel")
    public void cancel(HttpServletRequest req, HttpServletResponse res) {
        try {
            String resultJson = null;
            String id = req.getParameter("id");
            if (StringUtils.hasText(id)) {
                AppointmentModel appointmentModel = new AppointmentModel();
                appointmentModel.setId(id);
                if (appointmentService.cancleAppointment(appointmentModel)) {
                    resultJson = SendAppJSONUtil.getNormalString("取消成功!");
                } else {
                    resultJson = SendAppJSONUtil.getFailResultObject(CloudError.ReasonEnum.SQLEXCEPTION.getValue(), "取消失败!");
                }
            } else {
                resultJson = SendAppJSONUtil.getFailResultObject(CloudError.ReasonEnum.NODATA.getValue(), "请上传ID!");
            }
            logger.info(resultJson);
            res.getWriter().write(resultJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询预约
     *
     * @param req
     * @param res
     */
    @RequestMapping("query")
    public void query(HttpServletRequest req, HttpServletResponse res) {
        try {
            String returnResult = null;
            int page = PagingUtil.getPage(req);
            int pageSize = PagingUtil.getPageSize(req);
            String type = req.getParameter("type");
            String status = req.getParameter("status");

            AppointmentModel appointmentModel = new AppointmentModel();
            appointmentModel.setType(type);
            appointmentModel.setStatus(status);
            List<AppointmentModel> appointmentList = appointmentService.queryList(appointmentModel, page, pageSize);
            if (appointmentList == null || appointmentList.size() == 0) {
                returnResult = SendAppJSONUtil.getNullResultObject();
            } else {
                returnResult = SendAppJSONUtil.getPageJsonString(page, pageSize, 0, appointmentList);
            }
            res.getWriter().write(returnResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
