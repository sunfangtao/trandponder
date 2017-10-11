package com.aioute.service.impl;

import com.aioute.dao.AppointmentDao;
import com.aioute.model.AppointmentModel;
import com.aioute.service.AppointmentService;
import com.sft.util.DateUtil;
import com.aioute.util.SecurityUtil;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

@Repository
public class AppointmentServiceImpl implements AppointmentService {

    @Resource
    private AppointmentDao appointmentDao;

    public boolean addAppointment(AppointmentModel appointmentModel) {
        appointmentModel.setId(UUID.randomUUID().toString());
        appointmentModel.setUser_id(SecurityUtil.getUserId());
        appointmentModel.setCreateDate(DateUtil.getCurDate());
        appointmentModel.setUpdateDate(appointmentModel.getCreateDate());
        appointmentModel.setStatus("0");
        return appointmentDao.addAppointment(appointmentModel);
    }

    public boolean cancleAppointment(AppointmentModel appointmentModel) {
        appointmentModel.setUser_id(SecurityUtil.getUserId());
        appointmentModel.setUpdateDate(DateUtil.getCurDate());
        return appointmentDao.cancleAppointment(appointmentModel);
    }

    public List<AppointmentModel> queryList(AppointmentModel appointmentModel, int page, int pageSize) {
        appointmentModel.setUser_id(SecurityUtil.getUserId());
        return appointmentDao.queryList(appointmentModel, page, pageSize);
    }
}
