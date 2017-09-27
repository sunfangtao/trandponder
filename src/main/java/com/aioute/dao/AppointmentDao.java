package com.aioute.dao;

import com.aioute.model.AppointmentModel;

import java.util.List;

public interface AppointmentDao {

    public boolean addAppointment(AppointmentModel appointmentModel);

    public boolean cancleAppointment(AppointmentModel appointmentModel);

    public List<AppointmentModel> queryList(AppointmentModel appointmentModel, int page, int pageSize);
}
