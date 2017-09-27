package com.aioute.service;

import com.aioute.model.AppointmentModel;

import java.util.List;

public interface AppointmentService {

    public boolean addAppointment(AppointmentModel appointmentModel);

    public boolean cancleAppointment(AppointmentModel appointmentModel);

    public List<AppointmentModel> queryList(AppointmentModel appointmentModel, int page, int pageSize);
}
