package com.example.docktooth.features.appointments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.docktooth.data.repository.AppointmentRepositoryImpl
import com.example.docktooth.data.repository.AuthRepositoryImpl
import com.example.docktooth.domain.model.Appointment
import com.example.docktooth.domain.model.AppointmentStatus
import com.example.docktooth.domain.model.User
import com.example.docktooth.domain.model.UserRole
import com.example.docktooth.domain.repository.AppointmentRepository
import com.example.docktooth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.*
import java.time.YearMonth

class AgendaViewModel(
    private val appointmentRepository: AppointmentRepository = AppointmentRepositoryImpl(),
    private val authRepository: AuthRepository = AuthRepositoryImpl()
) : ViewModel() {

    private val _selectedDay = MutableStateFlow(24)
    private val _selectedMonth = MutableStateFlow(4) // 4 = Abril
    private val _selectedYear = MutableStateFlow(2025)

    private val _searchQuery = MutableStateFlow("")
    private val _showDialog = MutableStateFlow(false)
    private val _newPatient = MutableStateFlow("")
    private val _newSpecialty = MutableStateFlow("Odontología general")
    private val _newTime = MutableStateFlow("09:00 a.m.")

    private val _newDateDay = MutableStateFlow(24)
    private val _newDateMonth = MutableStateFlow(4)
    private val _newDateYear = MutableStateFlow(2025)

    private val _newDoctorName = MutableStateFlow("Dr. Juan Sebastián Gonzalez")

    private fun getDaysInMonth(month: Int, year: Int): Int {
        return try {
            YearMonth.of(year, month).lengthOfMonth()
        } catch (_: Exception) {
            30
        }
    }

    private data class MainAgendaParams(
        val appts: List<Appointment>,
        val userRole: UserRole,
        val userFullName: String,
        val day: Int,
        val month: Int,
        val year: Int,
        val query: String,
        val showDlg: Boolean
    )

    private data class NewDateParams(
        val day: Int,
        val month: Int,
        val year: Int,
        val patient: String,
        val specialty: String,
        val time: String,
        val doctor: String
    )

    @Suppress("UNCHECKED_CAST")
    val uiState: StateFlow<AgendaState> = combine(
        combine(
            appointmentRepository.appointments,
            authRepository.currentUser,
            _selectedDay,
            _selectedMonth,
            _selectedYear,
            _searchQuery,
            _showDialog
        ) { array ->
            val appts = array[0] as List<Appointment>
            val user = array[1] as User
            val day = array[2] as Int
            val month = array[3] as Int
            val year = array[4] as Int
            val query = array[5] as String
            val showDlg = array[6] as Boolean
            MainAgendaParams(appts, user.role, user.fullName, day, month, year, query, showDlg)
        },
        combine(
            _newDateDay,
            _newDateMonth,
            _newDateYear,
            _newPatient,
            _newSpecialty,
            _newTime,
            _newDoctorName
        ) { array ->
            val day = array[0] as Int
            val month = array[1] as Int
            val year = array[2] as Int
            val patient = array[3] as String
            val specialty = array[4] as String
            val time = array[5] as String
            val doctor = array[6] as String
            NewDateParams(day, month, year, patient, specialty, time, doctor)
        }
    ) { mainParams, newParams ->
        val targetDayPadded = mainParams.day.toString().padStart(2, '0')
        val targetMonthPadded = mainParams.month.toString().padStart(2, '0')

        val patternPadded = "$targetDayPadded/$targetMonthPadded/${mainParams.year}"
        val patternShort = "${mainParams.day}/${mainParams.month}/${mainParams.year}"
        val patternDayShort = "${mainParams.day}/$targetMonthPadded/${mainParams.year}"
        val patternMonthShort = "$targetDayPadded/${mainParams.month}/${mainParams.year}"

        val filteredByDate = mainParams.appts.filter { appt ->
            appt.date == patternPadded || appt.date == patternShort || appt.date == patternDayShort || appt.date == patternMonthShort
        }

        val filtered = filteredByDate.filter {
            mainParams.query.isBlank() ||
                    it.patientName.contains(mainParams.query, ignoreCase = true) ||
                    it.specialty.contains(mainParams.query, ignoreCase = true) ||
                    it.doctorName.contains(mainParams.query, ignoreCase = true)
        }

        AgendaState(
            selectedDay = mainParams.day,
            selectedMonth = mainParams.month,
            selectedYear = mainParams.year,
            searchQuery = mainParams.query,
            appointments = filtered,
            showAddDialog = mainParams.showDlg,
            newPatientName = if (newParams.patient.isBlank() && mainParams.userRole == UserRole.PACIENTE) mainParams.userFullName else newParams.patient,
            newSpecialty = newParams.specialty,
            newTime = newParams.time,
            newDateDay = newParams.day,
            newDateMonth = newParams.month,
            newDateYear = newParams.year,
            newDoctorName = newParams.doctor,
            currentUserRole = mainParams.userRole,
            currentUserName = mainParams.userFullName
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AgendaState()
    )

    fun onDateSelected(day: Int) {
        _selectedDay.value = day
        _newDateDay.value = day
    }

    fun onMonthSelected(month: Int) {
        _selectedMonth.value = month
        _newDateMonth.value = month
        val maxDays = getDaysInMonth(month, _selectedYear.value)
        if (_selectedDay.value > maxDays) _selectedDay.value = maxDays
        if (_newDateDay.value > maxDays) _newDateDay.value = maxDays
    }

    fun onYearSelected(year: Int) {
        _selectedYear.value = year
        _newDateYear.value = year
        val maxDays = getDaysInMonth(_selectedMonth.value, year)
        if (_selectedDay.value > maxDays) _selectedDay.value = maxDays
        if (_newDateDay.value > maxDays) _newDateDay.value = maxDays
    }

    fun onPrevMonth() {
        if (_selectedMonth.value == 1) {
            _selectedMonth.value = 12
            _selectedYear.value -= 1
        } else {
            _selectedMonth.value -= 1
        }
        _newDateMonth.value = _selectedMonth.value
        _newDateYear.value = _selectedYear.value

        val maxDays = getDaysInMonth(_selectedMonth.value, _selectedYear.value)
        if (_selectedDay.value > maxDays) _selectedDay.value = maxDays
        _newDateDay.value = _selectedDay.value
    }

    fun onNextMonth() {
        if (_selectedMonth.value == 12) {
            _selectedMonth.value = 1
            _selectedYear.value += 1
        } else {
            _selectedMonth.value += 1
        }
        _newDateMonth.value = _selectedMonth.value
        _newDateYear.value = _selectedYear.value

        val maxDays = getDaysInMonth(_selectedMonth.value, _selectedYear.value)
        if (_selectedDay.value > maxDays) _selectedDay.value = maxDays
        _newDateDay.value = _selectedDay.value
    }

    fun onSearchQueryChanged(query: String) { _searchQuery.value = query }

    fun onShowAddDialog(show: Boolean) {
        if (show) {
            _newDateDay.value = _selectedDay.value
            _newDateMonth.value = _selectedMonth.value
            _newDateYear.value = _selectedYear.value
            val user = authRepository.currentUser.value
            if (user.role == UserRole.PACIENTE) {
                _newPatient.value = user.fullName
            }
        }
        _showDialog.value = show
    }

    fun onNewPatientChanged(name: String) { _newPatient.value = name }
    fun onNewSpecialtyChanged(specialty: String) { _newSpecialty.value = specialty }
    fun onNewTimeChanged(time: String) { _newTime.value = time }

    fun onNewDateDayChanged(day: Int) { _newDateDay.value = day }

    fun onNewDateMonthChanged(month: Int) {
        _newDateMonth.value = month
        val maxDays = getDaysInMonth(month, _newDateYear.value)
        if (_newDateDay.value > maxDays) {
            _newDateDay.value = maxDays
        }
    }

    fun onNewDateYearChanged(year: Int) {
        _newDateYear.value = year
        val maxDays = getDaysInMonth(_newDateMonth.value, year)
        if (_newDateDay.value > maxDays) {
            _newDateDay.value = maxDays
        }
    }

    fun onNewDoctorNameChanged(doctor: String) { _newDoctorName.value = doctor }

    fun createAppointment() {
        val patientName = _newPatient.value.ifBlank { authRepository.currentUser.value.fullName }

        val dayPadded = _newDateDay.value.toString().padStart(2, '0')
        val monthPadded = _newDateMonth.value.toString().padStart(2, '0')
        val formattedDate = "$dayPadded/$monthPadded/${_newDateYear.value}"

        val newAppt = Appointment(
            id = System.currentTimeMillis().toString(),
            time = _newTime.value,
            patientName = patientName,
            specialty = _newSpecialty.value,
            date = formattedDate,
            status = AppointmentStatus.CONFIRMADA,
            doctorName = _newDoctorName.value
        )
        appointmentRepository.addAppointment(newAppt)

        // Switch active agenda view to the chosen day, month and year
        _selectedDay.value = _newDateDay.value
        _selectedMonth.value = _newDateMonth.value
        _selectedYear.value = _newDateYear.value

        _showDialog.value = false
    }

    fun toggleAppointmentStatus(id: String, currentStatus: AppointmentStatus) {
        val nextStatus = if (currentStatus == AppointmentStatus.CONFIRMADA) "pendiente" else "confirmada"
        appointmentRepository.updateAppointmentStatus(id, nextStatus)
    }
}
